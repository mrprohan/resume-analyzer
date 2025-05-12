import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

function ResumeDetails() {
  const { id } = useParams();
  const [resume, setResume] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(`http://localhost:8080/api/resumes/${id}`)
      .then(res => res.json())
      .then(data => {
        setResume(data);
        setLoading(false);
      });
  }, [id]);

  if (loading) return <p>Loading resume details...</p>;
  if (!resume) return <p>Resume not found.</p>;

  return (
    <div style={{ padding: '20px' }}>
      <h2>{resume.originalFileName}</h2>
      <p><strong>Uploaded:</strong> {resume.uploadedAt}</p>
      <h3>Extracted Text:</h3>
      <pre style={{ background: '#f0f0f0', padding: '10px' }}>{resume.extractedText}</pre>
      <h3>AI Feedback:</h3>
      <p>{resume.feedback}</p>
    </div>
  );
}

export default ResumeDetails;
