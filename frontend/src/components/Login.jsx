import { useState } from 'react';

export default function Login({ onLogin }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email: email, password: password })
            });

            if (response.ok) {
                onLogin();
            } else if (response.status === 401 || response.status === 403) {
                setError('Błędne dane logowania!');
            } else {
                setError('Wystąpił błąd po stronie serwera.');
            }
        } catch (err) {
            console.error(err);
            setError('Brak połączenia z API Gateway. Sprawdź, czy Spring działa!');
        }
    };

    return (
        <section className="card">
            <h2>Zaloguj się</h2>
            {error && <p style={{ color: '#ff4d4d', fontWeight: 'bold', marginBottom: '15px' }}>{error}</p>}

            <form onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="email">E-mail</label>
                    <input
                        type="email"
                        id="email"
                        required
                        placeholder="user@example.com"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
                <div className="input-group">
                    <label htmlFor="password">Hasło główne</label>
                    <input
                        type="password"
                        id="password"
                        required
                        placeholder="••••••••"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button type="submit" className="btn-primary">Wejdź do sejfu</button>
            </form>
        </section>
    );
}