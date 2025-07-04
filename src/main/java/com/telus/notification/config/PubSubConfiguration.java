package com.telus.notification.config;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.telus.notification.service.PubSubListenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Arrays;
import java.util.List;

@Configuration
public class PubSubConfiguration {

    @Value("${pubsub.subscription.user-registration}")
    private String userRegistrationSubscription;

    @Value("${pubsub.subscription.user-approved}")
    private String userApprovedSubscription;

    @Value("${pubsub.subscription.user-reject}")
    private String userRejectSubscription;

    @Autowired
    private PubSubListenerService pubSubListenerService;

    @Bean
    public MessageChannel pubsubInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter userRegistrationAdapter(
            MessageChannel pubsubInputChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, userRegistrationSubscription);
        adapter.setOutputChannel(pubsubInputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(String.class);
        return adapter;
    }

    @Bean
    public PubSubInboundChannelAdapter userApprovedAdapter(
            MessageChannel pubsubInputChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, userApprovedSubscription);
        adapter.setOutputChannel(pubsubInputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(String.class);
        return adapter;
    }

    @Bean
    public PubSubInboundChannelAdapter userRejectAdapter(
            MessageChannel pubsubInputChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, userRejectSubscription);
        adapter.setOutputChannel(pubsubInputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(String.class);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public MessageHandler messageReceiver() {
        return message -> {
            String payload = new String((byte[]) message.getPayload());
            BasicAcknowledgeablePubsubMessage originalMessage =
                    message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE, 
                            BasicAcknowledgeablePubsubMessage.class);
            
            String subscriptionName = originalMessage.getProjectSubscriptionName().getSubscription();

            if (subscriptionName.equals(userRegistrationSubscription)) {
                pubSubListenerService.receiveUserRegistrationMessage(payload);
            } else if (subscriptionName.equals(userApprovedSubscription)) {
                pubSubListenerService.receiveAccountApprovedMessage(payload);
            } else if (subscriptionName.equals(userRejectSubscription)) {
                pubSubListenerService.receiveAccountRejectMessage(payload);
            } else {
                throw new IllegalArgumentException("Unknown subscription: " + subscriptionName);
            }

            originalMessage.ack();
        };
    }
}
