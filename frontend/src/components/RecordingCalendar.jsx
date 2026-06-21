import styles from './RecordingCalendar.module.css';

export default function RecordingCalendar({ sessions }) {
  if (!sessions || sessions.length === 0) {
    return (
      <div className={styles.empty}>
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#d1d5db" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
          <line x1="16" y1="2" x2="16" y2="6" />
          <line x1="8" y1="2" x2="8" y2="6" />
          <line x1="3" y1="10" x2="21" y2="10" />
        </svg>
        <p>No recording sessions scheduled</p>
      </div>
    );
  }

  const formatDate = (date) => {
    if (!date) return '—';
    return new Date(date).toLocaleDateString('en-US', {
      weekday: 'short',
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  const formatTime = (date) => {
    if (!date) return '—';
    return new Date(date).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className={styles.list}>
      {sessions.map((session, idx) => (
        <div key={session.id || idx} className={styles.session}>
          <div className={styles.dateCol}>
            <span className={styles.date}>{formatDate(session.date)}</span>
            <span className={styles.time}>{formatTime(session.date)}</span>
          </div>
          <div className={styles.details}>
            {session.episodeTitle && (
              <span className={styles.episodeTitle}>{session.episodeTitle}</span>
            )}
            {session.meetingLink ? (
              <a
                href={session.meetingLink}
                target="_blank"
                rel="noopener noreferrer"
                className={styles.meetingLink}
              >
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M18 13v6a2 2 0 01-2 2H5a2 2 0 01-2-2V8a2 2 0 012-2h6" />
                  <polyline points="15 3 21 3 21 9" />
                  <line x1="10" y1="14" x2="21" y2="3" />
                </svg>
                Join Meeting
              </a>
            ) : (
              <span className={styles.noLink}>No meeting link</span>
            )}
          </div>
        </div>
      ))}
    </div>
  );
}
