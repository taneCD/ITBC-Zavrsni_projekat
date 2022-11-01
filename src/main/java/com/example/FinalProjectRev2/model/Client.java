package com.example.FinalProjectRev2.model;
import java.util.UUID;

//@Table(name="clients")
public class Client {
    private String username;
    private String password;
    private String email;
    private boolean isAdmin;
    private UUID id;
    private int logCount;
    private int clientType;

    public enum ClientType{
        FREE,
        REGULAR,
        PREMIUM,
    }
    public int getclientTypeInt(){
        return this.clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }
    public Client() {
    }
    public Client(String username, String password, String email, int clientType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.clientType=clientType;
    }
    public int getLogCount() {
        return logCount;
    }
    public void setLogCount(int logCount) {
        this.logCount = logCount;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void generateRandomId() {
        this.id = UUID.randomUUID();
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    public String getUsername() {
        return username;
    }
    public void setUserName(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Client.ClientType getClientType() {
        if (clientType == 0) {
            return ClientType.FREE;
        } else if (clientType == 1) {
            return ClientType.REGULAR;
        } else if (clientType == 2) {
            return ClientType.PREMIUM;
        }else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "userName='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
