// Upload button click event
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
                fileInput.value = ""; // Clear input after successful upload
                loadFiles().then(() => {
                    console.log("Files loaded successfully!");

                }).catch(err => {
                    console.log("Error loading files: ", err);
                });
            })
            .catch(error => {
                console.error("Error uploading file: ", error);
            });
    } else {
        alert("Please select a file to upload.");
    }
});

// Load the list of files
async function loadFiles() {
    const response = await fetch('/api/files/', {
        method: 'GET',
        credentials: 'include'
    });

    if (response.ok) {
        const files = await response.json();
        displayFiles(files);
    } else {
        console.error('Failed to load files');
    }
}

function displayFiles(files) {
    const fileList = document.getElementById('file-list');
    fileList.innerHTML = '';

    files.forEach(file => {
        const listItem = document.createElement('li');
        const link = document.createElement('a');

        link.href = `/api/files/download/${file.name}`;
        link.textContent = file.name;

        listItem.appendChild(link);
        fileList.appendChild(listItem);
    });
}

// Load the files when the page loads
document.addEventListener('DOMContentLoaded', loadFiles);
