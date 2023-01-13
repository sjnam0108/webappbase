package kr.co.bbmc.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.bbmc.info.SrsGlobalInfo;
import kr.co.bbmc.models.srs.Member;

import kr.co.bbmc.models.srs.service.SrsService;
import kr.co.bbmc.models.srs.service.MemberService;
import kr.co.bbmc.viewmodels.DropDownListItem;


@Component
public class SolUtil {
	private static final Logger logger = LoggerFactory.getLogger(SolUtil.class);

	/**
	 * 사이트 설정 String 값의 동일 여부 반환(session 값으로)
	 */
	public static boolean propEqVal(HttpSession session, String code, String value) {
		String tmp = getProperty(session, code);
		
		return (Util.isValid(tmp) && tmp.equals(value));
	}

	/**
	 * 사이트 설정 String 값의 동일 여부 반환(사이트 번호로)
	 */
	public static boolean propEqVal(int siteId, String code, String value) {
		String tmp = getProperty(siteId, code);
		
		return (Util.isValid(tmp) && tmp.equals(value));
	}
	
	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(HttpSession session, String code) {
		return getProperty(session, code, null, "");
	}

	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(HttpSession session, String code, Locale locale) {
		return getProperty(session, code, locale, "");
	}

	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(HttpSession session, String code, Locale locale, 
			String defaultValue) {
		String value = getPropertyValue(Util.getSessionSiteId(session), code, locale);
		
		return Util.isValid(value) ? value : defaultValue;
	}
	
	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(int siteId, String code) {
		return getProperty(siteId, code, null, "");
	}

	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(int siteId, String code, Locale locale) {
		return getProperty(siteId, code, locale, "");
	}

	/**
	 * 사이트 설정 String 값 획득
	 */
	public static String getProperty(int siteId, String code, Locale locale, 
			String defaultValue) {
		String value = getPropertyValue(siteId, code, locale);
		
		return Util.isValid(value) ? value : defaultValue;
	}
	
	private static String getPropertyValue(int siteId, String code, Locale locale) {
		
    	// [WAB] --------------------------------------------------------------------------
		if (Util.isValid(code) && 
				(code.equals("logo.title") || code.equals("quicklink.max.menu"))) {
			return Util.getFileProperty(code);
		} else {
			return "";
		}
    	// [WAB] --------------------------------------------------------------------------
	}
	
	//
	// [CashGo] ext -----------------------------------------------------------------
	//
	
	@Autowired
	public void setStaticSrsService(SrsService srsService) {
		SolUtil.sSrsService = srsService;
	}
	
	@Autowired
	public void setStaticMemberService(MemberService memService) {
		SolUtil.sMemService = memService;
	}
	

	
	static SrsService sSrsService;
	static MemberService sMemService;

	
	/**
	 * API 코드에 대응하는 메시지 획득
	 */
	public static String getApiMsg(Integer code) {
		
		if (code == null) {
			return "WrongCode";
		}
		
		if (code == 0) { return "OK"; }
		else if (code == -1) { return "WrongParam"; }
		else if (code == -2) { return "UnexpectedError"; }
		else if (code == -3) { return "FileCopyError"; }
		else if (code == -4) { return "TooLongData"; }
		else if (code == -5) { return "WrongFileFormat"; }
		else if (code == -6) { return "NoLongerUsed"; }
		
		if (code == -101) { return "WrongBirthDate"; }
		else if (code == -102) { return "WrongMemberPhoto"; }
		else if (code == -103) { return "SameUkid"; }
		else if (code == -104) { return "NoPublicKey"; }
		else if (code == -105) { return "WrongPhoneNum"; }
		else if (code == -106) { return "WrongEmail"; }
		else if (code == -107) { return "NoMember"; }
		else if (code == -108) { return "UnactivatedAccount"; }
		else if (code == -109) { return "DormantAccount"; }
		else if (code == -110) { return "WrongPassword"; }
		else if (code == -111) { return "WrongToken"; }
		else if (code == -112) { return "WrongBank"; }
		else if (code == -113) { return "NoFcmToken"; }
		else if (code == -114) { return "NotMemberFcm"; }
		else if (code == -115) { return "NotActiveMember"; }
		else if (code == -116) { return "NotFinishedDeal"; }
		else if (code == -117) { return "WrongPlusStatus"; }
		
		if (code == -201) { return "VerNotReady"; }
		else if (code == -202) { return "NoProd"; }
		
		if (code == -301) { return "NoVerFile"; }
		else if (code == -302) { return "WrongCatBrand"; }
		else if (code == -303) { return "WrongPrice"; }
		else if (code == -304) { return "NoAppraisal"; }
		else if (code == -305) { return "WrongAppraisal"; }
		else if (code == -306) { return "WrongItem"; }
		else if (code == -307) { return "WrongAiCode"; }
		
		if (code == -401) { return "WrongContractDate"; }
		else if (code == -402) { return "WrongEndDate"; }
		else if (code == -403) { return "NoDeal"; }
		else if (code == -404) { return "WrongDealStatus"; }
		else if (code == -405) { return "WrongDeal"; }
		else if (code == -406) { return "NotLoan"; }
		else if (code == -407) { return "WrongBalance"; }
		else if (code == -408) { return "NotSales"; }
		
		return "CodeNotFound";
	}

