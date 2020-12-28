package AiApi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AiRes {
	public static String AiAns(String uuid, String text) {
		String openApiURL = "http://aiopen.etri.re.kr:8000/Dialog";
		String accessKey = "34e1d871-d35a-4cb0-825f-26908159b85f";    // 발급받은 API Key
		String method = "dialog";           // method 호출 방식
		Gson gson = new Gson();
		String res = null;
		Map<String, Object> request = new HashMap<>();
		Map<String, String> argument = new HashMap<>();
		argument.put("uuid", uuid);
		argument.put("method", method);
		argument.put("text", text);

		request.put("access_key", accessKey);
		request.put("argument", argument);

		URL url;
		String responBody = null;
		try {
			url = new URL(openApiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(gson.toJson(request).getBytes("UTF-8"));
			wr.flush();
			wr.close();

			InputStream is = con.getInputStream();
			byte[] buffer = new byte[is.available()];
			int byteRead = is.read(buffer);
			responBody = new String(buffer,"UTF-8");

//	  		처음 key값 분리하기
	          String yourJson = responBody;
	          JsonElement element = JsonParser.parseString(yourJson);
	          JsonObject obj = element.getAsJsonObject(); //since you know it's a JsonObject
	          Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();//will return members of your object
	          JsonElement[] arr = new JsonElement[3]; 
	          int i = 0;
	          for (Map.Entry<String, JsonElement> entry: entries) {
	         	 i++;
	         	 arr[i] = entry.getValue();
	          }
	          
//		 		두번쨰 키값 분리하기
	          JsonElement[] arr1 = new JsonElement[5];
	          i = 0;
	          entries = arr[2].getAsJsonObject().entrySet();
	          for (Map.Entry<String, JsonElement> entry: entries) {
	         	 i++;
	         	 arr1[i] = entry.getValue();
	          }
//	          세번째 키값 분리하기
	          JsonElement[] arr2 = new JsonElement[5];
	          i = 0;
	          entries = arr1[3].getAsJsonObject().entrySet();
	          for (Map.Entry<String, JsonElement> entry: entries) {
	         	 i++;
	         	 arr2[i] = entry.getValue();
	          }
	          res = arr2[1].toString();
		}  catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
		return res;
	}
}
