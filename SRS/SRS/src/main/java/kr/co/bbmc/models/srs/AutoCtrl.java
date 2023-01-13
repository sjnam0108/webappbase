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
@Table(name="lc_auto_ctrl")
public class AutoCtrl {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "auto_ctrl_seq_gen")
	@SequenceGenerator(name = "auto_ctrl_seq_gen", sequenceName = "srs_AUTO_CTRL_SEQ")
	@Column(name = "ctrl_id")
	private int id;

	
	@Column(name = "lc_name", nullable = false,length = 11)
	private String lc_name;	
	
	@Column(name = "lc_mac", nullable = false,length = 11)
	private String lc_mac;	
	
	@Column(name = "time" , length = 50)
	private String time;
	
	@Column(name = "speed" , length = 50)
	private String speed;
	
	@Column(name = "v_ability" , length = 50)
	private String v_ability;
	
	@Column(name = "rain" , length = 50)
	private String rain;

	@Column(name = "snow", length = 50)
	private String snow;
	

	@Column(name = "ctrl_wLight", length = 50)
	private String ctrl_wLight;
	
	@Column(name = "ctrl_yLight", length = 50)
	private String ctrl_yLight;	

	@Column(name = "ctrl_condition",length = 50)
	private String ctrl_condition;	

	@Column(name = "ctrl_create_date" )
	private Date whoCreationDate;
	
	@Column(name = "ctrl_last_date" )
	private Date whoLastUpdateDate;
	
	public AutoCtrl() {}
	
	public AutoCtrl(String lc_name, String lc_mac ,HttpSession session) {
		
		this.lc_name = lc_name;
		this.lc_mac = lc_mac;

		
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
	public String getlc_mac() {
		return lc_mac;
	}

	public void setlc_mac(String lc_mac) {
		this.lc_mac = lc_mac;
	}
	

	public String gettime() {
		return time;
	}

	public void settime(String time) {
		this.time = time;
	}
	
	public String getspeed() {
		return speed;
	}

	public void setspeed(String speed) {
		this.speed = speed;
	}
	
	public String getrain() {
		return rain;
	}

	public void setrain(String rain) {
		this.rain = rain;
	}

	public String getsnow() {
		return snow;
	}

	public void setsnow(String snow) {
		this.snow = snow;
	}
	
	
	public String getctrl_yLight() {
		return ctrl_yLight;
	}

	public void setctrl_yLight(String ctrl_yLight) {
		this.ctrl_yLight = ctrl_yLight;
	}
	
	public String getctrl_wLight() {
		return ctrl_wLight;
	}

	public void setctrl_wLight(String ctrl_wLight) {
		this.ctrl_wLight = ctrl_wLight;
	}
	
	public String getv_ability() {
		return v_ability;
	}

	public void setv_ability(String v_ability) {
		this.v_ability = v_ability;
	}
	
	public String getctrl_condition() {
		return ctrl_condition;
	}

	public void setctrl_condition(String ctrl_condition) {
		this.ctrl_condition = ctrl_condition;
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


}
