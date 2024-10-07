// Function to handle login
async function handleLogin(event) {
    event.preventDefault();

    // Get form input values
    const username = document.getElementById("usernameInput").value;
    const password = document.getElementById("passwordInput").value;

    // Creating the request payload
    const formData = new URLSearchParams();
    formData.append('username', username);
    formData.append('password', password);

    try {
        const response = await fetch("http://localhost:8080/user/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: formData.toString()
        });

        if (response.ok) {
            window.location.href = "storage.html";
        } else {
            const errorData = await response.json();
            alert(`Login failed: ${errorData.message}`);
        }
    } catch (error) {
        console.error("Error during login:", error);
        alert("An error occurred. Please try again later.");
    }
}

document.querySelector("button[type='submit']").addEventListener("click", handleLogin);

document.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        handleLogin(event)
            .then(() => console.log("Login attempt made"))
            .catch((error) => console.error("Error during login:", error));
    }
});
