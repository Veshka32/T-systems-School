package services.implementations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.entity.Tariff;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.interfaces.JmsSenderI;
import services.interfaces.TariffServiceI;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.*;
import java.util.List;

@Service
public class JmsSender implements JmsSenderI {
    private static final int COUNT = 3;
    private static final Logger logger = Logger.getLogger(JmsSender.class);
    private static final long TIME_TO_LIVE_MILLISEC = 30_000;

    @Resource(lookup = "java:/mobile/MyConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/mobile/MyQueue")
    private Destination destination;

    @Autowired
    TariffServiceI tariffServiceI;

    @PostConstruct
    public void postConstruct() {
        sendData();
    }

    @Override
    public void sendData(){
        try (
                //Authentication info can be omitted if we are using in-vm
                QueueConnection connection = (QueueConnection) connectionFactory.createConnection();
                QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(destination)
        ) {
            String jsonString=buildJson();
            TextMessage message = session.createTextMessage(jsonString);
            producer.setTimeToLive(TIME_TO_LIVE_MILLISEC);
            producer.send(message);
            logger.info("send jms");

        } catch (JMSException ex) {
            logger.error(ex.getMessage(),ex);
        }
    }

    private String buildJson(){
        List<Tariff> tariffList = tariffServiceI.getLast(COUNT);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(tariffList);

//        JsonElement element=new JsonObject();
//        element.getAsJsonObject().add("tariffs", gson.toJsonTree(tariffList));
//        return gson.toJson(element);
    }
}
