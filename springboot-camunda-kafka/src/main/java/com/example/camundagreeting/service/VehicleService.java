package com.example.camundagreeting.service;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.camundagreeting.dto.VehicleRequest;

@Service
public class VehicleService {
	 private final RuntimeService runtimeService;
	 private String processInstanceId=null;
	
	 public VehicleService(RuntimeService runtimeService) {
	        this.runtimeService = runtimeService;
	    }
    public Map<String, Object> processVehicle(VehicleRequest request) {
    	Map<String, Object> response = new HashMap<>();
    try {
        response.put("insuredId", request.getInsuredId());
        response.put("vehicleMake", request.getVehicleMake());
        response.put("vehicleModel", request.getVehicleModel());
        response.put("vehicleVariant", request.getVehicleVariant());
        response.put("dateOfFirstPurchase", request.getDateOfFirstPurchase());
        
        DateTimeFormatter f = new DateTimeFormatterBuilder().parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toFormatter();
        	LocalDate currentDate = LocalDate.now();
            LocalDate datetime = LocalDate.parse(request.getDateOfFirstPurchase(), f);
            System.out.println(datetime); // 2019-12-22
            Period period = Period.between(datetime,currentDate);
            System.out.println("Vehicle Age(Years): "+period.getYears()); 
            
            if (period.getYears() > 10) {
                response.put("message", "Should go to under writer due to age greater than 10 years");
                request.setUnderWriterFlag(true);
                response.put("uwFlag", request.getUnderWriterFlag());
            } else {
                response.put("message", "Vehicle age within acceptable range");
                response.put("uwFlag", request.getUnderWriterFlag());
                response.put("uwPassed", "No");
            }
            String processDefinitionKey = "vehicle_age_check";
            ProcessInstance processInstance = runtimeService.createProcessInstanceByKey(processDefinitionKey)
                .businessKey(String.valueOf(request.getInsuredId()))
                .setVariables(response)
                .executeWithVariablesInReturn();
      
       processInstanceId=processInstance.getId();
       System.out.println("PID processInstanceId "+processInstance.getProcessDefinitionKey());
       
    } catch (DateTimeParseException e) {
    	  System.out.println("Exception Occured ==>"+e.getMessage());
    } 
    return response;
        
    }
    
    
    public Map<String, Object> getUWData(int hour) {
    	Map<String, Object> response = new HashMap<>();
   	VariableInstance v = 
   		  runtimeService.createVariableInstanceQuery()
   		    .processInstanceIdIn(processInstanceId)
   		    .variableName("InsuredDetails")
   		  .singleResult();
   	
   	String processDefinitionKey = "vehicle_age_check";
   	return response;
   }
    
    private String messageRecived=null;
    
@KafkaListener(topics = "${tpd.topic-name}")
public void publish(String message)
{
 System.out.println(
     "You have a new message: "
     + message);
 messageRecived=message;
}
    
public String messageRecived(){
	return messageRecived;
}

}
