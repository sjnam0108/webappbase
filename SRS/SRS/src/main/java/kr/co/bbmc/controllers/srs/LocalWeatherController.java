package kr.co.bbmc.controllers.srs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import kr.co.bbmc.exceptions.ServerOperationForbiddenException;
import kr.co.bbmc.info.SrsGlobalInfo;
import kr.co.bbmc.info.GlobalInfo;
import kr.co.bbmc.models.SrsUserCookie;
import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.service.LcSrService;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 지역날씨 컨트롤러
 */
@Controller("srs-local-weather-controller")
@RequestMapping(value = "/srs/localweather")
public class LocalWeatherController {

	private static final Logger logger = LoggerFactory.getLogger(LocalWeatherController.class);

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	@Autowired
	private LocalCtrlService loctrlService;

	/**
	 * 지역날씨 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "지역날씨"), });

		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/localweather";
	}

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request) {

		try {
			return loctrlService.getLocalCtrlList(request);
		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
	}

	/**
	 * 요청한 페이지의 xml 데이터 읽어오기 액션
	 */
	public static class XmlExtraction {

		public static Map<String, String> getXmlData(String xmlFile) throws Exception {
			Map<String, String> localW = new HashMap<String, String>();
			// 1.문서를 읽기위한 공장을 만들어야 한다.
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			// 2.빌더 생성
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			// 3.생성된 빌더를 통해서 xml문서를 Document객체로 파싱해서 가져온다
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();// 문서 구조 안정화
			Object a = doc.getDocumentElement().getNodeName();
			Element root = doc.getDocumentElement();
			NodeList nList = doc.getElementsByTagName("CurrWth");
			Element currWth = (Element) nList.item(0);
			// Node itema = currWth.getFirstChild();
			// Element item = (Element) getChildren(currWth, "ITEM");
			// Element base = (Element) item.item(0);
			// String baseTime = item.getAttribute("baseTime");
			// String b = nList.toString();

			Element eventEle = (Element) nList.item(0);
			Element item = getChildren(eventEle, "Item");
			String lc_pu_humidity = item.getAttribute("reh");
			String lc_pu_rain_state = item.getAttribute("pty");
			String lc_pu_wCondition = item.getAttribute("dispWeather");
			String lc_pu_temp = item.getAttribute("tem");
			String lc_pu_wDirection = item.getAttribute("dispWeather");
			String lc_pu_wSpeed = item.getAttribute("wsd");
			String max = item.getAttribute("max");
			String min = item.getAttribute("min");
			String lc_pu_max_min = max + " / " + min;
			String lc_pu_dust = item.getAttribute("pm10");
			String lc_pu_ultra_dust = item.getAttribute("pm25");
			localW.put("lc_pu_humidity", lc_pu_humidity);
			localW.put("lc_pu_wCondition", lc_pu_wCondition);
			localW.put("lc_pu_temp", lc_pu_temp);
			localW.put("lc_pu_wDirection", lc_pu_wDirection);
			localW.put("lc_pu_rain_state", lc_pu_rain_state);
			localW.put("lc_pu_wSpeed", lc_pu_wSpeed);
			localW.put("lc_pu_max_min", lc_pu_max_min);
			localW.put("lc_pu_dust", lc_pu_dust);
			localW.put("lc_pu_ultra_dust", lc_pu_ultra_dust);
			logger.info(lc_pu_wDirection);
			return localW;

		}

		public static Element getChildren(Element element, String tagName) {
			NodeList list = element.getElementsByTagName(tagName);
			Element cElement = (Element) list.item(0);

			return cElement;

		}

	}

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/readWeather", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, String> readWeather(@RequestBody Map<String, String> addr) {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("msg", "0");

		Map<String, String> localW = new HashMap<String, String>();

		List<LocalCtrl> localCtrls = loctrlService.getLocalCtrlList();
		ArrayList<String> lc_addr = new ArrayList<String>();
		for (LocalCtrl localCtrl : localCtrls) {
			for (String key : addr.keySet()) {
				if (localCtrl.getlc_addr1().contains(key) == true) {
					Object code = addr.get(key);
					String url = "http://api.signcast.co.kr/lifestyle/weather/weather_" + code + ".xml";
					try {
						localW = XmlExtraction.getXmlData(url);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String lc_pu_temp = (String) localW.get("lc_pu_temp");
					String lc_pu_rain_state = (String) localW.get("lc_pu_rain_state");
					String lc_pu_max_min = (String) localW.get("lc_pu_max_min");
					String lc_pu_wCondition = (String) localW.get("lc_pu_wCondition");
					String lc_pu_humidity = (String) localW.get("lc_pu_humidity");
					String lc_pu_wDirection =(String) localW.get("lc_pu_wDirection");
					String lc_pu_wSpeed = (String) localW.get("lc_pu_wSpeed");
					String lc_pu_dust = (String) localW.get("lc_pu_dust");
					String lc_pu_ultra_dust = (String) localW.get("lc_pu_ultra_dust");
					localCtrl.setlc_pu_temp(lc_pu_temp);
					localCtrl.setlc_pu_rain_state(lc_pu_rain_state);
					localCtrl.setlc_pu_max_min(lc_pu_max_min);
					localCtrl.setlc_pu_wCondition(lc_pu_wCondition);
					localCtrl.setlc_pu_humidity(lc_pu_humidity);
					localCtrl.setlc_pu_wDirection(lc_pu_wDirection);
					localCtrl.setlc_pu_wSpeed(lc_pu_wSpeed);
					localCtrl.setlc_pu_dust(lc_pu_dust);
					localCtrl.setlc_pu_ultra_dust(lc_pu_ultra_dust);
					loctrlService.saveOrUpdate(localCtrl);
				}
			}

		}
		return map1;

	}

}
