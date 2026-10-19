
<div class="app-container">

    <!-- ==================== SIDEBAR ==================== -->

    <aside class="sidebar">

        <div class="sidebar-logo">
            <img
                src="${pageContext.request.contextPath}/images/core-bank-logo.png"
                alt="Core Bank"
                class="img-fluid">
        </div>

        <nav class="sidebar-menu">

            <button id="homeButton" class="menu-button active">
                Home
            </button>

            <button id="accountsButton" class="menu-button">
                My Accounts
            </button>

            <button id="transactionsButton" class="menu-button">
                Transactions
            </button>

            <button id="transferButton" class="menu-button">
                Transfers
            </button>

            <button id="paymentsButton" class="menu-button">
                Payments
            </button>

            <button id="profileButton" class="menu-button">
                My Profile
            </button>

        </nav>

    </aside>


    <!-- ==================== MAIN AREA ==================== -->

    <div class="main-area">

        <!-- TOP BAR -->

        <header class="top-bar">

            <h5 class="mb-0">Client Area</h5>

            <div class="user-area">

                <span id="username"></span>

                <button
                    type="button"
                    id="logoutButton"
                    class="btn btn-outline-danger btn-sm">
                    Logout
                </button>

            </div>

        </header>


        <!-- ==================== CONTENT ==================== -->

        <main class="content">

            <!-- HOME -->

            <section id="homeSection">

                <h2>Welcome to Core Bank</h2>

                <p class="text-secondary">
                    Welcome, <strong id="homeUsername"></strong>.
                </p>

                <div class="row g-4 mt-2">

                    <div class="col-md-4">
                        <div class="card shadow-sm">
                            <div class="card-body">

                                <h6 class="text-secondary">
                                    My Accounts
                                </h6>

                                <h3 id="totalAccounts">-</h3>

                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="card shadow-sm">
                            <div class="card-body">

                                <h6 class="text-secondary">
                                    Total Balance
                                </h6>

                                <h3 id="totalBalance">-</h3>

                            </div>
                        </div>
                    </div>

                </div>

            </section>


          
            <!-- ==================== ACCOUNTS ==================== -->

            <section id="accountsSection" class="d-none">

                <div class="mb-4">
                    <h2 class="mb-1">My Accounts</h2>
                    <p class="text-secondary mb-0">
                        View your bank accounts and balances.
                    </p>
                </div>

                <!-- ACCOUNT LIST -->
                <div id="accountsList" class="row g-4"></div>

                
                <!-- ACCOUNT CARD TEMPLATE -->

                <template id="accountCardTemplate">

                    <div class="col-md-6 col-xl-4">

                        <div class="card h-100 shadow-sm border-start border-primary border-4">

                            <div class="card-body">

                                <!-- HEADER -->

                                <div class="d-flex justify-content-between align-items-start">

                                    <h5 class="account-type mt-1 mb-2"></h5>

                                    <span class="account-status badge"></span>

                                </div>

                                <hr>

                                <!-- IBAN -->

                                <div class="mb-4">

                                    <small class="text-secondary d-block">
                                        IBAN
                                    </small>

                                    <strong class="account-iban text-primary"></strong>

                                </div>

                                <!-- ACCOUNT DETAILS -->

                                <div class="row">

                                    <div class="col-6">

                                        <small class="text-secondary d-block">
                                            Balance
                                        </small>

                                        <strong class="account-balance"></strong>

                                    </div>

                                    <div class="col-6 border-start">

                                        <small class="text-secondary d-block">
                                            Account ID
                                        </small>

                                        <strong class="account-id"></strong>

                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>

                </template>


            </section>



            <!-- TRANSACTIONS -->

            <section id="transactionsSection" class="d-none">

                <h2>Transactions</h2>

                <p class="text-secondary">
                    View your transaction history.
                </p>

                <div class="card shadow-sm">

                    <div class="card-body">

                        <div class="table-responsive">

                            <table class="table table-hover align-middle mb-0">

                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Account</th>
                                        <th>Type</th>
                                        <th>Amount</th>
                                        <th>Date</th>
                                    </tr>
                                </thead>

                                <tbody id="transactionsList">
                                    <!-- Loaded with JavaScript -->
                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>

            </section>


            <!-- TRANSFERS -->

                        
            <section id="transfersSection" class="d-none">

                <h2>Transfers</h2>
                <p class="text-secondary">
                    Transfer money between bank accounts.
                </p>

                <div class="card shadow-sm">
                    <div class="card-body">

                        <form id="transferForm">

                            <div class="mb-3">
                                <label for="transferSource" class="form-label">
                                    Source Account
                                </label>

                                <select id="transferSource"
                                        class="form-select"
                                        required>
                                    <option value="">Select account</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="transferDestination" class="form-label">
                                    Destination IBAN
                                </label>

                                <input type="text"
                                    id="transferDestination"
                                    class="form-control"
                                    placeholder="Enter destination IBAN"
                                    maxlength="34"
                                    required>
                            </div>

                            <div class="mb-3">
                                <label for="transferAmount" class="form-label">
                                    Amount (&euro;)
                                </label>

                                <input type="number"
                                    id="transferAmount"
                                    class="form-control"
                                    min="0.01"
                                    step="0.01"
                                    required>

                                    <div class="mt-2">
                                        <small class="text-secondary">
                                            Available balance:
                                        </small>

                                        <strong id="transferAccountBalance" class="ms-1">
                                            -
                                        </strong>
                                    </div>
                            </div>

                            <button type="submit" class="btn btn-primary">
                                Transfer
                            </button>

                        </form>

                        <div id="transferMessage" class="mt-3"></div>

                    </div>
                </div>

            </section>

            <section id="paymentsSection" class="d-none">

                <h2>Payments</h2>
                <p class="text-secondary">
                    Make payments from your bank accounts.
                </p>

                <div class="card shadow-sm">
                    <div class="card-body">

                        <form id="paymentForm">

                            <div class="mb-3">
                                <label for="paymentAccount" class="form-label">
                                    Source Account
                                </label>

                                <select id="paymentAccount"
                                        class="form-select"
                                        required>
                                    <option value="">Select account</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="paymentEntity" class="form-label">
                                    Entity
                                </label>

                                <input type="text"
                                    id="paymentEntity"
                                    class="form-control"
                                    required>
                            </div>

                            <div class="mb-3">
                                <label for="paymentReference" class="form-label">
                                    Reference
                                </label>

                                <input type="text"
                                    id="paymentReference"
                                    class="form-control"
                                    required>
                            </div>

                            <div class="mb-3">
                                <label for="paymentAmount" class="form-label">
                                    Amount (&euro;)
                                </label>

                                <input type="number"
                                    id="paymentAmount"
                                    class="form-control"
                                    min="0.01"
                                    step="0.01"
                                    required>

                                <div class="mt-2">
                                    <small class="text-secondary">
                                        Available balance:
                                    </small>

                                    <strong id="paymentAccountBalance" class="ms-1">
                                        -
                                    </strong>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary">
                                Pay
                            </button>

                        </form>

                        <div id="paymentMessage" class="mt-3"></div>

                    </div>
                </div>

            </section>

            <!-- PROFILE -->

            <section id="profileSection" class="d-none">

                <h2>My Profile</h2>

                <p class="text-secondary">
                    View your personal information.
                </p>

                
                <div class="card shadow-sm">
                    <div class="card-body">

                        <h5 class="card-title mb-4">Personal Information</h5>

                        <div id="profileContent" class="row g-3">

                            <div class="col-md-6">
                                <strong>Name</strong>
                                <p id="profileName" class="text-secondary">-</p>
                            </div>

                            <div class="col-md-6">
                                <strong>NIF</strong>
                                <p id="profileNif" class="text-secondary">-</p>
                            </div>

                            <div class="col-md-6">
                                <strong>Email</strong>
                                <p id="profileEmail" class="text-secondary">-</p>
                            </div>

                            <div class="col-md-6">
                                <strong>Phone Number</strong>
                                <p id="profilePhone" class="text-secondary">-</p>
                            </div>

                            <div class="col-md-6">
                                <strong>Status</strong>
                                <p id="profileStatus" class="text-secondary">-</p>
                            </div>

                        </div>
                    </div>
                </div>


            </section>

        </main>

    </div>

</div>
