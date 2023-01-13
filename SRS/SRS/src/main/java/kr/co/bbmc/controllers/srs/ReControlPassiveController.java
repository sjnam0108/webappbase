package kr.co.bbmc.controllers.srs;

import java.io.File;
import java.io.FilenameFilter;
import java.security.PrivateKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Date;
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
import kr.co.bbmc.models.srs.LcSr;
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.Member;
import kr.co.bbmc.models.srs.service.LcSrService;
import kr.co.bbmc.models.srs.service.MemberService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 수동제어 컨트롤러
 */
@Controller("srs-re-control-passive-controller")
@RequestMapping(value = "/srs/recontrolpassive")
public class ReControlPassiveController {

	private static final Logger logger = LoggerFactory.getLogger(ReControlPassiveController.class);

	@Autowired
	private LcSrService lcSrService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 수동제어 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "수동제어"), });

		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/recontrolpassive";
	}

	/**
	 * 변경 액션
	 */
	@RequestMapping(value = "/updatePower", method = RequestMethod.POST)
	public @ResponseBody String updatePower(@RequestBody Map<String, String> model, Locale locale,
			HttpSession session) {

		String lc_mac = (String) model.get("lc_mac");
		List<LcSr> lcSrs = lcSrService.getLcSrList(lc_mac);
		ArrayList<String> sr_nos = new ArrayList<String>();

		ArrayList<String> sr_ctrl_powers = new ArrayList<String>();
		ArrayList<String> sr_ctrl_lights = new ArrayList<String>();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		model.remove("lc_mac");
		for (String sr_no : model.keySet()) {
			sr_nos.add(sr_no.substring(5, 11));
			ids.add(Integer.parseInt(sr_no.substring(0, 4)));
		}
		for (String sr_val : model.values()) {
			String[] a = sr_val.split("-");
			sr_ctrl_powers.add(a[0]);
			sr_ctrl_lights.add(a[1]);
		}
		Integer i = 0;
		for (LcSr lcSr : lcSrs) {
			i = 0;
			Integer checkId = lcSr.getId();
			lcSr.touchCtrl(session);
			for (Integer id : ids) {
				if (checkId.equals(id)) {
					if (lcSr.getlight_type().equals("0")) {
						lcSr.setsr_ctrl_wLight(sr_ctrl_lights.get(i));
						lcSr.setsr_ctrl_yLight("00000");
					} else if (lcSr.getlight_type().equals("1")) {
						lcSr.setsr_ctrl_yLight(sr_ctrl_lights.get(i));
						lcSr.setsr_ctrl_wLight("00000");
					}
					lcSr.setsr_ctrl_light(sr_ctrl_lights.get(i));
					lcSr.setsr_ctrl_power(sr_ctrl_powers.get(i));
				}
				i = i + 1;
			}
		}

		try {
			lcSrService.saveOrUpdates(lcSrs);
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

}
