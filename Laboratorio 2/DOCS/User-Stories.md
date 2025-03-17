## 1. Realizar Cadastro

**Como** Usuário  
**Quero** realizar meu cadastro no sistema  
**Para** ter acesso às funcionalidades e criar pedidos de aluguel

### Critérios de Aceite
- O sistema deve permitir a inserção dos dados de identificação (RG, CPF, Nome, Endereço).
- Deve ser possível informar dados adicionais como profissão, entidade empregadora e até 3 rendimentos.
- O cadastro deve ser confirmado com uma mensagem de sucesso e os dados armazenados corretamente.

---

## 2. Realizar Login

**Como** Usuário  
**Quero** realizar login no sistema  
**Para** acessar as funcionalidades de acordo com meu perfil

### Critérios de Aceite
- O sistema deve validar o usuário e a senha.
- Se as credenciais forem válidas, o usuário deve ser direcionado para a tela inicial do seu perfil.
- Em caso de falha na autenticação, o sistema deve exibir uma mensagem apropriada e permitir nova tentativa.

---

## Pré-Requisito Comum

**Observação:**  
Todos os casos de uso a seguir exigem que o usuário esteja autenticado (login realizado).

---

## 3. Criar Pedido de Aluguel

**Como** Cliente  
**Quero** criar um pedido de aluguel de automóvel  
**Para** solicitar o serviço de aluguel e iniciar o processo de avaliação financeira

### Critérios de Aceite
- O sistema deve permitir a inserção dos dados necessários para o pedido.
- Deve haver validação dos campos obrigatórios.
- O pedido criado deve ficar disponível para consulta e futuras alterações.
- Se o pedido incluir a opção de financiamento, deverá permitir a associação de um contrato de crédito (ver história 11).

---

## 4. Consultar Pedido de Aluguel

**Como** Cliente ou Agente  
**Quero** consultar os pedidos de aluguel  
**Para** verificar o status e os detalhes dos pedidos realizados

### Critérios de Aceite
- O sistema deve exibir uma lista dos pedidos com seus respectivos status.
- O usuário deve conseguir acessar os detalhes completos de cada pedido.

---

## 5. Modificar Pedido de Aluguel (Cliente)

**Como** Cliente  
**Quero** modificar um pedido de aluguel  
**Para** corrigir ou atualizar informações antes da execução do contrato

### Critérios de Aceite
- O sistema deve permitir a alteração dos dados de um pedido previamente consultado.
- Somente pedidos em fase de análise ou pendentes podem ser modificados.
- As alterações devem ser validadas e confirmadas com uma mensagem de sucesso.
- É necessário consultar o pedido antes de realizar as modificações.

---

## 6. Cancelar Pedido de Aluguel

**Como** Cliente  
**Quero** cancelar um pedido de aluguel  
**Para** desistir do serviço ou corrigir um pedido que não atende às minhas necessidades

### Critérios de Aceite
- A ação de cancelamento deve ocorrer após a consulta dos detalhes do pedido.
- Deve ser exibida uma mensagem de confirmação do cancelamento.

---

## 7. Modificar Pedido de Aluguel (Agente)

**Como** Agente  
**Quero** modificar os pedidos de aluguel  
**Para** corrigir informações ou ajustar dados conforme as políticas internas

### Critérios de Aceite
- O sistema deve permitir que o agente edite os pedidos.
- O agente deve primeiro consultar os detalhes do pedido antes de realizar modificações.
---

## 8. Avaliar Pedido de Aluguel

**Como** Agente  
**Quero** avaliar os pedidos de aluguel  
**Para** verificar a viabilidade financeira e a conformidade dos dados apresentados

### Critérios de Aceite
- O sistema deve apresentar todos os dados do pedido para análise.
- O agente deve poder registrar um parecer financeiro (positivo ou negativo) para cada pedido.
- A avaliação deve influenciar o fluxo do pedido, encaminhando-o para aprovação ou recusa.

---

## 9. Aprovar/Recusar Pedido de Aluguel

**Como** Agente  
**Quero** aprovar ou recusar os pedidos de aluguel  
**Para** definir se o contrato de aluguel será executado

### Critérios de Aceite
- O sistema deve permitir que o agente registre a decisão de aprovação ou recusa do pedido.
- A decisão deve ser baseada na avaliação financeira e nos critérios de risco.
- O status do pedido deve ser atualizado conforme a decisão tomada.
- Este caso de uso se estende ao fluxo de avaliação do pedido.

---

## 10. Gerenciar Dados do Automóvel

**Como** Agente  
**Quero** gerenciar os dados dos automóveis  
**Para** manter as informações atualizadas e garantir a veracidade dos dados usados nos pedidos de aluguel

### Critérios de Aceite
- O sistema deve permitir a inclusão, alteração e remoção dos dados dos automóveis.
- Os dados a serem gerenciados incluem matrícula, ano, marca, modelo e placa.
- Qualquer alteração deve ser refletida imediatamente nas consultas e pedidos relacionados.

---

## 11. Associar Pedido com Contrato de Crédito

**Como** Cliente  
**Quero** associar um contrato de crédito ao meu pedido de aluguel  
**Para** viabilizar o financiamento do aluguel por meio de um banco, quando aplicável

### Critérios de Aceite
- O sistema deve permitir a associação opcional de um contrato de crédito durante a criação do pedido.
- Ao selecionar a opção de financiamento, o cliente deve conseguir vincular os dados do contrato ao pedido.
- A associação deve ser validada e registrada para consulta futura.
