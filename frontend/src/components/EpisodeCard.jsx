import { Link } from 'react-router-dom';
import StatusBadge from './StatusBadge';
import styles from './EpisodeCard.module.css';

export default function EpisodeCard({ episode }) {
  if (!episode) return null;

  const formatDuration = (sec) => {
    if (!sec && sec !== 0) return '--:--';
    const m = Math.floor(sec / 60);
    const s = sec % 60;
    return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`;
  };

  const formatDate = (date) => {
    if (!date) return 'TBD';
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  return (
    <Link to={`/production-board`} className={styles.card}>
      <div className={styles.episodeNum}>EP {episode.episodeNumber}</div>
      <h3 className={styles.title}>{episode.title}</h3>
      <div className={styles.meta}>
        <span className={styles.duration}>{formatDuration(episode.duration)}</span>
        <span className={styles.date}>{formatDate(episode.publishDate)}</span>
      </div>
      <div className={styles.badgeWrap}>
        <StatusBadge status={episode.status} />
      </div>
    </Link>
  );
}
