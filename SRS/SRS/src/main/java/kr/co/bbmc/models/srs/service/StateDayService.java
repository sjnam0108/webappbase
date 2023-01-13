package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.StateDay;


@Transactional
public interface StateDayService {

	// Common
	public void flush();
	
	



	//
	// for GridView Dao
	//
	// Common

	public StateDay getStateDayById(int id);
	public StateDay getStateDay(String lc_mac);
	public List<StateDay> getStateDayList(String lc_mac,String date);
	public List<StateDay> getStateDayList(String date);
	public List<StateDay> getMacStateDayList(String lc_mac);
	public List<StateDay> getMonthStateDayList(String date);
	public List<StateDay> getYearStateDayList(String date);
	public List<StateDay> getWeekStateDayList(String date);
	public List<StateDay> getDayStateDayList(String date);
	public void saveOrUpdate( StateDay stateday);
	public void saveOrUpdates(List<StateDay> Stateday);
	public void deleteStateDay(StateDay Stateday);
	public void deleteStateDays(List<StateDay> Stateday);


	// for Kendo Grid Remote Read
	public DataSourceResult getStatedayList(DataSourceRequest request);




	// for LocalCtrl specific
	public List<StateDay> getStatedayListByStatus(String status);


	
	
}
