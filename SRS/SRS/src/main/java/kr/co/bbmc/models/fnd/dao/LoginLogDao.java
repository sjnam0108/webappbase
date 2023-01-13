package kr.co.bbmc.models.fnd.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.fnd.LoginLog;

public interface LoginLogDao {
	// Common
	public LoginLog get(int id);
	public List<LoginLog> getList();
	public void saveOrUpdate(LoginLog loginLog);
	public void delete(LoginLog loginLog);
	public void delete(List<LoginLog> loginLogs);
	public int getCount();

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for LoginLog specific
	public LoginLog getLastByUserId(int userId);
}
