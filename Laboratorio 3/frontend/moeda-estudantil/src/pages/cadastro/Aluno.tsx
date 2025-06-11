import { useState, useEffect } from 'react';
import type { ChangeEvent, FormEvent } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import api from '../../services/api';

interface Instituicao {
  id: string;
  nome: string;
}
interface Curso {
  id: string;
  nome: string;
}

export default function CadastroAluno() {
  const [form, setForm] = useState({
    nome: '',
    email: '',
    senha: '',
    cpf: '',
    rg: '',
    endereco: '',
    instituicaoId: '',
    cursoId: ''
  });
  const [instituicoes, setInstituicoes] = useState<Instituicao[]>([]);
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [mensagem, setMensagem] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    async function fetchInstituicoes() {
      const res = await api.get('/api/instituicoes');
      setInstituicoes(res.data);
    }
    fetchInstituicoes();
  }, []);

  useEffect(() => {
    async function fetchCursos() {
      if (form.instituicaoId) {
        const res = await api.get(`/api/instituicoes/${form.instituicaoId}/cursos`);
        console.log(res.data);
        setCursos(res.data);
      } else {
        setCursos([]);
      }
    }
    fetchCursos();
  }, [form.instituicaoId]);

  function handleChange(e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setLoading(true);
    setMensagem('');
    try {
      await api.post('/api/alunos/cadastro', {
        nome: form.nome,
        email: form.email,
        senha: form.senha,
        cpf: form.cpf,
        rg: form.rg,
        endereco: form.endereco,
        curso: { id: form.cursoId }
      });
      setMensagem('Cadastro realizado com sucesso!');
      setForm({ nome: '', email: '', senha: '', cpf: '', rg: '', endereco: '', instituicaoId: '', cursoId: '' });
    } catch (err) {
      setMensagem('Erro ao cadastrar aluno.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <MainLayout>
      <div className="max-w-lg mx-auto mt-8 bg-dark-gray-800 p-8 rounded-lg shadow-lg border border-dark-gray-700">
        <h2 className="text-2xl font-bold mb-6 text-dark-gray-100">Cadastro de Aluno</h2>
        <form className="space-y-4" onSubmit={handleSubmit}>
          <input className="input-field" name="nome" placeholder="Nome" value={form.nome} onChange={handleChange} required />
          <input className="input-field" name="email" placeholder="Email" type="email" value={form.email} onChange={handleChange} required />
          <input className="input-field" name="senha" placeholder="Senha" type="password" value={form.senha} onChange={handleChange} required />
          <input className="input-field" name="cpf" placeholder="CPF" value={form.cpf} onChange={handleChange} required />
          <input className="input-field" name="rg" placeholder="RG" value={form.rg} onChange={handleChange} required />
          <input className="input-field" name="endereco" placeholder="Endereço" value={form.endereco} onChange={handleChange} required />
          <select className="input-field" name="instituicaoId" value={form.instituicaoId} onChange={handleChange} required>
            <option value="">Selecione a Instituição</option>
            {instituicoes.map((i) => <option key={i.id} value={i.id}>{i.nome}</option>)}
          </select>
          <select className="input-field" name="cursoId" value={form.cursoId} onChange={handleChange} required>
            <option value="">Selecione o Curso</option>
            {cursos.map((c) => <option key={c.id} value={c.id}>{c.nome}</option>)}
          </select>
          <button className="btn btn-primary w-full" type="submit" disabled={loading}>{loading ? 'Cadastrando...' : 'Cadastrar'}</button>
        </form>
        {mensagem && <div className="mt-4 text-center text-coin-green-400">{mensagem}</div>}
      </div>
    </MainLayout>
  );
} 