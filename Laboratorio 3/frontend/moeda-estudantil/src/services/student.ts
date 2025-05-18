import type { Student, Transaction } from '../types';
import api from './api';

export async function getStudentProfile(): Promise<Student> {
  const response = await api.get<Student>('/students/profile');
  return response.data;
}

export async function getStudentBalance(): Promise<number> {
  const response = await api.get<{ balance: number }>('/students/balance');
  return response.data.balance;
}

export async function getStudentTransactions(): Promise<Transaction[]> {
  const response = await api.get<Transaction[]>('/students/transactions');
  return response.data;
}