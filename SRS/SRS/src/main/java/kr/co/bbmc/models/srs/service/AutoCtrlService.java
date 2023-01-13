package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.NoticeEnroll;
import kr.co.bbmc.models.srs.AutoCtrl;


@Transactional
public interface AutoCtrlService {

	// Common
	public void flush();
	
	



	//
	// for GridView Dao
	//
	// Common
	public AutoCtrl getAutoCtrl(int id);
	public List<AutoCtrl> getAutoCtrlList();
	public List<AutoCtrl> getAutoCtrlList(String lc_name);
	public void saveOrUpdate(AutoCtrl autCtrl);
	public void saveOrUpdates(List<AutoCtrl> autoCtrl);
	public void deleteAutoCtrl(AutoCtrl autoCtrl);
	public void deleteAutoCtrls(List<AutoCtrl> autoCtrl);


	// for Kendo Grid Remote Read
	public DataSourceResult getAutoCtrlList(DataSourceRequest request);




	// for LocalCtrl specific
	public List<AutoCtrl> getAutoCtrlListByStatus(String status);



	
	
}
