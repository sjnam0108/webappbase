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
@Table(name="lc_state_time")
public class StateTime {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "lc_state_time_seq_gen")
	@SequenceGenerator(name = "lc_state_time_seq_gen", sequenceName = "srs_LC_STATE_TIME_SEQ")
	@Column(name = "state_time_id")
	private int id;

	
	@Column(name = "lc_mac", nullable = false, length = 11)
	private String lc_mac;	

	@Column(name = "lc_nomal_time", nullable = false, length = 50)
	private String lc_nomal_time;

	@Column(name = "sr_part_off_time", nullable = false, length = 50)
	private String sr_part_off_time;
	
	@Column(name = "sr_all_off_time", nullable = false, length = 50)
	private String sr_all_off_time;
	
	@Column(name = "lc_off_time", nullable = false, length = 50)
	private String lc_off_time;
	
	@Column(name = "no_enroll_time", nullable = false, length = 50)
	private String no_enroll_time;
	
	@Column(name = "lc_create_date" ,nullable = false)
	private Date whoCreationDate;
	

	public StateTime() {}
	
	public StateTime(String lc_mac,  String lc_nomal_time, String sr_part_off_time,
			String sr_all_off_time,String lc_off_time, String no_enroll_time,HttpSession session) {
		
		this.lc_mac = lc_mac;
		this.lc_nomal_time = lc_nomal_time;
		this.sr_part_off_time = sr_part_off_time;
		this.sr_all_off_time = sr_all_off_time;
		this.lc_off_time = lc_off_time;
		this.no_enroll_time = no_enroll_time;
		

		
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


	public String getlc_nomal_time() {
		return lc_nomal_time;
	}

	public void setlc_nomal_time(String lc_nomal_time) {
		this.lc_nomal_time = lc_nomal_time;
	}

	public String getsr_part_off_time() {
		return sr_part_off_time;
	}

	public void setsr_part_off_time(String sr_part_off_time) {
		this.sr_part_off_time = sr_part_off_time;
	}

	public String getsr_all_off_time() {
		return sr_all_off_time;
	}

	public void setsr_all_off_time(String sr_all_off_time) {
		this.sr_all_off_time = sr_all_off_time;
	}
	
	public String getlc_off_time() {
		return lc_off_time;
	}

	public void setlc_off_time(String lc_off_time) {
		this.lc_off_time = lc_off_time;
	}
	
	public String getno_enroll_time() {
		return no_enroll_time;
	}

	public void setno_enroll_time(String no_enroll_time) {
		this.no_enroll_time = no_enroll_time;
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