	/**
	 * 물리적인 루트 디렉토리 획득
	 */
	public static String getPhysicalRoot(String ukid) {
		if (Util.isNotValid(ukid)) {
			return null;
		}
		
		String rootDirPath = Util.getFileProperty("dir.rootPath");
		//String ftpDirName = Util.getFileProperty("dir.ftp");
		
		if (ukid.equals("ApprTest")) {
			return Util.getValidRootDir(rootDirPath) + "apprUpload";
		} else if (ukid.equals("ApprPrivate")) {
            return Util.getValidRootDir(rootDirPath) + "apprPrivate";
		} else if (ukid.equals("ApprRealfake")) {
            return Util.getValidRootDir(rootDirPath) + "apprRealfake";
		} else if (ukid.equals("Base")) {
            return Util.getValidRootDir(rootDirPath) + "base";
		} else if (ukid.equals("Contract")) {
            return Util.getValidRootDir(rootDirPath) + "contracts";
		} else if (ukid.equals("Deal")) {
            return Util.getValidRootDir(rootDirPath) + "deal";
		} else if (ukid.equals("Loan")) {
            return Util.getValidRootDir(rootDirPath) + "loans";
		} else if (ukid.equals("Member")) {
			return Util.getValidRootDir(rootDirPath) + "members";
		} else if (ukid.equals("Plus")) {
            return Util.getValidRootDir(rootDirPath) + "plusUpload";
		} else if (ukid.equals("ProdOpt")) {
            return Util.getValidRootDir(rootDirPath) + "prodopts";
		} else if (ukid.equals("ProdPic")) {
            return Util.getValidRootDir(rootDirPath) + "prodpics";
		} else if (ukid.equals("UpTemp")) {
            return Util.getValidRootDir(rootDirPath) + "uptemp";
		} else if (ukid.equals("Recording")) {
            return Util.getValidRootDir(rootDirPath) + "recording";
		}

        return Util.getPhysicalRoot(ukid);
	}
	
	/**
	 * 웹 접근 혹은 상대경로 접근을 위한 루트 디렉토리 획득
	 */
	public static String getUrlRoot(String ukid) {
		
		if (Util.isNotValid(ukid)) {
			return "";
		}
		
		if (ukid.equals("ApprTest")) {
			return "/apprUpload";
		} else if (ukid.equals("ApprPrivate")) {
            return "/apprPrivate";
		} else if (ukid.equals("ApprRealfake")) {
            return "/apprRealfake";
		} else if (ukid.equals("Base")) {
            return "/base";
		} else if (ukid.equals("Contract")) {
            return "/contracts";
		} else if (ukid.equals("Deal")) {
            return "/deal";
		} else if (ukid.equals("Loan")) {
            return "/loans";
		} else if (ukid.equals("Member")) {
			return "/members";
		} else if (ukid.equals("Plus")) {
            return "/plusUpload";
		} else if (ukid.equals("ProdOpt")) {
            return "/prodopts";
		} else if (ukid.equals("ProdPic")) {
            return "/prodpics";
		} else if (ukid.equals("UpTemp")) {
            return "/uptemp";
		} else if (ukid.equals("Recording")) {
            return "/recording";
		}

        return "";
	}
	


	
	/**
	 * 기준값의 퍼센티지 하단 금액 획득
	 */
	public static int getPctFloorPrice(int v, int pct, int depre, int m) {
		
		return getPctFloorPrice(v, pct, depre, m, 1d, 1d);
	}
	
