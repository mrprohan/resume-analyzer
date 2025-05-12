import React, { useState } from 'react';
import './ResumeForm.css';
import { useNavigate } from 'react-router-dom';

function ResumeForm() {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState('');
  const [viewLink, setViewLink] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const allowedExtensions = ['pdf', 'doc', 'docx'];
  const maxSize = 2 * 1024 * 1024; // 2MB

  const validateFile = (file) => {
    const extension = file.name.split('.').pop().toLowerCase();
    if (!allowedExtensions.includes(extension)) {
      return '❌ Invalid file type. Only PDF, DOC, or DOCX allowed.';
    }
    if (file.size > maxSize) {
      return '❌ File size exceeds 2MB.';
    }
    return null;
  };

  const handleChange = (e) => {
    const selectedFile = e.target.files[0];
    if (!selectedFile) return;

    const error = validateFile(selectedFile);
    if (error) {
      alert(error);
      e.target.value = ''; // Reset file input
      setFile(null);
      return;
    }

    setFile(selectedFile);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) {
      alert('⚠️ Please choose a file.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    setLoading(true);
    setMessage('');
    setViewLink('');

    try {
      const response = await fetch('http://localhost:8080/api/resumes/upload', {
        method: 'POST',
        body: formData,
      });

      const data = await response.json();
      setMessage(data.message);
      setViewLink(`/resume/${data.resumeId}`);
    } catch (err) {
      console.error(err);
      setMessage('❌ Upload failed. Try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleViewClick = () => {
    if (viewLink) {
      navigate(viewLink); // Navigate to /resume/:id
    }
  };

  return (
    <div className="form-container">
      <h2>Upload Your Resume</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="file"
          onChange={handleChange}
          accept=".pdf,.doc,.docx"
          required
        />
        <input type="submit" value={loading ? 'Uploading...' : 'Upload Resume'} />
      </form>

      {message && (
        <div className="success-message">
          ✅ {message}
          <br />
          {viewLink && (
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
