package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.StateDay;
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.dao.StateDayDao;


@Transactional
@Service("StatedayService")
public class StateDayServiceImpl implements StateDayService {

    @Autowired
    private SessionFactory sessionFactory;
    

    
    @Autowired
    private StateDayDao stateDayDao;

    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
   




	//
	// for Stateday Dao
	//
	
	
	
	
	@Override
	public StateDay getStateDay(String lc_mac) {
		// TODO Auto-generated method stub
		return stateDayDao.get(lc_mac);
	}
	
	@Override
	public StateDay getStateDayById(int id) {
		// TODO Auto-generated method stub
		return stateDayDao.get(id);
	}


	@Override
	public List<StateDay> getStateDayList(String lc_mac,String date) {
		// TODO Auto-generated method stub
		return stateDayDao.getList(lc_mac,date);
	}
	
	@Override
	public List<StateDay> getStateDayList(String date) {
		// TODO Auto-generated method stub
		return stateDayDao.getList(date);
	}
	
	@Override
	public List<StateDay> getMacStateDayList(String lc_mac) {
		// TODO Auto-generated method stub
		return stateDayDao.getMacList(lc_mac);
	}

	@Override
	public List<StateDay> getMonthStateDayList(String date) {
		// TODO Auto-generated method stub
		return stateDayDao.getMonthList(date);
	}
	@Override
	public List<StateDay> getYearStateDayList(String date) {
		// TODO Auto-generated method stub
		return stateDayDao.getYearList(date);
	}
	@Override
	public List<StateDay> getWeekStateDayList(String date) {
		// TODO Auto-generated method stub
		return stateDayDao.getWeekList(date);
	}
	@Override
	public List<StateDay> getDayStateDayList(String date) {
		// TODO Auto-generated method stub
		return stateDayDao.getDayList(date);
	}
	@Override
	public void saveOrUpdate(StateDay Stateday) {
		// TODO Auto-generated method stub
		stateDayDao.saveOrUpdate(Stateday);
	}
	
	@Override
	public void saveOrUpdates(List<StateDay> Statedays) {
		// TODO Auto-generated method stub
		stateDayDao.saveOrUpdate(Statedays);
	}

	@Override
	public void deleteStateDay(StateDay Stateday) {
		// TODO Auto-generated method stub
		stateDayDao.delete(Stateday);
	}

	@Override
	public void deleteStateDays(List<StateDay> Statedays) {
		// TODO Auto-generated method stub
		stateDayDao.delete(Statedays);
	}


	@Override
	public DataSourceResult getStatedayList(DataSourceRequest request) {
		// TODO Auto-generated method stub
		return stateDayDao.getList(request);
	}





	@Override
	public List<StateDay> getStatedayListByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}







}
