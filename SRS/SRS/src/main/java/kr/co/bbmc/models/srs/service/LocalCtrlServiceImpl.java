package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.LcSr;
import kr.co.bbmc.models.srs.LocalCtrl;
import kr.co.bbmc.models.srs.dao.LocalCtrlDao;


@Transactional
@Service("LocalCtrlService")
public class LocalCtrlServiceImpl implements LocalCtrlService {

    @Autowired
    private SessionFactory sessionFactory;
    
   
    @Autowired
    private LocalCtrlDao localCtrlDao;

    
	@Override
	public void flush() {

		sessionFactory.getCurrentSession().flush();
	}
   

	//
	// for LocalCtrl Dao
	//
	
	

	
	@Override
	public LocalCtrl getLocalCtrl(int id) {
		// TODO Auto-generated method stub
		return localCtrlDao.get(id);
	}



	@Override
	public List<LocalCtrl> getLocalCtrlList() {
		// TODO Auto-generated method stub
		return localCtrlDao.getList();
	}



	@Override
	public void saveOrUpdate(LocalCtrl localCtrl) {
		// TODO Auto-generated method stub
		localCtrlDao.saveOrUpdate(localCtrl);
	}

	@Override
	public void deleteLocalCtrl(LocalCtrl localCtrl) {
		// TODO Auto-generated method stub
		localCtrlDao.delete(localCtrl);
	}




	@Override
	public void deleteLocalCtrls(List<LocalCtrl> localCtrls) {
		// TODO Auto-generated method stub
		localCtrlDao.delete(localCtrls);
	}


	@Override
	public DataSourceResult getLocalCtrlList(DataSourceRequest request) {
		// TODO Auto-generated method stub
		return localCtrlDao.getList(request);
	}



	@Override
	public LocalCtrl getLocalCtrl(String lc_mac) {
		// TODO Auto-generated method stub
		return localCtrlDao.get(lc_mac);
	}



	@Override
	public List<LocalCtrl> getLocalCtrlListByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<LocalCtrl> getLocalStatecount() {
		// TODO Auto-generated method stub
		return localCtrlDao.getLocalStateCount();
	}



}
