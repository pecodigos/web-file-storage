// Function to handle login
async function handleLogin(event) {
    // Prevent default action for the button
    event.preventDefault();

    // Get username and password values from input fields
    const username = document.getElementById("usernameInput").value;
    const password = document.getElementById("passwordInput").value;

    // Check if username or password is empty
    if (!username || !password) {
        alert("Please enter both username and password.");
        return;
    }

    // Payload for the login request
    const payload = {
        username: username,
        password: password
    };

    try {
        console.log("Payload being sent:", JSON.stringify(payload));

        const response = await fetch("https://api.zapdrive.shop/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const data = await response.json();
            // Check if the token is received
            if (data.token) {
                localStorage.setItem('jwtToken', data.token);
                window.location.href = "/storage";
            } else {
                alert("Login failed. Please try again.");
            }
        } else {
            const errorData = response.headers.get("Content-Type") === "application/json"
                ? await response.json()
                : { message: "Login failed. Please try again." };
            alert(`${errorData.message}`);
        }
    } catch (error) {
        alert("An error occurred. Please try again later.");
    }
}

// Update the button selector to target the correct button
document.getElementById("loginButton").addEventListener("click", handleLogin);

// Allow Enter key to trigger the login function
document.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();
        handleLogin(event);
    }
});
