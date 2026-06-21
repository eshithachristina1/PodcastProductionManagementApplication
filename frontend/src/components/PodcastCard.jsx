import { Link } from 'react-router-dom';
import styles from './PodcastCard.module.css';

export default function PodcastCard({ podcast }) {
  if (!podcast) return null;

  return (
    <Link to={`/podcasts/${podcast.id}`} className={styles.card}>
      <div className={styles.header}>
        <div className={styles.avatar}>
          {podcast.title?.charAt(0)?.toUpperCase() || 'P'}
        </div>
        <span className={styles.category}>{podcast.category || 'General'}</span>
      </div>
      <h3 className={styles.title}>{podcast.title}</h3>
      {podcast.description && (
        <p className={styles.description}>{podcast.description}</p>
      )}
      <div className={styles.meta}>
        <span className={styles.host}>{podcast.hostName || 'Unknown Host'}</span>
        <span className={styles.seasons}>
          {podcast.seasonCount ?? 0} {podcast.seasonCount === 1 ? 'Season' : 'Seasons'}
        </span>
      </div>
    </Link>
  );
}
