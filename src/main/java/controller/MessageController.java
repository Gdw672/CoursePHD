package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class MessageController {
    Jedis jedis = new Jedis("localhost", 6379);
    private String message = "Hello from server!";

    @PostMapping("/message")
    public String updateMessage(@RequestBody String newMessage) {
        message += newMessage;
        return  message;
    }

    @PostMapping("/postData")
    public String GetLoginData(@RequestBody LoginData loginData) {
        var data = loginData;

        var user = new HashMap<String, String>();

        user.put(data.username, data.password);

        String savedPassword = jedis.hget("users", loginData.username);

        if(savedPassword != null)
        {
            if(savedPassword.equals(loginData.password)) {
                return ("OldAccess");
            }
            else {
                return "WrongAccess";
            }
        }

       for (Map.Entry<String, String> entry : user.entrySet()) {
            jedis.hset("users", entry.getKey(), entry.getValue());
        }

        return "NewAccess";
    }

    @PostMapping("/getAllEvents")
    @ResponseBody
    public ResponseEntity<ArrayList<Event>>  getAllEvents() throws JsonProcessingException {
        TryPut();
        var events = new ArrayList<Event>();

        Set<String> eventKeys = jedis.keys("event:*");

        ObjectMapper objectMapper = new ObjectMapper();

        for(String key : eventKeys) {
            String eventJson = jedis.get(key);
            Event event = objectMapper.readValue(eventJson, Event.class);
            events.add(event);
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/tryPut")
    public void TryPut() throws JsonProcessingException {
        Event concreteEvent = new Event();

        concreteEvent.id = 3;
        concreteEvent.country = "Mexico";
        concreteEvent.description = "We are in Mexico!";
        concreteEvent.name = "Mexico love all";
        concreteEvent.receivedDate = "06.01.2025";

        ObjectMapper objectMapper = new ObjectMapper();

        String eventJson = objectMapper.writeValueAsString(concreteEvent);

        jedis.set("event:" + concreteEvent.id, eventJson);
    }
}

