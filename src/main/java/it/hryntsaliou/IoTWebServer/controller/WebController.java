package it.hryntsaliou.IoTWebServer.controller;

import it.hryntsaliou.IoTWebServer.model.SensorData;
import it.hryntsaliou.IoTWebServer.config.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Controller
@RequestMapping("/")
public class WebController {
    private final RedisUtil util;

    public WebController(RedisUtil util) {
        this.util = util;
    }

    @RequestMapping(value = "/getSensorData", method = RequestMethod.POST)
    public @ResponseBody List<String> findValue(@RequestBody SensorData sensorData, @RequestParam("reqData") int reqData) {
        List<String>  retrieveMap;
        JedisPool jedisPool = util.getJedisPool();
        try (Jedis jedis = jedisPool.getResource()) {
            String key=getListKey(sensorData.getSensorType(),sensorData.getSensorLocation());
            retrieveMap = jedis.lrange(key, 0, reqData);
        }
        return retrieveMap;
    }

    private String getListKey(String sensorType,String location){
        return sensorType+":"+location;
    }
}
