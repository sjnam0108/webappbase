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
@Table(name="freez_predic")
public class FreezPredic {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "freez_predic_seq_gen")
	@SequenceGenerator(name = "freez_predic_seq_gen", sequenceName = "srs_FREEZ_PREDIC_SEQ")
	@Column(name = "id")
	private int id;
	
	@Column(name = "lc_name", nullable = false,length = 11)
	private String lc_name;	
	
	@Column(name = "lc_mac", nullable = false, length = 11)
	private String lc_mac;	

	@Column(name = "air_temp", nullable = false, length = 50)
	private String air_temp;

	@Column(name = "air_humid", nullable = false, length = 50)
	private String air_humid;
	
	@Column(name = "rain", nullable = false, length = 50)
	private String rain;
	
	@Column(name = "snow", nullable = false, length = 50)
	private String snow;
	
	@Column(name = "win_speed", nullable = false, length = 50)
	private String win_speed;
	
	@Column(name = "road_temp", nullable = false, length = 50)
	private String road_temp;
	
	@Column(name = "freez_predic", nullable = false, length = 50)
	private String freez_predic;
	
	@Column(name = "create_date" ,nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "last_date" ,nullable = false)
	private Date whoLastUpdateDate;

	public FreezPredic() {}
	
	public FreezPredic(String lc_name ,String lc_mac,  String air_temp, String air_humid,
			String rain,String snow,String win_speed,String road_temp,String freez_predic ,HttpSession session) {
		
		this.lc_mac = lc_mac;
		this.lc_name = lc_name;
		this.air_temp = air_temp;
		this.air_humid = air_humid;
		this.rain = rain;
		this.snow = snow;
		this.win_speed = win_speed;
		this.road_temp = road_temp;
		this.freez_predic = freez_predic;
		

		
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

	public String getair_temp() {
		return air_temp;
	}

	public void setair_temp(String air_temp) {
		this.air_temp = air_temp;
	}

	public String getair_humid() {
		return air_humid;
	}

	public void setair_humid(String air_humid) {
		this.air_humid = air_humid;
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
	
	
	public String getlc_mac() {
		return lc_mac;
	}

	public void setlc_mac(String lc_mac) {
		this.lc_mac = lc_mac;
	}
	
	public String getwin_speed() {
		return win_speed;
	}

	public void setwin_speed(String win_speed) {
		this.win_speed = win_speed;
	}
	public String getroad_temp() {
		return road_temp;
	}

	public void setroad_temp(String road_temp) {
		this.road_temp = road_temp;
	}
	public String getfreez_predic() {
		return freez_predic;
	}

	public void setfreez_predic(String freez_predic) {
		this.freez_predic = freez_predic;
	}

	public Date getWhoCreationDate() {
		return whoCreationDate;
	}

	public void setWhoCreationDate(Date whoCreationDate) {
		this.whoCreationDate = whoCreationDate;
	}




}
