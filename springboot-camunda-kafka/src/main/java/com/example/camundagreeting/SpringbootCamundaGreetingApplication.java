package com.example.camundagreeting;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SpringbootCamundaGreetingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootCamundaGreetingApplication.class, args);
	}
	
	
    public TaskListener getTaskListener() {
    	return new TaskListener() {
    		public void notify(DelegateTask delegateTask) {

    			System.out.println("test start...");
    }
    };
}
    
    Map<String, Object> response = new HashMap<>();
    
    
    
    @Bean
    public ApplicationRunner runner(KafkaTemplate<String, Map<String, Object>> template, Foo foo) {
        return args -> {
            template.send("so55280173", response);
            if (foo.kafkaTemplate == template) {
                System.out.println("they are the same");
            }
        };
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic("so55280173", 1, (short) 1);
    }



@Component
class Foo {
	KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
 //   final KafkaTemplate<String, String> template;

    @Autowired
    Foo(KafkaTemplate<String, Map<String, Object>> template) {
        this.kafkaTemplate = template;
    }
}
@Autowired
private KafkaProperties kafkaProperties;

@Bean
public ConsumerFactory<String, Object> consumerFactory() {
    final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
    jsonDeserializer.addTrustedPackages("*");
    return new DefaultKafkaConsumerFactory<>(
    		kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
    );
}


}