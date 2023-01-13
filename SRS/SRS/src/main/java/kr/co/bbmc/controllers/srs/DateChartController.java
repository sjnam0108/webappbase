package kr.co.bbmc.controllers.srs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.StateDay;
import kr.co.bbmc.models.srs.StateTime;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.models.srs.service.MemberService;
import kr.co.bbmc.models.srs.service.StateDayService;
import kr.co.bbmc.models.srs.service.StateTimeService;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 기간차트 컨트롤러
 */
@Controller("srs-date-chart-controller")
@RequestMapping(value = "/srs/datechart")
public class DateChartController {

	private static final Logger logger = LoggerFactory.getLogger(DateChartController.class);

	@Autowired
	private LocalCtrlService localCtrlService;

	@Autowired
	private StateTimeService stateTimeService;

	@Autowired
	private StateDayService stateDayService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 기간차트 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "기간차트"), });

		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/datechart";
	}

	/**
	 * 1시간 별 데이터 불러오기 액션
	 */
	@RequestMapping(value = "/getStateDay", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getStateDay(@RequestBody Map<String, String> model) {
		Map<String, Integer> sumDate = new HashMap<String, Integer>();
		Map<String, Integer> avgDate = new HashMap<String, Integer>();
		Map<String, Integer> todayData = new HashMap<String, Integer>();
		Map<String, Object> result_json = new HashMap<String, Object>();
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 오늘 날짜 데이터 구하기
		Date today = new Date();
		Integer deviceCnt = 0;
		Integer nowNo = 0;
		String to = transFormat.format(today);
		List<LocalCtrl> lcList = localCtrlService.getLocalCtrlList();
		deviceCnt = lcList.size();
		List<StateDay> sdList = new ArrayList<StateDay>();
		Integer sum_lc_nomal = 0;
		Integer reAvgNoCnt = 0;
		Integer avgNoCnt = 0;
		for (LocalCtrl lc : lcList) {
			StateDay sd_new = new StateDay();
			String lc_state = lc.getlc_state();
			String lc_mac = lc.getlc_mac();
			if (lc.getlc_state().equals("0")) {
				nowNo = nowNo + 1;
			}
			List<StateTime> stList = stateTimeService.getStateTimeList(lc_mac, to);
			Integer avgVal = stList.size();
			for (StateTime st : stList) {
				sum_lc_nomal = sum_lc_nomal + Integer.parseInt(st.getlc_nomal_time());

				if (!st.getlc_nomal_time().equals("0")) {
					avgNoCnt = avgNoCnt + 1;
				}
			}
			reAvgNoCnt = reAvgNoCnt + avgNoCnt;
		}
		if (!reAvgNoCnt.equals(0) || !avgNoCnt.equals(0)) {
			Integer avgLcNoCnt = reAvgNoCnt / avgNoCnt;
			todayData.put("avgLcNoCnt", avgLcNoCnt);
		} else {
			Integer avgLcNoCnt = 0;
			todayData.put("avgLcNoCnt", avgLcNoCnt);
		}
		Integer avgLcNormal = sum_lc_nomal / deviceCnt;
		todayData.put("deviceCnt", deviceCnt);
		todayData.put("avgLcNoraml", avgLcNormal);
		String type = model.get("type");
		String start = model.get("start");
		String end = model.get("end");

		Date startDate = null;
		try {
			startDate = transFormat.parse(start);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Date endDate = null;
		try {
			endDate = transFormat.parse(end);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 검색유형이 데일리일경우
		if (type.equals("DAY")) {
			List<String> dayList = new ArrayList<String>();
			Calendar startD = Calendar.getInstance();
			startD.setTime(startDate);
			Calendar endD = Calendar.getInstance();
			endD.setTime(endDate);
			endD.add(Calendar.DATE, +0);
			String endDay = new java.text.SimpleDateFormat("yyyy-MM-dd").format(endD.getTime());

			startD.add(Calendar.DATE, +0);
			String startDay = new java.text.SimpleDateFormat("yyyy-MM-dd").format(startD.getTime());
			dayList.add(startDay);
			while (true) {
				startD.add(Calendar.DATE, +1);
				startDay = new java.text.SimpleDateFormat("yyyy-MM-dd").format(startD.getTime());
				dayList.add(startDay);
				if (startDay.equals(endDay)) {
					break;
				}

			}
			for (String day : dayList) {
				List<StateDay> sdLists = stateDayService.getDayStateDayList(day);
				List<String> sumList = new ArrayList<String>();
				if (!sdLists.isEmpty()) {
					Integer sum = sdLists.size();
					Integer avg = 0;
					for (StateDay sd : sdLists) {
						sumList.add(sd.getlc_mac());
						avg = avg + Integer.parseInt(sd.getlc_nomal_day());
					}
					// List를 Set으로 변경
					Set<String> set = new HashSet<String>(sumList);
					// Set을 List로 변경
					List<String> sumListNew = new ArrayList<String>(set);
					sumDate.put(day, sumListNew.size());
					if (avg != 0) {
						avg = avg / sumListNew.size();
					}
					avgDate.put(day, avg);
				} else if (sdLists.isEmpty()) {
					Integer sum = 0;
					Integer avg = 0;
					sumDate.put(day, sum);
					avgDate.put(day, avg);
				}

			}
			result_json.put("sum", sumDate);
			result_json.put("avg", avgDate);
			result_json.put("todayData", todayData);
			logger.debug(startDay);
		}
		// 검색유형이 주간일 경우
		if (type.equals("WEEK")) {
			List<String> weekList = new ArrayList<String>();
			Calendar startW = Calendar.getInstance();
			startW.setTime(startDate);
			Calendar endW = Calendar.getInstance();
			endW.setTime(endDate);
			endW.add(Calendar.WEEK_OF_MONTH, +0);
			String endWeek = new java.text.SimpleDateFormat("yyyy-MM-dd").format(endW.getTime());

			startW.add(Calendar.WEEK_OF_MONTH, +0);
			String startWeek = new java.text.SimpleDateFormat("yyyy-MM-dd").format(startW.getTime());
			weekList.add(startWeek);
			while (true) {
				startW.add(Calendar.WEEK_OF_MONTH, +1);
				startWeek = new java.text.SimpleDateFormat("yyyy-MM-dd").format(startW.getTime());
				weekList.add(startWeek);
				if (startWeek.equals(endWeek)) {
					break;
				}

			}
			for (String week : weekList) {
				List<StateDay> sdLists = stateDayService.getWeekStateDayList(week);
				List<String> sumList = new ArrayList<String>();
				if (!sdLists.isEmpty()) {
					Integer sum = sdLists.size();
					Integer avg = 0;
					for (StateDay sd : sdLists) {
						sumList.add(sd.getlc_mac());
						avg = avg + Integer.parseInt(sd.getlc_nomal_day());
					}
					// List를 Set으로 변경
					Set<String> set = new HashSet<String>(sumList);
					// Set을 List로 변경
					List<String> sumListNew = new ArrayList<String>(set);
					sumDate.put(week, sumListNew.size());
					if (avg != 0) {
						avg = avg / sumListNew.size();
					}
					avgDate.put(week, avg);
				} else if (sdLists.isEmpty()) {
					Integer sum = 0;
					Integer avg = 0;
					sumDate.put(week, sum);
					avgDate.put(week, avg);
				}

			}
			result_json.put("sum", sumDate);
			result_json.put("avg", avgDate);
			result_json.put("todayData", todayData);
			logger.debug(startWeek);
		}
		// 검색유형이 연간일 경우
		if (type.equals("YEAR")) {
			List<String> yearList = new ArrayList<String>();
			Calendar startY = Calendar.getInstance();
			startY.setTime(startDate);
			Calendar endY = Calendar.getInstance();
			endY.setTime(endDate);
			String endYear = new java.text.SimpleDateFormat("yyyy").format(endY.getTime());
			startY.add(Calendar.YEAR, +0);
			String startYear = new java.text.SimpleDateFormat("yyyy").format(startY.getTime());
			yearList.add(startYear);
			while (true) {
				startY.add(Calendar.YEAR, +1);
				startYear = new java.text.SimpleDateFormat("yyyy").format(startY.getTime());
				yearList.add(startYear);
				if (startYear.equals(endYear)) {
					break;
				}

			}
			for (String year : yearList) {
				List<StateDay> sdLists = stateDayService.getYearStateDayList(year);
				List<String> sumList = new ArrayList<String>();
				if (!sdLists.isEmpty()) {
					Integer sum = sdLists.size();
					Integer avg = 0;
					for (StateDay sd : sdLists) {
						sumList.add(sd.getlc_mac());
						avg = avg + Integer.parseInt(sd.getlc_nomal_day());
					}
					// List를 Set으로 변경
					Set<String> set = new HashSet<String>(sumList);
					// Set을 List로 변경
					List<String> sumListNew = new ArrayList<String>(set);
					sumDate.put(year, sumListNew.size());
					if (avg != 0) {
						avg = avg / sumListNew.size();
					}
					avgDate.put(year, avg);
				} else if (sdLists.isEmpty()) {
					Integer sum = 0;
					Integer avg = 0;
					sumDate.put(year, sum);
					avgDate.put(year, avg);
				}

			}
			result_json.put("sum", sumDate);
			result_json.put("avg", avgDate);
			result_json.put("todayData", todayData);
		}
		// 검색유형이 월별일 경우
		if (type.equals("MONTH")) {
			// 한달 전
			List<String> monList = new ArrayList<String>();
			Calendar startM = Calendar.getInstance();
			startM.setTime(startDate);
			Calendar endM = Calendar.getInstance();
			endM.setTime(endDate);
			String endMonth = new java.text.SimpleDateFormat("yyyy-MM").format(endM.getTime());
			startM.add(Calendar.MONTH, +0);
			String startMonth = new java.text.SimpleDateFormat("yyyy-MM").format(startM.getTime());
			monList.add(startMonth);
			while (true) {
				startM.add(Calendar.MONTH, +1);
				startMonth = new java.text.SimpleDateFormat("yyyy-MM").format(startM.getTime());
				monList.add(startMonth);
				if (startMonth.equals(endMonth)) {
					break;
				}

			}
			for (String mon : monList) {
				List<StateDay> sdLists = stateDayService.getMonthStateDayList(mon);
				List<String> sumList = new ArrayList<String>();
				if (!sdLists.isEmpty()) {
					Integer sum = sdLists.size();
					Integer avg = 0;
					for (StateDay sd : sdLists) {
						sumList.add(sd.getlc_mac());
						avg = avg + Integer.parseInt(sd.getlc_nomal_day());
					}
					// List를 Set으로 변경
					Set<String> set = new HashSet<String>(sumList);
					// Set을 List로 변경
					List<String> sumListNew = new ArrayList<String>(set);
					sumDate.put(mon, sumListNew.size());
					if (avg != 0) {
						avg = avg / sumListNew.size();
					}
					avgDate.put(mon, avg);
				} else if (sdLists.isEmpty()) {
					Integer sum = 0;
					Integer avg = 0;
					sumDate.put(mon, sum);
					avgDate.put(mon, avg);
				}

			}
			result_json.put("sum", sumDate);
			result_json.put("avg", avgDate);
			result_json.put("todayData", todayData);
			logger.debug(startMonth);
		}

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
