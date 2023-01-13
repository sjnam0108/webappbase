package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;


@Transactional
@Service("srsService")
public class SrsServiceImpl implements SrsService {

	@Autowired
    private SessionFactory sessionFactory;
    

  
	
	@Autowired
	private MemberService memService;

    
	@Override
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
    
	





}
