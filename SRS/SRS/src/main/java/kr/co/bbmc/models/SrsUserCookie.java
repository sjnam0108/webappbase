package kr.co.bbmc.models;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.bbmc.utils.Util;

public class SrsUserCookie extends UserCookie {

	private String viewCodeMember = "E";
	private String viewCodeDeal = "G";
	
	public SrsUserCookie() {}
	
	public SrsUserCookie(HttpServletRequest request) {
		
		super(request);
		
		setViewCodeMember(Util.cookieValue(request, "viewCodeMember"));
		setViewCodeDeal(Util.cookieValue(request, "viewCodeDeal"));
	}

	public String getViewCodeMember() {
		
		return viewCodeMember;
	}

	public void setViewCodeMember(String viewCodeMember) {
		
		if (Util.isValid(viewCodeMember)) {
			this.viewCodeMember = viewCodeMember;
		}
	}

	public void setViewCodeMember(String viewCodeMember, HttpServletResponse response) {
		
		if (Util.isValid(viewCodeMember)) {
			this.viewCodeMember = viewCodeMember;
			response.addCookie(Util.cookie("viewCodeMember", viewCodeMember));
		}
	}

	
	public String getViewCodeDeal() {

		return viewCodeDeal;
	}

	public void setViewCodeDeal(String viewCodeDeal) {
		
		if (Util.isValid(viewCodeDeal)) {
			this.viewCodeDeal = viewCodeDeal;
		}
	}

	public void setViewCodeDeal(String viewCodeDeal, HttpServletResponse response) {
		
		if (Util.isValid(viewCodeDeal)) {
			this.viewCodeDeal = viewCodeDeal;
			response.addCookie(Util.cookie("viewCodeDeal", viewCodeDeal));
		}
	}
}
