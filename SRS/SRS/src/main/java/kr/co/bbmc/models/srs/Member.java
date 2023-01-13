package kr.co.bbmc.models.srs;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.co.bbmc.utils.Util;

@Entity
@Table(name="srs_MEMBERS")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "member_seq_gen")
	@SequenceGenerator(name = "member_seq_gen", sequenceName = "srs_MEMBERS_SEQ")
	@Column(name = "MEMBER_ID")
	private int id;
	
	
	@Column(name = "GRADE", nullable = false, length = 1)
	private String grade = "1";		// 0 / 1 / 2


	@Column(name = "UKID", nullable = false, length = 100, unique = true)
	private String ukid;
	
	@Column(name = "FIRST_NAME", nullable = false, length = 50)
	private String firstName;
	
	@Column(name = "LAST_NAME", nullable = false, length = 50)
	private String lastName;
	
	@Column(name = "SALT", nullable = false, length = 22)
	private String salt;
	
	@Column(name = "PASSWORD", nullable = false, length = 50)
	private String password;
	
	@Column(name = "API_TOKEN", nullable = false, length = 10)
	private String apiToken = "";

	
	@Column(name = "C_PHONE", nullable = false, length = 20)
	private String cPhone;		

	@Column(name = "ZIP_CODE", length = 10)
	private String zipCode = "";

	@Column(name = "ADDR_1", length = 100)
	private String addr1 = "";

	@Column(name = "ADDR_2", length = 100)
	private String addr2 = "";

	@Column(name = "GENDER", nullable = false, length = 1)
	private String gender;			// 남: M, 여: F

	@Column(name = "BIRTH_DATE", nullable = false)
	private Date birthDate;
	
	@Column(name = "IMAGE_ID")
	private Integer imageId;
	
	
	@Transient
	private String imageUrl;
	
	@Transient
	private String plusImg1Url;
	
	@Transient
	private String plusImg2Url;
	
	@Transient
	private String plusImg3Url;
	

	@Column(name = "BANK_CODE", length = 3)
	private String bankCode;
	
	@Column(name = "BANK_ACC_NO", length = 20)	// 은행권 공동 API에서는 16자
	private String bankAccountNo;

	@Column(name = "PLUS_UKID", length = 9)
	private String plusUkid;
	
	@Column(name = "STATUS", nullable = false, length = 1)
	private String status = "M";				// 등록/메일활성화 전: M, 정상: A, 휴면: U, 탈퇴: Z, 메일전송오류: F
	
	
	@Column(name = "BALANCE", nullable = false)
	private int balance = 0;
	
	@Column(name = "WITHDRAWABLE_AMT", nullable = false)
	private int withdrawableAmount = 0;

	
	@Column(name = "PLUS_CHECKED", nullable = false, length = 1)
	private String plusChecked = "N";			// 플러스 등급 Y/N
	
	@Column(name = "VERIF_GRADE_2", nullable = false, length = 1)
	private String verifGrade2 = "N";			// 휴대폰 본인 인증 확인 Y/N
	
	@Column(name = "VERIF_PLUS", nullable = false, length = 1)
	private String verifPlus = "N";				// 플러스 등급 확인 여부 Y/P(심사중)/N


	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	
	public Member() {}
	
	public Member(String ukid, String firstName, String lastName, String password, HttpSession session) {
		
		this.ukid = ukid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.salt = Util.getRandomSalt();
		this.password = Util.encrypt(password, salt);

		touchWhoC(session);
	}

	private void touchWhoC(HttpSession session) {
		
		this.whoCreationDate = new Date();
		touchWho(session);
	}
	
	public void touchWho(HttpSession session) {
		
		this.whoLastUpdateDate = new Date();
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUkid() {
		return ukid;
	}

	public void setUkid(String ukid) {
		this.ukid = ukid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getWhoCreationDate() {
		return whoCreationDate;
	}

	public void setWhoCreationDate(Date whoCreationDate) {
		this.whoCreationDate = whoCreationDate;
	}

	public Date getWhoLastUpdateDate() {
		return whoLastUpdateDate;
	}

	public void setWhoLastUpdateDate(Date whoLastUpdateDate) {
		this.whoLastUpdateDate = whoLastUpdateDate;
	}

	public String getcPhone() {
		return cPhone;
	}

	public void setcPhone(String cPhone) {
		this.cPhone = cPhone;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getWithdrawableAmount() {
		return withdrawableAmount;
	}

	public void setWithdrawableAmount(int withdrawableAmount) {
		this.withdrawableAmount = withdrawableAmount;
	}

	public String getPlusChecked() {
		return plusChecked;
	}

	public void setPlusChecked(String plusChecked) {
		this.plusChecked = plusChecked;
	}

	public String getPlusUkid() {
		return plusUkid;
	}

	public void setPlusUkid(String plusUkid) {
		this.plusUkid = plusUkid;
	}

	public String getVerifPlus() {
		return verifPlus;
	}

	public void setVerifPlus(String verifPlus) {
		this.verifPlus = verifPlus;
	}

	public String getVerifGrade2() {
		return verifGrade2;
	}

	public void setVerifGrade2(String verifGrade2) {
		this.verifGrade2 = verifGrade2;
	}

	public String getPlusImg1Url() {
		return plusImg1Url;
	}

	public void setPlusImg1Url(String plusImg1Url) {
		this.plusImg1Url = plusImg1Url;
	}

	public String getPlusImg2Url() {
		return plusImg2Url;
	}

	public void setPlusImg2Url(String plusImg2Url) {
		this.plusImg2Url = plusImg2Url;
	}

	public String getPlusImg3Url() {
		return plusImg3Url;
	}

	public void setPlusImg3Url(String plusImg3Url) {
		this.plusImg3Url = plusImg3Url;
	}

	
	public String getcPhoneDisp() {
		
		if (Util.isValid(cPhone) && cPhone.startsWith("01")) {
			if (cPhone.length() == 11) {
				return cPhone.substring(0, 3) + "-" + cPhone.substring(3, 7) + "-" + cPhone.substring(7);
			} else if (cPhone.length() == 10) {
				return cPhone.substring(0, 3) + "-" + cPhone.substring(3, 6) + "-" + cPhone.substring(6);
			}
		}
		
		return cPhone;
	}
	
	public String getAddrDisp() {
		
		String ret = "";
		if (Util.isValid(zipCode)) {
			ret = "(우 " + zipCode + ") ";
		}
		if (Util.isValid(addr1)) {
			ret += addr1 + " ";
		}
		if (Util.isValid(addr2)) {
			ret += addr2;
		}
		
		return ret;
	}
}
