package kr.co.bbmc.controllers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import kr.co.bbmc.exceptions.ServerOperationForbiddenException;
import kr.co.bbmc.info.GlobalInfo;
import kr.co.bbmc.models.SrsUserCookie;
import kr.co.bbmc.models.ExcelDownloadView;
import kr.co.bbmc.models.FormRequest;
import kr.co.bbmc.models.LoginUser;
import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;
import kr.co.bbmc.models.fnd.LoginLog;
import kr.co.bbmc.models.fnd.User;
import kr.co.bbmc.models.fnd.service.UserService;
import kr.co.bbmc.utils.Util;

/**
 * 홈 컨트롤러
 */
@Controller("home-controller")
@RequestMapping(value="")
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired 
    private UserService userService;

	@Autowired
	private MessageManager msgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
	/**
	 * 웹 애플리케이션 컨텍스트 홈
	 */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session) {
    	
		String logoutType = Util.parseString(request.getParameter("forcedLogout"));
		if (Util.isValid(logoutType) && logoutType.equals("true")) {
	    	model.addAttribute("forcedLogout", true);
		}

		String appMode = Util.getAppModeFromRequest(request);
		
		if (Util.isValid(appMode)) {
			if (appMode.equals("A")) {
				return "forward:/applogin";
			} else {
				return "forward:/home";
			}
		} else {
			return "forward:/home";
		}
	}
	
	/**
	 * 로그인 페이지
	 */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String toLogin(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session) {
    	msgMgr.addCommonMessages(model, locale, session, request);
    	
    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
   					new Message("pageTitle", "home.title"),
       				new Message("label_username", "home.username"),
       				new Message("label_password", "home.password"),
    				new Message("tip_remember", "home.remember"),
    				new Message("btn_login", "home.login"),
    				new Message("msg_forcedLogout", "home.msg.forcedLogout"),
    			});

    	model.addAttribute("logoPathFile", Util.getLogoPathFile("login", request.getServerName()));
    	
    	Util.prepareKeyRSA(model, session);
    	
		return "home";
	}
	
	/**
	 * FavIcon 액션
	 */
    @RequestMapping(value = "/favicon.ico", method = RequestMethod.GET)
    public void favicon(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		response.sendRedirect("/resources/favicon.ico");
    	} catch (IOException e) {
    		logger.error("favicon", e);
    	}
    }

	/**
	 * 로그인 암호키 확인 액션
	 */
    @RequestMapping(value = "/loginkey", method = RequestMethod.POST)
    public @ResponseBody String checkLoginKey(@RequestBody Map<String, Object> model) {
    	
    	String clientKey = Util.parseString((String)model.get("key"));
    	
    	return (Util.isValid(clientKey) && clientKey.equals(GlobalInfo.RSAKeyMod)) ? "Y" : "N";
    }
    
	/**
	 * 로그인 프로세스
	 */
    private String doLogin(String username, String password, String appMode, HttpSession session, 
    		Locale locale, HttpServletRequest request, HttpServletResponse response) {
    	
    	if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
    		return "home.server.msg.wrongIdPwd";
    	}
    	
    	// RSA 인코딩되었을 때의 처리
    	if (username.length() == 512 && password.length() == 512) {
    		PrivateKey privateKey = (PrivateKey) session.getAttribute("rsaPrivateKey");
    		if (privateKey != null) {
    			username = Util.decryptRSA(privateKey, username);
    			password = Util.decryptRSA(privateKey, password);
    		}
    	}
    	//-
    	
    	User dbUser = userService.getUser(username);
    	if (dbUser == null || !Util.isSameUserPassword(password, dbUser.getSalt(), dbUser.getPassword())) {
    		logger.info("Login Error(WrongIdPwd): {}/{}", username, password);
    		
    		return "home.server.msg.wrongIdPwd";
    	}
    	
    	// 여기까지 오면 패스워드까지 일치
    	if (!userService.isEffectiveUser(dbUser)) {
    		logger.info("Login Error(EffectiveDate): {}/{}", username, password);
    		
    		return "home.server.msg.notEffectiveUser";
    	}
    	
    	LoginLog lastLoginLog = userService.getLastLoginLogByUserId(dbUser.getId());
    	if (lastLoginLog != null) {
        	DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        	
    		session.setAttribute("loginUserLastLoginTime", df.format(lastLoginLog.getWhoCreationDate()));
    	}
    	
    	LoginLog loginLog = new LoginLog(request.getRemoteAddr(), dbUser, session);

    	try {
        	userService.saveOrUpdate(loginLog);
        	
        	LoginUser successLoginUser = new LoginUser(dbUser, loginLog.getId());
        	successLoginUser.setUserSites(userService.getUserSites(dbUser.getId()));
        	
        	session.setAttribute("loginUser", successLoginUser);
    		
        	// [WAB] --------------------------------------------------------------------------
    		/*
        	session.setAttribute("userCookie", new UserCookie(request));
    		*/
        	// [WAB] --------------------------------------------------------------------------
        	// [CashGo] ext ----------------------------------------------------------- start
        	//
        	//
    		
        	SrsUserCookie userCookie = new SrsUserCookie(request);
        	if (Util.isValid(appMode)) {
        		userCookie.setAppMode(appMode);
        	}
        	
        	session.setAttribute("userCookie", userCookie);
        	
        	//
        	//
        	// [CashGo] ext ------------------------------------------------------------- end

        	String cookieKey = "currentSiteId_" + dbUser.getId();
        	String cookieValue = Util.cookieValue(request, cookieKey);
        	
        	if (cookieValue == null) {
        		// LoginUser의 dropdownlist의 첫 값 획득
        		// 있으면 변수에 설정하고 쿠키 저장
        		// 없으면 패스
        		cookieValue = successLoginUser.getFirstSiteIdInUserSites();
        		if (cookieValue != null) {
        			response.addCookie(Util.cookie(cookieKey, cookieValue));
        		}
        	} else {
        		// LoginUser의 dropdownlist의 값 존재 확인
        		// 있으면 패스
        		// 없으면 
        		// LoginUser의 dropdownlist의 첫 값 획득
        		// 있으면 변수에 설정하고 쿠키 저장
        		// 없으면 패스
        		if (!successLoginUser.hasSiteIdInUserSites(cookieValue)) {
            		cookieValue = successLoginUser.getFirstSiteIdInUserSites();
            		if (cookieValue != null) {
            			response.addCookie(Util.cookie(cookieKey, cookieValue));
            		}
        		}
        	}
        	
        	session.setAttribute("currentSiteId", cookieValue);
        	
        	
        	session.removeAttribute("mainMenuLang");
        	session.removeAttribute("mainMenuData");
        	
        	// 세션 무효화 권한 사용자 처리
        	if (Util.hasThisPriv(session, "internal.NoTimeOut")) {
        		session.setMaxInactiveInterval(-1);
        	}
        	
        	// 사용자의 View 설정
        	userService.setUserViews(successLoginUser, cookieValue, null, session, locale);
    	} catch (Exception e) {
    		logger.error("doLogin", e);
    		
    		return "common.server.msg.loginError";
    	}

        return "OK";
    }
    
	/**
	 * 로그인 액션
	 */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody String login(@RequestBody User loginUser, HttpSession session, 
    		Locale locale, HttpServletRequest request, HttpServletResponse response) {
    	
    	String username = loginUser.getUsername();
    	String password = loginUser.getPassword();
    	
    	String result = doLogin(username, password, "", session, locale, request, response);
    	
    	if (result.equals("OK")) {
    		return result;
    	} else {
    		throw new ServerOperationForbiddenException(msgMgr.message(result, locale));
    	}
    }
    
	/**
	 * 앱 로그인 액션
	 */
    @RequestMapping(value = "/applogin", method = RequestMethod.POST)
    public void appLogin(HttpServletRequest request, HttpSession session, 
    		Locale locale, HttpServletResponse response) throws ServletException, IOException {
    	
    	String username = Util.parseString(request.getParameter("username"));
    	String password = Util.parseString(request.getParameter("password"));
    	String appMode = Util.parseString(request.getParameter("appMode"));
    	
    	String result = doLogin(username, password, appMode, session, locale, request, response);
        
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
      		  response.getOutputStream(), "UTF-8"));
        
        response.setHeader("Cache-Control", "no-store");              // HTTP 1.1
        response.setHeader("Pragma", "no-cache, must-revalidate");    // HTTP 1.0
        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        out.print(result);
        out.close();
    }
    
	/**
	 * 로그아웃 액션
	 */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
    	userService.logout(session);
    	
    	return new ModelAndView("redirect:/");
    }
    
	/**
	 * 패스워드 변경 액션
	 */
    @RequestMapping(value = "/passwordupdate", method = RequestMethod.POST)
    public @ResponseBody String updatePassword(@RequestBody FormRequest form, HttpSession session, 
    		Locale locale, HttpServletRequest request) {
    	String currentPwd = form.getCurrentPassword();
    	String newPwd = form.getNewPassword();
    	String confirmPwd = form.getConfirmPassword();
    	
    	LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

    	if (currentPwd == null || currentPwd.isEmpty() || newPwd == null || newPwd.isEmpty() ||
    			confirmPwd == null || confirmPwd.isEmpty() || loginUser == null) {
    		throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
    	}
    	
    	// RSA 인코딩되었을 때의 처리
    	if (currentPwd.length() == 512 && newPwd.length() == 512 && confirmPwd.length() == 512) {
    		PrivateKey privateKey = (PrivateKey) session.getAttribute("rsaPrivateKey");
    		if (privateKey != null) {
    			currentPwd = Util.decryptRSA(privateKey, currentPwd);
    			newPwd = Util.decryptRSA(privateKey, newPwd);
    			confirmPwd = Util.decryptRSA(privateKey, confirmPwd);
    		}
    	}
    	//-
    	
    	if (!newPwd.equals(confirmPwd)) {
    		throw new ServerOperationForbiddenException(msgMgr.message("passwordupdate.msg.samePassword", locale));
    	}
    	
    	User dbUser = userService.getUser(loginUser.getId());
    	if (dbUser == null || !Util.isSameUserPassword(currentPwd, dbUser.getSalt(), dbUser.getPassword())) {
    		throw new ServerOperationForbiddenException(msgMgr.message("common.server.msg.wrongParamError", locale));
    	}
    	
    	// 지금부터 새 패스워드 저장
    	dbUser.setPassword(Util.encrypt(newPwd, dbUser.getSalt()));
        
    	dbUser.touchWho(session);
    	
        try {
            userService.saveOrUpdate(dbUser);
        } catch (Exception e) {
    		logger.error("updatePassword", e);
        	throw new ServerOperationForbiddenException("SaveError");
        }

        return "OK";
    }
	
	/**
	 * 엑셀 다운로드 최종 페이지
	 */
	@RequestMapping(value = "/export", method = RequestMethod.GET)
    public View excelExportFile() {
    	return new ExcelDownloadView();
    }
	
	/**
	 * 사용자의 현재 사이트 변경 액션
	 */
	@RequestMapping(value = "/changesite", method = RequestMethod.GET)
    public ModelAndView changeSite(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session, Locale locale) {
		String userUrl = request.getParameter("uri");
		
		if (userUrl == null || userUrl.isEmpty()) {
			userUrl = "/userhome";
		}

		if (session != null) {
			LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
			if (loginUser != null) {
				String cookieKey = "currentSiteId_" + Util.loginUserId(session);
				String cookieValue = request.getParameter("siteId");

				if (!loginUser.hasSiteIdInUserSites(cookieValue)) {
		    		cookieValue = loginUser.getFirstSiteIdInUserSites();
				}

				response.addCookie(Util.cookie(cookieKey, cookieValue));
				
				session.setAttribute("currentSiteId", cookieValue);
			    

	        	// 사용자의 View 설정
	        	userService.setUserViews(loginUser, cookieValue, null, session, locale);
			}
		}
		
    	return new ModelAndView("redirect:" + userUrl);
    }
	
	/**
	 * 사용자의 현재 뷰 변경 액션
	 */
	@RequestMapping(value = "/changeview", method = RequestMethod.GET)
    public ModelAndView changeView(HttpServletRequest request, HttpServletResponse response, 
    		HttpSession session, Locale locale) {
		String userUrl = request.getParameter("uri");
		
		if (userUrl == null || userUrl.isEmpty()) {
			userUrl = "/userhome";
		}

		if (session != null) {
			LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
			if (loginUser != null) {
				String viewId = request.getParameter("viewId");

				if (!loginUser.hasViewIdInUserViews(viewId)) {
					viewId = loginUser.getFirstViewIdInUserViews();
				}
	        	
	        	// 사용자의 View 설정
	        	userService.setUserViews(loginUser, Util.getSessionSiteId(session) + "", 
	        			viewId, session, locale);
			}
		}
		
    	return new ModelAndView("redirect:" + userUrl);
    }
	
    /**
     * 로컬 파일 저장을 지원하지 않는 브라우저를 위한 프록시 기능 액션
     * 대상 브라우저: IE9 혹은 그 이하, Safari
     */
    
    @RequestMapping(value = "/proxySave", method = RequestMethod.POST)
    public @ResponseBody void save(String fileName, String base64, 
    		String contentType, HttpServletResponse response) throws IOException {

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        response.setContentType(contentType);

        byte[] data = DatatypeConverter.parseBase64Binary(base64);

        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.flushBuffer();
    }
    
    /**
     * 로그인 후 엔트리 페이지로 이동 액션
     */
    @RequestMapping(value = "/userhome", method = RequestMethod.GET)
    public ModelAndView userhome(HttpSession session, Locale locale, HttpServletRequest request) {
    	
    	// getFirstReachableUrl의 로직을 먼저 실행 후 선택 필요
    	String url = modelMgr.getFirstReachableUrl(Util.getAppModeFromRequest(request), locale, session);
    	
    	String tmp = Util.parseString((String)request.getParameter("dst"));
    	if (Util.isValid(tmp)) {
    		url =  tmp;
    	}
		
		if (Util.isValid(url)) {
			return new ModelAndView("redirect:" + url);
		} else {
			return new ModelAndView("redirect:/common/passwordupdate");
		}
    }
	
	/**
	 * 앱 로그인 페이지
	 */
    @RequestMapping(value = "/applogin", method = RequestMethod.GET)
    public String appLogin(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session) {
    	
    	// 하이브리드 앱에서의 접근 코드
    	model.addAttribute("appMode", Util.getAppModeFromRequest(request));
    	
		return "applogin";
	}
	
	//
	// [CashGo] ext -----------------------------------------------------------------
	//
	
	/**
	 * 개인정보보호 정책 페이지
	 */
    @RequestMapping(value = "/privacy", method = RequestMethod.GET)
    public String privacy(HttpServletRequest request, HttpServletResponse response) {

    	return "forward:/resources/cashgo_privacy.html";
    }
	
	/**
	 * 도로명 주소 API 진입 페이지
	 */
    @RequestMapping(value = "/address", method = { RequestMethod.GET, RequestMethod.POST})
    public String address(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session) {
    	
    	// 모바일에서의 접근 여부: 모바일에서의 접근 시 다른 모양이나 형태 지정이 가능토록
    	model.addAttribute("isMobileMode", !Util.isFromComputer(request));
    	
		return "address";
	}
	
	/**
	 * 계정 활성화 진입 페이지 접근
	 */
    @RequestMapping(value = "/welcome", method = { RequestMethod.POST, RequestMethod.GET })
    public String goInitPage(Model model, HttpServletRequest request, 
    		Locale locale, HttpSession session) {
    	
    	String ukid = Util.parseString(request.getParameter("email"));
    	String key = Util.parseString(request.getParameter("key"));
    	
    	model.addAttribute("ukid", ukid);
    	model.addAttribute("key", key);
    	
    	return "welcome";
	}
	
	/**
	 * 계정 활성화 액션
	 */
    @RequestMapping(value = "/activate", method = { RequestMethod.POST, RequestMethod.GET })
    public String activate(Model model, HttpServletRequest request, 
    		Locale locale, HttpSession session) {
    	
    	return "forward:/srs/common/activate";
	}
	
	/**
	 * 휴대폰 본인 인증 진입 페이지
	 */
    @RequestMapping(value = "/kmc", method = { RequestMethod.GET, RequestMethod.POST})
    public String kmc(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session) {
    	
        Calendar today = Calendar.getInstance();
        String day = new SimpleDateFormat("yyyyMMddHHmmss").format(today.getTime());
        
        Random ran = new Random();
        
        //랜덤 문자 길이
        int numLength = 6;
        String randomStr = "";
        for (int i = 0; i < numLength; i++) {
            //0 ~ 9 랜덤 숫자 생성
            randomStr += ran.nextInt(10);
        }

        // <본인인증시 필수 정보>
    	String cpId          = Util.getFileProperty("kmc.cpId"); // 회원사ID - 필수
		// 001004 : http://spring.signcast.co.kr/kmc/kmcmodule.jsp
    	String urlCode       = Util.getFileProperty("kmc.urlCode"); // URL코드 - 필수
    	String certNum       = day + randomStr; // 요청번호 - 최대 40byte 까지 사용 가능 - 필수
    	String date          = day; // 요청일시 - 필수
    	// 인증방법 : 모바일에서는 모바일 인증만 가능(한국모바일인증 전화로 확인)
    	String certMet       = "M"; // 본인인증방법 - 필수(M : 휴대폰 본인확인)
    	String extendVar     = "0000000000000000"; // 확장변수 - 필수
    	
        // <본인인증시 필수가 아닌 정보>
    	String name          = ""; // 성명 - 필수 아님
    	String phoneNo	     = ""; // 휴대폰번호 - 필수 아님
    	String phoneCorp     = ""; // 이동통신사 - 필수 아님
    	String birthDay	     = ""; // 생년월일 - 필수 아님
    	String gender	     = ""; // 성별 - 필수 아님
    	String nation        = ""; // 내외국인 구분 - 필수 아님
    	String plusInfo      = ""; // 추가DATA정보 - 필수 아님
    	
    	String tr_url     = Util.getFileProperty("url.kmcResult");         // 본인인증서비스 결과수신 POPUP URL - 필수
    	String tr_add     = "N";         // IFrame사용여부 - 필수
    	
    	/** certNum 주의사항 **************************************************************************************
    	* 1. 본인인증 결과값 복호화를 위한 키로 활용되므로 중요함.
    	* 2. 본인인증 요청시 중복되지 않게 생성해야함. (예-시퀀스번호)
    	* 3. certNum값은 본인인증 결과값 수신 후 복호화키로 사용함.
    	       tr_url값에 certNum값을 포함해서 전송하고, (tr_url = tr_url + "?certNum=" + certNum;)
    		   tr_url에서 쿼리스트링 형태로 certNum값을 받으면 됨. (certNum = request.getParameter("certNum"); )
    	*
    	***********************************************************************************************************/
    	
        //01. 한국모바일인증(주) 암호화 모듈 선언
	    com.icert.comm.secu.IcertSecuManager seed  = new com.icert.comm.secu.IcertSecuManager();

		//02. 1차 암호화 (tr_cert 데이터변수 조합 후 암호화)
		String tr_cert = cpId +"/"+ urlCode +"/"+ certNum +"/"+ date +"/"+ certMet +"/"+ birthDay +"/"+ gender +"/"+ name +"/"+ phoneNo +"/"+ phoneCorp +"/"+ nation +"/"+ plusInfo +"/"+ extendVar;
		String enc_tr_cert = seed.getEnc(tr_cert, "");

		//03. 1차 암호화 데이터에 대한 위변조 검증값 생성 (HMAC)
		String hmacMsg = "";
		hmacMsg = seed.getMsg(enc_tr_cert);

		//04. 2차 암호화 (1차 암호화 데이터, HMAC 데이터, extendVar 조합 후 암호화)
		tr_cert  = seed.getEnc(enc_tr_cert + "/" + hmacMsg + "/" + extendVar, "");
		
		model.addAttribute("tr_cert", tr_cert);
		model.addAttribute("tr_url", tr_url);
		model.addAttribute("tr_add", tr_add);
		model.addAttribute("returnYn", "N");
		
    	return "kmc";
	}
	
	/**
	 * 휴대폰 본인 인증 결과 페이지
	 */
    @RequestMapping(value = "/kmcResult", method = { RequestMethod.GET, RequestMethod.POST})
    public String kmcResult(Model model, Locale locale, HttpServletRequest request,
    		HttpSession session) {
    	
    	String encPara = "", encMsg1 = "", encMsg2 = "", msgChk = "";
    	String rec_cert = request.getParameter("rec_cert").trim();  // 결과값(암호화)
    	String k_certNum = request.getParameter("certNum").trim();   // certNum
    	// 파라미터 유효성 검증
    	if(rec_cert.length() == 0 || k_certNum.length() == 0){
    		logger.info("결과값 비정상 >>> [{}]", "비정상");
    		model.addAttribute("result", "F");			// 본인인증 결과 값(Y:성공 / N:실패 / F:오류)
    		return "/kmc/kmcmodule";
    	}
    	
        //01. 암호화 모듈 (jar) Loading
        com.icert.comm.secu.IcertSecuManager seed = new com.icert.comm.secu.IcertSecuManager();

        //02. 1차 복호화
        //수신된 certNum를 이용하여 복호화
        rec_cert  = seed.getDec(rec_cert, k_certNum);

        //03. 1차 파싱
        int inf1 = rec_cert.indexOf("/", 0);
        int inf2 = rec_cert.indexOf("/", inf1+1);
		encPara  = rec_cert.substring(0, inf1);         //암호화된 통합 파라미터
        encMsg1  = rec_cert.substring(inf1+1, inf2);    //암호화된 통합 파라미터의 Hash값
        
		//04. 위변조 검증
		encMsg2  = seed.getMsg(encPara);
		
        if(encMsg2.equals(encMsg1)){
            msgChk="Y";
        }
        if(msgChk.equals("N")){
        	logger.info("결과값 비정상 >>> msgChk[{}][{}]", msgChk, "비정상");
        	model.addAttribute("result", "F");			// 본인인증 결과 값(Y:성공 / N:실패 / F:오류)
        	return "/kmc/kmcmodule";
        }

        //05. 2차 복호화
		rec_cert  = seed.getDec(encPara, k_certNum);
    	
        int info1  = rec_cert.indexOf("/", 0);
        int info2  = rec_cert.indexOf("/", info1+1);
        int info3  = rec_cert.indexOf("/", info2+1);
        int info4  = rec_cert.indexOf("/", info3+1);
        int info5  = rec_cert.indexOf("/", info4+1);
        int info6  = rec_cert.indexOf("/", info5+1);
        int info7  = rec_cert.indexOf("/", info6+1);
        int info8  = rec_cert.indexOf("/", info7+1);
		int info9  = rec_cert.indexOf("/", info8+1);
		int info10 = rec_cert.indexOf("/", info9+1);
		int info11 = rec_cert.indexOf("/", info10+1);
		int info12 = rec_cert.indexOf("/", info11+1);
		int info13 = rec_cert.indexOf("/", info12+1);
		int info14 = rec_cert.indexOf("/", info13+1);
		int info15 = rec_cert.indexOf("/", info14+1);
		int info16 = rec_cert.indexOf("/", info15+1);
		int info17 = rec_cert.indexOf("/", info16+1);
		int info18 = rec_cert.indexOf("/", info17+1);

        String certNum = rec_cert.substring(0, info1);
        String date = rec_cert.substring(info1+1, info2);
        String CI = rec_cert.substring(info2+1, info3);
        String phoneNo = rec_cert.substring(info3+1, info4);
        String phoneCorp = rec_cert.substring(info4+1, info5);
        String birthDay = rec_cert.substring(info5+1, info6);
        String gender = rec_cert.substring(info6+1, info7);
        String nation = rec_cert.substring(info7+1, info8);
		String name = rec_cert.substring(info8+1, info9);
		String result = rec_cert.substring(info9+1, info10);
		String certMet = rec_cert.substring(info10+1, info11);
		String ip = rec_cert.substring(info11+1, info12);
		String M_name = rec_cert.substring(info12+1, info13);
		String M_birthDay = rec_cert.substring(info13+1, info14);
		String M_Gender	= rec_cert.substring(info14+1, info15);
		String M_nation	= rec_cert.substring(info15+1, info16);
		String plusInfo	= rec_cert.substring(info16+1, info17);
		String DI = rec_cert.substring(info17+1, info18);
        
        //07. CI, DI 복호화
        CI  = seed.getDec(CI, k_certNum);
        DI  = seed.getDec(DI, k_certNum);
        
		model.addAttribute("returnYn", "Y");
		
		model.addAttribute("certNum", certNum);  		// 서비스 인증번호
		model.addAttribute("date", date);				// 서비스 요청 일시
		model.addAttribute("CI", CI);					// Unique 한 식별정보
		model.addAttribute("phoneNo", phoneNo);			// 휴대폰 번호
		// SKT : SKT 텔레콤 / KTF : KT / LGT : LG U+
		// SKM : SKT mvno / KTM : KT mvno / LGM : LG U+ mvno
		model.addAttribute("phoneCorp", phoneCorp);		// 이동통신사 정보
		model.addAttribute("birthDay", birthDay);		// 생년월일 정보
		model.addAttribute("gender", gender);			// 성별 정보 (0:남자 / 1:여자)
		model.addAttribute("nation", nation);			// 내 .외국인 정보(0:내국인/ 1:외국인)
		model.addAttribute("name", name);				// 성명
		model.addAttribute("result", result);			// 본인인증 결과 값(Y:성공 / N:실패 / F:오류)
		model.addAttribute("certMet", certMet);			// 서비스 방법(M:휴대폰 본인확인)
		model.addAttribute("ip", ip);					// 이용자 IP주소 정보
		model.addAttribute("M_name", M_name);			// 14세 미만 성명 정보
		model.addAttribute("M_birthDay", M_birthDay);	// 14세 미만 생년월일 정보
		model.addAttribute("M_Gender", M_Gender);		// 14세 미만 성별 정보
		model.addAttribute("M_nation", M_nation);		// 14세 미만 내,외국인 정보
		model.addAttribute("plusInfo", plusInfo);		// 요청정보 지원
		model.addAttribute("DI", DI);					// Unique 시별 정보 (웹사이트 기준)
		
        
    	return "kmc";
    }
    	
	
}
