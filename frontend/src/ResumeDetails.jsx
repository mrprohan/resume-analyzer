import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import './ResumeDetails.css';

function ResumeDetails() {
  const { id } = useParams();
  const [resume, setResume] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    async function fetchResume() {
      try {
        const res = await fetch(`http://localhost:8080/api/resumes/${id}`);
        const data = await res.json();
  
        // Try parsing feedback as JSON, fallback to plain string
        if (typeof data.feedback === 'string') {
          try {
            const parsed = JSON.parse(data.feedback);
            data.feedback = parsed;
          } catch {
            data.feedback = { text: data.feedback }; // fallback as plain string
          }
        }
  
        setResume(data);
      } catch (err) {
        console.error(err);
        setError('❌ Unable to load resume details.');
      }
    }
  
    fetchResume();
  }, [id]);
  

  if (error) return <p className="error">{error}</p>;
  if (!resume) return <p>Loading...</p>;

  return (
    <div className="resume-details">
      <h2>📄 Resume Summary</h2>
      <p><strong>File Name:</strong> {resume.originalFileName || 'N/A'}</p>
      <p><strong>Uploaded At:</strong> {resume.uploadedAt ? new Date(resume.uploadedAt).toLocaleString() : 'N/A'}</p>

      <h3>📃 Extracted Resume Text</h3>
      <pre className="extracted-text">{resume.extractedText || 'No text extracted.'}</pre>

      <h3>🤖 AI Skill Feedback</h3>
{resume.feedback?.technical_skills ? (
  <>
    <p><strong>Technical Skills:</strong></p>
    <ul>{resume.feedback.technical_skills.map((skill, i) => <li key={i}>{skill}</li>)}</ul>

    <p><strong>Soft Skills:</strong></p>
    <ul>{resume.feedback.soft_skills.map((skill, i) => <li key={i}>{skill}</li>)}</ul>

    <p><strong>Strengths:</strong> {resume.feedback.strengths}</p>
    <p><strong>Improvements:</strong> {resume.feedback.improvements}</p>
  </>
) : resume.feedback?.text ? (
  <div style={{ whiteSpace: 'pre-line', backgroundColor: '#f4f4f4', padding: '1rem', borderRadius: '6px' }}>
    {resume.feedback.text}
  </div>
) : (
  <p>⚠️ Feedback data not available.</p>
)}

    </div>
  );
}

export default ResumeDetails;
