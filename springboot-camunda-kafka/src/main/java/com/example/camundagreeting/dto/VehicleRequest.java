package com.example.camundagreeting.dto;


import java.awt.print.Book;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class VehicleRequest {
	public int getInsuredId() {
		return insuredId;
	}
	public void setInsuredId(int insuredId) {
		this.insuredId = insuredId;
	}
	boolean underWriterFlag;
	public boolean getUnderWriterFlag() {
		return underWriterFlag;
	}
	public void setUnderWriterFlag(boolean underWriterFlag) {
		this.underWriterFlag = underWriterFlag;
	}
	private int insuredId;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleVariant;

    public VehicleRequest(boolean underWriterFlag, int insuredId, String vehicleMake, String vehicleModel,
			String vehicleVariant, String dateOfFirstPurchase) {
		super();
		this.underWriterFlag = false;
		this.insuredId = insuredId;
		this.vehicleMake = vehicleMake;
		this.vehicleModel = vehicleModel;
		this.vehicleVariant = vehicleVariant;
		this.dateOfFirstPurchase = dateOfFirstPurchase;
	}
	
    private String dateOfFirstPurchase;

    // Getters and setters
    public String getVehicleMake() {
        return vehicleMake;
    }
    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }
    public String getVehicleModel() {
        return vehicleModel;
    }
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
    public String getVehicleVariant() {
        return vehicleVariant;
    }
    public void setVehicleVariant(String vehicleVariant) {
        this.vehicleVariant = vehicleVariant;
    }
    public String getDateOfFirstPurchase() {
        return dateOfFirstPurchase;
    }
    public void setDateOfFirstPurchase(String dateOfFirstPurchase) {

    	
        this.dateOfFirstPurchase = dateOfFirstPurchase;
    }
    
    @Bean
    public ConsumerFactory<String, VehicleRequest> consumerFactory()
    {

        // Creating a map of string-object type
        Map<String, Object> config = new HashMap<>();

        // Adding the Configuration
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                   "127.0.0.1:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG,
                   "group_id");
        config.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class);
        config.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            JsonDeserializer.class);

        // Returning message in JSON format
        return new DefaultKafkaConsumerFactory<>(
            config, new StringDeserializer(),
            new JsonDeserializer<>(VehicleRequest.class));
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,
    VehicleRequest>
    bookListener()
    {
        ConcurrentKafkaListenerContainerFactory<
            String, VehicleRequest> factory
            = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
                                                     
        return factory;
    }
}
    
    

