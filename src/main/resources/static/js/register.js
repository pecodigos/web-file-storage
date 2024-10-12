// Function to handle registration
async function handleRegistration(event) {
    event.preventDefault(); // Prevent form submission

    // Get form input values
    const name = document.getElementById("nameInput").value;
    const username = document.getElementById("usernameInput").value;
    const email = document.getElementById("emailInput").value;
    const password = document.getElementById("passwordInput").value;

    // Create the request payload
    const registerData = {
        name: name,
        username: username,
        email: email,
        password: password
    };

    try {
        const response = await fetch("/user/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(registerData)
        });

        if (response.ok) {
            window.location.href = "login.html";
        } else if (response.status === 401) {
            alert("Username or email already taken.");
        }
        else {
            const errorData = await response.json();
            alert(`Error: ${errorData.message}`);
        }
    } catch (error) {
        console.error("Error during registration:", error);
        alert("An error occurred. Please try again later.");
    }
}

document.querySelector("button[type='submit']").addEventListener("click", handleRegistration);

document.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        handleRegistration(event)
            .then(() => console.log("Registration attempt made"))
            .catch((error) => console.error("Error during registration:", error));
    }
});