	/**
	 * 기준값의 퍼센티지 하단 금액 획득
	 */
	public static int getPctFloorPrice(int v, int pct, int depre, int m, double factor1, double factor2) {
		
		double depreciationrate =  (100 - depre) * 0.01;
		int pow = (int) Math.pow(10, m);

		logger.info("[SolUtil] v: " + v);
		logger.info("[SolUtil] pct: " + pct);
		logger.info("[SolUtil] depreciationrate: " + depreciationrate);
		logger.info("[SolUtil] pow: " + pow);
		logger.info("[SolUtil] factor1: " + factor1);
		logger.info("[SolUtil] factor2: " + factor2);
		logger.info("[SolUtil] getPctFloorPrice: "+
				(int)((double)v * (double) pct * depreciationrate * factor1 * factor2 / 100d / (double)pow) * pow);
		
		return (int)((double)v * (double) pct * depreciationrate * factor1 * factor2 / 100d / (double)pow) * pow;
	}
	
	/**
	 * 연체일을 획득
	 */
	public static int getOverdueDays(Date dueDate) {
		
		return getOverdueDays(dueDate, new Date());
	}
	
	/**
	 * 연체일을 획득
	 */
	public static int getOverdueDays(Date dueDate, Date referenceDate) {
		
		if (dueDate == null || referenceDate == null) {
			return 0;
		}
		
		Date realDueDate = Util.removeTimeOfDate(dueDate);
		Date realRefDate = Util.removeTimeOfDate(referenceDate);
		
		if (!realDueDate.before(realRefDate)) {
			return 0;
		}
		
		long diff = realRefDate.getTime() - realDueDate.getTime();
		
		TimeUnit timeUnit = TimeUnit.DAYS;
		return (int)timeUnit.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 은행 이름을 획득
	 */
	public static String getBankName(String code) {
		
		if (Util.isNotValid(code)) {
			return "";
		}
		
		switch (code) {
		case "081" : return "KEB하나은행";
		case "023" : return "SC제일은행";
		case "039" : return "경남은행";
		case "034" : return "광주은행";
		case "004" : return "국민은행";
		case "003" : return "기업은행";
		case "011" : return "농협";
		case "031" : return "대구은행";
		case "032" : return "부산은행";
		case "002" : return "산업은행";
		case "007" : return "수협";
		case "088" : return "신한은행";
		case "027" : return "씨티은행";
		case "020" : return "우리은행";
		case "037" : return "전북은행";
		case "035" : return "제주은행";
		}

		return "";
	}
	
	/**
	 * 대출 조기 상환금 총액 획득(최소 30일 이자 포함)
	 */
	public static int getRestLoanPlusInterest(int loanBalance, float pctRate, Date lastPayDate) {
		
		int sum = loanBalance;
		
		if (lastPayDate == null) {
			return sum;
		}
		
		Date startDate = Util.addDays(lastPayDate, 1);
		
		GregorianCalendar cal = new GregorianCalendar();
		int nDays = 0, lDays = 0;
		for(int i = 0; i < 365; i ++) {
	        cal.setTime(startDate);
	        cal.add(Calendar.DATE, i);
			
	        if (isLeapYear(cal.get(Calendar.YEAR))) {
	        	lDays ++;
	        } else {
	        	nDays ++;
	        }
	        
	        if (lDays + nDays == 30) {
	        	break;
	        }
		}
		
		double intDPerDay = pctRate / 100f / 365f;		// 평년 1원에 대한 1일당 이자
		double intLPerDay = pctRate / 100f / 366f;		// 윤년 1원에 대한 1일당 이자
		
		return sum + (int)(Math.floor(loanBalance * (intDPerDay * nDays + intLPerDay * lDays) / 10d) * 10d);
	}
	
	/**
	 * 대출 조기 상환금 총액 획득
	 */
	public static int getRestLoanPlusInterest(int loanBalance, float pctRate, Date lastPayDate, Date dueDate) {
		
		int sum = loanBalance;
		
		if (lastPayDate == null || dueDate == null) {
			return sum;
		}
		
		Date startDate = Util.addDays(lastPayDate, 1);
		if (startDate.compareTo(dueDate) > 0) {
			return sum;
		}
		
		GregorianCalendar cal = new GregorianCalendar();
		int nDays = 0, lDays = 0;
		for(int i = 0; i < 365; i ++) {
	        cal.setTime(startDate);
	        cal.add(Calendar.DATE, i);
			
	        if (isLeapYear(cal.get(Calendar.YEAR))) {
	        	lDays ++;
	        } else {
	        	nDays ++;
	        }
	        
	        Date d = cal.getTime();
	        
	        if (d.compareTo(dueDate) == 0) {
	        	break;
	        }
		}
		
		double intDPerDay = pctRate / 100f / 365f;		// 평년 1원에 대한 1일당 이자
		double intLPerDay = pctRate / 100f / 366f;		// 윤년 1원에 대한 1일당 이자
		
		return sum + (int)(Math.floor(loanBalance * (intDPerDay * nDays + intLPerDay * lDays) / 10d) * 10d);
	}
	
	private static boolean isLeapYear(int year) {
		
		GregorianCalendar gc = new GregorianCalendar();
		return gc.isLeapYear(year);
	}
	

	// 
	//
	//  브랜드 자료 엑셀 업로드 & 일괄 업데이트
	//
	//
	private static HashMap<String, Integer> getOptPrices(JsonNode nodeRoot) {
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		Iterator<JsonNode> iter = nodeRoot.iterator();
		while(iter.hasNext()){
			JsonNode optNode = iter.next();
			String name = optNode.path("name").asText();
			int price = optNode.path("price").asInt();
			
			if (!map.containsKey(name)) {
				map.put(name, price);
			}
		}
		
		return map;
	}
	
	/**
	 * 물품 옵션 선택의 기본 감정가 획득
	 * 
	 * 반환값:
	 *        -6 - 잘못된 인자
	 *        -5 - 가격 설정 오류(예: 브랜드에 최저가격 지정없음)
	 *        -4 - 예기치 않은 오류 발생
	 *        -3 - 파일의 내용이 없음
	 *        -2 - 대상의 카테고리, 브랜드 발견 못함
	 *        -1 - 파일이 존재하지 않음
	 *        >=0 - 기본 감정가
	 */
	public static int getPriceOfAppraisal(String optVer, String catName, String brand, String options) {

		if (Util.isNotValid(optVer) || Util.isNotValid(catName) || Util.isNotValid(brand) || Util.isNotValid(options)) {
			return -6;
		}
		
		logger.info("[Appraisal]");
		logger.info("[Appraisal] v" + optVer + " / " + catName + " / " + brand + " / " + options);
		
		String filename = SolUtil.getPhysicalRoot("ProdOpt") + "/" + "cashgo-" + optVer + ".json";
		Util.checkParentDirectory(filename);
		
		File file = new File(filename);
		if (!file.exists()) {
			
			return -1;
		}
		
		String ctnt = Util.getFileContent(filename);
		if (Util.isNotValid(ctnt)) {
			
			return -3;
		}
		
		try {
			ObjectMapper m = new ObjectMapper();
			JsonNode rootNode = m.readTree(ctnt);
			
			JsonNode catNodes = rootNode.path("categories");
			if (catNodes != null) {
        		Iterator<JsonNode> iter = catNodes.iterator();
        		while(iter.hasNext()){
        			JsonNode catNode = iter.next();
        			String catStr = catNode.path("name").asText();

        			if (catStr.equals(catName)) {
        				JsonNode brandNodes = catNode.path("brands");
        				if (brandNodes != null) {
        	        		Iterator<JsonNode> iter2 = brandNodes.iterator();
        	        		while(iter2.hasNext()){
        	        			JsonNode brandNode = iter2.next();
        	        			String brandStr = brandNode.path("name").asText();

        	        			if (brandStr.equals(brand)) {
        	        				int price = brandNode.path("minPrice").asInt();
        	        				logger.info("[Appraisal] min price: " + new DecimalFormat("###,###,###,##0").format(price));
        	        				
        	        				if (price > 0) {
            	        				List<String> opts = Util.tokenizeValidStr(options);
            	        				JsonNode optRootNodes = brandNode.path("brandOptions");
            	        				
            	        				int idx = 0;
            	        				if (opts.size() > 0 && optRootNodes != null) {
            	        					Iterator<JsonNode> iter3 = optRootNodes.iterator();
            	        					while(iter3.hasNext()){
            	        						JsonNode optNode = iter3.next();
            	        						String optStr = optNode.path("name").asText();
                	        					
                	        					JsonNode optNodes = optNode.path("options");
                	        					if (optNodes != null) {
                	        						HashMap<String, Integer> map = getOptPrices(optNodes);
                	        						if (map.containsKey(opts.get(idx))) {
                	        							int optPrice = map.get(opts.get(idx));
                	        							
                	        							if (optPrice > 0) {
                                	        				logger.info("[Appraisal] option: " + optStr + ", sel: " + opts.get(idx) + ", optPrice: " + new DecimalFormat("###,###,###,##0").format(optPrice));
                        	        						price += optPrice;
                	        							} else {
                                	        				logger.info("[Appraisal] option: " + optStr + ", sel: " + opts.get(idx));
                	        							}
                	        						}
                	        					}
                	        					
                	        					idx ++;
                	        					if (idx >= opts.size()) {
                	        						break;
                	        					}
            	        					}
            	        				}
            	        				
            	        				logger.info("[Appraisal] price: " + new DecimalFormat("###,###,###,##0").format(price));
            	        				
            	        				return price;
        	        				} else {
        	        					return -5;
        	        				}
        	        			}
        	        		}
        				}
        				
        				break;
        			}
        		}
				
			}
			
		} catch (Exception e) {
			logger.error("getPriceOfAppraisal", e);
			
			return -4;
		}
		
		return -2;
	}

	
	// 
	//
	//  브랜드 자료 엑셀 업로드 & 일괄 업데이트
	//
	//
	private static boolean isBrandExcelRightFormat(XSSFSheet sheet) {
		
		if (sheet != null) {
			if (getExcelCellValue(sheet, 0, 0).equals("카테고리") && 
					getExcelCellValue(sheet, 1, 0).equals("브랜드") &&
					getExcelCellValue(sheet, 2, 0).equals("최저가") &&
					Util.isValid(getExcelCellValue(sheet, 0, 1)) &&
					Util.isValid(getExcelCellValue(sheet, 1, 1)) &&
					Util.isValid(getExcelCellValue(sheet, 2, 1))) {
				
				return true;
			}
		}
		
		return false;
	}
	
	private static String getExcelCellValue(XSSFSheet sheet, int rowIndex, int colIndex) {
		
		String ret = "";
		
		XSSFRow row = sheet.getRow(rowIndex);
		if (row != null) {
			XSSFCell cell = row.getCell(colIndex);
			if (cell == null) {
				ret = "";
			} else {
				switch (cell.getCellTypeEnum()) {
				case _NONE:
				case BOOLEAN:
				case ERROR:
				case BLANK:
				case FORMULA:
					ret = "";
					break;
				case NUMERIC:
	                ret = String.valueOf(cell.getNumericCellValue());
	                break;
				case STRING:
					ret = cell.getStringCellValue();
					break;
				}
			}
		}
		
		return ret;
	}
	
	private static List<DropDownListItem> getOneBrandOption(XSSFSheet sheet, int rowIndex) {
	
		List<DropDownListItem> ret = new ArrayList<DropDownListItem>();

		while(true) {
			XSSFRow row = sheet.getRow(rowIndex);
			if (row == null) {
				break;
			}
			
			String col1 = getExcelCellValue(sheet, rowIndex, 0);
			String col2 = getExcelCellValue(sheet, rowIndex, 1);
			
			if (Util.isNotValid(col1) || Util.isNotValid(col2) || col1.startsWith("*")) {
				break;
			}
			
			int price = (int)Util.parseDouble(col2, -1);
			if (price >= 0) {
				ret.add(new DropDownListItem(col1.trim(), new DecimalFormat("###########0").format(price)));
			}
			
			rowIndex++;
		}
		
		return ret;
	}
	

	// 
	//
	//  메일 발송
	//
	//
	
	/**
	 * 환영 이메일 발송
	 */
    public static boolean sendWelcomeMail(String email, String key) {
		
		if (Util.isNotValid(email)) {
			return false;
		}
    
    	String title = "[CashGo] 환영합니다!!";
    	String from = "noreply@cashgo.co.kr";
    	
    	String url = Util.getFileProperty("url.activateAccount") + "?email=" + email + "&key=" + key;
    	
    	String content = "<strong>CashGo</strong> 고객 등록이 완료되었습니다.<br><br>" +
    			"<a href='" + url + "'>여기</a>를 클릭하여 계정의 활성화를 진행해 주시기 바랍니다.";
    	
    	return sendMail(true, title, email, from, content);
    }
	
	/**
	 * 대출 계약서 이메일 발송
	 */
    public static boolean sendContractMail(String email, String filename, String content, String dealType) {
		
		if (Util.isNotValid(email)) {
			return false;
		}
    
    	String title = "";
    	String from ="noreply@cashgo.co.kr";
    	
    	//String content = "첨부 파일을 확인해 주시기 바랍니다.";
    	
    	ArrayList<DropDownListItem> attachFiles = new ArrayList<DropDownListItem>();
		if(dealType == "L"){
			title = "[CashGo] 대출 계약서";
			attachFiles.add(new DropDownListItem(SrsGlobalInfo.ContractMailMyFilename, getPhysicalRoot("Contract") + "/" + filename));
		}else if(dealType == "S"){
			title = "[CashGo] 판매 계약서";
			attachFiles.add(new DropDownListItem(SrsGlobalInfo.ContractMailMyFilename, getPhysicalRoot("Deal") + "/" + filename));
		}
    	
    	//attachFiles.add(new DropDownListItem(SrsGlobalInfo.ContractMailStandardFilename, getPhysicalRoot("Base") + "/" + SrsGlobalInfo.StandardContractFilename));
    	
    	return sendMail(false, title, email, from, content, attachFiles);
    }
    
	/**
	 * 이메일 발송
	 */
	public static boolean sendMail(boolean highPriorityMode, String title, String to, String from, String content) {
		
		Properties p = new Properties();
		
		p.put("mail.smtp.user", Util.getFileProperty("mail.smtp.user"));
		p.put("mail.smtp.host", Util.getFileProperty("mail.smtp.host"));
		p.put("mail.smtp.port", Util.getFileProperty("mail.smtp.port"));
		p.put("mail.smtp.starttls.enable", Util.getFileProperty("mail.smtp.starttls.enable"));
		p.put("mail.smtp.auth", Util.getFileProperty("mail.smtp.auth"));

		p.put("mail.smtp.debug", Util.getFileProperty("mail.smtp.debug"));
		p.put("mail.smtp.socketFactory.port", Util.getFileProperty("mail.smtp.socketFactory.port"));
		p.put("mail.smtp.socketFactory.fallback", Util.getFileProperty("mail.smtp.socketFactory.fallback"));

		try {
			Authenticator auth = new SMTPAuthenticator();
	         
			Session session = Session.getInstance(p, auth);
			session.setDebug(true);

			MimeMessage msg = new MimeMessage(session);
			
			msg.setSubject(title, "UTF-8");
			
			if (highPriorityMode) {
				msg.setHeader("X-Priority", "1");
			}


			//보내는 사람의 메일주소
			msg.setFrom(new InternetAddress(from));

			// 받는 사람의 메일주소
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setSentDate(new Date());
			String basehtml = "<div class='cashgo_main'>"//cid:AbcXyz123
							+ "<div style='background-image:url(\"http://211.234.117.207/base/mail_base.png\"); width: 560px; height: 798px;'>"
							+ "<div style='width: 500px; height: 350px; padding: 5%; padding-top: 65%;'>"
							+ "<p style=\"font-size: 1.1rem;\">"+content+"</p>"
							+ "</div>";
			// 내용 및 첨부 파일 지정
			Multipart multipart = new MimeMultipart();
			String base_mail_png = SolUtil.getPhysicalRoot("Base") + "/" + "mail_base.png";

			// 내용 및 첨부 파일 지정 - 내용
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(basehtml, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			
			// MimeBodyPart imagePart = new MimeBodyPart();
			// imagePart.setHeader("Content-ID", "<AbcXyz123>");
			// imagePart.setDisposition(MimeBodyPart.INLINE);
			// // attach the image file
			// imagePart.attachFile(base_mail_png);
			// multipart.addBodyPart(imagePart);

			msg.setContent(multipart);
			//메세지 전송
			Transport.send(msg);
			
			return true;
		} catch (Exception e) {
			logger.error("sendMail", e);
		}

		return false;
	}
    
	/**
	 * 이메일 발송
	 */
	public static boolean sendMail(boolean highPriorityMode, String title, String to, String from, String content, 
			List<DropDownListItem> attachFiles) {
		
		if (attachFiles == null || attachFiles.size() == 0) {

			return sendMail(highPriorityMode, title, to, from, content);
		}
		
		Properties p = new Properties();
		
		p.put("mail.smtp.user", Util.getFileProperty("mail.smtp.user"));
		p.put("mail.smtp.host", Util.getFileProperty("mail.smtp.host"));
		p.put("mail.smtp.port", Util.getFileProperty("mail.smtp.port"));
		p.put("mail.smtp.starttls.enable", Util.getFileProperty("mail.smtp.starttls.enable"));
		p.put("mail.smtp.auth", Util.getFileProperty("mail.smtp.auth"));

		p.put("mail.smtp.debug", Util.getFileProperty("mail.smtp.debug"));
		p.put("mail.smtp.socketFactory.port", Util.getFileProperty("mail.smtp.socketFactory.port"));
		p.put("mail.smtp.socketFactory.fallback", Util.getFileProperty("mail.smtp.socketFactory.fallback"));

		try {
			Authenticator auth = new SMTPAuthenticator();
	         
			Session session = Session.getInstance(p, auth);
			session.setDebug(true);

			MimeMessage msg = new MimeMessage(session);
			
			msg.setSubject(title, "UTF-8");
			
			if (highPriorityMode) {
				msg.setHeader("X-Priority", "1");
			}


			//보내는 사람의 메일주소
			msg.setFrom(new InternetAddress(from));

			// 받는 사람의 메일주소
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			msg.setSentDate(new Date());
			String basehtml = "<div class='cashgo_main'>"
					+ "<div style='background-image:url(\"http://211.234.117.207/base/mail_base.png\"); width: 560px; height: 798px;'>"
					+ "<div style='width: 500px; height: 350px; padding: 5%; padding-top: 65%;'>"
  					+"<p style=\"font-size: 1.1rem;\">"+content+"</p>"
  					+"</div>";
			// 내용 및 첨부 파일 지정
			Multipart multipart = new MimeMultipart();
			String base_mail_png = SolUtil.getPhysicalRoot("Base") + "/" + "mail_base.png";
			
			// 내용 및 첨부 파일 지정 - 내용
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(basehtml, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			
			// MimeBodyPart imagePart = new MimeBodyPart();
			// imagePart.setHeader("Content-ID", "<AbcXyz123>");
			// imagePart.setDisposition(MimeBodyPart.INLINE);
			// // attach the image file
			// imagePart.attachFile(base_mail_png);
			// multipart.addBodyPart(imagePart);
			
			// 내용 및 첨부 파일 지정 - 첨부 파일
			for (DropDownListItem attach : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();
				attachPart.attachFile(attach.getValue());
				attachPart.setFileName(MimeUtility.encodeText(attach.getText(), "UTF-8", null));
				
				multipart.addBodyPart(attachPart);
			}
			
			msg.setContent(multipart);
			//makeTestContractImage();
			//메세지 전송
			Transport.send(msg);
			
			return true;
		} catch (Exception e) {
			logger.error("sendMail", e);
		}

		return false;
	}
	
	private static class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(Util.getFileProperty("mail.smtp.user"), 
					Util.getFileProperty("mail.smtp.userPwd")); 
		}
	}
	

	
	// 
	//
	//  대출 계약서 이미지 생성
	//
	//
	


	
	
	// 
	//
	//  녹취 서비스
	//
	//
	
	/**
	 * 녹취 서비스 등록 요청
	 */
	public static void requestRecService(String phoneNum, String memberName, int amount, int dealId) {
		
		String url = Util.getFileProperty("url.recService");
		if (Util.isValid(url)) {
			try {
				url += "|" + phoneNum + "|" + URLEncoder.encode(memberName, "UTF-8") + "|" + amount + "|24|" + dealId;

				logger.info("[Request Recording Service] url: " + url);
				Util.requestHttpGet(url);
			} catch (Exception e) {
				logger.error("requestRecService", e);
			}
		}
	}
	

	
	// 
	//
	//  SMS 전송
	//
	//
	
	/**
	 * 휴대폰번호 인증 SMS 전송
	 */
	public static void sendSmsVerifPhoneNumber(String phoneNum, String numbers) {
		
		String msg = Util.getFileProperty("sms.msg.phonenum");
		int result = SmsUtil.sendSms(phoneNum, Util.replaceWithTokens(msg, numbers));
		if (result == 1) {
			logger.info("[SMS send] Verification of Phone Number: " + phoneNum + ", " + numbers + " - success");
		}
	}
}
