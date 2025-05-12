import React, { useState } from 'react';
import './ResumeForm.css';
import { useNavigate } from 'react-router-dom';

function ResumeForm() {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState('');
  const [resumeId, setResumeId] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const allowedExtensions = ['pdf', 'doc', 'docx'];
  const maxSize = 2 * 1024 * 1024;

  const validateFile = (file) => {
    const ext = file.name.split('.').pop().toLowerCase();
    if (!allowedExtensions.includes(ext)) return '❌ Only PDF, DOC, or DOCX files are allowed.';
    if (file.size > maxSize) return '❌ File size exceeds 2MB.';
    return null;
  };

  const handleChange = (e) => {
    const selected = e.target.files[0];
    const error = validateFile(selected);
    if (error) {
      alert(error);
      e.target.value = '';
      setFile(null);
    } else {
      setFile(selected);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) {
      alert('⚠️ Please select a resume.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    setLoading(true);

    try {
      const uploadRes = await fetch('http://localhost:8080/api/resumes/upload', {
        method: 'POST',
        body: formData,
      });

      const uploadData = await uploadRes.json();
      if (uploadData.resumeId) {
        setResumeId(uploadData.resumeId);
        setMessage(uploadData.message);
      } else {
        throw new Error('Resume ID missing in response.');
      }
    } catch (err) {
      setMessage('❌ Upload failed. Try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleViewClick = () => {
    navigate(`/resume/${resumeId}`);
  };

  return (
    <div className="form-container">
      <h2>Upload Your Resume</h2>
      <form onSubmit={handleSubmit}>
        <input type="file" onChange={handleChange} accept=".pdf,.doc,.docx" required />
        <input type="submit" value={loading ? 'Uploading...' : 'Upload Resume'} />
      </form>

      {message && (
        <div className="success-message">
          ✅ {message}
          {resumeId && (
            <button onClick={handleViewClick} style={{ marginTop: '10px' }}>
              🔍 View Resume
            </button>
          )}
        </div>
      )}
    </div>
  );
}

export default ResumeForm;
