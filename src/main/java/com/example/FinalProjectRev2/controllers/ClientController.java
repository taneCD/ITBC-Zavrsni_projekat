package com.example.FinalProjectRev2.controllers;
import com.example.FinalProjectRev2.model.Authorization;
import com.example.FinalProjectRev2.model.Client;
import com.example.FinalProjectRev2.model.Log;
import com.example.FinalProjectRev2.repository.Interfaces.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ClientController {

    private ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @RequestMapping(path = "/api/clients/register", method = RequestMethod.POST)
    public ResponseEntity<Boolean> clientRegister(@RequestBody Client client) {
        if (client.getUsername().length() < 3 || !client.getEmail().contains("@") || !client.getEmail().contains(".")
                || client.getPassword().length() < 8 || !client.getPassword().matches(".*[0-9].*") || !client.getPassword().matches(".*[A-Z].*")) {
            System.out.println("Username must be longer then 3 characters and e-mail and pass must be valid! Pass at least one num and one uppercase letter");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        } else {
            boolean success = clientRepository.clientRegister(client);
            if (success) {
                return new ResponseEntity<>(true, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(false, HttpStatus.CONFLICT);
            }
        }
    }

    @RequestMapping(path = "/api/clients/login", method = RequestMethod.POST)
    public ResponseEntity<Authorization> clientLogin(@RequestBody Client client) {
        Authorization authorization = clientRepository.clientLogin(client);
        if (authorization == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(authorization, HttpStatus.OK);
        }
    }
    @RequestMapping(path = "/api/logs/create", method = RequestMethod.POST)
    public ResponseEntity<String> createLog(@RequestHeader("Authorization") UUID token, @RequestBody Log log) {
        if (log.getLogType() == Log.LogType.UNKNOWN) {
//            if (log.getLogType() != Log.LogType.ERROR && log.getLogType() != Log.LogType.WARNING && log.getLogType() != Log.LogType.INFO) {
                return new ResponseEntity<String>("Incorrect logType", HttpStatus.BAD_REQUEST);
            }
            if (log.getMessage().length() > 1024) {
                return new ResponseEntity<String>("Message should be less than 1024", HttpStatus.PAYLOAD_TOO_LARGE);
            }
            String username = clientRepository.getUsernameFromToken(token);
//        System.out.println(username+" Testiranje");
            if (username == null) {
                return new ResponseEntity<String>("Incorrect token", HttpStatus.UNAUTHORIZED);
            }
            clientRepository.createLog(log, username);
//        return new ResponseEntity<String>(token, HttpStatus.CREATED);
            return new ResponseEntity<>("Sve ok", HttpStatus.OK);
        }

    @RequestMapping(path = "/api/logs/search", method = RequestMethod.GET)
    public ResponseEntity<String> searchLogs(@RequestHeader("Authorization") UUID token, @RequestParam() ){
//        if(srcLog.getDateFrom()==)
//        clientRepository.searchLogs(srcLog,  );
        return new ResponseEntity<String>("Sve ok", HttpStatus.OK);

        if () {
            return new ResponseEntity<String>("Invalid dates or invalid logType", HttpStatus.BAD_REQUEST);
            if ()
                return new ResponseEntity<String>("Incorrect token", HttpStatus.UNAUTHORIZED);

    }
    }
