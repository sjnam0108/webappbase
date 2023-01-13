package kr.co.bbmc.controllers.srs;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.bbmc.models.srs.Member;
import kr.co.bbmc.models.srs.service.MemberService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;

/**
 * srs 공통 컨트롤러
 */
@Controller("srs-common-controller")
@RequestMapping(value = "/srs/common")
public class SrsCommonController {

	private static final Logger logger = LoggerFactory.getLogger(SrsCommonController.class);

	@Autowired
	private MemberService memService;

	/**
	 * 계정 활성화 액션
	 */
	@RequestMapping(value = "/activate", method = { RequestMethod.POST, RequestMethod.GET })
	public String activateAccount(Model model, HttpServletRequest request, Locale locale, HttpSession session) {

		String ukid = Util.parseString(request.getParameter("email"));
		String key = Util.parseString(request.getParameter("key"));

		String result = "OK";
		String msg = "계정이 활성화되었습니다.";

		if (Util.isNotValid(ukid) || Util.isNotValid(key) || key.length() != 9) {
			result = "WrongParam";
			msg = "잘못된 값이 전달되었습니다.";
		} else {
			Member member = memService.getMember(ukid);
			if (member == null) {
				result = "NoMember";
				msg = "계정 정보가 등록되지 않았습니다.";
			} else {
				String apiToken = member.getApiToken();
				if (Util.isNotValid(apiToken) || apiToken.length() == 10 || !member.getStatus().equals("M")) {
					result = "ActivatedBefore";
					msg = "이미 활성화가 완료된 계정입니다.";
				} else if (key.equals(apiToken)) {
					Date allowedTime = Util.addSeconds(member.getWhoLastUpdateDate(), 10);
					if (new Date().after(allowedTime)) {
						try {
							member.setApiToken("welcome");
							member.setStatus("A");

							memService.saveOrUpdate(member);
						} catch (Exception e) {
							logger.error("activateAccount", e);

							result = "Error";
							msg = "작업 중 오류가 발생되었습니다.";
						}
					} else {
						result = "NotManualAccess";
						msg = "잠시 후 다시 시도해 주시기 바랍니다.";
					}
				} else {
					result = "WrongKey";
					msg = "잘못된 키값입니다.";
				}
			}
		}

		logger.info("");
		logger.info("[Activation] ukid: " + ukid + ", key: " + key + ", result: " + result);
		logger.info("");

		model.addAttribute("result", result);
		model.addAttribute("msg", msg);

		return "activated";
	}

	/**
	 * 모듈 전용 파일 업로드 저장 액션
	 */
	@RequestMapping(value = "/uploadsave", method = RequestMethod.POST)
	public @ResponseBody String save(@RequestParam List<MultipartFile> files, @RequestParam String type,
			HttpSession session) {
		try {
			if (Util.isValid(type)) {
				if (type.equals("BRAND")) { // 브랜드별 기준 금액: wizard
					String typeRootDir = SolUtil.getPhysicalRoot("UpTemp");

					for (MultipartFile file : files) {
						if (!file.isEmpty()) {
							File uploadedFile = new File(typeRootDir + "/" + file.getOriginalFilename());
							Util.checkParentDirectory(uploadedFile.getAbsolutePath());

							FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadedFile));
						}
					}
				} else if (type.equals("PRODPIC")) {
					String typeRootDir = SolUtil.getPhysicalRoot("ProdPic");

					for (MultipartFile file : files) {
						if (!file.isEmpty()) {
							File uploadedFile = new File(typeRootDir + "/" + file.getOriginalFilename());
							Util.checkParentDirectory(uploadedFile.getAbsolutePath());

							FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadedFile));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("uploadsave", e);
		}

		// Return an empty string to signify success
		return "";
	}

}
