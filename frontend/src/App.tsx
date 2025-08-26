import React, { useState, useEffect } from 'react';
import './App.css';

interface ApiResponse {
  message: string;
  timestamp: string;
  status: string;
}

function App() {
  const [backendStatus, setBackendStatus] = useState<string>('Checking...');
  const [apiResponse, setApiResponse] = useState<ApiResponse | null>(null);

  useEffect(() => {
    // Test backend connection
    fetch('http://localhost:8080/api/health')
      .then(res => res.text())
      .then(data => setBackendStatus(data))
      .catch(() => setBackendStatus('Backend not running'));

    // Test API endpoint
    fetch('http://localhost:8080/api/test')
      .then(res => res.json())
      .then(data => setApiResponse(data))
      .catch(err => console.log('API test failed:', err));
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <h1>🎵 Spotify Lyrics Generator</h1>
        <div style={{ marginBottom: '20px' }}>
          <h3>Backend Status</h3>
          <p style={{ color: backendStatus.includes('running') ? 'lightgreen' : 'orange' }}>
            {backendStatus}
          </p>
        </div>
        
        {apiResponse && (
          <div style={{ marginBottom: '20px', textAlign: 'left' }}>
            <h3>API Test</h3>
            <pre style={{ background: '#282c34', padding: '10px', borderRadius: '5px' }}>
              {JSON.stringify(apiResponse, null, 2)}
            </pre>
          </div>
        )}

        <div style={{ marginTop: '40px' }}>
          <h3>Coming Soon:</h3>
          <ul style={{ textAlign: 'left', color: '#61dafb' }}>
            <li>Spotify Authentication</li>
            <li>Artist Style Analysis</li>
            <li>AI-Generated Lyrics</li>
            <li>Genre Blending</li>
          </ul>
        </div>
      </header>
    </div>
  );
}

export default App;