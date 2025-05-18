import React, { useState } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';

// Dados mockados
const MOCK_STUDENTS = [
  { id: '1', name: 'Maria Silva', email: 'maria.silva@aluno.com', course: 'Engenharia de Software' },
  { id: '2', name: 'João Santos', email: 'joao.santos@aluno.com', course: 'Ciência da Computação' },
  { id: '3', name: 'Ana Oliveira', email: 'ana.oliveira@aluno.com', course: 'Sistemas de Informação' },
  { id: '4', name: 'Pedro Costa', email: 'pedro.costa@aluno.com', course: 'Engenharia de Software' },
  { id: '5', name: 'Beatriz Lima', email: 'beatriz.lima@aluno.com', course: 'Ciência da Computação' },
];

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

  const filteredStudents = MOCK_STUDENTS.filter(student =>
    student.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    student.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Simula o envio
    setTimeout(() => {
      setSuccess(true);
      setFormData({ studentId: '', amount: 0, description: '' });
      setTimeout(() => setSuccess(false), 3000);
    }, 500);
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
                {filteredStudents.map((student) => (
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
                ))}
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