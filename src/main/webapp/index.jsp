<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Core Bank - Login</title>

    <link rel="stylesheet" href="css/style.css">
</head>

<body class="login-page">
    <div class="login-box">
        <div class="login-header">
            <img src="images/core-bank-logo.png" alt="Core Bank" class="login-logo">
        </div>

        <form id="loginForm">
            <div class="form-group">
                <label for="username">Username</label>

                <input type="text" id="username" placeholder="Enter your username" required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>

                <input type="password" id="password" placeholder="Enter your password" required>
            </div>

            <button type="submit" class="primary-button">
                Sign In
            </button>
        </form>

        <p id="message" class="error-message"></p>
    </div>

    <script src="js/login.js"></script>
</body>

</html>