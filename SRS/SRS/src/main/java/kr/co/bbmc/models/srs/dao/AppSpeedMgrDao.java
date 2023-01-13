package kr.co.bbmc.models.srs.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.LcSr;
import kr.co.bbmc.models.srs.AppSpeedMgr;

public interface AppSpeedMgrDao {

	// Common
	public AppSpeedMgr get(int id);
	public List<AppSpeedMgr> getList();
	public List<AppSpeedMgr> getList(String lc_name);
	public void saveOrUpdate(AppSpeedMgr AppSpeedMgr);
	public void delete(AppSpeedMgr AppSpeedMgr);
	public void delete(List<AppSpeedMgr> AppSpeedMgr);

	// for Kendo Grid Remote Read
	public DataSourceResult getListAppSpeedMgr(DataSourceRequest request);
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode);

	// for Member specific
	public AppSpeedMgr get(String lc_name);
	public List<AppSpeedMgr> getListByStatus(String status);
	public List<AppSpeedMgr> getSelectOption();
}
