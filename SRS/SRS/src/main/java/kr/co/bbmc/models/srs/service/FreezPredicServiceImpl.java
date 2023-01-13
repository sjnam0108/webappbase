package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;


import kr.co.bbmc.models.srs.FreezPredic;
import kr.co.bbmc.models.srs.dao.FreezPredicDao;



@Transactional
@Service("FreezPredicService")
public class FreezPredicServiceImpl implements FreezPredicService {

    @Autowired
    private SessionFactory sessionFactory;
    


    
    @Autowired
    private FreezPredicDao FreezPredicDao;

    
	@Override
	public void flush() {

		sessionFactory.getCurrentSession().flush();
	}
   







	//
	// for FreezPredic Dao
	//
	
	
	@Override
	public List<FreezPredic> getFreezPredicList(String lc_name) {
		// TODO Auto-generated method stub
		return FreezPredicDao.getList(lc_name);
	}
	
	@Override
	public FreezPredic getFreezPredic(int id) {
		// TODO Auto-generated method stub
		return FreezPredicDao.get(id);
	}



	@Override
	public List<FreezPredic> getFreezPredicList() {
		// TODO Auto-generated method stub
		return FreezPredicDao.getList();
	}



	@Override
	public void saveOrUpdate(FreezPredic FreezPredic) {
		// TODO Auto-generated method stub
		FreezPredicDao.saveOrUpdate(FreezPredic);
	}

	@Override
	public void deleteFreezPredic(FreezPredic localCtrl) {
		// TODO Auto-generated method stub
		FreezPredicDao.delete(localCtrl);
	}




	@Override
	public void deleteFreezPredics(List<FreezPredic> localCtrls) {
		// TODO Auto-generated method stub
		FreezPredicDao.delete(localCtrls);
	}


	@Override
	public DataSourceResult getFreezPredicList(DataSourceRequest request) {
		// TODO Auto-generated method stub
		return FreezPredicDao.getListFreezPredic(request);
	}



	@Override
	public FreezPredic getFreezPredic(String lc_name) {
		// TODO Auto-generated method stub
		return FreezPredicDao.get(lc_name);
	}



	@Override
	public List<FreezPredic> getFreezPredicListByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<FreezPredic> getSelectOption() {
		// TODO Auto-generated method stub
		return FreezPredicDao.getSelectOption();
	}



}
