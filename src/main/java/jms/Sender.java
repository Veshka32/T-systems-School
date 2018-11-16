package jms;

import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import java.util.HashMap;
import java.util.Map;

public class Sender {

    private JmsTemplate jmsTemplate;
    private Queue queue;
    private Destination destination;

    public void setConnectionFactory(ConnectionFactory cf) {
        jmsTemplate = new JmsTemplate(cf);
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void simpleSend(String message) {
        jmsTemplate.send(this.queue, s -> s.createTextMessage(message));
        jmsTemplate.send(destination, s -> s.createTextMessage(message));
    }

    public void sendMessage(Object employee) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", employee.getClass());
        map.put("string", employee.toString());
        jmsTemplate.convertAndSend(map);
    }
}
