package kr.co.bbmc.controllers.srs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.exception.ConstraintViolationException;
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
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.NoticeEnroll;
import kr.co.bbmc.models.srs.StateDay;
import kr.co.bbmc.models.srs.AutoCtrl;
import kr.co.bbmc.models.srs.StateTime;
import kr.co.bbmc.models.srs.FreezPredic;
import kr.co.bbmc.models.srs.AppSpeedMgr;
import kr.co.bbmc.exceptions.ServerOperationForbiddenException;
import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;

import kr.co.bbmc.models.srs.service.AppSpeedMgrService;
import kr.co.bbmc.models.srs.service.FreezPredicService;
import kr.co.bbmc.models.srs.service.LcSrService;
import kr.co.bbmc.models.srs.service.AutoCtrlService;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.models.srs.service.NoticeEnrollService;
import kr.co.bbmc.models.srs.service.StateDayService;
import kr.co.bbmc.models.srs.service.StateTimeService;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 로컬제어기 컨트롤러
 */
@Controller("srs-localctrl-controller")
@RequestMapping(value = "/srs/localctrl")
public class LocalCtrlController {

	private static final Logger logger = LoggerFactory.getLogger(LocalCtrlController.class);

	@Autowired
	private LcSrService lcSrService;

	@Autowired
	private LocalCtrlService loctrlService;

	@Autowired
	private AutoCtrlService autoCtrlService;

	@Autowired
	private StateTimeService stateTimeService;

	@Autowired
	private StateDayService stateDayService;

	@Autowired
	private NoticeEnrollService noticeEnrollService;

	@Autowired
	private AppSpeedMgrService appSpeedMgrService;

