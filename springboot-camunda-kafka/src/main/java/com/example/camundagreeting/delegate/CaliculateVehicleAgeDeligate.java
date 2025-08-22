package com.example.camundagreeting.delegate;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CaliculateVehicleAgeDeligate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
    	
    	System.out.println("In Caliculate Vehicle Age Deligate ");
    	
    	int InsuredId=Integer.valueOf(execution.getVariable("insuredId").toString());
    	Map<String, Object> response=execution.getVariables();
        System.out.println("Insured Data "+response);
        execution.setVariable("VehicleAgeParams", response);
    } 
}