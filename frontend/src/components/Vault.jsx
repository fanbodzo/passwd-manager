export default function Vault({ onLogout }) {
    return (
        <section className="card">
            <h2>Twoje Hasła</h2>
            <ul id="password-list">
                <li>
                    <span>Facebook</span>
                    <button className="btn-secondary">Kopiuj</button>
                </li>
                <li>
                    <span>Konto Bankowe</span>
                    <button className="btn-secondary">Kopiuj</button>
                </li>
                <li>
                    <span>GitHub</span>
                    <button className="btn-secondary">Kopiuj</button>
                </li>
            </ul>
            <button className="btn-primary" style={{ marginTop: '20px' }}>+ Dodaj nowe hasło</button>
            <button className="btn-secondary" style={{ marginTop: '10px' }} onClick={onLogout}>Wyloguj</button>
        </section>
    );
}