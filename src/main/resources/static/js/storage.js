// Check if the user is authenticated
function checkAuth() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        // Redirect to login if no token is found
        window.location.href = "login.html";
    }
}

// Upload button click event
document.getElementById("uploadButton").addEventListener("click", async function() {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await fetch('/api/files/upload', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
                },
                body: formData,
            });

            if (response.ok) {
                const data = await response.json();
                console.log("File uploaded successfully: ", data);
                fileInput.value = "";
                await loadFiles();
            } else {
                const errorData = await response.json();
                alert(errorData.message || "Failed to upload file.");
            }
        } catch (error) {
            console.error("Error uploading file: ", error);
            alert("An error occurred while uploading the file.");
        }
    } else {
        alert("Please select a file to upload.");
    }
});

// Load the list of files
async function loadFiles() {
    try {
        const response = await fetch('/api/files/', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
            }
        });

        if (response.ok) {
            const files = await response.json();
            displayFiles(files);
        } else {
            const errorData = await response.json();
            alert(errorData.message || "Failed to load files.");
        }
    } catch (error) {
        console.error('Error loading files:', error);
        alert("An error occurred while loading files.");
    }
}

// Display the list of files
function displayFiles(files) {
    const fileList = document.getElementById('file-list');
    fileList.innerHTML = '';

    files.forEach(file => {
        const listItem = document.createElement('li');
        const link = document.createElement('a');

        // Create a link to download the file
        link.href = `/api/files/download/${file.name}`;
        link.textContent = file.name;

        // Create a span to display the file size
        const sizeSpan = document.createElement('span');
        sizeSpan.textContent = ` (${formatFileSize(file.size)})`;
        sizeSpan.style.marginLeft = '10px';

        // Append the link and size to the list item
        listItem.appendChild(link);
        listItem.appendChild(sizeSpan);

        const separator = document.createElement('hr');
        separator.style.border = '0.1rem solid rgba(243, 222, 173, 0.4)';
        separator.style.width = "100%";
        separator.style.margin = '0.2rem 0';

        // Append the list item and separator to the file list
        fileList.appendChild(listItem);
        fileList.appendChild(separator);
    });
}

// Function to format file size from bytes to a readable format
function formatFileSize(bytes) {
    if (bytes === 0) return '0 Bytes';
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(1024));
    return Math.round(bytes / Math.pow(1024, i)) + ' ' + sizes[i];
}

// Load the files when the page loads
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    loadFiles();
});
