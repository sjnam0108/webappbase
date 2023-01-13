package kr.co.bbmc.dbsetup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import kr.co.bbmc.models.fnd.Menu;
import kr.co.bbmc.models.fnd.Privilege;
import kr.co.bbmc.models.fnd.Role;
import kr.co.bbmc.models.fnd.RolePrivilege;
import kr.co.bbmc.models.fnd.Site;
import kr.co.bbmc.models.fnd.SiteUser;
import kr.co.bbmc.models.fnd.User;
import kr.co.bbmc.models.fnd.UserPrivilege;
import kr.co.bbmc.utils.HibernateUtil;

public class InitDataCreation {
	/**
	 * Main Metho
	 */
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		
		// ----ds------------------------------------------------------------
		// 
		// 권한:
		//
		//     AccessAnyChildSite   모든 자식 사이트 접근 권한
		//     AccessAnyMenu        모든 메뉴 접근 권한
		//     ManageSiteJob        사이트 관리 권한
		//     NoConcurrentCheck    동일계정 동시사용체크 무효화 권한
		//     NoTimeOut            세션만기시간 무효화 권한
		//
		//
		//     상세 설명:
		//     
		//         AccessAnyChildSite:
		//            - 현재 사이트와 매핑된 모든 자식 사이트 및 자식 사이트 하위 STB 그룹 "사용자 뷰"를 
		//              이용할 수 있는 권한.
		//            - 이 권한의 원할한 사용을 위해서는 "ManageSiteJob" 권한이 필요함.
		//
		//         AccessAnyMenu:
		//            - 서버에 등록된 모든 메뉴에 접근할 수 있는 권한.
		//            - super user인 경우에는 이 권한만 부여하면 충분하며, 어떤 사용자 계정에 특정 페이지 
		//              접근만 허락할 경우에는 대상 페이지 메뉴 권한을 사용자 계정에 부여해야 함.
		//
		//         ManageSiteJob:
		//            - 사이트 수준의 모든 관리 작업을 수행할 수 있는 권한.
		//
		//         NoConcurrentCheck:
		//            - 동일 계정 사용자의 동시 사용(타 브라우저, 타 기기) 체크를 무효화 시키는 권한.
		//            - 보안의 요소보다 편의성 요소를 더 중요시 하는 고객 또는 상황 발생 시 적용.
		//            - 이 권한을 가진 사용자 계정은 동일 시점 동시 사용이 가능함.
		//
		//         NoTimeOut:
		//            - WAS에 설정된 세션 만기 시간을 무효화 시키는 권한.
		//            - 보안의 요소보다 편의성 요소를 더 중요시 하는 고객 또는 상황 발생 시 적용.
		//            - 이 권한을 가진 사용자는 로그인 시 세션 만기 시간이 자동적으로 무효화되며 수동적으로
		//              로그아웃 버튼을 누르거나 사용중인 브라우저가 닫히기 전까지 계속 세션이 유효하게 됨.
		//            - 기본적으로 롤에 포함되지 않으며, 필요시 개별 사용자에게 권한으로 부여함.
		//
		// ----------------------------------------------------------------
		
		

		// ----------------------------------------------------------------
		// 
		// 롤:
		//
		//     SystemAdmin      모든 사이트 관리 목적의 슈퍼 관리자 롤
		//
		//
		//     상세 설명:
		//     
		//         SystemAdmin:
		//            - 모든 사이트 관리 목적의 슈퍼 관리자 롤
		//            - AccessAnyMenu 권한을 포함하기 때문에 별도의 페이지 접근 권한을 포함하지 않음
		//            - 아래의 특수 권한을 포함:
		//                AccessAnyChildSite   모든 자식 사이트 접근 권한
		//                AccessAnyMenu        모든 메뉴 접근 권한
		//                ManageSiteJob        사이트 관리 권한
		//
		// ----------------------------------------------------------------
		
		
		//
		// 사용자 홈을 초기 데이터로 생성 여부를 지정
		//
		//   USER_HOME_MODE:
		//        true : 사용자 홈 추가 모드
		//        false: 사용자 홈을 사용하지 않음
		//
		final boolean USER_HOME_MODE = true;
		
		
		

		Transaction tx = session.beginTransaction();

		
		//
		//
		// Step 1. 필수 자료
		//
		//
		
		
		//
		// 메뉴
		//
		
