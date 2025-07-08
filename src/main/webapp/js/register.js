console.log("register.js thực sự đã load vào trang");
document.addEventListener('DOMContentLoaded', function() {
    console.log("register.js loaded");

    var form = document.querySelector('.register-form');
    if (!form) return;

    // Validate mật khẩu realtime
    var password = form.querySelector('input[name="password"]');
    var confirm = form.querySelector('input[name="confirmPassword"]');
    var passwordError = document.getElementById('password-error');
    password.addEventListener('input', function () {
        var pwVal = password.value;
        var valid = pwVal.length >= 6 && /[A-Za-z]/.test(pwVal) && /[0-9]/.test(pwVal);
        if (!valid && pwVal.length > 0) {
            password.classList.add('is-invalid');
            if (passwordError) passwordError.style.display = 'block';
        } else {
            password.classList.remove('is-invalid');
            if (passwordError) passwordError.style.display = 'none';
        }
    });

    form.addEventListener('submit', function(e) {
        var valid = true;
        password.classList.remove('is-invalid');
        confirm.classList.remove('is-invalid');
        if (passwordError) passwordError.style.display = 'none';

        var pwVal = password.value;
        if (pwVal.length < 6 || !/[A-Za-z]/.test(pwVal) || !/[0-9]/.test(pwVal)) {
            password.classList.add('is-invalid');
            if (passwordError) passwordError.style.display = 'block';
            valid = false;
        }
        if (password.value !== confirm.value) {
            confirm.classList.add('is-invalid');
            valid = false;
        }
        if (!valid) {
            e.preventDefault();
        }
    });

    // Lấy tỉnh/thành phố, quận/huyện, phường/xã
    const provinceSelect = document.getElementById('province');
    const districtSelect = document.getElementById('district');
    const wardSelect = document.getElementById('ward');
    const addressInput = document.getElementById('address');

    // Lấy danh sách tỉnh/thành phố
    fetch('https://provinces.open-api.vn/api/p/')
        .then(res => res.json())
        .then(data => {
            data.forEach(province => {
                const option = document.createElement('option');
                option.value = province.code;
                option.text = province.name;
                provinceSelect.appendChild(option);
            });
        });

    // Khi chọn tỉnh, lấy quận/huyện
    provinceSelect.addEventListener('change', function () {
        districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';
        wardSelect.innerHTML = '<option value="">Chọn phường/xã</option>';
        districtSelect.disabled = true;
        wardSelect.disabled = true;
        if (!this.value) return;
        fetch(`https://provinces.open-api.vn/api/p/${this.value}?depth=2`)
            .then(res => res.json())
            .then(data => {
                data.districts.forEach(district => {
                    const option = document.createElement('option');
                    option.value = district.code;
                    option.text = district.name;
                    districtSelect.appendChild(option);
                });
                districtSelect.disabled = false;
            });
    });

    // Khi chọn quận/huyện, lấy phường/xã
    districtSelect.addEventListener('change', function () {
        wardSelect.innerHTML = '<option value="">Chọn phường/xã</option>';
        wardSelect.disabled = true;
        if (!this.value) return;
        fetch(`https://provinces.open-api.vn/api/d/${this.value}?depth=2`)
            .then(res => res.json())
            .then(data => {
                data.wards.forEach(ward => {
                    const option = document.createElement('option');
                    option.value = ward.code;
                    option.text = ward.name;
                    wardSelect.appendChild(option);
                });
                wardSelect.disabled = false;
            });
    });

    // Khi submit form, gộp địa chỉ lại
    form.addEventListener('submit', function (e) {
        const provinceText = provinceSelect.options[provinceSelect.selectedIndex].text;
        const districtText = districtSelect.options[districtSelect.selectedIndex].text;
        const wardText = wardSelect.options[wardSelect.selectedIndex].text;
        addressInput.value = `${wardText}, ${districtText}, ${provinceText}`;
    });
});