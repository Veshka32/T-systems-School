package jms;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import websocketService.SessionHandler;
import websocketService.WebSocketServer;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "java:/mobile/MyQueue"
        )
})
public class Receiver implements MessageListener {

    @Inject
    WebSocketServer socketServer;

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String messageText = textMessage.getText();
                socketServer.send(messageText);
            } catch (JMSException ex) {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);            }

        }
    }
}