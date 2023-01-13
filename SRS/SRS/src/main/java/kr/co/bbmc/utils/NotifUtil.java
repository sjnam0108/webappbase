package kr.co.bbmc.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.bbmc.models.srs.service.SrsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class NotifUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(NotifUtil.class);
	
	@Autowired
	public void setStaticSrsService(SrsService srsService) {
		NotifUtil.sSrsService = srsService;
	}
	
	static SrsService sSrsService;

	
	private static String getMessage(String title, String body, List<String> to) {
		
		//
		// 알림 메시지 키
		//    참고: https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=ko
		//
		
	    JSONObject obj = new JSONObject();
	    obj.put("title", title);
	    obj.put("body", body);
	    
    	JSONObject msg = new JSONObject();
    	// 2020.03.11 FCM 전송 notification을 data 로 변경
    	//msg.put("notification", obj);
    	msg.put("data", obj);
    	
    	if (to == null || to.size() == 0) {
    		return null;
    	} else if (to.size() == 1) {
        	msg.put("to", to.get(0));
    	} else {
    		JSONArray mobileTokens = new JSONArray();
    		for(String token : to) {
    			mobileTokens.add(token);
    		}
    		msg.put("registration_ids", mobileTokens);
    	}
    	
    	return msg.toString();
	}
	
	private static String getMessage(String title, String body, String dataTitle, String dataBody, 
			List<String> to) {
		
		//
		// 알림 메시지 키
		//    참고: https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=ko
		//
		
	    JSONObject obj = new JSONObject();
	    obj.put("title", title);
	    obj.put("body", body);
	    
    	JSONObject msg = new JSONObject();
    	msg.put("notification", obj);

    	JSONObject dataObj = new JSONObject();
    	dataObj.put("title", dataTitle);
    	dataObj.put("body", dataBody);
    	msg.put("data", dataObj);
    	
    	if (to == null || to.size() == 0) {
    		return null;
    	} else if (to.size() == 1) {
        	msg.put("to", to.get(0));
    	} else {
    		JSONArray mobileTokens = new JSONArray();
    		for(String token : to) {
    			mobileTokens.add(token);
    		}
    		msg.put("registration_ids", mobileTokens);
    	}
    	
    	return msg.toString();
	}
	
    /**
	 * FCM 통지 전송
	 * 
	 *   결과:
	 *          0 - 성공(모두 성공),
	 *         -1 - 실패(모두 실패),
	 *         -2 - 전송 대상 수 0,
	 *         -3 - 잘못된 인자(title or key)
	 *         -4 - 예기치 않은 상태(예: 요청 건수와 결과 건수가 다름 등)
	 *         >0 - 부분 성공(성공한 수)
	 */



	

}
