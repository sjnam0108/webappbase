package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.StateTime;


@Transactional
public interface StateTimeService {

	// Common
	public void flush();
	
	



	//
	// for GridView Dao
	//
	// Common
	public StateTime getStateTimeById(int id);
	public StateTime getStateTime(String lc_mac);
	public List<StateTime> getStateTimeList(String lc_mac,String date);
	public List<StateTime> getStateTimeList(String lc_mac);
	public void saveOrUpdate( StateTime stateTime);
	public void saveOrUpdates(List<StateTime> StateTime);
	public void deleteStateTime(StateTime StateTime);
	public void deleteStateTimes(List<StateTime> StateTime);


	// for Kendo Grid Remote Read
	public DataSourceResult getStateTimeList(DataSourceRequest request);




	// for LocalCtrl specific
	public List<StateTime> getStateTimeListByStatus(String status);


	
}
