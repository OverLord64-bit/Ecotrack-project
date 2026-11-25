import { NavLink, Navigate, Route, Routes } from 'react-router-dom';
import { AgentDashboardPage } from './pages/AgentDashboardPage';
import { AgentReportPage } from './pages/AgentReportPage';

export function App() {
  return (
    <>
      <header className="topbar">
        <strong>EcoTrack</strong>
        <nav>
          <span className="space">Agent</span>
          <NavLink to="/agent">Dashboard</NavLink>
        </nav>
      </header>
      <main>
        <Routes>
          <Route path="/" element={<Navigate to="/agent" replace />} />
          <Route path="/agent" element={<AgentDashboardPage />} />
          <Route path="/agent/reports/:id" element={<AgentReportPage />} />
          <Route path="*" element={<p>Page not found.</p>} />
        </Routes>
      </main>
    </>
  );
}
