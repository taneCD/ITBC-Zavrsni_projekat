package com.example.FinalProjectRev2.model;

import java.util.Date;

public class LogSearchReturn {
    String message;
    int logType;
    Date createdDate;

    public LogSearchReturn() {
    }
    public LogSearchReturn(String message, int logType, Date createdDate) {
        this.message = message;
        this.logType = logType;
        this.createdDate = createdDate;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getLogType() {
        return logType;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
