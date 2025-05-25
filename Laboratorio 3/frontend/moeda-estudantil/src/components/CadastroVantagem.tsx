import { useState } from 'react';
import type { CadastroVantagemDTO } from '../types/vantagem';
import { vantagemService } from '../services/vantagemService';
import { Card } from './common/Card';

export const CadastroVantagem = () => {
  const [vantagem, setVantagem] = useState<CadastroVantagemDTO>({
    titulo: '',
    descricao: '',
    custo: 0,
    foto: ''
  });
  const [mensagem, setMensagem] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await vantagemService.cadastrarVantagem(vantagem);
      setMensagem('Vantagem cadastrada com sucesso!');
      setVantagem({ titulo: '', descricao: '', custo: 0, foto: '' });
    } catch (error) {
      setMensagem('Erro ao cadastrar vantagem.');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setVantagem(prev => ({
      ...prev,
      [name]: name === 'custo' ? Number(value) : value
    }));
  };

  return (
    <Card>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="input-label">Título</label>
          <input
            type="text"
            name="titulo"
            value={vantagem.titulo}
            onChange={handleChange}
            className="input-field"
            required
          />
        </div>

        <div>
          <label className="input-label">Descrição</label>
          <textarea
            name="descricao"
            value={vantagem.descricao}
            onChange={handleChange}
            className="input-field"
            rows={4}
            required
          />
        </div>

        <div>
          <label className="input-label">Custo (em moedas)</label>
          <input
            type="number"
            name="custo"
            value={vantagem.custo}
            onChange={handleChange}
            className="input-field"
            min="0"
            required
          />
        </div>

        <div>
          <label className="input-label">URL da Foto</label>
          <input
            type="url"
            name="foto"
            value={vantagem.foto}
            onChange={handleChange}
            className="input-field"
            required
          />
        </div>

        <button
          type="submit"
          className="btn btn-primary w-full"
        >
          Cadastrar Vantagem
        </button>
      </form>

      {mensagem && (
        <div className={`mt-4 p-3 rounded ${
          mensagem.includes('sucesso')
            ? 'bg-coin-green-900/20 border border-coin-green-500/50 text-coin-green-400'
            : 'bg-red-900/20 border border-red-500/50 text-red-400'
        }`}>
          {mensagem}
        </div>
      )}
    </Card>
  );
}; 