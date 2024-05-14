package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Event;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/tryPut")
    public void TryPut() throws JsonProcessingException {
        Event concreteEvent = new Event();

        concreteEvent.id = 2;
        concreteEvent.country = "Mexico";
        concreteEvent.description = "Culture is active today!";
        concreteEvent.name = "Mexico Folk Music";
        concreteEvent.receivedDate = "04.06.2025";

        ObjectMapper objectMapper = new ObjectMapper();

        String eventJson = objectMapper.writeValueAsString(concreteEvent);

        jedis.set("event:" + concreteEvent.id, eventJson);
    }
}

