package kr.co.bbmc.models.fnd.dao;

import java.util.List;

import kr.co.bbmc.models.DataSourceRequest;
import kr.co.bbmc.models.DataSourceResult;
import kr.co.bbmc.models.fnd.UserPrivilege;

public interface UserPrivilegeDao {
	// Common
	public UserPrivilege get(int id);
	public List<UserPrivilege> getList();
	public void saveOrUpdate(UserPrivilege userPrivilege);
	public void delete(UserPrivilege userPrivilege);
	public void delete(List<UserPrivilege> userPrivileges);
	public int getCount();

	// for Kendo Grid Remote Read
	public DataSourceResult getList(DataSourceRequest request);

	// for UserPrivilege specific
	public boolean isRegistered(int userId, int privilegeId);
	public void saveOrUpdate(List<UserPrivilege> userPrivileges);
	public List<UserPrivilege> getListByUserId(int userId);
}
