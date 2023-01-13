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
@Table(name="local_notice")
public class NoticeEnroll {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "lo_no_seq_gen")
	@SequenceGenerator(name = "lo_no_seq_gen", sequenceName = "srs_LO_NO_SEQ")
	@Column(name = "notice_id")
	private int id;


	@Column(name = "lc_name", nullable = false, length = 50)
	private String lc_name;
	
	@Column(name = "notice_name", nullable = false, length = 50)
	private String notice_name;

	@Column(name = "notice_content", nullable = false, length = 50)
	private String notice_content;

	@Column(name = "notice_create_date" ,nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "notice_last_date", nullable = false)
	private Date whoLastUpdateDate;
	
	public NoticeEnroll() {}
	
	public NoticeEnroll(String lc_name, String notice_name,  String notice_content,
			 HttpSession session) {
		
		this.lc_name = lc_name;
		this.notice_name = notice_name;
		this.notice_content = notice_content;
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
	
	public String getnotice_name() {
		return notice_name;
	}

	public void setnotice_name(String notice_name) {
		this.notice_name = notice_name;
	}

	public String getnotice_content() {
		return notice_content;
	}

	public void setnotice_content(String notice_content) {
		this.notice_content = notice_content;
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
