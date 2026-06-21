import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import StatusBadge from '../components/StatusBadge';
import styles from './DashboardPage.module.css';

export default function DashboardPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [dashData, setDashData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboard();
  }, []);

  const fetchDashboard = async () => {
    try {
      const res = await api.get('/dashboard');
      setDashData(res.data.data);
    } catch (err) {
      toast.error('Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading dashboard...</div>;
  }

  const chartData = dashData?.episodesByStatus
    ? Object.entries(dashData.episodesByStatus).map(([status, count]) => ({ status, count }))
    : [];

  const upcomingRecordings = dashData?.upcomingRecordings || [];
  const recentNotifications = dashData?.recentNotifications || [];

  return (
    <div className={styles.page}>
      <div className={styles.headerRow}>
        <div>
          <h1 className={styles.title}>Dashboard</h1>
          <p className={styles.subtitle}>Welcome back, {user?.name || 'User'}</p>
        </div>
        {user?.teamName && (
          <div className={styles.teamBadge}>{user.teamName}</div>
        )}
      </div>

      <div className={styles.cards}>
        <div className={styles.card}>
          <div className={styles.cardIcon}>&#127911;</div>
          <div className={styles.cardInfo}>
            <span className={styles.cardValue}>{dashData?.totalEpisodes || 0}</span>
            <span className={styles.cardLabel}>Total Episodes</span>
          </div>
        </div>
        <div className={styles.card}>
          <div className={styles.cardIcon}>&#128196;</div>
          <div className={styles.cardInfo}>
            <span className={styles.cardValue}>{dashData?.totalPodcasts || 0}</span>
            <span className={styles.cardLabel}>Podcasts</span>
          </div>
        </div>
        <div className={styles.card}>
          <div className={styles.cardIcon}>&#128203;</div>
          <div className={styles.cardInfo}>
            <span className={styles.cardValue}>{dashData?.pendingTasks || 0}</span>
            <span className={styles.cardLabel}>Pending Tasks</span>
          </div>
        </div>
        <div className={styles.card}>
          <div className={styles.cardIcon}>&#9989;</div>
          <div className={styles.cardInfo}>
            <span className={styles.cardValue}>{dashData?.completedTasks || 0}</span>
            <span className={styles.cardLabel}>Completed Tasks</span>
          </div>
        </div>
      </div>

      <div className={styles.grid}>
        <div className={styles.chartSection}>
          <h2 className={styles.sectionTitle}>Episodes by Status</h2>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="status" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="count" fill="#6366f1" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div className={styles.recordingsSection}>
          <h2 className={styles.sectionTitle}>Upcoming Recordings</h2>
          {upcomingRecordings.length === 0 ? (
            <p className={styles.emptyText}>No upcoming recordings</p>
          ) : (
            <ul className={styles.recordingsList}>
              {upcomingRecordings.map((rec) => (
                <li key={rec.id} className={styles.recordingItem}>
                  <div className={styles.recordingDate}>
                    {rec.recordingDate ? new Date(rec.recordingDate).toLocaleDateString() : 'TBD'}
                  </div>
                  <div className={styles.recordingInfo}>
                    <span className={styles.recordingTitle}>{rec.episodeTitle || 'Untitled'}</span>
                    <span className={styles.recordingEpisode}>
                      {rec.recordingTime ? `@ ${rec.recordingTime}` : ''}
                    </span>
                  </div>
                </li>
              ))}
            </ul>
          )}
          <button className={styles.viewAllBtn} onClick={() => navigate('/production-board')}>
            View All Recordings
          </button>
        </div>
      </div>

      <div className={styles.grid}>
        <div className={styles.notificationsSection}>
          <h2 className={styles.sectionTitle}>Recent Notifications</h2>
          {recentNotifications.length === 0 ? (
            <p className={styles.emptyText}>No notifications</p>
          ) : (
            <ul className={styles.notificationList}>
              {recentNotifications.map((notif) => (
                <li key={notif.id} className={`${styles.notificationItem} ${!notif.isRead ? styles.unread : ''}`}>
                  <span className={styles.notifMessage}>{notif.message || notif.title}</span>
                  <span className={styles.notifTime}>
                    {notif.createdAt ? new Date(notif.createdAt).toLocaleDateString() : ''}
                  </span>
                </li>
              ))}
            </ul>
          )}
          <button className={styles.viewAllBtn} onClick={() => navigate('/notifications')}>
            View All Notifications
          </button>
        </div>

        <div className={styles.tasksSection}>
          <h2 className={styles.sectionTitle}>My Tasks</h2>
          <div className={styles.taskSummary}>
            <div className={styles.taskStat}>
              <span className={styles.taskStatValue}>{dashData?.tasksByUser?.[user.id] || 0}</span>
              <span className={styles.taskStatLabel}>Assigned to me</span>
            </div>
          </div>
          <button className={styles.viewAllBtn} onClick={() => navigate('/production-board')}>
            View All Tasks
          </button>
        </div>
      </div>
    </div>
  );
}
