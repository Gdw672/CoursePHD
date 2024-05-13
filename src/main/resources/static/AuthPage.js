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
        console.log(data);
    })
    .catch(error => {
        console.error('There has been a problem with your fetch operation:', error);
    });
}
