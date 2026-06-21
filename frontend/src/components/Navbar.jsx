import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import styles from './Navbar.module.css';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className={styles.navbar}>
      <div className={styles.left}>
        <span className={styles.greeting}>Welcome, {user?.name || 'User'}</span>
        {user?.teamName && (
          <span className={styles.teamBadge}>{user.teamName}</span>
        )}
      </div>
      <div className={styles.right}>
        <div className={styles.profile}>
          <Link to="/profile" className={styles.profileLink}>
            <div className={styles.avatar}>
              {user?.name?.charAt(0)?.toUpperCase() || 'U'}
            </div>
            <div className={styles.profileInfo}>
              <span className={styles.userName}>{user?.name || 'User'}</span>
              <span className={styles.userRole}>{user?.role || ''}</span>
            </div>
          </Link>
        </div>
        <button className={styles.logoutBtn} onClick={handleLogout}>
          Logout
        </button>
      </div>
    </nav>
  );
}
