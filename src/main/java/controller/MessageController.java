package controller;

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
                return ("welcome!, you already exist");
            }
            else {
                return "Wrong password";
            }
        }

        if(jedis.hget("users", loginData.username) != null && jedis.hget("users", loginData.username) != loginData.password)
        {
            return ("Wrong password");
        }

       for (Map.Entry<String, String> entry : user.entrySet()) {
            jedis.hset("users", entry.getKey(), entry.getValue());
        }

        return loginData.username + " welcome, you are new user!";
    }
}

