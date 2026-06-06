import { useState, useEffect } from 'react';
import { authFetch } from '../utils/authFetch';

export default function Vault({ onLogout }) {
    const [passwords, setPasswords] = useState([]);
    const [isAdding, setIsAdding] = useState(false);
    const [error, setError] = useState('');

    const [revealedPasswords, setRevealedPasswords] = useState({});

    const [domainName, setDomainName] = useState('');
    const [login, setLogin] = useState('');
    const [passwordValue, setPasswordValue] = useState('');
    const [pendingDelete, setPendingDelete] = useState(null);

    const fetchPasswords = async () => {
        try {
            const response = await authFetch('/api/vault/all',);
            if (response.ok) {
                const data = await response.json();
                setPasswords(data);
                setRevealedPasswords({});
            } else if (response.status === 401 || response.status === 403) {
                onLogout();
            } else {
                setError('Błąd pobierania haseł z serwera.');
            }
        } catch (err) {
            setError('Brak połączenia z API Gateway.');
        }
    };

    useEffect(() => {
        fetchPasswords();
    }, []);

    const handleRevealPassword = async (id) => {
        if (revealedPasswords[id]) {
            setRevealedPasswords(prev => {
                const newState = { ...prev };
                delete newState[id];
                return newState;
            });
            return;
        }

        try {
            const response = await authFetch(`/api/vault/${id}/password`);
            if (response.ok) {
                const decryptedPassword = await response.text();
                setRevealedPasswords(prev => ({ ...prev, [id]: decryptedPassword }));
            } else {
                setError('Nie udało się odszyfrować hasła.');
            }
        } catch (err) {
            setError('Błąd serwera przy odszyfrowywaniu.');
        }
    };

    const handleAddPassword = async (e) => {
        e.preventDefault();
        try {
            const response = await authFetch('/api/vault/add', {
                method: 'POST',
                body: JSON.stringify({
                    domainName,
                    login,
                    password: passwordValue
                })
            });

            if (response.ok) {
                setDomainName('');
                setLogin('');
                setPasswordValue('');
                setIsAdding(false);
                fetchPasswords();
            } else {
                setError('Nie udało się zapisać hasła.');
            }
        } catch (err) {
            setError('Błąd serwera podczas zapisywania.');
        }
    };

    const handleDeletePassword = async (id) => {
        try {
            const response = await authFetch(`/api/vault/delete/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                fetchPasswords();
            } else {
                setError('Nie udało się usunąć wpisu.');
            }
        } catch (err) {
            setError('Błąd podczas usuwania.');
        }
    };

    const copyToClipboard = (text) => {
        navigator.clipboard.writeText(text);
        alert('Hasło skopiowane do schowka!');
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        onLogout();
    };

    return (
        <section className="card" style={{ width: '100%' }}>
            <h2>Twoje Hasła</h2>
            {error && <p style={{ color: '#ff4d4d', fontWeight: 'bold', marginBottom: '15px' }}>{error}</p>}

            <ul id="password-list">
                {passwords.map((item) => (
                    <li key={item.id} style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>

                        <div style={{ display: 'flex', justifyContent: 'space-between', width: '100%', marginBottom: '10px' }}>
                            <div>
                                <strong style={{ display: 'block', fontSize: '1.1rem' }}>{item.domainName}</strong>
                                <small style={{ color: 'var(--text-muted)' }}>{item.login}</small>
                            </div>

                            <div style={{ display: 'flex', gap: '5px', alignItems: 'center' }}>
                                <button
                                    className="btn-secondary"
                                    onClick={() => handleRevealPassword(item.id)}
                                    title="Pokaż/Ukryj hasło"
                                >
                                    {revealedPasswords[item.id] ? '🙈' : '👁️'}
                                </button>

                                {/* Przycisk kopiowania pojawia się TYLKO jak hasło jest odszyfrowane */}
                                {revealedPasswords[item.id] && (
                                    <button
                                        className="btn-secondary"
                                        onClick={() => copyToClipboard(revealedPasswords[item.id])}>
                                        📋 Kopiuj
                                    </button>
                                )}

                                <button
                                    className="btn-secondary"
                                    style={{ color: '#ff4d4d', borderColor: '#4d0000' }}
                                    onClick={() => setPendingDelete(item.id)}>
                                    🗑️ Usuń
                                </button>
                            </div>
                        </div>

                        {/* Miejsce na wyświetlanie hasła (gwiazdki albo odszyfrowane) */}
                        <div style={{
                            backgroundColor: 'var(--bg-color)',
                            padding: '8px 12px',
                            borderRadius: '4px',
                            width: '100%',
                            fontFamily: 'monospace',
                            fontSize: '1.2rem',
                            letterSpacing: revealedPasswords[item.id] ? 'normal' : '3px'
                        }}>
                            {revealedPasswords[item.id] ? revealedPasswords[item.id] : '••••••••'}
                        </div>

                    </li>
                ))}

                {passwords.length === 0 && !error && (
                    <p style={{ textAlign: 'center', color: 'var(--text-muted)' }}>Brak zapisanych haseł w sejfie.</p>
                )}
            </ul>
            {pendingDelete && (
                <div
                    onClick={() => setPendingDelete(null)}
                    style={{ position:"fixed", inset:0, background:"rgba(0,0,0,0.6)",
                        display:"flex", alignItems:"center", justifyContent:"center", zIndex:999 }}>
                    <div
                        onClick={e => e.stopPropagation()}
                        style={{ background:"var(--card-bg)", border:"1px solid var(--border-color)",
                            borderRadius:"8px", padding:"28px", maxWidth:"360px", width:"90%" }}>
                        <h3 style={{ margin:"0 0 8px" }}>Usunąć wpis?</h3>
                        <p style={{ margin:"0 0 20px", color:"var(--text-muted)", fontSize:"14px" }}>
                            Tej operacji nie można cofnąć.
                        </p>
                        <div style={{ display:"flex", gap:"10px" }}>
                            <button
                                className="btn-secondary"
                                style={{ flex:1 }}
                                onClick={() => setPendingDelete(null)}>
                                Anuluj
                            </button>
                            <button
                                className="btn-secondary"
                                style={{ flex:1, color:"#ff4d4d", borderColor:"#4d0000" }}
                                onClick={() => { handleDeletePassword(pendingDelete); setPendingDelete(null); }}>
                                Usuń
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {isAdding ? (
                <form onSubmit={handleAddPassword} style={{ marginTop: '20px', padding: '15px', border: '1px solid var(--border-color)', borderRadius: '6px' }}>
                    <h3 style={{ marginBottom: '10px' }}>Dodaj nowy wpis</h3>
                    <div className="input-group">
                        <label>Domena / Serwis</label>
                        <input type="text" required value={domainName} onChange={(e) => setDomainName(e.target.value)} />
                    </div>
                    <div className="input-group">
                        <label>Login / E-mail</label>
                        <input type="text" required value={login} onChange={(e) => setLogin(e.target.value)} />
                    </div>
                    <div className="input-group">
                        <label>Hasło</label>
                        <input type="password" required value={passwordValue} onChange={(e) => setPasswordValue(e.target.value)} />
                    </div>
                    <button type="submit" className="btn-primary" style={{ marginBottom: '10px' }}>Zapisz w sejfie</button>
                    <button type="button" className="btn-secondary" onClick={() => setIsAdding(false)}>Anuluj</button>
                </form>
            ) : (
                <button className="btn-primary" style={{ marginTop: '20px' }} onClick={() => setIsAdding(true)}>
                    + Dodaj nowe hasło
                </button>
            )}

            <button className="btn-secondary" style={{ marginTop: '10px' }} onClick={handleLogout}>Wyloguj</button>
        </section>
    );
}