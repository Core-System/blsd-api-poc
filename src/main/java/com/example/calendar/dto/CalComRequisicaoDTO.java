package com.example.calendar.dto;

public class CalComRequisicaoDTO {
    private Integer eventTypeId;
    private String start;
    private AttendeeDTO attendee;

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public AttendeeDTO getAttendee() {
        return attendee;
    }

    public void setAttendee(AttendeeDTO attendee) {
        this.attendee = attendee;
    }
}
