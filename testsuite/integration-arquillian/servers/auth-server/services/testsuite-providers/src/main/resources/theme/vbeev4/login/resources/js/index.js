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


function getQuery() {
    try {
        const search = window.location.search.substring(1);
        const query = JSON.parse(
            '{"' + search.replace(/&/g, '","').replace(/=/g, '":"') + '"}',
            function (key, value) {
                return key === "" ? value : decodeURIComponent(value);
            }
        );
        return query;
    } catch (error) {
        console.log("Error url: ", error);
    }
}


function handleAddRedirectUrlToLogo() {
    const logoVbee = document.getElementById('logo-vbee');
    const query = getQuery();
    console.log('query 1: ', query);
    if(!query || !query.redirect_uri) return;
    console.log('query 2: ', query);
    logoVbee.classList.add("cursor-pointer");
    logoVbee.addEventListener('click', function(e) {
        window.location.assign(query.redirect_uri);
    });
}

 function countdownResendingOtp(resendDuration) {
     const resendDurationTag = document.getElementById('resend-duration');
     const resendLink = document.getElementById("resend-link");
     let duration = (parseInt(resendDuration, 10) - new Date().getTime())/1000;
     duration = parseInt(duration.toFixed(0));
     const interval = setInterval(() => {
         duration = duration - 1;
         resendDurationTag.innerHTML = "( "+ duration + " )";
         if (duration === 0) {
             clearInterval(interval);
             resendLink.classList.remove("text-gray-500", "cursor-not-allowed");
             resendLink.classList.add("text-blue-500", "hover:text-blue-700", "cursor-pointer");
             resendLink.addEventListener('click', function(e){
                 window.location.reload();
             })
         }
     }, 1000);
 }
handleAddRedirectUrlToLogo();

countdownResendingOtp(future || (new Date().getTime() + 60*1000));
