document.addEventListener('DOMContentLoaded', function () {
    const provinceSelect = document.getElementById('province');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');
    const addressInput = document.getElementById('address');
    const form = addressInput.closest('form');

    function updateAddress() {
        const provinceName = provinceSelect.selectedOptions[0]?.getAttribute('data-name') || '';
        const districtName = districtSelect.selectedOptions[0]?.getAttribute('data-name') || '';
        const wardName = wardSelect.selectedOptions[0]?.getAttribute('data-name') || '';
        let address = [wardName, districtName, provinceName].filter(Boolean).join(', ');
        addressInput.value = address;
    }

    // Load tỉnh/thành phố
    fetch('https://provinces.open-api.vn/api/p/')
        .then(res => res.json())
        .then(provinces => {
            provinceSelect.innerHTML = '<option value="">Chọn tỉnh/thành phố</option>';
            provinces.forEach(p => {
                provinceSelect.innerHTML += `<option value="${p.code}" data-name="${p.name}">${p.name}</option>`;
            });
            provinceSelect.disabled = false;
        });

    provinceSelect.addEventListener('change', function () {
        const provinceCode = this.value;
        districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';
        wardSelect.innerHTML = '<option value="">Chọn phường/xã</option>';
        districtSelect.disabled = true;
        wardSelect.disabled = true;
        updateAddress();
        if (!provinceCode) return;
        fetch(`https://provinces.open-api.vn/api/p/${provinceCode}?depth=2`)
            .then(res => res.json())
            .then(data => {
                (data.districts || []).forEach(d => {
                    districtSelect.innerHTML += `<option value="${d.code}" data-name="${d.name}">${d.name}</option>`;
                });
                districtSelect.disabled = false;
            });
    });

    districtSelect.addEventListener('change', function () {
        const districtCode = this.value;
        wardSelect.innerHTML = '<option value="">Chọn phường/xã</option>';
        wardSelect.disabled = true;
        updateAddress();
        if (!districtCode) return;
        fetch(`https://provinces.open-api.vn/api/d/${districtCode}?depth=2`)
            .then(res => res.json())
            .then(data => {
                (data.wards || []).forEach(w => {
                    wardSelect.innerHTML += `<option value="${w.code}" data-name="${w.name}">${w.name}</option>`;
                });
                wardSelect.disabled = false;
            });
    });

    wardSelect.addEventListener('change', updateAddress);

    // Đảm bảo luôn cập nhật địa chỉ trước khi submit
    if (form) {
        form.addEventListener('submit', function () {
            updateAddress();
        });
    }

    // Tự động chọn lại địa chỉ cũ nếu có
    const provinceSelected = provinceSelect.getAttribute('data-selected');
    const districtSelected = districtSelect.getAttribute('data-selected');
    const wardSelected = wardSelect.getAttribute('data-selected');

    if (provinceSelected) {
        fetch('https://provinces.open-api.vn/api/p/')
            .then(res => res.json())
            .then(provinces => {
                provinceSelect.innerHTML = '<option value="">Chọn tỉnh/thành phố</option>';
                let selectedProvinceCode = "";
                provinces.forEach(p => {
                    const selected = (p.name === provinceSelected) ? 'selected' : '';
                    if (selected) selectedProvinceCode = p.code;
                    provinceSelect.innerHTML += `<option value="${p.code}" data-name="${p.name}" ${selected}>${p.name}</option>`;
                });
                provinceSelect.disabled = false;
                if (selectedProvinceCode && districtSelected) {
                    fetch(`https://provinces.open-api.vn/api/p/${selectedProvinceCode}?depth=2`)
                        .then(res => res.json())
                        .then(data => {
                            districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';
                            let selectedDistrictCode = "";
                            (data.districts || []).forEach(d => {
                                const selected = (d.name === districtSelected) ? 'selected' : '';
                                if (selected) selectedDistrictCode = d.code;
                                districtSelect.innerHTML += `<option value="${d.code}" data-name="${d.name}" ${selected}>${d.name}</option>`;
                            });
                            districtSelect.disabled = false;
                            if (selectedDistrictCode && wardSelected) {
                                fetch(`https://provinces.open-api.vn/api/d/${selectedDistrictCode}?depth=2`)
                                    .then(res => res.json())
                                    .then(data => {
                                        wardSelect.innerHTML = '<option value="">Chọn phường/xã</option>';
                                        (data.wards || []).forEach(w => {
                                            const selected = (w.name === wardSelected) ? 'selected' : '';
                                            wardSelect.innerHTML += `<option value="${w.code}" data-name="${w.name}" ${selected}>${w.name}</option>`;
                                        });
                                        wardSelect.disabled = false;
                                    });
                            }
                        });
                }
            });
    }
});


