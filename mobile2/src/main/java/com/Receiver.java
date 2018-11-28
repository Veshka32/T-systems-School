package com;

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

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Receiver.class);

    @Inject
    private Cash cash;

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String messageText = textMessage.getText();
                List<Tariff> tariffs = new Gson().fromJson(messageText, new TypeToken<List<Tariff>>() {
                }.getType());
                cash.setTariffs(tariffs);
                logger.info("got jms message: " + messageText);
                cash.updateView();

            } catch (JMSException ex) {
                logger.warn(ex.getMessage(), ex);
            }
        }
    }
}