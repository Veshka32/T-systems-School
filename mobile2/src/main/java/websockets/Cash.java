package websockets;

import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
@Named
public class Cash {
    private static final String URI = "http://localhost:8080/mobile/hotTariffs";

    private static final Logger logger = org.apache.log4j.Logger.getLogger(Cash.class);
    private List<Tariff> tariffs = new CopyOnWriteArrayList<>();
    @Inject
    @Push
    private PushContext channel;

    void send(String message) {
        channel.send("update");
        logger.info("channel send: " + "update");
    }


    @PostConstruct
    public void getData() {
        askData();
    }

    public void log(String m) {
        logger.info("log method fire: " + m);
    }

    public List<Tariff> getTariffs() {
        return tariffs;
    }

    public void setTariffs(List<Tariff> tariffs) {
        this.tariffs.clear();
        this.tariffs.addAll(tariffs);
    }

    boolean isEmpty() {
        return tariffs.isEmpty();
    }

    private void askData() {

        try {
            URL url = new URL(URI);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            int status = connection.getResponseCode();
            logger.info("response status: " + status);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }

    }
}
