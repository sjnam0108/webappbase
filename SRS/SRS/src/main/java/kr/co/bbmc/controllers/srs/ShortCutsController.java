package kr.co.bbmc.controllers.srs;

import java.util.ArrayList;
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
import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;
import kr.co.bbmc.models.srs.service.MemberService;
import kr.co.bbmc.utils.Util;

/**
 * 사용자 홈 컨트롤러
 */
@Controller("srs-short-cuts-controller")
@RequestMapping(value = "/srs/shortcuts")
public class ShortCutsController {

	private static final Logger logger = LoggerFactory.getLogger(ShortCutsController.class);

	@Autowired
	private MemberService memService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 사용자 홈 바로가기 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "사용자 홈"), });

		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/shortcuts";
	}

}
