import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api';
import { toast } from 'react-toastify';
import styles from './RegisterPage.module.css';

export default function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ teamName: '', adminName: '', adminEmail: '', adminPassword: '', confirmPassword: '' });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.teamName || !form.adminName || !form.adminEmail || !form.adminPassword) {
      toast.error('Please fill in all required fields');
      return;
    }
    if (form.adminPassword !== form.confirmPassword) {
      toast.error('Passwords do not match');
      return;
    }
    if (form.adminPassword.length < 6) {
      toast.error('Password must be at least 6 characters');
      return;
    }
    setLoading(true);
    try {
      await api.post('/teams/register', {
        teamName: form.teamName,
        adminName: form.adminName,
        adminEmail: form.adminEmail,
        adminPassword: form.adminPassword
      });
      toast.success('Team registered! Admin can now add team members.');
      navigate('/login');
    } catch (err) {
      if (err.response) {
        toast.error(err.response.data?.message || 'Registration failed');
      } else if (err.request) {
        toast.error('Cannot reach server. Make sure the backend is running on port 8082.');
      } else {
        toast.error('An unexpected error occurred.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <div className={styles.brand}>
          <h1 className={styles.brandName}>Flowcast</h1>
          <p className={styles.tagline}>Register your production team</p>
        </div>
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.field}>
            <label className={styles.label}>Team Name</label>
            <input
              type="text"
              className={styles.input}
              placeholder="Your production company"
              value={form.teamName}
              onChange={(e) => setForm({...form, teamName: e.target.value})}
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Admin Name</label>
            <input
              type="text"
              className={styles.input}
              placeholder="John Doe"
              value={form.adminName}
              onChange={(e) => setForm({...form, adminName: e.target.value})}
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Admin Email</label>
            <input
              type="email"
              className={styles.input}
              placeholder="admin@example.com"
              value={form.adminEmail}
              onChange={(e) => setForm({...form, adminEmail: e.target.value})}
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Admin Password</label>
            <input
              type="password"
              className={styles.input}
              placeholder="Min 6 characters"
              value={form.adminPassword}
              onChange={(e) => setForm({...form, adminPassword: e.target.value})}
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Confirm Password</label>
            <input
              type="password"
              className={styles.input}
              placeholder="Repeat your password"
              value={form.confirmPassword}
              onChange={(e) => setForm({...form, confirmPassword: e.target.value})}
            />
          </div>
          <button type="submit" className={styles.btn} disabled={loading}>
            {loading ? 'Registering team...' : 'Register Team'}
          </button>
        </form>
        <p className={styles.footerText}>
          Already registered? <Link to="/login" className={styles.link}>Sign In</Link>
        </p>
      </div>
    </div>
  );
}
