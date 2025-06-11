import React, { useEffect, useState } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';
import { alunoService } from '../../services/alunoService'; // Importar o serviço do aluno
import type { ResgateCupom } from '../../types/aluno'; // Importar o tipo ResgateCupom
import { useNavigate } from 'react-router-dom';

export const MinhasVantagens: React.FC = () => {
  const [cupons, setCupons] = useState<ResgateCupom[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCupons = async () => {
      try {
        setLoading(true);
        const resgateCupons = await alunoService.buscarMeusCupons();
        setCupons(resgateCupons);
      } catch (err) {
        console.error('Erro ao carregar cupons:', err);
         if (err instanceof Error && err.message === 'Usuário não autenticado') {
          navigate('/login'); // Redireciona para login se não autenticado
        } else {
           setError('Erro ao carregar suas vantagens resgatadas. Tente novamente.');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchCupons();
  }, [navigate]); // Adicionar navigate como dependência do useEffect

  if (loading) {
    return (
      <MainLayout>
        <div className="flex items-center justify-center h-full">
          <p className="text-dark-gray-200">Carregando suas vantagens resgatadas...</p>
        </div>
      </MainLayout>
    );
  }

  if (error) {
      return (
          <MainLayout>
              <div className="p-4 bg-red-900/20 border border-red-500/50 rounded-md">
                  <p className="text-red-400">{error}</p>
              </div>
          </MainLayout>
      );
  }

  return (
    <MainLayout>
      <div className="space-y-6">
        <h1 className="text-2xl font-bold text-dark-gray-100">Minhas Vantagens</h1>
        <Card>
          {cupons.length === 0 ? (
            <p className="text-dark-gray-300">Você ainda não resgatou nenhuma vantagem.</p>
          ) : (
            <div className="space-y-4">
              {cupons.map((cupom) => (
                <div key={cupom.id} className="p-4 bg-dark-gray-700/50 rounded-lg">
                  <h3 className="text-lg font-semibold text-dark-gray-100">{cupom.vantagem.nome}</h3>
                  <p className="text-dark-gray-300 mt-1">{cupom.vantagem.descricao}</p>
                   {cupom.vantagem.fotoUrl && (
                     <img src={cupom.vantagem.fotoUrl} alt={cupom.vantagem.nome} className="w-full h-32 object-cover rounded mt-2" />
                   )}
                  <p className="text-sm text-dark-gray-400 mt-2">
                    Resgatado em: {new Date(cupom.dataUtilizacao).toLocaleDateString('pt-BR')}
                  </p>
                  <p className="text-sm text-dark-gray-400">
                    Valor: {cupom.valor.toFixed(2)} moedas
                  </p>
                   <p className="text-sm text-dark-gray-400">
                    Código do Cupom: <span className="font-mono text-coin-green-400">{cupom.codigoGerado}</span>
                  </p>
                  {/* Você pode adicionar um botão para "Usar Cupom" aqui, se necessário */}
                </div>
              ))}
            </div>
          )}
        </Card>
      </div>
    </MainLayout>
  );
}; 