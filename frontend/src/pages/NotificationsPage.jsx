import { useState, useEffect } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import Modal from '../components/Modal';
import styles from './NotificationsPage.module.css';

const TABS = { INBOX: 'inbox', SENT: 'sent' };

export default function NotificationsPage() {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState(TABS.INBOX);
  const [notifications, setNotifications] = useState([]);
  const [sentMessages, setSentMessages] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [composeOpen, setComposeOpen] = useState(false);
  const [users, setUsers] = useState([]);
  const [form, setForm] = useState({ userId: '', title: '', message: '' });
  const [attachment, setAttachment] = useState(null);
  const [sending, setSending] = useState(false);

  useEffect(() => {
    if (user) fetchAll();
  }, [user]);

  const fetchAll = async () => {
    setLoading(true);
    try {
      const [notifRes, unreadRes, sentRes] = await Promise.all([
        api.get(`/notifications/user/${user.id}`),
        api.get(`/notifications/user/${user.id}/unread-count`),
        api.get(`/notifications/sent/${user.id}`)
      ]);
      setNotifications(notifRes.data.data || []);
      setUnreadCount(unreadRes.data.data || 0);
      setSentMessages(sentRes.data.data || []);
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not complete this operation. Check your connection.');
    } finally {
      setLoading(false);
    }
  };

  const handleMarkRead = async (id) => {
    try {
      await api.put(`/notifications/${id}/read`);
      toast.success('Notification marked as read');
      fetchAll();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not complete this operation. Check your connection.');
    }
  };

  const openCompose = async (replyTo) => {
    try {
      const res = await api.get('/users');
      setUsers(res.data.data || []);
      setForm({
        userId: replyTo?.senderId || '',
        title: replyTo ? `Re: ${replyTo.title}` : '',
        message: replyTo ? `\n\n--- Original message from ${replyTo.senderName} ---\n${replyTo.message}` : '',
      });
      setComposeOpen(true);
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not complete this operation. Check your connection.');
    }
  };

  const handleSend = async (e) => {
    e.preventDefault();
    if (!form.userId) {
      toast.error('Please select a recipient');
      return;
    }
    setSending(true);
    try {
      const formData = new FormData();
      formData.append('title', form.title);
      formData.append('message', form.message);
      formData.append('userId', form.userId);
      if (attachment) {
        formData.append('file', attachment);
      }
      await api.post('/notifications', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      toast.success('Notification sent');
      setComposeOpen(false);
      setAttachment(null);
      fetchAll();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Could not complete this operation. Check your connection.');
    } finally {
      setSending(false);
    }
  };

  const currentList = activeTab === TABS.INBOX ? notifications : sentMessages;

  if (loading) return <div className={styles.loading}>Loading notifications...</div>;

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h1 className={styles.title}>Notifications</h1>
          {unreadCount > 0 && activeTab === TABS.INBOX && (
            <span className={styles.unreadBadge}>{unreadCount} unread</span>
          )}
        </div>
        <button className={styles.composeBtn} onClick={openCompose}>
          + New Message
        </button>
      </div>

      <div className={styles.tabs} style={{ display: 'flex', gap: 0, marginBottom: 16 }}>
        <button
          className={activeTab === TABS.INBOX ? 'btn btn-primary btn-sm' : 'btn btn-secondary btn-sm'}
          onClick={() => setActiveTab(TABS.INBOX)}
          style={{ borderRadius: '8px 0 0 8px' }}
        >
          Inbox
        </button>
        <button
          className={activeTab === TABS.SENT ? 'btn btn-primary btn-sm' : 'btn btn-secondary btn-sm'}
          onClick={() => setActiveTab(TABS.SENT)}
          style={{ borderRadius: '0 8px 8px 0' }}
        >
          Sent
        </button>
      </div>

      {currentList.length === 0 ? (
        <div className={styles.empty}>
          <p className={styles.emptyText}>
            {activeTab === TABS.INBOX ? 'No notifications yet' : 'No sent messages'}
          </p>
        </div>
      ) : (
        <ul className={styles.list}>
          {currentList.map((notif) => (
            <li
              key={notif.id}
              className={`${styles.notification} ${!notif.isRead && activeTab === TABS.INBOX ? styles.unread : ''}`}
            >
              <div className={styles.notifDot}>
                {!notif.isRead && activeTab === TABS.INBOX && <span className={styles.dot} />}
              </div>
              <div className={styles.notifBody}>
                <p className={styles.notifMeta}>
                  <strong>
                    {activeTab === TABS.INBOX
                      ? (notif.senderName || notif.userName || 'Unknown')
                      : (notif.userName || 'Unknown')}
                  </strong>
                  {activeTab === TABS.INBOX ? ' sent you a message' : ''}
                </p>
                <p className={styles.notifTitle} style={{ fontWeight: 600, margin: '0 0 4px 0' }}>{notif.title}</p>
                <p className={styles.notifMessage}>{notif.message}</p>
                {notif.filePath && (
                  <a
                    className={styles.attachmentLink}
                    href={`/api/notifications/download/${notif.id}`}
                    download
                  >
                    Download Attachment
                  </a>
                )}
                <span className={styles.notifTime}>
                  {notif.createdAt ? new Date(notif.createdAt).toLocaleString() : ''}
                </span>
              </div>
              <div className={styles.notifActions}>
                {activeTab === TABS.INBOX && (
                  <button className={styles.replyBtn} onClick={() => openCompose(notif)}>
                    Reply
                  </button>
                )}
                {!notif.isRead && activeTab === TABS.INBOX && (
                  <button className={styles.readBtn} onClick={() => handleMarkRead(notif.id)}>
                    Mark Read
                  </button>
                )}
              </div>
            </li>
          ))}
        </ul>
      )}

      <Modal isOpen={composeOpen} onClose={() => { setComposeOpen(false); setAttachment(null); }} title="New Message">
        <form className={styles.form} onSubmit={handleSend}>
          <div className={styles.field}>
            <label className={styles.label}>Recipient</label>
            <select
              className={styles.select}
              value={form.userId}
              onChange={(e) => setForm({ ...form, userId: e.target.value })}
            >
              <option value="">-- Select a user --</option>
              {users.map((u) => (
                <option key={u.id} value={u.id}>
                  {u.name || u.username || u.email}
                </option>
              ))}
            </select>
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Title</label>
            <input
              className={styles.input}
              type="text"
              value={form.title}
              onChange={(e) => setForm({ ...form, title: e.target.value })}
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Message</label>
            <textarea
              className={styles.textarea}
              rows="4"
              value={form.message}
              onChange={(e) => setForm({ ...form, message: e.target.value })}
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Attachment (optional)</label>
            <input type="file" onChange={(e) => setAttachment(e.target.files[0] || null)} />
          </div>
          <div className={styles.formActions}>
            <button type="button" className={styles.cancelBtn} onClick={() => { setComposeOpen(false); setAttachment(null); }}>
              Cancel
            </button>
            <button type="submit" className={styles.sendBtn} disabled={sending}>
              {sending ? 'Sending...' : 'Send'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
