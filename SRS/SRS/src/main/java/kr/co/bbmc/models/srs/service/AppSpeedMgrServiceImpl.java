package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;


import kr.co.bbmc.models.srs.AppSpeedMgr;
import kr.co.bbmc.models.srs.dao.AppSpeedMgrDao;



@Transactional
@Service("AppSpeedMgrService")
public class AppSpeedMgrServiceImpl implements AppSpeedMgrService {

    @Autowired
    private SessionFactory sessionFactory;
    

    
    @Autowired
    private AppSpeedMgrDao AppSpeedMgrDao;

    
	@Override
	public void flush() {

		sessionFactory.getCurrentSession().flush();
	}
   




	//
	// for AppSpeedMgr Dao
	//
	
	
	@Override
	public List<AppSpeedMgr> getAppSpeedMgrList(String lc_name) {
		// TODO Auto-generated method stub
		return AppSpeedMgrDao.getList(lc_name);
	}
	
	@Override
	public AppSpeedMgr getAppSpeedMgr(int id) {
		// TODO Auto-generated method stub
		return AppSpeedMgrDao.get(id);
	}



	@Override
	public List<AppSpeedMgr> getAppSpeedMgrList() {
		// TODO Auto-generated method stub
		return AppSpeedMgrDao.getList();
	}



	@Override
	public void saveOrUpdate(AppSpeedMgr AppSpeedMgr) {
		// TODO Auto-generated method stub
		AppSpeedMgrDao.saveOrUpdate(AppSpeedMgr);
	}

	@Override
	public void deleteAppSpeedMgr(AppSpeedMgr localCtrl) {
		// TODO Auto-generated method stub
		AppSpeedMgrDao.delete(localCtrl);
	}




	@Override
	public void deleteAppSpeedMgrs(List<AppSpeedMgr> localCtrls) {
		// TODO Auto-generated method stub
		AppSpeedMgrDao.delete(localCtrls);
	}


	@Override
	public DataSourceResult getAppSpeedMgrList(DataSourceRequest request) {
		// TODO Auto-generated method stub
		return AppSpeedMgrDao.getListAppSpeedMgr(request);
	}



	@Override
	public AppSpeedMgr getAppSpeedMgr(String notice_name) {
		// TODO Auto-generated method stub
		return AppSpeedMgrDao.get(notice_name);
	}



	@Override
	public List<AppSpeedMgr> getAppSpeedMgrListByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<AppSpeedMgr> getSelectOption() {
		// TODO Auto-generated method stub
		return AppSpeedMgrDao.getSelectOption();
	}



}
