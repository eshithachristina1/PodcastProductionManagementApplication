import styles from './Footer.module.css';

export default function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className={styles.footer}>
      <span>&copy; {year} PodManage. All rights reserved.</span>
    </footer>
  );
}
