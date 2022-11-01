package com.example.FinalProjectRev2.repository.Interfaces;
import com.example.FinalProjectRev2.model.Authorization;
import com.example.FinalProjectRev2.model.Client;
import com.example.FinalProjectRev2.model.Log;
import com.example.FinalProjectRev2.model.LogCount;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository {
    boolean clientRegister(Client client);

    public Authorization clientLogin(Client client);

    public boolean createLog(Log log, String username);

    public String getUsernameFromToken(UUID token);

    public String getUsernameFromID(UUID id);

    public List<Log> searchLogs(String userName, String message, LocalDate dateFrom, LocalDate dateTo, int logType);

    //Admin
    public List<Client> getAllClients();

    public boolean getAdminFromUsername(String username);

    public boolean changeClientPassword(Client client, UUID id);
    public boolean changeClientType(Client client, UUID id);
}
