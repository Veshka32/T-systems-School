package websocketService;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class SessionHandler {
    private final Set<Session> sessions = new HashSet<>();

    void addSession(Session session) {
        sessions.add(session);
    }

    void removeSession(Session session) {
        sessions.remove(session);
    }

    void sendToAllConnectedSessions(String message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    void sendToSession(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            sessions.remove(session);
        }
    }
}
