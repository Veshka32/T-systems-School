package websocketService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@ServerEndpoint(value = "/test")
public class WebSocketServer {

    private static final String URI ="http://localhost:8080/mobile/hotTariffs";

    @Inject
    private SessionHandler sessionHandler;

    public void send(String message){
        sessionHandler.sendToAllConnectedSessions(message);
    }

    @OnOpen
    public void open(Session session) {
        sessionHandler.addSession(session);
        askData();
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    private void askData(){
        try
        {
            URL url=new URL(URI);
            HttpURLConnection connection=(HttpURLConnection)(url.openConnection());
            int status = connection.getResponseCode();
            System.out.println("status"+status);
        } catch (ProtocolException | MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e){
            System.out.println(e.getMessage());

        }

    }

}
