# Sistema de Moeda Estudantil - Backend

Este é o backend do Sistema de Moeda Estudantil, uma plataforma que permite a gestão de uma moeda virtual para estudantes, onde professores podem conceder moedas aos alunos e estes podem resgatar vantagens oferecidas por empresas parceiras.

## Funcionalidades Principais

- Sistema de notificações por email
- Gestão de moedas estudantis
- Resgate de vantagens/cupons
- Integração com empresas parceiras
- Sistema de reenvio automático de notificações

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- Spring Mail
- H2 Database (banco de dados em memória para desenvolvimento)
- Lombok
- Maven
- Swagger/OpenAPI 3.0 (Documentação da API)

## Estrutura do Projeto

```
src/
├── main/
│   └── java/br/com/moeda_estudantil/
│       ├── config/         # Configurações do Spring
│       ├── controller/     # Controladores REST
│       ├── model/          # Entidades do sistema
│       ├── repository/     # Repositórios JPA
│       ├── service/        # Lógica de negócio
│       ├── dto/            # Objetos de transferência de dados
│       └── exception/      # Tratamento de exceções
└── test/
    └── java/br/com/moeda_estudantil/
        └── [testes unitários e de integração]
```

## Configuração

1. Clone o repositório
2. Execute o projeto usando Maven ou sua IDE preferida
3. O banco de dados H2 já está configurado para desenvolvimento
4. Acesse o console do H2 em: http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:moedadb
   - Username: sa
   - Password: password
5. Acesse a documentação da API em: http://localhost:8080/swagger-ui.html
   - Interface interativa com todos os endpoints
   - Possibilidade de testar as requisições diretamente
   - Documentação detalhada dos modelos de dados

### Configurações Sensíveis

#### Configuração do JWT Secret

O JWT secret é usado para assinar os tokens de autenticação. Para configurar:

1. Abra o arquivo `src/main/resources/application.properties`
2. Procure pela seção de configurações JWT
3. Edite a linha `app.jwt.secret` com sua chave secreta:

```properties
app.jwt.secret=sua-chave-secreta-aqui
```

> **Importante:** A chave secreta deve ser uma string longa e complexa. Você pode gerar uma usando ferramentas online de geração de chaves ou comandos como `openssl rand -base64 512`.

#### Configuração do SMTP (Email)

Para configurar o serviço de email:

1. Abra o arquivo `src/main/resources/application.properties`
2. Procure pela seção de configurações de email
3. Edite as seguintes linhas com suas credenciais:

```properties
spring.mail.host=seu-servidor-smtp
spring.mail.port=sua-porta-smtp
spring.mail.username=seu-usuario-smtp
spring.mail.password=sua-senha-smtp
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.debug=true
```

> **Dica:** Para desenvolvimento, você pode usar serviços como Mailtrap.io que fornecem servidores SMTP de teste gratuitos.

## Endpoints Principais

### Autenticação

- `POST /api/auth/login` - Login de usuário (aluno, professor ou empresa)

### Alunos

- `GET /api/alunos` - Listar todos os alunos (apenas professores)
- `GET /api/alunos/{id}` - Buscar aluno por ID
- `POST /api/alunos/cadastro` - Cadastrar novo aluno
- `PUT /api/alunos/{id}` - Atualizar dados do aluno
- `DELETE /api/alunos/{id}` - Deletar aluno (apenas professores)
- `POST /api/alunos/{id}/adicionar-moedas` - Adicionar moedas ao aluno (apenas professores)

### Professores

- `GET /api/professores` - Listar todos os professores
- `GET /api/professores/{id}` - Buscar professor por ID
- `GET /api/professores/me` - Obter dados do professor autenticado
- `PUT /api/professores/{id}` - Atualizar dados do professor
- `POST /api/professores/enviar-moedas` - Enviar moedas para aluno
- `GET /api/professores/saldo` - Consultar saldo do professor
- `GET /api/professores/extrato` - Consultar extrato do professor

### Empresas Parceiras

- `GET /api/empresas` - Listar todas as empresas
- `GET /api/empresas/{id}` - Buscar empresa por ID
- `POST /api/empresas/cadastro` - Cadastrar nova empresa

### Vantagens

- `GET /api/vantagens` - Listar todas as vantagens
- `GET /api/vantagens/empresa/{empresaId}` - Listar vantagens por empresa
- `GET /api/vantagens/{id}` - Buscar vantagem por ID
- `POST /api/vantagens/empresa/{empresaId}` - Criar nova vantagem
- `POST /api/vantagens/{id}/foto` - Adicionar foto à vantagem
- `PUT /api/vantagens/{id}` - Atualizar vantagem
- `DELETE /api/vantagens/{id}` - Deletar vantagem
- `POST /api/vantagens/{id}/resgatar` - Resgatar vantagem (apenas alunos)

### Transações

