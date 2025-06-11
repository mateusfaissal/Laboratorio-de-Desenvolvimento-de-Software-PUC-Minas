import React, { useEffect, useState } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';
import { alunoService } from '../../services/alunoService';
import { vantagemService } from '../../services/vantagemService';
import type { Aluno, Transacao, Vantagem } from '../../types/aluno';
import { useNavigate } from 'react-router-dom';
import { Button } from '../../components/common/Button';

export const Dashboard: React.FC = () => {
  const [aluno, setAluno] = useState<Aluno | null>(null);
  const [transacoes, setTransacoes] = useState<Transacao[]>([]);
  const [vantagens, setVantagens] = useState<Vantagem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [resgatandoId, setResgatandoId] = useState<string | null>(null);
  const navigate = useNavigate();

  const carregarDados = async () => {
    try {
      setLoading(true);
      const [dadosAluno, listaTransacoesBruta, listaVantagensBruta] = await Promise.all([
        alunoService.buscarMeusDados(),
        alunoService.listarTransacoes(),
        alunoService.listarVantagens() // Esta é a lista inconsistente
      ]);

      const vantagensValidas: Vantagem[] = [];
      const processarItemVantagem = (item: any) => {
          // Verifica se o item tem as propriedades mínimas de uma Vantagem
          if (item && typeof item === 'object' && item.id && item.nome && item.custoMoedas !== undefined && item.descricao !== undefined) {
              // Garante que a propriedade empresa é um objeto, mesmo que incompleto, para evitar erros no ?.nome
              const empresaObj = (item.empresa && typeof item.empresa === 'object' && item.empresa.id) 
                                 ? item.empresa 
                                 : (typeof item.empresa === 'string' ? { id: item.empresa, nome: 'Empresa Desconhecida' } : { id: 'desconhecido', nome: 'Empresa Desconhecida' });

              vantagensValidas.push({
                  id: item.id,
                  nome: item.nome,
                  descricao: item.descricao,
                  custoMoedas: item.custoMoedas,
                  fotoUrl: item.fotoUrl || null, // Trata fotoUrl que pode vir como "" ou null
                  empresa: empresaObj // Usa o objeto empresa processado
              });
          }
      };


      // Processa os itens do array principal
      listaVantagensBruta.forEach((item: any) => {
          processarItemVantagem(item);
           // Verifica se existe um array de vantagens aninhado (como no seu exemplo dentro da empresa)
          if (item && item.empresa && typeof item.empresa === 'object' && item.empresa.vantagens && Array.isArray(item.empresa.vantagens)) {
              item.empresa.vantagens.forEach((subItem: any) => {
                  processarItemVantagem(subItem);
              });
          }
      });

      // Remove duplicatas baseadas no ID
      const vantagensUnicasMap = new Map<string, Vantagem>();
      vantagensValidas.forEach(vantagem => vantagensUnicasMap.set(vantagem.id, vantagem));
      const vantagensProcessadas = Array.from(vantagensUnicasMap.values());


      console.log('Vantagens processadas:', vantagensProcessadas);

      setAluno(dadosAluno);
      setTransacoes(listaTransacoesBruta);
      setVantagens(vantagensProcessadas); // Usar a lista processada

    } catch (err) {
      console.error('Erro ao carregar dados:', err);
      if (err instanceof Error && err.message === 'Usuário não autenticado') {
        navigate('/login');
      } else {
        setError('Erro ao carregar dados. Tente novamente.');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    carregarDados();
  }, [navigate]);

  const handleResgatar = async (vantagemId: string, custoMoedas: number) => {
      if (!aluno) return; // Não resgatar se o aluno não estiver carregado

      if (aluno.saldoMoedas < custoMoedas) {
          alert('Saldo insuficiente para resgatar esta vantagem.');
          return;
      }

      setResgatandoId(vantagemId);
      try {
          // Chamar o serviço de resgate
          await vantagemService.resgatarVantagem(vantagemId);
          alert('Vantagem resgatada com sucesso!');
          
          // Recarregar dados para atualizar o saldo e a lista de vantagens (opcional, dependendo da necessidade)
          // Melhor só atualizar o saldo localmente para dar feedback rápido
          setAluno(prevAluno => prevAluno ? { ...prevAluno, saldoMoedas: prevAluno.saldoMoedas - custoMoedas } : null);

          // Redirecionar para a página de vantagens resgatadas
          navigate('/student/my-advantages'); // Ajuste a rota conforme necessário

      } catch (error) {
          console.error('Erro ao resgatar vantagem:', error);
          alert('Erro ao resgatar vantagem. Tente novamente.');
      } finally {
          setResgatandoId(null);
      }
  };

  if (loading) {
    return (
      <MainLayout>
        <div className="flex items-center justify-center h-full">
          <p className="text-dark-gray-200">Carregando...</p>
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

  if (!aluno) {
    return (
      <MainLayout>
        <div className="p-4 bg-yellow-900/20 border border-yellow-500/50 rounded-md">
          <p className="text-yellow-400">Nenhum dado encontrado.</p>
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="space-y-6">
        <h1 className="text-2xl font-bold text-dark-gray-100">Dashboard do Aluno</h1>

        {/* Saldo de Moedas */}
        <Card>
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-xl font-semibold text-dark-gray-100">Saldo de Moedas</h2>
              <p className="text-3xl font-bold text-coin-green-500 mt-2">
                {aluno.saldoMoedas.toFixed(2)} moedas
              </p>
            </div>
          </div>
        </Card>

        {/* Últimas Transações */}
        {/* <Card>
          <h2 className="text-xl font-semibold text-dark-gray-100 mb-4">Últimas Transações</h2>
          <div className="space-y-4">
            {transacoes.length === 0 ? (
              <p className="text-dark-gray-300">Nenhuma transação encontrada.</p>
            ) : (
              transacoes.map(transacao => (
                <div
                  key={transacao.id}
                  className="flex items-center justify-between p-4 bg-dark-gray-700/50 rounded-lg"
                >
                  <div>
                    <p className="text-dark-gray-100">{transacao.descricao}</p>
                    <p className="text-sm text-dark-gray-300">
                      {new Date(transacao.dataHora).toLocaleDateString('pt-BR')}
                    </p>
                  </div>
                  <span
                    className={`font-semibold ${
                      transacao.tipo === 'TRANSFERENCIA' ? 'text-coin-green-500' : 'text-red-500'
                    }`}
                  >
                    {transacao.tipo === 'TRANSFERENCIA' ? '+' : '-'}
                    {transacao.valor} moedas
                  </span>
                </div>
              ))
            )}
          </div>
        </Card> */}

        {/* Vantagens Disponíveis */}
        <Card>
          <h2 className="text-xl font-semibold text-dark-gray-100 mb-4">Vantagens Disponíveis</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {vantagens.length === 0 ? (
              <p className="text-dark-gray-300">Nenhuma vantagem disponível.</p>
            ) : (
              vantagens.map(vantagem => {
                return (
                <div
                  key={vantagem.id}
                  className="bg-dark-gray-700/50 rounded-lg overflow-hidden"
                >
                  {vantagem.fotoUrl && (
                    <img
                      src={vantagem.fotoUrl}
                      alt={vantagem.nome}
                      className="w-full h-48 object-cover"
                    />
                  )}
                  <div className="p-4">
                    <h3 className="text-lg font-semibold text-dark-gray-100">
                      {vantagem.nome}
                    </h3>
                    <p className="text-dark-gray-300 mt-2">{vantagem.descricao}</p>
                    {/* Usamos optional chaining aqui para segurança */}
                    <p className="text-sm text-dark-gray-400 mt-1">
                      Empresa: {vantagem.empresa?.nome || 'Nome da Empresa Indisponível'}
                    </p>
                    <div className="mt-4 flex items-center justify-between">
                      <span className="text-coin-green-500 font-semibold">
                        {vantagem.custoMoedas} moedas
                      </span>
                      <Button
                        onClick={() => handleResgatar(vantagem.id, vantagem.custoMoedas)}
                        disabled={resgatandoId === vantagem.id || (aluno && aluno.saldoMoedas < vantagem.custoMoedas)}
                        isLoading={resgatandoId === vantagem.id}
                      >
                        {resgatandoId === vantagem.id ? 'Resgatando...' : 'Resgatar'}
                      </Button>
                    </div>
                  </div>
                </div>
              );
            })
            )}
          </div>
        </Card>
      </div>
    </MainLayout>
  );
};