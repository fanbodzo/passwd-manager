import { useState } from 'react';

export default function Login({ onLogin, onGoToRegister }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [otp, setOtp] = useState('');

    const [otpStep, setOtpStep] = useState(false);
    const [error, setError] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email,
                    password
                })
            });

            if (response.ok) {
                setOtpStep(true);
            } else {
                setError('Błędny email lub hasło.');
            }
        } catch (err) {
            console.error(err);
            setError('Brak połączenia z API Gateway.');
        }
    };

    const handleVerifyOtp = async (e) => {
        e.preventDefault();
        setError('');

        try {
            const response = await fetch('/api/auth/login/verify', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email,
                    code: otp

                })
            });

            if (response.ok) {
                const token = await response.text();
                localStorage.setItem('token', token);
                onLogin();
            } else {
                setError('Niepoprawny lub wygasły kod OTP');
                setOtp('');
            }
        } catch (err) {
            console.error(err);
            setError('Błąd podczas weryfikacji OTP.');
        }
    };

    return (
        <section className="card">
            {!otpStep ? (
                <>
                    <h2>Zaloguj się</h2>

                    {error && (
                        <p style={{ color: '#ff4d4d', fontWeight: 'bold' }}>
                            {error}
                        </p>
                    )}

                    <form onSubmit={handleLogin}>
                        <div className="input-group">
                            <label>E-mail</label>
                            <input
                                type="email"
                                required
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>

                        <div className="input-group">
                            <label>Hasło</label>
                            <input
                                type="password"
                                required
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>

                        <button
                            type="submit"
                            className="btn-primary"
                            style={{ marginBottom: '10px' }}
                        >
                            Zaloguj
                        </button>

                        <button
                            type="button"
                            className="btn-secondary"
                            onClick={onGoToRegister}
                        >
                            Nie masz konta? Zarejestruj się
                        </button>
                    </form>
                </>
            ) : (
                <>
                    <h2>Weryfikacja OTP</h2>

                    <p>
                        Na Twój adres email został wysłany kod
                        weryfikacyjny.
                    </p>

                    {error && (
                        <p style={{ color: '#ff4d4d', fontWeight: 'bold' }}>
                            {error}
                        </p>
                    )}

                    <form onSubmit={handleVerifyOtp}>
                        <div className="input-group">
                            <label>Kod OTP</label>
                            <input
                                type="text"
                                required
                                value={otp}
                                onChange={(e) => setOtp(e.target.value)}
                                placeholder="123456"
                            />
                        </div>

                        <button
                            type="submit"
                            className="btn-primary"
                        >
                            Validate OTP
                        </button>
                        <button type="button" onClick={() => setOtpStep(false)}>
                            Cofnij
                        </button>
                        <button type="button" onClick={handleLogin}>
                            Wyślij kod ponownie
                        </button>
                    </form>
                </>
            )}
        </section>
    );
}