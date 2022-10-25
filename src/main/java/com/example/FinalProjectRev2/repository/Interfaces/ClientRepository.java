package com.example.FinalProjectRev2.repository.Interfaces;

import com.example.FinalProjectRev2.model.Authorization;
import com.example.FinalProjectRev2.model.Client;
import com.example.FinalProjectRev2.model.Log;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository {
    boolean clientRegister(Client client);

    public Authorization clientLogin(Client client);

    public boolean createLog(Log log, String username);

    public String getUsernameFromToken(UUID token);

    List<SearchLogs>searchLogs(SearchLogs srcLog, String username);

    //Admin
    List<Client> getAllClients();
}