    	// [CashGo] ext ----------------------------------------------------------- start
    	//
    	//
		
		Menu deviceSettingMenu = new Menu("DeviceSetting", 300, "fa fa-cog", "002");
		Menu moniteringMenu = new Menu("Monitering", 600, "fa fa-desktop", "003");
		Menu remoteControlMenu = new Menu("RemoteControl", 200, "fa fa-wifi", "004");
		Menu noticeMenu = new Menu("Notice", 500, "fa fa-bell", "005");
    	
    	//
    	//
    	// [CashGo] ext ------------------------------------------------------------- end
		
		Menu siteUserConfigMenu = new Menu("SiteUserConfig", 800, "wrench", "001");



    	// [WAB] --------------------------------------------------------------------------
		// 사용자 홈 페이지
		if (USER_HOME_MODE) {
			Menu userHomeMenu = new Menu("UserHome", "/fnd/userhome", "home", "009", 10, true);
			
			session.save(userHomeMenu);
		}
    	// [WAB] --------------------------------------------------------------------------
		
		
    	// [CashGo] ext ----------------------------------------------------------- start
    	//
    	//
		
		Menu memberMenu = new Menu("Member", "/srs/member", "far fa-user", "", 10, false);
		memberMenu.setParent(deviceSettingMenu);

		Menu localCtrlMenu = new Menu("LocalCtrl", "/srs/localctrl", "", "", 20, false);
		localCtrlMenu.setParent(deviceSettingMenu);


		Menu moniterMainMenu = new Menu("MoniterMain", "/srs/monitermain", "", "", 10, false);
		moniterMainMenu.setParent(moniteringMenu);
		
		Menu gridViewMenu = new Menu("GridView", "/srs/gridview", "", "", 10, false);
		gridViewMenu.setParent(moniteringMenu);

		Menu mapViewMenu = new Menu("MapView", "/srs/mapview", "", "", 10, false);
		mapViewMenu.setParent(moniteringMenu);

		Menu dateChartMenu = new Menu("DateChart", "/srs/datechart", "", "", 10, false);
		dateChartMenu.setParent(moniteringMenu);

		Menu localWeatherMenu = new Menu("LocalWeather", "/srs/localweather", "", "", 10, false);
		localWeatherMenu.setParent(moniteringMenu);

		Menu onSiteMenu = new Menu("OnSite", "/srs/onsite", "", "", 10, false);
		onSiteMenu.setParent(moniteringMenu);
		
		Menu roadInfoMenu = new Menu("RoadInfo", "/srs/roadinfo", "", "", 10, false);
		roadInfoMenu.setParent(moniteringMenu);
		
		Menu spLimitInfoMenu = new Menu("SpLimitInfo", "/srs/splimitInfo", "", "", 10, false);
		spLimitInfoMenu.setParent(moniteringMenu);

		Menu reControlPassiveMenu = new Menu("ReControlPassive", "/srs/recontrolpassive", "", "", 10, false);
		onSiteMenu.setParent(remoteControlMenu);
		
		Menu reControlAutoMenu = new Menu("ReControlAuto", "/srs/recontrolauto", "", "", 10, false);
		onSiteMenu.setParent(remoteControlMenu);
		
		
		Menu noticeEnrollMenu = new Menu("NoticeEnroll", "/srs/noticeenroll", "", "", 10, false);
		noticeEnrollMenu.setParent(noticeMenu);

		Menu noticeSettingMenu = new Menu("NoticeSetting", "/srs/noticesetting", "", "", 10, false);
		noticeSettingMenu.setParent(noticeMenu);
		
		Menu appSpeedMgrMenu = new Menu("AppSpeedMgr", "/srs/appspeeedmgr", "", "", 10, false);
		appSpeedMgrMenu.setParent(noticeMenu);
		
		Menu freezPredicMenu = new Menu("FreezPredic", "/srs/freezPredic", "", "", 10, false);
		freezPredicMenu.setParent(noticeMenu);

		
		Menu virtualFormMenu = new Menu("VirtualForm", "/srs/virtualform", "vial", "", 700, false);
		Menu shortCutsForMenu = new Menu("ShortCutsForm", "/srs/shortcuts", "fa fa-home", "", 700, false);
		
		
		session.save(deviceSettingMenu);
		session.save(remoteControlMenu);
		session.save(reControlPassiveMenu);
		session.save(reControlAutoMenu);
		

