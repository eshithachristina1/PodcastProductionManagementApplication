import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import StatusBadge from '../components/StatusBadge';
import DataTable from '../components/DataTable';
import Modal from '../components/Modal';
import ConfirmDialog from '../components/ConfirmDialog';
import styles from './ProductionBoardPage.module.css';

const TABS = { EPISODES: 'episodes', TASKS: 'tasks', RECORDING: 'recording' };

const EPISODE_STATUSES = ['IDEA', 'RESEARCH', 'SCRIPT_WRITING', 'RECORDING', 'EDITING', 'REVIEW', 'READY_TO_PUBLISH'];

const emptyForm = {
  episodeNumber: '', title: '', description: '', publishDate: '', duration: '', seasonId: '',
   deadline: '', priority: 'MEDIUM', status: 'PENDING', episodeId: '', assignedToId: '',
  recordingDate: '', recordingTime: '', meetingLink: '', location: '', notes: '',
};

export default function ProductionBoardPage() {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState(TABS.EPISODES);
  const [loading, setLoading] = useState(true);

  const [episodes, setEpisodes] = useState([]);
  const [seasons, setSeasons] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [users, setUsers] = useState([]);
  const [sessions, setSessions] = useState([]);

  const [modalOpen, setModalOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);
  const [showUpcoming, setShowUpcoming] = useState(true);
  const [form, setForm] = useState(emptyForm);

  useEffect(() => { fetchAll(); }, []);

  const fetchAll = async () => {
    setLoading(true);
    const safe = async (promise) => { try { return (await promise).data.data || []; } catch { return []; } };
    const [episodes, seasons, tasks, users, sessions] = await Promise.all([
      safe(api.get('/episodes')),
      safe(api.get('/seasons')),
      safe(api.get('/tasks')),
      safe(api.get('/users')),
      safe(api.get('/recording-sessions')),
    ]);
    setEpisodes(episodes);
    setSeasons(seasons);
    setTasks(tasks);
    setUsers(users);
    setSessions(sessions);
    setLoading(false);
  };

  const handleStatusChange = async (episode, newStatus) => {
    try {
      await api.put(`/episodes/${episode.id}`, {
        episodeNumber: episode.episodeNumber,
        title: episode.title,
        description: episode.description || '',
        publishDate: episode.publishDate || null,
        duration: episode.duration || null,
        status: newStatus,
        seasonId: episode.seasonId,
      });
      toast.success('Episode status updated');
      fetchAll();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not update status. Check your connection.');
    }
  };

  const handleMarkDone = async (task) => {
    try {
      await api.put(`/tasks/${task.id}`, {
        title: task.title,
        description: task.description || '',
        deadline: task.deadline || null,
        status: 'COMPLETED',
        priority: task.priority,
        assignedToId: task.assignedToId || null,
        episodeId: task.episodeId,
      });
      toast.success('Task marked as completed');
      fetchAll();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not update task. Check your connection.');
    }
  };

  const handleMarkPending = async (task) => {
    try {
      await api.put(`/tasks/${task.id}`, {
        title: task.title,
        description: task.description || '',
        deadline: task.deadline || null,
        status: 'PENDING',
        priority: task.priority,
        assignedToId: task.assignedToId || null,
        episodeId: task.episodeId,
      });
      toast.success('Task reverted to pending');
      fetchAll();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not update task. Check your connection.');
    }
  };

  const handleSave = async () => {
    try {
      if (activeTab === TABS.EPISODES) {
        const { status, ...episodePayload } = form;
        if (selectedItem) {
          await api.put(`/episodes/${selectedItem.id}`, episodePayload);
          toast.success('Episode updated');
        } else {
          await api.post('/episodes', episodePayload);
          toast.success('Episode created');
        }
      } else if (activeTab === TABS.TASKS) {
        if (selectedItem) {
          await api.put(`/tasks/${selectedItem.id}`, form);
          toast.success('Task updated');
        } else {
          await api.post('/tasks', form);
          toast.success('Task created');
        }
      } else if (activeTab === TABS.RECORDING) {
        if (selectedItem) {
          await api.put(`/recording-sessions/${selectedItem.id}`, form);
          toast.success('Session updated');
        } else {
          await api.post('/recording-sessions', form);
          toast.success('Session scheduled');
        }
      }
      setModalOpen(false);
      resetForm();
      fetchAll();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not complete this operation. Check your connection.');
    }
  };

  const handleDelete = async () => {
    try {
      if (activeTab === TABS.EPISODES) {
        await api.delete(`/episodes/${selectedItem.id}`);
        toast.success('Episode deleted');
      } else if (activeTab === TABS.TASKS) {
        await api.delete(`/tasks/${selectedItem.id}`);
        toast.success('Task deleted');
      } else if (activeTab === TABS.RECORDING) {
        await api.delete(`/recording-sessions/${selectedItem.id}`);
        toast.success('Session deleted');
      }
      setConfirmOpen(false);
      fetchAll();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not complete this operation. Check your connection.');
    }
  };

  const resetForm = () => {
    setForm(emptyForm);
    setSelectedItem(null);
  };

  const openAdd = () => {
    resetForm();
    setModalOpen(true);
  };

  const openEdit = (item) => {
    setSelectedItem(item);
    if (activeTab === TABS.EPISODES) {
      setForm({
        ...emptyForm,
        episodeNumber: item.episodeNumber || '',
        title: item.title || '',
        description: item.description || '',
        publishDate: item.publishDate || '',
        duration: item.duration || '',
        seasonId: item.seasonId || '',
      });
    } else if (activeTab === TABS.TASKS) {
      setForm({
        ...emptyForm,
        title: item.title || '',
        description: item.description || '',
        deadline: item.deadline || '',
        priority: item.priority || 'MEDIUM',
        status: item.status || 'PENDING',
        episodeId: item.episodeId || '',
        assignedToId: item.assignedToId || '',
      });
    } else if (activeTab === TABS.RECORDING) {
      setForm({
        ...emptyForm,
        recordingDate: item.recordingDate || '',
        recordingTime: item.recordingTime || '',
        meetingLink: item.meetingLink || '',
        location: item.location || '',
        notes: item.notes || '',
        episodeId: item.episodeId || '',
      });
    }
    setModalOpen(true);
  };

  const openDelete = (item) => {
    setSelectedItem(item);
    setConfirmOpen(true);
  };

  const [changingStatus, setChangingStatus] = useState(null);

  const handleInlineStatusChange = async (episode, newStatus) => {
    setChangingStatus(episode.id);
    await handleStatusChange(episode, newStatus);
    setChangingStatus(null);
  };

  const canManageEpisodes = ['ADMIN', 'HOST', 'WRITER'].includes(user?.role);
  const canManageTasks = true;
  const canManageSessions = ['ADMIN', 'PRODUCER'].includes(user?.role);

  if (loading) return <div className="loading">Loading...</div>;

  const episodeColumns = [
    { key: 'episodeNumber', label: 'Episode #' },
    { key: 'title', label: 'Title' },
    { key: 'seasonTitle', label: 'Season' },
    {
      key: 'status', label: 'Status',
      render: (val, row) => (
        <select
          className={styles.statusSelect}
          value={val}
          onChange={(e) => handleInlineStatusChange(row, e.target.value)}
          disabled={changingStatus === row.id}
        >
          {EPISODE_STATUSES.map(s => <option key={s} value={s}>{s.replace(/_/g, ' ')}</option>)}
        </select>
      ),
    },
    { key: 'duration', label: 'Duration (min)' },
  ];

  const taskColumns = [
    { key: 'title', label: 'Title' },
    {
      key: 'status', label: 'Status',
      render: (val) => <StatusBadge status={val} />,
    },
    {
      key: 'priority', label: 'Priority',
      render: (val) => <span className={`${styles.priorityBadge} ${styles[val?.toLowerCase()]}`}>{val}</span>,
    },
    { key: 'deadline', label: 'Deadline' },
    { key: 'assignedToName', label: 'Assigned To' },
    { key: 'episodeTitle', label: 'Episode' },
    {
      key: 'actions', label: 'Actions',
      render: (_, row) => (
        <div className={styles.inlineActions}>
          {row.status === 'COMPLETED' ? (
            <button className={styles.pendingBtn} onClick={() => handleMarkPending(row)}>Mark Pending</button>
          ) : (
            <button className={styles.doneBtn} onClick={() => handleMarkDone(row)}>Mark Done</button>
          )}
          {(user.id === row.createdById || user.id === row.assignedToId) && <button className={styles.editBtn} onClick={() => openEdit(row)}>Edit</button>}
          {(user.id === row.createdById || user.id === row.assignedToId) && <button className={styles.deleteBtn} onClick={() => openDelete(row)}>Delete</button>}
        </div>
      ),
    },
  ];

  const sessionColumns = [
    { key: 'recordingDate', label: 'Date' },
    { key: 'recordingTime', label: 'Time' },
    { key: 'episodeTitle', label: 'Episode' },
    {
      key: 'meetingLink', label: 'Meeting Link',
      render: (val) => val ? <a href={val} target="_blank" rel="noopener noreferrer" className={styles.meetingLink}>{val}</a> : val,
    },
    { key: 'location', label: 'Location' },
    { key: 'notes', label: 'Notes' },
  ];

  const filteredSessions = showUpcoming
    ? sessions.filter(s => s.recordingDate && new Date(s.recordingDate) >= new Date())
    : sessions;

  const getModalTitle = () => {
    const prefix = selectedItem ? 'Edit' : 'Add';
    if (activeTab === TABS.EPISODES) return `${prefix} Episode`;
    if (activeTab === TABS.TASKS) return `${prefix} Task`;
    return `${prefix} Recording Session`;
  };

  const getDeleteTitle = () => {
    if (activeTab === TABS.EPISODES) return 'Delete Episode';
    if (activeTab === TABS.TASKS) return 'Delete Task';
    return 'Delete Session';
  };

  const getDeleteMessage = () => {
    if (activeTab === TABS.EPISODES) return `Are you sure you want to delete ${selectedItem?.title}?`;
    if (activeTab === TABS.TASKS) return `Are you sure you want to delete ${selectedItem?.title}?`;
    return 'Are you sure you want to delete this recording session?';
  };

  return (
    <div className={styles.page}>
      <div className={styles.tabs}>
        <button
          className={`${styles.tab} ${activeTab === TABS.EPISODES ? styles.active : ''}`}
          onClick={() => setActiveTab(TABS.EPISODES)}
        >
          Episodes
        </button>
        <button
          className={`${styles.tab} ${activeTab === TABS.TASKS ? styles.active : ''}`}
          onClick={() => setActiveTab(TABS.TASKS)}
        >
          Tasks
        </button>
        <button
          className={`${styles.tab} ${activeTab === TABS.RECORDING ? styles.active : ''}`}
          onClick={() => setActiveTab(TABS.RECORDING)}
        >
          Recording Schedule
        </button>
      </div>

      <div className={styles.tabContent}>
        {activeTab === TABS.EPISODES && (
          <>
            <div className={styles.header}>
              <h1 className={styles.title}>Episodes</h1>
              {canManageEpisodes && <button className={styles.addBtn} onClick={openAdd}>Add Episode</button>}
            </div>
            <DataTable columns={episodeColumns} data={episodes} onEdit={canManageEpisodes ? openEdit : undefined} onDelete={canManageEpisodes ? openDelete : undefined} />
          </>
        )}

        {activeTab === TABS.TASKS && (
          <>
            <div className={styles.header}>
              <h1 className={styles.title}>Tasks</h1>
              <button className={styles.addBtn} onClick={openAdd}>Add Task</button>
            </div>
            <DataTable columns={taskColumns} data={tasks} />
          </>
        )}

        {activeTab === TABS.RECORDING && (
          <>
            <div className={styles.header}>
              <h1 className={styles.title}>Recording Schedule</h1>
              {canManageSessions && <button className={styles.addBtn} onClick={openAdd}>Schedule Session</button>}
            </div>
            <div className={styles.toggle}>
              <button
                className={`${styles.toggleBtn} ${showUpcoming ? styles.toggleActive : ''}`}
                onClick={() => setShowUpcoming(true)}
              >Upcoming</button>
              <button
                className={`${styles.toggleBtn} ${!showUpcoming ? styles.toggleActive : ''}`}
                onClick={() => setShowUpcoming(false)}
              >All</button>
            </div>
            <DataTable columns={sessionColumns} data={filteredSessions} onEdit={canManageSessions ? openEdit : undefined} onDelete={canManageSessions ? openDelete : undefined} />
          </>
        )}
      </div>

      <Modal isOpen={modalOpen} onClose={() => { setModalOpen(false); resetForm(); }} title={getModalTitle()}>
        {activeTab === TABS.EPISODES && (
          <div className={styles.form}>
            <div className={styles.field}>
              <label className={styles.label}>Episode Number</label>
              <input className={styles.input} type="number" value={form.episodeNumber} onChange={(e) => setForm({...form, episodeNumber: e.target.value})} />
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Title</label>
              <input className={styles.input} value={form.title} onChange={(e) => setForm({...form, title: e.target.value})} />
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Description</label>
              <textarea className={styles.textarea} value={form.description} onChange={(e) => setForm({...form, description: e.target.value})} />
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Publish Date</label>
              <input className={styles.input} type="date" value={form.publishDate} onChange={(e) => setForm({...form, publishDate: e.target.value})} />
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Duration (min)</label>
              <input className={styles.input} type="number" value={form.duration} onChange={(e) => setForm({...form, duration: e.target.value})} />
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Season</label>
              <select className={styles.select} value={form.seasonId} onChange={(e) => setForm({...form, seasonId: e.target.value})}>
                <option value="">Select Season</option>
                {seasons.map(s => <option key={s.id} value={s.id}>{s.title}</option>)}
              </select>
            </div>
            <button className={styles.saveBtn} onClick={handleSave}>Save</button>
          </div>
        )}

        {activeTab === TABS.TASKS && (
          <div className={styles.form}>
            <div className={styles.field}>
              <label className={styles.label}>Title</label>
              <input className={styles.input} value={form.title} onChange={(e) => setForm({...form, title: e.target.value})} />
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Description</label>
              <textarea className={styles.textarea} value={form.description} onChange={(e) => setForm({...form, description: e.target.value})} />
            </div>
            <div className={styles.row}>
              <div className={styles.field}>
                <label className={styles.label}>Deadline</label>
                <input className={styles.input} type="date" value={form.deadline} onChange={(e) => setForm({...form, deadline: e.target.value})} />
              </div>
              <div className={styles.field}>
                <label className={styles.label}>Priority</label>
                <select className={styles.select} value={form.priority} onChange={(e) => setForm({...form, priority: e.target.value})}>
                  <option value="LOW">Low</option>
                  <option value="MEDIUM">Medium</option>
                  <option value="HIGH">High</option>
                  <option value="URGENT">Urgent</option>
                </select>
              </div>
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Status</label>
              <select className={styles.select} value={form.status} onChange={(e) => setForm({...form, status: e.target.value})}>
                <option value="PENDING">Pending</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="COMPLETED">Completed</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>
            <div className={styles.row}>
              <div className={styles.field}>
                <label className={styles.label}>Episode</label>
                <select className={styles.select} value={form.episodeId} onChange={(e) => setForm({...form, episodeId: e.target.value})}>
                  <option value="">Select Episode</option>
                  {episodes.map(ep => <option key={ep.id} value={ep.id}>{ep.title}</option>)}
                </select>
              </div>
              <div className={styles.field}>
                <label className={styles.label}>Assigned To</label>
                <select className={styles.select} value={form.assignedToId} onChange={(e) => setForm({...form, assignedToId: e.target.value})}>
                  <option value="">Select User</option>
                  {users.map(u => <option key={u.id} value={u.id}>{u.name}</option>)}
                </select>
              </div>
            </div>
            <button className={styles.saveBtn} onClick={handleSave}>Save</button>
          </div>
        )}

        {activeTab === TABS.RECORDING && (
          <div className={styles.form}>
            <div className={styles.row}>
              <div className={styles.field}>
                <label className={styles.label}>Recording Date</label>
                <input className={styles.input} type="date" value={form.recordingDate} onChange={(e) => setForm({...form, recordingDate: e.target.value})} />
              </div>
              <div className={styles.field}>
                <label className={styles.label}>Recording Time</label>
                <input className={styles.input} type="time" value={form.recordingTime} onChange={(e) => setForm({...form, recordingTime: e.target.value})} />
              </div>
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Meeting Link</label>
              <input className={styles.input} value={form.meetingLink} onChange={(e) => setForm({...form, meetingLink: e.target.value})} />
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Location</label>
              <input className={styles.input} value={form.location} onChange={(e) => setForm({...form, location: e.target.value})} />
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Notes</label>
              <textarea className={styles.textarea} value={form.notes} onChange={(e) => setForm({...form, notes: e.target.value})} />
            </div>
            <div className={styles.field}>
              <label className={styles.label}>Episode</label>
              <select className={styles.select} value={form.episodeId} onChange={(e) => setForm({...form, episodeId: e.target.value})}>
                <option value="">Select Episode</option>
                {episodes.map(ep => <option key={ep.id} value={ep.id}>{ep.title}</option>)}
              </select>
            </div>
            <button className={styles.saveBtn} onClick={handleSave}>Save</button>
          </div>
        )}
      </Modal>

      <ConfirmDialog
        isOpen={confirmOpen}
        onConfirm={handleDelete}
        onCancel={() => setConfirmOpen(false)}
        title={getDeleteTitle()}
        message={getDeleteMessage()}
      />
    </div>
  );
}
