package com.example.FinalProjectRev2.model;
import java.time.LocalDateTime;
import java.util.UUID;

public class Log {
    private UUID id;
    private String message;
    private int logType;
    private LocalDateTime createdDate;

    public enum LogType{
        ERROR,
        WARNING,
        INFO,
        UNKNOWN
    }
    public Log() {
    }
    public int getLogTypeInt(){
        return this.logType;
    }
    public Log(UUID id, String message, int logType, LocalDateTime createdDate) {
        this.id = id;
        this.message = message;
        this.logType = logType;
        this.createdDate = createdDate;
    }
    public void generateRandomId() {
        this.id= UUID.randomUUID();
    }
    public void setCurrentDate(){
        createdDate=LocalDateTime.now();
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LogType getLogType() {
        if (logType == 0) {
            return LogType.ERROR;
        } else if (logType == 1) {
            return LogType.WARNING;
        } else if (logType == 2) {
            return LogType.INFO;
        } else {
            return LogType.UNKNOWN;
        }
    }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", logType=" + logType +
                ", createdDate=" + createdDate +
                '}';
    }
}
