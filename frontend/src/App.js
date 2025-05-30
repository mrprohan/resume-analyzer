import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ResumeForm from './ResumeForm';
import ResumeDetails from './ResumeDetails';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<ResumeForm />} />
        <Route path="/resume/:id" element={<ResumeDetails />} />
      </Routes>
    </Router>
  );
}

export default App;
