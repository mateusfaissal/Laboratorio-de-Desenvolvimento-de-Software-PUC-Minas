import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import logo from '../../assets/logo.png';

interface MainLayoutProps {
  children: React.ReactNode;
}

export const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  const { user, signOut } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const isProfessor = user?.role === 'professor';

  const handleSignOut = () => {
    signOut();
    navigate('/login');
  };

  const menuItems = isProfessor
    ? [
        { path: '/professor/dashboard', label: 'Dashboard' },
        { path: '/professor/send-coins', label: 'Enviar Moedas' },
        { path: '/professor/statement', label: 'Extrato' },
      ]
    : [
        { path: '/student/dashboard', label: 'Dashboard' },
      ];

  return (
    <div className="min-h-screen bg-dark-gray-900">
      {/* Sidebar */}
      <aside className="fixed top-0 left-0 h-full w-64 bg-dark-gray-800 border-r border-dark-gray-700">
        <div className="p-6">
          <Link to={isProfessor ? "/professor/dashboard" : "/student/dashboard"}>
            <img src={logo} alt="Moeda Estudantil" className="h-16 w-auto hover:opacity-90 transition-opacity" />
          </Link>
        </div>
        
        <nav className="mt-6">
          {menuItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`flex items-center px-6 py-3 text-sm font-medium ${
                location.pathname === item.path
                  ? 'text-coin-green-400 bg-dark-gray-700'
                  : 'text-dark-gray-300 hover:text-coin-green-400 hover:bg-dark-gray-700'
              }`}
            >
              {item.label}
            </Link>
          ))}
        </nav>
      </aside>

      {/* Main content */}
      <div className="ml-64">
        {/* Header */}
        <header className="h-16 bg-dark-gray-800 border-b border-dark-gray-700 flex items-center justify-between px-6">
          <div className="flex items-center">
            <span className="text-dark-gray-300">
              Bem-vindo, <span className="text-coin-green-400 font-medium">{user?.name}</span>
            </span>
          </div>
          <button
            onClick={handleSignOut}
            className="px-4 py-2 text-sm font-medium text-dark-gray-300 hover:text-coin-green-400"
          >
            Sair
          </button>
        </header>

        {/* Page content */}
        <main className="p-6">
          {children}
        </main>
      </div>
    </div>
  );
}; 