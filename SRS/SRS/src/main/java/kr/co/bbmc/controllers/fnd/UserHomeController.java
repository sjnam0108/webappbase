package kr.co.bbmc.controllers.fnd;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.bbmc.models.Message;
import kr.co.bbmc.models.MessageManager;
import kr.co.bbmc.models.ModelManager;
import kr.co.bbmc.utils.Util;

/**
 * 사용자 홈 컨트롤러
 */
@Controller("fnd-user-home-controller")
@RequestMapping(value="/fnd/userhome")
public class UserHomeController {

    @Autowired
	private MessageManager msgMgr;
    
	@Autowired
	private ModelManager modelMgr;
	
	/**
	 * 사용자 홈 페이지
	 */
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index(Model model, Locale locale, HttpSession session,
    		HttpServletRequest request) {
    	modelMgr.addMainMenuModel(model, locale, session, request);
    	msgMgr.addCommonMessages(model, locale, session, request);

    	msgMgr.addViewMessages(model, locale,
    			new Message[] {
					new Message("pageTitle", "userhome.title"),
    			});
    	
    	String lastLogin = (String) session.getAttribute("loginUserLastLoginTime");
    	if (Util.isValid(lastLogin)) {
    		session.removeAttribute("loginUserLastLoginTime");
    		
        	model.addAttribute("LastLogin", msgMgr.message("userhome.lastLogin", locale).replace("{0}", lastLogin));
    	}
    	
        return "fnd/userhome";
    }
}
