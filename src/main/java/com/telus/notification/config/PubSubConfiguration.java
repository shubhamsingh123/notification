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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Arrays;
import java.util.List;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
public class PubSubConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(PubSubConfiguration.class);

    @Value("${pubsub.subscription.user-registration}")
    private String userRegistrationSubscription;

    @Value("${pubsub.subscription.user-approved}")
    private String userApprovedSubscription;

    @Value("${pubsub.subscription.user-reject}")
    private String userRejectSubscription;

    @Autowired
    private PubSubListenerService pubSubListenerService;

    // ✅ Track processed message IDs to prevent duplicate processing
    private final Set<String> processedMessageIds = ConcurrentHashMap.newKeySet();


    @Bean
    public MessageChannel pubsubInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter userRegistrationAdapter(
         @Value("${pubsub.subscription.user-registration}") String subscription,
            MessageChannel pubsubInputChannel,
            PubSubTemplate pubSubTemplate) {
        return createChannelAdapter(subscription, pubsubInputChannel, pubSubTemplate);
    }

    @Bean
    public PubSubInboundChannelAdapter userApprovedAdapter(
             @Value("${pubsub.subscription.user-approved}") String subscription,
            MessageChannel pubsubInputChannel,
            PubSubTemplate pubSubTemplate) {
        return createChannelAdapter(subscription, pubsubInputChannel, pubSubTemplate);
    }

    @Bean
    public PubSubInboundChannelAdapter userRejectAdapter(
             @Value("${pubsub.subscription.user-reject}") String subscription,
            MessageChannel pubsubInputChannel,
            PubSubTemplate pubSubTemplate) {
        return createChannelAdapter(subscription, pubsubInputChannel, pubSubTemplate);
    }

    private PubSubInboundChannelAdapter createChannelAdapter(
            String subscription,
            MessageChannel pubsubInputChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, subscription);
        adapter.setOutputChannel(pubsubInputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(String.class);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public MessageHandler messageReceiver() {
        return message -> {
            // String payload = new String((byte[]) message.getPayload());
            BasicAcknowledgeablePubsubMessage originalMessage =
                    message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE, 
                            BasicAcknowledgeablePubsubMessage.class);
            
            if (originalMessage == null) {
                logger.warn("⚠️ Received message without original Pub/Sub header. Skipping.");
                return;
            }
             String payload = (String) message.getPayload(); // ✅ FIXED
            String messageId = originalMessage.getPubsubMessage().getMessageId();
            

            // 🔁 Deduplication check
            if (!processedMessageIds.add(messageId)) {
                logger.warn("⚠️ Duplicate message detected with ID: {} — Skipping.", messageId);
                originalMessage.ack(); // Already handled earlier
                return;
            }

            logger.info("📨 Processing messageId={} payload={}", messageId, payload);
            System.out.println(">>>> inside messageReceiver handler");


            try{
                 // Get subscription name from message attributes
                String subscription = originalMessage.getPubsubMessage().getAttributesOrDefault("subscription", "");


            if (userRegistrationSubscription.endsWith(subscription)) {
                pubSubListenerService.receiveUserRegistrationMessage(payload);
            } else if (subscription.equals(userApprovedSubscription)) {
                pubSubListenerService.receiveAccountApprovedMessage(payload);
            } else if (subscription.equals(userRejectSubscription)) {
                pubSubListenerService.receiveAccountRejectMessage(payload);
            } else {
                    logger.warn("⚠️ Unknown subscription: {}. Skipping.", subscription);
                    originalMessage.nack();
                    return;
                }

                 originalMessage.ack(); // ✅ Only ACK if successful
                logger.info("✅ ACKed messageId={}", messageId);


            }catch (Exception e) {
                logger.error("❌ Error processing messageId=" + messageId, e);
                originalMessage.nack(); // ❌ Retry via Pub/Sub
            }
            
            // String subscriptionName = originalMessage.getProjectSubscriptionName().getSubscription();

           
            // originalMessage.ack();
        };
    }
}
