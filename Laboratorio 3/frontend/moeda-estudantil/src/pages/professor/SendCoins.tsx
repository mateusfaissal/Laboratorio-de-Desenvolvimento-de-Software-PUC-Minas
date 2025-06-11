import React, { useState, useEffect } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';
import { getStudentList, sendCoins } from '../../services/professor';
import type { Student } from '../../types';

interface FormData {
  studentId: string;
  amount: number;
  description: string;
}

export const SendCoins: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
    studentId: '',
    amount: 0,
    description: '',
  });
  const [searchTerm, setSearchTerm] = useState('');
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchStudents() {
      try {
        const data = await getStudentList();
        setStudents(data);
      } catch (err) {
        setError('Erro ao carregar alunos.');
      } finally {
        setLoading(false);
      }
    }
    fetchStudents();
  }, []);

  const filteredStudents = students.filter(student =>
    (student.name?.toLowerCase() || '').includes(searchTerm.toLowerCase()) ||
    (student.email?.toLowerCase() || '').includes(searchTerm.toLowerCase())
  );

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    try {
      await sendCoins({
        aluno: {
          id: formData.studentId
        },
        valor: formData.amount,
        motivoReconhecimento: formData.description,
        tipo: 'TRANSFERENCIA'
      });
      setSuccess(true);
      setFormData({ studentId: '', amount: 0, description: '' });
      setTimeout(() => setSuccess(false), 3000);
    } catch (err) {
      setError('Erro ao enviar moedas.');
    }
  };

  return (
    <MainLayout>
      <div className="space-y-6">
        {/* Cabeçalho */}
        <div>
          <h1 className="text-2xl font-bold text-dark-gray-100">Enviar Moedas</h1>
          <p className="mt-1 text-dark-gray-400">Distribua moedas para seus alunos</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Lista de Alunos */}
          <Card title="Selecionar Aluno">
            <div className="space-y-4">
              <input
                type="text"
                placeholder="Buscar aluno..."
                className="input-field"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />

              <div className="space-y-2 max-h-96 overflow-y-auto">
                {loading ? (
                  <div className="text-dark-gray-400">Carregando alunos...</div>
                ) : error ? (
                  <div className="text-red-400">{error}</div>
                ) : filteredStudents.length === 0 ? (
                  <div className="text-dark-gray-400">Nenhum aluno encontrado.</div>
                ) : (
                  filteredStudents.map((student) => (
                    <div
                      key={student.id}
                      className={`p-4 rounded-lg cursor-pointer transition-colors duration-200 ${
                        formData.studentId === student.id
                          ? 'bg-coin-green-600 text-white'
                          : 'bg-dark-gray-700 hover:bg-dark-gray-600'
                      }`}
                      onClick={() => setFormData({ ...formData, studentId: student.id })}
                    >
                      <p className="font-medium">{student.name}</p>
                      <p className="text-sm opacity-80">{student.email}</p>
                      <p className="text-sm opacity-80">{student.course}</p>
                    </div>
                  ))
                )}
              </div>
            </div>
          </Card>

          {/* Formulário de Envio */}
          <Card title="Detalhes do Envio">
            <form onSubmit={handleSubmit} className="space-y-6">
              <div>
                <label htmlFor="amount" className="input-label">
                  Quantidade de Moedas
                </label>
                <input
                  type="number"
                  id="amount"
                  min="1"
                  className="input-field"
                  value={formData.amount || ''}
                  onChange={(e) => setFormData({ ...formData, amount: parseInt(e.target.value) || 0 })}
                  required
                />
              </div>

              <div>
                <label htmlFor="description" className="input-label">
                  Motivo do Envio
                </label>
                <textarea
                  id="description"
                  rows={4}
                  className="input-field"
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  required
                  placeholder="Descreva o motivo do envio das moedas..."
                />
              </div>

              {success && (
                <div className="rounded-lg bg-coin-green-600/20 border border-coin-green-500/50 p-4">
                  <p className="text-coin-green-400 text-sm">
                    Moedas enviadas com sucesso!
                  </p>
                </div>
              )}
              {error && !loading && (
                <div className="rounded-lg bg-red-900/20 border border-red-500/50 p-4">
                  <p className="text-red-400 text-sm">{error}</p>
                </div>
              )}

              <button
                type="submit"
                disabled={!formData.studentId || !formData.amount || !formData.description}
                className={`w-full py-2.5 px-4 rounded-lg text-sm font-medium transition-colors duration-200 ${
                  formData.studentId && formData.amount && formData.description
                    ? 'bg-coin-green-600 text-white hover:bg-coin-green-700'
                    : 'bg-dark-gray-700 text-dark-gray-400 cursor-not-allowed'
                }`}
              >
                Enviar Moedas
              </button>
            </form>
          </Card>
        </div>
      </div>
    </MainLayout>
  );
};