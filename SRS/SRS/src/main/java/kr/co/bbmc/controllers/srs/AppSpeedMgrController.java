package kr.co.bbmc.controllers.srs;

import java.util.ArrayList;
import java.util.Collections;
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

import kr.co.bbmc.exceptions.ServerOperationForbiddenException;
import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;

import kr.co.bbmc.models.srs.AppSpeedMgr;
import kr.co.bbmc.models.srs.AutoCtrl;
import kr.co.bbmc.models.srs.NoticeEnroll;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.models.srs.service.AppSpeedMgrService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 적정속도관리 컨트롤러
 */
@Controller("srs-app-speed-mgr-controller")
@RequestMapping(value = "/srs/appspeedmgr")
public class AppSpeedMgrController {

	private static final Logger logger = LoggerFactory.getLogger(AppSpeedMgrController.class);

	@Autowired
	private LocalCtrlService loctrlService;

	@Autowired
	private AppSpeedMgrService appSpeedMgrService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 적정속도관리 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "적정속도 관리"), });

		return "srs/appspeedmgr";
	}

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request) {
		try {
			return appSpeedMgrService.getAppSpeedMgrList(request);
		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}
	}

	/**
	 * 추가 액션
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody String create(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

		String lc_name = (String) model.get("lc_name");
		String lc_mac = (String) model.get("lc_mac");
		String v_ability = (String) model.get("v_ability");
		String limit_speed = (String) model.get("limit_speed");
		String de_section = (String) model.get("de_section");
		String speed = (String) model.get("speed");

		if (limit_speed == "") {
			limit_speed = "0";
		}
		if (speed == "") {
			speed = "0";
		}
		if (v_ability == "") {
			v_ability = "0";
		}
		// int lc_id_int = Integer.parseInt(lc_id);
		AppSpeedMgr appSpeedMgr = new AppSpeedMgr(lc_name, lc_mac, v_ability, limit_speed, de_section, speed, session);

		try {
			appSpeedMgrService.saveOrUpdate(appSpeedMgr);
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

		int id = Integer.parseInt((String) model.get("id"));
		String v_ability = (String) model.get("v_ability");
		String limit_speed = (String) model.get("limit_speed");
		String de_section = (String) model.get("de_section");
		String speed = (String) model.get("speed");

		AppSpeedMgr target = appSpeedMgrService.getAppSpeedMgr(id);
		if (target != null) {

			target.setv_ability(v_ability);
			target.setlimit_speed(limit_speed);
			target.setde_section(de_section);
			target.setspeed(speed);
			target.touchWho(session);

			try {
				appSpeedMgrService.saveOrUpdate(target);
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
	 * 삭제 액션
	 */

	@RequestMapping(value = "/destroy", method = RequestMethod.POST)
	public @ResponseBody String destroy(@RequestBody Map<String, Object> model) {

		@SuppressWarnings("unchecked")
		ArrayList<Object> objs = (ArrayList<Object>) model.get("items");

		List<AppSpeedMgr> appSpeedMgrs = new ArrayList<AppSpeedMgr>();

		for (Object id : objs) {
			AppSpeedMgr appSpeedMgr = new AppSpeedMgr();

			appSpeedMgr.setId((int) id);

			appSpeedMgrs.add(appSpeedMgr);
		}

		try {
			appSpeedMgrService.deleteAppSpeedMgrs(appSpeedMgrs);
		} catch (Exception e) {
			logger.error("destroy", e);
			throw new ServerOperationForbiddenException("DeleteError");
		}

		return "OK";
	}

	/**
	 * 셀렉트 트리거 액션
	 */
	@RequestMapping(value = "/selectOp", method = RequestMethod.POST)
	public @ResponseBody List selectOption(@RequestBody Map<String, Object> data) {

		try {
			return appSpeedMgrService.getSelectOption();
		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}

	}

	/**
	 * 셀렉트 로컬제어기 불러오기 액션
	 */
	@RequestMapping(value = "/selectOpLc", method = RequestMethod.POST)
	public @ResponseBody List selectOptionLc(@RequestBody Map<String, Object> data) {

		try {
			return loctrlService.getLocalCtrlList();
		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}

	}

}
