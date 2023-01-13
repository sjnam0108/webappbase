package kr.co.bbmc.controllers.srs;

import java.util.ArrayList;
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

import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.Member;
import kr.co.bbmc.exceptions.ServerOperationForbiddenException;
import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;
import kr.co.bbmc.models.srs.service.LocalCtrlService;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 지도뷰 컨트롤러
 */
@Controller("srs-mapview-controller")
@RequestMapping(value = "/srs/mapview")
public class MapViewController {

	private static final Logger logger = LoggerFactory.getLogger(MapViewController.class);

	@Autowired
	private LocalCtrlService loctrlService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 지도뷰 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "지도 뷰"), });

		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/mapview";
	}

	/**
	 * 읽기액션
	 */
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public @ResponseBody List countstate(@RequestBody Map<String, Object> localCtrl) {

		try {
			localCtrl.put("state", "0");
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
