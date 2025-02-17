# Histórias de Usuário - Sistema de Matrículas

## 1. Realizar Login

**Como** Usuário (Aluno, Professor ou Secretaria)  
**Quero** realizar login no sistema  
**Para** poder acessar as funcionalidades de acordo com meu perfil

### Critérios de Aceite
- O sistema deve validar o usuário e a senha.
- Se as credenciais forem válidas, o usuário deve ser direcionado para a tela inicial do seu perfil (Aluno, Professor ou Secretária).
- Em caso de falha na autenticação, o sistema deve exibir mensagem apropriada e permitir nova tentativa.

---

## 2. Realizar Matrícula

**Como** Aluno  
**Quero** efetuar a matrícula em disciplinas do próximo semestre  
**Para** garantir minha participação nas aulas e dar continuidade aos meus estudos

### Critérios de Aceite
- O aluno deve poder escolher até **4 disciplinas obrigatórias** e **2 disciplinas optativas**.
- O sistema deve verificar se **o período de matrículas** está aberto.
- Ao atingir **60 alunos inscritos** em uma disciplina, o sistema deve impedir novas matrículas naquela disciplina.
- O sistema deve registrar a matrícula de forma persistente.
- Ao concluir a matrícula, o sistema deve **notificar o sistema de cobranças** para geração dos boletos ou faturas.

---

## 3. Cancelar Matrícula

**Como** Aluno  
**Quero** cancelar uma matrícula feita anteriormente  
**Para** ajustar meu plano de disciplinas antes do fim do período de matrículas

### Critérios de Aceite
- Deve ser possível cancelar a matrícula de uma ou mais disciplinas **enquanto o período de matrículas estiver aberto**.
- O sistema deve atualizar a quantidade de alunos inscritos na disciplina.
- Caso uma disciplina volte a ter menos de 3 alunos após cancelamentos, deve ficar em estado de alerta para possível cancelamento final.
---

## 4. Consultar Matrícula

**Como** Aluno  
**Quero** consultar as disciplinas em que estou matriculado  
**Para** acompanhar meu status de inscrição e confirmar se tudo está correto

### Critérios de Aceite
- O sistema deve exibir todas as disciplinas em que o aluno está inscrito (obrigatórias e optativas).
- Deve mostrar o status de cada disciplina (matriculado, em espera, cancelada etc.).
- Deve apresentar informações de horário e carga horária.
- Caso a disciplina atinja o número mínimo (3) ou máximo (60) de alunos, o status deve refletir a situação (confirmada ou encerrada para matrículas).

---

## 5. Fechar Período de Matrículas e Verificar Disciplinas Ativas

**Como** Secretaria  
**Quero** encerrar o período de matrículas e verificar quais disciplinas permanecem ativas  
**Para** confirmar quais disciplinas terão aulas no próximo semestre

### Critérios de Aceite
- Ao encerrar o período, disciplinas com menos de **3 alunos matriculados** devem ser **canceladas**.
- Disciplinas com 3 ou mais alunos permanecem ativas.
- O sistema deve gerar uma notificação ou relatório final com o status de cada disciplina.
- Alunos matriculados em disciplinas canceladas devem ser notificados.

---

## 6. Visualizar Alunos Matriculados

**Como** Professor  
**Quero** visualizar a lista de alunos matriculados em minhas disciplinas  
**Para** saber quantos alunos estão inscritos e planejar as aulas

### Critérios de Aceite
- Deve ser possível selecionar uma disciplina para ver a lista de alunos.
- O sistema deve exibir informações básicas de cada aluno (nome, ID, e-mail).
- A lista deve estar atualizada conforme as últimas matrículas e cancelamentos.

---

## 7. Gerar Currículo (Histórico) por Semestre

**Como** Secretaria  
**Quero** gerar o currículo (ou histórico) de cada aluno  
**Para** ter um registro oficial das disciplinas cursadas, notas e situação acadêmica

### Critérios de Aceite
- Deve ser possível selecionar um aluno para gerar o histórico.
- O sistema deve exibir todas as disciplinas cursadas, com notas (quando disponíveis) e status (aprovado, reprovado, em curso).
---

## 8. Notificar Sistema de Cobranças

**Como** Sistema de Matrículas (processo interno)  
**Quero** notificar o sistema de cobranças sobre novas matrículas e cancelamentos  
**Para** que o aluno seja cobrado corretamente pelas disciplinas em que está efetivamente inscrito

### Critérios de Aceite
- A notificação deve ocorrer **automaticamente** sempre que uma matrícula é criada, alterada ou cancelada.
- Devem ser enviados os dados do aluno (ID, nome) e o valor correspondente às disciplinas inscritas.
- Em caso de falha na comunicação, deve haver registro de log e possibilidade de reenvio.
- O sistema de cobranças deve retornar um status de confirmação ou erro.

---

## 9. Manter Informações de Cursos e Disciplinas

**Como** Secretaria  
**Quero** cadastrar e atualizar cursos e disciplinas, incluindo suas características (nome, créditos, pré-requisitos etc.)  
**Para** garantir que o sistema reflita a estrutura acadêmica atualizada

### Critérios de Aceite
- Deve ser possível criar, editar ou remover cursos.
- Cada curso deve ter um nome, número de créditos e disciplinas associadas.
- Deve ser possível adicionar ou remover disciplinas de um curso, definindo se são obrigatórias ou optativas.
- O sistema deve bloquear a remoção de uma disciplina que esteja com matrículas ativas (exceto se for cancelada).

