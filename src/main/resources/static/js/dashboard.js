document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
        window.location.href = '/';
        return;
    }

    // Initial Load
    loadAccounts();
    loadTransactions();

    // Event Listeners
    document.getElementById('createAccountBtn').addEventListener('click', () => openModal('createAccountModal'));
    document.getElementById('transferBtn').addEventListener('click', () => openModal('transferModal'));
    document.getElementById('depositBtn').addEventListener('click', () => openModal('depositModal'));
    document.getElementById('withdrawBtn').addEventListener('click', () => openModal('withdrawModal'));

    document.querySelectorAll('.close-modal').forEach(btn => {
        btn.addEventListener('click', () => closeModal(btn.closest('.modal').id));
    });

    // Forms
    document.getElementById('createAccountForm').addEventListener('submit', handleCreateAccount);
    document.getElementById('transferForm').addEventListener('submit', handleTransfer);
    document.getElementById('depositForm').addEventListener('submit', handleDeposit);
    document.getElementById('withdrawForm').addEventListener('submit', handleWithdraw);
});

// --- API Calls ---

async function loadAccounts() {
    const userId = localStorage.getItem('userId');
    try {
        const response = await fetch(`/api/accounts/user/${userId}`);
        const accounts = await response.json();
        renderAccounts(accounts);
        updateAccountSelects(accounts);
        updateTotalBalance(accounts);
    } catch (error) {
        console.error('Error loading accounts:', error);
    }
}

async function loadTransactions() {
    const userId = localStorage.getItem('userId');
    // Fetch accounts first to get IDs, then fetch transactions for each
    // For simplicity in this demo, we might need an endpoint to get all user transactions
    // Or we just fetch for the first account or aggregate.
    // Let's aggregate for now.
    try {
        const accResponse = await fetch(`/api/accounts/user/${userId}`);
        const accounts = await accResponse.json();

        let allTransactions = [];
        for (const acc of accounts) {
            const txResponse = await fetch(`/api/transactions/${acc.accountId}`);
            const transactions = await txResponse.json();
            allTransactions = allTransactions.concat(transactions);
        }

        // Sort by date desc
        allTransactions.sort((a, b) => new Date(b.transactionDate) - new Date(a.transactionDate));
        renderTransactions(allTransactions);
    } catch (error) {
        console.error('Error loading transactions:', error);
    }
}

async function handleCreateAccount(e) {
    e.preventDefault();
    const userId = localStorage.getItem('userId');
    const type = document.getElementById('accountType').value;

    try {
        const response = await fetch('/api/accounts/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId, type })
        });

        if (response.ok) {
            closeModal('createAccountModal');
            loadAccounts();
            showNotification('Account created successfully!', 'success');
        } else {
            showNotification('Failed to create account', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

async function handleTransfer(e) {
    e.preventDefault();
    const fromAccountId = document.getElementById('transferFrom').value;
    const toAccountNumber = document.getElementById('transferTo').value;
    const amount = document.getElementById('transferAmount').value;

    try {
        const response = await fetch('/api/transfers/transfer', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ fromId: fromAccountId, toNumber: toAccountNumber, amount })
        });

        if (response.ok) {
            closeModal('transferModal');
            loadAccounts();
            loadTransactions();
            showNotification('Transfer successful!', 'success');
        } else {
            const text = await response.text();
            showNotification(text, 'error');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

async function handleDeposit(e) {
    e.preventDefault();
    const accountId = document.getElementById('depositAccount').value;
    const amount = document.getElementById('depositAmount').value;

    try {
        const response = await fetch('/api/transactions/deposit', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ accountId, amount })
        });

        if (response.ok) {
            closeModal('depositModal');
            loadAccounts();
            loadTransactions();
            showNotification('Deposit successful!', 'success');
        } else {
            showNotification('Deposit failed', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

async function handleWithdraw(e) {
    e.preventDefault();
    const accountId = document.getElementById('withdrawAccount').value;
    const amount = document.getElementById('withdrawAmount').value;

    try {
        const response = await fetch('/api/transactions/withdraw', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ accountId, amount })
        });

        if (response.ok) {
            closeModal('withdrawModal');
            loadAccounts();
            loadTransactions();
            showNotification('Withdrawal successful!', 'success');
        } else {
            const text = await response.text();
            showNotification(text, 'error');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// --- UI Functions ---

function renderAccounts(accounts) {
    const grid = document.getElementById('accountsGrid');
    grid.innerHTML = '';

    accounts.forEach(acc => {
        const card = document.createElement('div');
        card.className = 'account-card';
        // Add interest rate badge if > 0
        const interestBadge = acc.interestRate > 0
            ? `<div class="interest-badge">${(acc.interestRate * 100).toFixed(1)}% APY</div>`
            : '';

        card.innerHTML = `
            <div class="account-type">${acc.accountType}</div>
            <div class="account-number">${acc.accountNumber}</div>
            <div class="balance">$${acc.balance.toFixed(2)}</div>
            ${interestBadge}
        `;
        grid.appendChild(card);
    });
}

function renderTransactions(transactions) {
    const tbody = document.getElementById('transactionsBody');
    tbody.innerHTML = '';

    transactions.forEach(tx => {
        const row = document.createElement('tr');
        const date = new Date(tx.transactionDate).toLocaleDateString();
        const isPositive = tx.type === 'Deposit';
        const amountClass = isPositive ? 'amount-positive' : 'amount-negative';
        const prefix = isPositive ? '+' : '-';

        row.innerHTML = `
            <td>${date}</td>
            <td>${tx.description || tx.type}</td>
            <td>${tx.type}</td>
            <td class="${amountClass}">${prefix}$${tx.amount.toFixed(2)}</td>
        `;
        tbody.appendChild(row);
    });
}

function updateAccountSelects(accounts) {
    const selects = ['transferFrom', 'depositAccount', 'withdrawAccount'];
    selects.forEach(id => {
        const select = document.getElementById(id);
        if (!select) return;

        const currentVal = select.value;
        select.innerHTML = '';

        accounts.forEach(acc => {
            const option = document.createElement('option');
            option.value = acc.accountId;
            option.textContent = `${acc.accountType} - ${acc.accountNumber} ($${acc.balance.toFixed(2)})`;
            select.appendChild(option);
        });

        if (currentVal) select.value = currentVal;
    });
}

function updateTotalBalance(accounts) {
    const total = accounts.reduce((sum, acc) => sum + acc.balance, 0);
    document.getElementById('totalBalance').textContent = `$${total.toFixed(2)}`;
}

function openModal(modalId) {
    document.getElementById(modalId).classList.add('active');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
}

function showNotification(message, type = 'info') {
    // Simple alert for now, or custom toast
    alert(message);
}
