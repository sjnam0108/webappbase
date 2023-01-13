package kr.co.bbmc.models.srs.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.LcSr;
import kr.co.bbmc.models.srs.LocalCtrl;

public interface LocalCtrlDao {

	// Common
	public LocalCtrl get(int id);
	public List<LocalCtrl> getList();
	public List<LocalCtrl> getList(String lc_mac);
	public void saveOrUpdate(LocalCtrl localCtrl);
	public void delete(LocalCtrl localCtrl);
	public void delete(List<LocalCtrl> localCtrl);

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode);

	// for Member specific
	public LocalCtrl get(String lc_mac);
	public List<LocalCtrl> getListByStatus(String status);
	public List<LocalCtrl> getLocalStateCount();
}
