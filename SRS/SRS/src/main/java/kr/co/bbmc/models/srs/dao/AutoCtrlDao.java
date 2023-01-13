package kr.co.bbmc.models.srs.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.AutoCtrl;
import kr.co.bbmc.models.srs.NoticeEnroll;

public interface AutoCtrlDao {

	// Common
	public AutoCtrl get(int id);
	public List<AutoCtrl> getList();
	public List<AutoCtrl> getListLcName(String lc_name);
	
	public List<AutoCtrl> getList(String lc_mac);
	public void saveOrUpdate(AutoCtrl autoCtrl);
	public void delete(AutoCtrl autoCtrl);
	public void delete(List<AutoCtrl> autoCtrl);

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode);

	// for Member specific
	public AutoCtrl get(String lc_mac);
	public List<AutoCtrl> getListByStatus(String status);
	public List<AutoCtrl> getLocalStateCount();
}
