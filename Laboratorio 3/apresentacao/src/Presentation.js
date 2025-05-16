import React, { useState, useEffect } from "react";
import mermaid from "mermaid";
import { ChevronRight, ChevronLeft, Home } from "lucide-react";

// Inicializa o Mermaid
const Mermaid = ({ chart }) => {
  React.useEffect(() => {
    try {
      mermaid.initialize({
        startOnLoad: true,
        theme: "default",
        securityLevel: "loose",
        flowchart: { useMaxWidth: false },
      });
      mermaid.contentLoaded();
    } catch (e) {
      console.error("Mermaid error:", e);
    }
  }, [chart]);

  return (
    <div
      className="mermaid bg-white p-4 rounded-lg overflow-auto max-w-full"
      style={{ minHeight: "400px" }}
    >
      {chart}
    </div>
  );
};

const Presentation = () => {
  const [currentSlide, setCurrentSlide] = useState(0);

  // Adiciona navegação por teclado
  useEffect(() => {
    const handleKeyPress = (event) => {
      if (event.key === "ArrowRight") {
        nextSlide();
      } else if (event.key === "ArrowLeft") {
        prevSlide();
      }
    };

    window.addEventListener("keydown", handleKeyPress);
    return () => window.removeEventListener("keydown", handleKeyPress);
  }, [currentSlide]);

  const slides = [
    // Slide 1: Capa
    {
      title: "Sistema de Moeda Estudantil",
      content: () => (
        <div className="flex flex-col items-center justify-center h-full">
          <h1 className="text-4xl font-bold mb-8 text-blue-700">
            Sistema de Moeda Estudantil
          </h1>
          <div className="text-xl mb-2">
            Laboratório de Desenvolvimento de Software
          </div>
          <div className="mt-4 text-lg text-gray-700 font-semibold">
            Integrantes:
            <br />
            <span className="block">Luiz Paulo Saud</span>
            <span className="block">Mateus Faissal</span>
          </div>
          <div className="mt-8 border-t-2 border-blue-500 pt-4 text-gray-600">
            Data: {new Date().toLocaleDateString()}
          </div>
        </div>
      ),
    },

    // Slide 2: Tecnologias Utilizadas
    {
      title: "Tecnologias Utilizadas",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-8 text-blue-700">
            Tecnologias Utilizadas
          </h2>
          <div className="grid grid-cols-2 gap-8 w-full max-w-4xl">
            <div className="text-center p-6 rounded-lg shadow-lg bg-white">
              <div className="text-5xl mb-4 text-blue-600">☕</div>
              <h3 className="text-2xl font-semibold">Java</h3>
              <p className="mt-2 text-gray-600">Backend e lógica de negócios</p>
            </div>

            <div className="text-center p-6 rounded-lg shadow-lg bg-white">
              <div className="text-5xl mb-4 text-green-600">🍃</div>
              <h3 className="text-2xl font-semibold">Spring</h3>
              <p className="mt-2 text-gray-600">Framework para API RESTful</p>
            </div>

            <div className="text-center p-6 rounded-lg shadow-lg bg-white">
              <div className="text-5xl mb-4 text-blue-400">⚛️</div>
              <h3 className="text-2xl font-semibold">React</h3>
              <p className="mt-2 text-gray-600">
                Frontend e interface de usuário
              </p>
            </div>

            <div className="text-center p-6 rounded-lg shadow-lg bg-white">
              <div className="text-5xl mb-4 text-indigo-700">💾</div>
              <h3 className="text-2xl font-semibold">PostgreSQL</h3>
              <p className="mt-2 text-gray-600">Banco de dados relacional</p>
            </div>
          </div>
        </div>
      ),
    },

    // SLIDES DE TECNOLOGIAS
    {
      title: "Java",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">Java</h2>
          <p className="text-lg max-w-2xl text-center mb-4">
            Java é uma das linguagens de programação mais utilizadas no mundo
            corporativo, reconhecida por sua robustez, segurança e
            portabilidade. No nosso sistema, o Java é a base do backend,
            permitindo a construção de uma aplicação confiável, escalável e de
            alta performance.
          </p>
          <p className="text-lg max-w-2xl text-center">
            Com o Java, conseguimos integrar facilmente frameworks modernos,
            garantir a manutenção do código e conectar o sistema a bancos de
            dados relacionais como o PostgreSQL, proporcionando uma experiência
            sólida tanto para desenvolvedores quanto para usuários finais.
          </p>
        </div>
      ),
    },
    {
      title: "Spring",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-green-700">Spring</h2>
          <p className="text-lg max-w-2xl text-center">
            O framework Spring é utilizado para criar a API RESTful, facilitando
            a criação de endpoints seguros, injeção de dependências,
            autenticação JWT e integração com o banco de dados.
          </p>
        </div>
      ),
    },
    {
      title: "React",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-400">React</h2>
          <p className="text-lg max-w-2xl text-center">
            React é a biblioteca utilizada para o frontend, proporcionando uma
            interface de usuário moderna, responsiva e interativa para
            diferentes perfis de usuários do sistema.
          </p>
        </div>
      ),
    },
    {
      title: "PostgreSQL",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-indigo-700">
            PostgreSQL
          </h2>
          <p className="text-lg max-w-2xl text-center">
            PostgreSQL é o banco de dados relacional utilizado para armazenar
            todas as informações do sistema, garantindo integridade, performance
            e recursos avançados como transações ACID e backup automático.
          </p>
        </div>
      ),
    },

    // Slide 3: Use Case Diagram
    {
      title: "Diagrama de Caso de Uso",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Diagrama de Caso de Uso
          </h2>
          <img
            src="/Use-Case-Diagram.png"
            alt="Diagrama de Caso de Uso"
            className="max-w-full rounded-lg shadow-lg"
          />
        </div>
      ),
    },

    // Slide 4: Arquitetura do Sistema (adicionei este slide já que estava faltando a página 4)
    {
      title: "Arquitetura do Sistema",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-8 text-blue-700">
            Arquitetura do Sistema
          </h2>
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <h3 className="text-2xl font-semibold mb-4">
              Arquitetura em Camadas
            </h3>
            <div className="flex flex-col gap-4">
              <div className="border-2 border-blue-300 p-3 rounded-lg bg-blue-50">
                <h4 className="font-semibold">Apresentação (React)</h4>
                <p>Interfaces para diferentes tipos de usuários</p>
              </div>
              <div className="border-2 border-green-300 p-3 rounded-lg bg-green-50">
                <h4 className="font-semibold">API (Spring REST)</h4>
                <p>Endpoints para comunicação frontend-backend</p>
              </div>
              <div className="border-2 border-yellow-300 p-3 rounded-lg bg-yellow-50">
                <h4 className="font-semibold">Serviços (Spring Service)</h4>
                <p>Lógica de negócios e regras da aplicação</p>
              </div>
              <div className="border-2 border-red-300 p-3 rounded-lg bg-red-50">
                <h4 className="font-semibold">Persistência (JPA/Hibernate)</h4>
                <p>Comunicação com banco de dados PostgreSQL</p>
              </div>
            </div>
          </div>
        </div>
      ),
    },

    // Slide 5: Class Diagram
    {
      title: "Diagrama de Classes",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Diagrama de Classes
          </h2>
          <img
            src="/Class-Diagram.png"
            alt="Diagrama de Classes"
            className="max-w-full rounded-lg shadow-lg"
          />
        </div>
      ),
    },

    // Slide 6: Component Diagram
    {
      title: "Diagrama de Componentes",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Diagrama de Componentes
          </h2>
          <img
            src="/Components-Diagram.png"
            alt="Diagrama de Componentes"
            className="max-w-full rounded-lg shadow-lg"
          />
        </div>
      ),
    },

    // SLIDES DE ENDPOINTS
    {
      title: "Endpoints - Autenticação",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Endpoints - Autenticação
          </h2>
          <ul className="text-lg list-disc pl-6">
            <li>
              <b>POST</b> /api/auth/login – Login do usuário
            </li>
          </ul>
        </div>
      ),
    },
    {
      title: "Endpoints - Usuários",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Endpoints - Usuários
          </h2>
          <ul className="text-lg list-disc pl-6">
            <li>
              <b>GET</b> /api/usuarios – Listar todos os usuários
            </li>
            <li>
              <b>GET</b> /api/usuarios/{"{id}"} – Buscar usuário por ID
            </li>
          </ul>
        </div>
      ),
    },
    {
      title: "Endpoints - Alunos",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Endpoints - Alunos
          </h2>
          <ul className="text-lg list-disc pl-6">
            <li>
              <b>GET</b> /api/alunos – Listar todos os alunos
            </li>
            <li>
              <b>GET</b> /api/alunos/{"{id}"} – Buscar aluno por ID
            </li>
            <li>
              <b>POST</b> /api/alunos/cadastro – Cadastrar aluno
            </li>
            <li>
              <b>PUT</b> /api/alunos/{"{id}"} – Atualizar aluno
            </li>
            <li>
              <b>DELETE</b> /api/alunos/{"{id}"} – Deletar aluno
            </li>
            <li>
              <b>POST</b> /api/alunos/{"{id}"}/adicionar-moedas – Adicionar
              moedas ao aluno
            </li>
          </ul>
        </div>
      ),
    },
    {
      title: "Endpoints - Professores",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Endpoints - Professores
          </h2>
          <ul className="text-lg list-disc pl-6">
            <li>
              <b>GET</b> /api/professores – Listar todos os professores
            </li>
            <li>
              <b>GET</b> /api/professores/{"{id}"} – Buscar professor por ID
            </li>
            <li>
              <b>GET</b> /api/professores/saldo – Consultar saldo de moedas
            </li>
            <li>
              <b>POST</b> /api/professores/enviar-moedas – Enviar moedas para
              aluno
            </li>
          </ul>
        </div>
      ),
    },
    {
      title: "Endpoints - Empresas Parceiras",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Endpoints - Empresas Parceiras
          </h2>
          <ul className="text-lg list-disc pl-6">
            <li>
              <b>GET</b> /api/empresas – Listar todas as empresas
            </li>
            <li>
              <b>GET</b> /api/empresas/{"{id}"} – Buscar empresa por ID
            </li>
            <li>
              <b>POST</b> /api/empresas/cadastro – Cadastrar empresa
            </li>
            <li>
              <b>PUT</b> /api/empresas/{"{id}"} – Atualizar empresa
            </li>
            <li>
              <b>DELETE</b> /api/empresas/{"{id}"} – Deletar empresa
            </li>
            <li>
              <b>GET</b> /api/empresas/{"{id}"}/resgates – Listar resgates da
              empresa
            </li>
          </ul>
        </div>
      ),
    },
    {
      title: "Endpoints - Vantagens",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Endpoints - Vantagens
          </h2>
          <ul className="text-lg list-disc pl-6">
            <li>
              <b>GET</b> /api/vantagens – Listar todas as vantagens
            </li>
            <li>
              <b>GET</b> /api/vantagens/empresa/{"{empresaId}"} – Listar
              vantagens por empresa
            </li>
            <li>
              <b>GET</b> /api/vantagens/{"{id}"} – Buscar vantagem por ID
            </li>
            <li>
              <b>POST</b> /api/vantagens/{"{id}"}/resgatar – Resgatar vantagem
            </li>
            <li>
              <b>DELETE</b> /api/vantagens/{"{id}"} – Deletar vantagem
            </li>
          </ul>
        </div>
      ),
    },
    {
      title: "Endpoints - Transações e Cupons",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Endpoints - Transações e Cupons
          </h2>
          <ul className="text-lg list-disc pl-6">
            <li>
              <b>GET</b> /api/transacoes/extrato – Consultar extrato do usuário
            </li>
            <li>
              <b>GET</b> /api/transacoes/transferencias/aluno/{"{alunoId}"} –
              Listar transferências de um aluno
            </li>
            <li>
              <b>GET</b> /api/transacoes/cupons/{"{cupomId}"} – Buscar detalhes
              de um cupom
            </li>
            <li>
              <b>GET</b> /api/transacoes/cupons/empresa/{"{empresaId}"} – Listar
              cupons resgatados em uma empresa
            </li>
            <li>
              <b>POST</b> /api/transacoes/cupons/{"{cupomId}"}/validar – Validar
              um cupom
            </li>
          </ul>
        </div>
      ),
    },
    {
      title: "Endpoints - Cursos e Instituições",
      content: () => (
        <div className="flex flex-col items-center">
          <h2 className="text-3xl font-bold mb-4 text-blue-700">
            Endpoints - Cursos e Instituições
          </h2>
          <ul className="text-lg list-disc pl-6">
            <li>
              <b>GET</b> /api/instituicoes – Listar todas as instituições
            </li>
            <li>
              <b>GET</b> /api/instituicoes/{"{id}"} – Buscar instituição por ID
            </li>
            <li>
              <b>DELETE</b> /api/instituicoes/{"{id}"} – Excluir instituição
            </li>
            <li>
              <b>GET</b> /api/cursos – Listar todos os cursos
            </li>
            <li>
              <b>GET</b> /api/instituicoes/{"{instituicaoId}"}/cursos – Listar
              cursos por instituição
            </li>
          </ul>
        </div>
      ),
    },

    // Slide de Conclusão (mover para o final)
    {
      title: "Conclusão",
      content: () => (
        <div className="flex flex-col items-center justify-center h-full">
          <h2 className="text-3xl font-bold mb-8 text-blue-700">Conclusão</h2>
          <div className="bg-white p-6 rounded-lg shadow-lg max-w-lg">
            <h3 className="text-2xl font-semibold mb-4">
              Sistema de Moeda Estudantil
            </h3>
            <ul className="list-disc pl-6 space-y-2">
              <li>
                Solução completa para gerenciamento de moeda virtual acadêmica
              </li>
              <li>
                Facilita o reconhecimento de mérito entre professores e alunos
              </li>
              <li>Cria ecossistema de benefícios através de parcerias</li>
              <li>
                Implementado com stack moderna: Java, Spring, React e PostgreSQL
              </li>
              <li>
                Arquitetura em camadas para manutenibilidade e escalabilidade
              </li>
            </ul>
          </div>
          <div className="mt-10 text-4xl font-bold text-blue-600 animate-bounce">
            OBRIGADO!
          </div>
        </div>
      ),
    },
  ];

  const nextSlide = () => {
    if (currentSlide < slides.length - 1) {
      setCurrentSlide(currentSlide + 1);
    }
  };

  const prevSlide = () => {
    if (currentSlide > 0) {
      setCurrentSlide(currentSlide - 1);
    }
  };

  const goToHome = () => {
    setCurrentSlide(0);
  };

  return (
    <div className="min-h-screen min-w-screen bg-gray-100 flex flex-col justify-center items-center">
      <div className="flex-1 w-full flex flex-col justify-center items-center">
        <div className="max-w-6xl w-full bg-white rounded-xl shadow-lg p-8 min-h-[600px] flex flex-col justify-center items-center transition-all duration-300">
          {slides[currentSlide].content()}
        </div>
      </div>
      <div className="bg-white border-t border-gray-200 p-4 w-full">
        <div className="max-w-6xl mx-auto flex justify-between items-center">
          <button
            onClick={goToHome}
            className="p-2 rounded-full hover:bg-gray-100 transition-colors"
            title="Ir para o início"
          >
            <Home className="w-6 h-6 text-gray-600" />
          </button>
          <div className="flex items-center space-x-4">
            <button
              onClick={prevSlide}
              disabled={currentSlide === 0}
              className={`p-2 rounded-full ${
                currentSlide === 0
                  ? "text-gray-300 cursor-not-allowed"
                  : "text-gray-600 hover:bg-gray-100"
              } transition-colors`}
            >
              <ChevronLeft className="w-6 h-6" />
            </button>
            <span className="text-gray-600">
              {currentSlide + 1} / {slides.length}
            </span>
            <button
              onClick={nextSlide}
              disabled={currentSlide === slides.length - 1}
              className={`p-2 rounded-full ${
                currentSlide === slides.length - 1
                  ? "text-gray-300 cursor-not-allowed"
                  : "text-gray-600 hover:bg-gray-100"
              } transition-colors`}
            >
              <ChevronRight className="w-6 h-6" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Presentation;
