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
                await response.json();
            }
        } catch (error) {
            console.error("Error uploading file: ", error);
            alert(error.message);
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
            credentials: 'include',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
            }
        });

        if (response.ok) {
            const files = await response.json();
            displayFiles(files);
        } else {
            await response.json();
        }
    } catch (error) {
        console.error('Error loading files:', error);
        alert(error.message);
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
