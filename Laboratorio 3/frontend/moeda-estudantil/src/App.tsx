import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { AuthProvider } from './contexts/AuthContext'
import './App.css'

// Importando páginas de autenticação
import { Login } from './pages/auth/Login'

// Importando páginas do estudante
import { Dashboard as StudentDashboard } from './pages/student/Dashboard'

// Importando páginas do professor
import { Dashboard as ProfessorDashboard } from './pages/professor/Dashboard'
import { SendCoins } from './pages/professor/SendCoins'
import { Statement as ProfessorStatement } from './pages/professor/Statement'

// Importando páginas da empresa
import { Dashboard as EmpresaDashboard } from './pages/empresa/Dashboard'
import ValidarCupomEmpresa from './pages/empresa/ValidarCupom'

// Importando páginas de vantagens
import { Vantagens } from './pages/Vantagens'

// Importando páginas de cadastro
import CadastroAluno from './pages/cadastro/Aluno'
import CadastroEmpresa from './pages/cadastro/Empresa'

// Importando páginas de cupons
import CuponsAluno from './pages/student/Cupons'
import { MinhasVantagens } from './pages/student/MinhasVantagens'

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="app-container">
          <Routes>
            {/* Rotas de autenticação */}
            <Route path="/login" element={<Login />} />

            {/* Rotas de cadastro */}
            <Route path="/cadastro/aluno" element={<CadastroAluno />} />
            <Route path="/cadastro/empresa" element={<CadastroEmpresa />} />

            {/* Rotas do estudante */}
            <Route path="/student/dashboard" element={<StudentDashboard />} />
            <Route path="/student/vantagens" element={<Vantagens />} />
            <Route path="/student/cupons" element={<CuponsAluno />} />
            <Route path="/student/my-advantages" element={<MinhasVantagens />} />

            {/* Rotas do professor */}
            <Route path="/professor/dashboard" element={<ProfessorDashboard />} />
            <Route path="/professor/send-coins" element={<SendCoins />} />
            <Route path="/professor/statement" element={<ProfessorStatement />} />

            {/* Rotas da empresa parceira */}
            <Route path="/empresa/dashboard" element={<EmpresaDashboard />} />
            <Route path="/empresa/vantagens" element={<Vantagens />} />
            <Route path="/empresa/validar-cupom" element={<ValidarCupomEmpresa />} />

            {/* Redirecionar para login por padrão */}
            <Route path="/" element={<Login />} />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  )
}

export default App
