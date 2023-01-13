package kr.co.bbmc.models.srs.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.StateDay;


public interface StateDayDao {

	// Common
	public StateDay get(int id);
	public List<StateDay> getList(String lc_mac,String date);
	public List<StateDay> getList(String date);
	public List<StateDay> getMacList(String lc_mac);
	public List<StateDay> getMonthList(String date);
	public List<StateDay> getYearList(String date);
	public List<StateDay> getWeekList(String date);
	public List<StateDay> getDayList(String date);
	public void saveOrUpdate(StateDay stateday);
	public void saveOrUpdate(List<StateDay> stateday);
	public void delete(StateDay stateday);
	public void delete(List<StateDay> stateday);


	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for Member specific
	public StateDay get(String lc_mac);
	public List<StateDay> getListByStatus(String status);
}
