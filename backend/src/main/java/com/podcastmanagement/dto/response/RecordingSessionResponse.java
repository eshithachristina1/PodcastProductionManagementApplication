package com.podcastmanagement.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class RecordingSessionResponse {

    private Long id;
    private LocalDate recordingDate;
    private LocalTime recordingTime;
    private String meetingLink;
    private String location;
    private String notes;
    private Long episodeId;
    private String episodeTitle;

    public RecordingSessionResponse() {}

    public RecordingSessionResponse(Long id, LocalDate recordingDate, LocalTime recordingTime, String meetingLink,
                                    String location, String notes, Long episodeId, String episodeTitle) {
        this.id = id;
        this.recordingDate = recordingDate;
        this.recordingTime = recordingTime;
        this.meetingLink = meetingLink;
        this.location = location;
        this.notes = notes;
        this.episodeId = episodeId;
        this.episodeTitle = episodeTitle;
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
    public Long getEpisodeId() { return episodeId; }
    public void setEpisodeId(Long episodeId) { this.episodeId = episodeId; }
    public String getEpisodeTitle() { return episodeTitle; }
    public void setEpisodeTitle(String episodeTitle) { this.episodeTitle = episodeTitle; }
}
