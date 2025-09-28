const baseUrl = "http://localhost:8080/api/users";

async function createUser() {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const bmi = parseFloat(document.getElementById("bmi").value);

    const user = { name, email, bmi };

    const response = await fetch("http://localhost:8080/api/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: "Alice",
            email: "alice@example.com",
            bmi: 19.8
        })
    })
        .then(res => res.json())
        .then(data => console.log(data))
        .catch(err => console.error(err));

async function loadUsers() {
    const response = await fetch(baseUrl);
    const users = await response.json();

    const list = document.getElementById("userList");
    list.innerHTML = "";
    users.forEach(u => {
        const li = document.createElement("li");
        li.textContent = `${u.id}: ${u.name} (${u.email}) - BMI: ${u.bmi}`;
        list.appendChild(li);
    });
}

// Load users on page load
window.onload = loadUsers;
