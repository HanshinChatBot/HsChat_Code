# HsChatBot

# 👉Welcome to HsChatBot Service👈

한신대학교 정보관리팀 시스템 개편으로 인하여 바쁜 지금 챗봇을 통하여 기본적인 문의사항을 해결합니다.

챗봇 시스테을 위하여 공공 인공지능 오픈 api를 활용하여 대화 및 언어처리를 합니다.

### 💻 Referenced Libraries

👀 gson-2.8.6.jar

👀 servlet-api.jar

👀 websocket-api.jar

👀 mysql-connector-java—8.0.22.jar

### 📎 핵심기능 설명

-정보관리팀에 문의하기

: 챗봇을 통해 문의를 하며 빠르게 해답을 얻습니다.

### 🌱 사용자 화면

메인 화면

![https://github.com/HanshinChatBot/HsChat_Code/blob/master/gitimg/one.png](HsChatBot%204542a4ae3af24da787449b167779ab2f/one.png)

문의 화면

![HsChatBot%204542a4ae3af24da787449b167779ab2f/two.png](HsChatBot%204542a4ae3af24da787449b167779ab2f/two.png)

언어 분석 화면

![HsChatBot%204542a4ae3af24da787449b167779ab2f/three.png](HsChatBot%204542a4ae3af24da787449b167779ab2f/three.png)

### 🐳주요 코드

대화처리 Api(대화분석) 

```java
String openApiURL = "http://aiopen.etri.re.kr:8000/Dialog";
String accessKey = "34e1d871-d35a-4cb0-825f-26908159b85f"; // 발급받은 API Key
String domain = "HANSHIN_AI_PROJECT"; // 도메인 명
String access_method = "internal_data"; // 도메인 방식
String method = "open_dialog"; // method 호출 방식
```

messageHandler(WebSocket)

```java
@OnMessage
public String handleMessage(String message) {
	if(i==0) {
		i++;
		// 하나의 채팅당 하나의 uuid로 세션을 관리한다
		uuid = intro.Info()[0].substring(1,(intro.Info()[0].length()-1));
	}
	//형태소 분석 클래스
	anal.print(message); 
	//대화분석 클래스
	message = AiRes.AiAns(uuid, message);
	//client로 보낼 메세지
	message = message.replace("<br>", "");
	message = message.substring(1,message.length()-3);
	return message;
}
```

언어처리 Api(형태소 분석)

```java
String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";         
String accessKey = "34e1d871-d35a-4cb0-825f-26908159b85f";   // 발급받은 API Key
String analysisCode = "ner";        // 언어 분석 코드
String text = "";           // 분석할 텍스트 데이터
```

형태소 분석기 결과 수집 및 정렬

```java
for( Map<String, Object> sentence : sentences ) {
    List<Map<String, Object>> morphologicalAnalysisResult = (List<Map<String, Object>>) sentence.get("morp");
    for( Map<String, Object> morphemeInfo : morphologicalAnalysisResult ) {
        String lemma = (String) morphemeInfo.get("lemma");
        Morpheme morpheme = morphemesMap.get(lemma);
        if ( morpheme == null ) {
            morpheme = new Morpheme(lemma, (String) morphemeInfo.get("type"), 1);
            morphemesMap.put(lemma, morpheme);
        } else {
        morpheme.count = morpheme.count + 1;
    }
}
```
