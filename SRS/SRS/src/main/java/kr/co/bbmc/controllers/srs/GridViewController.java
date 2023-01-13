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

import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.JSONObject;
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

import kr.co.bbmc.models.srs.LcSr;
import kr.co.bbmc.models.srs.StateTime;
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.exceptions.ServerOperationForbiddenException;
import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.models.srs.service.StateTimeService;
import kr.co.bbmc.models.srs.service.LcSrService;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 그리드 뷰 컨트롤러
 */
@Controller("srs-grid-view-controller")
@RequestMapping(value = "/srs/gridview")
public class GridViewController {

	private static final Logger logger = LoggerFactory.getLogger(GridViewController.class);

	@Autowired
	private LcSrService lcSrService;

	@Autowired
	private StateTimeService stateTimeService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 그리드뷰 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "그리드 뷰"), });
		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/gridview";
	}

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/readSr", method = RequestMethod.POST)
	public @ResponseBody DataSourceResult readSr(@RequestBody DataSourceRequest request) {

		try {
			return lcSrService.getLcSrList(request);
		} catch (RuntimeException re) {
			logger.error("readSr", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("readSrr", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
	}

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/readSrGroup", method = RequestMethod.POST)
	public @ResponseBody DataSourceResult readSrGroup(@RequestBody DataSourceRequest request) {
		// DataSourceRequest request = model.get("dataItem");
		String sr_group = (String) request.getData().get("sr_group");
		try {
			return lcSrService.getLcSrList(request, sr_group);
		} catch (RuntimeException re) {
			logger.error("readSr", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("readSrr", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
	}

	/**
	 * 로컬제어기 상세보기 읽기 액션
	 */
	@RequestMapping(value = "/stateTime", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> stateTime(@RequestBody Map<String, String> model) {
		Map<String, String> data = new HashMap<String, String>();

		String lc_mac = model.get("lc_mac");
		String date = model.get("date");
		List<String> time = new ArrayList<String>();
		List<Integer> sums = new ArrayList<Integer>();

		Integer i = 0;
		for (i = 0; i <= 23; i++) {
			if (i < 10) {
				time.add("0" + i.toString());
			} else {
				time.add(i.toString());
			}
		}
		i = 0;
		List<StateTime> stLists = stateTimeService.getStateTimeList(lc_mac, date);
		if (stLists.isEmpty()) {
			for (i = 0; i <= 23; i++) {
				if (i < 10) {
					data.put("0" + i.toString(), "#959595");
				} else {
					data.put(i.toString(), "#959595");
				}
			}

		}
		Integer normalSum = 0;
		Integer partOffSum = 0;
		Integer allOffSum = 0;
		Integer lcOffSum = 0;
		for (StateTime st : stLists) {
			Date time_date = st.getWhoCreationDate();
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH");
			String to = transFormat.format(time_date);
			String hour = to.substring(to.length() - 2, to.length());
			for (String ho : time) {
				if (hour.equals(ho)) {
					if (!st.getlc_nomal_time().equals("0")) {
						data.put(ho, "#3B92DA");
						normalSum = normalSum + Integer.parseInt(st.getlc_nomal_time());
					} else if (!st.getsr_part_off_time().equals("0")) {
						data.put(ho, "#F3C51D");
						partOffSum = partOffSum + Integer.parseInt(st.getsr_part_off_time());
					} else if (!st.getsr_all_off_time().equals("0")) {
						data.put(ho, "#F37C1D");
						allOffSum = allOffSum + Integer.parseInt(st.getsr_all_off_time());
					} else if (!st.getlc_off_time().equals("0")) {
						data.put(ho, "#C70000");
						lcOffSum = lcOffSum + Integer.parseInt(st.getlc_off_time());
					} else if (!st.getno_enroll_time().equals("0")) {
						data.put(ho, "#959595");
					}
				}

			}

		}
		for (String a : time) {
			if (!data.keySet().contains(a)) {
				data.put(a, "#959595");
			}

		}
		sums.add(normalSum);
		sums.add(partOffSum);
		sums.add(allOffSum);
		sums.add(lcOffSum);
		data.put("sums", sums.toString());
		try {
			return data;
		} catch (RuntimeException re) {
			logger.error("readSr", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("readSrr", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
	}

	private static String getTimestampToDate(String timestampStr) {
		long timestamp = Long.parseLong(timestampStr);
		Date date = new java.util.Date(timestamp * 1000L);
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
		String formattedDate = sdf.format(date);
		return formattedDate;
	}

}