- `GET /api/transacoes/extrato` - Consultar extrato do usuário autenticado
- `GET /api/transacoes/transferencias/aluno/{alunoId}` - Listar transferências de um aluno
- `GET /api/transacoes/cupons` - Listar cupons do usuário autenticado
- `GET /api/transacoes/cupons/empresa/{empresaId}` - Listar cupons pendentes da empresa
- `POST /api/transacoes/cupons/{cupomId}/validar` - Validar cupom (apenas empresas)

### Notificações

- `GET /api/notificacoes` - Listar todas as notificações (apenas professores)
- `GET /api/notificacoes/minhas` - Listar notificações do usuário autenticado

### Instituições e Cursos

- `GET /api/instituicoes` - Listar todas as instituições
- `GET /api/instituicoes/{id}` - Buscar instituição por ID
- `GET /api/instituicoes/{instituicaoId}/cursos` - Listar cursos de uma instituição
- `POST /api/instituicoes/{instituicaoId}/cursos` - Criar novo curso

### Departamentos

- `GET /api/departamentos` - Listar todos os departamentos
- `GET /api/departamentos/instituicao/{instituicaoId}` - Listar departamentos por instituição
- `GET /api/departamentos/{departamentoId}/professores` - Listar professores de um departamento

### Usuários

- `GET /api/usuarios` - Listar todos os usuários (apenas professores)
- `GET /api/usuarios/{id}` - Buscar usuário por ID
- `PUT /api/usuarios/{id}/senha` - Alterar senha do usuário

## Dados Iniciais

O sistema já vem com alguns dados pré-configurados para facilitar o desenvolvimento e testes. Estes dados são carregados automaticamente através da classe `DataInitializer.java` localizada em `src/main/java/br/com/moeda_estudantil/config/`.

Os dados iniciais incluem:

- Uma instituição: "Universidade Exemplo"
- Um departamento: "Departamento de Ciência da Computação"
- Um professor administrador com as seguintes credenciais:
  - Email: admin@example.com
  - Senha: admin123
  - Saldo inicial: 1000.0 moedas

Estes dados são carregados automaticamente nos perfis de desenvolvimento (`dev`), teste (`test`) e padrão (`default`). No perfil de produção (`prod`), estes dados não são carregados.

## Fluxo de Uso

### 1. Início: Configuração Inicial e Autenticação

#### 1.1 Login como Administrador

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "senha": "admin123"
}
```

**Resposta esperada:**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "nome": "Admin do Sistema",
  "email": "admin@example.com",
  "tipo": "PROFESSOR"
}
```

> **Importante:** Copie o token retornado e adicione-o como header em todas as próximas requisições:
>
> ```
> Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
> ```

#### 1.2 Criar uma Nova Instituição

```http
POST http://localhost:8080/api/instituicoes
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

{
  "nome": "Universidade Federal de Exemplo",
  "endereco": "Av. Brasil, 1500, Belo Horizonte, MG"
}
```

#### 1.3 Criar um Novo Departamento

```http
POST http://localhost:8080/api/departamentos/instituicao/{instituicaoId}
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

{
  "nome": "Departamento de Engenharia de Software"
}
```

#### 1.4 Criar um Curso

```http
POST http://localhost:8080/api/instituicoes/{instituicaoId}/cursos
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

{
  "nome": "Engenharia de Software"
}
```

### 2. Meio: Cadastro de Usuários e Início das Operações

#### 2.1 Cadastrar um Aluno

```http
POST http://localhost:8080/api/alunos/cadastro
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@aluno.com",
  "senha": "senha123",
  "cpf": "123.456.789-01",
  "rg": "MG-12.345.678",
  "endereco": "Rua dos Estudantes, 789, Apt 102",
  "curso": {
    "id": "{cursoId}"
  }
}
```

#### 2.2 Cadastrar uma Empresa Parceira

```http
POST http://localhost:8080/api/empresas/cadastro
Content-Type: application/json

{
  "nome": "Livraria Universitária",
  "email": "livraria@exemplo.com",
  "senha": "empresa123",
  "cpf": "111.222.333-44",
  "cnpj": "12.345.678/0001-99",
  "descricao": "Livraria especializada em material acadêmico e técnico."
}
```

#### 2.3 Login como Empresa

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "livraria@exemplo.com",
  "senha": "empresa123"
}
```

> **Importante:** Salve o token da empresa para os próximos passos:
>
> ```
> Authorization: Bearer eyJhbGciOiJIUzUxMiJ9... [TOKEN DA EMPRESA]
> ```

#### 2.4 Empresa Cria uma Vantagem

```http
POST http://localhost:8080/api/vantagens/empresa/{empresaId}
Content-Type: application/json
Authorization: Bearer [TOKEN DA EMPRESA]

