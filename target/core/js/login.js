const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");

loginForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const username =
        document.getElementById("username").value;

    const password =
        document.getElementById("password").value;

    try {

        const response = await fetch("/core/api/auth/login", {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        if (!response.ok) {
            message.textContent = "Invalid username or password.";
            return;
        }

        const data = await response.json();

        localStorage.setItem("token", data.token);
        localStorage.setItem("username", data.username);
        localStorage.setItem("role", data.role);

        console.log("LOGIN JS VERSION 3");
        console.log("Role:", data.role);

        if (data.role === "EMPLOYEE") {
            window.location.href = "employee.jsp";
        }

        else if (data.role === "CLIENT") {
            window.location.href = "client.jsp";
        }

    } catch (error) {

        message.textContent = "Unable to connect to the server.";

        console.error(error);
    }
});