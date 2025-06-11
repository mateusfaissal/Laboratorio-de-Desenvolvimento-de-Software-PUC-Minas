import type { Student, Transaction } from '../types';
import api from './api';

export async function getStudentProfile(): Promise<Student> {
  const response = await api.get<Student>('/api/alunos/me');
  return response.data;
}

export async function getStudentBalance(): Promise<number> {
  const response = await api.get<{ saldo: number }>('/api/alunos/me/saldo');
  return response.data.saldo;
}

export async function getStudentTransactions(): Promise<Transaction[]> {
  const response = await api.get<Transaction[]>('/api/transacoes/extrato');
  return response.data;
}