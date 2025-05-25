import { useLocation } from 'react-router-dom';
import { ListagemVantagens } from '../components/ListagemVantagens';
import { CadastroVantagem } from '../components/CadastroVantagem';
import { MainLayout } from '../components/layout/MainLayout';

export const Vantagens = () => {
  const location = useLocation();
  const isEmpresaParceira = location.pathname.startsWith('/empresa');

  return (
    <MainLayout>
      <div className="space-y-6">
        {/* Cabeçalho */}
        <div>
          <h1 className="text-2xl font-bold text-dark-gray-100">
            {isEmpresaParceira ? 'Cadastro de Vantagens' : 'Vantagens Disponíveis'}
          </h1>
          <p className="mt-1 text-dark-gray-400">
            {isEmpresaParceira 
              ? 'Cadastre novas vantagens para os alunos'
              : 'Explore e resgate vantagens com suas moedas'}
          </p>
        </div>

        {isEmpresaParceira ? (
          <CadastroVantagem />
        ) : (
          <ListagemVantagens />
        )}
      </div>
    </MainLayout>
  );
}; 