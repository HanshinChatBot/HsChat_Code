<%@ page import="java.util.*"%>
<%@page import="db.selectDb"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" import="AiApi.intro" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>한신 AI 빅데이터 활용 공모전</title>
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
	<meta charset="EUC-KR">
	<link href="./design.css" rel="stylesheet" type="text/css">
</head>
<body>
        <div class="chat_wrap">
        	<!-- 헤더부분 -->
	        <div class="header"> 관리자 페이지 </div>
	        <div class="chat">
		        <ul>
		        	<!-- 동적 생성 -->
		        </ul>
	        </div>
	        <!-- format -->
	        <div class="chat format">
		        <ul>
			        <li>
			        <div class="sender">
			        	<span></span>
			        </div>
			        <div class="message">
			        	<span></span>
			        </div>
			        </li>
		        </ul>
	        </div>
        </div>
    </body>
</html>
<script type="text/javascript">
    var textarea = document.getElementById("messageWindow");
    var webSocket = new WebSocket("ws://localhost:8080/AiProject/websocket");	
    
  	//메세지 태그 만들기
	function createMessageTag(LR_className, senderName, message) {
	    // 형식 가져오기
	    let chatLi = $('div.chat.format ul li').clone();
	    // 값 채우기	
	    chatLi.addClass(LR_className);
	    chatLi.find('.sender span').text(senderName);
	    chatLi.find('.message span').text(message);
	    return chatLi;
	}
 	 // 메세지 태그 추가하기, 스크롤바 고정하기
	function addMessageTag(chatLi){
		$('div.chat:not(.format) ul').append(chatLi);
		// 스크롤바 아래 고정
		$('div.chat').scrollTop($('div.chat').prop('scrollHeight'));
	}

	// ************************** 소켓 설정부분 ******************************
    //error 이벤트함수
    webSocket.onerror = function(event) {
    	onError(event)
    };
    //error가 발생했을떄의 이벤트
    function onError(event) {
    	alert(event.data);
    }
    //open 이벤트함수
	function onOpen(event) {
	}
	
    //처음 시작했을 떄의 이벤트
    webSocket.onopen = function(event) {
    	onOpen(event)
    	let data = {
			"senderName"    : "bot",
			"message"        : "관리자 페이지에 오신것을 환영합니다. 위 페이지에서는 사용자가 입력한 문장을 데이터화 및 시각화하여 보여줍니다."
		};
		let chatLi = createMessageTag("left", data.senderName, data.message);
		addMessageTag(chatLi);
		<%
			ArrayList<String> result = selectDb.select();
			for(String a: result){ %>
			data.message = "<%= a%>"
	    	chatLi = createMessageTag("left", data.senderName, data.message);
			addMessageTag(chatLi);
			<%}
		%>
    };
    
  	//onmessage 이벤트함수
    webSocket.onmessage = function(event) {
    	onMessage(event)
    };

    //onMessage 이벤트(봇이 보내는 부분)
    function onMessage(event) {
	}


</script>