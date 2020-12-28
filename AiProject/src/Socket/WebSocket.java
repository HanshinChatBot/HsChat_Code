package Socket;

import AiApi.anal;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;


import AiApi.AiRes;
import AiApi.intro;
// WebSocket의 호스트 주소 설정
@ServerEndpoint("/websocket")
public class WebSocket {
// WebSocket으로 브라우저가 접속하면 요청되는 함수
@OnOpen
public void handleOpen() {
// 콘솔에 접속 로그를 출력한다.
System.out.println("client is now connected...");
}
// WebSocket으로 메시지가 오면 요청되는 함수


int i = 0;
String uuid = null;
@OnMessage
public String handleMessage(String message) {
	if(i==0) {
		i++;
		// Open Dialog로 부터 생성된 UUID
		uuid = intro.Info()[0].substring(1,(intro.Info()[0].length()-1));
	}
	System.out.println(message);
	anal.print(message);
	message = AiRes.AiAns(uuid, message);
	//client로 보낼 메세지

	message = message.replace("<br>", "");
	message = message.substring(1,message.length()-3);
	
	return message;
}
// WebSocket과 브라우저가 접속이 끊기면 요청되는 함수
@OnClose
public void handleClose() {
// 콘솔에 접속 끊김 로그를 출력한다.
System.out.println("client is now disconnected...");
}
// WebSocket과 브라우저 간에 통신 에러가 발생하면 요청되는 함수.
@OnError
public void handleError(Throwable t) {
// 콘솔에 에러를 표시한다.
t.printStackTrace();
}
}