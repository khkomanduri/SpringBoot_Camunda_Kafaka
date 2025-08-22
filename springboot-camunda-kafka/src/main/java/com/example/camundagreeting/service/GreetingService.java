package com.example.camundagreeting.service;
import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GreetingService {
    private final RuntimeService runtimeService;
    
    @Autowired
	KafkaSender kafkaSender;
    
    public GreetingService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }
    
    public String pushKafkaMessage(String hour) {
    	 Map<String, Object> processVariables = new HashMap<>();
         processVariables.put("InsuredObjectInfo", hour);
         processVariables.put("priority", "high");
         kafkaSender.send(processVariables);
      return "true";
    }
    
    
    
    
    
}