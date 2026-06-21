import { useState } from 'react';
import { toast } from 'react-toastify';
import styles from './AssetUploader.module.css';

const fileTypes = [
  { value: 'audio', label: 'Audio' },
  { value: 'video', label: 'Video' },
  { value: 'image', label: 'Image' },
  { value: 'document', label: 'Document' },
  { value: 'other', label: 'Other' },
];

export default function AssetUploader({ episodeId, onUploadComplete }) {
  const [file, setFile] = useState(null);
  const [fileType, setFileType] = useState('audio');
  const [uploading, setUploading] = useState(false);

  const handleFileChange = (e) => {
    const selected = e.target.files[0];
    if (selected) setFile(selected);
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!file) {
      toast.warn('Please select a file');
      return;
    }

    setUploading(true);

    const formData = new FormData();
    formData.append('file', file);
    formData.append('fileType', fileType);
    if (episodeId) formData.append('episodeId', episodeId);

    try {
      const token = localStorage.getItem('token');
      const res = await fetch('/api/assets/upload', {
        method: 'POST',
        headers: token ? { Authorization: `Bearer ${token}` } : {},
        body: formData,
      });

      if (!res.ok) throw new Error('Upload failed');

      const data = await res.json();
      toast.success('File uploaded successfully');
      setFile(null);
      setFileType('audio');
      if (onUploadComplete) onUploadComplete(data);
    } catch (err) {
      toast.error(err.message || 'Upload failed');
    } finally {
      setUploading(false);
    }
  };

  return (
    <form className={styles.uploader} onSubmit={handleUpload}>
      <h3 className={styles.heading}>Upload Asset</h3>
      <div className={styles.field}>
        <label className={styles.label}>File Type</label>
        <select
          className={styles.select}
          value={fileType}
          onChange={(e) => setFileType(e.target.value)}
        >
          {fileTypes.map((t) => (
            <option key={t.value} value={t.value}>{t.label}</option>
          ))}
        </select>
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Choose File</label>
        <div className={styles.fileInputWrap}>
          <input
            type="file"
            id="assetFile"
            className={styles.fileInput}
            onChange={handleFileChange}
          />
          <label htmlFor="assetFile" className={styles.fileLabel}>
            {file ? file.name : 'Browse files'}
          </label>
        </div>
      </div>
      <button
        type="submit"
        className={styles.submitBtn}
        disabled={uploading || !file}
      >
        {uploading ? 'Uploading...' : 'Upload'}
      </button>
    </form>
  );
}
