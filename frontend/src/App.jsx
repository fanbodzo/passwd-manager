import { useState, useEffect } from 'react';
import Login from './components/Login';
import Register from './components/Register';
import Vault from './components/Vault';

function App() {
  const [currentView, setCurrentView] = useState('login');
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const [theme, setTheme] = useState(
      localStorage.getItem('savedTheme') || 'theme-dark'
  );

  // 🔥 INIT: sprawdź czy user ma token
  useEffect(() => {
    const token = localStorage.getItem('token');

    if (token) {
      setIsAuthenticated(true);
      setCurrentView('vault');
    }
  }, []);

  // theme handler
  useEffect(() => {
    document.body.className = theme;
    localStorage.setItem('savedTheme', theme);
  }, [theme]);

  // 🔐 LOGIN SUCCESS (po OTP verify)
  const handleLogin = () => {
    setIsAuthenticated(true);
    setCurrentView('vault');
  };

  // 🚪 LOGOUT
  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
    setCurrentView('login');
  };

  return (
      <div className="container">
        <header>
          <h1>🔒 VaultManager</h1>

          <div className="theme-switcher">
            <button onClick={() => setTheme('theme-light')}>Jasny</button>
            <button onClick={() => setTheme('theme-dark')}>Ciemny</button>
            <button onClick={() => setTheme('theme-czerwony')} className="czerwony-btn">
              Czerwony
            </button>
            <button onClick={() => setTheme('theme-zielony')} className="zielony-btn">
              Zielony
            </button>
          </div>
        </header>

        <main>
          {currentView === 'login' && (
              <Login
                  onLogin={() => setCurrentView('vault')}
                  onGoToRegister={() => setCurrentView('register')}
              />
          )}

          {currentView === 'register' && (
              <Register
                  onRegisterSuccess={() => setCurrentView('login')}
                  onGoToLogin={() => setCurrentView('login')}
              />
          )}

          {currentView === 'vault' && (
              <Vault onLogout={() => setCurrentView('login')} />
          )}
        </main>
      </div>
  );
}

export default App;