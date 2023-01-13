package kr.co.bbmc.models.fnd;

import java.util.Comparator;
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
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonIgnore;

import kr.co.bbmc.utils.Util;

@Entity
@Table(name="FND_SITES")
public class Site {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "site_seq_gen")
	@SequenceGenerator(name = "site_seq_gen", sequenceName = "FND_SITES_SEQ")
	@Column(name = "SITE_ID")
	private int id;
	
	@Column(name = "SHORT_NAME", nullable = false, length = 50, unique = true)
	private String shortName;
	
	@Column(name = "SITE_NAME", nullable = false, length = 50)
	private String siteName;
	
	@Column(name = "EFFECTIVE_START_DATE", nullable = false)
	private Date effectiveStartDate;
	
	@Column(name = "EFFECTIVE_END_DATE")
	private Date effectiveEndDate;
	
	@Column(name = "SERVER_HOST", length = 50)
	private String serverHost;
	
	@Column(name = "SERVER_PORT")
	private Integer serverPort;
	
	@Column(name = "FTP_HOST", length = 50)
	private String ftpHost;
	
	@Column(name = "FTP_PORT")
	private Integer ftpPort;
	
	@Column(name = "FTP_USERNAME", length = 50)
	private String ftpUsername;
	
	@Column(name = "FTP_PASSWORD", length = 50)
	private String ftpPassword;
	
	@Column(name = "CREATION_DATE", nullable = false)
	private Date whoCreationDate;
	
	@Column(name = "LAST_UPDATE_DATE", nullable = false)
	private Date whoLastUpdateDate;
	
	@Column(name = "CREATED_BY", nullable = false)
	private int whoCreatedBy;
	
	@Column(name = "LAST_UPDATED_BY", nullable = false)
	private int whoLastUpdatedBy;
	
	@Column(name = "LAST_UPDATE_LOGIN", nullable = false)
	private int whoLastUpdateLogin;
	
	@OneToMany(mappedBy = "site", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<SiteUser> siteUsers = new HashSet<SiteUser>(0);
	
	@OneToMany(mappedBy = "parentSite", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<SiteSite> parentSites = new HashSet<SiteSite>(0);
	
	@OneToMany(mappedBy = "childSite", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<SiteSite> childSites = new HashSet<SiteSite>(0);

	
	public Site() {}

	public Site(String shortName, String siteName, Date effectiveStartDate, Date effectiveEndDate) {
		this(shortName, siteName, effectiveStartDate, effectiveEndDate, "", 0, "", 0, "", "", null);
	}

	public Site(String shortName, String siteName, Date effectiveStartDate, Date effectiveEndDate,
			String serverHost, Integer serverPort, String ftpHost, Integer ftpPort, String ftpUsername, String ftpPassword) {
		this(shortName, siteName, effectiveStartDate, effectiveEndDate, serverHost, serverPort, ftpHost, ftpPort,
				ftpUsername, ftpPassword, null);
	}

	public Site(String shortName, String siteName, Date effectiveStartDate, Date effectiveEndDate,
			String serverHost, Integer serverPort, String ftpHost, Integer ftpPort, String ftpUsername, String ftpPassword,
			HttpSession session) {
		this.shortName = shortName;
		this.siteName = siteName;
		this.effectiveStartDate = Util.removeTimeOfDate(effectiveStartDate == null ? new Date() : effectiveStartDate);
		this.effectiveEndDate = Util.setMaxTimeOfDate(effectiveEndDate);
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.ftpHost = ftpHost;
		this.ftpPort = ftpPort;
		this.ftpUsername = ftpUsername;
		this.ftpPassword = ftpPassword;

		touchWhoC(session);
	}

	private void touchWhoC(HttpSession session) {
		this.whoCreatedBy = Util.loginUserId(session);
		this.whoCreationDate = new Date();
		touchWho(session);
	}
	
	public void touchWho(HttpSession session) {
		this.whoLastUpdatedBy = Util.loginUserId(session);
		this.whoLastUpdateDate = new Date();
		this.whoLastUpdateLogin = Util.loginId(session);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	@JsonIgnore
	public Set<SiteUser> getSiteUsers() {
		return siteUsers;
	}

	public void setSiteUsers(Set<SiteUser> siteUsers) {
		this.siteUsers = siteUsers;
	}

	@JsonIgnore
	public Set<SiteSite> getParentSites() {
		return parentSites;
	}

	public void setParentSites(Set<SiteSite> parentSites) {
		this.parentSites = parentSites;
	}

	@JsonIgnore
	public Set<SiteSite> getChildSites() {
		return childSites;
	}

	public void setChildSites(Set<SiteSite> childSites) {
		this.childSites = childSites;
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
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

	public int getWhoCreatedBy() {
		return whoCreatedBy;
	}

	public void setWhoCreatedBy(int whoCreatedBy) {
		this.whoCreatedBy = whoCreatedBy;
	}

	public int getWhoLastUpdatedBy() {
		return whoLastUpdatedBy;
	}

	public void setWhoLastUpdatedBy(int whoLastUpdatedBy) {
		this.whoLastUpdatedBy = whoLastUpdatedBy;
	}

	public int getWhoLastUpdateLogin() {
		return whoLastUpdateLogin;
	}

	public void setWhoLastUpdateLogin(int whoLastUpdateLogin) {
		this.whoLastUpdateLogin = whoLastUpdateLogin;
	}

	public static Comparator<Site> ShortNameComparator = new Comparator<Site>() {
		@Override
		public int compare(Site s1, Site s2) {
			return (s1.getShortName().toUpperCase().compareTo(s2.getShortName().toUpperCase()));
		}
	};

}
