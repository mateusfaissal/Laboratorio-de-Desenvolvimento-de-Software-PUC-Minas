import React, { useState } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';
import { Input } from '../../components/common/Input';
import { Button } from '../../components/common/Button';
import { vantagemService } from '../../services/vantagemService';
import type { CadastroVantagemDTO } from '../../types/vantagem';

export const Dashboard: React.FC = () => {
  const [formData, setFormData] = useState<CadastroVantagemDTO>({
    nome: '',
    descricao: '',
    custoMoedas: 0,
    fotoUrl: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'custoMoedas' ? Number(value) : value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      await vantagemService.cadastrarVantagem(formData);
      setSuccess('Vantagem cadastrada com sucesso!');
      setFormData({
        nome: '',
        descricao: '',
        custoMoedas: 0,
        fotoUrl: ''
      });
    } catch (err) {
      setError('Erro ao cadastrar vantagem. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <MainLayout>
      <div className="space-y-6">
        <h1 className="text-2xl font-bold text-dark-gray-100">Dashboard da Empresa</h1>
        
        <Card>
          <h2 className="text-xl font-semibold text-dark-gray-100 mb-4">Cadastrar Nova Vantagem</h2>
          
          {error && (
            <div className="mb-4 p-4 bg-red-900/20 border border-red-500/50 rounded-md">
              <p className="text-red-400">{error}</p>
            </div>
          )}
          
          {success && (
            <div className="mb-4 p-4 bg-green-900/20 border border-green-500/50 rounded-md">
              <p className="text-green-400">{success}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <Input
              label="Nome da Vantagem"
              type="text"
              name="nome"
              value={formData.nome}
              onChange={handleChange}
              required
            />

            <div>
              <label htmlFor="descricao" className="block text-sm font-medium text-dark-gray-300 mb-1">
                Descrição
              </label>
              <textarea
                id="descricao"
                name="descricao"
                value={formData.descricao}
                onChange={handleChange}
                required
                className="input-field min-h-[100px]"
                placeholder="Descreva a vantagem..."
              />
            </div>

            <Input
              label="Custo em Moedas"
              type="number"
              name="custoMoedas"
              value={formData.custoMoedas}
              onChange={handleChange}
              required
              min="1"
            />

            <Input
              label="URL da Foto (opcional)"
              type="text"
              name="fotoUrl"
              value={formData.fotoUrl}
              onChange={handleChange}
              placeholder="https://exemplo.com/foto.jpg"
            />

            <Button
              type="submit"
              disabled={loading}
              className="w-full"
            >
              {loading ? 'Cadastrando...' : 'Cadastrar Vantagem'}
            </Button>
          </form>
        </Card>
      </div>
    </MainLayout>
  );
}; 