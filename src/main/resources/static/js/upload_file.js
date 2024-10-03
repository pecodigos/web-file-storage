document.getElementById("uploadButton").addEventListener("click", function() {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append("file", file);

        fetch('/api/files/upload', {
            method: 'POST',
            body: formData,
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('File upload failed.');
            }
        })
        .then(data => {
            console.log("File uploaded successfully: ", data);
            fileInput.value = "";
        })
        .catch(error => {
            console.log("Error uploading file: ", error)
        });
    } else {
        alert("Please select a file to upload.");
    }
});