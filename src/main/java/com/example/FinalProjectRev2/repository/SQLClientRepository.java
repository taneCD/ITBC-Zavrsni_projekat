package com.example.FinalProjectRev2.repository;
import com.example.FinalProjectRev2.model.Authorization;
import com.example.FinalProjectRev2.model.Client;
import com.example.FinalProjectRev2.model.Log;
import com.example.FinalProjectRev2.repository.Interfaces.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class SQLClientRepository implements ClientRepository {
    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Override
    public boolean clientRegister(Client client) {
        String query = "SELECT username, password, email FROM clients";
        List<Client> clients = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Client.class));
        boolean clientExist = false;
        for (var el : clients) {
            if ((el.getUsername().equals(client.getUsername())) || el.getEmail().equals(client.getEmail())) {
                clientExist = true;
                System.out.println("Klijent vec postoji u bazi.");
                break;
            }
        }
        if (!clientExist) {
            clients.add(client);
            jdbcTemplate.update("INSERT INTO clients (username, password, email) VALUES (?, ?, ?)", client.getUsername(), client.getPassword(), client.getEmail());
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
            if (el.getUsername().equals(client.getUsername()) && el.getPassword().equals(client.getPassword())) { // proveravamo da li u bazi imamo korisnika sa ovim username i password, ako ima moze da se uloguje
                System.out.println("Login je moguc!");
                UUID token = UUID.randomUUID();
                jdbcTemplate.update("INSERT INTO tokens (token, username) VALUES (?, ?)",token, client.getUsername());
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
//        String query = "SELECT id, username, message, logType, createdDate FROM log";
//        List<Log> logs = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Log.class));
//        boolean logNesto = false;
//        for (var el : logs) {
//            if (el.getId().equals(log.getId())) {
//                logNesto = true;
//                break;
//            }
//        }
//        if (!logNesto) {
            log.generateRandomId();
            log.setCurrentDate();
            jdbcTemplate.update("INSERT INTO log (id, username, message, logType, createdDate) VALUES (?, ?, ?, ?, ?)",log.getId(), username, log.getMessage(), log.getLogTypeInt(), log.getCreatedDate());
            return true;

    }
    @Override
    public String getUsernameFromToken(UUID token) {
        String query = "SELECT username FROM tokens WHERE token=?";
        System.out.println("Za sad sve ok");
        try {
            String username = jdbcTemplate.queryForObject(query, new Object[]{token}, String.class);
            System.out.println("Prosao sam query");
//        List<String> tokens = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(String.class), token);
//            if (username == null || username.length() == 0) {
//                return null;
//            }
            return username;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override//????????????????????????????????????????????????????????????
    public List<SearchLogs> searchLogs(SearchLogs srcLog, String username) {
        String query = "SELECT message, logType FROM log WHERE event_date between dateFrom and dateTo";
        List<SearchLogs> logs = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(SearchLogs.class));
//        jdbcTemplate.update("INSERT INTO log (message, logType) VALUES (?, ?)",srcLog.getMessage(), srcLog.getLogType());

        return logs;
    }






    //Admin
    @Override
    public List<Client> getAllClients() {
        String query = "SELECT username, email, logCount FROM clients";
        List<Client> clients = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Client.class));
        return clients;
    }
}