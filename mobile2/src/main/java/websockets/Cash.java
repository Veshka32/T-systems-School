package websockets;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class Cash {
    private static final String URI = "http://localhost:8080/mobile/hotTariffs";

    private List<String> jsonData = new CopyOnWriteArrayList<>();


    @PostConstruct
    public void getData() {
        askData();
    }

    public String getJsonData() {
        return jsonData.get(0);
    }

    public void update(String data) {
        jsonData.clear();
        jsonData.add(data);
    }

    public boolean isEmpty() {
        return jsonData.isEmpty();
    }

    private void askData() {

        try {
            URL url = new URL(URI);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            int status = connection.getResponseCode();
            Logger.getLogger(WebSocketServer.class.getName()).log(Level.INFO, null, "response status " + status);
        } catch (IOException e) {
            Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, e);
        }

    }
}
