import React, { useState, useEffect } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';
import { getProfessorTransactions, getProfessorBalance } from '../../services/professor';
import type { Transacao } from '../../types/aluno';
import { useNavigate } from 'react-router-dom';

export const Dashboard: React.FC = () => {
  const [saldo, setSaldo] = useState<number>(0);
  const [transacoes, setTransacoes] = useState<Transacao[]>([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchData() {
      try {
        setLoading(true);
        const [saldoRes, transacoesRes] = await Promise.all([
          getProfessorBalance(),
          getProfessorTransactions()
        ]);

        setSaldo(saldoRes);
        const validTransactions: Transacao[] = transacoesRes.filter((item: any) =>
          item && typeof item === 'object' && item.id && item.dataHora && item.valor !== undefined
        );

        setTransacoes(validTransactions);
        setErro('');
      } catch (err) {
        console.error('Erro ao carregar dados do professor:', err);
        setErro('Erro ao carregar dados do professor. Tente novamente.');
        if (err instanceof Error && err.message === 'Usuário não autenticado') {
          navigate('/login');
        }
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, [navigate]);

  const totalDistribuido = Array.isArray(transacoes) ? transacoes.reduce((sum, transaction) => sum + (typeof transaction.valor === 'number' ? transaction.valor : 0), 0) : 0;

  return (
    <MainLayout>
      <div className="space-y-6">
        {/* Cabeçalho */}
        <div>
          <h1 className="text-2xl font-bold text-dark-gray-100">Dashboard do Professor</h1>
          <p className="mt-1 text-dark-gray-400">Acompanhe suas moedas e distribuição</p>
        </div>

        {/* Cards de Resumo */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Card className="bg-gradient-to-br from-coin-green-600 to-coin-green-700">
            <div className="text-white">
              <p className="text-sm font-medium opacity-80">Saldo Disponível</p>
              <p className="text-4xl font-bold mt-2">{saldo.toFixed(2)} moedas</p>
            </div>
          </Card>

          <Card className="bg-gradient-to-br from-coin-green-600 to-coin-green-700">
            <div className="text-white">
              <p className="text-sm font-medium opacity-80">Total Distribuído</p>
              <p className="text-4xl font-bold mt-2">{totalDistribuido.toFixed(2)} moedas</p>
            </div>
          </Card>
        </div>

        {/* Últimas Transações */}
        <Card title="Últimas Transações">
          {loading ? (
            <div className="text-dark-gray-400">Carregando transações...</div>
          ) : erro ? (
            <div className="text-red-400">{erro}</div>
          ) : (
            <div className="divide-y divide-dark-gray-700">
              {Array.isArray(transacoes) && transacoes.slice(0, 5).map((transacao) => (
                <div key={transacao.id} className="py-4 first:pt-0 last:pb-0">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium text-dark-gray-200">{transacao.aluno?.nome || 'N/A'}</p>
                      <p className="text-sm text-dark-gray-400 mt-1">{transacao.motivoReconhecimento || 'Transferência'}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-coin-green-400 font-medium">
                        {typeof transacao.valor === 'number' ? transacao.valor.toFixed(2) : 'N/A'} moedas
                      </p>
                      <p className="text-sm text-dark-gray-400 mt-1">
                        {new Date(transacao.dataHora).toLocaleDateString('pt-BR')}
                      </p>
                    </div>
                  </div>
                </div>
              ))}
              {Array.isArray(transacoes) && transacoes.length === 0 && (
                <div className="text-dark-gray-400 py-4">Nenhuma transação encontrada.</div>
              )}
            </div>
          )}
        </Card>
      </div>
    </MainLayout>
  );
};