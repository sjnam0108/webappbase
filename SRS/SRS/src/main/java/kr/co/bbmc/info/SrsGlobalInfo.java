package kr.co.bbmc.info;

import java.util.ArrayList;

import kr.co.bbmc.models.srs.Member;
import kr.co.bbmc.viewmodels.DropDownListItem;

public class SrsGlobalInfo {

	public static ArrayList<Member> WelcomeMembers = new ArrayList<Member>();
	
	public static ArrayList<DropDownListItem> ApprResultQueue = new ArrayList<DropDownListItem>();
	
	// 이후에 [설정] 페이지에 포함될 항목값
	public static String BaseContractFilename = "contract-v1.0.png";
	public static String StandardContractFilename = "standard-contract.pdf";
	
	public static String ContractMailMyFilename = "대부거래 표준계약서.png";
	public static String ContractMailStandardFilename = "대부거래 표준약관.pdf";
	
	public static String PublicBankAccount = "우리은행 110-0000-0000";
	public static String PublicBankAccountOwner = "캐시고";
	
	public static int DealFee = 0;
	public static float InterestPctRate = 24f;
	
	//-
}
