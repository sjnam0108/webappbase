package kr.co.bbmc.models.srs.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.LcSr;
import kr.co.bbmc.models.srs.FreezPredic;

public interface FreezPredicDao {

	// Common
	public FreezPredic get(int id);
	public List<FreezPredic> getList();
	public List<FreezPredic> getList(String lc_name);
	public void saveOrUpdate(FreezPredic FreezPredic);
	public void delete(FreezPredic FreezPredic);
	public void delete(List<FreezPredic> FreezPredic);

	// for Kendo Grid Remote Read
	public DataSourceResult getListFreezPredic(DataSourceRequest request);
	public DataSourceResult getList(DataSourceRequest request, 
			boolean isEffectiveMode, boolean isPlusReviewMode);

	// for Member specific
	public FreezPredic get(String lc_name);
	public List<FreezPredic> getListByStatus(String status);
	public List<FreezPredic> getSelectOption();
}
