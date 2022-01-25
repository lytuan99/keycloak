const togglePassword = document.querySelector('#togglePassword');
const password = document.querySelector('#password');
const toggleConfirmPassword = document.querySelector('#toggleConfirmPassword')
const confirmPassword = document.querySelector('#passwordConfirm')
const toggleNewPassword = document.querySelector('#toggleNewPassword')
const newPassword = document.querySelector('#passwordNew')

if(togglePassword){
    togglePassword.addEventListener('click', function (e) {
        const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
        password.setAttribute('type', type);
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });
}
if(toggleConfirmPassword){
    toggleConfirmPassword.addEventListener('click', function (e) {
        const type = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
        confirmPassword.setAttribute('type', type);
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });
}

if(toggleNewPassword) {
    toggleNewPassword.addEventListener('click', function (e) {
        const type = newPassword.getAttribute('type') === 'password' ? 'text' : 'password';
        newPassword.setAttribute('type', type);
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });
}
