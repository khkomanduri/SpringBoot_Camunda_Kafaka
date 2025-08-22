package com.example.camundagreeting.controller;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.camundagreeting.dto.VehicleRequest;
import com.example.camundagreeting.service.GreetingService;
import com.example.camundagreeting.service.VehicleService;
import com.fasterxml.jackson.core.JacksonException;
@RestController
@RequestMapping("/api/greeting")
public class GreetingController {
    @Autowired
	private final GreetingService greetingService;
    private final KafkaTemplate<String, Object> template;
    private final String topicName;
    private final int messagesPerRequest;
    private CountDownLatch latch;
    public GreetingController(
            final KafkaTemplate<String, Object> template,
            @Value("${tpd.topic-name}") final String topicName,
            @Value("${tpd.messages-per-request}") final int messagesPerRequest,
            final GreetingService greetingService) {
        this.template = template;
        this.topicName = topicName;
        this.messagesPerRequest = messagesPerRequest;
        this.greetingService=greetingService;
        
        consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // change if needed
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "spring-rest-consumer-" + UUID.randomUUID()); 
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // read from beginning
    }

    @GetMapping("/india/{hour}")
      public String getGreeting(@PathVariable String hour) {
        System.out.println("Frm Controller "+hour);
    	return greetingService.pushKafkaMessage(hour);
    }
    
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/check-age")
    public Map<String, Object> checkVehicleAge(@RequestBody VehicleRequest request) {
        return vehicleService.processVehicle(request);
    }
    @Value("${tpd.topic-name}")
    private String kafkaTopic;
    
    private final Properties consumerProps;
    
    @GetMapping("/kafka/messages")
    public List<String> readMessages(@RequestParam(defaultValue = "30") int limit) throws JacksonException {
        List<String> messages = new ArrayList<>();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(Collections.singletonList(kafkaTopic));

            int readCount = 0;
            long timeout = System.currentTimeMillis() + 5000; // 5 seconds max wait

            while (readCount < limit && System.currentTimeMillis() < timeout) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : records) {
                    messages.add("offset=" + record.offset() + ", key=" + record.key() + ", value=" + record.value());
                    readCount++;
                    if (readCount >= limit) break;
                }
            }
        }
        
        List<String> cleanedList = new ArrayList<>();
        for (String item : messages) {
            cleanedList.add(item.replace("\\\"", "\""));  // remove slashes
        }
        return cleanedList;
    }
  
    
}