
const token = localStorage.getItem("token");
const role = localStorage.getItem("role");
const username = localStorage.getItem("username");


// ========================================================
// ACCESS CONTROL
// ========================================================

if (!token || role !== "CLIENT") {
    window.location.href = "index.jsp";
}


// ========================================================
// USER
// ========================================================

document.getElementById("username").textContent = username;
document.getElementById("homeUsername").textContent = username;


// ========================================================
// SECTIONS
// ========================================================

const sections = [
    "homeSection",
    "accountsSection",
    "transactionsSection",
    "transfersSection",
    "paymentsSection",
    "profileSection"
];

function showSection(sectionId) {

    sections.forEach(function (id) {

        document
            .getElementById(id)
            .classList.add("d-none");
    });

    document
        .getElementById(sectionId)
        .classList.remove("d-none");
}


// ========================================================
// MENU
// ========================================================

const menuButtons =
    document.querySelectorAll(".menu-button");

menuButtons.forEach(function (button) {

    button.addEventListener("click", function () {

        menuButtons.forEach(function (item) {
            item.classList.remove("active");
        });

        button.classList.add("active");
    });
});


// ========================================================
// HOME
// ========================================================

document
    .getElementById("homeButton")
    .addEventListener("click", function () {

        showSection("homeSection");
    });


// ========================================================
// ACCOUNTS
// ========================================================

document
    .getElementById("accountsButton")
    .addEventListener("click", function () {

        showSection("accountsSection");
    });


// ========================================================
// TRANSACTIONS
// ========================================================

document
    .getElementById("transactionsButton")
    .addEventListener("click", async function () {

        showSection("transactionsSection");

        await loadMyAccounts();
        await loadMyTransactions();
    });


// ========================================================
// TRANSFERS
// ========================================================

document
    .getElementById("transferButton")
    .addEventListener("click", async function () {

        showSection("transfersSection");

        await loadMyAccounts();
        loadTransferAccounts();
    });


// ========================================================
// PAYMENTS
// ========================================================

document
    .getElementById("paymentsButton")
    .addEventListener("click", async function () {

        showSection("paymentsSection");

        await loadMyAccounts();
        loadPaymentAccounts();
    });

// ========================================================
// PROFILE
// ========================================================

document
    .getElementById("profileButton")
    .addEventListener("click", function () {

        showSection("profileSection");
        loadMyProfile();
    });


// ========================================================
// LOGOUT
// ========================================================

