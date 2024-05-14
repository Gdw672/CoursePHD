function validateForm() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    if (username.trim() === '' || password.trim() === '') {
        alert("Please enter both username and password.");
        return false;
    }

    console.log("Username: " + username);
    console.log("Password: " + password);

    return true;
}

function postData() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    fetch('/postData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username: username, password: password})
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    })
    .then(data => {
         setCookie("username", document.getElementById("username").value);

         if(data == "OldAccess" || data == "NewAccess") {
         console.log("Password correct");
         window.location.href = "acces.html";
         }
         else {
         console.log("Password incorrect");
         }
    })
    .catch(error => {
        console.error('There has been a problem with your fetch operation:', error);
    });
}

function setCookie(name, value) {
    document.cookie = name + "=" + encodeURIComponent(value) + "; path=/";
}

function getCookie(name) {
    var nameEQ = name + "=";
    var cookies = document.cookie.split(';');
    for(var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1, cookie.length);
        }
        if (cookie.indexOf(nameEQ) === 0) {
            return decodeURIComponent(cookie.substring(nameEQ.length, cookie.length));
        }
    }
    return null;
}


