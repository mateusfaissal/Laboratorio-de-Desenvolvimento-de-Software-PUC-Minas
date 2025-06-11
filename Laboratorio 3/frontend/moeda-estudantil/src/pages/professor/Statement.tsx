import React, { useState, useEffect } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';
import { getProfessorTransactions } from '../../services/professor';
import type { Transacao } from '../../types/aluno';
import { useNavigate } from 'react-router-dom';

export const Statement: React.FC = () => {
  const [transactions, setTransactions] = useState<Transacao[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedMonth, setSelectedMonth] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const navigate = useNavigate();

  const fetchTransactions = async () => {
    try {
      setLoading(true);
      const data = await getProfessorTransactions();

      const validTransactions: Transacao[] = data.filter((item: any) =>
        item && typeof item === 'object' && item.id && item.dataHora && item.valor !== undefined
      );

      setTransactions(validTransactions);
      console.log('Transações processadas:', validTransactions);
      setError('');
    } catch (err) {
      console.error('Erro ao carregar extrato:', err);
      if (err instanceof Error && err.message === 'Usuário não autenticado') {
        navigate('/login');
      } else {
        setError('Erro ao carregar extrato.');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTransactions();
  }, [refreshTrigger, navigate]);

  const refreshStatement = () => {
    setRefreshTrigger(prev => prev + 1);
  };

  const filteredTransactions = transactions.filter(transaction => {
    const alunoNome = transaction.aluno?.nome || '';
    const motivo = transaction.motivoReconhecimento || '';

    const matchesSearch = alunoNome.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         motivo.toLowerCase().includes(searchTerm.toLowerCase());

    if (!selectedMonth) return matchesSearch;

    const transactionDate = new Date(transaction.dataHora);
    if (isNaN(transactionDate.getTime())) {
      console.warn('Data de transação inválida:', transaction.dataHora);
      return false;
    }
    const transactionMonth = transactionDate.getMonth();
    return matchesSearch && transactionMonth === parseInt(selectedMonth);
  });

  const totalMoedas = filteredTransactions.reduce((sum, transaction) => sum + (typeof transaction.valor === 'number' ? transaction.valor : 0), 0);

  return (
    <MainLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-2xl font-bold text-dark-gray-100">Extrato de Moedas</h1>
          <p className="mt-1 text-dark-gray-400">Histórico de moedas distribuídas</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="md:col-span-2">
            <div className="flex flex-col sm:flex-row gap-4">
              <div className="flex-1">
                <input
                  type="text"
                  placeholder="Buscar por aluno ou descrição..."
                  className="input-field"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
              <div className="w-full sm:w-48">
                <select
                  className="input-field"
                  value={selectedMonth}
                  onChange={(e) => setSelectedMonth(e.target.value)}
                >
                  <option value="">Todos os meses</option>
                  <option value="0">Janeiro</option>
                  <option value="1">Fevereiro</option>
                  <option value="2">Março</option>
                  <option value="3">Abril</option>
                  <option value="4">Maio</option>
                  <option value="5">Junho</option>
                  <option value="6">Julho</option>
                  <option value="7">Agosto</option>
                  <option value="8">Setembro</option>
                  <option value="9">Outubro</option>
                  <option value="10">Novembro</option>
                  <option value="11">Dezembro</option>
                </select>
              </div>
            </div>
          </Card>

          <Card className="bg-gradient-to-br from-coin-green-600 to-coin-green-700">
            <div className="text-white">
              <p className="text-sm font-medium opacity-80">Total Distribuído</p>
              <p className="text-3xl font-bold mt-1">{totalMoedas.toFixed(2)} moedas</p>
            </div>
          </Card>
        </div>

        <Card>
          {loading ? (
            <div className="text-dark-gray-400">Carregando extrato...</div>
          ) : error ? (
            <div className="text-red-400">{error}</div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-dark-gray-700">
                    <th className="text-left py-3 px-4 text-dark-gray-300 font-medium">Data</th>
                    <th className="text-left py-3 px-4 text-dark-gray-300 font-medium">Aluno</th>
                    <th className="text-left py-3 px-4 text-dark-gray-300 font-medium">Descrição</th>
                    <th className="text-right py-3 px-4 text-dark-gray-300 font-medium">Valor</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-dark-gray-700">
                  {filteredTransactions.map((transaction) => (
                    <tr key={transaction.id} className="hover:bg-dark-gray-700/50">
                      <td className="py-3 px-4 text-dark-gray-200">
                        {new Date(transaction.dataHora).toLocaleDateString('pt-BR')}
                      </td>
                      <td className="py-3 px-4 text-dark-gray-200">{transaction.aluno?.nome || 'N/A'}</td>
                      <td className="py-3 px-4 text-dark-gray-400">{transaction.motivoReconhecimento || 'Transferência'}</td>
                      <td className="py-3 px-4 text-right text-coin-green-400 font-medium">
                        {typeof transaction.valor === 'number' ? transaction.valor.toFixed(2) : 'N/A'} moedas
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </Card>
      </div>
    </MainLayout>
  );
};