document
    .getElementById("logoutButton")
    .addEventListener("click", async function () {

        try {

            await fetch("/core/api/auth/logout", {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        } catch (error) {

            console.error("Logout error:", error);
        }

        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("role");

        window.location.href = "index.jsp";
    });

// ========================================================
// LOAD MY ACCOUNTS
// ========================================================

let myAccounts = [];

async function loadMyAccounts() {

    try {

        const response = await fetch("/core/api/accounts/my", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Error loading accounts.");
        }

        const accounts = await response.json();

        myAccounts = accounts;

        // HOME
        document.getElementById("totalAccounts").textContent =
            accounts.length;

        const totalBalance = accounts.reduce(function (total, account) {
            return total + account.balance;
        }, 0);

        document.getElementById("totalBalance").textContent =
            totalBalance.toLocaleString("pt-PT", {
                style: "currency",
                currency: "EUR"
            });

        // MY ACCOUNTS
        const accountsList = document.getElementById("accountsList");

        accountsList.innerHTML = "";
        
        const template = document.getElementById("accountCardTemplate");

        accounts.forEach(function (account) {

            const card = template.content.cloneNode(true);

            card.querySelector(".account-type").textContent =
                account.accountType;

            card.querySelector(".account-iban").textContent =
                account.iban;

            card.querySelector(".account-balance").textContent =
                account.balance.toLocaleString("pt-PT", {
                    style: "currency",
                    currency: "EUR"
                });

            card.querySelector(".account-id").textContent = account.id;

            const status = card.querySelector(".account-status");

            status.textContent = account.status;

            status.classList.add(
                account.status === "ACTIVE" ? "bg-success" : "bg-secondary"
            );

            accountsList.appendChild(card);

        });


    } catch (error) {

        console.error("Error loading accounts:", error);

    }
}


// ========================================================
// LOAD MY TRANSACTIONS
// ========================================================

async function loadMyTransactions() {

    const transactionsList =
        document.getElementById("transactionsList");

    transactionsList.replaceChildren();

    try {

        const responses = await Promise.all(
            myAccounts.map(function (account) {

                return fetch(
                    "/core/api/transactions/account/" + account.id,
                    {
                        headers: {
                            "Authorization": "Bearer " + token
                        }
                    }
                );
            })
        );

        const transactions = [];

        for (let i = 0; i < responses.length; i++) {

            if (!responses[i].ok) {
                throw new Error("Error loading transactions.");
            }

            const accountTransactions =
                await responses[i].json();

            accountTransactions.forEach(function (transaction) {
                transactions.push(transaction);
            });
        }

        transactions.sort(function (a, b) {
            return new Date(b.date) - new Date(a.date);
        });

        if (transactions.length === 0) {

            const row = transactionsList.insertRow();
            const cell = row.insertCell();

            cell.colSpan = 5;
            cell.textContent = "No transactions found.";
            cell.className = "text-center text-secondary";

            return;
        }

        transactions.forEach(function (transaction) {

            const row = transactionsList.insertRow();

            row.insertCell().textContent = transaction.id;
            row.insertCell().textContent = transaction.accountId;
            const isIncome =
                transaction.type === "DEPOSIT" ||
                transaction.type === "TRANSFER_IN";

            const typeCell = row.insertCell();
            const badge = document.createElement("span");

            badge.className = isIncome
                ? "badge text-bg-success"
                : "badge text-bg-danger";

            badge.textContent = transaction.type;
            typeCell.appendChild(badge);

            const amountCell = row.insertCell();

            amountCell.className = isIncome
                ? "text-success fw-bold"
                : "text-danger fw-bold";

            amountCell.textContent =
                (isIncome ? "+ " : "- ") +
                transaction.amount.toLocaleString("pt-PT", {
                    style: "currency",
                    currency: "EUR"
                });

            row.insertCell().textContent =
                transaction.date
                    ? new Date(transaction.date).toLocaleString("pt-PT")
                    : "-";
        });

    } catch (error) {

        console.error("Error loading transactions:", error);

        transactionsList.replaceChildren();

        const row = transactionsList.insertRow();
        const cell = row.insertCell();

        cell.colSpan = 5;
        cell.textContent = "Error loading transactions.";
        cell.className = "text-center text-danger";
    }
}


// ========================================================
// LOAD TRANSFER ACCOUNTS
// ========================================================

function loadTransferAccounts() {

    const sourceSelect =
        document.getElementById("transferSource");

    sourceSelect.replaceChildren();

    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.textContent = "Select account";
    sourceSelect.appendChild(defaultOption);

    myAccounts.forEach(function (account) {

        if (account.status !== "ACTIVE") {
            return;
        }

        const option = document.createElement("option");

        option.value = account.id;
        option.textContent =
            account.iban + " - " + account.accountType;

        sourceSelect.appendChild(option);
    });
}


// ========================================================
// CREATE TRANSFER
// ========================================================


document
    .getElementById("transferForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const sourceAccountId =
            Number(document.getElementById("transferSource").value);

        const destinationIban =
            document.getElementById("transferDestination")
                .value.replace(/\s+/g, "").toUpperCase();

        const amount =
            Number(document.getElementById("transferAmount").value);

        const message =
            document.getElementById("transferMessage");

        message.textContent = "";

        // VALIDATIONS

        if (!sourceAccountId || !destinationIban ||
            !Number.isFinite(amount) || amount <= 0) {

            message.className = "mt-3 text-danger";
            message.textContent = "Please enter valid transfer details.";
            return;
        }

        // FIND SOURCE ACCOUNT

        const sourceAccount = myAccounts.find(function (account) {
            return account.id === sourceAccountId;
        });

        if (!sourceAccount) {

            message.className = "mt-3 text-danger";
            message.textContent = "Source account not found.";
            return;
        }

        const sourceIban =
            sourceAccount.iban.replace(/\s+/g, "").toUpperCase();

        if (sourceIban === destinationIban) {

            message.className = "mt-3 text-danger";
            message.textContent =
                "Source and destination accounts must be different.";
            return;
        }

        if (amount > sourceAccount.balance) {

            message.className = "mt-3 text-danger";
            message.textContent = "Insufficient balance.";
            return;
        }

        // API REQUEST

        const params = new URLSearchParams({
            sourceIban: sourceIban,
            destinationIban: destinationIban,
            amount: amount
        });

        try {

            const response = await fetch(
                "/core/api/transfers?" + params.toString(),
                {
                    method: "POST",
                    headers: {
                        "Authorization": "Bearer " + token
                    }
                }
            );

            const result = await response.json();

            if (!response.ok) {

                message.className = "mt-3 text-danger";
                message.textContent =
                    result.error || "Transfer failed.";

                return;
            }

            // SUCCESS

            message.className = "mt-3 text-success";
            message.textContent =
                result.message || "Transfer completed successfully.";

            document.getElementById("transferForm").reset();

            document.getElementById("transferAccountBalance").textContent = "-";

            await loadMyAccounts();
            loadTransferAccounts();

        } catch (error) {

            console.error("Transfer error:", error);

            message.className = "mt-3 text-danger";
            message.textContent = "Error processing transfer.";
        }
    });

    
// ========================================================
// LOAD PAYMENT ACCOUNTS
// ========================================================

function loadPaymentAccounts() {

    const paymentAccount =
        document.getElementById("paymentAccount");

    paymentAccount.replaceChildren();

    paymentAccount.add(new Option("Select account", ""));

    myAccounts.forEach(function (account) {

        if (account.status !== "ACTIVE") {
            return;
        }

        const optionText =
            account.iban + " - " + account.accountType;

        paymentAccount.add(
            new Option(optionText, account.id)
        );
    });
}

    
// ========================================================
// CREATE PAYMENT
// ========================================================

document
    .getElementById("paymentForm")
    .addEventListener("submit", async function (event) {

        event.preventDefault();

        const accountId =
            Number(document.getElementById("paymentAccount").value);

        const entity =
            document.getElementById("paymentEntity").value.trim();

        const reference =
            document.getElementById("paymentReference").value.trim();

        const amount =
            Number(document.getElementById("paymentAmount").value);

        const message =
            document.getElementById("paymentMessage");

        message.textContent = "";

        // VALIDATIONS

        if (!accountId || !entity || !reference ||
            !Number.isFinite(amount) || amount <= 0) {

            message.className = "mt-3 text-danger";
            message.textContent = "Please enter valid payment details.";
            return;
        }

        const account = myAccounts.find(function (item) {
            return item.id === accountId;
        });

        if (!account) {

            message.className = "mt-3 text-danger";
            message.textContent = "Account not found.";
            return;
        }

        if (amount > account.balance) {

            message.className = "mt-3 text-danger";
            message.textContent = "Insufficient balance.";
            return;
        }

        // API REQUEST

        const params = new URLSearchParams({
            accountId: accountId,
            entity: entity,
            reference: reference,
            amount: amount
        });

        try {

            const response = await fetch(
                "/core/api/payments?" + params.toString(),
                {
                    method: "POST",
                    headers: {
                        "Authorization": "Bearer " + token
                    }
                }
            );

            const result = await response.json();

            if (!response.ok) {

                message.className = "mt-3 text-danger";
                message.textContent =
                    result.error || "Payment failed.";
                return;
            }

            // SUCCESS

            message.className = "mt-3 text-success";
            message.textContent =
                result.message || "Payment completed successfully.";

            document.getElementById("paymentForm").reset();

            document.getElementById("paymentAccountBalance").textContent = "-";

            await loadMyAccounts();
            loadPaymentAccounts();

        } catch (error) {

            console.error("Payment error:", error);

            message.className = "mt-3 text-danger";
            message.textContent = "Error processing payment.";
        }
    });

// ========================================================
// PAYMENT ACCOUNT BALANCE
// ========================================================

document
    .getElementById("paymentAccount")
    .addEventListener("change", function () {

        const accountId = Number(this.value);

        const account = myAccounts.find(function (item) {
            return item.id === accountId;
        });

        const balance =
            document.getElementById("paymentAccountBalance");

        if (account) {

            balance.textContent =
                account.balance.toLocaleString("pt-PT", {
                    style: "currency",
                    currency: "EUR"
                });

        } else {

            balance.textContent = "-";
        }
    });


// ========================================================
// TRANSFER ACCOUNT BALANCE
// ========================================================

document
    .getElementById("transferSource")
    .addEventListener("change", function () {

        const accountId = Number(this.value);

        const account = myAccounts.find(function (item) {
            return item.id === accountId;
        });

        const balance =
            document.getElementById("transferAccountBalance");

        if (account) {

            balance.textContent =
                account.balance.toLocaleString("pt-PT", {
                    style: "currency",
                    currency: "EUR"
                });

        } else {

            balance.textContent = "-";
        }
    });

    
// ========================================================
// LOAD MY PROFILE
// ========================================================

async function loadMyProfile() {

    try {

        const response = await fetch("/core/api/clients/my", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Error loading profile.");
        }

        const client = await response.json();

        document.getElementById("profileName").textContent =
            client.name || "-";

        document.getElementById("profileNif").textContent =
            client.nif || "-";

        document.getElementById("profileEmail").textContent =
            client.email || "-";

        document.getElementById("profilePhone").textContent =
            client.phoneNumber || "-";

        const status = document.getElementById("profileStatus");

        status.textContent = client.status || "-";

        status.className =
            client.status === "ACTIVE"
                ? "text-success"
                : "text-secondary";

    } catch (error) {

        console.error("Error loading profile:", error);

        document.getElementById("profileContent").textContent =
            "Error loading profile.";
    }
}


// ========================================================
// INITIAL LOAD
// ========================================================

loadMyAccounts();
