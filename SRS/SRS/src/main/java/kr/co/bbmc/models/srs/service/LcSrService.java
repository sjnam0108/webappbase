package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.LcSr;


@Transactional
public interface LcSrService {

	// Common
	public void flush();
	
	



	//
	// for GridView Dao
	//
	// Common
	public LcSr getLcSrById(int id);
	public LcSr getLcSr(String lc_mac);
	public LcSr getLcSr(String lc_mac,String sr_no);
	public List<LcSr> getLcSrList(String lc_mac);
	public void saveOrUpdate(LcSr lcSr);
	public void saveOrUpdates(List<LcSr> lcSr);
	public void deleteLcSr(LcSr lcSr);
	public void deleteLcSrs(List<LcSr> lcSr);


	// for Kendo Grid Remote Read
	public DataSourceResult getLcSrList(DataSourceRequest request);
	public DataSourceResult getLcSrList(DataSourceRequest request,String sr_group);



	// for LocalCtrl specific
	public List<LcSr> getLcSrListByStatus(String status);



	
	
}
