async function handleUpdate(event) {
    event.preventDefault(); // Prevent form submission
    console.log("Button clicked")

    // Get form input values
    const name = document.getElementById("nameInput").value;
    const username = document.getElementById("usernameInput").value;
    const email = document.getElementById("emailInput").value;
    const password = document.getElementById("passwordInput").value;

    // Create the request payload
    const updateData = {
        name: name,
        username: username,
        email: email,
        password: password
    };

    try {
        const response = await fetch("https://api.zapdrive.shop/api/auth/update", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updateData)
        });

        if (response.ok) {
            window.location.href = "/login";
        } else if (response.status === 401) {
            alert("Username or email already taken.");
        }
        else {
            const errorData = await response.json();
            alert(`Error: ${errorData.message}`);
        }
    } catch (error) {
        console.error("Error during update:", error);
        alert("An error occurred. Please try again later.");
    }
}

document.getElementById("updateButton").addEventListener("click", handleUpdate);

document.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        handleUpdate(event)
            .then()
            .catch((error) => console.error("Error during user update:", error));
    }
});
