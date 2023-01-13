package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.StateTime;
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.dao.StateTimeDao;


@Transactional
@Service("StateTimeService")
public class StateTimeServiceImpl implements StateTimeService {

    @Autowired
    private SessionFactory sessionFactory;
    
    
    @Autowired
    private StateTimeDao stateTimeDao;

    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
   





	//
	// for StateTime Dao
	//
	
	
	
	
	@Override
	public StateTime getStateTime(String lc_mac) {
		// TODO Auto-generated method stub
		return stateTimeDao.get(lc_mac);
	}
	
	@Override
	public StateTime getStateTimeById(int id) {
		// TODO Auto-generated method stub
		return stateTimeDao.get(id);
	}


	@Override
	public List<StateTime> getStateTimeList(String lc_mac,String date) {
		// TODO Auto-generated method stub
		return stateTimeDao.getList(lc_mac,date);
	}
	
	@Override
	public List<StateTime> getStateTimeList(String lc_mac) {
		// TODO Auto-generated method stub
		return stateTimeDao.getList(lc_mac);
	}
	@Override
	public void saveOrUpdate(StateTime StateTime) {
		// TODO Auto-generated method stub
		stateTimeDao.saveOrUpdate(StateTime);
	}
	
	@Override
	public void saveOrUpdates(List<StateTime> StateTimes) {
		// TODO Auto-generated method stub
		stateTimeDao.saveOrUpdate(StateTimes);
	}

	@Override
	public void deleteStateTime(StateTime StateTime) {
		// TODO Auto-generated method stub
		stateTimeDao.delete(StateTime);
	}

	@Override
	public void deleteStateTimes(List<StateTime> StateTimes) {
		// TODO Auto-generated method stub
		stateTimeDao.delete(StateTimes);
	}


	@Override
	public DataSourceResult getStateTimeList(DataSourceRequest request) {
		// TODO Auto-generated method stub
		return stateTimeDao.getList(request);
	}





	@Override
	public List<StateTime> getStateTimeListByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}







}
