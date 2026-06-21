import Modal from './Modal';
import styles from './ConfirmDialog.module.css';

export default function ConfirmDialog({ isOpen, onConfirm, onCancel, title, message }) {
  return (
    <Modal isOpen={isOpen} onClose={onCancel} title={title || 'Confirm'}>
      {message && <p className={styles.message}>{message}</p>}
      <div className={styles.actions}>
        <button className={styles.cancelBtn} onClick={onCancel}>
          Cancel
        </button>
        <button className={styles.confirmBtn} onClick={onConfirm}>
          Confirm
        </button>
      </div>
    </Modal>
  );
}
