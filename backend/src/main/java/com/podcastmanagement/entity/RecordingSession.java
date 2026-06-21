package com.podcastmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "recording_sessions")
public class RecordingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate recordingDate;

    private LocalTime recordingTime;

    private String meetingLink;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    public RecordingSession() {}

    public RecordingSession(Long id, LocalDate recordingDate, LocalTime recordingTime, String meetingLink,
                            String location, String notes) {
        this.id = id;
        this.recordingDate = recordingDate;
        this.recordingTime = recordingTime;
        this.meetingLink = meetingLink;
        this.location = location;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getRecordingDate() { return recordingDate; }
    public void setRecordingDate(LocalDate recordingDate) { this.recordingDate = recordingDate; }
    public LocalTime getRecordingTime() { return recordingTime; }
    public void setRecordingTime(LocalTime recordingTime) { this.recordingTime = recordingTime; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Episode getEpisode() { return episode; }
    public void setEpisode(Episode episode) { this.episode = episode; }
}
