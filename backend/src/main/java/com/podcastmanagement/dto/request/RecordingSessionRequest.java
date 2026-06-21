package com.podcastmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class RecordingSessionRequest {

    @NotNull(message = "Recording date is required")
    private LocalDate recordingDate;

    private LocalTime recordingTime;
    private String meetingLink;
    private String location;
    private String notes;

    @NotNull(message = "Episode ID is required")
    private Long episodeId;

    public RecordingSessionRequest() {}

    public RecordingSessionRequest(LocalDate recordingDate, LocalTime recordingTime, String meetingLink,
                                   String location, String notes, Long episodeId) {
        this.recordingDate = recordingDate;
        this.recordingTime = recordingTime;
        this.meetingLink = meetingLink;
        this.location = location;
        this.notes = notes;
        this.episodeId = episodeId;
    }

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
}
