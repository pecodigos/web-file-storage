// Function to handle login
async function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById("usernameInput").value;
    const password = document.getElementById("passwordInput").value;

    const payload = {
        username: username,
        password: password
    };

    try {
        const response = await fetch("http://localhost:8080/user/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const data = await response.json();
            console.log("Response Data:", data); // Add this line for debugging
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

// Attach the handleLogin function to the submit button
document.querySelector("button[type='submit']").addEventListener("click", handleLogin);

// Allow Enter key to trigger the login function
document.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();
        handleLogin(event);
    }
});
