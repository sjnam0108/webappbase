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

import kr.co.bbmc.models.srs.FreezPredic;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.models.srs.service.FreezPredicService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 결빙예측 컨트롤러
 */
@Controller("srs-freez-predic-controller")
@RequestMapping(value = "/srs/freezpredic")
public class FreezPredicController {

	private static final Logger logger = LoggerFactory.getLogger(FreezPredicController.class);

	@Autowired
	private LocalCtrlService loctrlService;

	@Autowired
	private FreezPredicService freezPredicService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 결빙예측 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "결빙예측"), });

		return "srs/freezpredic";
	}

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request) {
		try {
			return freezPredicService.getFreezPredicList(request);
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
		String air_temp = (String) model.get("air_temp");
		String air_humid = (String) model.get("air_humid");
		String rain = (String) model.get("rain");
		String snow = (String) model.get("snow");
		String win_speed = (String) model.get("winter_speed");
		String road_temp = (String) model.get("road_temp");
		String freez_predic = (String) model.get("freez_predic");

		if (air_temp == "") {
			air_temp = "0";
		}
		if (air_humid == "") {
			air_humid = "0";
		}
		if (rain == "") {
			rain = "0";
		}
		if (snow == "") {
			snow = "0";
		}
		if (win_speed == "") {
			win_speed = "0";
		}
		if (road_temp == "") {
			road_temp = "0";
		}
		// int lc_id_int = Integer.parseInt(lc_id);
		FreezPredic freezPredic = new FreezPredic(lc_name, lc_mac, air_temp, air_humid, rain, snow, win_speed,
				road_temp, freez_predic, session);

		try {
			freezPredicService.saveOrUpdate(freezPredic);
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
		String air_temp = (String) model.get("air_temp");
		String air_humid = (String) model.get("air_humid");
		String rain = (String) model.get("rain");
		String snow = (String) model.get("snow");
		String win_speed = (String) model.get("winter_speed");
		String road_temp = (String) model.get("road_temp");
		String freez_predic = (String) model.get("freez_predic");

		FreezPredic target = freezPredicService.getFreezPredic(id);
		if (target != null) {

			target.setair_temp(air_temp);
			target.setair_humid(air_humid);
			target.setrain(rain);
			target.setsnow(snow);
			target.setwin_speed(win_speed);
			target.setroad_temp(road_temp);
			target.setfreez_predic(freez_predic);
			target.touchWho(session);

			try {
				freezPredicService.saveOrUpdate(target);
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

		List<FreezPredic> freezPredics = new ArrayList<FreezPredic>();

		for (Object id : objs) {
			FreezPredic freezPredic = new FreezPredic();

			freezPredic.setId((int) id);

			freezPredics.add(freezPredic);
		}

		try {
			freezPredicService.deleteFreezPredics(freezPredics);
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
			return freezPredicService.getSelectOption();
		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}

	}

	/**
	 * 셀렉트 로컬제어기값 호출 액션
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
