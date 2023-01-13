package kr.co.bbmc.models.srs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import kr.co.bbmc.utils.Util;

@Entity
@Table(name="lc_state_day")
public class StateDay {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "lc_state_day_seq_gen")
	@SequenceGenerator(name = "lc_state_day_seq_gen", sequenceName = "srs_LC_STATE_DAY_SEQ")
	@Column(name = "state_day_id")
	private int id;

	
	@Column(name = "lc_mac", nullable = false, length = 11)
	private String lc_mac;	

	@Column(name = "lc_nomal_day", nullable = false, length = 50)
	private String lc_nomal_day;

	@Column(name = "sr_part_off_day", nullable = false, length = 50)
	private String sr_part_off_day;
	
	@Column(name = "sr_all_off_day", nullable = false, length = 50)
	private String sr_all_off_day;
	
	@Column(name = "lc_off_day", nullable = false, length = 50)
	private String lc_off_day;
	
	@Column(name = "no_enroll_day", nullable = false, length = 50)
	private String no_enroll_day;
	
	@Column(name = "lc_create_date" ,nullable = false)
	private Date whoCreationDate;
	

	public StateDay() {}
	
	public StateDay(String lc_mac,  String lc_nomal_day, String sr_part_off_day,
			String sr_all_off_day,String lc_off_day, String no_enroll_day,HttpSession session) {
		
		this.lc_mac = lc_mac;
		this.lc_nomal_day = lc_nomal_day;
		this.sr_part_off_day = sr_part_off_day;
		this.sr_all_off_day = sr_all_off_day;
		this.lc_off_day = lc_off_day;
		this.no_enroll_day = no_enroll_day;
		

		
		touchWhoC(session);
	}

	public void touchWhoC(HttpSession session) {
		SimpleDateFormat transFormatNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date create = new Date();
		String setDate = transFormatNew.format(create);
		Date createNew = null;
		try {
			createNew = transFormatNew.parse(setDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.whoCreationDate = createNew;
	}

	


	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getlc_nomal_day() {
		return lc_nomal_day;
	}

	public void setlc_nomal_day(String lc_nomal_day) {
		this.lc_nomal_day = lc_nomal_day;
	}

	public String getsr_part_off_day() {
		return sr_part_off_day;
	}

	public void setsr_part_off_day(String sr_part_off_day) {
		this.sr_part_off_day = sr_part_off_day;
	}

	public String getsr_all_off_day() {
		return sr_all_off_day;
	}

	public void setsr_all_off_day(String sr_all_off_day) {
		this.sr_all_off_day = sr_all_off_day;
	}
	
	public String getlc_off_day() {
		return lc_off_day;
	}

	public void setlc_off_day(String lc_off_day) {
		this.lc_off_day = lc_off_day;
	}
	
	public String getno_enroll_day() {
		return no_enroll_day;
	}

	public void setno_enroll_day(String no_enroll_day) {
		this.no_enroll_day = no_enroll_day;
	}
	
	
	public String getlc_mac() {
		return lc_mac;
	}

	public void setlc_mac(String lc_mac) {
		this.lc_mac = lc_mac;
	}


	public Date getWhoCreationDate() {
		return whoCreationDate;
	}

	public void setWhoCreationDate(Date whoCreationDate) {
		this.whoCreationDate = whoCreationDate;
	}




}
