import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import StatusBadge from '../components/StatusBadge';
import DataTable from '../components/DataTable';
import Modal from '../components/Modal';
import styles from './AssetsPage.module.css';

export default function AssetsPage() {
  const [assets, setAssets] = useState([]);
  const [episodes, setEpisodes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [uploadModalOpen, setUploadModalOpen] = useState(false);
  const [uploadForm, setUploadForm] = useState({ episodeId: '', fileType: 'SCRIPT', name: '' });
  const [uploadFile, setUploadFile] = useState(null);

  useEffect(() => { fetchData(); }, []);

  const fetchData = async () => {
    const safe = async (promise) => { try { return (await promise).data.data || []; } catch { return []; } };
    const [assets, episodes] = await Promise.all([
      safe(api.get('/assets')),
      safe(api.get('/episodes')),
    ]);
    setAssets(assets);
    setEpisodes(episodes);
    setLoading(false);
  };

  const handleUpload = async () => {
    if (!uploadFile || !uploadForm.episodeId) {
      toast.error('Please select a file and episode');
      return;
    }
    const formData = new FormData();
    formData.append('file', uploadFile);
    formData.append('fileType', uploadForm.fileType);
    formData.append('name', uploadForm.name || uploadFile.name);
    try {
      await api.post(`/assets/upload/${uploadForm.episodeId}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      toast.success('Asset uploaded successfully');
      setUploadModalOpen(false);
      setUploadFile(null);
      setUploadForm({ episodeId: '', fileType: 'SCRIPT', name: '' });
      fetchData();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not upload file. Check your connection.');
    }
  };

  const handleDownload = async (id) => {
    try {
      const res = await api.get(`/assets/download/${id}`, { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const a = document.createElement('a');
      a.href = url;
      a.download = `asset-${id}`;
      a.click();
      window.URL.revokeObjectURL(url);
      toast.success('Download started');
    } catch (err) {
      toast.error('Download failed');
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/assets/${id}`);
      toast.success('Asset deleted');
      fetchData();
    } catch (err) {
      toast.error('Delete failed');
    }
  };

  const columns = [
    { key: 'name', label: 'Name' },
    {
      key: 'fileType', label: 'Type',
      render: (val) => <StatusBadge status={val} />
    },
    { key: 'episodeTitle', label: 'Episode' },
    { key: 'uploadDate', label: 'Uploaded' },
    {
      key: 'id', label: 'Actions',
      render: (val, row) => (
        <div style={{ display: 'flex', gap: 8 }}>
          <button className="btn btn-sm btn-secondary" onClick={() => handleDownload(val)}>Download</button>
          <button className="btn btn-sm btn-danger" onClick={() => handleDelete(val)}>Delete</button>
        </div>
      )
    },
  ];


  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className={styles.page}>
      <div className="page-header">
        <h1>Assets</h1>
        <button className="btn btn-primary" onClick={() => setUploadModalOpen(true)}>Upload Asset</button>
      </div>
      <DataTable columns={columns} data={assets} />

      <Modal isOpen={uploadModalOpen} onClose={() => setUploadModalOpen(false)} title="Upload Asset">
        <div className="form-group">
          <label>Name</label>
          <input value={uploadForm.name} onChange={(e) => setUploadForm({...uploadForm, name: e.target.value})} placeholder="Optional" />
        </div>
        <div className="form-group">
          <label>File Type</label>
          <select value={uploadForm.fileType} onChange={(e) => setUploadForm({...uploadForm, fileType: e.target.value})}>
            <option value="SCRIPT">Script</option>
            <option value="THUMBNAIL">Thumbnail</option>
            <option value="RAW_AUDIO">Raw Audio</option>
            <option value="EDITED_AUDIO">Edited Audio</option>
            <option value="TRANSCRIPT">Transcript</option>
          </select>
        </div>
        <div className="form-group">
          <label>Episode</label>
          <select value={uploadForm.episodeId} onChange={(e) => setUploadForm({...uploadForm, episodeId: e.target.value})}>
            <option value="">{episodes.length === 0 ? 'No episodes available' : 'Select Episode'}</option>
            {episodes.map(ep => <option key={ep.id} value={ep.id}>{ep.title}</option>)}
          </select>
        </div>
        <div className="form-group">
          <label>File</label>
          <input type="file" onChange={(e) => setUploadFile(e.target.files[0])} />
          {uploadFile && <p style={{ fontSize: 13, color: '#6b7280', marginTop: 4 }}>{uploadFile.name}</p>}
        </div>
        <button className="btn btn-primary mt-20" onClick={handleUpload}>Upload</button>
      </Modal>
    </div>
  );
}