		session.save(moniteringMenu);
		session.save(moniterMainMenu);
		session.save(gridViewMenu);
		session.save(mapViewMenu);
		session.save(dateChartMenu);
		session.save(localWeatherMenu);
		session.save(onSiteMenu);
		session.save(roadInfoMenu);
		session.save(spLimitInfoMenu);


		session.save(noticeMenu);
		session.save(noticeEnrollMenu);
		session.save(noticeSettingMenu);
		session.save(appSpeedMgrMenu);
		session.save(freezPredicMenu);
		
		session.save(virtualFormMenu);
		session.save(shortCutsForMenu);
		
		session.save(memberMenu);
		session.save(localCtrlMenu);


    	//
    	//
    	// [CashGo] ext ------------------------------------------------------------- end
		
		
		

		
		
		Menu siteMenu = new Menu("Site", "/fnd/site", "globe", "", 10);
		siteMenu.setParent(siteUserConfigMenu);
		
		Menu userMenu = new Menu("User", "/fnd/user", "user", "", 20);
		userMenu.setParent(siteUserConfigMenu);
		
		Menu siteUserMenu = new Menu("SiteUser", "/fnd/siteuser", "user-check", "", 30);
		siteUserMenu.setParent(siteUserConfigMenu);
		
		Menu loginLogMenu = new Menu("LoginLog", "/fnd/loginlog", "sign-in-alt", "", 50);
		loginLogMenu.setParent(siteUserConfigMenu);

		Menu childSiteMenu = new Menu("ChildSite", "/fnd/childsite", "child", "", 60);
		childSiteMenu.setParent(siteUserConfigMenu);
		
		Menu menuMenu = new Menu("Menu", "/fnd/menu", "concierge-bell", "", 70);
		menuMenu.setParent(siteUserConfigMenu);

		Menu privMenu = new Menu("Privilege", "/fnd/privilege", "cog", "", 80);
		privMenu.setParent(siteUserConfigMenu);

		Menu roleMenu = new Menu("Role", "/fnd/role", "cogs", "", 90);
		roleMenu.setParent(siteUserConfigMenu);

		Menu rolePrivMenu = new Menu("RolePrivilege", "/fnd/rolepriv", "award", "", 100);
		rolePrivMenu.setParent(siteUserConfigMenu);

		Menu userPrivMenu = new Menu("UserPrivilege", "/fnd/userpriv", "user-cog", "", 110);
		userPrivMenu.setParent(siteUserConfigMenu);
		
		

		session.save(siteUserConfigMenu);
		
		session.save(siteMenu);
		session.save(userMenu);
		session.save(siteUserMenu);
		session.save(loginLogMenu);
		session.save(childSiteMenu);
		session.save(menuMenu);
		session.save(privMenu);
		session.save(roleMenu);
		session.save(rolePrivMenu);
		session.save(userPrivMenu);

		
		
		// 메뉴 관련 권한 및 롤 자동 생성
		syncWithPrivAndRole(session);
		
		
		//
		// 권한
		//
		Privilege accessAnyMenuPriv = new Privilege("internal.AccessAnyMenu");
		session.save(accessAnyMenuPriv);
		
		Privilege accessAnyChildSitePriv = new Privilege("internal.AccessAnyChildSite");
		session.save(accessAnyChildSitePriv);
		
		Privilege manageSiteJobPriv = new Privilege("internal.ManageSiteJob");
		session.save(manageSiteJobPriv);
		
		Privilege noConCheckPriv = new Privilege("internal.NoConcurrentCheck");
		session.save(noConCheckPriv);
		
		Privilege noTimeOutPriv = new Privilege("internal.NoTimeOut");
		session.save(noTimeOutPriv);
		
		
		
		//
		// 롤
		//
		Role systemAdminRole = new Role("internal.SystemAdmin");
		session.save(systemAdminRole);
		
		
		
		//
		// 롤 권한
		//
		session.save(new RolePrivilege(systemAdminRole, accessAnyMenuPriv));
		session.save(new RolePrivilege(systemAdminRole, accessAnyChildSitePriv));
		session.save(new RolePrivilege(systemAdminRole, manageSiteJobPriv));
		
		
		
