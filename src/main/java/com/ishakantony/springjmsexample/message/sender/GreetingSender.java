package com.ishakantony.springjmsexample.message.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishakantony.springjmsexample.config.JmsConfig;
import com.ishakantony.springjmsexample.model.GreetingMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GreetingSender {

    private final JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 3000)
    public void sendMessage() {
        GreetingMessage message = GreetingMessage.builder()
                .id(UUID.randomUUID())
                .message("Hi there!")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.GREETING_QUEUE, message);
    }

    @Scheduled(fixedRate = 3000)
    public void sendAndReceiveMessage() throws JMSException {
        GreetingMessage message = GreetingMessage.builder()
                .id(UUID.randomUUID())
                .message("Hi there mate!")
                .build();

        Message reply = jmsTemplate.sendAndReceive(JmsConfig.GREETING_AND_REPLY_QUEUE, session -> {
            Message greetingMessage = null;
            try {
                greetingMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                greetingMessage.setStringProperty("_type", GreetingMessage.class.getName());
                return greetingMessage;
            } catch (JsonProcessingException e) {
                throw new JMSException("Something wrong!");
            }
        });

        System.out.println(reply.getBody(String.class));
    }
}
