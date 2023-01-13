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

import kr.co.bbmc.models.srs.NoticeEnroll;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.models.srs.service.NoticeEnrollService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 유고상황 안내설정 컨트롤러
 */
@Controller("srs-notice-setting-controller")
@RequestMapping(value = "/srs/noticesetting")
public class NoticeSettingController {

	private static final Logger logger = LoggerFactory.getLogger(NoticeSettingController.class);

	@Autowired
	private LocalCtrlService loctrlService;

	@Autowired
	private NoticeEnrollService noticeEnrollService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 유고상황 안내설정 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "안내 설정"), });

		return "srs/noticesetting";
	}

	/**
	 * 읽기 액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public @ResponseBody DataSourceResult read(@RequestBody DataSourceRequest request) {
		String type = "setting";
		try {
			return noticeEnrollService.getNoticeEnrollList(request, type);
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

		String notice_name = (String) model.get("notice_name");
		NoticeEnroll notice_content_ob = noticeEnrollService.getNoticeEnroll(notice_name);
		String notice_content = (String) notice_content_ob.getnotice_content();
		String lc_name = (String) model.get("lc_name");
		// 파라미터 검증

		if (Util.isNotValid(notice_name)) {
			throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
		}

		NoticeEnroll noticeEnroll = new NoticeEnroll(lc_name, notice_name, notice_content, session);

		try {
			noticeEnrollService.saveOrUpdate(noticeEnroll);
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

		String notice_name = (String) model.get("notice_name");

		// 파라미터 검증
		if (Util.isNotValid(notice_name) ) {
			throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
		}

		NoticeEnroll target = noticeEnrollService.getNoticeEnroll((int) model.get("id"));
		if (target != null) {

			target.setnotice_name(notice_name);
			target.touchWho(session);

			try {
				noticeEnrollService.saveOrUpdate(target);
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

		List<NoticeEnroll> noticeEnrolls = new ArrayList<NoticeEnroll>();

		for (Object id : objs) {
			NoticeEnroll noticeEnroll = new NoticeEnroll();

			noticeEnroll.setId((int) id);

			noticeEnrolls.add(noticeEnroll);
		}

		try {
			noticeEnrollService.deleteNoticeEnrolls(noticeEnrolls);
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
			return noticeEnrollService.getSelectOption();
		} catch (RuntimeException re) {
			logger.error("read", re);
			throw new ServerOperationForbiddenException("ReadError");
		} catch (Exception e) {
			logger.error("read", e);
			throw new ServerOperationForbiddenException("ReadError");
		}

	}

	/**
	 * 로컬제어기 불러오기 액션
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
