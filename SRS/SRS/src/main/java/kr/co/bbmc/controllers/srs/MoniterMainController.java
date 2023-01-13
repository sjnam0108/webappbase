package kr.co.bbmc.controllers.srs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.bbmc.exceptions.ServerOperationForbiddenException;
import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.StateDay;
import kr.co.bbmc.models.srs.StateTime;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.models.srs.service.StateDayService;
import kr.co.bbmc.models.srs.service.StateTimeService;
import kr.co.bbmc.utils.Util;

/**
 * 모니터링 메인 컨트롤러
 */
@Controller("srs-moniter-main-controller")
@RequestMapping(value = "/srs/monitermain")
public class MoniterMainController {

	private static final Logger logger = LoggerFactory.getLogger(MoniterMainController.class);

	@Autowired
	private StateDayService stateDayService;

	@Autowired
	private StateTimeService stateTimeService;

	@Autowired
	private LocalCtrlService localCtrlService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 모니터링 메인 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "모니터링 현황판"), });

		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/monitermain";
	}

	/**
	 * 로컬제어기 상세 보기 상태 읽기 액션
	 */
	@RequestMapping(value = "/getStateDay", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getStateDay(@RequestBody Map<String, String> model) {
		Map<String, Integer> cntDate = new HashMap<String, Integer>();
		Map<String, Integer> avgDate = new HashMap<String, Integer>();
		Map<String, Integer> todayNomal = new HashMap<String, Integer>();
		Map<String, Object> result_json = new HashMap<String, Object>();
		List<Object> result = new ArrayList<Object>();
		List<LocalCtrl> lcList = localCtrlService.getLocalCtrlList();
		Integer todayNo1 = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Integer todayNo = 0;
		Integer nowNo = 0;
		String to = transFormat.format(today);
		String to1 = transFormat.format(today);
		
		for (LocalCtrl lc : lcList) {
			Integer sum_lc_nomal = 0;
			String lc_mac = lc.getlc_mac();
			List<StateTime> stList = stateTimeService.getStateTimeList(lc_mac,to);				
			for (StateTime st : stList) {
				sum_lc_nomal += Integer.parseInt(st.getlc_nomal_time());
			}
			if (sum_lc_nomal != 0) {
				todayNo1++;
			}
		}
		

		for (String val : model.values()) {
			val = val.replaceAll(" ", "");
			val = val.replace(".", "-");
			val = val.substring(0, val.length() - 1);
			List<StateDay> sdLists = stateDayService.getStateDayList(val);
			Integer sum = sdLists.size();
			cntDate.put(val, sum);
			
			Integer avg = 0;		
			
			for (StateDay sd : sdLists) {
				avg += Integer.parseInt(sd.getlc_nomal_day());
			}
			if (avg != 0) {
				avg = (avg/60);
				avg = avg / todayNo1;
			}
			avgDate.put(val, avg);
		}
		result.add(cntDate);
		result.add(avgDate);

			// 금일운영기기 구하는 부분


			Map<String, String> map1 = new HashMap<String, String>();
			lcList = localCtrlService.getLocalCtrlList();
			List<StateDay> sdList = new ArrayList<StateDay>();

			for (LocalCtrl lc : lcList) {
				Integer sum_lc_nomal = 0;
				StateDay sd_new = new StateDay();
				String lc_state = lc.getlc_state();
				String lc_mac = lc.getlc_mac();
				if (lc.getlc_state().equals("0")) {
					nowNo++;
				}
				List<StateTime> stList = stateTimeService.getStateTimeList(lc_mac, to1);
				for (StateTime st : stList) {
					sum_lc_nomal += Integer.parseInt(st.getlc_nomal_time());

				}
				if (sum_lc_nomal != 0) {
					todayNo++;
				}
			}

			todayNomal.put("today", todayNo);
			todayNomal.put("now", nowNo);
			result.add(todayNomal);
			result_json.put("result", result);
		try {
			return result_json;
		} catch (RuntimeException re) {
			logger.error("readSr", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("readSrr", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
	}

}
