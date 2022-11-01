package com.example.FinalProjectRev2.controllers;
import com.example.FinalProjectRev2.model.Authorization;
import com.example.FinalProjectRev2.model.Client;
import com.example.FinalProjectRev2.model.Log;
import com.example.FinalProjectRev2.model.LogCount;
import com.example.FinalProjectRev2.repository.Interfaces.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
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
                || client.getPassword().length() < 8 || !client.getPassword().matches(".*[0-9].*") || !client.getPassword().matches(".*[A-Z].*") || client.getclientTypeInt()>2) {
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
    public ResponseEntity<Authorization>clientLogin(@RequestBody Client client) {
        Authorization authorization = clientRepository.clientLogin(client);
        if (authorization == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(authorization, HttpStatus.OK);
        }
    }
    @RequestMapping(path = "/api/logs/create", method = RequestMethod.POST)
    public ResponseEntity<String>createLog(@RequestHeader("Authorization") UUID token, @RequestBody Log log) {
        if (log.getLogType() == Log.LogType.UNKNOWN) {
            return new ResponseEntity<String>("Incorrect logType", HttpStatus.BAD_REQUEST);
        }
        if (log.getMessage().length() > 1024) {
            return new ResponseEntity<String>("Message should be less than 1024", HttpStatus.PAYLOAD_TOO_LARGE);
        }
        String username = clientRepository.getUsernameFromToken(token);
        if (username == null) {
            return new ResponseEntity<String>("Incorrect token", HttpStatus.UNAUTHORIZED);
        }
        clientRepository.createLog(log, username);
        return new ResponseEntity<>("Sve ok", HttpStatus.OK);
    }

    @RequestMapping(path = "/api/logs/search", method = RequestMethod.GET)
    public ResponseEntity<List<Log>>searchLogs(@RequestHeader("Authorization") UUID token, @RequestParam("dateFrom") String dateFrom, @RequestParam("dateTo") String dateTo, @RequestParam("message") String message, @RequestParam("logType") String logType) {
        try {
            LocalDate realDateFrom = LocalDate.parse(dateFrom);
            LocalDate realDateTo = LocalDate.parse(dateTo);
            if (realDateFrom.isAfter(realDateTo)) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (!logType.equals("0") && !logType.equals("1") && !logType.equals("2")) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        String username = clientRepository.getUsernameFromToken(token);
        if (username == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        var result = clientRepository.searchLogs(username, message, LocalDate.parse(dateFrom), LocalDate.parse(dateTo), Integer.parseInt(logType));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(path = "/api/clients", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> getAllClients(@RequestHeader("Authorization") UUID token) {
        String username = clientRepository.getUsernameFromToken(token);

        if (username == null) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        boolean isAdmin = clientRepository.getAdminFromUsername(username);
        if (!isAdmin) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        var result = clientRepository.getAllClients();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @RequestMapping(path = "/api/clients/{id}/reset-password", method = RequestMethod.PATCH)
    public ResponseEntity<Client>changeClientPassword(@RequestHeader("Authorization") UUID token, @RequestBody Client client, @PathVariable UUID id){

        String usernameById=clientRepository.getUsernameFromID(id);
        if (usernameById == null) {
            System.out.println("Non existing id! " +id);
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        String username = clientRepository.getUsernameFromToken(token);
        boolean isAdmin = clientRepository.getAdminFromUsername(username);
        if (!isAdmin) {
            System.out.println("You are not admin! "+token);
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        boolean clientpass=clientRepository.changeClientPassword(client, id);
        if (clientpass) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return null;
    }
    @RequestMapping(path="/api/clients/{id}/clientType", method = RequestMethod.PATCH)
    public ResponseEntity<Client>changeClientType(@RequestHeader("Authorization") UUID token, @RequestBody Client client, @PathVariable UUID id){

        if(client.getclientTypeInt()==0){

        }

        if (client.getclientTypeInt()!=0 && client.getclientTypeInt()!=1 && client.getclientTypeInt()!=2) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        String usernameById=clientRepository.getUsernameFromID(id);
        if (usernameById == null) {
            System.out.println("Non existing id! "+id);
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        String username = clientRepository.getUsernameFromToken(token);
        boolean isAdmin = clientRepository.getAdminFromUsername(username);
        if (!isAdmin) {
            System.out.println("You are not admin! "+token);
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        boolean clientType=clientRepository.changeClientType(client, id);
        if (clientType) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return null;
    }
}
