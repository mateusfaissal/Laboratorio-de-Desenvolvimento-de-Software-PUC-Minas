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
import { Statement } from './pages/professor/Statement'

// Importando páginas de vantagens
import { Vantagens } from './pages/Vantagens'

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="app-container">
          <Routes>
            {/* Rotas de autenticação */}
            <Route path="/login" element={<Login />} />

            {/* Rotas do estudante */}
            <Route path="/student/dashboard" element={<StudentDashboard />} />
            <Route path="/student/vantagens" element={<Vantagens />} />

            {/* Rotas do professor */}
            <Route path="/professor/dashboard" element={<ProfessorDashboard />} />
            <Route path="/professor/send-coins" element={<SendCoins />} />
            <Route path="/professor/statement" element={<Statement />} />

            {/* Rotas da empresa parceira */}
            <Route path="/empresa/vantagens" element={<Vantagens />} />

            {/* Redirecionar para login por padrão */}
            <Route path="/" element={<Login />} />
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  )
}

export default App
