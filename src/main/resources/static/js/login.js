document.querySelector("button[type='submit']").addEventListener("click", async (event) => {
    event.preventDefault();

    // Get form input values
    const username = document.getElementById("usernameInputLogin").value;
    const password = document.getElementById("passwordInputLogin").value;

    // Create the request payload
    const loginData = {
        username: username,
        password: password
    };

    try {
        const response = await fetch("http://localhost:8080/user/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(loginData)
        });

        if (response.ok) {
            alert("Login successful!");
            window.location.href = "storage.html";
        } else {
            const errorData = await response.json();
            alert(`Login failed: ${errorData.message}`);
        }
    } catch (error) {
        console.error("Error during login:", error);
        alert("An error occurred. Please try again later.");
    }
});
