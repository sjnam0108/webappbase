package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.AutoCtrl;
import kr.co.bbmc.models.srs.NoticeEnroll;
import kr.co.bbmc.models.srs.dao.AutoCtrlDao;


@Transactional
@Service("AutoCtrlService")
public class AutoCtrlServiceImpl implements AutoCtrlService {

    @Autowired
    private SessionFactory sessionFactory;
    

 

    
    @Autowired
    private AutoCtrlDao autoCtrlDao;

    
	@Override
	public void flush() {

		sessionFactory.getCurrentSession().flush();
	}
   


	//
	// for AutoCtrl Dao
	//
	
	
	
	@Override
	public AutoCtrl getAutoCtrl(int id) {
		// TODO Auto-generated method stub
		return autoCtrlDao.get(id);
	}



	@Override
	public List<AutoCtrl> getAutoCtrlList() {
		// TODO Auto-generated method stub
		return autoCtrlDao.getList();
	}

	@Override
	public List<AutoCtrl> getAutoCtrlList(String lc_name) {
		// TODO Auto-generated method stub
		return autoCtrlDao.getListLcName(lc_name);
	}
	

	@Override
	public void saveOrUpdate(AutoCtrl autoCtrl) {
		// TODO Auto-generated method stub
		autoCtrlDao.saveOrUpdate(autoCtrl);
	}

	@Override
	public void deleteAutoCtrl(AutoCtrl autoCtrl) {
		// TODO Auto-generated method stub
		autoCtrlDao.delete(autoCtrl);
	}




	@Override
	public void deleteAutoCtrls(List<AutoCtrl> autoCtrls) {
		// TODO Auto-generated method stub
		autoCtrlDao.delete(autoCtrls);
	}


	@Override
	public DataSourceResult getAutoCtrlList(DataSourceRequest request) {
		// TODO Auto-generated method stub
		return autoCtrlDao.getList(request);
	}



	@Override
	public List<AutoCtrl> getAutoCtrlListByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void saveOrUpdates(List<AutoCtrl> autoCtrl) {
		// TODO Auto-generated method stub
		
	}





}
