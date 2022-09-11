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

var menuButton = document.getElementById('menu-button');
var dropdown = document.getElementById('target-international-menu');
var avatarButton = document.getElementById('avatar-button');
var avatarDropdown = document.getElementById('target-avatar-menu');

menuButton.addEventListener('click', function(e) {
    e.stopPropagation();
    if (dropdown.classList.contains('hidden'))
        dropdown.classList.remove('hidden');
    else dropdown.classList.add('hidden');
});

avatarButton.addEventListener('click', function(e) {
    e.stopPropagation();
    if (avatarDropdown.classList.contains('hidden'))
        avatarDropdown.classList.remove('hidden');
    else avatarDropdown.classList.add('hidden');
});

window.addEventListener('click', function() {
    if (!avatarDropdown.classList.contains('hidden'))
        avatarDropdown.classList.add('hidden');
    if (!dropdown.classList.contains('hidden'))
        dropdown.classList.add('hidden');
});
