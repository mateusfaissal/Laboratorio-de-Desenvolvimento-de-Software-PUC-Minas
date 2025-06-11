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

  const isAluno = user?.tipo === 'ALUNO';
  const isProfessor = user?.tipo === 'PROFESSOR';
  const isEmpresa = user?.tipo === 'EMPRESA';

  const handleSignOut = () => {
    signOut();
    navigate('/login');
  };

  const menuItems = [
    { path: '/', label: 'Início' },
    ...(isAluno ? [
      { path: '/student/dashboard', label: 'Dashboard' },
      { path: '/student/my-advantages', label: 'Minhas Vantagens' },
      { path: '/student/cupons', label: 'Meus Cupons' },
    ] : []),
    ...(isProfessor ? [
      { path: '/professor/dashboard', label: 'Dashboard' },
      { path: '/professor/send-coins', label: 'Enviar Moedas' },
      { path: '/professor/statement', label: 'Extrato' },
      { path: '/cadastro/aluno', label: 'Cadastro de Aluno' },
      { path: '/cadastro/empresa', label: 'Cadastro de Empresa' },
    ] : []),
    ...(isEmpresa ? [
      { path: '/empresa/dashboard', label: 'Dashboard' },
      // { path: '/empresa/vantagens', label: 'Vantagens' },
      // { path: '/empresa/validar-cupom', label: 'Validar Cupom' },
    ] : []),
  ];

  return (
    <div className="min-h-screen bg-dark-gray-900">
      {/* Sidebar */}
      <aside className="fixed top-0 left-0 h-full w-64 bg-dark-gray-800 border-r border-dark-gray-700">
        <div className="p-6">
          <Link to={isProfessor ? "/professor/dashboard" : isEmpresa ? "/empresa/dashboard" : "/student/dashboard"}>
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