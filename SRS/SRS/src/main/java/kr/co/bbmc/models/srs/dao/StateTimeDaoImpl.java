package kr.co.bbmc.models.srs.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.StateTime;




@Transactional
@Component
public class StateTimeDaoImpl implements StateTimeDao {

    @Autowired
    private SessionFactory sessionFactory;

    
	@Override
	public StateTime get(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StateTime> list = session.createCriteria(StateTime.class)
				.add(Restrictions.eq("id", id)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}


	@Override
	public List<StateTime> getList(String lc_mac,String date) {
		
		Session session = sessionFactory.getCurrentSession();

		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");


		Date dateNew = null;
		Date yDate = null;
		Date tDate = null;
		try {
			dateNew = transFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar cal = new GregorianCalendar(Locale.KOREA);
		cal.setTime(dateNew);
		cal.add(Calendar.DATE, -1);
		String y_date = transFormat.format(cal.getTime());
		cal.setTime(dateNew);
		cal.add(Calendar.DATE, +1);
		String t_date = transFormat.format(cal.getTime());
		
		SimpleDateFormat transFormatNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		y_date = y_date + " 23:59:59";
		t_date = t_date + " 00:00:00";
		try {
			yDate = transFormatNew.parse(y_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			tDate = transFormatNew.parse(t_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		@SuppressWarnings("unchecked")
		List<StateTime> list = session.createCriteria(StateTime.class)
				.add(Restrictions.eq("lc_mac", lc_mac))
				.add(Restrictions.lt("whoCreationDate", tDate))
				.add(Restrictions.gt("whoCreationDate", yDate)).list();

		
		return list;
	}



	
	
	
	@Override
	public List<StateTime> getList(String lc_mac) {
		
		Session session = sessionFactory.getCurrentSession();

		@SuppressWarnings("unchecked")
		List<StateTime> list = session.createCriteria(StateTime.class)
				.add(Restrictions.eq("lc_mac", lc_mac)).list();
		
		return list;
	}

	@Override
	public DataSourceResult getList(DataSourceRequest request) {
       	String lc_mac = (String)request.getData().get("lc_mac");
        return request.toDataSourceResult(sessionFactory.getCurrentSession(), StateTime.class, lc_mac);
	}
	

	
	@Override
	public StateTime get(String lc_mac) {
		
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<StateTime> list = session.createCriteria(StateTime.class)
				.add(Restrictions.eq("lc_mac", lc_mac)).list();
		
		return (list.isEmpty() ? null : list.get(0));
	}

	@Override
	public void saveOrUpdate(StateTime StateTime) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(StateTime);
	}
	
	@Override
	public void saveOrUpdate(List<StateTime> StateTimes) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (StateTime StateTime : StateTimes) {
        	session.saveOrUpdate(StateTime);

        }
	}

	@Override
	public void delete(StateTime StateTime) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.delete(session.load(StateTime.class, StateTime.getId()));
	}

	@Override
	public void delete(List<StateTime> StateTimes) {
		
		Session session = sessionFactory.getCurrentSession();
		
        for (StateTime StateTime : StateTimes) {
            session.delete(session.load(StateTime.class, StateTime.getId()));
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StateTime> getListByStatus(String status) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return session.createCriteria(StateTime.class)
				.add(Restrictions.eq("status", status)).list();
	}
    
}
