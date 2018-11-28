package websockets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "java:/mobile/MyQueue"
        )
})
public class Receiver implements MessageListener {
    //
//    @Inject
//    private WebSocketServer socketServer;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Cash.class);
    @Inject
    private Cash cash;

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String messageText = textMessage.getText();
                Gson gson = new Gson();
                List<Tariff> tariffs = gson.fromJson(messageText, new TypeToken<List<Tariff>>() {
                }.getType());
                cash.setTariffs(tariffs);
                logger.info("got jms message: " + messageText);
                //socketServer.sendToAll(messageText);
                cash.send(messageText);

            } catch (JMSException ex) {
                logger.warn(ex.getMessage(), ex);
            }
        }
    }
}