package AiApi;

import db.connectDb;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;



public class anal {
    static public class Morpheme {
        final String text;
        final String type;
        Integer count;
        public Morpheme (String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
    }
    static public class NameEntity {
        final String text;
        final String type;
        Integer count;
        public NameEntity (String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
    }
    String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";         
    String accessKey = "34e1d871-d35a-4cb0-825f-26908159b85f";   // 발급받은 API Key
    String analysisCode = "ner";        // 언어 분석 코드
    String text = "";           // 분석할 텍스트 데이터
    Gson gson = new Gson();
    
    public static String print (String text) {
    	 String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken";         
         String accessKey = "34e1d871-d35a-4cb0-825f-26908159b85f";   // 발급받은 API Key
         String analysisCode = "ner";        // 언어 분석 코드
         Gson gson = new Gson(); 
         Map<String, Object> request = new HashMap<>();
         Map<String, String> argument = new HashMap<>();
  
         argument.put("analysis_code", analysisCode);
         argument.put("text", text);
  
         request.put("access_key", accessKey);
         request.put("argument", argument);
  
         URL url;
         Integer responseCode = null;
         String responBodyJson = null;
         Map<String, Object> responeBody = null;
         try {
             url = new URL(openApiURL);
             HttpURLConnection con = (HttpURLConnection) url.openConnection();
             con.setRequestMethod("POST");
             con.setDoOutput(true);
  
             DataOutputStream wr = new DataOutputStream(con.getOutputStream());
             wr.write(gson.toJson(request).getBytes("UTF-8"));
             wr.flush();
             wr.close();
  
             responseCode = con.getResponseCode();
             InputStream is = con.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             StringBuffer sb = new StringBuffer();
  
             String inputLine = "";
             while ((inputLine = br.readLine()) != null) {
                 sb.append(inputLine);
             }
             responBodyJson = sb.toString();
  
             // http 요청 오류 시 처리
             if ( responseCode != 200 ) {
                 // 오류 내용 출력
                 System.out.println("[error] " + responBodyJson);
                 return null;
             }
  
             responeBody = gson.fromJson(responBodyJson, Map.class);
             Integer result = ((Double) responeBody.get("result")).intValue();
             Map<String, Object> returnObject;
             List<Map> sentences;
  
             // 분석 요청 오류 시 처리
             if ( result != 0 ) {
  
                 // 오류 내용 출력
                 System.out.println("[error] " + responeBody.get("result"));
                 return null;
             }
  
             // 분석 결과 활용
             returnObject = (Map<String, Object>) responeBody.get("return_object");
             sentences = (List<Map>) returnObject.get("sentence");
  
             Map<String, Morpheme> morphemesMap = new HashMap<String, Morpheme>();
             Map<String, NameEntity> nameEntitiesMap = new HashMap<String, NameEntity>();
             List<Morpheme> morphemes = null;
             List<NameEntity> nameEntities = null;
  
             for( Map<String, Object> sentence : sentences ) {
                 // 형태소 분석기 결과 수집 및 정렬
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
  
                 // 개체명 분석 결과 수집 및 정렬
                 List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
                 for( Map<String, Object> nameEntityInfo : nameEntityRecognitionResult ) {
                     String name = (String) nameEntityInfo.get("text");
                     NameEntity nameEntity = nameEntitiesMap.get(name);
                     if ( nameEntity == null ) {
                         nameEntity = new NameEntity(name, (String) nameEntityInfo.get("type"), 1);
                         nameEntitiesMap.put(name, nameEntity);
                     } else {
                         nameEntity.count = nameEntity.count + 1;
                     }
                 }
             }
  
             if ( 0 < morphemesMap.size() ) {
                 morphemes = new ArrayList<Morpheme>(morphemesMap.values());
                 morphemes.sort( (morpheme1, morpheme2) -> {
                     return morpheme2.count - morpheme1.count;
                 });
             }
  
             if ( 0 < nameEntitiesMap.size() ) {
                 nameEntities = new ArrayList<NameEntity>(nameEntitiesMap.values());
                 nameEntities.sort( (nameEntity1, nameEntity2) -> {
                     return nameEntity2.count - nameEntity1.count;
                 });
             }
  
             // 형태소들 중 명사들에 대해서 많이 노출된 순으로 출력 ( 최대 5개 )
             morphemes
                 .stream()
                 .filter(morpheme -> {
                     return morpheme.type.equals("NNG") ||
                             morpheme.type.equals("NNP") ||
                             morpheme.type.equals("NNB");
                 })
                 .limit(5)
                 .forEach(morpheme -> {
                     System.out.println("[명사] " + morpheme.text + " ("+morpheme.count+")" );
                     connectDb.databaseInsert("[명사] "+morpheme.text);
                     
                     
                 });
  
             // 형태소들 중 동사들에 대해서 많이 노출된 순으로 출력 ( 최대 5개 )
             System.out.println("");
             morphemes
                 .stream()
                 .filter(morpheme -> {
                     return morpheme.type.equals("VV");
                 })
                 .limit(5)
                 .forEach(morpheme -> {
//                     System.out.println("[동사] " + morpheme.text + " ("+morpheme.count+")" );
                	 	connectDb.databaseInsert("[동사] "+morpheme.text);
                 });
             // 인식된 개채명들 많이 노출된 순으로 출력 ( 최대 5개 )
             System.out.println("");
             if(nameEntities != null) {
                 nameEntities
                 .stream()
                 .limit(5)
                 .forEach(nameEntity -> {
//                     System.out.println("[개체명] " + nameEntity.text + " ("+nameEntity.count+")" );
                 });
             }

         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
		return null;
    }

}
