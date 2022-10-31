package com.example.FinalProjectRev2.repository;
import com.example.FinalProjectRev2.model.Authorization;
import com.example.FinalProjectRev2.model.Client;
import com.example.FinalProjectRev2.model.Log;
import com.example.FinalProjectRev2.model.LogCount;
import com.example.FinalProjectRev2.repository.Interfaces.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public class SQLClientRepository implements ClientRepository {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Override
    public boolean clientRegister(Client client) {
        String query = "SELECT username, password, email, isAdmin FROM clients";
        List<Client> clients = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Client.class));
        boolean clientExist = false;
        for (var el : clients) {
            if ((el.getUsername().equals(client.getUsername())) || el.getEmail().equals(client.getEmail())) {
                clientExist = true;
                System.out.println("Klijent vec postoji u bazi.");
                break;
            }
        }
        if (!clientExist) { // ako klijent ne postoji u bazi, ubacujemo ga
            client.generateRandomId();
            clients.add(client);
            jdbcTemplate.update("INSERT INTO clients (id, username, password, email, isAdmin, clientType) VALUES (?, ?, ?, ?, ?, ?)",client.getId(), client.getUsername(), client.getPassword(), client.getEmail(), client.isAdmin(), client.getclientTypeInt());
            System.out.println("Dodavanje u bazu uspesno!");
            return true;
        }
        return false;
    }
    @Override
    public Authorization clientLogin(Client client) {
        String query = "SELECT username, password, email FROM clients";
        List<Client> clients = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Client.class));
        for (var el : clients) {
            if (el.getUsername().equals(client.getUsername()) && el.getPassword().equals(client.getPassword())) { // proveravamo da li u bazi imamo registrovanog korisnika sa ovim username i password, ako ima moze, da se uloguje
                System.out.println("Login je moguc!");
                UUID token = UUID.randomUUID();
                jdbcTemplate.update("INSERT INTO tokens (token, username) VALUES (?, ?)", token, client.getUsername());
                var auth = new Authorization();
                auth.token = token;
                return auth;
            }
        }
        System.out.println("User ne postoji");
        return null;
    }
    @Override
    public boolean createLog(Log log, String username) {
        log.generateRandomId();
        log.setCurrentDate();
        jdbcTemplate.update("INSERT INTO log (id, username, message, logType, createdDate) VALUES (?, ?, ?, ?, ?)", log.getId(), username, log.getMessage(), log.getLogTypeInt(), log.getCreatedDate());
        return true;
    }
    @Override
    public String getUsernameFromToken(UUID token) {
        String query = "SELECT username FROM tokens WHERE token=?";
        try {
            String username = jdbcTemplate.queryForObject(query, new Object[]{token}, String.class);

            return username;
        } catch (DataAccessException e) {
            return null;
        }
    }
    @Override
    public List<Log> searchLogs(String userName, String message, LocalDate dateFrom, LocalDate dateTo, int logType) {
        //Query koji vraca listu svih trazenih logova

        List<Log> logovi = jdbcTemplate.query("SELECT id, message,logType,createdDate FROM log WHERE logType=" + logType + " and message like '%" + message + "%' and createdDate >= '" + dateFrom + "' and createdDate <= '" + dateTo + "' and username = '" + userName + "'", BeanPropertyRowMapper.newInstance(Log.class));
        return logovi;
    }

    //Admin
    @Override
    public List<Client> getAllClients() {
        String query ="SELECT COUNT(*) as totalLogs, username FROM log GROUP BY username";
        String query2 = "SELECT id, username, email FROM clients";
        List<LogCount>logCounts=jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(LogCount.class));
        List<Client> clients = jdbcTemplate.query(query2, BeanPropertyRowMapper.newInstance(Client.class));
        for(var klijent: clients){
            for(var log: logCounts){
                if(log.getUsername().equals(klijent.getUsername())){
                    klijent.setLogCount(log.getTotalLogs());
                    break;
                }
            }
        }
        return clients;
    }
    public boolean getAdminFromUsername(String username) {
        String query = "SELECT isAdmin FROM clients WHERE username=?";
        try {
            int isTrue = jdbcTemplate.queryForObject(query, new Object[]{username}, Integer.class);
            if(isTrue==1) {
                return true;
            }else{
                return false;
            }
        } catch (DataAccessException e) {
            return false;
        }
    }
    @Override
    public boolean changeClientPassword(Client client, UUID id) {
        var username=getUsernameFromToken(id);
        jdbcTemplate.update("UPDATE clients SET password=? WHERE id=?",client.getPassword(), id);
        return true;
    }
}