import React, { useState, useEffect } from 'react';
import type { FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { Input } from '../../components/common/Input';
import { Button } from '../../components/common/Button';
import { Card } from '../../components/common/Card';
import { useAuth } from '../../hooks/useAuth';
import logo from '../../assets/logo.png';

export const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  
  const { signIn, isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated && user) {
      if (user.tipo === 'PROFESSOR') {
        navigate('/professor/dashboard');
      } else if (user.tipo === 'ALUNO') {
        navigate('/student/dashboard');
      } else if (user.tipo === 'EMPRESA') {
        navigate('/empresa/dashboard');
      }
    }
  }, [isAuthenticated, user, navigate]);

  async function handleSubmit(event: FormEvent): Promise<void> {
    event.preventDefault();
    setError('');
    
    if (!email || !senha) {
      setError('Preencha todos os campos');
      return;
    }

    try {
      setIsLoading(true);
      await signIn({ email, senha });
      // O redirecionamento será feito automaticamente pelo useEffect
    } catch (err) {
      setError('Email ou senha incorretos. Tente novamente.');
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-dark-gray-900 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div>
          <div className="flex justify-center">
            <img src={logo} alt="Moeda Estudantil" className="h-24 w-auto" />
          </div>
          <p className="mt-2 text-center text-sm text-dark-gray-400">
            Faça login para acessar a plataforma
          </p>
        </div>

        {/* Mensagem de credenciais de teste */}
        <div className="rounded-md bg-dark-gray-800 border border-dark-gray-700 p-4">
          <div className="flex">
            <div className="ml-3">
              <p className="text-sm font-medium text-coin-green-400">Credenciais para teste:</p>
              <p className="text-sm text-dark-gray-300 mt-1">
                Professor: professor@teste.com<br />
                Estudante: estudante@teste.com<br />
                Empresa: empresa@teste.com<br />
                Senha: 123456
              </p>
            </div>
          </div>
        </div>

        {error && (
          <div className="rounded-md bg-red-900/20 border border-red-500/50 p-4">
            <div className="flex">
              <div className="ml-3">
                <p className="text-sm text-red-400">{error}</p>
              </div>
            </div>
          </div>
        )}

        <div className="mt-8 bg-dark-gray-800 py-8 px-4 shadow-xl rounded-lg border border-dark-gray-700 sm:px-10">
          <form className="space-y-6" onSubmit={handleSubmit}>
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-dark-gray-300">
                Email
              </label>
              <div className="mt-1">
                <input
                  id="email"
                  name="email"
                  type="email"
                  required
                  value={email}
                  onChange={e => setEmail(e.target.value)}
                  className="input-field"
                  placeholder="seu@email.com"
                />
              </div>
            </div>

            <div>
              <label htmlFor="senha" className="block text-sm font-medium text-dark-gray-300">
                Senha
              </label>
              <div className="mt-1">
                <input
                  id="senha"
                  name="senha"
                  type="password"
                  required
                  value={senha}
                  onChange={e => setSenha(e.target.value)}
                  className="input-field"
                  placeholder="••••••"
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                disabled={isLoading}
                className={`w-full flex justify-center py-2.5 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-coin-green-600 hover:bg-coin-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-coin-green-500 ${
                  isLoading ? 'opacity-75 cursor-not-allowed' : ''
                }`}
              >
                {isLoading ? 'Entrando...' : 'Entrar'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};