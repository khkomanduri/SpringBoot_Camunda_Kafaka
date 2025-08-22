package com.example.camundagreeting.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {
	
	@Autowired
	private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
	
	@Value("${tpd.topic-name}")
	    private String kafkaTopic;
	
	public void send(Map<String, Object> mymap) {
		System.out.println("My Constant==> "+kafkaTopic);
	    
	    kafkaTemplate.send(kafkaTopic, mymap);
	}
}
