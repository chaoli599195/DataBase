package edu.ncsu.dbms.roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import edu.ncsu.dbms.dbutil.DBMSConnectionUtil;
import edu.ncsu.dbms.exception.CustomDBMSException;

/**
 * This is the super class for all staff entities. Different staff have
 * different roles. The common tasks are present in this class
 */

public class Staff {

	private String getAllStaffQuery = "select Staff.StaffId, StoreId, Department.Name from Staff, works_in, member_of, department where works_in.staffid = staff.staffid and staff.staffid = member_of.staffid and department.deptid = member_of.deptid and staff.staffid = ?";
	
	protected int staffId;
	protected String departmentName;
	protected int storeId;

	protected DBMSConnectionUtil dbConnectionUtil = null;

	public void finalCLI()throws CustomDBMSException{
		
	}
	
	public DBMSConnectionUtil getDbConnectionUtil() {
		return dbConnectionUtil;
	}

	public void setDbConnectionUtil(DBMSConnectionUtil dbConnectionUtil) {
		this.dbConnectionUtil = dbConnectionUtil;
	}

	/**
	 * Constructor
	 * 
	 * @param staffId
	 * @throws CustomDBMSException
	 */
	public Staff(int staffId) throws CustomDBMSException {

		this.staffId = staffId;
		dbConnectionUtil = DBMSConnectionUtil.getInstance();
		if (!staffExists(staffId)) {
			throw new CustomDBMSException(
					"Could not find staff for the staff ID " + staffId);
		}

	}

	/**
	 * Verifies if a staff person exists with the passed staff id
	 * 
	 * @param staffId
	 * @return boolean
	 * @throws CustomDBMSException
	 */
	public boolean staffExists(int staffId) throws CustomDBMSException {

		PreparedStatement getStaffFromIdStatement = null;
		ResultSet resultSet = null;
		boolean foundStaff = false;

		Connection dbConnection = dbConnectionUtil.getConnection();
		try {
			getStaffFromIdStatement = dbConnection
					.prepareStatement(getAllStaffQuery);
			getStaffFromIdStatement.setInt(1, staffId);
			resultSet = getStaffFromIdStatement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				foundStaff = true;
				this.staffId = resultSet.getInt(1);
				storeId = resultSet.getInt(2);
				departmentName = resultSet.getString(3);
				resultSet.close();
			}
			getStaffFromIdStatement.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an SQL error while getting the staff details for id "
							+ staffId, e);
		} finally {
			this.dbConnectionUtil.closeConnection(dbConnection);
		}
		return foundStaff;
	}

	public static void main(String[] args) throws Exception {

		Staff s = new Staff(4);

	}

}
