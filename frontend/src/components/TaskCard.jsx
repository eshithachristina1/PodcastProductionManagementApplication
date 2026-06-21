import StatusBadge from './StatusBadge';
import styles from './TaskCard.module.css';

const priorityColors = {
  high: '#ef4444',
  medium: '#f59e0b',
  low: '#22c55e',
};

export default function TaskCard({ task }) {
  if (!task) return null;

  const formatDate = (date) => {
    if (!date) return 'No deadline';
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  return (
    <div className={styles.card}>
      <div className={styles.top}>
        <StatusBadge status={task.status} />
        <span
          className={styles.priority}
          style={{
            background: `${priorityColors[task.priority] || '#6b7280'}15`,
            color: priorityColors[task.priority] || '#6b7280',
          }}
        >
          {task.priority || 'normal'}
        </span>
      </div>
      <h3 className={styles.title}>{task.title}</h3>
      <div className={styles.meta}>
        <div className={styles.metaItem}>
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
            <line x1="16" y1="2" x2="16" y2="6" />
            <line x1="8" y1="2" x2="8" y2="6" />
            <line x1="3" y1="10" x2="21" y2="10" />
          </svg>
          <span>{formatDate(task.deadline)}</span>
        </div>
        {task.assignedToName && (
          <div className={styles.metaItem}>
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" />
              <circle cx="12" cy="7" r="4" />
            </svg>
            <span>{task.assignedToName}</span>
          </div>
        )}
      </div>
    </div>
  );
}
