package kr.co.bbmc.models.srs.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.LcSr;


public interface LcSrDao {

	// Common
	public LcSr get(int id);
	public List<LcSr> getList(String lc_mac);
	public void saveOrUpdate(LcSr lcSr);
	public void saveOrUpdate(List<LcSr> lcSr);
	public void delete(LcSr lcSr);
	public void delete(List<LcSr> lcSr);


	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);
	public DataSourceResult getListGroup(DataSourceRequest request,String sr_group);

	// for Member specific
	public LcSr get(String lc_mac);
	public LcSr get(String lc_mac,String sr_no);
	public List<LcSr> getListByStatus(String status);
}
