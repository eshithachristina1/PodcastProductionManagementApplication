import styles from './DashboardCards.module.css';

export default function DashboardCards({ totalPodcasts, totalEpisodes, pendingTasks, completedTasks }) {
  const cards = [
    {
      label: 'Total Podcasts',
      value: totalPodcasts ?? 0,
      color: '#4f46e5',
      icon: 'M19 11a7 7 0 01-7 7m0 0a7 7 0 01-7-7m7 7v4m0-11a3 3 0 00-3-3m3 3a3 3 0 013 3m0 0a3 3 0 01-3 3m0-6a3 3 0 013 3m0 0a3 3 0 01-3 3',
    },
    {
      label: 'Total Episodes',
      value: totalEpisodes ?? 0,
      color: '#22c55e',
      icon: 'M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3',
    },
    {
      label: 'Pending Tasks',
      value: pendingTasks ?? 0,
      color: '#f59e0b',
      icon: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z',
    },
    {
      label: 'Completed Tasks',
      value: completedTasks ?? 0,
      color: '#3b82f6',
      icon: 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z',
    },
  ];

  return (
    <div className={styles.grid}>
      {cards.map((card) => (
        <div key={card.label} className={styles.card}>
          <div className={styles.cardLeft}>
            <span className={styles.cardLabel}>{card.label}</span>
            <span className={styles.cardValue}>{card.value}</span>
          </div>
          <div className={styles.iconWrap} style={{ background: `${card.color}15`, color: card.color }}>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d={card.icon} />
            </svg>
          </div>
        </div>
      ))}
    </div>
  );
}
