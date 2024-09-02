document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById('form');
    const btnJoin = document.getElementById('btnJoin');
    const btnCheckEmail = document.getElementById('btnCheckEmail');
    const btnVerifyCode = document.getElementById('btnVerifyCode');
    const btnCheckId = document.getElementById('btnCheckId');

    // Handle form submission
    btnJoin.addEventListener('click', function() {
        if (!validateForm()) {
            return; // Exit if validation fails
        }

        // Collect form data and convert to JSON
        const formData = new FormData(form);
        const jsonData = {};
        formData.forEach((value, key) => { jsonData[key] = value; });

        fetch(form.action, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // Set content type to JSON
            },
            body: JSON.stringify(jsonData) // Convert JSON object to string
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("회원가입이 완료되었습니다.");
                    window.location.href = "/login";
                } else {
                    alert("오류가 발생했습니다: " + data.message);
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("회원가입 중 오류가 발생했습니다.");
            });
    });

    // Validate the form fields
    function validateForm() {
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const passwordCheck = document.getElementById('passwordCheck').value;
        const username = document.getElementById('username').value;
        const phone = document.getElementById('phone').value;
        const zonecode = document.getElementById('zonecode').value;
        const address = document.getElementById('address').value;
        const addressDetail = document.getElementById('addressDetail').value;

        console.log("Validating form: ", { email, password, passwordCheck, username, phone, zonecode, address, addressDetail });

        // Email validation
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email || !emailPattern.test(email)) {
            alert("이메일 형식이 올바르지 않습니다.");
            return false;
        }

        // Password validation
        if (password.length < 8 || password.length > 20) {
            alert("비밀번호는 8자 이상 20자 이하이어야 합니다.");
            return false;
        }
        if (!/(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])/.test(password)) {
            alert("비밀번호는 숫자, 소문자, 대문자를 각각 하나 이상 포함해야 합니다.");
            return false;
        }
        if (password !== passwordCheck) {
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }

        if (username.length < 3 || username.length > 10) {
            alert("아이디는 3자 이상 10자 이하이어야 합니다.");
            return false;
        }

        // Phone number validation
        if (!/^[0-9]{11}$/.test(phone)) {
            alert("핸드폰 번호는 11자리입니다.");
            return false;
        }

        // Address validation
        if (!zonecode || !address || !addressDetail) {
            alert("주소와 우편번호 및 상세주소는 필수 입력 사항입니다.");
            return false;
        }

        return true;
    }

    // Email verification check
    btnCheckEmail.addEventListener('click', function() {
        const email = document.getElementById('email').value;
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const emailCheckMessage = document.getElementById('emailCheckMessage');

        // Clear message if email is invalid
        if (!emailPattern.test(email)) {
            emailCheckMessage.textContent = "";
            emailCheckMessage.classList.add('d-none');
            alert("이메일 형식이 올바르지 않습니다.");
            return;
        }

        // API call to check email availability
        fetch(`/api/members/check-email?email=${encodeURIComponent(email)}`)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    emailCheckMessage.textContent = "이메일을 사용할 수 있습니다.";
                } else {
                    emailCheckMessage.textContent = "이미 사용 중인 이메일입니다.";
                    emailCheckMessage.style.color = "red";
                }
                emailCheckMessage.classList.remove('d-none');
            })
            .catch(error => {
                console.error("Error:", error);
            });
    });

    // Check username availability
    btnCheckId.addEventListener('click', function() {
        const username = document.getElementById('username').value;

        fetch(`/api/members/username/${encodeURIComponent(username)}/exists`)
            .then(response => response.json())
            .then(data => {
                const idCheckMessage = document.getElementById('idCheckMessage');
                if (data.success) {
                    idCheckMessage.textContent = "사용 가능한 아이디입니다.";
                    idCheckMessage.classList.remove('d-none');
                } else {
                    idCheckMessage.textContent = "이미 사용 중인 아이디입니다.";
                    idCheckMessage.style.color = "red";
                    idCheckMessage.classList.remove('d-none');
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
    });
});
