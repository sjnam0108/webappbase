package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.AppSpeedMgr;

import kr.co.bbmc.models.srs.CntState;
import kr.co.bbmc.models.srs.LocalCtrl;



@Transactional
public interface AppSpeedMgrService {

	// Common
	public void flush();
	
	



	//
	// for LocaCtrl Dao
	//
	// Common
	public AppSpeedMgr getAppSpeedMgr(int id);
	public List<AppSpeedMgr> getAppSpeedMgrList(String lc_name);
	public List<AppSpeedMgr> getAppSpeedMgrList();
	public List<AppSpeedMgr> getSelectOption();
	public void saveOrUpdate(AppSpeedMgr appspeedmgr);
	public void deleteAppSpeedMgr(AppSpeedMgr appSpeedMgr);
	public void deleteAppSpeedMgrs(List<AppSpeedMgr> appSpeedMgrs);

	// for Kendo Grid Remote Read
	public DataSourceResult getAppSpeedMgrList(DataSourceRequest request);


	// for appspeedmgr specific
	public AppSpeedMgr getAppSpeedMgr(String lc_name);
	public List<AppSpeedMgr> getAppSpeedMgrListByStatus(String status);



	
	
}
