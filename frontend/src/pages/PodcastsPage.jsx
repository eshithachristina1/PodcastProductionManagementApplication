import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import DataTable from '../components/DataTable';
import Modal from '../components/Modal';
import ConfirmDialog from '../components/ConfirmDialog';
import SearchBar from '../components/SearchBar';
import styles from './PodcastsPage.module.css';

export default function PodcastsPage() {
  const { user } = useAuth();
  const [podcasts, setPodcasts] = useState([]);
  const [seasons, setSeasons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  const [podcastModalOpen, setPodcastModalOpen] = useState(false);
  const [seasonModalOpen, setSeasonModalOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);
  const [confirmType, setConfirmType] = useState(null);

  const [podcastForm, setPodcastForm] = useState({ title: '', description: '', category: '', hostName: '' });
  const [seasonForm, setSeasonForm] = useState({ seasonNumber: '', title: '', podcastId: '' });

  useEffect(() => { fetchData(); }, []);

  const fetchData = async () => {
    try {
      const [podcastsRes, seasonsRes] = await Promise.all([
        api.get('/podcasts'),
        api.get('/seasons')
      ]);
      setPodcasts(podcastsRes.data.data || []);
      setSeasons(seasonsRes.data.data || []);
    } catch (err) {
      toast.error('Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  const handleSavePodcast = async () => {
    try {
      if (selectedItem) {
        await api.put(`/podcasts/${selectedItem.id}`, podcastForm);
        toast.success('Podcast updated successfully');
      } else {
        await api.post('/podcasts', podcastForm);
        toast.success('Podcast created successfully');
      }
      setPodcastModalOpen(false);
      setSelectedItem(null);
      setPodcastForm({ title: '', description: '', category: '', hostName: '' });
      fetchData();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not complete this operation. Check your connection.');
    }
  };

  const handleSaveSeason = async () => {
    try {
      if (selectedItem) {
        await api.put(`/seasons/${selectedItem.id}`, seasonForm);
        toast.success('Season updated successfully');
      } else {
        await api.post('/seasons', seasonForm);
        toast.success('Season created successfully');
      }
      setSeasonModalOpen(false);
      setSelectedItem(null);
      setSeasonForm({ seasonNumber: '', title: '', podcastId: '' });
      fetchData();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not complete this operation. Check your connection.');
    }
  };

  const handleDelete = async () => {
    try {
      const endpoint = confirmType === 'podcast' ? `/podcasts/${selectedItem.id}` : `/seasons/${selectedItem.id}`;
      await api.delete(endpoint);
      toast.success(confirmType === 'podcast' ? 'Podcast deleted successfully' : 'Season deleted successfully');
      setConfirmOpen(false);
      setSelectedItem(null);
      setConfirmType(null);
      fetchData();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not delete. Check your connection.');
    }
  };

  const openPodcastEdit = (podcast) => {
    setSelectedItem(podcast);
    setPodcastForm({
      title: podcast.title,
      description: podcast.description || '',
      category: podcast.category || '',
      hostName: podcast.hostName || ''
    });
    setPodcastModalOpen(true);
  };

  const openPodcastDelete = (podcast) => {
    setSelectedItem(podcast);
    setConfirmType('podcast');
    setConfirmOpen(true);
  };

  const openSeasonEdit = (season) => {
    setSelectedItem(season);
    setSeasonForm({
      seasonNumber: season.seasonNumber || '',
      title: season.title,
      podcastId: season.podcastId || ''
    });
    setSeasonModalOpen(true);
  };

  const openSeasonDelete = (season) => {
    setSelectedItem(season);
    setConfirmType('season');
    setConfirmOpen(true);
  };

  const openAddPodcast = () => {
    setSelectedItem(null);
    setPodcastForm({ title: '', description: '', category: '', hostName: '' });
    setPodcastModalOpen(true);
  };

  const openAddSeason = () => {
    setSelectedItem(null);
    setSeasonForm({ seasonNumber: '', title: '', podcastId: '' });
    setSeasonModalOpen(true);
  };

  const combinedData = [];
  podcasts.forEach(p => {
    const podcastSeasons = seasons.filter(s => s.podcastId === p.id);
    if (podcastSeasons.length === 0) {
      combinedData.push({
        podcastTitle: p.title, seasonNumber: '—', seasonTitle: '—', episodeCount: 0,
        podcastId: p.id, type: 'podcast', podcast: p
      });
    } else {
      podcastSeasons.forEach(s => {
        combinedData.push({
          podcastTitle: p.title, seasonNumber: s.seasonNumber, seasonTitle: s.title,
          episodeCount: s.episodeCount, podcastId: p.id, seasonId: s.id,
          type: 'season', season: s, podcast: p
        });
      });
    }
  });

  const filteredData = combinedData.filter(item => {
    if (!searchTerm) return true;
    const q = searchTerm.toLowerCase();
    return (
      (item.podcastTitle || '').toLowerCase().includes(q) ||
      String(item.seasonNumber || '').includes(q) ||
      (item.seasonTitle || '').toLowerCase().includes(q)
    );
  });

  const mergedColumns = [
    { key: 'podcastTitle', label: 'Podcast Title' },
    { key: 'seasonNumber', label: 'Season #' },
    { key: 'seasonTitle', label: 'Season Title' },
    { key: 'episodeCount', label: 'Episode Count' },
    {
      key: 'actions', label: 'Actions',
      render: (_, row) => canManage ? (
        <div style={{ display: 'flex', gap: 6 }}>
          <button className="btn btn-sm btn-secondary" onClick={() => openEditMerged(row)}>Edit</button>
          <button className="btn btn-sm btn-danger" onClick={() => openDeleteMerged(row)}>Delete</button>
        </div>
      ) : null
    }
  ];

  const openEditMerged = (row) => {
    if (row.type === 'season') {
      setSelectedItem(row.season);
      setSeasonForm({
        seasonNumber: row.season.seasonNumber || '',
        title: row.season.title,
        podcastId: row.season.podcastId || ''
      });
      setSeasonModalOpen(true);
    } else {
      setSelectedItem(row.podcast);
      setPodcastForm({
        title: row.podcast.title,
        description: row.podcast.description || '',
        category: row.podcast.category || '',
        hostName: row.podcast.hostName || ''
      });
      setPodcastModalOpen(true);
    }
  };

  const openDeleteMerged = (row) => {
    setSelectedItem(row.type === 'season' ? row.season : row.podcast);
    setConfirmType(row.type === 'season' ? 'season' : 'podcast');
    setConfirmOpen(true);
  };

  const canManage = ['ADMIN', 'HOST', 'PRODUCER'].includes(user?.role);

  if (loading) return <div className={styles.loading}>Loading...</div>;

  return (
    <div className={styles.page}>
      <h1 className={styles.pageTitle}>Podcasts & Seasons</h1>

      <div className={styles.headerRow}>
        {canManage && (
          <div className={styles.headerActions}>
            <button className={styles.addBtn} onClick={openAddPodcast}>+ Add Podcast</button>
            <button className={styles.addBtn} onClick={openAddSeason} style={{ marginLeft: 8 }}>+ Add Season</button>
          </div>
        )}
      </div>
      <SearchBar value={searchTerm} onChange={setSearchTerm} placeholder="Search podcasts, seasons..." />
      <DataTable columns={mergedColumns} data={filteredData} />

      <Modal isOpen={podcastModalOpen} onClose={() => setPodcastModalOpen(false)} title={selectedItem && confirmType !== 'season' ? 'Edit Podcast' : 'Add Podcast'}>
        <div className="form-group">
          <label>Title</label>
          <input value={podcastForm.title} onChange={(e) => setPodcastForm({...podcastForm, title: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Description</label>
          <textarea value={podcastForm.description} onChange={(e) => setPodcastForm({...podcastForm, description: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Category</label>
          <input value={podcastForm.category} onChange={(e) => setPodcastForm({...podcastForm, category: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Host Name</label>
          <input value={podcastForm.hostName} onChange={(e) => setPodcastForm({...podcastForm, hostName: e.target.value})} />
        </div>
        <button className="btn btn-primary mt-20" onClick={handleSavePodcast}>Save</button>
      </Modal>

      <Modal isOpen={seasonModalOpen} onClose={() => setSeasonModalOpen(false)} title={selectedItem && confirmType !== 'podcast' ? 'Edit Season' : 'Add Season'}>
        <div className="form-group">
          <label>Season Number</label>
          <input type="number" value={seasonForm.seasonNumber} onChange={(e) => setSeasonForm({...seasonForm, seasonNumber: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Title</label>
          <input value={seasonForm.title} onChange={(e) => setSeasonForm({...seasonForm, title: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Podcast</label>
          <select value={seasonForm.podcastId} onChange={(e) => setSeasonForm({...seasonForm, podcastId: e.target.value})}>
            <option value="">Select Podcast</option>
            {podcasts.map(p => <option key={p.id} value={p.id}>{p.title}</option>)}
          </select>
        </div>
        <button className="btn btn-primary mt-20" onClick={handleSaveSeason}>Save</button>
      </Modal>

      <ConfirmDialog
        isOpen={confirmOpen}
        onConfirm={handleDelete}
        onCancel={() => { setConfirmOpen(false); setSelectedItem(null); setConfirmType(null); }}
        title={confirmType === 'podcast' ? 'Delete Podcast' : 'Delete Season'}
        message={`Are you sure you want to delete ${selectedItem?.title}?`}
      />
    </div>
  );
}
