package kr.co.bbmc.controllers.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.security.PrivateKey;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpSession;

import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.bbmc.exceptions.ServerOperationForbiddenException;
import kr.co.bbmc.info.SrsGlobalInfo;
import kr.co.bbmc.info.GlobalInfo;
import kr.co.bbmc.models.srs.AppSpeedMgr;
import kr.co.bbmc.models.srs.AutoCtrl;
import kr.co.bbmc.models.srs.LcSr;
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.Member;
import kr.co.bbmc.models.srs.NoticeEnroll;
import kr.co.bbmc.models.srs.StateDay;
import kr.co.bbmc.models.srs.StateTime;
import kr.co.bbmc.models.srs.FreezPredic;

import kr.co.bbmc.models.srs.service.SrsService;
import kr.co.bbmc.models.srs.service.StateDayService;
import kr.co.bbmc.models.srs.service.StateTimeService;
import kr.co.bbmc.models.srs.service.AppSpeedMgrService;
import kr.co.bbmc.models.srs.service.AutoCtrlService;
import kr.co.bbmc.models.srs.service.FreezPredicService;
import kr.co.bbmc.models.srs.service.LcSrService;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.models.srs.service.MemberService;
import kr.co.bbmc.models.srs.service.NoticeEnrollService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;
import net.sf.json.JSONObject;

/**
 * 고객 API 컨트롤러
 */
@Controller("api-srs-controller")
@RequestMapping(value = "/api/v2")
public class SrsApiController {

	private static final Logger logger = LoggerFactory.getLogger(SrsApiController.class);

	@Autowired
	private MemberService memService;

	@Autowired
	private SrsService srsService;

	@Autowired
	private LcSrService lcSrService;
	
	@Autowired
	private StateTimeService stateTimeService;
	
	@Autowired
	private StateDayService stateDayService;

	@Autowired
	private LocalCtrlService loctrlService;
	
    @Autowired 
    private AutoCtrlService autoCtrlService;
	
	@Autowired
	private AppSpeedMgrService appSpeedMgrService;
	
	@Autowired
	private FreezPredicService freezPredicService;
	
   @Autowired 
    private NoticeEnrollService noticeEnrollService;


	/**
	 * 로컬제어기의 현장정보 업데이트 api
	 * 
	 */
	@RequestMapping(value = "/local/put", method =RequestMethod.POST)
	public @ResponseBody Map<String, String> updateOnSite(@RequestBody Map<String, String> onSite,HttpSession session) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Map<String, String> map1 = new HashMap<String, String>();
	    //파라미터의 맥주소값 check
		String lc_mac = onSite.get("macAddr");
		if (lc_mac == null) {
			map1.put("msg", "no macAddr");
			return map1;
		}
		// 파라미터에서 로컬제어기의 현장정보를 받아와 변수처리
		LocalCtrl localCtrl = loctrlService.getLocalCtrl(lc_mac);
		String lc_temp = (String) onSite.get("temp");
		String lc_rain = (String) onSite.get("rain");
		String lc_sun = (String) onSite.get("sun");
		String lc_humidity = (String) onSite.get("humidity");
		String lc_vRange = (String) onSite.get("vRange");
		String lc_aPressure = (String) onSite.get("aPressure");	
		String lc_wCondition = (String) onSite.get("wCondition");
		String lc_wDirection = (String) onSite.get("wDirection");
		String lc_wSpeed = (String) onSite.get("wSpeed");
		String lc_trafficJam = (String) onSite.get("trafficJam");
		String lc_avgSpeed = (String) onSite.get("avgSpeed1");
		String lc_road_temp = (String) onSite.get("surfaceTemp");
		String lc_battery = (String) onSite.get("battery");
		String lc_dust10 = (String) onSite.get("dust010");
		String lc_dust25 = (String) onSite.get("dust025");
		String lc_dust100 = (String) onSite.get("dust100");
		