		//
		//
		// Step 2. Foundation 옵션 자료
		//
		//
		
		//
		// 사용자
		//
		session.save(new User("system", "관리자", "welcome", new Date(), null));
		
    	// [WAB] --------------------------------------------------------------------------
		//
		// 사이트
		//
		Site site = new Site("cashgo", "캐쉬고", new Date(), null, "127.0.0.1", 80, "127.0.0.1", 21, "cashgo", "cashgopwd");
    	// [WAB] --------------------------------------------------------------------------
		
		session.save(site);
		
		
		//
		// 사이트 사용자
		//
		User siteUser = (User) session.createCriteria(User.class)
				.add(Restrictions.eq("username", "system")).list().get(0);
		
		session.save(new SiteUser(site, siteUser));

		
		//
		// 사용자 권한
		//
		grantRoleToUser(session, "internal.SystemAdmin", "system");
		

		
		//
		//
		// Step 3. 추가 옵션 자료
		//
		//
		

		

		
		System.out.println("--------------------------------------------------");
		System.out.println("creation finished!!");

		tx.commit();
		session.close();
		
		HibernateUtil.shutdown();
	}
	
	private static Menu getMenu(org.hibernate.Session session, String ukid) {
		@SuppressWarnings("unchecked")
		List<Menu> list = session.createCriteria(Menu.class)
				.add(Restrictions.eq("ukid", ukid)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	private static Role getRole(org.hibernate.Session session, String ukid) {
		@SuppressWarnings("unchecked")
		List<Role> list = session.createCriteria(Role.class)
				.add(Restrictions.eq("ukid", ukid)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	private static Privilege getPrivilege(org.hibernate.Session session, String ukid) {
		@SuppressWarnings("unchecked")
		List<Privilege> list = session.createCriteria(Privilege.class)
				.add(Restrictions.eq("ukid", ukid)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	private static List<Menu> getMenuListById(org.hibernate.Session session, Integer id) {
		Criterion rest;
		
		if (id == null) {
			rest = Restrictions.isNull("parent");
		} else {
			rest = Restrictions.eq("parent.id", id);
		}
		
		return session.createCriteria(Menu.class).add(rest).list();
	}
	
	@SuppressWarnings("unchecked")
	private static void syncWithPrivAndRole(org.hibernate.Session session) {
		List<Menu> menuList = session.createCriteria(Menu.class).list();
		
		for (Menu menu : menuList) {
			String ukidKey =  "menu." + menu.getUkid();
			if (getPrivilege(session, ukidKey) == null)
			{
				session.saveOrUpdate(new Privilege(ukidKey, null));
			}

			if (! getMenuListById(session, menu.getId()).isEmpty() &&
					getRole(session, ukidKey) == null) {
				session.saveOrUpdate(new Role(ukidKey, null));
			}
		}
		
		List<Privilege> privList = session.createCriteria(Privilege.class).list();
		List<Role> roleList = session.createCriteria(Role.class).list();

		ArrayList<Privilege> delPrivList = new ArrayList<Privilege>();
		for (Privilege priv : privList) {
			if (priv.getUkid().startsWith("menu.")) {
				if (getMenu(session, priv.getUkid().substring(5)) == null) {
					delPrivList.add(priv);
				}
			}
		}
        for (Privilege privilege : delPrivList) {
            session.delete(session.load(Privilege.class, privilege.getId()));
        }

		ArrayList<Role> delRoleList = new ArrayList<Role>();
		for (Role role : roleList) {
			if (role.getUkid().startsWith("menu.")) {
				Menu menu = getMenu(session, role.getUkid().substring(5));
				if (menu == null || getMenuListById(session, menu.getId()).isEmpty()) {
					delRoleList.add(role);
				}
			}
		}
        for (Role role : delRoleList) {
            session.delete(session.load(Role.class, role.getId()));
        }
	}
	
	@SuppressWarnings("unchecked")
	private static List<Privilege> getPrivilegeListByRoleId(org.hibernate.Session session, int id) {
		ArrayList<Privilege> privList = new ArrayList<Privilege>();
		
		List<RolePrivilege> list = session.createCriteria(RolePrivilege.class)
				.add(Restrictions.eq("role.id", id)).list();
		for (RolePrivilege rolePriv : list) {
			privList.add(rolePriv.getPrivilege());
		}
		
		return privList;
	}

	@SuppressWarnings("unchecked")
	private static List<UserPrivilege> getUserPrivilegeListByUserId(org.hibernate.Session session, 
			int id) {
		return session.createCriteria(UserPrivilege.class).add(Restrictions.eq("user.id", id))
				.list();
	}
	
	private static Menu getMenuByUkid(org.hibernate.Session session, String ukid) {
		@SuppressWarnings("unchecked")
		List<Menu> list = session.createCriteria(Menu.class)
				.add(Restrictions.eq("ukid", ukid)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}
	
	private static void grantRoleToUser(org.hibernate.Session session, String roleStr, String userStr) {
		User user = (User) session.createCriteria(User.class)
				.add(Restrictions.eq("username", userStr)).list().get(0);
		Role role = getRole(session, roleStr);
		
		if (user != null && role != null) {
			List<UserPrivilege> userPrivList = getUserPrivilegeListByUserId(session, user.getId());
			ArrayList<Integer> privIds = new ArrayList<Integer>();
			for (UserPrivilege userPriv : userPrivList) {
				privIds.add(userPriv.getPrivilege().getId());
			}
			
			if (roleStr.startsWith("menu.")) {
				Menu parentMenu = getMenuByUkid(session, roleStr.substring(5));
				if (parentMenu != null) {
					ArrayList<String> menuUkids = new ArrayList<String>();
					
					menuUkids.add(parentMenu.getUkid());
					List<Menu> menuList = getMenuListById(session, parentMenu.getId());
					for (Menu menu : menuList) {
						menuUkids.add(menu.getUkid());
					}
					
					for (String menuUkid : menuUkids) {
						Privilege priv = getPrivilege(session, "menu." + menuUkid);
						if (priv != null && !privIds.contains(priv.getId())) {
							session.save(new UserPrivilege(user, priv));
							privIds.add(priv.getId());
						}
					}
				}
			} else {
				List<Privilege> privList = getPrivilegeListByRoleId(session, role.getId());
				for (Privilege priv : privList) {
					if (!privIds.contains(priv.getId())) {
						session.save(new UserPrivilege(user, priv));
						privIds.add(priv.getId());
					}
				}
			}
		}
	}
	
	/*
	private static void grantMenuGroupsToRole(org.hibernate.Session session,
			List<String> parentMenuUkids, String roleStr) {
		Role role = getRole(session, roleStr);
		
		if (role != null) {
			ArrayList<String> menuUkids = new ArrayList<String>();
			for (String menuUkid : parentMenuUkids) {
				Menu parentMenu = getMenuByUkid(session, menuUkid);
				if (parentMenu != null) {
					
					menuUkids.add(parentMenu.getUkid());
					List<Menu> menuList = getMenuListById(session, parentMenu.getId());
					for (Menu menu : menuList) {
						if (!menuUkids.contains(menu.getUkid())) {
							menuUkids.add(menu.getUkid());
						}
					}
				}
			}
			
			List<Privilege> privList = getPrivilegeListByRoleId(session, role.getId());
			ArrayList<Integer> privIds = new ArrayList<Integer>();
			for (Privilege priv : privList) {
				if (!privIds.contains(priv.getId())) {
					privIds.add(priv.getId());
				}
			}

			for (String ukid : menuUkids) {
				Privilege priv = getPrivilege(session, "menu." + ukid);
				if (priv != null && !privIds.contains(priv.getId())) {
					session.save(new RolePrivilege(role, priv));
					privIds.add(priv.getId());
				}
			}
		}
	}
	
	private static void grantMenusToRole(org.hibernate.Session session,
			List<String> menuUkids, String roleStr) {
		Role role = getRole(session, roleStr);
		
		if (role != null) {
			List<Privilege> privList = getPrivilegeListByRoleId(session, role.getId());
			ArrayList<Integer> privIds = new ArrayList<Integer>();
			for (Privilege priv : privList) {
				if (!privIds.contains(priv.getId())) {
					privIds.add(priv.getId());
				}
			}

			for (String ukid : menuUkids) {
				Privilege priv = getPrivilege(session, "menu." + ukid);
				if (priv != null && !privIds.contains(priv.getId())) {
					session.save(new RolePrivilege(role, priv));
					privIds.add(priv.getId());
				}
			}
		}
	}
	*/
}
