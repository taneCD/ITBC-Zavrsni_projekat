package com.example.FinalProjectRev2.model;

public class LogCount {
    int TotalLogs;
    String username;

    public LogCount() {
    }
    public LogCount(int totalLogs, String username) {
        TotalLogs = totalLogs;
        this.username = username;
    }
    public int getTotalLogs() {
        return TotalLogs;
    }

    public void setTotalLogs(int totalLogs) {
        TotalLogs = totalLogs;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
