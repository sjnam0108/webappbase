package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.LocalCtrl;

import kr.co.bbmc.models.srs.CntState;
import kr.co.bbmc.models.srs.LcSr;



@Transactional
public interface LocalCtrlService {

	// Common
	public void flush();
	
	
	

	//
	// for LocaCtrl Dao
	//
	// Common
	public LocalCtrl getLocalCtrl(int id);
	public List<LocalCtrl> getLocalCtrlList();
	public List<LocalCtrl> getLocalStatecount();
	public void saveOrUpdate(LocalCtrl localCtrl);
	public void deleteLocalCtrl(LocalCtrl localCtrl);
	public void deleteLocalCtrls(List<LocalCtrl> localCtrls);

	// for Kendo Grid Remote Read
	public DataSourceResult getLocalCtrlList(DataSourceRequest request);


	// for LocalCtrl specific
	public LocalCtrl getLocalCtrl(String lc_mac);
	
	public List<LocalCtrl> getLocalCtrlListByStatus(String status);



	
	
}
