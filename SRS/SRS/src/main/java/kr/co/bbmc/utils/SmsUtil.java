package kr.co.bbmc.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SmsUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(SmsUtil.class);

	private static HostnameVerifier getHV (){
		
		// Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
            	
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };
        
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        	logger.error("getHV", e);
        }
            
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL Host: " + urlHostName + " vs. "
                       + session.getPeerHost());
                return true;
            }
        };
            
        return hv;
    }
	
    /**
	 * SMS 전송
	 * 
	 *   결과:
	 *          1 - 성공
	 *         -1 - 실패
	 *         -2 - 전화번호 혹은 전송문자가 빈 값
	 */
	public static int sendSms(String phoneNum, String content) {
		
		if (Util.isNotValid(phoneNum) || Util.isNotValid(content)) {
			return -2;
		}
		
    	HashMap<String, Object> resultMap = new HashMap<>();
    	resultMap.put("apiKey", Util.getFileProperty("key.sms"));	//-- 필수 고정(API 사용자 KEY)
    	resultMap.put("message", content);										// SMS 메시지
    	resultMap.put("phoneNumber", phoneNum);									// 전화번호
    	resultMap.put("callbackNumber", Util.getFileProperty("sms.callbackNumber"));							//-- 필수 고정(발신 전화 번호)
    	resultMap.put("agentId", Util.getFileProperty("sms.agentId"));			//-- 필수 고정(테스트용 - 운영 수정 필요)
    	resultMap.put("brandId", Util.getFileProperty("sms.brandId"));			//-- 필수 고정(테스트용 - 운영 수정 필요)
    	resultMap.put("isAdvertisement", false);								//-- 고정(광고메시지 추가 여부)
    	resultMap.put("serivceTelNumber", "");									//-- 고정(수신거부 번호)
    	resultMap.put("brandName", "");											//-- 고정(브랜드 이름)
    	
    	String jsonString = Util.getObjectToJson(resultMap, true);
    	
    	int ret = -1;

    	try {
	    	HostnameVerifier hv = getHV();
	    	
	    	URL obj = new URL(Util.getFileProperty("url.smsService"));
	    			
	    	HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	    	con.setHostnameVerifier(hv);
	    	con.setDoOutput(true);
	    	con.setRequestMethod("POST");
	    	con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
	    	con.setRequestProperty("Accept-Charset", "UTF-8");
	    	con.setRequestProperty("Accept", "application/json");
	    	
	    	PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
	    	pw.write(jsonString.toString());
	    	pw.flush();
			
			int status = con.getResponseCode();
			//InputStream error = con.getErrorStream();
			if(status == 200){
		        Reader reader = new InputStreamReader(con.getInputStream(), "UTF-8");
		        BufferedReader br = new BufferedReader(reader);        
		        String line = "";
		        StringBuilder builder = new StringBuilder();
		        while ((line = br.readLine()) != null) {
		            builder.append(line);
		        }        
		        br.close();
		        String result = builder.toString();
		        
		        //logger.info("result >> 결과 값 [{}]", result);
		        
		        ObjectMapper m = new ObjectMapper();
		        JsonNode rootNode = m.readTree(result);
		        
		        int code = rootNode.path("code").asInt();
		        if (code == 0) {
		        	ret = 1;
		        }
			} else {
				logger.info("[SMS send] Wrong send status: " + status);
			}

			pw.close();
    	} catch (Exception e) {
        	logger.error("sendSms", e);
    	}
    	
		return ret;
	}
}
