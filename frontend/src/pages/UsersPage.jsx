import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import DataTable from '../components/DataTable';
import Modal from '../components/Modal';
import ConfirmDialog from '../components/ConfirmDialog';
import SearchBar from '../components/SearchBar';
import styles from './UsersPage.module.css';

export default function UsersPage() {
  const { user } = useAuth();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [form, setForm] = useState({ name: '', email: '', password: '', role: 'HOST' });
  const [detailUser, setDetailUser] = useState(null);

  useEffect(() => { fetchUsers(); }, []);

  const fetchUsers = async () => {
    try {
      const res = await api.get('/users');
      setUsers(res.data.data || []);
    } catch (err) {
      toast.error('Failed to load users');
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    try {
      if (selectedUser) {
        await api.put(`/users/${selectedUser.id}`, form);
        toast.success('User updated successfully');
      } else {
        await api.post('/users', form);
        toast.success('User created successfully');
      }
      setModalOpen(false);
      resetForm();
      fetchUsers();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not complete this operation. Check your connection.');
    }
  };

  const handleDelete = async () => {
    try {
      await api.delete(`/users/${selectedUser.id}`);
      toast.success('User deleted successfully');
      setConfirmOpen(false);
      fetchUsers();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not delete user. Check your connection.');
    }
  };

  const resetForm = () => {
    setForm({ name: '', email: '', password: '', role: 'HOST' });
    setSelectedUser(null);
  };

  const openEdit = (u) => {
    setSelectedUser(u);
    setForm({ name: u.name, email: u.email, password: '', role: u.role });
    setModalOpen(true);
  };

  const openDelete = (u) => {
    setSelectedUser(u);
    setConfirmOpen(true);
  };

  const isAdmin = user?.role === 'ADMIN';

  const filtered = users.filter(u =>
    u.name?.toLowerCase().includes(search.toLowerCase()) ||
    u.email?.toLowerCase().includes(search.toLowerCase())
  );

  const columns = [
    { key: 'name', label: 'Name' },
    { key: 'email', label: 'Email' },
    { key: 'role', label: 'Role' },
    { key: 'teamName', label: 'Team' },
    {
      key: 'createdAt', label: 'Created',
      render: (val) => val ? new Date(val).toLocaleDateString() : '—'
    },
    ...(isAdmin ? [{
      key: 'actions', label: 'Actions',
      render: (_, row) => (
        <div style={{ display: 'flex', gap: 8 }}>
          <button className="btn btn-sm btn-secondary" onClick={() => setDetailUser(row)}>View</button>
          <button className="btn btn-sm btn-secondary" onClick={() => openEdit(row)}>Edit</button>
          <button className="btn btn-sm btn-danger" onClick={() => openDelete(row)}>Delete</button>
        </div>
      )
    }] : [{
      key: 'actions', label: 'Actions',
      render: (_, row) => (
        <button className="btn btn-sm btn-secondary" onClick={() => setDetailUser(row)}>View</button>
      )
    }])
  ];

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className={styles.page}>
      <div className="page-header">
        <h1>Users</h1>
        {isAdmin && (
          <button className="btn btn-primary" onClick={() => { resetForm(); setModalOpen(true); }}>Add User</button>
        )}
      </div>
      <SearchBar value={search} onChange={setSearch} placeholder="Search users..." />
      <DataTable columns={columns} data={filtered} />

      {detailUser && (
        <Modal isOpen={!!detailUser} onClose={() => setDetailUser(null)} title={detailUser.name}>
          <div className="form-group">
            <label>Email</label>
            <p>{detailUser.email}</p>
          </div>
          <div className="form-group">
            <label>Role</label>
            <p>{detailUser.role}</p>
          </div>
          <div className="form-group">
            <label>Team</label>
            <p>{detailUser.teamName || '—'}</p>
          </div>
          <div className="form-group">
            <label>Phone</label>
            <p>{detailUser.phone || '—'}</p>
          </div>
          <div className="form-group">
            <label>Notable Work</label>
            <p>{detailUser.notableWork || '—'}</p>
          </div>
          <div className="form-group">
            <label>Experience</label>
            <p>{detailUser.experience || '—'}</p>
          </div>
        </Modal>
      )}

      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title={selectedUser ? 'Edit User' : 'Add User'}>
        <div className="form-group">
          <label>Name</label>
          <input value={form.name} onChange={(e) => setForm({...form, name: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Email</label>
          <input type="email" value={form.email} onChange={(e) => setForm({...form, email: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Password {selectedUser && '(leave blank to keep)'}</label>
          <input type="password" value={form.password} onChange={(e) => setForm({...form, password: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Role</label>
          <select value={form.role} onChange={(e) => setForm({...form, role: e.target.value})}>
            <option value="ADMIN">Admin</option>
            <option value="HOST">Host</option>
            <option value="PRODUCER">Producer</option>
            <option value="WRITER">Writer</option>
            <option value="EDITOR">Editor</option>
            <option value="DESIGNER">Designer</option>
          </select>
        </div>
        <button className="btn btn-primary mt-20" onClick={handleSave}>Save</button>
      </Modal>

      <ConfirmDialog
        isOpen={confirmOpen}
        onConfirm={handleDelete}
        onCancel={() => setConfirmOpen(false)}
        title="Delete User"
        message={`Are you sure you want to delete ${selectedUser?.name}?`}
      />
    </div>
  );
}