	@Autowired
	private FreezPredicService freezPredicService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 로컬제어기 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "로컬 제어기"), });

		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/localctrl";
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
	 * 읽기 액션
	 */
	@RequestMapping(value = "/readId", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> readId(@RequestBody Map<String, String> model) {
		Integer id = Integer.parseInt(model.get("id"));
		Map<String, String> data = new HashMap<String, String>();
		LocalCtrl lc = loctrlService.getLocalCtrl(id);
		data.put("lat", lc.getlc_gps_lat());
		data.put("lon", lc.getlc_gps_long());
		try {
			return data;
		} catch (RuntimeException re) {
			logger.error("readId", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("readId", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
	}

	/**
	 * 추가 액션
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody String create(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

		String lc_ip = (String) model.get("lc_ip");
		String lc_name = (String) model.get("lc_name");
		String lc_mac = (String) model.get("lc_mac");
		String lc_valid_start = (String) model.get("lc_valid_start");
		String lc_valid_end = (String) model.get("lc_valid_end");
		String lc_area1 = (String) model.get("lc_area1");
		String lc_addr1 = (String) model.get("lc_addr1");
		String lc_gps_lat = (String) model.get("lc_gps_lat");
		String lc_gps_long = (String) model.get("lc_gps_long");
		String lc_manager_tel = (String) model.get("lc_manager_tel");
		String lc_sales_tel = "";
		String lc_memo = (String) model.get("lc_memo");
		String lc_road_name = (String) model.get("lc_road_name");
		String lc_sp_limit = (String) model.get("lc_sp_limit");
		String lc_total_distance = (String) model.get("lc_total_distance");
		String lc_dust = "0";
		String lc_state = "4";
		// 파라미터 검증
		if (lc_mac == "") {
			throw new ServerOperationForbiddenException(msgMgr.message("MAC주소를 입력해 주세요.", locale));
		}
		if (Util.isNotValid(lc_mac)) {
			throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
		}
		LocalCtrl localCtrl = new LocalCtrl(lc_ip, lc_name, lc_mac, lc_valid_start, lc_valid_end, lc_area1, lc_addr1,
				lc_gps_lat, lc_gps_long, lc_manager_tel, lc_sales_tel, lc_memo, lc_state, lc_road_name, lc_sp_limit,
				lc_total_distance, session);
		localCtrl.setlc_dust10(lc_dust);
		localCtrl.setlc_dust25(lc_dust);
		localCtrl.setlc_dust100(lc_dust);
		try {
			loctrlService.saveOrUpdate(localCtrl);
		} catch (DataIntegrityViolationException dive) {
			logger.error("create", dive);
			throw new ServerOperationForbiddenException("동일한 단축명의 자료가 이미 등록되어 있습니다.");
		} catch (ConstraintViolationException cve) {
			logger.error("create", cve);
			throw new ServerOperationForbiddenException("동일한 단축명의 자료가 이미 등록되어 있습니다.");
		} catch (Exception e) {
			logger.error("create", e);
			throw new ServerOperationForbiddenException("SaveError");
		}

		return "OK";
	}

	/**
	 * 변경 액션
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

		String lc_ip = (String) model.get("lc_ip");
		String lc_name = (String) model.get("lc_name");
		String lc_mac = (String) model.get("lc_mac");
		String lc_valid_start = (String) model.get("lc_valid_start");
		String lc_valid_end = (String) model.get("lc_valid_end");
		String lc_area1 = (String) model.get("lc_area1");
		String lc_addr1 = (String) model.get("lc_addr1");
		String lc_gps_lat = (String) model.get("lc_gps_lat");
		String lc_gps_long = (String) model.get("lc_gps_long");
		String lc_manager_tel = (String) model.get("lc_manager_tel");
		String lc_sales_tel = (String) model.get("lc_sales_tel");
		String lc_memo = (String) model.get("lc_memo");
		String lc_road_name = (String) model.get("lc_road_name");
		String lc_sp_limit = (String) model.get("lc_sp_limit");
		String lc_total_distance = (String) model.get("lc_total_distance");
		// 파라미터 검증
		if (Util.isNotValid(lc_name) || Util.isNotValid(lc_mac)) {
			throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
		}

		LocalCtrl target = loctrlService.getLocalCtrl((int) model.get("id"));
		String first_lc_name = target.getlc_name();
		target.setlc_ip(lc_ip);
		target.setlc_name(lc_name);
		target.setlc_mac(lc_mac);
		target.setlc_valid_start(lc_valid_start);
		target.setlc_valid_end(lc_valid_end);
		target.setlc_area1(lc_area1);
		target.setlc_addr1(lc_addr1);
		target.setlc_gps_lat(lc_gps_lat);
		target.setlc_gps_long(lc_gps_long);
		target.setlc_manager_tel(lc_manager_tel);
		target.setlc_sales_tel(lc_sales_tel);
		target.setlc_memo(lc_memo);
		target.setlc_road_name(lc_road_name);
		target.setlc_sp_limit(lc_sp_limit);
		target.setlc_total_distance(lc_total_distance);
		target.touchWho(session);

		List<NoticeEnroll> noticeEnrolls = noticeEnrollService.getNoticeList(first_lc_name.toString());
		List<AutoCtrl> autoCtrls = autoCtrlService.getAutoCtrlList(first_lc_name.toString());
		List<FreezPredic> freezPredics = freezPredicService.getFreezPredicList(first_lc_name.toString());
		List<AppSpeedMgr> appSpeedMgrs = appSpeedMgrService.getAppSpeedMgrList(first_lc_name.toString());
		if (!noticeEnrolls.isEmpty()) {
			for (NoticeEnroll noticeEnroll : noticeEnrolls) {
				noticeEnroll.setlc_name(lc_name);
				noticeEnrollService.saveOrUpdate(noticeEnroll);
			}
		}
		if (!autoCtrls.isEmpty()) {
			for (AutoCtrl autoCtrl : autoCtrls) {
				autoCtrl.setlc_name(lc_name);
				autoCtrlService.saveOrUpdate(autoCtrl);
			}
		}
		if (!freezPredics.isEmpty()) {
			for (FreezPredic freezPredic : freezPredics) {
				freezPredic.setlc_name(lc_name);
				freezPredicService.saveOrUpdate(freezPredic);
			}
		}
		if (!appSpeedMgrs.isEmpty()) {
			for (AppSpeedMgr appSpeedMgr : appSpeedMgrs) {
				appSpeedMgr.setlc_name(lc_name);
				appSpeedMgrService.saveOrUpdate(appSpeedMgr);
			}
		}

		try {
			loctrlService.saveOrUpdate(target);
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

		return "OK";
	}

	/**
	 * 삭제 액션
	 */

	@RequestMapping(value = "/destroy", method = RequestMethod.POST)
	public @ResponseBody String destroy(@RequestBody Map<String, Object> model) {

		@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");
		ArrayList<Object> lc_names = (ArrayList<Object>) model.get("lcName");
		ArrayList<Object> lc_macs = (ArrayList<Object>) model.get("lcMac");

		List<LocalCtrl> localCtrls = new ArrayList<LocalCtrl>();

		/**
		 * 로컬제어기 삭제시 관련데이터도 같이삭제
		 */

		for (Object lc_name : lc_names) {

			List<NoticeEnroll> noticeEnrolls = noticeEnrollService.getNoticeList(lc_name.toString());
			noticeEnrollService.deleteNoticeEnrolls(noticeEnrolls);

			List<AutoCtrl> autoCtrls = autoCtrlService.getAutoCtrlList(lc_name.toString());
			autoCtrlService.deleteAutoCtrls(autoCtrls);

			List<FreezPredic> freezPredics = freezPredicService.getFreezPredicList(lc_name.toString());
			freezPredicService.deleteFreezPredics(freezPredics);

			List<AppSpeedMgr> appSpeedMgrs = appSpeedMgrService.getAppSpeedMgrList(lc_name.toString());
			appSpeedMgrService.deleteAppSpeedMgrs(appSpeedMgrs);
		}

		for (Object lc_mac : lc_macs) {

			List<LcSr> lcSrs = lcSrService.getLcSrList(lc_mac.toString());
			lcSrService.deleteLcSrs(lcSrs);

			List<StateTime> stTimes = stateTimeService.getStateTimeList(lc_mac.toString());
			stateTimeService.deleteStateTimes(stTimes);

			List<StateDay> stDays = stateDayService.getMacStateDayList(lc_mac.toString());
			stateDayService.deleteStateDays(stDays);

		}

		for (Object id : objs) {
			LocalCtrl localCtrl = new LocalCtrl();

			localCtrl.setId((int) id);

			localCtrls.add(localCtrl);
		}

		try {
			loctrlService.deleteLocalCtrls(localCtrls);
		} catch (Exception e) {
			logger.error("destroy", e);
			throw new ServerOperationForbiddenException("DeleteError");
		}

		return "OK";
	}

	/**
	 * 로컬제어기들 상태 읽기 액션
	 */
	@RequestMapping(value = "/countstate", method = RequestMethod.POST)
	public @ResponseBody List countstate(@RequestBody Map<String, Object> model) {

		try {
			return loctrlService.getLocalStatecount();
		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}

	}

}
