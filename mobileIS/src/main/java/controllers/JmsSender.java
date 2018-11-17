package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.entity.Tariff;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import services.implementations.TariffService;
import services.interfaces.TariffServiceI;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.List;

@Service
public class JmsSender {
    @Resource(lookup = "java:/mobile/MyConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/mobile/MyQueue")
    Destination destination;

    @Autowired
    TariffServiceI tariffServiceI;

    private static final Logger logger = Logger.getLogger(JmsSender.class);

    public void sendData(){
        try (
            //Authentication info can be omitted if we are using in-vm
            QueueConnection connection = (QueueConnection) connectionFactory.createConnection();
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination))
        {
            String jsonString=buildJson();
            TextMessage message = session.createTextMessage(jsonString);
            producer.send(message);
            logger.info("send jms");

            } catch (JMSException ex) {
            logger.error(ex.getMessage(),ex);
        }
    }

    private String buildJson(){
        List<Tariff> tariffList=tariffServiceI.getAll();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        JsonElement element=new JsonObject();
        element.getAsJsonObject().add("tariffs", gson.toJsonTree(tariffList));
        return gson.toJson(element);
    }
}