		// 정보 별 데이터 포맷팅
		if (lc_wCondition.equals("01")) {
			lc_wCondition = "비";
		} else if (lc_wCondition.equals("02")) {
			lc_wCondition = "강설";
		} else if (lc_wCondition.equals("03")) {
			lc_wCondition = "강우";
		} else {
			lc_wCondition = "정상";
		}
		localCtrl.setlc_wCondition(lc_wCondition);
		Integer lc_wDirectionF = Integer.parseInt(lc_wDirection);
		lc_wDirection = lc_wDirectionF.toString();		
		localCtrl.setlc_wDirection(lc_wDirection);
		
		
		if (lc_wSpeed.substring(0, 1).equals("0")) {
			lc_wSpeed = lc_wSpeed.substring(1, 3) + "." + lc_wSpeed.substring(3);
		} else if (!lc_wSpeed.substring(0, 1).equals("0")) {
			lc_wSpeed = lc_wSpeed.substring(0, 3) + "." + lc_wSpeed.substring(3);
		}
		Float lc_wSpeedF = Float.parseFloat(lc_wSpeed);
		lc_wSpeed = lc_wSpeedF.toString();
		localCtrl.setlc_wSpeed(lc_wSpeed);
		
		
		if (lc_rain.substring(0, 1).equals("0")) {
			lc_rain = lc_rain.substring(1, 3) + "." + lc_rain.substring(3);
		} else if (!lc_rain.substring(0, 1).equals("0")) {
			lc_rain = lc_rain.substring(0, 3) + "." + lc_rain.substring(3);
		}
		Float lc_rainF = Float.parseFloat(lc_rain);
		lc_rain = lc_rainF.toString();
		localCtrl.setlc_rain(lc_rain);
		
		
		Float lc_vRangeF = Float.parseFloat(lc_vRange);
		lc_vRange = lc_vRangeF.toString();
		localCtrl.setlc_vRange(lc_vRange);
		
		
		if (lc_trafficJam.equals("1")) {
			localCtrl.setlc_trafficJam("정체");
		} else if (lc_trafficJam.equals("0")) {
			localCtrl.setlc_trafficJam("정상");
		}
		
		
		if (lc_avgSpeed.substring(0, 1).equals("0")) {
			lc_avgSpeed = lc_avgSpeed.substring(1, 3) + "." + lc_avgSpeed.substring(3);
		} else if (!lc_avgSpeed.substring(0, 1).equals("0")) {
			lc_avgSpeed = lc_avgSpeed.substring(0, 3) + "." + lc_avgSpeed.substring(3);
		}
		
		
		Float lc_avgSpeedF = Float.parseFloat(lc_avgSpeed);
		lc_avgSpeed = lc_avgSpeedF.toString();
		localCtrl.setlc_avgSpeed(lc_avgSpeed);
		
		
		if(!lc_sun.equals("0")) {
			if (lc_sun.substring(0, 1).equals("0")) {
				lc_sun = lc_sun.substring(1, 3) + "." + lc_sun.substring(3);
			} else if (!lc_sun.substring(0, 1).equals("0")) {
				lc_sun = lc_sun.substring(0, 3) + "." + lc_sun.substring(3);
			}	
			Float lc_sunF = Float.parseFloat(lc_sun);
			lc_sun = lc_sunF.toString();
		}
		
		
		if(!lc_dust10.equals("0")) {
			if (lc_dust10.substring(0, 1).equals("0")) {
				lc_dust10 = lc_dust10.substring(1, 3) + "." + lc_dust10.substring(3);
			} else if (!lc_dust10.substring(0, 1).equals("0")) {
				lc_dust10 = lc_dust10.substring(0, 3) + "." + lc_dust10.substring(3);
			}	
			Float lc_dust10F = Float.parseFloat(lc_dust10);
			lc_dust10 = lc_dust10F.toString();
		}
		
		
		if(!lc_dust25.equals("0")) {
			if (lc_dust25.substring(0, 1).equals("0")) {
				lc_dust25 = lc_dust25.substring(1, 3) + "." + lc_dust25.substring(3);
			} else if (!lc_dust25.substring(0, 1).equals("0")) {
				lc_dust25 = lc_dust25.substring(0, 3) + "." + lc_dust25.substring(3);
			}	
			Float lc_dust25F = Float.parseFloat(lc_dust25);
			lc_dust25 = lc_dust25F.toString();
		}
		
		
		if(!lc_dust100.equals("0")) {
			if (lc_dust100.substring(0, 1).equals("0")) {
				lc_dust100 = lc_dust100.substring(1, 3) + "." + lc_dust100.substring(3);
			} else if (!lc_dust100.substring(0, 1).equals("0")) {
				lc_dust100 = lc_dust100.substring(0, 3) + "." + lc_dust100.substring(3);
			}	
			Float lc_dust100F = Float.parseFloat(lc_dust100);
			lc_dust100 = lc_dust100F.toString();
		}

		
		if(!lc_road_temp.equals("0")) {
			if (lc_road_temp.substring(0, 1).equals("0")) {
				lc_road_temp = lc_road_temp.substring(1, 4) + "." + lc_road_temp.substring(4);
				Float lc_road_tempF = Float.parseFloat(lc_road_temp);
				lc_road_tempF = lc_road_tempF * -1;
				lc_road_temp = lc_road_tempF.toString();
			} else if (!lc_road_temp.substring(0, 1).equals("0")) {
				lc_road_temp = lc_road_temp.substring(1, 4) + "." + lc_road_temp.substring(4);
				Float lc_road_tempF = Float.parseFloat(lc_road_temp);
				lc_road_temp = lc_road_tempF.toString();
			}	
		}
		if(!lc_temp.equals("0")) {
			if (lc_temp.substring(0, 1).equals("0")) {
				lc_temp = lc_temp.substring(1, 4) + "." + lc_temp.substring(4);
				Float lc_tempF = Float.parseFloat(lc_temp);
				lc_tempF = lc_tempF * -1;
				lc_temp = lc_tempF.toString();
			} else if (!lc_temp.substring(0, 1).equals("0")) {
				lc_temp = lc_temp.substring(1, 4) + "." + lc_temp.substring(4);
				Float lc_tempF = Float.parseFloat(lc_temp);
				lc_temp = lc_tempF.toString();
			}	
		}
		if(!lc_humidity.equals("0")) {
			if (lc_humidity.substring(0, 1).equals("0")) {
				lc_humidity = lc_humidity.substring(1, 3) + "." + lc_humidity.substring(3);
			} else if (!lc_humidity.substring(0, 1).equals("0")) {
				lc_humidity = lc_humidity.substring(0, 3) + "." + lc_humidity.substring(3);
			}	
			Float lc_humidityF = Float.parseFloat(lc_humidity);
			lc_humidity = lc_humidityF.toString();

		}
		if(!lc_aPressure.equals("0")) {
			if (lc_aPressure.substring(0, 1).equals("0")) {
				lc_aPressure = lc_aPressure.substring(1, 3) + "." + lc_aPressure.substring(3);
			} else if (!lc_aPressure.substring(0, 1).equals("0")) {
				lc_aPressure = lc_aPressure.substring(0, 3) + "." + lc_aPressure.substring(3);
			}	
			Float lc_aPressureF = Float.parseFloat(lc_aPressure);
			lc_aPressure = lc_aPressureF.toString();

		}
		//해당 로컬제어기에 포맷팅한 현장정보를 set하고 이후 업데이트
		localCtrl.setlc_sun(lc_sun);
		localCtrl.setlc_dust10(lc_dust10);
		localCtrl.setlc_dust25(lc_dust25);
		localCtrl.setlc_dust100(lc_dust100);
		localCtrl.setlc_road_temp(lc_road_temp);
		localCtrl.setlc_battery(lc_battery);
		localCtrl.setlc_temp(lc_temp);
		localCtrl.setlc_humidity(lc_humidity);
		localCtrl.setlc_aPressure(lc_aPressure);
		localCtrl.touchWho(session);
		localCtrl.touchPower();
		loctrlService.saveOrUpdate(localCtrl);	
		
		Date now = new Date();
		map1.put("control", format.format(now));
		map1.put("msg", "");

