/**
 * Implements {@code JmsSenderI} interface.
 * It is a service-layer class for sending messages via Java Message Service, using JNDI destination and ConnectionFactory
 * Messages are sent to queue with certain expiration.
 * <p>
 *
 * @author Natalia Makarchuk
 */

package services.implementations;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import services.interfaces.JmsSenderI;

import javax.annotation.Resource;
import javax.jms.*;

@Service
public class JmsSender implements JmsSenderI {

    private static final long TIME_TO_LIVE_MILLISEC = 30_000; //messages older than that value won't be delivered to destination

    @Resource(lookup = "java:/mobile/MyConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/mobile/MyQueue")
    private Destination destination;

    /**
     * Send message to JMS queue
     *
     * @param jsonData message content in json formatted String
     */
    @Override
    public void sendData(String jsonData) {
        try (
                QueueConnection connection = (QueueConnection) connectionFactory.createConnection();
                QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(destination)
        ) {

            TextMessage message = session.createTextMessage(jsonData);
            producer.setTimeToLive(TIME_TO_LIVE_MILLISEC);
            producer.send(message);
            Logger.getLogger(JmsSender.class).info("send jms:" + jsonData);

        } catch (JMSException ex) {
            Logger.getLogger(JmsSender.class).error(ex.getMessage(), ex);
        }
    }
}
