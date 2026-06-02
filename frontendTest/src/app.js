// ===== LOGIKA MOTYWÓW =====
function setTheme(themeName) {
    document.body.className = themeName;
    // Zapisujemy wybrany motyw w pamięci przeglądarki
    localStorage.setItem('savedTheme', themeName);
}

// Podczas ładowania strony sprawdzamy czy user nie miał zapisanego motywu
window.onload = () => {
    const savedTheme = localStorage.getItem('savedTheme') || 'theme-dark';
    setTheme(savedTheme);
};

// ===== LOGIKA LOGOWANIA (SYMULACJA) =====
document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault(); // Zapobiega odświeżeniu strony

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // ----- MIEJSCE NA TWOJE API GATEWAY -----
    // Jak będziesz spinał to z backendem, użyjesz czegoś takiego:
    /*
    fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email, password: password })
    })
    .then(response => response.json())
    .then(data => {
        // Zapisujesz token JWT i przechodzisz do sejfu
    });
    */

    // Na ten moment robimy symulację:
    document.getElementById('login-section').classList.add('hidden');
    document.getElementById('vault-section').classList.remove('hidden');
});

// ===== LOGIKA WYLOGOWANIA =====
function logout() {
    document.getElementById('vault-section').classList.add('hidden');
    document.getElementById('login-section').classList.remove('hidden');
    document.getElementById('login-form').reset();
}