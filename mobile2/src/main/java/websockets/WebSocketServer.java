//package websockets;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@ApplicationScoped
//@ServerEndpoint(value = "/test")
//public class WebSocketServer {
//
//    @Inject
//    private SessionHandler sessionHandler;
//
//    @Inject
//    private Cash cash;
//
//    public void sendToAll(String message) {
//        sessionHandler.sendToAllConnectedSessions(message);
//    }
//
//    @OnOpen
//    public void open(Session session) {
//        sessionHandler.addSession(session);
//        if (!cash.isEmpty()) sessionHandler.sendToSession(session, cash.getTariffs().toString());
//    }
//
//    @OnClose
//    public void close(Session session) {
//        sessionHandler.removeSession(session);
//    }
//
//    @OnError
//    public void onError(Throwable error) {
//        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
//    }
//}
