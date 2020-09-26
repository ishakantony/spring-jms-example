package com.ishakantony.springjmsexample.message.listener;

import com.ishakantony.springjmsexample.config.JmsConfig;
import com.ishakantony.springjmsexample.model.GreetingMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GreetingListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.GREETING_QUEUE)
    public void listen(@Payload GreetingMessage greetingMessage,
                       @Headers MessageHeaders headers,
                       Message message) {
        System.out.println("Message received!");

        System.out.println(greetingMessage);
    }

    @JmsListener(destination = JmsConfig.GREETING_AND_REPLY_QUEUE)
    public void listenAndReply(@Payload GreetingMessage greetingMessage,
                       @Headers MessageHeaders headers,
                       Message message) throws JMSException {
        GreetingMessage greetingMessageReply = GreetingMessage.builder()
                .id(UUID.randomUUID())
                .message("Hiya!")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), greetingMessageReply);
    }

}
