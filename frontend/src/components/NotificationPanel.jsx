import styles from './NotificationPanel.module.css';

export default function NotificationPanel({ notifications, onMarkAsRead }) {
  if (!notifications || notifications.length === 0) {
    return (
      <div className={styles.empty}>
        <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#d1d5db" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
          <path d="M13.73 21a2 2 0 0 1-3.46 0" />
        </svg>
        <p>No notifications</p>
      </div>
    );
  }

  const formatTimestamp = (ts) => {
    if (!ts) return '';
    const date = new Date(ts);
    const now = new Date();
    const diff = now - date;
    const mins = Math.floor(diff / 60000);
    if (mins < 1) return 'Just now';
    if (mins < 60) return `${mins}m ago`;
    const hrs = Math.floor(mins / 60);
    if (hrs < 24) return `${hrs}h ago`;
    const days = Math.floor(hrs / 24);
    if (days < 7) return `${days}d ago`;
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
  };

  return (
    <div className={styles.list}>
      {notifications.map((n) => (
        <div
          key={n.id}
          className={`${styles.item} ${!n.read ? styles.unread : ''}`}
        >
          <div className={styles.content}>
            <span className={styles.title}>{n.title}</span>
            {n.message && <p className={styles.message}>{n.message}</p>}
            <span className={styles.time}>{formatTimestamp(n.timestamp)}</span>
          </div>
          {!n.read && onMarkAsRead && (
            <button
              className={styles.markBtn}
              onClick={() => onMarkAsRead(n.id)}
              title="Mark as read"
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <polyline points="9 11 12 14 22 4" />
                <path d="M21 12v7a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2h11" />
              </svg>
            </button>
          )}
        </div>
      ))}
    </div>
  );
}
