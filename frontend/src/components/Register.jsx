import { useState } from 'react';

export default function Register({ onRegisterSuccess, onGoToLogin }) {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [error, setError] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        setError('');
        setMessage('');

        try {
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    name,
                    email,
                    password
                })
            });

            if (response.ok) {
                setMessage('Konto zostało utworzone! Możesz się teraz zalogować.');

                setTimeout(() => {
                    onRegisterSuccess();
                }, 2000);
            } else {
                const errorText = await response.text();
                setError(errorText || 'Rejestracja nie powiodła się.');
            }
        } catch (err) {
            console.error(err);
            setError('Brak połączenia z API Gateway.');
        }
    };

    return (
        <section className="card">
            <h2>Zarejestruj się</h2>

            {error && (
                <p style={{ color: '#ff4d4d', fontWeight: 'bold', marginBottom: '15px' }}>
                    {error}
                </p>
            )}

            {message && (
                <p style={{ color: '#4dff79', fontWeight: 'bold', marginBottom: '15px' }}>
                    {message}
                </p>
            )}

            <form onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="reg-name">Nazwa użytkownika</label>
                    <input
                        type="text"
                        id="reg-name"
                        required
                        minLength={2}
                        placeholder="Jan Kowalski"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="reg-email">E-mail</label>
                    <input
                        type="email"
                        id="reg-email"
                        required
                        placeholder="user@example.com"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="reg-password">Hasło</label>
                    <input
                        type="password"
                        id="reg-password"
                        required
                        minLength={8}
                        placeholder="Minimum 8 znaków"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>

                <button
                    type="submit"
                    className="btn-primary"
                    style={{ marginBottom: '10px' }}
                >
                    Utwórz konto
                </button>

                <button
                    type="button"
                    className="btn-secondary"
                    onClick={onGoToLogin}
                >
                    Masz już konto? Zaloguj się
                </button>
            </form>
        </section>
    );
}