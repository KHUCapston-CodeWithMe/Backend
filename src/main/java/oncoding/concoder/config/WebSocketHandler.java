package oncoding.concoder.config;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 여러 클라이언트로부터받을 WebSocket 메시지를 처리하기위한 메시지 핸들러
 * 클라이언트로부터 메시지를받을 때 자신을 제외한 다른 모든 클라이언트에게 메시지를 보냄.
 */

@Component
public class WebSocketHandler extends TextWebSocketHandler {

  List<WebSocketSession>sessions = new CopyOnWriteArrayList<>();

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message)
      throws InterruptedException, IOException {
    for (WebSocketSession webSocketSession : sessions) {
      if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
        webSocketSession.sendMessage(message);
      }
    }
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessions.add(session);
  }
}