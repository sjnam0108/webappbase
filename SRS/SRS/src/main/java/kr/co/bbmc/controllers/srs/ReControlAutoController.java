package kr.co.bbmc.controllers.srs;

import java.io.File;
import java.io.FilenameFilter;
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
import kr.co.bbmc.models.srs.NoticeEnroll;
import kr.co.bbmc.models.srs.AutoCtrl;
import kr.co.bbmc.models.srs.service.AutoCtrlService;
import kr.co.bbmc.models.srs.service.LcSrService;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.models.srs.service.MemberService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 적정광도 자동제어 컨트롤러
 */
@Controller("srs-re-control-auto-controller")
@RequestMapping(value = "/srs/recontrolauto")
public class ReControlAutoController {

	private static final Logger logger = LoggerFactory.getLogger(ReControlAutoController.class);

	@Autowired
	private LcSrService lcSrService;

	@Autowired
	private LocalCtrlService loctrlService;

	@Autowired
	private AutoCtrlService autoCtrlService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 적정광도 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "적정광도 제어"), });

		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/recontrolauto";
	}

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public @ResponseBody DataSourceResult readSr(@RequestBody DataSourceRequest request) {

		try {
			return autoCtrlService.getAutoCtrlList(request);
		} catch (RuntimeException re) {
			logger.error("read_re", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read-e", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
	}

	/**
	 * 셀렉트 트리거 액션
	 */
	@RequestMapping(value = "/selectLc", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> readSr(@RequestBody Map<String, String> model) {
		String lc_name = (String) model.get("lc_name");
		String lc_mac = (String) model.get("lc_mac");
		Map<String, String> result = new HashMap<String, String>();
		LocalCtrl check = loctrlService.getLocalCtrl(lc_mac);
		result.put("speed", check.getlc_sp_limit());
		try {
			return result;
		} catch (RuntimeException re) {
			logger.error("read_re", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read-e", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
	}

	/**
	 * 추가 액션
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody String createAutoCtrl(@RequestBody Map<String, String> model, Locale locale,
			HttpSession session) {

		String lc_name = (String) model.get("lc_name");
		String lc_mac = (String) model.get("lc_mac");
		String rain = (String) model.get("rain");
		String speed = (String) model.get("speed");
		String time = (String) model.get("time");
		String ctrl_yLight = (String) model.get("yLight");
		String ctrl_wLight = (String) model.get("wLight");
		String v_ability = (String) model.get("v_ability");
		String ctrl_condition = "0";

		if (speed == "") {
			speed = "0";
		}

		// int lc_id_int = Integer.parseInt(lc_id);
		AutoCtrl autoCtrl = new AutoCtrl(lc_name, lc_mac, session);
		autoCtrl.setctrl_condition(ctrl_condition);
		autoCtrl.setrain(rain);
		autoCtrl.settime(time);
		autoCtrl.setspeed(speed);
		autoCtrl.setv_ability(v_ability);
		autoCtrl.setctrl_yLight(ctrl_yLight);
		autoCtrl.setctrl_wLight(ctrl_wLight);

		try {
			autoCtrlService.saveOrUpdate(autoCtrl);
		} catch (DataIntegrityViolationException dive) {
			logger.error("create", dive);
			throw new ServerOperationForbiddenException("동일한 로컬제어기가 이미 등록되어 있습니다.");
		} catch (ConstraintViolationException cve) {
			logger.error("create", cve);
			throw new ServerOperationForbiddenException("동일한 로컬제어기가 이미 등록되어 있습니다.");
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

		String rain = (String) model.get("rain");
		String speed = (String) model.get("speed");
		String time = (String) model.get("time");
		String ctrl_yLight = (String) model.get("yLight");
		String ctrl_wLight = (String) model.get("wLight");
		String v_ability = (String) model.get("v_ability");

		AutoCtrl target = autoCtrlService.getAutoCtrl((int) model.get("id"));
		if (target != null) {
			target.setrain(rain);
			target.setspeed(speed);
			target.settime(time);
			target.setctrl_yLight(ctrl_yLight);
			target.setctrl_wLight(ctrl_wLight);
			target.setv_ability(v_ability);
			target.touchWho(session);

			try {
				autoCtrlService.saveOrUpdate(target);
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
		}
		return "OK";
	}

	/**
	 * 적정관도제어 진행 / 중지 상태 변경 액션
	 */
	@RequestMapping(value = "/updateCtrlState", method = RequestMethod.POST)
	public @ResponseBody String updatetrlState(@RequestBody Map<String, String> model, Locale locale,
			HttpSession session) {

		String ctrl_condition = (String) model.get("ctrl_condition");
		AutoCtrl target = autoCtrlService.getAutoCtrl(Integer.parseInt(model.get("id")));
		// int lc_id_int = Integer.parseInt(lc_id);
		target.setctrl_condition(ctrl_condition);

		try {
			autoCtrlService.saveOrUpdate(target);
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
	 * 삭제 액션
	 */

	@RequestMapping(value = "/destroy", method = RequestMethod.POST)
	public @ResponseBody String destroy(@RequestBody Map<String, Object> model) {

		@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");

		List<AutoCtrl> autoCtrls = new ArrayList<AutoCtrl>();

		for (Object id : objs) {
			AutoCtrl autoCtrl = new AutoCtrl();

			autoCtrl.setId((int) id);

			autoCtrls.add(autoCtrl);
		}

		try {
			autoCtrlService.deleteAutoCtrls(autoCtrls);
		} catch (Exception e) {
			logger.error("destroy", e);
			throw new ServerOperationForbiddenException("DeleteError");
		}

		return "OK";
	}

	/**
	 * 셀렉트 SR 불러오기 액션
	 */
	@RequestMapping(value = "/selectOpSr", method = RequestMethod.POST)
	public @ResponseBody List selectOptionSr(@RequestBody Map<String, Object> data) {

		try {

			return lcSrService.getLcSrList(data.get("lc_mac").toString());
		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}

	}

}
