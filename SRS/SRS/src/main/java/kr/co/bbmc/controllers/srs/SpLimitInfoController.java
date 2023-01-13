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
import kr.co.bbmc.models.srs.Member;
import kr.co.bbmc.models.srs.service.MemberService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

/**
 * 제한속도 정보 컨트롤러
 */
@Controller("srs-sp-limit-info-controller")
@RequestMapping(value = "/srs/splimitinfo")
public class SpLimitInfoController {

	private static final Logger logger = LoggerFactory.getLogger(SpLimitInfoController.class);

	@Autowired
	private MemberService memService;

	@Autowired
	private MessageManager msgMgr;

	@Autowired
	private ModelManager modelMgr;

	/**
	 * 제한속도 정보 페이지
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(Model model, Locale locale, HttpSession session, HttpServletRequest request) {

		modelMgr.addMainMenuModel(model, locale, session, request);
		msgMgr.addCommonMessages(model, locale, session, request);

		msgMgr.addViewMessages(model, locale, new Message[] { new Message("pageTitle", "제한속도 정보"), });

		// Device가 PC일 경우에만, 다중 행 선택 설정
		Util.setMultiSelectableIfFromComputer(model, request);

		return "srs/splimitinfo";
	}

	/**
	 * 변경 액션
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody String update(@RequestBody Map<String, Object> model, Locale locale, HttpSession session) {

		String ukid = Util.parseString((String) model.get("ukid"));
		String password = Util.parseString((String) model.get("password"));
		String lastName = Util.parseString((String) model.get("lastName"));
		String firstName = Util.parseString((String) model.get("firstName"));
		String cPhone = Util.parseString((String) model.get("cPhone"));
		String zipCode = Util.parseString((String) model.get("zipCode"));
		String addr1 = Util.parseString((String) model.get("addr1"));
		String addr2 = Util.parseString((String) model.get("addr2"));
		String gender = Util.parseString((String) model.get("gender"));

		Date birthDate = Util.removeTimeOfDate(Util.parseZuluTime((String) model.get("birthDate")));

		if (Util.isNotValid(ukid) || Util.isNotValid(lastName) || Util.isNotValid(firstName) || Util.isNotValid(cPhone)
				|| Util.isNotValid(zipCode) || Util.isNotValid(addr1) || Util.isNotValid(addr2)
				|| Util.isNotValid(gender) || birthDate == null) {
			throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
		}

		if (password.length() == 512) {
			PrivateKey privateKey = GlobalInfo.RSAKeyPair.getPrivate();
			if (privateKey != null) {
				password = Util.decryptRSA(privateKey, password);
			}
		}

		String bankCode = Util.parseString((String) model.get("bankCode"));
		String bankAccountNo = Util.parseString((String) model.get("bankAccountNo"));

		Member target = memService.getMember((int) model.get("id"));
		if (target != null) {
			target.setUkid(ukid);
			target.setLastName(lastName);
			target.setFirstName(firstName);
			target.setcPhone(cPhone);
			target.setZipCode(zipCode);
			target.setAddr1(addr1);
			target.setAddr2(addr2);
			target.setGender(gender);
			target.setBirthDate(birthDate);

			target.setBankCode(bankCode);
			target.setBankAccountNo(bankAccountNo);

			if (Util.isValid(password)) {
				target.setPassword(Util.encrypt(password, target.getSalt()));
			}

			target.touchWho(session);

			try {
				memService.saveOrUpdate(target);
			} catch (DataIntegrityViolationException dive) {
				logger.error("saveOrUpdate", dive);
				throw new ServerOperationForbiddenException("동일한 고객ID의 자료가 이미 등록되어 있습니다.");
			} catch (ConstraintViolationException cve) {
				logger.error("saveOrUpdate", cve);
				throw new ServerOperationForbiddenException("동일한 고객ID의 자료가 이미 등록되어 있습니다.");
			} catch (Exception e) {
				logger.error("saveOrUpdate", e);
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

		List<Member> members = new ArrayList<Member>();

		for (Object id : objs) {
			Member member = new Member();

			member.setId((int) id);

			members.add(member);
		}

		try {
			memService.deleteMembers(members);
		} catch (Exception e) {
			logger.error("destroy", e);
			throw new ServerOperationForbiddenException("DeleteError");
		}

		return "OK";
	}

}
