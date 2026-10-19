const token = localStorage.getItem("token");
const role = localStorage.getItem("role");
const username = localStorage.getItem("username");


// ==================== ACCESS CONTROL ====================

if (!token || role !== "EMPLOYEE") {
    window.location.href = "index.jsp";
}


// ==================== USER ====================

document.getElementById("username").textContent = username;
document.getElementById("homeUsername").textContent = username;


// ==================== SECTIONS ====================

const sections = [
    "homeSection",
    "clientsSection",
    "accountsSection",
    "transactionsSection",
    "transfersSection",
    "paymentsSection",
    "employeesSection"
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


// ==================== MENU ====================

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


document
    .getElementById("homeButton")
    .addEventListener("click", function () {

        showSection("homeSection");
    });


document
    .getElementById("clientsButton")
    .addEventListener("click", function () {

        showSection("clientsSection");
        loadClients();
    });


document
    .getElementById("accountsButton")
    .addEventListener("click", function () {

        showSection("accountsSection");
        loadAccounts();
    });


document
    .getElementById("transactionsButton")
    .addEventListener("click", function () {

        showSection("transactionsSection");
        loadTransactions();
        loadTransactionAccounts();
    });


document
    .getElementById("transfersButton")
    .addEventListener("click", function () {

        showSection("transfersSection");

        loadTransfers();
        loadTransferAccounts();
    });


document
    .getElementById("paymentsButton")
    .addEventListener("click", function () {

        showSection("paymentsSection");

        loadPayments();
        loadPaymentAccounts();
    });


document
    .getElementById("employeesButton")
    .addEventListener("click", function () {

        showSection("employeesSection");
        loadEmployees();
    });


// ========================================================
// CLIENTS
// ========================================================

async function loadClients() {

    try {

        const response = await fetch("/core/api/clients", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            return;
        }

        const clients = await response.json();

        const clientsList =
            document.getElementById("clientsList");

        const template =
            document.getElementById("clientCardTemplate");

        clientsList.replaceChildren();

        clients.forEach(function (client) {

            const isActive = client.status === "ACTIVE";

            const card = template.content.cloneNode(true);

            card.querySelector(".client-name").textContent =
                client.name ?? "-";

            card.querySelector(".client-id").textContent =
                client.id;

            card.querySelector(".client-nif").textContent =
                client.nif ?? "-";

            card.querySelector(".client-email").textContent =
                client.email ?? "-";

            card.querySelector(".client-phone").textContent =
                client.phoneNumber ?? "-";

            const badge = card.querySelector(".client-status");

            badge.textContent = client.status ?? "UNKNOWN";

            badge.classList.add(
                isActive ? "text-bg-success" : "text-bg-secondary"
            );

            const button =
                card.querySelector(".client-status-button");

            button.textContent =
                isActive ? "Deactivate" : "Activate";

            button.classList.add(
                isActive ? "btn-outline-danger" : "btn-outline-success"
            );

            button.disabled =
                client.status !== "ACTIVE" &&
                client.status !== "INACTIVE";

            button.addEventListener("click", function () {

                updateClientStatus(
                    client.id,
                    isActive ? "INACTIVE" : "ACTIVE"
                );

            });

            clientsList.appendChild(card);

        });

    } catch (error) {

        console.error(error);
    }
}


async function updateClientStatus(id, status) {

    const action = status === "ACTIVE" ? "activate" : "deactivate";

    if (!confirm(`Are you sure you want to ${action} this client?`)) {
        return;
    }

    try {

        const response = await fetch(
            `/core/api/clients/${id}/status?status=${status}`,
            {
                method: "PUT",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            alert("Error updating client status.");
            return;
        }

        await loadClients();

    } catch (error) {

        console.error(error);
        alert("Error updating client status.");
    }
}


// ==================== CLIENT FORM ====================

document
    .getElementById("addClientButton")
    .addEventListener("click", function () {

        document
            .getElementById("clientFormContainer")
            .classList.remove("d-none");
    });


document
    .getElementById("cancelClientButton")
    .addEventListener("click", function () {

        document
            .getElementById("clientFormContainer")
            .classList.add("d-none");

        document
            .getElementById("clientForm")
            .reset();
    });


document
    .getElementById("clientForm")
    .addEventListener("submit", createClient);


async function createClient(event) {

    event.preventDefault();


    const client = {

        name:
            document.getElementById("clientName").value,

        nif:
            document.getElementById("clientNif").value,

        email:
            document.getElementById("clientEmail").value,

        phoneNumber:
            document.getElementById("clientPhone").value,

        status:
            document.getElementById("clientStatus").value
    };


    try {

        const response = await fetch("/core/api/clients", {

            method: "POST",

            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },

            body: JSON.stringify(client)
        });


        if (!response.ok) {

            document
                .getElementById("clientMessage")
                .textContent =
                "Client could not be created.";

            return;
        }


        document
            .getElementById("clientForm")
            .reset();


        document
            .getElementById("clientFormContainer")
            .classList.add("d-none");


        document
            .getElementById("clientMessage")
            .textContent = "";


        await loadClients();


    } catch (error) {

        console.error(error);
    }
}


// ========================================================
// ACCOUNTS
// ========================================================

async function loadAccounts() {

    try {

        const accountsResponse =
            await fetch("/core/api/accounts", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        const clientsResponse =
            await fetch("/core/api/clients", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        if (!accountsResponse.ok || !clientsResponse.ok) {
            return;
        }

        const accounts = await accountsResponse.json();
        const clients = await clientsResponse.json();

        const accountsList =
            document.getElementById("accountsList");

        const template =
            document.getElementById("accountCardTemplate");

        accountsList.replaceChildren();

        accounts.forEach(function (account) {

            const client = clients.find(function (item) {
                return item.id === account.clientId;
            });

            const isActive = account.status === "ACTIVE";

            const card = template.content.cloneNode(true);

            card.querySelector(".account-client-id").textContent =
                account.clientId;

            card.querySelector(".account-client-name").textContent =
                client ? client.name : "Unknown Client";

            card.querySelector(".account-client-nif").textContent =
                client ? client.nif : "-";

            card.querySelector(".account-iban").textContent =
                account.iban ?? "-";

            card.querySelector(".account-type").textContent =
                account.accountType ?? "-";

            card.querySelector(".account-balance").textContent =
                "€" + account.balance.toFixed(2);

            card.querySelector(".account-status-text").textContent =
                account.status ?? "UNKNOWN";

            card.querySelector(".account-id").textContent =
                account.id;

            const badge =
                card.querySelector(".account-status-badge");

            badge.textContent = account.status ?? "UNKNOWN";

            badge.classList.add(
                isActive ? "text-bg-success" : "text-bg-secondary"
            );

            const button =
                card.querySelector(".account-status-button");

            button.textContent =
                isActive ? "Deactivate" : "Activate";

            button.classList.add(
                isActive ? "btn-outline-danger" : "btn-outline-success"
            );

            button.disabled =
                account.status !== "ACTIVE" &&
                account.status !== "INACTIVE";

            button.addEventListener("click", function () {

                updateAccountStatus(
                    account.id,
                    isActive ? "INACTIVE" : "ACTIVE"
                );

            });

            accountsList.appendChild(card);

        });

        loadAccountClients(clients);

    } catch (error) {

        console.error(error);
    }
}


// ==================== UPDATE ACCOUNT STATUS ====================

async function updateAccountStatus(id, status) {

    const action =
        status === "ACTIVE" ? "activate" : "deactivate";

    if (!confirm(`Are you sure you want to ${action} this account?`)) {
        return;
    }

    try {

        const response = await fetch(
            `/core/api/accounts/${id}/status?status=${status}`,
            {
                method: "PUT",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            alert("Error updating account status.");
            return;
        }

        await loadAccounts();

    } catch (error) {

        console.error(error);
        alert("Error updating account status.");
    }
}


// ==================== ACCOUNT CLIENT SELECT ====================

function loadAccountClients(clients) {

    const accountClient =
        document.getElementById("accountClient");


    accountClient.innerHTML = `
        <option value="">
            Select a client
        </option>
    `;


    clients.forEach(function (client) {

        accountClient.innerHTML += `
            <option value="${client.id}">
                ${client.name} - NIF ${client.nif}
            </option>
        `;
    });
}


// ==================== ACCOUNT FORM ====================

document
    .getElementById("addAccountButton")
    .addEventListener("click", function () {

        document
            .getElementById("accountFormContainer")
            .classList.remove("d-none");
    });


document
    .getElementById("cancelAccountButton")
    .addEventListener("click", function () {

        document
            .getElementById("accountFormContainer")
            .classList.add("d-none");

        document
            .getElementById("accountForm")
            .reset();
    });


document
    .getElementById("accountForm")
    .addEventListener("submit", createAccount);


async function createAccount(event) {

    event.preventDefault();


    const account = {

        iban:
            document.getElementById("accountIban").value,

        accountType:
            document.getElementById("accountType").value,

        balance: 0,

        status:
            document.getElementById("accountStatus").value,

        clientId:
            Number(
                document.getElementById("accountClient").value
            )
    };


    try {

        const response = await fetch("/core/api/accounts", {

            method: "POST",

            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },

            body: JSON.stringify(account)
        });


        if (!response.ok) {

            document
                .getElementById("accountMessage")
                .textContent =
                "Account could not be created.";

            return;
        }


        document
            .getElementById("accountForm")
            .reset();


        document
            .getElementById("accountFormContainer")
            .classList.add("d-none");


        document
            .getElementById("accountMessage")
            .textContent = "";


        await loadAccounts();


    } catch (error) {

        console.error(error);
    }
}

// ========================================================
// TRANSACTIONS
// ========================================================

let transactionType = "";
let transactionAccounts = [];


// -------------------- LOAD TRANSACTIONS --------------------

async function loadTransactions() {

    try {

        const response = await fetch("/core/api/transactions", {

            headers: {
                "Authorization": "Bearer " + token
            }
        });


        if (!response.ok) {
            return;
        }


        const transactions = await response.json();

        const transactionsList =
            document.getElementById("transactionsList");

        transactionsList.innerHTML = "";


        transactions.forEach(function (transaction) {

            const date =
                new Date(transaction.date).toLocaleString();


            transactionsList.innerHTML += `
                <tr>

                    <td>
                        ${transaction.id}
                    </td>

                    <td>
                        ${transaction.accountId}
                    </td>

                    <td>
                        ${transaction.type}
                    </td>

                    <td>
                        €${transaction.amount.toFixed(2)}
                    </td>

                    <td>
                        ${date}
                    </td>

                </tr>
            `;
        });


    } catch (error) {

        console.error(error);
    }
}


// -------------------- LOAD ACCOUNTS --------------------

async function loadTransactionAccounts() {

    try {

        const accountsResponse =
            await fetch("/core/api/accounts", {

                headers: {
                    "Authorization": "Bearer " + token
                }
            });


        const clientsResponse =
            await fetch("/core/api/clients", {

                headers: {
                    "Authorization": "Bearer " + token
                }
            });


        if (!accountsResponse.ok ||
            !clientsResponse.ok) {

            return;
        }


        const accounts =
            await accountsResponse.json();
        
        transactionAccounts = accounts;

        const clients =
            await clientsResponse.json();


        const transactionAccount =
            document.getElementById("transactionAccount");


        transactionAccount.innerHTML = `
            <option value="">
                Select an account
            </option>
        `;


        accounts.forEach(function (account) {

            const client =
                clients.find(function (client) {

                    return client.id === account.clientId;
                });


            const clientName =
                client ? client.name : "Unknown Client";


            transactionAccount.innerHTML += `
                <option value="${account.id}">
                    ${clientName} - ${account.iban}
                </option>
            `;
        });


    } catch (error) {

        console.error(error);
    }
}

document
    .getElementById("transactionAccount")
    .addEventListener("change", function () {

        const accountId = Number(this.value);

        const account = transactionAccounts.find(function (account) {
            return account.id === accountId;
        });

        const balance =
            document.getElementById("transactionAccountBalance");


        if (account) {

            balance.textContent =
                "€" + account.balance.toFixed(2);

        } else {

            balance.textContent = "-";
        }
    });


// -------------------- DEPOSIT BUTTON --------------------

document
    .getElementById("depositButton")
    .addEventListener("click", function () {

        transactionType = "DEPOSIT";

        document
            .getElementById("transactionFormTitle")
            .textContent = "New Deposit";


        document
            .getElementById("submitTransactionButton")
            .textContent = "Confirm Deposit";


        document
            .getElementById("transactionFormContainer")
            .classList.remove("d-none");
    });


// -------------------- WITHDRAWAL BUTTON --------------------

document
    .getElementById("withdrawalButton")
    .addEventListener("click", function () {

        transactionType = "WITHDRAWAL";


        document
            .getElementById("transactionFormTitle")
            .textContent = "New Withdrawal";


        document
            .getElementById("submitTransactionButton")
            .textContent = "Confirm Withdrawal";


        document
            .getElementById("transactionFormContainer")
            .classList.remove("d-none");
    });


// -------------------- CANCEL --------------------

document
    .getElementById("cancelTransactionButton")
    .addEventListener("click", function () {

        document
            .getElementById("transactionFormContainer")
            .classList.add("d-none");


        document
            .getElementById("transactionForm")
            .reset();


        document
            .getElementById("transactionMessage")
            .textContent = "";


        transactionType = "";
    });


// -------------------- SUBMIT --------------------

document
    .getElementById("transactionForm")
    .addEventListener("submit", createTransaction);


async function createTransaction(event) {

    event.preventDefault();


    const accountId =
        document.getElementById("transactionAccount").value;

    const amount =
        document.getElementById("transactionAmount").value;


    if (!transactionType) {
        return;
    }


    let url;


    if (transactionType === "DEPOSIT") {

        url =
            "/core/api/transactions/deposit/" +
            accountId +
            "?amount=" +
            amount;

    } else {

        url =
            "/core/api/transactions/withdrawal/" +
            accountId +
            "?amount=" +
            amount;
    }


    try {

        const response = await fetch(url, {

            method: "POST",

            headers: {
                "Authorization": "Bearer " + token
            }
        });


        if (!response.ok) {

            const message =
                document.getElementById("transactionMessage");

            if (transactionType === "WITHDRAWAL") {
                message.textContent =
                    "Insufficient balance or transaction could not be completed.";
            } else {
                message.textContent =
                    "Transaction could not be completed.";
            }

            return;
        }


        document
            .getElementById("transactionForm")
            .reset();


        document
            .getElementById("transactionFormContainer")
            .classList.add("d-none");


        document
            .getElementById("transactionMessage")
            .textContent = "";


        transactionType = "";


        await loadTransactions();


    } catch (error) {

        document
            .getElementById("transactionMessage")
            .textContent =
            "Error connecting to server.";

        console.error(error);
    }
}

// ========================================================
// TRANSFERS
// ========================================================

let transferAccounts = [];


// -------------------- LOAD TRANSFERS --------------------

async function loadTransfers() {

    try {

        const response =
            await fetch("/core/api/transfers", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        if (!response.ok) {
            return;
        }

        const transfers =
            await response.json();

        const transfersList =
            document.getElementById("transfersList");

        transfersList.innerHTML = "";

        transfers.forEach(function (transfer) {

            const date =
                new Date(transfer.date).toLocaleString();

            transfersList.innerHTML += `
                <tr>
                    <td>${transfer.id}</td>
                    <td>${transfer.sourceAccountId}</td>
                    <td>${transfer.destinationAccountId}</td>
                    <td>€${transfer.amount.toFixed(2)}</td>
                    <td>${date}</td>
                    <td>${transfer.status}</td>
                </tr>
            `;
        });

    } catch (error) {

        console.error(error);
    }
}

// -------------------- TRANSFER FORM --------------------

document
    .getElementById("newTransferButton")
    .addEventListener("click", function () {

        document
            .getElementById("transferFormContainer")
            .classList.remove("d-none");
    });


document
    .getElementById("cancelTransferButton")
    .addEventListener("click", function () {

        document
            .getElementById("transferForm")
            .reset();

        document
            .getElementById("transferFormContainer")
            .classList.add("d-none");

        document
            .getElementById("transferMessage")
            .textContent = "";

        document
            .getElementById("sourceAccountBalance")
            .textContent = "-";
    });

    // -------------------- SOURCE ACCOUNT BALANCE --------------------


document
    .getElementById("sourceAccount")
    .addEventListener("change", function () {

        const sourceIban = this.value;

        const account =
            transferAccounts.find(function (account) {
                return account.iban === sourceIban;
            });

        const balance =
            document.getElementById("sourceAccountBalance");

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


// -------------------- CREATE TRANSFER --------------------

document
    .getElementById("transferForm")
    .addEventListener("submit", createTransfer);



async function createTransfer(event) {

    event.preventDefault();

    const sourceIban =
        document.getElementById("sourceAccount").value;

    const destinationIban =
        document.getElementById("destinationAccount").value;

    const amount =
        Number(document.getElementById("transferAmount").value);

    const message =
        document.getElementById("transferMessage");

    message.textContent = "";

    // VALIDATIONS

    if (!sourceIban || !destinationIban) {
        message.textContent =
            "Please select both accounts.";
        return;
    }

    if (sourceIban === destinationIban) {
        message.textContent =
            "Source and destination accounts must be different.";
        return;
    }

    if (!Number.isFinite(amount) || amount <= 0) {
        message.textContent =
            "Amount must be greater than zero.";
        return;
    }

    const sourceAccount = transferAccounts.find(function (account) {
        return account.iban === sourceIban;
    });

    if (!sourceAccount) {
        message.textContent = "Source account not found.";
        return;
    }

    if (amount > sourceAccount.balance) {
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
            message.textContent =
                result.error || "Transfer could not be completed.";
            return;
        }

        // SUCCESS

        document.getElementById("transferForm").reset();

        document
            .getElementById("transferFormContainer")
            .classList.add("d-none");

        document
            .getElementById("sourceAccountBalance")
            .textContent = "-";

        message.textContent = "";

        await loadTransfers();
        await loadTransferAccounts();

    } catch (error) {

        message.textContent =
            "Error connecting to server.";

        console.error(error);
    }
}


// ========================================================
// PAYMENTS
// ========================================================

let paymentAccounts = [];


// -------------------- LOAD PAYMENTS --------------------

async function loadPayments() {

    try {

        const response = await fetch("/core/api/payments", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            return;
        }

        const payments = await response.json();

        const paymentsList =
            document.getElementById("paymentsList");

        paymentsList.innerHTML = "";

        payments.forEach(function (payment) {

            const date =
                new Date(payment.date).toLocaleString();

            paymentsList.innerHTML += `
                <tr>
                    <td>${payment.id}</td>
                    <td>${payment.accountId}</td>
                    <td>${payment.entity}</td>
                    <td>${payment.reference}</td>
                    <td>€${payment.amount.toFixed(2)}</td>
                    <td>${date}</td>
                    <td>${payment.status}</td>
                </tr>
            `;
        });

    } catch (error) {

        console.error(error);
    }
}

// -------------------- PAYMENT FORM --------------------

document
    .getElementById("newPaymentButton")
    .addEventListener("click", function () {

        document
            .getElementById("paymentFormContainer")
            .classList.remove("d-none");
    });


document
    .getElementById("cancelPaymentButton")
    .addEventListener("click", function () {

        document.getElementById("paymentForm").reset();

        document
            .getElementById("paymentFormContainer")
            .classList.add("d-none");

        document
            .getElementById("paymentMessage")
            .textContent = "";

        document
            .getElementById("paymentAccountBalance")
            .textContent = "-";
    });


// -------------------- PAYMENT ACCOUNT BALANCE --------------------

document
    .getElementById("paymentAccount")
    .addEventListener("change", function () {

        const accountId = Number(this.value);

        const account = paymentAccounts.find(function (account) {
            return account.id === accountId;
        });

        const balance =
            document.getElementById("paymentAccountBalance");

        if (account) {

            balance.textContent =
                "€" + account.balance.toFixed(2);

        } else {

            balance.textContent = "-";
        }
    });


// -------------------- CREATE PAYMENT --------------------
document
    .getElementById("paymentForm")
    .addEventListener("submit", createPayment);


async function createPayment(event) {

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

    if (!accountId || !entity || !reference) {
        message.textContent = "All fields are required.";
        return;
    }

    if (!Number.isFinite(amount) || amount <= 0) {
        message.textContent = "Amount must be greater than zero.";
        return;
    }

    const account = paymentAccounts.find(function (account) {
        return account.id === accountId;
    });

    if (!account) {
        message.textContent = "Account not found.";
        return;
    }

    if (amount > account.balance) {
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

    const url = "/core/api/payments?" + params.toString();

    try {

        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            message.textContent =
                "Payment could not be completed.";
            return;
        }

        // SUCCESS

        document.getElementById("paymentForm").reset();

        document
            .getElementById("paymentFormContainer")
            .classList.add("d-none");

        document
            .getElementById("paymentAccountBalance")
            .textContent = "-";

        message.textContent = "";

        await loadPayments();
        await loadPaymentAccounts();

    } catch (error) {

        message.textContent = "Error connecting to server.";

        console.error(error);
    }
}


// -------------------- LOAD PAYMENT ACCOUNTS --------------------

async function loadPaymentAccounts() {

    try {

        const accountsResponse =
            await fetch("/core/api/accounts", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        const clientsResponse =
            await fetch("/core/api/clients", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        if (!accountsResponse.ok || !clientsResponse.ok) {
            return;
        }

        const accounts = await accountsResponse.json();
        const clients = await clientsResponse.json();

        paymentAccounts = accounts;

        const paymentAccount =
            document.getElementById("paymentAccount");

        paymentAccount.innerHTML = `
            <option value="">
                Select an account
            </option>
        `;

        accounts.forEach(function (account) {

            const client = clients.find(function (client) {
                return client.id === account.clientId;
            });

            const clientName =
                client ? client.name : "Unknown Client";

            paymentAccount.innerHTML += `
                <option value="${account.id}">
                    ${clientName} - ${account.iban}
                </option>
            `;
        });

    } catch (error) {

        console.error(error);
    }
}


// ========================================================
// EMPLOYEES
// ========================================================

// -------------------- EMPLOYEE FORM --------------------

document
    .getElementById("newEmployeeButton")
    .addEventListener("click", function () {

        document
            .getElementById("employeeFormContainer")
            .classList.remove("d-none");
    });


document
    .getElementById("cancelEmployeeButton")
    .addEventListener("click", function () {

        document.getElementById("employeeForm").reset();

        document
            .getElementById("employeeFormContainer")
            .classList.add("d-none");

        document
            .getElementById("employeeMessage")
            .textContent = "";
    });


// -------------------- CREATE EMPLOYEE --------------------

document
    .getElementById("employeeForm")
    .addEventListener("submit", createEmployee);


async function createEmployee(event) {

    event.preventDefault();

    const message =
        document.getElementById("employeeMessage");

    message.textContent = "";

    const employee = {
        name: document.getElementById("employeeName").value.trim(),
        email: document.getElementById("employeeEmail").value.trim(),
        phoneNumber: document.getElementById("employeePhone").value.trim(),
        status: "ACTIVE"
    };

    const username =
        document.getElementById("employeeUsername").value.trim();

    const password =
        document.getElementById("employeePassword").value;

    if (!username || !password) {
        message.textContent = "Username and password are required.";
        return;
    }

    try {

        // CREATE EMPLOYEE

        const employeeResponse = await fetch("/core/api/employees", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(employee)
        });

        if (!employeeResponse.ok) {
            message.textContent = "Employee could not be created.";
            return;
        }

        const createdEmployee = await employeeResponse.json();

        if (!createdEmployee.id) {
            message.textContent =
                "Employee created, but no employee ID was returned.";
            return;
        }

        // CREATE USER

        const user = {
            username: username,
            password: password,
            role: "EMPLOYEE",
            employeeId: createdEmployee.id,
            clientId: null
        };

        const userResponse = await fetch("/core/api/users", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        if (!userResponse.ok) {
            message.textContent =
                "Employee created, but user account could not be created.";
            await loadEmployees();
            return;
        }

        // SUCCESS

        document.getElementById("employeeForm").reset();

        document
            .getElementById("employeeFormContainer")
            .classList.add("d-none");

        message.textContent = "";

        await loadEmployees();

    } catch (error) {

        message.textContent = "Error connecting to server.";
        console.error(error);
    }
}


// -------------------- LOAD EMPLOYEES --------------------

async function loadEmployees() {

    try {

        const response = await fetch("/core/api/employees", {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            console.error("Error loading employees:", response.status);
            return;
        }

        const employees = await response.json();

        const employeesList =
            document.getElementById("employeesList");

        employeesList.innerHTML = "";

        employees.forEach(function (employee) {

            const row = document.createElement("tr");

            const values = [
                employee.id,
                employee.name,
                employee.email,
                employee.phoneNumber,
                employee.status
            ];

            values.forEach(function (value) {
                const cell = document.createElement("td");
                cell.textContent = value ?? "-";
                row.appendChild(cell);
            });

            employeesList.appendChild(row);
        });

    } catch (error) {

        console.error(error);
    }
}


// -------------------- LOAD ACCOUNTS --------------------


async function loadTransferAccounts() {

    try {

        const accountsResponse =
            await fetch("/core/api/accounts", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        const clientsResponse =
            await fetch("/core/api/clients", {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });

        if (!accountsResponse.ok || !clientsResponse.ok) {
            return;
        }

        const accounts = await accountsResponse.json();
        const clients = await clientsResponse.json();

        transferAccounts = accounts;

        const sourceAccount =
            document.getElementById("sourceAccount");

        const destinationAccount =
            document.getElementById("destinationAccount");

        sourceAccount.replaceChildren();
        destinationAccount.replaceChildren();

        sourceAccount.add(new Option("Select source account", ""));
        destinationAccount.add(new Option("Select destination account", ""));

        accounts.forEach(function (account) {

            const client = clients.find(function (client) {
                return client.id === account.clientId;
            });

            const clientName =
                client ? client.name : "Unknown Client";

            const optionText =
                clientName + " - " + account.iban;

            sourceAccount.add(
                new Option(optionText, account.iban)
            );

            destinationAccount.add(
                new Option(optionText, account.iban)
            );
        });

    } catch (error) {

        console.error(error);
    }
}


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

            console.error(error);
        }


        localStorage.clear();

        window.location.href = "index.jsp";
    });