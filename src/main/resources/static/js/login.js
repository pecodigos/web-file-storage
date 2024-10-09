// Function to handle login
async function handleLogin(event) {
    event.preventDefault(); // Prevent form submission

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
        // Sending login request to the back-end
        const response = await fetch("http://localhost:8080/user/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const data = await response.json();

            console.log("Response Data:", data);
            localStorage.setItem('jwtToken', data.token);

            window.location.href = "storage.html";
        } else {
            const errorData = response.headers.get("Content-Type") === "application/json"
                ? await response.json()
                : { message: "Login failed. Please try again." };
            alert(`Login failed: ${errorData.message}`);
        }
    } catch (error) {
        console.error("Error during login:", error);
        alert("An error occurred. Please try again later.");
    }
}

document.querySelector("button[type='submit']").addEventListener("click", handleLogin);

// Allow Enter key to trigger the login function
document.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();
        handleLogin(event);
    }
});
