package com.rawsanj.adminlte.web.controller;

import com.rawsanj.adminlte.dto.ChartDataDTO;
import com.rawsanj.adminlte.model.ThermoSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


import java.util.Random;


@Controller
public class DashboardController {

    private static Log logger = LogFactory.getLog(DashboardController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping("/")
    public String index() {
        return "dashboard/index";
    }

    @RequestMapping("/index2")
    public String index2() {
        return "dashboard/index2";
    }


    // Added to test 500 page
    @RequestMapping(path = "/tigger-error", produces = MediaType.APPLICATION_JSON_VALUE)
    public void error500() throws Exception {
        throw new Exception();
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getData() {
        return ResponseEntity.ok(new ChartDataDTO(System.currentTimeMillis(), new Random().nextInt(100)));
    }

    @PostMapping("/sensors/thermo")
    public ResponseEntity<String> temperature(@RequestBody ThermoSensor sensor) {

        logger.info("thermo sensor ="+ sensor.toString());

        this.simpMessagingTemplate.convertAndSend("/topic/airquality/temperature", ""+sensor.getValue() );
        this.simpMessagingTemplate.convertAndSend("/topic/airquality/windspeed", ""+sensor.getValue() );
        return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.TEXT_PLAIN).body("success");
    }

    @PostMapping("/sensors/count")
    public ResponseEntity<String> count(@RequestBody ThermoSensor sensor) {

        logger.info("count sensor = "+ sensor.toString());

        this.simpMessagingTemplate.convertAndSend("/topic/airquality/count", ""+sensor.getValue() );
        return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.TEXT_PLAIN).body("success");

    }

    @PostMapping("/sensors/humidity")
    public ResponseEntity<String> humidity(@RequestBody ThermoSensor sensor) {

        logger.info("humidity sensor = "+ sensor.toString());

        this.simpMessagingTemplate.convertAndSend("/topic/airquality/humidity", ""+sensor.getValue() );
        return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.TEXT_PLAIN).body("success");

    }

}
