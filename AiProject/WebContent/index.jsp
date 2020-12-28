<%@ page contentType="text/html; charset=UTF-8" import="AiApi.intro" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
		<meta charset="EUC-KR">
		<title>한신 AI 빅데이터 활용 공모전</title>
		<link href="./design.css" rel="stylesheet" type="text/css">
	</head>
	<body>
        <div class="chat_wrap">
        	<!-- 헤더부분 -->
	        <div class="header"OnClick="location.href = './admin.jsp'" style="cursor:pointer;"> CHAT </div>
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
	       	<!-- 사용자input창 -->
	        <div class="input-div">
	        	<textarea id = "messageWindow" placeholder="Press Enter for send message."></textarea>
	        </div>  
        </div>
    </body>
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
    	<% String[] info = intro.Info();%>
    	const data = {
			"senderName"    : "bot",
			"message"        : <%= info[1] = info[1].replace("<br>","") %>
		};
		const chatLi = createMessageTag("left", data.senderName, data.message);
		addMessageTag(chatLi);
    };
    
  	//onmessage 이벤트함수
    webSocket.onmessage = function(event) {
    	onMessage(event)
    };

    //onMessage 이벤트(봇이 보내는 부분)
    function onMessage(event) {
		const message = event.data
		const data = {
			"senderName"    : "bot",
			"message"        : message
		}
		const chatLi = createMessageTag("left", data.senderName, data.message);
		addMessageTag(chatLi);
	}
	const Chat = (function(){
		const myName = "me";
		// init 함수
		function init() {
			// enter 키 이벤트
			$(document).on('keydown', 'div.input-div textarea', function(e){
				if(e.keyCode == 13 && !e.shiftKey) {
					e.preventDefault();
					const message = $(this).val();
					// 내가 보낸메시지 java전송
					webSocket.send(message);
					//내가 보낸것 html처리
					sendMessage(message);
					// 입력창 clear
					$('div.input-div textarea').val('');
				}
			});
		}
		function sendMessage(message) {
			const data = {
			"senderName"    : "me",
			"message"        : message
			};
			const chatLi = createMessageTag("right", data.senderName, data.message);
			addMessageTag(chatLi);
		}
		return {
			'init': init
		};
	})();
	$(function(){
		Chat.init();
	});

</script>







