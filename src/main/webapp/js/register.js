// JS cho trang đăng ký
// Validate mật khẩu và xác nhận mật khẩu

document.addEventListener('DOMContentLoaded', function() {
    var form = document.querySelector('.register-form');
    if (!form) return;
    form.addEventListener('submit', function(e) {
        var password = form.querySelector('input[name="password"]');
        var confirm = form.querySelector('input[name="confirmPassword"]');
        var valid = true;
        // Reset lỗi
        password.classList.remove('is-invalid');
        confirm.classList.remove('is-invalid');
        // Kiểm tra độ mạnh mật khẩu
        var pwVal = password.value;
        if (pwVal.length < 6 || !/[A-Za-z]/.test(pwVal) || !/[0-9]/.test(pwVal)) {
            password.classList.add('is-invalid');
            valid = false;
        }
        // Kiểm tra trùng khớp
        if (password.value !== confirm.value) {
            confirm.classList.add('is-invalid');
            valid = false;
        }
        if (!valid) {
            e.preventDefault();
        }
    });
}); 