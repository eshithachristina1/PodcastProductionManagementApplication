import styles from './StatusBadge.module.css';

const statusColors = {
  completed: '#22c55e',
  ready: '#22c55e',
  published: '#22c55e',
  active: '#22c55e',
  approved: '#22c55e',
  in_progress: '#3b82f6',
  inprogress: '#3b82f6',
  recording: '#3b82f6',
  processing: '#3b82f6',
  pending: '#f59e0b',
  scheduled: '#f59e0b',
  draft: '#f59e0b',
  cancelled: '#ef4444',
  failed: '#ef4444',
  overdue: '#ef4444',
  archived: '#6b7280',
  inactive: '#6b7280',
};

export default function StatusBadge({ status }) {
  if (!status) return null;

  const normalized = status.toLowerCase().replace(/\s+/g, '_');
  const color = statusColors[normalized] || '#6b7280';

  return (
    <span
      className={styles.badge}
      style={{
        background: `${color}15`,
        color,
      }}
    >
      {status}
    </span>
  );
}
