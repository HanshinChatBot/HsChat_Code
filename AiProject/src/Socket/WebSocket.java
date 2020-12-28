package Socket;

import AiApi.anal;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;


import AiApi.AiRes;
import AiApi.intro;
// WebSocket�� ȣ��Ʈ �ּ� ����
@ServerEndpoint("/websocket")
public class WebSocket {
// WebSocket���� �������� �����ϸ� ��û�Ǵ� �Լ�
@OnOpen
public void handleOpen() {
// �ֿܼ� ���� �α׸� ����Ѵ�.
System.out.println("client is now connected...");
}
// WebSocket���� �޽����� ���� ��û�Ǵ� �Լ�


int i = 0;
String uuid = null;
@OnMessage
public String handleMessage(String message) {
	if(i==0) {
		i++;
		// Open Dialog�� ���� ������ UUID
		uuid = intro.Info()[0].substring(1,(intro.Info()[0].length()-1));
	}
	System.out.println(message);
	anal.print(message);
	message = AiRes.AiAns(uuid, message);
	//client�� ���� �޼���

	message = message.replace("<br>", "");
	message = message.substring(1,message.length()-3);
	
	return message;
}
// WebSocket�� �������� ������ ����� ��û�Ǵ� �Լ�
@OnClose
public void handleClose() {
// �ֿܼ� ���� ���� �α׸� ����Ѵ�.
System.out.println("client is now disconnected...");
}
// WebSocket�� ������ ���� ��� ������ �߻��ϸ� ��û�Ǵ� �Լ�.
@OnError
public void handleError(Throwable t) {
// �ֿܼ� ������ ǥ���Ѵ�.
t.printStackTrace();
}
}