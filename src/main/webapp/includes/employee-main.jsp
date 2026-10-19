<div class="app-container">

    <!-- ==================== SIDEBAR ==================== -->

    <aside class="sidebar">

        <div class="sidebar-logo">
            <img src="${pageContext.request.contextPath}/images/core-bank-logo.png" alt="Core Bank">
        </div>

        <nav>
            <button type="button" class="menu-button active" id="homeButton">
                Home
            </button>

            <button type="button" class="menu-button" id="clientsButton">
                Clients
            </button>

            <button type="button" class="menu-button" id="accountsButton">
                Accounts
            </button>

            <button type="button" class="menu-button" id="transactionsButton">
                Transactions
            </button>

            <button type="button" class="menu-button" id="transfersButton">
                Transfers
            </button>

            <button type="button" class="menu-button" id="paymentsButton">
                Payments
            </button>

            <button type="button" class="menu-button" id="employeesButton">
                Employees
            </button>
        </nav>
    </aside>


    <!-- ==================== MAIN AREA ==================== -->
    <main class="main-area">

        <!-- TOP BAR -->
        <header class="top-bar">

            <h2 class="mb-0"> Employee Area </h2>

            <div class="user-area">
                <span id="username"></span>

                <button type="button" id="logoutButton" class="btn btn-outline-primary">
                    Logout
                </button>
            </div>
        </header>


        <!-- ==================== CONTENT ==================== -->
        <div class="content">

            <!-- ==================== HOME ==================== -->
            <section id="homeSection">

                <h2>Welcome to Core Bank</h2>

                <p class="text-secondary">
                    Welcome,
                    <span id="homeUsername"></span>.
                </p>

                <div class="row g-4 mt-2">
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">
                                    Clients
                                </h5>
                                <p class="card-text text-secondary">
                                    Manage Core Bank clients.
                                </p>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">
                                    Accounts
                                </h5>
                                <p class="card-text text-secondary">
                                    Manage client bank accounts.
                                </p>
                            </div>
                        </div>
                    </div>


                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">
                                    Operations
                                </h5>
                                <p class="card-text text-secondary">
                                    Manage transactions, transfers and payments.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>


            <!-- ==================== CLIENTS ==================== -->

            <section id="clientsSection" class="d-none">

                <div class="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h2 class="mb-1"> Clients </h2>

                        <p class="text-secondary mb-0">
                            Manage Core Bank clients.
                        </p>
                    </div>

                    <button type="button" class="btn btn-primary" id="addClientButton">
                        + Add Client
                    </button>

                </div>


                <!-- CLIENT LIST -->
                <div id="clientsList" class="row g-4"></div>

                <!-- CLIENT CARD TEMPLATE -->
                <template id="clientCardTemplate">
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm border-start border-primary border-4">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-start">
                                    <h5 class="card-title client-name"></h5>
                                    <span class="badge client-status"></span>
                                </div>
                                <hr>
                                <p class="mb-2"><strong>Client ID:</strong> <span class="client-id"></span></p>
                                <p class="mb-2"><strong>NIF:</strong> <span class="client-nif"></span></p>
                                <p class="mb-2"><strong>Email:</strong> <span class="client-email"></span></p>
                                <p class="mb-0"><strong>Phone:</strong> <span class="client-phone"></span></p>
                                <div class="mt-3 text-end">
                                    <button type="button" class="btn btn-sm client-status-button"></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>


                <!-- ADD CLIENT FORM -->
                <div id="clientFormContainer" class="card shadow-sm mt-4 d-none">

                    <div class="card-body p-4">
                        <h4 class="mb-4">Add Client</h4>


                        <form id="clientForm">
                            <div class="row">

                                <div class="col-md-6 mb-3">

                                    <label for="clientName" class="form-label">
                                        Name
                                    </label>

                                    <input type="text" id="clientName" class="form-control" required>
                                </div>


                                <div class="col-md-6 mb-3">
                                    <label for="clientNif" class="form-label">
                                        NIF
                                    </label>

                                    <input type="text" id="clientNif" class="form-control" maxlength="9"required>
                                </div>


                                <div class="col-md-6 mb-3">
                                    <label for="clientEmail" class="form-label">
                                        Email
                                    </label>

                                    <input type="email" id="clientEmail" class="form-control" required>
                                </div>


                                <div class="col-md-6 mb-3">
                                    <label for="clientPhone" class="form-label">
                                        Phone Number
                                    </label>

                                    <input type="text" id="clientPhone" class="form-control" maxlength="9" required>
                                </div>


                                <div class="col-md-6 mb-3">
                                    <label for="clientStatus" class="form-label">
                                        Status
                                    </label>

                                    <select id="clientStatus" class="form-select">

                                        <option value="ACTIVE">
                                            ACTIVE
                                        </option>

                                        <option value="INACTIVE">
                                            INACTIVE
                                        </option>

                                    </select>
                                </div>
                            </div>


                            <p id="clientMessage" class="text-danger"></p>

                            <div class="d-flex justify-content-end gap-2">

                                <button type="button" class="btn btn-outline-secondary" id="cancelClientButton">
                                    Cancel
                                </button>

                                <button type="submit" class="btn btn-primary">
                                    Create Client
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </section>


            <!-- ==================== ACCOUNTS ==================== -->
            <section id="accountsSection" class="d-none">

                <div class="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h2 class="mb-1">Accounts</h2>

                        <p class="text-secondary mb-0">Manage Core Bank accounts.</p>
                    </div>


                    <button type="button" class="btn btn-primary" id="addAccountButton">
                        + Add Account
                    </button>
                </div>


                <!-- ACCOUNT LIST -->
                <div id="accountsList" class="row g-4"> </div>

                <!-- ACCOUNT CARD TEMPLATE -->
                <template id="accountCardTemplate">
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm border-start border-primary border-4">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <small class="text-secondary">Client ID: <span class="account-client-id"></span></small>
                                        <h5 class="mt-1 mb-2 account-client-name"></h5>
                                        <p class="mb-0"><strong>NIF:</strong> <span class="account-client-nif"></span></p>
                                    </div>
                                    <span class="badge account-status-badge"></span>
                                </div>
                                <hr>
                                <div class="mb-4">
                                    <small class="text-secondary d-block">IBAN</small>
                                    <strong class="text-primary account-iban"></strong>
                                </div>
                                <div class="row">
                                    <div class="col-6">
                                        <small class="text-secondary d-block">Type</small>
                                        <strong class="account-type"></strong>
                                        <small class="text-secondary d-block mt-3">Balance</small>
                                        <strong class="account-balance"></strong>
                                    </div>
                                    <div class="col-6 border-start">
                                        <small class="text-secondary d-block">Status</small>
                                        <strong class="account-status-text"></strong>
                                        <small class="text-secondary d-block mt-3">Account ID</small>
                                        <strong class="account-id"></strong>
                                    </div>
                                </div>
                                <div class="mt-3 text-end">
                                    <button type="button" class="btn btn-sm account-status-button"></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>



                <!-- ADD ACCOUNT FORM -->
                <div id="accountFormContainer" class="card shadow-sm mt-4 d-none">
                    <div class="card-body p-4">
                        <h4 class="mb-4">Add Account</h4>

                        <form id="accountForm">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="accountClient" class="form-label">
                                        Client
                                    </label>

                                    <select id="accountClient" class="form-select" required>
                                        <option value="">
                                            Select a client
                                        </option>
                                    </select>
                                </div>


                                <div class="col-md-6 mb-3">
                                    <label for="accountIban" class="form-label">
                                        IBAN
                                    </label>

                                    <input type="text" id="accountIban" class="form-control" placeholder="PT50..." required>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="accountType" class="form-label">
                                        Account Type
                                    </label>

                                    <select id="accountType" class="form-select" required>
                                        <option value="CHECKING">
                                            CHECKING
                                        </option>

                                        <option value="SAVINGS">
                                            SAVINGS
                                        </option>
                                    </select>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="accountStatus" class="form-label">
                                        Status
                                    </label>

                                    <select id="accountStatus" class="form-select">
                                        <option value="ACTIVE">
                                            ACTIVE
                                        </option>

                                        <option value="INACTIVE">
                                            INACTIVE
                                        </option>
                                    </select>
                                </div>
                            </div>

                            <p id="accountMessage" class="text-danger"> </p>

                            <div class="d-flex justify-content-end gap-2">
                                <button type="button" class="btn btn-outline-secondary" id="cancelAccountButton">
                                    Cancel
                                </button>

                                <button type="submit" class="btn btn-primary">
                                    Create Account
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </section>


            <!-- ==================== TRANSACTIONS ==================== -->

            <section
                id="transactionsSection"
                class="d-none">

                <!-- HEADER -->

                <div class="d-flex justify-content-between align-items-center mb-4">

                    <div>

                        <h2 class="mb-1">
                            Transactions
                        </h2>

                        <p class="text-secondary mb-0">
                            Manage deposits and withdrawals.
                        </p>

                    </div>


                    <div class="d-flex gap-2">

                        <button
                            type="button"
                            class="btn btn-success"
                            id="depositButton">
                            + Deposit
                        </button>

                        <button
                            type="button"
                            class="btn btn-outline-danger"
                            id="withdrawalButton">
                            - Withdrawal
                        </button>

                    </div>

                </div>


                <!-- ==================== TRANSACTION FORM ==================== -->

                <div
                    id="transactionFormContainer"
                    class="card shadow-sm mb-4 d-none">

                    <div class="card-body p-4">

                        <h4
                            id="transactionFormTitle"
                            class="mb-4">
                            New Transaction
                        </h4>


                        <form id="transactionForm">

                            <div class="row">

                                <!-- ACCOUNT -->

                                <div class="col-md-6 mb-3">

                                    <label
                                        for="transactionAccount"
                                        class="form-label">
                                        Account
                                    </label>

                                    <select
                                        id="transactionAccount"
                                        class="form-select"
                                        required>

                                        <option value="">
                                            Select an account
                                        </option>

                                    </select>

                                    <div class="mt-2">

                                        <small class="text-secondary">
                                            Current balance:
                                        </small>

                                        <strong
                                            id="transactionAccountBalance"
                                            class="ms-1">
                                            -
                                        </strong>

                                    </div>

                                </div>


                                <!-- AMOUNT -->

                                <div class="col-md-6 mb-3">

                                    <label
                                        for="transactionAmount"
                                        class="form-label">
                                        Amount
                                    </label>

                                    <div class="input-group">

                                        <span class="input-group-text">
                                            &euro;
                                        </span>

                                        <input
                                            type="number"
                                            id="transactionAmount"
                                            class="form-control"
                                            min="0.01"
                                            step="0.01"
                                            required>

                                    </div>

                                </div>

                            </div>


                            <!-- MESSAGE -->

                            <p
                                id="transactionMessage"
                                class="text-danger">
                            </p>


                            <!-- BUTTONS -->

                            <div class="d-flex justify-content-end gap-2">

                                <button
                                    type="button"
                                    class="btn btn-outline-secondary"
                                    id="cancelTransactionButton">
                                    Cancel
                                </button>

                                <button
                                    type="submit"
                                    class="btn btn-primary"
                                    id="submitTransactionButton">
                                    Confirm
                                </button>

                            </div>

                        </form>

                    </div>

                </div>


                <!-- ==================== TRANSACTION LIST ==================== -->

                <div class="card shadow-sm">

                    <div class="card-body">

                        <div class="table-responsive">

                            <table class="table table-hover align-middle mb-0">

                                <thead>

                                    <tr>

                                        <th>
                                            ID
                                        </th>

                                        <th>
                                            Account
                                        </th>

                                        <th>
                                            Type
                                        </th>

                                        <th>
                                            Amount
                                        </th>

                                        <th>
                                            Date
                                        </th>

                                    </tr>

                                </thead>


                                <tbody id="transactionsList">

                                    <!-- Transactions loaded with JavaScript -->

                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>

            </section>


            <!-- ==================== TRANSFERS ==================== -->

            <section
                id="transfersSection"
                class="d-none">

                <!-- HEADER -->

                <div class="d-flex justify-content-between align-items-center mb-4">

                    <div>
                        <h2 class="mb-1">
                            Transfers
                        </h2>

                        <p class="text-secondary mb-0">
                            Manage bank transfers.
                        </p>
                    </div>

                    <button
                        type="button"
                        class="btn btn-primary"
                        id="newTransferButton">
                        + New Transfer
                    </button>

                </div>


                <!-- ==================== TRANSFER FORM ==================== -->

                <div
                    id="transferFormContainer"
                    class="card shadow-sm mb-4 d-none">

                    <div class="card-body p-4">

                        <h4 class="mb-4">
                            New Transfer
                        </h4>


                        <form id="transferForm">

                            <div class="row">

                                <!-- SOURCE ACCOUNT -->

                                <div class="col-md-6 mb-3">

                                    <label for="sourceAccount" class="form-label">
                                        Source IBAN
                                    </label>

                                    <select
                                        id="sourceAccount"
                                        class="form-select"
                                        required>

                                        <option value="">
                                            Select source account
                                        </option>

                                    </select>


                                    <div class="mt-2">

                                        <small class="text-secondary">
                                            Current balance:
                                        </small>

                                        <strong
                                            id="sourceAccountBalance"
                                            class="ms-1">
                                            -
                                        </strong>

                                    </div>

                                </div>


                                <!-- DESTINATION ACCOUNT -->

                                <div class="col-md-6 mb-3">

                                    <label for="destinationAccount" class="form-label">
                                        Destination IBAN
                                    </label>

                                    <select
                                        id="destinationAccount"
                                        class="form-select"
                                        required>

                                        <option value="">
                                            Select destination account
                                        </option>

                                    </select>

                                </div>


                                <!-- AMOUNT -->

                                <div class="col-md-6 mb-3">

                                    <label
                                        for="transferAmount"
                                        class="form-label">
                                        Amount
                                    </label>

                                    <div class="input-group">

                                        <span class="input-group-text">
                                            &euro;
                                        </span>

                                        <input
                                            type="number"
                                            id="transferAmount"
                                            class="form-control"
                                            min="0.01"
                                            step="0.01"
                                            required>

                                    </div>

                                </div>

                            </div>


                            <!-- MESSAGE -->

                            <p
                                id="transferMessage"
                                class="text-danger">
                            </p>


                            <!-- BUTTONS -->

                            <div class="d-flex justify-content-end gap-2">

                                <button
                                    type="button"
                                    class="btn btn-outline-secondary"
                                    id="cancelTransferButton">
                                    Cancel
                                </button>

                                <button
                                    type="submit"
                                    class="btn btn-primary">
                                    Confirm Transfer
                                </button>

                            </div>

                        </form>

                    </div>

                </div>


                <!-- ==================== TRANSFER LIST ==================== -->

                <div class="card shadow-sm">

                    <div class="card-body">

                        <div class="table-responsive">

                            <table class="table table-hover align-middle mb-0">

                                <thead>

                                    <tr>
                                        <th>ID</th>
                                        <th>Source Account</th>
                                        <th>Destination Account</th>
                                        <th>Amount</th>
                                        <th>Date</th>
                                        <th>Status</th>
                                    </tr>

                                </thead>


                                <tbody id="transfersList">

                                    <!-- Transfers loaded with JavaScript -->

                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>

            </section>


            <!-- ==================== PAYMENTS ==================== -->

            <section id="paymentsSection" class="d-none">

                <!-- HEADER -->

                <div class="d-flex justify-content-between align-items-center mb-4">

                    <div>
                        <h2 class="mb-1">Payments</h2>

                        <p class="text-secondary mb-0">
                            Manage banking payments.
                        </p>
                    </div>

                    <button
                        type="button"
                        class="btn btn-primary"
                        id="newPaymentButton">
                        + New Payment
                    </button>

                </div>


                <!-- ==================== PAYMENT FORM ==================== -->

                <div
                    id="paymentFormContainer"
                    class="card shadow-sm mb-4 d-none">

                    <div class="card-body p-4">

                        <h4 class="mb-4">New Payment</h4>

                        <form id="paymentForm">

                            <div class="row">

                                <!-- ACCOUNT -->

                                <div class="col-md-6 mb-3">

                                    <label
                                        for="paymentAccount"
                                        class="form-label">
                                        Account
                                    </label>

                                    <select
                                        id="paymentAccount"
                                        class="form-select"
                                        required>

                                        <option value="">
                                            Select an account
                                        </option>

                                    </select>

                                    <div class="mt-2">
                                        <small class="text-secondary">
                                            Current balance:
                                        </small>

                                        <strong
                                            id="paymentAccountBalance"
                                            class="ms-1">
                                            -
                                        </strong>
                                    </div>

                                </div>


                                <!-- ENTITY -->

                                <div class="col-md-6 mb-3">

                                    <label
                                        for="paymentEntity"
                                        class="form-label">
                                        Entity
                                    </label>

                                    <input
                                        type="text"
                                        id="paymentEntity"
                                        class="form-control"
                                        maxlength="5"
                                        placeholder="e.g. 12345"
                                        required>

                                </div>


                                <!-- REFERENCE -->

                                <div class="col-md-6 mb-3">

                                    <label
                                        for="paymentReference"
                                        class="form-label">
                                        Reference
                                    </label>

                                    <input
                                        type="text"
                                        id="paymentReference"
                                        class="form-control"
                                        maxlength="9"
                                        placeholder="e.g. 123456789"
                                        required>

                                </div>


                                <!-- AMOUNT -->

                                <div class="col-md-6 mb-3">

                                    <label
                                        for="paymentAmount"
                                        class="form-label">
                                        Amount
                                    </label>

                                    <div class="input-group">

                                        <span class="input-group-text">
                                            &euro;
                                        </span>

                                        <input
                                            type="number"
                                            id="paymentAmount"
                                            class="form-control"
                                            min="0.01"
                                            step="0.01"
                                            required>

                                    </div>

                                </div>

                            </div>


                            <!-- MESSAGE -->

                            <p
                                id="paymentMessage"
                                class="text-danger">
                            </p>


                            <!-- BUTTONS -->

                            <div class="d-flex justify-content-end gap-2">

                                <button
                                    type="button"
                                    class="btn btn-outline-secondary"
                                    id="cancelPaymentButton">
                                    Cancel
                                </button>

                                <button
                                    type="submit"
                                    class="btn btn-primary">
                                    Confirm Payment
                                </button>

                            </div>

                        </form>

                    </div>

                </div>


                <!-- ==================== PAYMENTS LIST ==================== -->

                <div class="card shadow-sm">

                    <div class="card-body">

                        <div class="table-responsive">

                            <table class="table table-hover align-middle mb-0">

                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Account</th>
                                        <th>Entity</th>
                                        <th>Reference</th>
                                        <th>Amount</th>
                                        <th>Date</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>

                                <tbody id="paymentsList">
                                    <!-- Payments loaded with JavaScript -->
                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>

            </section>



            <!-- ==================== EMPLOYEES ==================== -->

            <section id="employeesSection" class="d-none">

                <!-- HEADER -->

                <div class="d-flex justify-content-between align-items-center mb-4">

                    <div>
                        <h2 class="mb-1">Employees</h2>
                        <p class="text-secondary mb-0">
                            Manage bank employees.
                        </p>
                    </div>

                    <button
                        type="button"
                        class="btn btn-primary"
                        id="newEmployeeButton">
                        + Add Employee
                    </button>

                </div>


                <!-- ==================== EMPLOYEE FORM ==================== -->

                <div id="employeeFormContainer" class="card shadow-sm mb-4 d-none">
                    <div class="card-body p-4">
                        <h4 class="mb-4">New Employee</h4>

                        <form id="employeeForm">
                            <div class="row">

                                <!-- NAME -->
                                <div class="col-md-6 mb-3">

                                    <label for="employeeName" class="form-label">
                                        Full Name
                                    </label>

                                    <input type="text" id="employeeName" class="form-control" required>
                                </div>


                                <!-- EMAIL -->
                                <div class="col-md-6 mb-3">
                                    <label for="employeeEmail" class="form-label">
                                        Email
                                    </label>

                                    <input type="email" id="employeeEmail" class="form-control" required>
                                </div>


                                <!-- PHONE -->
                                <div class="col-md-6 mb-3">
                                    <label for="employeePhone" class="form-label">
                                        Phone Number
                                    </label>

                                    <input type="tel" id="employeePhone" class="form-control">
                                </div>


                                <!-- STATUS -->
                                <div class="col-md-6 mb-3">

                                    <label for="employeeStatus" class="form-label">
                                        Status
                                    </label>

                                    <select
                                        id="employeeStatus"
                                        class="form-select"
                                        required>

                                        <option value="ACTIVE">ACTIVE</option>
                                        <option value="INACTIVE">INACTIVE</option>

                                    </select>

                                </div>

                                
                                <!-- USERNAME -->

                                <div class="col-md-6 mb-3">

                                    <label for="employeeUsername" class="form-label">
                                        Username
                                    </label>

                                    <input
                                        type="text"
                                        id="employeeUsername"
                                        class="form-control"
                                        autocomplete="off"
                                        required>

                                </div>


                                <!-- PASSWORD -->

                                <div class="col-md-6 mb-3">

                                    <label for="employeePassword" class="form-label">
                                        Password
                                    </label>

                                    <input
                                        type="password"
                                        id="employeePassword"
                                        class="form-control"
                                        autocomplete="new-password"
                                        required>

                                </div>


                            </div>


                            <!-- MESSAGE -->

                            <p
                                id="employeeMessage"
                                class="text-danger">
                            </p>


                            <!-- BUTTONS -->

                            <div class="d-flex justify-content-end gap-2">

                                <button
                                    type="button"
                                    class="btn btn-outline-secondary"
                                    id="cancelEmployeeButton">
                                    Cancel
                                </button>

                                <button
                                    type="submit"
                                    class="btn btn-primary">
                                    Create Employee
                                </button>

                            </div>

                        </form>
                    </div>
                </div>


                <!-- ==================== EMPLOYEES LIST ==================== -->

                <div class="card shadow-sm">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <th>Phone Number</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody id="employeesList">
                                    <!-- Employees loaded with JavaScript -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </section>

        </div>
    </main>
</div>