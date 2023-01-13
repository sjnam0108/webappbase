package kr.co.bbmc.models.srs;

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
@Table(name="lc_sr")
public class LcSr {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "lc_sr_seq_gen")
	@SequenceGenerator(name = "lc_sr_seq_gen", sequenceName = "srs_LC_SR_SEQ")
	@Column(name = "lc_sr_id")
	private int id;

	
	@Column(name = "lc_id", nullable = false,unique = true, length = 11)
	private int lc_id;	
	
	@Column(name = "srs_id", nullable = false, length = 11)
	private int srs_id;	

	@Column(name = "lc_mac", nullable = false, length = 50)
	private String lc_mac;
	
	@Column(name = "sr_no", nullable = false, length = 50)
	private String sr_no;

	@Column(name = "sr_power", nullable = false, length = 50)
	private String sr_power;
	
	@Column(name = "sr_volt", nullable = false, length = 50)
	private String sr_volt;
	
	@Column(name = "sr_light", nullable = false, length = 50)
	private String sr_light;
	
	@Column(name = "light_type", nullable = false, length = 50)
	private String light_type;
	
	@Column(name = "sr_ctrl_power", nullable = false, length = 50)
	private String sr_ctrl_power;
	
	@Column(name = "sr_ctrl_light", nullable = false, length = 50)
	private String sr_ctrl_light;
	
	@Column(name = "sr_ctrl_wLight", nullable = false, length = 50)
	private String sr_ctrl_wLight;
	
	@Column(name = "sr_ctrl_yLight", nullable = false, length = 50)
	private String sr_ctrl_yLight;

	@Column(name = "sr_battery", nullable = false, length = 50)
	private String sr_battery;	

	@Column(name = "ctrl_update_date" ,nullable = false)
	private Date ctrl_update_date;
	
	@Column(name = "lc_create_date" ,nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "lc_last_date", nullable = false)
	private Date whoLastUpdateDate;
	
	public LcSr() {}
	
	public LcSr(int lc_id, String sr_no,  String lc_mac,
			 HttpSession session) {
		
		this.lc_id = lc_id;
		this.sr_no = sr_no;
		this.lc_mac = lc_mac;

		
		touchWhoC(session);
	}

	public void touchWhoC(HttpSession session) {
		this.whoCreationDate = new Date();
		touchWho(session);
	}

	
	public void touchWho(HttpSession session) {
		this.whoLastUpdateDate = new Date();

	}
	public void touchCtrl(HttpSession session) {
		this.ctrl_update_date = new Date();

	}
	
	public void touchWho() {
		this.whoLastUpdateDate = new Date();

	}
	public void touchCtrl() {
		this.ctrl_update_date = new Date();

	}


	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getlc_id() {
		return lc_id;
	}

	public void setlc_id(int lc_id) {
		this.lc_id = lc_id;
	}
	
	public int getsrs_id() {
		return srs_id;
	}

	public void setsrs_id(int srs_id) {
		this.srs_id = srs_id;
	}


	public String getsr_no() {
		return sr_no;
	}

	public void setsr_no(String sr_no) {
		this.sr_no = sr_no;
	}

	public String getsr_battery() {
		return sr_battery;
	}

	public void setsr_battery(String sr_battery) {
		this.sr_battery = sr_battery;
	}

	public String getsr_power() {
		return sr_power;
	}

	public void setsr_power(String sr_power) {
		this.sr_power = sr_power;
	}
	
	public String getsr_volt() {
		return sr_volt;
	}

	public void setsr_volt(String sr_volt) {
		this.sr_volt = sr_volt;
	}
	
	public String getsr_light() {
		return sr_light;
	}

	public void setsr_light(String sr_light) {
		this.sr_light = sr_light;
	}
	
	public String getlight_type() {
		return light_type;
	}

	public void setlight_type(String light_type) {
		this.light_type = light_type;
	}
	
	public String getsr_ctrl_power() {
		return sr_ctrl_power;
	}

	public void setsr_ctrl_power(String sr_ctrl_power) {
		this.sr_ctrl_power = sr_ctrl_power;
	}
	
	public String getsr_ctrl_light() {
		return sr_ctrl_light;
	}

	public void setsr_ctrl_light(String sr_ctrl_light) {
		this.sr_ctrl_light = sr_ctrl_light;
	}
	
	public String getsr_ctrl_wLight() {
		return sr_ctrl_wLight;
	}

	public void setsr_ctrl_wLight(String sr_ctrl_wLight) {
		this.sr_ctrl_wLight = sr_ctrl_wLight;
	}
	
	public String getsr_ctrl_yLight() {
		return sr_ctrl_yLight;
	}

	public void setsr_ctrl_yLight(String sr_ctrl_yLight) {
		this.sr_ctrl_yLight = sr_ctrl_yLight;
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

	public Date getWhoLastUpdateDate() {
		return whoLastUpdateDate;
	}

	public void setWhoLastUpdateDate(Date whoLastUpdateDate) {
		this.whoLastUpdateDate = whoLastUpdateDate;
	}
	public Date getctrl_update_date() {
		return ctrl_update_date;
	}

	public void setctrl_update_date(Date ctrl_update_date) {
		this.ctrl_update_date = ctrl_update_date;
	}


}
