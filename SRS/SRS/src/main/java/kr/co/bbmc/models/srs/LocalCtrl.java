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
@Table(name="local_ctrl")
public class LocalCtrl {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "local_ctrl_seq_gen")
	@SequenceGenerator(name = "local_ctrl_seq_gen", sequenceName = "srs_LOCAL_CTRL_SEQ")
	@Column(name = "lc_id")
	private int id;

	@Column(name = "lc_ip", length = 50)
	private String lc_ip;
	
	@Column(name = "lc_mac", nullable = false, length = 50, unique = true)
	private String lc_mac;
	
	@Column(name = "lc_name", nullable = false, length = 50)
	private String lc_name;
	
	@Column(name = "lc_valid_start", length = 50)
	private String lc_valid_start;
	
	@Column(name = "lc_valid_end", length = 50)
	private String lc_valid_end;
	
	@Column(name = "lc_area1", length = 50)
	private String lc_area1;

	@Column(name = "lc_addr1", length = 100)
	private String lc_addr1;

	@Column(name = "lc_gps_lat", length = 50)
	private String lc_gps_lat;

	@Column(name = "lc_gps_long", length = 50)
	private String lc_gps_long;

	@Column(name = "lc_manager_tel", length = 50)
	private String lc_manager_tel;

	@Column(name = "lc_sales_tel", length = 50)
	private String lc_sales_tel;

	@Column(name = "lc_memo", length = 50)
	private String lc_memo;

	@Column(name = "lc_use_time", length = 50)
	private String lc_use_time;

	@Column(name = "lc_state", length = 50)
	private String lc_state;
	
	@Column(name = "lc_pu_rain_state", length = 50)
	private String lc_pu_rain_state;
	
	@Column(name = "lc_pu_temp", length = 50)
	private String lc_pu_temp;
	
	@Column(name = "lc_pu_max_min", length = 50)
	private String lc_pu_max_min;
	
	@Column(name = "lc_pu_humidity", length = 50)
	private String lc_pu_humidity;
	
	@Column(name = "lc_pu_wCondition", length = 50)
	private String lc_pu_wCondition;
	
	@Column(name = "lc_pu_wDirection", length = 50)
	private String lc_pu_wDirection;
	
	@Column(name = "lc_pu_wSpeed", length = 50)
	private String lc_pu_wSpeed;
	
	@Column(name = "lc_pu_dust", length = 50)
	private String lc_pu_dust;
	
	@Column(name = "lc_pu_ultra_dust", length = 50)
	private String lc_pu_ultra_dust;
	
	@Column(name = "lc_temp", length = 50)
	private String lc_temp;
	
	@Column(name = "lc_rain", length = 50)
	private String lc_rain;
	
	@Column(name = "lc_humidity", length = 50)
	private String lc_humidity;
	
	@Column(name = "lc_vRange", length = 50)
	private String lc_vRange;
	
	@Column(name = "lc_aPressure", length = 50)
	private String lc_aPressure;
	
	@Column(name = "lc_wCondition", length = 50)
	private String lc_wCondition;
	
	@Column(name = "lc_wDirection", length = 50)
	private String lc_wDirection;
	
	@Column(name = "lc_wSpeed", length = 50)
	private String lc_wSpeed;
	
	@Column(name = "lc_traffic", length = 50)
	private String lc_traffic;
	
	@Column(name = "lc_avgSpeed", length = 50)
	private String lc_avgSpeed;
	
	@Column(name = "lc_trafficJam", length = 50)
	private String lc_trafficJam;
	
	@Column(name = "lc_sun", length = 50)
	private String lc_sun;
	
	@Column(name = "lc_dust10", length = 50)
	private String lc_dust10;
	
	@Column(name = "lc_dust25", length = 50)
	private String lc_dust25;	
	
	@Column(name = "lc_dust100", length = 50)
	private String lc_dust100;
	@Column(name = "lc_battery", length = 50)
	private String lc_battery;
	
	@Column(name = "lc_road_temp", length = 50)
	private String lc_road_temp;
	
	@Column(name = "lc_road_name", length = 50)
	private String lc_road_name;
	
	@Column(name = "lc_sp_limit", length = 50)
	private String lc_sp_limit;
	
	@Column(name = "lc_total_distance", length = 50)
	private String lc_total_distance;
	
	@Column(name = "lc_create_date" ,nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "lc_last_date", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "lc_power_date", nullable = false)
	private Date powerLastUpdateDate;
	

	public LocalCtrl() {}
	
	public LocalCtrl(String lc_ip, String lc_name, String lc_mac, String lc_valid_start, String lc_valid_end, String lc_area1,
			String lc_addr1, String lc_gps_lat, String lc_gps_long, String lc_manager_tel, String lc_sales_tel, String lc_memo, String lc_state,
			String lc_road_name, String lc_sp_limit, String lc_total_distance,HttpSession session) {
		this.lc_ip = lc_ip;
		this.lc_name = lc_name;
		this.lc_mac = lc_mac;
		this.lc_valid_start = lc_valid_start;
		this.lc_valid_end = lc_valid_end;
		this.lc_area1 = lc_area1;
		this.lc_addr1 = lc_addr1;
		this.lc_gps_lat = lc_gps_lat;
		this.lc_gps_long = lc_gps_long;
		this.lc_manager_tel = lc_manager_tel;
		this.lc_sales_tel = lc_sales_tel;
		this.lc_memo = lc_memo;
		this.lc_road_name = lc_road_name;
		this.lc_sp_limit = lc_sp_limit;
		this.lc_total_distance = lc_total_distance;
		this.lc_state = lc_state;


		
		
		if (Util.isNotValid(lc_ip)) {
			this.lc_ip = "";
		}
		if (Util.isNotValid(lc_valid_start)) {
			this.lc_valid_start = "";
		}
		if (Util.isNotValid(lc_valid_end)) {
			this.lc_valid_end = "";
		}
		if (Util.isNotValid(lc_area1)) {
			this.lc_area1 = "";
		}
		if (Util.isNotValid(lc_addr1)) {
			this.lc_addr1 = "";
		}
		if (Util.isNotValid(lc_gps_lat)) {
			this.lc_gps_lat = "";
		}
		if (Util.isNotValid(lc_gps_long)) {
			this.lc_gps_long = "";
		}
		if (Util.isNotValid(lc_manager_tel)) {
			this.lc_manager_tel = "";
		}
		if (Util.isNotValid(lc_sales_tel)) {
			this.lc_sales_tel = "";
		}
		if (Util.isNotValid(lc_memo)) {
			this.lc_memo = "";
		}
		if (Util.isNotValid(lc_road_name)) {
			this.lc_road_name = "";
		}
		if (Util.isNotValid(lc_sp_limit)) {
			this.lc_sp_limit = "0";
		}
		if (Util.isNotValid(lc_total_distance)) {
			this.lc_total_distance = "0";
		}
		if (Util.isNotValid(lc_state)) {
			this.lc_state = "4";
		}

		touchWhoC(session);
	}
	
	

	private void touchWhoC(HttpSession session) {

		this.whoCreationDate = new Date();
		touchWho(session);
		touchPower();
	}

	
	public void touchWho(HttpSession session) {
		this.whoLastUpdateDate = new Date();
	}
	public void touchWho() {
		this.whoLastUpdateDate = new Date();
	}
	public void touchPower() {
		this.powerLastUpdateDate = new Date();
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getlc_ip() {
		return lc_ip;
	}
	
	public void setlc_ip(String lc_ip) {
		this.lc_ip = lc_ip;
	}

	public void setlc_valid_start(String lc_valid_start) {
		this.lc_valid_start = lc_valid_start;
	}
	
	public String getlc_valid_start() {
		return lc_valid_start;
	}

	public void setlc_valid_end(String lc_valid_end) {
		this.lc_valid_end = lc_valid_end;
	}
	public String getlc_valid_end() {
		return lc_valid_end;
	}
	
	public String getlc_addr1() {
		return lc_addr1;
	}

	public void setlc_addr1(String lc_addr1) {
		this.lc_addr1 = lc_addr1;
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

	public String getlc_area1() {
		return lc_area1;
	}
	
	public void setlc_area1(String lc_area1) {
		this.lc_area1 = lc_area1;
	}

	public String getlc_gps_lat() {
		return lc_gps_lat;
	}
	
	public void setlc_gps_lat(String lc_gps_lat) {
		this.lc_gps_lat = lc_gps_lat;
	}

	public String getlc_gps_long() {
		return lc_gps_long;
	}
	
	public void setlc_gps_long(String lc_gps_long) {
		this.lc_gps_long = lc_gps_long;
	}

	public String getlc_manager_tel() {
		return lc_manager_tel;
	}
	
	public void setlc_manager_tel(String lc_manager_tel) {
		this.lc_manager_tel = lc_manager_tel;
	}

	public String getlc_sales_tel() {
		return lc_sales_tel;
	}
	
	public void setlc_sales_tel(String lc_sales_tel) {
		this.lc_sales_tel = lc_sales_tel;
	}

	public String getlc_memo() {
		return lc_memo;
	}
	
	public void setlc_memo(String lc_memo) {
		this.lc_memo = lc_memo;
	}
	
	public String getlc_road_name() {
		return lc_road_name;
	}
	
	public void setlc_road_name(String lc_road_name) {
		this.lc_road_name = lc_road_name;
	}
	
	public String getlc_sp_limit() {
		return lc_sp_limit;
	}
	
	public void setlc_sp_limit(String lc_sp_limit) {
		this.lc_sp_limit = lc_sp_limit;
	}
	
	public String getlc_total_distance() {
		return lc_total_distance;
	}
	
	public void setlc_total_distance(String lc_total_distance) {
		this.lc_total_distance = lc_total_distance;
	}

	public String getlc_use_time() {
		return lc_use_time;
	}
	
	public void setlc_use_time(String lc_use_time) {
		this.lc_use_time = lc_use_time;
	}

	public String getlc_state() {
		return lc_state;
	}
	
	public void setlc_state(String lc_state) {
		this.lc_state = lc_state;
	}
	
	public String getlc_pu_rain_state() {
		return lc_pu_rain_state;
	}
	
	public void setlc_pu_rain_state(String lc_pu_rain_state) {
		this.lc_pu_rain_state = lc_pu_rain_state;
	}
	
	public String getlc_pu_temp() {
		return lc_pu_temp;
	}
	
	public void setlc_pu_temp(String lc_pu_temp) {
		this.lc_pu_temp = lc_pu_temp;
	}
	
	public String getlc_pu_max_min() {
		return lc_pu_max_min;
	}
	
	public void setlc_pu_max_min(String lc_pu_max_min) {
		this.lc_pu_max_min = lc_pu_max_min;
	}
	
	public String getlc_pu_humidity() {
		return lc_pu_humidity;
	}
	
	public void setlc_pu_humidity(String lc_pu_humidity) {
		this.lc_pu_humidity = lc_pu_humidity;
	}
	
	
	public String getlc_pu_wDirection() {
		return lc_pu_wDirection;
	}
	
	public void setlc_pu_wDirection(String lc_pu_wDirection) {
		this.lc_pu_wDirection = lc_pu_wDirection;
	}
	
	public String getlc_pu_wSpeed() {
		return lc_pu_wSpeed;
	}
	
	public void setlc_pu_wSpeed(String lc_pu_wSpeed) {
		this.lc_pu_wSpeed = lc_pu_wSpeed;
	}
	
	public String getlc_pu_dust() {
		return lc_pu_dust;
	}
	
	public void setlc_pu_dust(String lc_pu_dust) {
		this.lc_pu_dust = lc_pu_dust;
	}
	
	public String getlc_pu_ultra_dust() {
		return lc_pu_ultra_dust;
	}
	
	public void setlc_pu_ultra_dust(String lc_pu_ultra_dust) {
		this.lc_pu_ultra_dust = lc_pu_ultra_dust;
	}
	
	public String getlc_pu_wCondition() {
		return lc_pu_wCondition;
	}
	
	public void setlc_pu_wCondition(String lc_pu_wCondition) {
		this.lc_pu_wCondition = lc_pu_wCondition;
	}

	public String getlc_temp() {
		return lc_temp;
	}
	
	public void setlc_temp(String lc_temp) {
		this.lc_temp = lc_temp;
	}
	
	public String getlc_rain() {
		return lc_rain;
	}
	
	public void setlc_rain(String lc_rain) {
		this.lc_rain = lc_rain;
	}
	
	public String getlc_humidity() {
		return lc_humidity;
	}
	
	public void setlc_humidity(String lc_humidity) {
		this.lc_humidity = lc_humidity;
	}
	
	public String getlc_vRange() {
		return lc_vRange;
	}
	
	public void setlc_vRange(String lc_vRange) {
		this.lc_vRange = lc_vRange;
	}
	
	public String getlc_aPressure() {
		return lc_aPressure;
	}
	
	public void setlc_aPressure(String lc_aPressure) {
		this.lc_aPressure = lc_aPressure;
	}
	
	public String getlc_wCondition() {
		return lc_wCondition;
	}
	
	public void setlc_wCondition(String lc_wCondition) {
		this.lc_wCondition = lc_wCondition;
	}
	
	public String getlc_wDirection() {
		return lc_wDirection;
	}
	
	public void setlc_wDirection(String lc_wDirection) {
		this.lc_wDirection = lc_wDirection;
	}
	
	public String getlc_wSpeed() {
		return lc_wSpeed;
	}
	
	public void setlc_wSpeed(String lc_wSpeed) {
		this.lc_wSpeed = lc_wSpeed;
	}
	
	public String getlc_traffic() {
		return lc_traffic;
	}
	
	public void setlc_traffic(String lc_traffic) {
		this.lc_traffic = lc_traffic;
	}
	
	public String getlc_trafficJam() {
		return lc_trafficJam;
	}
	
	public void setlc_trafficJam(String lc_trafficJam) {
		this.lc_trafficJam = lc_trafficJam;
	}
	
	public String getlc_avgSpeed() {
		return lc_avgSpeed;
	}
	
	public void setlc_avgSpeed(String lc_avgSpeed) {
		this.lc_avgSpeed = lc_avgSpeed;
	}
	
	
	
	public String getlc_sun() {
		return lc_sun;
	}
	
	public void setlc_sun(String lc_sun) {
		this.lc_sun = lc_sun;
	}
	
	public String getlc_dust10() {
		return lc_dust10;
	}
	
	public void setlc_dust10(String lc_dust10) {
		this.lc_dust10 = lc_dust10;
	}
	
	public String getlc_dust25() {
		return lc_dust25;
	}
	
	public void setlc_dust25(String lc_dust25) {
		this.lc_dust25 = lc_dust25;
	}
	
	public String getlc_dust100() {
		return lc_dust100;
	}
	
	public void setlc_dust100(String lc_dust100) {
		this.lc_dust100 = lc_dust100;
	}
	
	public String getlc_battery() {
		return lc_battery;
	}
	
	public void setlc_battery(String lc_battery) {
		this.lc_battery = lc_battery;
	}
	
	public String getlc_road_temp() {
		return lc_road_temp;
	}
	
	public void setlc_road_temp(String lc_road_temp) {
		this.lc_road_temp = lc_road_temp;
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
	
	public Date getPowerLastUpdateDate() {
		return powerLastUpdateDate;
	}

	public void setPowerLastUpdateDate(Date PowerLastUpdateDate) {
		this.powerLastUpdateDate = powerLastUpdateDate;
	}


	
}
