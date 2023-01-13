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
@Table(name="app_speed_mgr")
public class AppSpeedMgr {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "app_speed_mgr_seq_gen")
	@SequenceGenerator(name = "app_speed_mgr_seq_gen", sequenceName = "srs_APP_SPEED_MGR_SEQ")
	@Column(name = "id")
	private int id;
	
	@Column(name = "lc_name", nullable = false,length = 11)
	private String lc_name;	
	
	@Column(name = "lc_mac", nullable = false, length = 11)
	private String lc_mac;	

	@Column(name = "v_ability", nullable = false, length = 50)
	private String v_ability;

	@Column(name = "limit_speed", nullable = false, length = 50)
	private String limit_speed;
	
	@Column(name = "de_section", nullable = false, length = 50)
	private String de_section;
	
	@Column(name = "speed", nullable = false, length = 50)
	private String speed;
	
	@Column(name = "create_date" ,nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "last_date" ,nullable = false)
	private Date whoLastUpdateDate;

	public AppSpeedMgr() {}
	
	public AppSpeedMgr(String lc_name ,String lc_mac,  String v_ability, String limit_speed,
			String de_section,String speed, HttpSession session) {
		
		this.lc_mac = lc_mac;
		this.lc_name = lc_name;
		this.v_ability = v_ability;
		this.limit_speed = limit_speed;
		this.de_section = de_section;
		this.speed = speed;
		

		
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


	public String getlc_name() {
		return lc_name;
	}

	public void setlc_name(String lc_name) {
		this.lc_name = lc_name;
	}

	public String getv_ability() {
		return v_ability;
	}

	public void setv_ability(String v_ability) {
		this.v_ability = v_ability;
	}

	public String getlimit_speed() {
		return limit_speed;
	}

	public void setlimit_speed(String limit_speed) {
		this.limit_speed = limit_speed;
	}
	
	public String getde_section() {
		return de_section;
	}

	public void setde_section(String de_section) {
		this.de_section = de_section;
	}
	
	public String getspeed() {
		return speed;
	}

	public void setspeed(String speed) {
		this.speed = speed;
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
