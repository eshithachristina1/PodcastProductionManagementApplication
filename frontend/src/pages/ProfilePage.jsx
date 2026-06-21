import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import { toast } from 'react-toastify';
import styles from './ProfilePage.module.css';

export default function ProfilePage() {
  const { user, login } = useAuth();
  const [form, setForm] = useState({
    name: '', notableWork: '', experience: '', phone: '', password: ''
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user) {
      setForm({
        name: user.name || '',
        notableWork: user.notableWork || '',
        experience: user.experience || '',
        phone: user.phone || '',
        password: ''
      });
    }
  }, [user]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await api.put('/users/profile', form);
      login(res.data.data);
      toast.success('Profile updated successfully');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not update profile');
    } finally {
      setLoading(false);
    }
  };

  if (!user) return <div className="loading">Loading...</div>;

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <h1>My Profile</h1>
        <div className={styles.teamBadge}>{user.teamName || 'No Team'}</div>
      </div>
      <div className={styles.profileCard}>
        <div className={styles.avatarSection}>
          <div className={styles.avatar}>{user.name?.[0]?.toUpperCase() || '?'}</div>
          <div>
            <h2 className={styles.userName}>{user.name}</h2>
            <p className={styles.userEmail}>{user.email}</p>
            <span className={styles.roleBadge}>{user.role}</span>
          </div>
        </div>
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Full Name</label>
            <input value={form.name} onChange={(e) => setForm({...form, name: e.target.value})} />
          </div>
          <div className="form-group">
            <label>Phone</label>
            <input value={form.phone} onChange={(e) => setForm({...form, phone: e.target.value})} placeholder="+1 555-0000" />
          </div>
          <div className="form-group">
            <label>Notable Work</label>
            <textarea value={form.notableWork} onChange={(e) => setForm({...form, notableWork: e.target.value})} placeholder="List your notable projects, podcasts, or achievements..." rows={3} />
          </div>
          <div className="form-group">
            <label>Experience</label>
            <textarea value={form.experience} onChange={(e) => setForm({...form, experience: e.target.value})} placeholder="Describe your background and experience..." rows={3} />
          </div>
          <div className="form-group">
            <label>New Password (leave blank to keep current)</label>
            <input type="password" value={form.password} onChange={(e) => setForm({...form, password: e.target.value})} placeholder="Enter new password" />
          </div>
          <div className={styles.actions}>
            <button type="submit" className="btn btn-primary" disabled={loading}>
              {loading ? 'Saving...' : 'Save Changes'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
