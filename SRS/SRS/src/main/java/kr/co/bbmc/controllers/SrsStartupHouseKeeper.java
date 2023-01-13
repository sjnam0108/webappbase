package kr.co.bbmc.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import kr.co.bbmc.info.SrsGlobalInfo;
import kr.co.bbmc.info.GlobalInfo;
import kr.co.bbmc.models.srs.Member;
import kr.co.bbmc.models.srs.service.MemberService;
import kr.co.bbmc.utils.SolUtil;
import kr.co.bbmc.utils.Util;
import kr.co.bbmc.viewmodels.DropDownListItem;

@Component
public class SrsStartupHouseKeeper implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(SrsStartupHouseKeeper.class);
    

    @Autowired 
    private MemberService memService;

	private static Timer bgEmailTimer;
	private static Timer bgAITestTimer;
	

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		String appId = event.getApplicationContext().getId();
		logger.info("Enter onApplicationEvent() - id=" + appId);
		
		if (!appId.equals("org.springframework.web.context.WebApplicationContext:/" + GlobalInfo.AppId)) {
			return;
		}
		
		//boolean bgMainServerMode = Util.getBooleanFileProperty("backgroundJob.mainServerMode");
		
		//
		// 역할: 사이트 신규 회원에게 활성화 링크 메일 발송
		// 주기: 구동 1분 후부터 30 초 단위
		// 
		if (bgEmailTimer == null) {
			bgEmailTimer = new Timer();
			bgEmailTimer.scheduleAtFixedRate(new TimerTask() {
		    	   public void run()
		 	       {
		    		   doEmailJob();
			       }
		        }, 1 * (60 * 1000), 30 * (1000));
		}
		
		//
		// 역할: AI 서버 테스트를 위한 감정 결과 등록
		// 주기: 구동 1분 후부터 3 초 단위
		// 
		if (bgAITestTimer == null) {
			bgAITestTimer = new Timer();
			bgAITestTimer.scheduleAtFixedRate(new TimerTask() {
		    	   public void run()
		 	       {
		    		   doRegisterAppraisalResult();
			       }
		        }, 1 * (60 * 1000), 3 * (1000));
		}
	}
	
	public void doEmailJob() {
		
		if (SrsGlobalInfo.WelcomeMembers.size() > 0) {
			List<Member> copyList = new ArrayList<Member>();
			
			copyList.addAll(SrsGlobalInfo.WelcomeMembers);
			SrsGlobalInfo.WelcomeMembers.clear();
			
			logger.info("detect " + copyList.size() + " new members...");
			for (Member member : copyList) {
				if (!SolUtil.sendWelcomeMail(member.getUkid(), member.getApiToken())) {
					logger.error("doEmailJob - sendWelcomeMail");
					
					// 메일 전송 오류 발생 시의 처리 변경
					//SrsGlobalInfo.WelcomeMembers.add(member);
					try {
				    	member.setStatus("F");
				    	memService.saveOrUpdate(member);
					} catch (Exception e) {
			    		logger.error("doEmailJob", e);
			    		SrsGlobalInfo.WelcomeMembers.add(member);
					}
					//-
				} else {
					logger.info("[Mail] welcome mail sent: " + member.getUkid());
				}
			}
		}
	}
    
	public void doRegisterAppraisalResult() {
	
		if (SrsGlobalInfo.ApprResultQueue.size() > 0) {
			List<DropDownListItem> copyList = new ArrayList<DropDownListItem>();
			
			copyList.addAll(SrsGlobalInfo.ApprResultQueue);
			SrsGlobalInfo.ApprResultQueue.clear();
			
			logger.info("detect " + copyList.size() + " new appraisal requests...");
			for (DropDownListItem item : copyList) {
				String server = Util.getFileProperty("url.currentServer");
				int res = Util.sendStreamToServer(server + "/api/appraisal/update",
						"application/json; charset=UTF-8",
						"{ \"id\": " + item.getText() + ", \"result\": \"" + item.getValue() + "\" }");
				logger.info("appraisal id: " + item.getText() + ", result: " + item.getValue() + ", response: " + res);
			}
		}
	}

}
