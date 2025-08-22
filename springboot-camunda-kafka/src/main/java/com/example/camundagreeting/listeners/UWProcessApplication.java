package com.example.camundagreeting.listeners;
import java.util.Map;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.camundagreeting.service.GreetingService;
import com.example.camundagreeting.service.KafkaSender;
import com.fasterxml.jackson.databind.ObjectMapper;


@ProcessApplication
@Service
public class UWProcessApplication implements ExecutionListener {
	String processDefinitionKey = "vehicle_age_check";
	@Autowired
	KafkaSender kafkaSender;
    @Autowired
	private  GreetingService greetingService;

    public UWProcessApplication(GreetingService greetingService) {
        this.greetingService = greetingService;
    }
	
	@Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
    	System.out.println("I am in Execution Listner");
        //CamundaProperties camundaProperties = delegateExecution.getBpmnModelElementInstance().getExtensionElements().getElementsQuery().filterByType(CamundaProperties.class).singleResult();
        Map<String, Object> response=delegateExecution.getVariables();
    	System.out.println("InputVariables==> "+response);
    	ObjectMapper objectMapper = new ObjectMapper();
    	    String jacksonData = objectMapper.writeValueAsString(response);
    	    greetingService.pushKafkaMessage(jacksonData);

    }
}