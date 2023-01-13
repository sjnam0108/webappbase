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
import kr.co.bbmc.models.srs.dao.LcSrDao;


@Transactional
@Service("LcSrService")
public class LcSrServiceImpl implements LcSrService {

    @Autowired
    private SessionFactory sessionFactory;
    

    
    @Autowired
    private LcSrDao gridViewDao;

    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
   





	//
	// for LcSr Dao
	//
	
	
	
	
	@Override
	public LcSr getLcSr(String lc_mac) {
		// TODO Auto-generated method stub
		return gridViewDao.get(lc_mac);
	}
	@Override
	public LcSr getLcSr(String lc_mac,String sr_no) {
		// TODO Auto-generated method stub
		return gridViewDao.get(lc_mac,sr_no);
	}
	
	@Override
	public LcSr getLcSrById(int id) {
		// TODO Auto-generated method stub
		return gridViewDao.get(id);
	}


	@Override
	public List<LcSr> getLcSrList(String lc_mac) {
		// TODO Auto-generated method stub
		return gridViewDao.getList(lc_mac);
	}
	@Override
	public void saveOrUpdate(LcSr lcSr) {
		// TODO Auto-generated method stub
		gridViewDao.saveOrUpdate(lcSr);
	}
	
	@Override
	public void saveOrUpdates(List<LcSr> lcSrs) {
		// TODO Auto-generated method stub
		gridViewDao.saveOrUpdate(lcSrs);
	}

	@Override
	public void deleteLcSr(LcSr lcSr) {
		// TODO Auto-generated method stub
		gridViewDao.delete(lcSr);
	}

	@Override
	public void deleteLcSrs(List<LcSr> lcSrs) {
		// TODO Auto-generated method stub
		gridViewDao.delete(lcSrs);
	}


	@Override
	public DataSourceResult getLcSrList(DataSourceRequest request) {
		// TODO Auto-generated method stub
		return gridViewDao.getList(request);
	}

	@Override
	public DataSourceResult getLcSrList(DataSourceRequest request, String sr_group) {
		// TODO Auto-generated method stub
		return gridViewDao.getListGroup(request,sr_group);
	}




	@Override
	public List<LcSr> getLcSrListByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}







}
