import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import logo from '../../assets/logo.png';

export const Navbar: React.FC = () => {
  const { user, signOut } = useAuth();
  const location = useLocation();

  const isProfessor = user?.tipo === 'PROFESSOR';
  const isStudent = user?.tipo === 'ALUNO';
  const isEmpresa = user?.tipo === 'partner';

  return (
    <nav className="bg-blue-600 text-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex">
            <div className="flex-shrink-0 flex items-center">
              <Link 
                to={isProfessor ? "/professor/dashboard" : isStudent ? "/student/dashboard" : "/"} 
                className="flex items-center hover:opacity-90 transition-opacity"
              >
                <img src={logo} alt="Moeda Estudantil" className="h-12 w-auto" />
              </Link>
            </div>
            <div className="ml-10 flex items-center space-x-4">
              {!user && (
                <>
                  <Link to="/cadastro/aluno" className="px-3 py-2 rounded-md text-sm font-medium text-white hover:bg-blue-700">Cadastro Aluno</Link>
                  <Link to="/cadastro/empresa" className="px-3 py-2 rounded-md text-sm font-medium text-white hover:bg-blue-700">Cadastro Empresa</Link>
                </>
              )}
              {isProfessor && (
                <>
                  <Link
                    to="/professor/dashboard"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${location.pathname === '/professor/dashboard' ? 'bg-blue-800 text-white' : 'text-white hover:bg-blue-700'}`}
                  >
                    Dashboard
                  </Link>
                  <Link
                    to="/professor/send-coins"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${location.pathname === '/professor/send-coins' ? 'bg-blue-800 text-white' : 'text-white hover:bg-blue-700'}`}
                  >
                    Enviar Moedas
                  </Link>
                  <Link
                    to="/professor/statement"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${location.pathname === '/professor/statement' ? 'bg-blue-800 text-white' : 'text-white hover:bg-blue-700'}`}
                  >
                    Extrato
                  </Link>
                </>
              )}
              {isStudent && (
                <>
                  <Link
                    to="/student/dashboard"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${location.pathname === '/student/dashboard' ? 'bg-blue-800 text-white' : 'text-white hover:bg-blue-700'}`}
                  >
                    Dashboard
                  </Link>
                  <Link
                    to="/student/vantagens"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${location.pathname === '/student/vantagens' ? 'bg-blue-800 text-white' : 'text-white hover:bg-blue-700'}`}
                  >
                    Vantagens
                  </Link>
                  <Link
                    to="/student/cupons"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${location.pathname === '/student/cupons' ? 'bg-blue-800 text-white' : 'text-white hover:bg-blue-700'}`}
                  >
                    Meus Cupons
                  </Link>
                </>
              )}
              {isEmpresa && (
                <>
                  <Link
                    to="/empresa/vantagens"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${location.pathname === '/empresa/vantagens' ? 'bg-blue-800 text-white' : 'text-white hover:bg-blue-700'}`}
                  >
                    Vantagens
                  </Link>
                  <Link
                    to="/empresa/validar-cupom"
                    className={`px-3 py-2 rounded-md text-sm font-medium ${location.pathname === '/empresa/validar-cupom' ? 'bg-blue-800 text-white' : 'text-white hover:bg-blue-700'}`}
                  >
                    Validar Cupom
                  </Link>
                </>
              )}
            </div>
          </div>
          <div className="flex items-center">
            {user && (
              <div className="flex items-center ml-4">
                <span className="text-sm mr-4">Olá, {user.name}</span>
                <button
                  onClick={signOut}
                  className="bg-red-500 hover:bg-red-600 px-3 py-1 rounded-md text-sm font-medium"
                >
                  Sair
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};