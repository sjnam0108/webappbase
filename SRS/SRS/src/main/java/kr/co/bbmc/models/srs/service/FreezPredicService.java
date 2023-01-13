package kr.co.bbmc.models.srs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.srs.FreezPredic;

import kr.co.bbmc.models.srs.CntState;
import kr.co.bbmc.models.srs.LocalCtrl;



@Transactional
public interface FreezPredicService {

	// Common
	public void flush();
	

	//
	// for LocaCtrl Dao
	//
	// Common
	public FreezPredic getFreezPredic(int id);
	public List<FreezPredic> getFreezPredicList(String lc_name);
	public List<FreezPredic> getFreezPredicList();
	public List<FreezPredic> getSelectOption();
	public void saveOrUpdate(FreezPredic freezPredic);
	public void deleteFreezPredic(FreezPredic freezPredic);
	public void deleteFreezPredics(List<FreezPredic> freezPredics);

	// for Kendo Grid Remote Read
	public DataSourceResult getFreezPredicList(DataSourceRequest request);


	// for FreezPredic specific
	public FreezPredic getFreezPredic(String lc_name);
	public List<FreezPredic> getFreezPredicListByStatus(String status);



	
	
}
