import { useState } from 'react';
import type { CadastroVantagemDTO } from '../types/vantagem';
import { vantagemService } from '../services/vantagemService';
import { Card } from './common/Card';
import { Input } from './common/Input';
import { Button } from './common/Button';

export const CadastroVantagem = () => {
  const [vantagem, setVantagem] = useState<CadastroVantagemDTO>({
    nome: '',
    descricao: '',
    custoMoedas: 0,
    fotoUrl: ''
  });
  const [mensagem, setMensagem] = useState('');
  const [erro, setErro] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await vantagemService.cadastrarVantagem(vantagem);
      setMensagem('Vantagem cadastrada com sucesso!');
      setVantagem({ nome: '', descricao: '', custoMoedas: 0, fotoUrl: '' });
      setErro('');
    } catch (error) {
      setErro('Erro ao cadastrar vantagem. Por favor, tente novamente.');
      setMensagem('');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setVantagem(prev => ({
      ...prev,
      [name]: name === 'custoMoedas' ? Number(value) : value
    }));
  };

  return (
    <Card>
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input
          label="Nome da Vantagem"
          type="text"
          name="nome"
          value={vantagem.nome}
          onChange={handleChange}
          required
        />

        <div>
          <label htmlFor="descricao" className="block text-sm font-medium text-dark-gray-200">
            Descrição
          </label>
          <textarea
            id="descricao"
            name="descricao"
            value={vantagem.descricao}
            onChange={handleChange}
            className="mt-1 block w-full rounded-md border-dark-gray-700 bg-dark-gray-800 text-dark-gray-100 shadow-sm focus:border-coin-green-500 focus:ring-coin-green-500"
            rows={3}
            required
          />
        </div>

        <Input
          label="Custo em Moedas"
          type="number"
          name="custoMoedas"
          value={vantagem.custoMoedas}
          onChange={handleChange}
          min="1"
          required
        />

        <Input
          label="URL da Foto (opcional)"
          type="text"
          name="fotoUrl"
          value={vantagem.fotoUrl}
          onChange={handleChange}
        />

        {mensagem && (
          <div className="text-coin-green-500 text-sm">{mensagem}</div>
        )}

        {erro && (
          <div className="text-red-500 text-sm">{erro}</div>
        )}

        <Button type="submit" variant="primary">
          Cadastrar Vantagem
        </Button>
      </form>
    </Card>
  );
}; 