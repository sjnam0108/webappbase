package kr.co.bbmc.models.srs.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.StateTime;


public interface StateTimeDao {

	// Common
	public StateTime get(int id);
	public List<StateTime> getList(String lc_mac,String date);
	public List<StateTime> getList(String lc_mac);
	public void saveOrUpdate(StateTime stateTime);
	public void saveOrUpdate(List<StateTime> stateTime);
	public void delete(StateTime stateTime);
	public void delete(List<StateTime> stateTime);


	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for Member specific
	public StateTime get(String lc_mac);
	public List<StateTime> getListByStatus(String status);
}
