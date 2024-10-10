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
                await response.json();
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

function displayFiles(files) {
    const fileTableBody = document.querySelector('#file-list-table tbody');
    fileTableBody.innerHTML = '';

    files.forEach(file => {
        const row = document.createElement('tr');

        const fileNameCell = document.createElement('td');
        const fileNameText = document.createElement('span');

        const maxFileNameLength = 60;
        const fileParts = file.name.split('.');

        // Check if the file has an extension
        if (fileParts.length > 1) {
            const extension = fileParts.pop();
            const namePart = fileParts.join('.');

            // Truncate the name part if it exceeds the max length
            fileNameText.textContent = namePart.length > maxFileNameLength
                ? `${namePart.slice(0, maxFileNameLength - 2)}..` + '.' + extension
                : file.name;
        } else {
            // If no extension, just check the name length
            fileNameText.textContent = file.name.length > maxFileNameLength
                ? `${file.name.slice(0, maxFileNameLength - 2)}..`
                : file.name;
        }

        fileNameCell.appendChild(fileNameText);

        const fileSizeCell = document.createElement('td');
        fileSizeCell.textContent = formatFileSize(file.size);

        const uploadDateCell = document.createElement('td');
        uploadDateCell.textContent = new Date(file.uploadDate).toLocaleDateString();

        const actionCell = document.createElement('td');

        const downloadButton = document.createElement('button');
        downloadButton.innerHTML = '<i class="fas fa-download"></i>';
        downloadButton.classList.add('download-button');
        downloadButton.addEventListener('click', () => downloadFile(file.name));
        actionCell.appendChild(downloadButton);

        const deleteButton = document.createElement('button');
        deleteButton.innerHTML = '<i class="fas fa-trash"></i>';
        deleteButton.classList.add('delete-button');
        deleteButton.addEventListener('click', async function () {
            if (confirm(`Are you sure you want to delete file named "${file.name}?"`)) {
                await deleteFile(file.id);
            }
        });
        actionCell.appendChild(deleteButton);

        row.appendChild(fileNameCell);
        row.appendChild(fileSizeCell);
        row.appendChild(uploadDateCell);
        row.appendChild(actionCell);

        fileTableBody.appendChild(row);
    });
}


// Function to handle file download
async function downloadFile(fileName) {
    try {
        const token = localStorage.getItem('jwtToken');
        const response = await fetch(`/api/files/download/${fileName}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);

            // Create a temporary link to trigger the download
            const a = document.createElement('a');
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            a.remove();

            // Release the URL object
            window.URL.revokeObjectURL(url);
        } else {
            alert('Failed to download the file.');
        }
    } catch (error) {
        console.error('Error downloading file:', error);
        alert("An error occurred while downloading the file.");
    }
}

async function deleteFile(fileId) {
    try {
        const token = localStorage.getItem('jwtToken');
        const response = await fetch(`/api/files/${fileId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            alert('File deleted successfully');
            await loadFiles();
        } else {
            const errorData = await response.json();
            alert(errorData.message || "Failed to delete the file.");
        }
    } catch (error) {
        console.error('Error deleting file:', error);
        alert("An error occurred while deleting the file.");
    }
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

document.getElementById('fileInput').addEventListener('change', function() {
    const file = this.files.length > 0 ? this.files[0].name : 'No file selected';
    const fileParts = file.split('.');

    // Ensure there's an extension
    if (fileParts.length > 1) {
        const namePart = fileParts[0];
        const extension = fileParts[fileParts.length - 1];
        let formattedName = namePart.length > 6 ? `${namePart.slice(0, 9)}..` : namePart;

        // Set the formatted name with the extension
        document.getElementById('fileName').textContent = `${formattedName}.${extension}`;
    } else {
        // Handle the case where there is no extension
        document.getElementById('fileName').textContent = file;
    }
});