{
  "nome": "Desconto em Livros Técnicos",
  "descricao": "Desconto de 20% em livros técnicos da área de computação",
  "custoMoedas": 50.0
}
```

#### 2.5 Upload de Foto para a Vantagem

```http
POST http://localhost:8080/api/vantagens/{vantagemId}/foto
Content-Type: multipart/form-data
Authorization: Bearer [TOKEN DA EMPRESA]

foto: [ARQUIVO DE IMAGEM]
```

#### 2.6 Login como Professor (Admin)

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "senha": "admin123"
}
```

> **Importante:** Salve o token do professor:
>
> ```
> Authorization: Bearer eyJhbGciOiJIUzUxMiJ9... [TOKEN DO PROFESSOR]
> ```

#### 2.7 Professor Envia Moedas para Aluno

```http
POST http://localhost:8080/api/professores/enviar-moedas
Content-Type: application/json
Authorization: Bearer [TOKEN DO PROFESSOR]

{
  "professor": {
      "id": "{professorId}"
  },
  "aluno": {
    "id": "{alunoId}"
  },
  "valor": 100.0,
  "motivoReconhecimento": "Excelente desempenho no projeto intermediario da disciplina",
  "tipo": "TRANSFERENCIA"
}
```

#### 2.8 Professor Consulta seu Saldo

```http
GET http://localhost:8080/api/professores/saldo
Authorization: Bearer [TOKEN DO PROFESSOR]
```

#### 2.9 Professor Consulta seu Extrato

```http
GET http://localhost:8080/api/professores/extrato
Authorization: Bearer [TOKEN DO PROFESSOR]
```

### 3. Fim: Resgate de Vantagens e Validação

#### 3.1 Login como Aluno

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "joao@aluno.com",
  "senha": "senha123"
}
```

> **Importante:** Salve o token do aluno:
>
> ```
> Authorization: Bearer eyJhbGciOiJIUzUxMiJ9... [TOKEN DO ALUNO]
> ```

#### 3.2 Aluno Consulta seu Saldo

```http
GET http://localhost:8080/api/alunos/{alunoId}
Authorization: Bearer [TOKEN DO ALUNO]
```

#### 3.3 Aluno Visualiza Vantagens Disponíveis

```http
GET http://localhost:8080/api/vantagens
Authorization: Bearer [TOKEN DO ALUNO]
```

#### 3.4 Aluno Resgata uma Vantagem

```http
POST http://localhost:8080/api/vantagens/{vantagemId}/resgatar
Authorization: Bearer [TOKEN DO ALUNO]
```

#### 3.5 Aluno Consulta seus Cupons

```http
GET http://localhost:8080/api/transacoes/cupons
Authorization: Bearer [TOKEN DO ALUNO]
```

#### 3.6 Login como Empresa

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "livraria@exemplo.com",
  "senha": "empresa123"
}
```

#### 3.7 Empresa Consulta Cupons Pendentes

```http
GET http://localhost:8080/api/transacoes/cupons/empresa/{empresaId}
Authorization: Bearer [TOKEN DA EMPRESA]
```

#### 3.8 Empresa Valida um Cupom

```http
POST http://localhost:8080/api/transacoes/cupons/{cupomId}/validar?codigoConfirmacao=ABC12345
Authorization: Bearer [TOKEN DA EMPRESA]
```

#### 3.9 Verificação Final de Notificações

##### 3.9.1 Aluno Verifica suas Notificações

```http
GET http://localhost:8080/api/notificacoes/minhas
Authorization: Bearer [TOKEN DO ALUNO]
```

##### 3.9.2 Professor Verifica Histórico de Transações

```http
GET http://localhost:8080/api/transacoes/extrato
Authorization: Bearer [TOKEN DO PROFESSOR]
```

### Verificações Adicionais (Opcional)

#### Verificar Instituições Cadastradas

```http
GET http://localhost:8080/api/instituicoes
```

#### Verificar Cursos de uma Instituição

```http
GET http://localhost:8080/api/instituicoes/{instituicaoId}/cursos
```

#### Verificar Departamentos de uma Instituição

```http
GET http://localhost:8080/api/departamentos/instituicao/{instituicaoId}
```

#### Verificar Professores de um Departamento

```http
GET http://localhost:8080/api/departamentos/{departamentoId}/professores
```

## Sistema de Notificações

O sistema possui um serviço robusto de notificações que:

- Envia emails automáticos para alunos e empresas
- Mantém histórico de todas as notificações
- Possui sistema de reenvio automático para notificações que falharam
- Notifica sobre:
  - Recebimento de moedas
  - Resgate de vantagens
  - Cupons gerados

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE.md](LICENSE.md) para mais detalhes.

A licença MIT é uma licença de software livre permissiva que permite que qualquer pessoa use, copie, modifique, mescle, publique, distribua, sublicencie e/ou venda cópias do software, desde que inclua o aviso de copyright e a permissão em todas as cópias ou partes substanciais do software.