		return map1;

	}

	
	/**
	 * 로컬제어기의 SR 정보 업데이트
	 */
	@RequestMapping(value = "/sr/put", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> srPut(@RequestBody Map<String, String> model,HttpSession session) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Map<String, String> map1 = new HashMap<String, String>();
		//ArrayList<Integer> ids = new ArrayList<Integer>();
		// 파라미터에서 맥주소와 ip주소 check
		String lc_mac = (String) model.get("macAddr");
		String lc_ip = (String) model.get("ipAddr");
		if (lc_mac == null) {
			map1.put("msg", "no macAddr");
			return map1;
		}
		if (lc_ip == null) {
			map1.put("msg", "no ipAddr");
			return map1;
		}
		map1.put("code", "0");
		map1.put("macAddr", lc_mac);
		//로컬제어기 check
		LocalCtrl check = loctrlService.getLocalCtrl(lc_mac);
		//사용한 정보 제거
		model.remove("macAddr");
		model.remove("sendTime");
		model.remove("ipAddr");
		//로컬제어기가 없으면 새로 만들고 SR업데이트
		if (check == null) {
			String lc_name = "auto_" + lc_mac;
			String lc_valid_start = "";
			String lc_valid_end = "";
			String lc_area1 = "";
			String lc_addr1 = "";
			String lc_gps_lat = "";
			String lc_gps_long = "";
			String lc_manager_tel = "";
			String lc_sales_tel = "";
			String lc_memo = "";
			String lc_road_name = "";
			String lc_sp_limit = "";
			String lc_total_distance = "";
			String lc_state = "";
			String lc_dust = "0";

			LocalCtrl localCtrl = new LocalCtrl(lc_ip, lc_name, lc_mac, lc_valid_start, lc_valid_end, lc_area1,
					lc_addr1, lc_gps_lat, lc_gps_long, lc_manager_tel, lc_sales_tel, lc_memo, lc_state,
					lc_road_name,lc_sp_limit,lc_total_distance,session);
			localCtrl.setlc_dust10(lc_dust);
			localCtrl.setlc_dust25(lc_dust);
			localCtrl.setlc_dust100(lc_dust);
			loctrlService.saveOrUpdate(localCtrl);
			LocalCtrl lc_id = loctrlService.getLocalCtrl(lc_mac);
			int id_val = lc_id.getId();
			//SR 정보 인서트 & 업데이트
			List<LcSr> lcSrs = new ArrayList<LcSr>();
			@SuppressWarnings("unchecked")
			ArrayList<String> sr_nos = new ArrayList<String>();
			ArrayList<String> sr_batterys = new ArrayList<String>();
			ArrayList<String> sr_volts = new ArrayList<String>();
			ArrayList<String> sr_powers = new ArrayList<String>();
			ArrayList<String> light_types = new ArrayList<String>();
			ArrayList<String> lights = new ArrayList<String>();
			ArrayList<String> check_state = new ArrayList<String>();
			for (String key : model.keySet()) {
				sr_nos.add(key);
			}
			for (String val : model.values()) {
				String bat = val.substring(1, 4);
				String volt = val.substring(4,7);
				if (bat.substring(0, 1).equals("0")) {
					bat = bat.substring(1,3);
				}
				if (volt.substring(0, 1).equals("0")) {
					volt = volt.substring(1, 2) + "." + volt.substring(2);
				} else if (!volt.substring(0,1).equals("0")) {
					volt = volt.substring(0, 2) + "." + volt.substring(2);
				}
				sr_batterys.add(bat);
				sr_volts.add(volt);
				light_types.add(val.substring(7,8));
				lights.add(val.substring(8));
				sr_powers.add(val.substring(0, 1));
			}

			for (int j = 0; j < sr_nos.size(); j++) {
				LcSr lcSr = new LcSr();
				lcSr.touchWhoC(session);
				lcSr.touchCtrl(session);
				lcSr.setsr_no(sr_nos.get(j));
				lcSr.setsr_battery(sr_batterys.get(j));
				lcSr.setsr_volt(sr_volts.get(j));
				lcSr.setsr_power(sr_powers.get(j));
				lcSr.setlight_type(light_types.get(j));
				lcSr.setsr_light(lights.get(j));
				lcSr.setlc_id(id_val);
				lcSr.setlc_mac(lc_mac);
				lcSr.setsr_ctrl_light(lights.get(j));
				lcSr.setsr_ctrl_wLight(lights.get(j));
				lcSr.setsr_ctrl_yLight(lights.get(j));
				lcSr.setsr_ctrl_power("1");
				lcSrs.add(lcSr);
			}
			// sr정보를check하여 로컬제어기 상태 업데이트
			String lc_state_set = "";
			if (sr_powers.contains("0")) {
				if (sr_powers.contains("1")) {
					lc_state_set = "1";
				} else {
					lc_state_set = "2";
				}
			} else {
				lc_state_set = "0";
			}
			LocalCtrl localCtrl_set = loctrlService.getLocalCtrl(id_val);
			localCtrl_set.setlc_state(lc_state_set);
			loctrlService.saveOrUpdate(localCtrl_set);

			try {
				lcSrService.saveOrUpdates(lcSrs);
			} catch (DataIntegrityViolationException dive) {
				logger.error("update", dive);
				throw new ServerOperationForbiddenException("동일한 단축명의 자료가 이미 등록되어 있습니다.");
			} catch (ConstraintViolationException cve) {
				logger.error("update", cve);
				throw new ServerOperationForbiddenException("동일한 단축명의 자료가 이미 등록되어 있습니다.");
			} catch (Exception e) {
				logger.error("update", e);
				throw new ServerOperationForbiddenException("SaveError");
			}

			int lastIndex = lcSrs.size() - 1;
			Date control_date = lcSrs.get(lastIndex).getctrl_update_date();
			map1.put("control", format.format(control_date));	
			map1.put("msg", "");
			return map1;
			// 로컬제어기가 있을 경우 SR정보만 업데이트
		} else {
			LocalCtrl lc_id = loctrlService.getLocalCtrl(lc_mac);
			int id_val = lc_id.getId();
			ArrayList<String> sr_powers = new ArrayList<String>();
			List<LcSr> sr_li = lcSrService.getLcSrList(lc_mac);
			List<LcSr> lcSrs = new ArrayList<LcSr>();
			if (sr_li.isEmpty()) {
				for (String sr_no : model.keySet()) {
					String val = model.get(sr_no);
					LcSr lcSr = new LcSr();
					String sr_battery = val.substring(1, 4);
					String sr_volt = val.substring(4,7);
					if (sr_battery.substring(0, 1).equals("0")) {
						sr_battery = sr_battery.substring(1,3);
					}
					if (sr_volt.substring(0, 1).equals("0")) {
						sr_volt = sr_volt.substring(1, 2) + "." + sr_volt.substring(2);
					} else if (!sr_volt.substring(0,1).equals("0")) {
						sr_volt = sr_volt.substring(0, 2) + "." + sr_volt.substring(2);
					}
					String sr_light = val.substring(8);
					String light_type = val.substring(7,8);
					String sr_power = val.substring(0, 1);
					sr_powers.add(val.substring(0, 1));
					lcSr.setsr_battery(sr_battery);
					lcSr.setsr_volt(sr_volt);
					lcSr.setsr_light(sr_light);
					lcSr.setlight_type(light_type);
					lcSr.setsr_no(sr_no);
					lcSr.setsr_power(sr_power);
					lcSr.setlc_mac(lc_mac);
					lcSr.setlc_id(id_val);
					lcSr.setsr_ctrl_light(sr_light);
					lcSr.setsr_ctrl_wLight(sr_light);
					lcSr.setsr_ctrl_yLight(sr_light);
					lcSr.setsr_ctrl_power("1");
					lcSr.touchWhoC(session);
					lcSr.touchCtrl(session);
					lcSrs.add(lcSr);	
				}
			}
			else {
				ArrayList<String> sr_nos = new ArrayList<String>();
				for (LcSr sr_no : sr_li) {
					sr_nos.add(sr_no.getsr_no());
				}
				for (String sr_no : model.keySet()) {
					if (sr_nos.contains(sr_no)) {
						LcSr sr =lcSrService.getLcSr(lc_mac,sr_no);
						String val = model.get(sr_no);
						String sr_battery = val.substring(1, 4);
						String sr_volt = val.substring(4,7);
						if (sr_battery.substring(0, 1).equals("0")) {
							sr_battery = sr_battery.substring(1,3);
						}
						if (sr_volt.substring(0, 1).equals("0")) {
							sr_volt = sr_volt.substring(1, 2) + "." + sr_volt.substring(2);
						} else if (!sr_volt.substring(0,1).equals("0")) {
							sr_volt = sr_volt.substring(0, 2) + "." + sr_volt.substring(2);
						}
						String sr_light = val.substring(8);
						String light_type = val.substring(7,8);
						String sr_power = val.substring(0, 1);
						sr_powers.add(val.substring(0, 1));
						sr.setsr_battery(sr_battery);
						sr.setsr_volt(sr_volt);
						sr.setsr_power(sr_power);
						sr.setsr_light(sr_light);
						sr.setlight_type(light_type);
						sr.setlc_mac(lc_mac);
						sr.setlc_id(id_val);
						lcSrService.saveOrUpdate(sr);
					} else if (!sr_nos.contains(sr_no)) {
						String val = model.get(sr_no);
						LcSr lcSr = new LcSr();
						String sr_battery = val.substring(1, 4);
						String sr_volt = val.substring(4,7);
						if (sr_battery.substring(0, 1).equals("0")) {
							sr_battery = sr_battery.substring(1,3);
						}
						if (sr_volt.substring(0, 1).equals("0")) {
							sr_volt = sr_volt.substring(1, 2) + "." + sr_volt.substring(2);
						} else if (!sr_volt.substring(0,1).equals("0")) {
							sr_volt = sr_volt.substring(0, 2) + "." + sr_volt.substring(2);
						}
						String sr_light = val.substring(8);
						String light_type = val.substring(7,8);
						String sr_power = val.substring(0, 1);
						sr_powers.add(val.substring(0, 1));
						lcSr.setsr_battery(sr_battery);
						lcSr.setsr_volt(sr_volt);
						lcSr.setsr_light(sr_light);
						lcSr.setlight_type(light_type);
						lcSr.setsr_no(sr_no);
						lcSr.setsr_power(sr_power);
						lcSr.setlc_mac(lc_mac);
						lcSr.setlc_id(id_val);
						lcSr.setsr_ctrl_light(sr_light);
						lcSr.setsr_ctrl_wLight(sr_light);
						lcSr.setsr_ctrl_yLight(sr_light);
						lcSr.setsr_ctrl_power("1");
						lcSr.touchWhoC(session);
						Date today = new Date();
						Calendar Today  = Calendar.getInstance();
						Today.setTime(today);
						Today.add(Calendar.MINUTE, - 10);
						Date ctrl = Today.getTime();
						lcSr.setctrl_update_date(ctrl);
						lcSrs.add(lcSr);	
					}
				}
			}	
			// sr정보를check하여 로컬제어기 상태 업데이트
			String lc_state_set = "";
			if (sr_powers.contains("0")) {
				if (sr_powers.contains("1")) {
					lc_state_set = "1";
				} else {
					lc_state_set = "2";
				}
			} else {
				lc_state_set = "0";
			}
			LocalCtrl localCtrl_set = loctrlService.getLocalCtrl(id_val);
			localCtrl_set.touchPower();
			localCtrl_set.setlc_state(lc_state_set);
			loctrlService.saveOrUpdate(localCtrl_set);
			try {
				lcSrService.saveOrUpdates(lcSrs);
			} catch (DataIntegrityViolationException dive) {
				logger.error("update", dive);
				throw new ServerOperationForbiddenException("동일한 유니크키 자료가 이미 등록되어 있습니다.");
			} catch (ConstraintViolationException cve) {
				logger.error("update", cve);
				throw new ServerOperationForbiddenException("동일한 유니크키 자료가 이미 등록되어 있습니다.");
			} catch (Exception e) {
				logger.error("update", e);
				throw new ServerOperationForbiddenException("SaveError");
			}
			// 업데이트 시간을 control key 값에 저장하여 반환
			if(!sr_li.isEmpty()) {
				int lastIndex = sr_li.size() - 1;
				Date control_date = sr_li.get(lastIndex).getctrl_update_date();
				map1.put("control", format.format(control_date));
			}
			if(sr_li.isEmpty()) {
				int lastIndex = lcSrs.size() - 1;
				Date control_date = lcSrs.get(lastIndex).getctrl_update_date();
				map1.put("control", format.format(control_date));
			}
			map1.put("msg", "");
			return map1;
		}

	}

	/**
	 * 변경된 SR 정보 내보내기
	 */
	@RequestMapping(value = "/control/get", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> controlGet(@RequestBody Map<String, String> model) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Map<String, String> map1 = new HashMap<String, String>();
		String lc_mac = (String) model.get("macAddr");
		String sr_info = (String) model.get("sr");
		//맥주소 or  SR정보 유무 체크
		if (lc_mac == null) {
			map1.put("msg", "no macAddr");
			return map1;
		}
		if (sr_info == null) {
			map1.put("msg", "no sr");
			return map1;
		}

		map1.put("code", "0");
		map1.put("macAddr", lc_mac);
		ArrayList<Integer> ids = new ArrayList<Integer>();
		//로컬제어기 check
		LocalCtrl check = loctrlService.getLocalCtrl(lc_mac);
		if (check == null) {
			map1.put("msg", "this macAddr is not enrolled");
			return map1;
		}

		else {
		// 로컬제어기가 있으면 현재 제어된 SR정보를 불러와 반환 
			List<LcSr> sr_li = lcSrService.getLcSrList(lc_mac);
			String sr_info_set = "SR" + sr_info;
			if (sr_info_set.equals("SR0000")) {
				for (LcSr sr : sr_li) {
					String ctrl_power = sr.getsr_ctrl_power();
					String ctrl_wLight = sr.getsr_ctrl_wLight();
					String ctrl_yLight = sr.getsr_ctrl_yLight();
					if (ctrl_power == null) {
						ctrl_power = "1";
					}
					if (ctrl_yLight == null) {
						ctrl_yLight = "00000";
					}
					if (ctrl_wLight == null) {
						ctrl_wLight = "00000";
					}
					if (ctrl_wLight.length() == 4) {
						ctrl_wLight = "0" + ctrl_wLight;
					}else if (ctrl_wLight.length() == 3) {
						ctrl_wLight = "00" + ctrl_wLight;
					} else if (ctrl_wLight.length() == 2) {
						ctrl_wLight = "000" + ctrl_wLight;
					} else if (ctrl_wLight.length() == 1) {
						ctrl_wLight = "0000" + ctrl_wLight;
					}
					if (ctrl_yLight.length() == 4) {
						ctrl_yLight = "0" + ctrl_yLight;
					}else if (ctrl_yLight.length() == 3) {
						ctrl_yLight = "00" + ctrl_yLight;
					} else if (ctrl_yLight.length() == 2) {
						ctrl_yLight = "000" + ctrl_yLight;
					} else if (ctrl_yLight.length() == 1) {
						ctrl_yLight = "0000" + ctrl_yLight;
					}
					String sum = ctrl_power + ctrl_wLight + ctrl_yLight;
					map1.put(sr.getsr_no(), sum);
					Date control_date = sr.getctrl_update_date();
					map1.put("control", format.format(control_date));
				}
			} else if (!sr_info_set.equals("SR0000") && sr_info_set.substring(4, 6).equals("00") ) {

				for (LcSr sr : sr_li) {
					if (sr_info_set.substring(0,4).equals(sr.getsr_no().substring(0,4))) {
						String ctrl_power = sr.getsr_ctrl_power();
						String ctrl_wLight = sr.getsr_ctrl_wLight();
						String ctrl_yLight = sr.getsr_ctrl_yLight();
						if (ctrl_power == null) {
							ctrl_power = "1";
						}
						if (ctrl_wLight == null) {
							ctrl_wLight = "0";
						}
						if (ctrl_wLight.length() == 4) {
							ctrl_wLight = "0" + ctrl_wLight;
						}else if (ctrl_wLight.length() == 3) {
							ctrl_wLight = "00" + ctrl_wLight;
						} else if (ctrl_wLight.length() == 2) {
							ctrl_wLight = "000" + ctrl_wLight;
						} else if (ctrl_wLight.length() == 1) {
							ctrl_wLight = "0000" + ctrl_wLight;
						}
						if (ctrl_yLight.length() == 4) {
							ctrl_yLight = "0" + ctrl_yLight;
						}else if (ctrl_yLight.length() == 3) {
							ctrl_yLight = "00" + ctrl_yLight;
						} else if (ctrl_yLight.length() == 2) {
							ctrl_yLight = "000" + ctrl_yLight;
						} else if (ctrl_yLight.length() == 1) {
							ctrl_yLight = "0000" + ctrl_yLight;
						}
						String sum = ctrl_power + ctrl_wLight + ctrl_yLight;
						map1.put(sr.getsr_no(), sum);
						Date control_date = sr.getctrl_update_date();
						map1.put("control", format.format(control_date));
					}
				}
			} else {
				for (LcSr sr : sr_li) {
					if (sr_info_set.equals(sr.getsr_no())) {
						String ctrl_power = sr.getsr_ctrl_power();
						String ctrl_wLight = sr.getsr_ctrl_wLight();
						String ctrl_yLight = sr.getsr_ctrl_yLight();
						if (ctrl_power == null) {
							ctrl_power = "1";
						}
						if (ctrl_wLight == null) {
							ctrl_wLight = "0";
						}
						if (ctrl_wLight.length() == 4) {
							ctrl_wLight = "0" + ctrl_wLight;
						}else if (ctrl_wLight.length() == 3) {
							ctrl_wLight = "00" + ctrl_wLight;
						} else if (ctrl_wLight.length() == 2) {
							ctrl_wLight = "000" + ctrl_wLight;
						} else if (ctrl_wLight.length() == 1) {
							ctrl_wLight = "0000" + ctrl_wLight;
						}
						if (ctrl_yLight.length() == 4) {
							ctrl_yLight = "0" + ctrl_yLight;
						}else if (ctrl_yLight.length() == 3) {
							ctrl_yLight = "00" + ctrl_yLight;
						} else if (ctrl_yLight.length() == 2) {
							ctrl_yLight = "000" + ctrl_yLight;
						} else if (ctrl_yLight.length() == 1) {
							ctrl_yLight = "0000" + ctrl_yLight;
						}
						String sum = ctrl_power + ctrl_wLight + ctrl_yLight;
						map1.put(sr.getsr_no(), sum);
						Date control_date = sr.getctrl_update_date();
						map1.put("control", format.format(control_date));
					}
				}

			}
			return map1;
		}

	}
	
	/**
	 * 1분주기로 실행하여 적정광도 자동제어를 실시
	 * @throws ParseException 
	 * 
	 */
	@RequestMapping(value = "/update/auto_ctrl", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, String> updateAutoCtrl() throws ParseException {

		Map<String, String> result = new HashMap<String, String>();
		List<AutoCtrl> autoCtrls = autoCtrlService.getAutoCtrlList();
		
		if(autoCtrls.isEmpty()) {
			result.put("msg","등록된 적정광도제어 없습니다.");
			return result;
		}
		else {
			Integer i = 1;
			Integer j = 1;
			for(AutoCtrl autoCtrl : autoCtrls) {
				String lc_name = autoCtrl.getlc_name();
				String lc_mac  = autoCtrl.getlc_mac();
				LocalCtrl lc = loctrlService.getLocalCtrl(lc_mac);
					if(autoCtrl.getctrl_condition().equals("1")) {
						if(lc.getlc_vRange().isEmpty() || lc.getlc_rain().isEmpty()) {
							result.put("error" + i,  lc.getlc_name()+" 로컬제어기에 등록된 시야거리 or 강수량이 없습니다.");
							i = i +1;
							continue;
						}
						else{
							Float lc_vRange = Float.parseFloat(lc.getlc_vRange());
							Float lc_rain = Float.parseFloat(lc.getlc_rain());
							String lc_wCondtion = lc.getlc_wCondition();
							Date now = new Date();
							// 포맷팅 정의
							SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
							SimpleDateFormat formatS = new SimpleDateFormat("yyyyMMdd");
							// 포맷팅 적용
							String formatedNow = format.format(now);
							String formatedNowS = formatS.format(now);
							Date now_new = format.parse(formatedNow);
							Date check_a = format.parse(formatedNowS + "190000");
							Date check_b = format.parse(formatedNowS + "235959");
							Date check_aa = format.parse(formatedNowS + "000000");
							Date check_bb = format.parse(formatedNowS + "070000");
							Date day = format.parse(formatedNowS + "070000");
							Date night = format.parse(formatedNowS +"190000");
							Date dayPlusNew = format.parse(formatedNowS +"190000");
							if(now_new.before(check_b) && now_new.after(check_a)) {
								Date now_newS = formatS.parse(formatedNowS);
								day = format.parse(formatedNowS + "070000");
								night = format.parse(formatedNowS +"190000");
								
								Calendar cal = new GregorianCalendar(Locale.KOREA);
								cal.setTime(day);
								cal.add(Calendar.DATE,+1);
								String dayPlus = format.format(cal.getTime());
								dayPlusNew = format.parse(dayPlus);								
							}
							else if(now_new.before(check_bb) && now_new.after(check_aa)){
								Date now_newS = formatS.parse(formatedNowS);
								day = format.parse(formatedNowS + "070000");
								night = format.parse(formatedNowS +"190000");
								dayPlusNew = day;	
								Calendar cal = new GregorianCalendar(Locale.KOREA);
								cal.setTime(day);
								cal.add(Calendar.DATE,-1);
								String dayPlus = format.format(cal.getTime());
								night = format.parse(dayPlus);
							}
							//적정광도제어 설정값이 주간일 경우
							if(autoCtrl.gettime().equals("주간") && now_new.after(day) &&  now_new.before(night)) {
								result.put("msg", "주간에 들어옴");
								if( autoCtrl.getrain().equals("0 ~ 999")) {
									if(!lc_wCondtion.equals("정상")) {
										continue;
									}
									String[] vRange = autoCtrl.getv_ability().split(" ~ ");
									Float min_vRange = Float.parseFloat(vRange[0]);
									Float max_vRange = Float.parseFloat(vRange[1]);
									if(lc_vRange >= min_vRange && lc_vRange< max_vRange ) {
										List<LcSr> lcSrs = lcSrService.getLcSrList(lc_mac);
										for(LcSr sr : lcSrs) {
											if(sr.getlight_type().equals("0")) {
												sr.setsr_ctrl_wLight(autoCtrl.getctrl_wLight());
												sr.setsr_ctrl_light(autoCtrl.getctrl_wLight());
												sr.setsr_ctrl_yLight("00000");
												sr.touchCtrl();
											}
											else if(sr.getlight_type().equals("1")) {
												sr.setsr_ctrl_yLight(autoCtrl.getctrl_yLight());
												sr.setsr_ctrl_light(autoCtrl.getctrl_yLight());
												sr.setsr_ctrl_wLight("00000");
												sr.touchCtrl();
											}
										}
										lcSrService.saveOrUpdates(lcSrs);
										result.put("msg-"+lc.getlc_name(), lc.getlc_name() + " 업데이트 완료 시간 :주간");
										j = j +1;
									}

								}
								else {
									if(lc_wCondtion.equals("정상")) {
										continue;
									}
									String[] rain = autoCtrl.getrain().split(" ~ ");
									Float min_rain = Float.parseFloat(rain[0]);
									Float max_rain = Float.parseFloat(rain[1]);
									String[] vRange = autoCtrl.getv_ability().split(" ~ ");
									Float min_vRange = Float.parseFloat(vRange[0]);
									Float max_vRange = Float.parseFloat(vRange[1]);
									if(lc_vRange >= min_vRange && lc_vRange< max_vRange && lc_rain >= min_rain && lc_rain < max_rain) {
										List<LcSr> lcSrs = lcSrService.getLcSrList(lc_mac);
										for(LcSr sr : lcSrs) {
											if(sr.getlight_type().equals("0")) {
												sr.setsr_ctrl_wLight(autoCtrl.getctrl_wLight());
												sr.setsr_ctrl_light(autoCtrl.getctrl_wLight());
												sr.setsr_ctrl_yLight("00000");
												sr.touchCtrl();
											}
											else if(sr.getlight_type().equals("1")) {
												sr.setsr_ctrl_yLight(autoCtrl.getctrl_yLight());
												sr.setsr_ctrl_light(autoCtrl.getctrl_yLight());
												sr.setsr_ctrl_wLight("00000");
												sr.touchCtrl();
											}
										}
										lcSrService.saveOrUpdates(lcSrs);
										result.put("msg-"+lc.getlc_name(), lc.getlc_name() + " 업데이트 완료 시간 :주간");
										j = j +1;
									}

								}								
							}
							//적정광도제어 설정값이 야간일 경우
							else if(autoCtrl.gettime().equals("야간") && now_new.before(dayPlusNew) &&  now_new.after(night)) {
								result.put("msg", "야간에 들어옴");
								if( autoCtrl.getrain().equals("0 ~ 999")) {
									if(!lc_wCondtion.equals("정상")) {
										continue;
									}
									String[] vRange = autoCtrl.getv_ability().split(" ~ ");
									Float min_vRange = Float.parseFloat(vRange[0]);
									Float max_vRange = Float.parseFloat(vRange[1]);
									if(lc_vRange >= min_vRange && lc_vRange<= max_vRange ) {
										List<LcSr> lcSrs = lcSrService.getLcSrList(lc_mac);
										for(LcSr sr : lcSrs) {
											if(sr.getlight_type().equals("0")) {
												sr.setsr_ctrl_wLight(autoCtrl.getctrl_wLight());
												sr.setsr_ctrl_light(autoCtrl.getctrl_wLight());
												sr.setsr_ctrl_yLight("00000");
												sr.touchCtrl();
											}
											else if(sr.getlight_type().equals("1")) {
												sr.setsr_ctrl_yLight(autoCtrl.getctrl_yLight());
												sr.setsr_ctrl_light(autoCtrl.getctrl_yLight());
												sr.setsr_ctrl_wLight("00000");
												sr.touchCtrl();
											}
										}
										lcSrService.saveOrUpdates(lcSrs);
										result.put("msg-"+lc.getlc_name(), lc.getlc_name() + " 업데이트 완료 시간 :야간");
										j = j +1;
									}
									
								}
								else {
									if(lc_wCondtion.equals("정상")) {
										continue;
									}
									String[] vRange = autoCtrl.getv_ability().split(" ~ ");
									Float min_vRange = Float.parseFloat(vRange[0]);
									Float max_vRange = Float.parseFloat(vRange[1]);
									String[] rain = autoCtrl.getrain().split(" ~ ");
									Float min_rain = Float.parseFloat(rain[0]);
									Float max_rain = Float.parseFloat(rain[1]);
									if(lc_vRange >= min_vRange && lc_vRange<= max_vRange && lc_rain >= min_rain && lc_rain <= max_rain) {
										List<LcSr> lcSrs = lcSrService.getLcSrList(lc_mac);
										for(LcSr sr : lcSrs) {
											if(sr.getlight_type().equals("0")) {
												sr.setsr_ctrl_wLight(autoCtrl.getctrl_wLight());
												sr.setsr_ctrl_light(autoCtrl.getctrl_wLight());
												sr.setsr_ctrl_yLight("00000");
												sr.touchCtrl();
											}
											else if(sr.getlight_type().equals("1")) {
												sr.setsr_ctrl_yLight(autoCtrl.getctrl_yLight());
												sr.setsr_ctrl_light(autoCtrl.getctrl_yLight());
												sr.setsr_ctrl_wLight("00000");
												sr.touchCtrl();
											}
										}
										lcSrService.saveOrUpdates(lcSrs);
										result.put("msg-"+lc.getlc_name(), lc.getlc_name() + " 업데이트 완료 시간 :야간");
										j = j +1;
									}
									
								}
							}
							//적정광도설정이 주간도 야간도 아닐때
							else {
								result.put("msg-"+lc.getlc_name(), "예외발생");
							}	
						}
					}//중지상태인 자동제어 
					else {
						continue;
					}		

			}
			return result;			
		}
	}
	
	
	/**
	 * 시간별 로컬제어기의 상태 업데이트 dateChart, moniterMain 및 로컬제어기 상세보기에 쓰이는 데이타 업데이트
	 */
	@RequestMapping(value = "/updateLcStateTime", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> updateLcStateTime(HttpSession session) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Map<String, String> map1 = new HashMap<String, String>();
		List<LocalCtrl> lcList = loctrlService.getLocalCtrlList();
		List<StateTime> stList = new ArrayList<StateTime>();
		for (LocalCtrl lc : lcList) {
			StateTime st_new = new StateTime();
			String lc_state = lc.getlc_state();
			String lc_mac = lc.getlc_mac();
			if (lc_state==null){
				lc_state = "4";
			}
			// 1시간 주기별로 로컬제어기 상태에 따라 시간업데이트
			if (lc_state.equals("0")) {
				st_new.setlc_nomal_time("60");
				st_new.setsr_part_off_time("0");
				st_new.setsr_all_off_time("0");
				st_new.setlc_off_time("0");
				st_new.setno_enroll_time("0");
			}
			else if (lc_state.equals("1")) {
				st_new.setlc_nomal_time("0");
				st_new.setsr_part_off_time("60");
				st_new.setsr_all_off_time("0");
				st_new.setlc_off_time("0");
				st_new.setno_enroll_time("0");
			}
			else if (lc_state.equals("2")) {
				st_new.setlc_nomal_time("0");
				st_new.setsr_part_off_time("0");
				st_new.setsr_all_off_time("60");
				st_new.setlc_off_time("0");
				st_new.setno_enroll_time("0");
			}
			else if (lc_state.equals("3")) {
				st_new.setlc_nomal_time("0");
				st_new.setsr_part_off_time("0");
				st_new.setsr_all_off_time("0");
				st_new.setlc_off_time("60");
				st_new.setno_enroll_time("0");
			}
			else if (lc_state.equals("4")) {
				st_new.setlc_nomal_time("0");
				st_new.setsr_part_off_time("0");
				st_new.setsr_all_off_time("0");
				st_new.setlc_off_time("0");
				st_new.setno_enroll_time("60");
			}
			st_new.setlc_mac(lc_mac);
			st_new.touchWhoC(session);
			stList.add(st_new);
		}
		stateTimeService.saveOrUpdates(stList);
	
			map1.put("msg", "ok");
			return map1;
	}
	
	
	/**
	 * 하루 주기 로컬데이터의 상태 시간 업데이트
	 */
	@RequestMapping(value = "/updateLcStateDay", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> updateLcStateDay(HttpSession session) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddmmss");
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		
		String to = transFormat.format(today);

		Map<String, String> map1 = new HashMap<String, String>();
		List<LocalCtrl> lcList = loctrlService.getLocalCtrlList();
		List<StateDay> sdList = new ArrayList<StateDay>();
		
		for (LocalCtrl lc : lcList) {
			Integer sum_lc_nomal = 0;
			Integer sum_part_off = 0;
			Integer sum_all_off = 0;
			Integer sum_lc_off = 0;
			Integer sum_no_enroll = 0;
			StateDay sd_new = new StateDay();
			String lc_state = lc.getlc_state();
			String lc_mac = lc.getlc_mac();
			//  하룻동안 저장해온 시간별 데이터를 종합하여 업데이트
			List<StateTime> stList = stateTimeService.getStateTimeList(lc_mac,to);
			for(StateTime st : stList) {
				sum_lc_nomal = sum_lc_nomal + Integer.parseInt(st.getlc_nomal_time());
				sum_part_off = sum_part_off + Integer.parseInt(st.getsr_part_off_time());
				sum_all_off = sum_all_off + Integer.parseInt(st.getsr_all_off_time());
				sum_lc_off = sum_lc_off + Integer.parseInt(st.getlc_off_time());
				sum_no_enroll = sum_no_enroll + Integer.parseInt(st.getno_enroll_time());
			}
			sd_new.setlc_mac(lc_mac);
			sd_new.setlc_nomal_day(sum_lc_nomal.toString());
			sd_new.setsr_part_off_day(sum_part_off.toString());
			sd_new.setsr_all_off_day(sum_all_off.toString());
			sd_new.setlc_off_day(sum_lc_off.toString());
			sd_new.setno_enroll_day(sum_no_enroll.toString());
			sd_new.touchWhoC(session);
			sdList.add(sd_new);

		}
		stateDayService.saveOrUpdates(sdList);
	
			map1.put("msg", "ok");
			return map1;
	}
	
	

	/**
	 * 현장날씨 내보내기
	 * 
	 */
	@RequestMapping(value = "/out/on_site", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, Object> outOnSite(@RequestParam("id") String id) {

		Map<String, Object> result = new HashMap<String, Object>();
		LocalCtrl lc = loctrlService.getLocalCtrl(Integer.parseInt(id));
		if (lc == null) {
			result.put("msg","올바른 로컬제어기 아이디를 입력하세요.");
			return result;	
		}
		else {
			if (lc.getlc_wDirection() == null) {
				result.put("풍향" , "0.0");
			}else {
				result.put("풍향" , lc.getlc_wDirection());	
			}
			if (lc.getlc_battery() == null) {
				result.put("배터리" , "0");
			}else {
				result.put("배터리" , lc.getlc_battery());	
			}
			if (lc.getlc_vRange() == null) {
				result.put("가시거리" , "0");
			}else {
				result.put("가시거리" , lc.getlc_vRange());	
			}
			result.put("로컬제어기 이름" , lc.getlc_name());
			String lc_temp = lc.getlc_temp();
			String lc_humidity = lc.getlc_humidity();
			String lc_wCondition = lc.getlc_humidity();
			String lc_wSpeed = lc.getlc_humidity();
			String lc_trafficJam = lc.getlc_humidity();
			String lc_avgSpeed = lc.getlc_avgSpeed();
			String lc_aPressure = lc.getlc_aPressure();
			String lc_road_temp = lc.getlc_road_temp();
			if (lc_temp == null) {
				lc_temp = "0.0";
			}
			if (lc_humidity == null) {
				lc_humidity = "0";
			}
			if (lc_road_temp == null) {
				lc_road_temp = "0.0";
			}
			if (lc_wCondition == null) {
				lc_wCondition = "00";
			}
			if (lc_wSpeed == null) {
				lc_wSpeed = "0";
			}
			if (lc_trafficJam == null) {
				lc_trafficJam = "0";
			}
			if (lc_avgSpeed == null) {
				lc_avgSpeed = "0";
			}
			if (lc_aPressure == null) {
				lc_aPressure = "0";
			}
			if (lc_wCondition.equals("01")) {
				lc_wCondition = "비";
			} else if (lc_wCondition.equals("02")) {
				lc_wCondition = "강설";
			} else if (lc_wCondition.equals("03")) {
				lc_wCondition = "강우";
			} else {
				lc_wCondition = "정상";
			}
			result.put("날씨" , lc_wCondition);
			result.put("풍속" , lc_wSpeed);
			if (lc_trafficJam.equals("1")) {
				result.put("정체여부" , "정체");
			} else if (lc_trafficJam.equals("0")) {
				result.put("정체여부" , "정상");
			}
			result.put("평균속도" , lc_avgSpeed);
			result.put("노면온도" , lc_road_temp);
			result.put("현장온도" , lc_temp);	
			result.put("현장습도" , lc_humidity);
			result.put("대기압" , lc.getlc_aPressure());
			
			
			return result;			
		}

	}

	/**
	 * 10 분 동안 로컬제어기에게 정보가 오지않으면 제어기를 꺼져있다고 판단하여 상태 없데이트 
	 * @throws ParseException 
	 * 
	 */
	@RequestMapping(value = "/update/lc_power", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, String> updateLcPower() throws ParseException {

		Map<String, String> result = new HashMap<String, String>();
		List<LocalCtrl> localCtrls = loctrlService.getLocalCtrlList();
		if(localCtrls.isEmpty()) {
			result.put("status","1");
			result.put("msg","등록된 로컬제어기가 없습니다.");
			return result;
		}
		else {	
			for (LocalCtrl lc : localCtrls) {
				if(lc.getlc_state().equals("4")) {
					continue;
				}
				Date now = new Date();
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				// 포맷팅 적용
				String formatedNow = transFormat.format(now);
				Date nowNew = transFormat.parse(formatedNow);
				Date powerDate = lc.getPowerLastUpdateDate();
				Calendar cal = new GregorianCalendar(Locale.KOREA);
				cal.setTime(powerDate);
				cal.add(Calendar.MINUTE,+10);
				String plusPowerDate = transFormat.format(cal.getTime());
				Date plusPowerDateNew = transFormat.parse(plusPowerDate);
				if(plusPowerDateNew.after(nowNew)) {
					result.put(lc.getlc_name(),"넘어감");
					continue;
					
				}
				else {
					lc.setlc_state("3");
					result.put(lc.getlc_name(),"저장완료.");
					loctrlService.saveOrUpdate(lc);
				}
				
			}
			result.put("status","0");
			result.put("msg","저장완료.");
			return result;			
		}
	}
	
	/**
	 * 매일 정각에 로컬제어기가 꺼져있다면 로컬제어기 상태를 미확인으로 변경
	 * 
	 */
	@RequestMapping(value = "/update/lc_state", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, String> updateLcState() {

		Map<String, String> result = new HashMap<String, String>();
		List<LocalCtrl> localCtrls = loctrlService.getLocalCtrlList();
		if(localCtrls.isEmpty()) {
			result.put("status","1");
			result.put("msg","등록된 로컬제어기가 없습니다.");
			return result;
		}
		else {	
			for (LocalCtrl lc : localCtrls) {
				if (lc.getlc_state().equals("3")) {
					lc.setlc_state("4");
					loctrlService.saveOrUpdate(lc);
				}
				else {
					continue;
				}
			}
			result.put("status","0");
			return result;			
		}
	}
	
	/**
	 * 로컬정보 내보내기
	 * 
	 */
	@RequestMapping(value = "/out/local_ctrl", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, String> outLocalCtrl() {

		Map<String, String> result = new HashMap<String, String>();
		List<LocalCtrl> localCtrls = loctrlService.getLocalCtrlList();
		if(localCtrls.isEmpty()) {
			result.put("status","1");
			result.put("msg","등록된 로컬제어기가 없습니다.");
			return result;
		}
		else {	
			Integer i = 1;
			for (LocalCtrl lc : localCtrls) {
				Map<String, String> localInfo = new HashMap<String, String>();
				localInfo.put("ID", Integer.toString(lc.getId()) );
				localInfo.put("Name", lc.getlc_name() );
				localInfo.put("lat", lc.getlc_gps_lat() );
				localInfo.put("lon", lc.getlc_gps_long() );
				localInfo.put("주소", lc.getlc_addr1() );
				result.put( lc.getlc_name(), localInfo.toString());
				i = i +1;
			}

			
			return result;			
		}
	}
	
	/**
	 * 로컬정보 내보내기
	 * 
	 */
	@RequestMapping(value = "/out/local_ctrl_gps", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String, String> outLocalCtrlByGps(@RequestParam("lat") String lat,@RequestParam("lon") String lon) {

		Map<String, String> result = new HashMap<String, String>();
		List<LocalCtrl> localCtrls = loctrlService.getLocalCtrlList();
		Map<String, Double> resultInfo = new HashMap<String, Double>();
		if(localCtrls.isEmpty()) {
			result.put("status","1");
			result.put("msg","등록된 로컬제어기가 없습니다.");
			return result;
		}
		else {	
			Integer i = 1;
			for (LocalCtrl lc : localCtrls) {
				if(lc.getlc_gps_lat().isEmpty() ||lc.getlc_gps_long().isEmpty()) {
					continue;
				}
				Double lc_lat = Double.parseDouble(lc.getlc_gps_lat());
				Double lc_lon = Double.parseDouble(lc.getlc_gps_long());
				Double val = distance(Double.parseDouble(lat),Double.parseDouble(lon),lc_lat,lc_lon,"meter");
				resultInfo.put(Integer.toString(lc.getId()),val );
				i = i +1;
			}
			// Min Value
			Double minValue = Collections.min(resultInfo.values());
			String id = getKey(resultInfo,minValue);
			result.put("msg",id);
			result.put("status","0");
			return result;			
		}
	}
	
	 // hashmap에 value 로 key 찾기
	public static <K, V> K getKey(Map<K, V> map, V value) {
        for (K key : map.keySet()) {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
	}

	// 마일(Mile) 단위
    double distanceMile =
        distance(37.504198, 127.047967, 37.501025, 127.037701, "");
     
    // 미터(Meter) 단위
    double distanceMeter =
        distance(37.504198, 127.047967, 37.501025, 127.037701, "meter");
     
    // 킬로미터(Kilo Meter) 단위
    double distanceKiloMeter =
        distance(37.504198, 127.047967, 37.501025, 127.037701, "kilometer");
    /**
 	* 두 지점간의 거리 계산
 	*
 	* @param lat1 지점 1 위도
 	* @param lon1 지점 1 경도
 	* @param lat2 지점 2 위도
 	* @param lon2 지점 2 경도
 	* @param unit 거리 표출단위
 	* @return
 	*/
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

    	double theta = lon1 - lon2;
    	double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
     
    	dist = Math.acos(dist);
    	dist = rad2deg(dist);
    	dist = dist * 60 * 1.1515;
     
    	if (unit == "kilometer") {
        	dist = dist * 1.609344;
    	} else if(unit == "meter"){
        	dist = dist * 1609.344;
    	}

    	return (dist);
	}
 

	// This function converts decimal degrees to radians
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
 
	// This function converts radians to decimal degrees
	private static double rad2deg(double rad) {
    	return (rad * 180 / Math.PI);
	}

	
	
	/**
	 *유고사항 내보내기
	 * 
	 */
	@RequestMapping(value = "/out/notice", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String,Object> outNotice(@RequestParam("id") String id) {

		Map<String, Object> result = new HashMap<String, Object>();
		LocalCtrl lc = loctrlService.getLocalCtrl(Integer.parseInt(id));
		if (lc == null) {
			result.put("status","1");
			result.put("msg","올바른 로컬제어기 아이디를 입력하세요.");
			return result;	
		}
		String lc_name = lc.getlc_name();
		Integer i = 1;
		List<NoticeEnroll> noticeEnrolls = noticeEnrollService.getNoticeList(lc_name);
		if(noticeEnrolls.isEmpty()) {
			result.put("status","1");
			result.put("msg","로컬제어기에 해당하는 유고사항이 없습니다.");
			return result; 
		}
		else {
			for(NoticeEnroll ne : noticeEnrolls) {
				Map<String, String> noticeInfo = new HashMap<String, String>();
				noticeInfo.put("유고사항", ne.getnotice_name() );
				noticeInfo.put("내용", ne.getnotice_content() );
				noticeInfo.put("로컬제어기 이름", ne.getlc_name() );
				result.put( i.toString(), noticeInfo);
				i = i +1;
			}
			return result;			
		}


	}

	/**
	 * 적정속도관리 내보내기
	 * 
	 */
	@RequestMapping(value = "/out/speed_mgr", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String,Object> outSpeedMgrCtrl(@RequestParam("id") String id,@RequestParam("level") String level) {

		Map<String, Object> result = new HashMap<String, Object>();
		LocalCtrl localCtrl = loctrlService.getLocalCtrl(Integer.parseInt(id));
		if(localCtrl == null) {
			result.put("status","1");
			result.put("msg","등록된 로컬제어기가 없습니다.");
			return result;
		}
		if(level.equals("1")||level.equals("2")||level.equals("3")) {
			String lc_vRange_s = localCtrl.getlc_vRange();
			Float lc_vRange = (float) 0.00;
			if(lc_vRange_s == null) {
				result.put("status","1");
				result.put("msg","로컬제어기에 등록된 시야거리가 없습니다.");
				return result;					
			}
			else {
				lc_vRange = Float.parseFloat(localCtrl.getlc_vRange());	
			}
			List<AppSpeedMgr> appSpeedMgrs = appSpeedMgrService.getAppSpeedMgrList(localCtrl.getlc_name());
			if(appSpeedMgrs.isEmpty()) {
				result.put("status","1");
				result.put("msg","등록된 적정속도가 없습니다.");
				return result;
			}
			else {
				for (AppSpeedMgr asm : appSpeedMgrs) {
					if(asm.getde_section().substring(0,1).equals(level)) {
						String vRange = asm.getv_ability();
						String[] vRangeList = vRange.split("~");
						Float min_vRange = Float.parseFloat(vRangeList[0]);
						Float max_vRange = Float.parseFloat(vRangeList[1]);
						if(min_vRange <= lc_vRange  && lc_vRange < max_vRange) {
							    result.put("status","0");
								result.put("msg", asm.getspeed());
								return result;
						}		
						else {
							result.put("status","1");
							result.put("msg","로컬제어기의 현재 시야거리에 해당하는 적정속도가 없습니다.");
						}
					}
					else {
						continue;
					}
		
				}					
				
			}
				
			return result;	
			
		}
		else {	
			result.put("status","1");
			result.put("msg","올바르지 않은 Level을 입력하였습니다.");
			return result;
		}
	}
	
	
	/**
	 * 결빙예측 내보내기
	 * 
	 */
	@RequestMapping(value = "/out/blackice", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Map<String,Object> outblackice(@RequestParam("id") String id) {

		Map<String, Object> result = new HashMap<String, Object>();
		LocalCtrl lc = loctrlService.getLocalCtrl(Integer.parseInt(id));
		if(lc == null) {
			result.put("status","1");
			result.put("msg","등록된 로컬제어기가 없습니다.");
			return result;
		}
		else {	
			FreezPredic fp = freezPredicService.getFreezPredic(lc.getlc_name());
			if(fp == null) {
				result.put("status","1");
				result.put("msg","요청한 로컬제어기에 등록된 결빙예측 값이 없습니다.");
				return result;		
			}
			else{
				String lc_wCondition = lc.getlc_wCondition();
				String lc_temp = lc.getlc_temp();
				String lc_humidity = lc.getlc_humidity();
				String lc_road_temp = lc.getlc_road_temp();
				String lc_wSpeed = lc.getlc_wSpeed();
				if(lc_wCondition == null) {
					result.put("msg","요청한 로컬제어기에 등록된 현장기상 상태 값이 없습니다");
					result.put("status","1");
					return result;	
				}
				if(lc_temp == null) {
					result.put("msg","요청한 로컬제어기에 등록된 현장 대기온도 값이 없습니다");
					result.put("status","1");
					return result;	 
				}
				if(lc_humidity == null) {
					result.put("msg","요청한 로컬제어기에 등록된 현장 대기습도 값이 없습니다");
					result.put("status","1");
					return result;	 
				}
				if(lc_road_temp == null) {
					result.put("msg","요청한 로컬제어기에 등록된 현장 노면온도 값이 없습니다");
					result.put("status","1");
					return result;	 
				}
				if(lc_wSpeed == null) {
					result.put("msg","요청한 로컬제어기에 등록된 현장 풍속 값이 없습니다");
					result.put("status","1");
					return result;	 
				}	
				float lc_temp_i = 0;
				float lc_road_temp_i = 0;
				if(lc_temp.contains("-")) {
					lc_temp = lc_temp.substring(1);
					lc_temp_i = Float.parseFloat(lc_temp) * -1;
				}
				else {
					lc_temp_i = Float.parseFloat(lc_temp);
				}
				if(lc_road_temp.contains("-")) {
						lc_road_temp = lc_road_temp.substring(1);							lc_road_temp_i = Float.parseFloat(lc_road_temp) * -1;
				}
				else {
					lc_road_temp_i = Float.parseFloat(lc_road_temp);
				}
				if( lc_temp_i <= Float.parseFloat(fp.getair_temp()) 
						&& Float.parseFloat(lc_humidity) >= Float.parseFloat(fp.getair_humid()) 
						&& lc_road_temp_i <= Float.parseFloat(fp.getroad_temp())
						&& Float.parseFloat(lc_wSpeed) <= Float.parseFloat(fp.getwin_speed())) {
					result.put("msg","결빙");
					result.put("status","0");
					return result;		
				}
				else {
					result.put("msg","비결빙");
					result.put("status","0");
					return result;	
				}		
			}
		}
	}
	
	


}
