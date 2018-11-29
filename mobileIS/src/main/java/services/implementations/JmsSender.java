package services.implementations;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import services.interfaces.JmsSenderI;

import javax.annotation.Resource;
import javax.jms.*;

@Service
public class JmsSender implements JmsSenderI {
    private static final Logger logger = Logger.getLogger(JmsSender.class);
    private static final long TIME_TO_LIVE_MILLISEC = 30_000;

    @Resource(lookup = "java:/mobile/MyConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/mobile/MyQueue")
    private Destination destination;

    @Override
    public void sendData(String jsonData) {
        try (
                //Authentication info can be omitted if we are using in-vm
                QueueConnection connection = (QueueConnection) connectionFactory.createConnection();
                QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(destination)
        ) {

            TextMessage message = session.createTextMessage(jsonData);
            producer.setTimeToLive(TIME_TO_LIVE_MILLISEC);
            producer.send(message);
            logger.info("send jms");

        } catch (JMSException ex) {
            logger.error(ex.getMessage(),ex);
        }
    }


}
