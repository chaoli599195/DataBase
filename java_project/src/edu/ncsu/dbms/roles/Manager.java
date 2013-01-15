package edu.ncsu.dbms.roles;

import java.sql.CallableStatement;

import java.sql.Connection;

import java.sql.Date;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import edu.ncsu.dbms.constants.DBMSFieldsEnum;
import edu.ncsu.dbms.dbutil.DBMSUtils;

import edu.ncsu.dbms.exception.CustomDBMSException;

import edu.ncsu.dbms.logger.DBMSLogger;

public class Manager extends Staff {

	private String addStaffQueryString = "begin Insert_Staff(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); end;";

	private String updateStaffQueryString = "begin Update_Staff(?, ?, ?, ?, ?, ?, ?,?,?,?,?); end;";

	private String deleteStaffQueryString = "begin Delete_staff_with_name(?,?); end;";

	private DBMSLogger logger = DBMSLogger.getInstance();

	public Manager(int staffId) throws CustomDBMSException {
		super(staffId);
		// TODO Auto-generated constructor stub
	}

	public boolean addStaff(String name, long ssn, String jobTitle,
			String gender, Date DOB, String address, long phoneNumber,
			float salary, String depname, int storeid, int managerId)
			throws CustomDBMSException {
		Connection dbConnection = dbConnectionUtil.getConnection();

		CallableStatement insertStaffStatement = null;

		boolean success = true;

		logger.debug("Adding a new row in the staff table with the name "

		+ name + " and phone number " + phoneNumber);

		try {

			// Get the prepared statement to call the procedure

			insertStaffStatement = dbConnection
					.prepareCall(addStaffQueryString);

			// System.out.println(addStaffQueryString);

			insertStaffStatement.setLong(1, ssn);

			insertStaffStatement.setString(2, name);

			insertStaffStatement.setString(3, jobTitle);

			insertStaffStatement.setString(4, gender);

			insertStaffStatement.setDate(5, DOB);

			insertStaffStatement.setString(6, address);

			insertStaffStatement.setLong(7, phoneNumber);

			insertStaffStatement.setFloat(8, salary);

			insertStaffStatement.setString(9, depname);

			insertStaffStatement.setInt(10, storeid);

			insertStaffStatement.setInt(11, managerId);

			// System.out.println(insertStaffStatement.toString());

			insertStaffStatement.execute();

			/*
			 * // Verify if the passed bank account exists
			 * 
			 * if (DBMSUtils.verifyBankAccount(bankAccountNumber)) {
			 * 
			 * // The passed bank account is valid. Commit the transaction
			 * 
			 * logger.debug("Vendor succesfully added");
			 * 
			 * dbConnection.commit();
			 * 
			 * } else {
			 * 
			 * // The passed bank account is invalid. Rollback the transaction
			 * 
			 * logger.warn("The credit card verification failed. Rolling back..."
			 * );
			 * 
			 * dbConnection.rollback();
			 * 
			 * success = false;
			 * 
			 * }
			 */

			insertStaffStatement.close();
			dbConnection.commit();
			dbConnectionUtil.closeConnection(dbConnection);

		} catch (SQLException e) {

			throw new CustomDBMSException("Got an sql exception...", e);

		}

		return success;
	}

	// //////

	public boolean updateStaff(int staffid, String name, long ssn,
			String jobTitle, String gender, Date DOB, String address,
			long phoneNumber, float salary, String depname, int storeid,
			int managerId) throws CustomDBMSException {

		Connection dbConnection = dbConnectionUtil.getConnection();

		CallableStatement updateStaffStatement = null;

		boolean success = true;

		logger.debug("Updating the row in the staff table with the name "

		+ name + " and phone number " + phoneNumber);

		try {

			// Get the prepared statement to call the procedure

			updateStaffStatement = dbConnection

			.prepareCall(updateStaffQueryString);

			// Set the parameters

			updateStaffStatement.setInt(1, staffid);

			updateStaffStatement.setLong(2, ssn);

			updateStaffStatement.setString(3, name);

			updateStaffStatement.setString(4, jobTitle);

			updateStaffStatement.setString(5, gender);

			updateStaffStatement.setDate(6, DOB);

			updateStaffStatement.setString(7, address);

			updateStaffStatement.setLong(8, phoneNumber);

			updateStaffStatement.setFloat(9, salary);

			updateStaffStatement.setString(10, depname);

			updateStaffStatement.setInt(11, managerId);

			updateStaffStatement.execute();

			/*
			 * // Verify if the passed bank account exists
			 * 
			 * if (DBMSUtils.verifyBankAccount(bankAccountNumber)) {
			 * 
			 * // The passed bank account is valid. Commit the transaction
			 * 
			 * dbConnection.commit();
			 * 
			 * logger.debug("Vendor succesfully updated");
			 * 
			 * } else {
			 * 
			 * // The passed bank account is invalid. Rollback the transaction
			 * 
			 * logger.warn("The credit card verification failed. Rolling back..."
			 * );
			 * 
			 * dbConnection.rollback();
			 * 
			 * success = false;
			 * 
			 * }
			 */

			updateStaffStatement.close();
			dbConnection.commit();
			dbConnectionUtil.closeConnection(dbConnection);

		} catch (SQLException e) {

			throw new CustomDBMSException("Got an sql exception...", e);

		}

		return success;

	}

	// ///
	// Delete_staff_with_name(name_in IN staff.name%TYPE, address_in IN
	// staff.address%TYPE)

	public boolean deleteStaff(String name, String address)
			throws CustomDBMSException {

		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement deleteStaffStatement = null;
		boolean success = true;

		logger.debug("Deleting the row in the staff table with the name "
				+ name + " and phone number " + address);

		try {
			// Get the prepared statement to call the procedure
			deleteStaffStatement = dbConnection
					.prepareCall(deleteStaffQueryString);

			// Set the parameters
			deleteStaffStatement.setString(1, name);
			deleteStaffStatement.setString(2, address);

			deleteStaffStatement.execute();
			dbConnection.commit();
			logger.debug("Vendor succesfully deleted");
			deleteStaffStatement.close();

		} catch (SQLException e) {
			throw new CustomDBMSException("Got an sql exception...", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}

		return success;

	}

	public void addStaffCLI() throws CustomDBMSException {

		String nameString = null, ssnString = null, jobTitleString = null;
		String genderString = null, DOBString = null;
		String salaryString = null, addressString = null;
		String phoneNumberString = null, depnameString = null, storeidString = null, manageridString = null;

		nameString = DBMSUtils.promptInputFromCLI(
				"Enter the name of the Staff", DBMSFieldsEnum.NAME);
		ssnString = DBMSUtils.promptInputFromCLI("Enter the ssn of the Staff",
				DBMSFieldsEnum.SSN);
		jobTitleString = DBMSUtils.promptInputFromCLI(
				"Enter the job title of the Staff", DBMSFieldsEnum.NAME);
		genderString = DBMSUtils.promptInputFromCLI(
				"Enter the gender of the Staff", DBMSFieldsEnum.GENDER);
		DOBString = DBMSUtils.promptInputFromCLI("Enter the DOB of the Staff",
				DBMSFieldsEnum.DATE);
		salaryString = DBMSUtils.promptInputFromCLI(
				"Enter the salary of the Staff", DBMSFieldsEnum.FLOAT);
		addressString = DBMSUtils.promptInputFromCLI(
				"Enter the address of the Staff", DBMSFieldsEnum.ADDRESS);
		phoneNumberString = DBMSUtils.promptInputFromCLI(
				"Enter the phone_number of the Staff",
				DBMSFieldsEnum.PHONE_NUMBER);
		depnameString = DBMSUtils.promptInputFromCLI(
				"Enter the department_name  of the Staff", DBMSFieldsEnum.NAME);
		storeidString = DBMSUtils.promptInputFromCLI(
				"Enter the Store_id of the Staff", DBMSFieldsEnum.INT);
		manageridString = DBMSUtils.promptInputFromCLI(
				"Enter the Manager_id of the Staff", DBMSFieldsEnum.INT);

		addStaff(nameString, Long.parseLong(ssnString), jobTitleString,
				genderString, DBMSUtils.getSqlDate(DOBString), addressString,
				Long.parseLong(phoneNumberString),
				Float.parseFloat(salaryString), depnameString,
				Integer.parseInt(storeidString),
				Integer.parseInt(manageridString));

	}

	public void updateStaffCLI() throws CustomDBMSException {

		String staffidString = null;
		String nameString = null, ssnString = null, jobTitleString = null;
		String genderString = null, DOBString = null;
		String salaryString = null, addressString = null;
		String phoneNumberString = null, depnameString = null, storeidString = null, manageridString = null;

		staffidString = DBMSUtils.promptInputFromCLI(
				"Enter the id of the Staff", DBMSFieldsEnum.INT);

		nameString = DBMSUtils.promptInputFromCLI(
				"Enter the name of the Staff", DBMSFieldsEnum.NAME);
		ssnString = DBMSUtils.promptInputFromCLI("Enter the ssn of the Staff",
				DBMSFieldsEnum.SSN);
		jobTitleString = DBMSUtils.promptInputFromCLI(
				"Enter the job title of the Staff", DBMSFieldsEnum.NAME);
		genderString = DBMSUtils.promptInputFromCLI(
				"Enter the gender of the Staff", DBMSFieldsEnum.GENDER);
		DOBString = DBMSUtils.promptInputFromCLI("Enter the DOB of the Staff",
				DBMSFieldsEnum.DATE);
		salaryString = DBMSUtils.promptInputFromCLI(
				"Enter the salary of the Staff", DBMSFieldsEnum.FLOAT);
		addressString = DBMSUtils.promptInputFromCLI(
				"Enter the address of the Staff", DBMSFieldsEnum.ADDRESS);
		phoneNumberString = DBMSUtils.promptInputFromCLI(
				"Enter the phone_number of the Staff",
				DBMSFieldsEnum.PHONE_NUMBER);
		depnameString = DBMSUtils.promptInputFromCLI(
				"Enter the department_name  of the Staff", DBMSFieldsEnum.NAME);
		storeidString = DBMSUtils.promptInputFromCLI(
				"Enter the Store_id of the Staff", DBMSFieldsEnum.INT);
		manageridString = DBMSUtils.promptInputFromCLI(
				"Enter the Manager_id of the Staff", DBMSFieldsEnum.INT);

		updateStaff(Integer.parseInt(staffidString), nameString,
				Long.parseLong(ssnString), jobTitleString, genderString,
				DBMSUtils.getSqlDate(DOBString), addressString,
				Long.parseLong(phoneNumberString),
				Float.parseFloat(salaryString), depnameString,
				Integer.parseInt(storeidString),
				Integer.parseInt(manageridString));

	}

	public void deleteStaffCLI() throws CustomDBMSException {

		String nameString = null, addressString = null;

		nameString = DBMSUtils.promptInputFromCLI(
				"Enter the name of the Staff", DBMSFieldsEnum.NAME);

		addressString = DBMSUtils.promptInputFromCLI(
				"Enter the address of the Staff", DBMSFieldsEnum.ADDRESS);

		deleteStaff(nameString, addressString);

	}

	public void finalCLI() throws CustomDBMSException {

		String menuChoice = null;
		boolean done = false;

		while (!done) {
			DBMSUtils.printOnCLI("Choose the action");
			DBMSUtils.printOnCLI("1 add Staff");
			DBMSUtils.printOnCLI("2 Update Staff");
			DBMSUtils.printOnCLI("3 Delete Staff");
			menuChoice = DBMSUtils.promptInputFromCLI("Enter your choice",
					DBMSFieldsEnum.INT);
			switch (Integer.parseInt(menuChoice)) {
			case 1:
				addStaffCLI();
				done = true;
				break;
			case 2:
				updateStaffCLI();
				done = true;
				break;
			case 3:
				deleteStaffCLI();
				done = true;
				break;
			default:
				DBMSUtils.printOnCLI("Invalid choice. Please try again");
				break;
			}

		}

	}

	public static void main(String[] args) throws Exception {

		Manager c = new Manager(1);

		try {
			// c.addStaff("lich2",392821398L,"M-Unit-99","M",DBMSUtils.getSqlDate("01/11/2012"),"3521 ivy commons",9199618613L,100000,"Management",1,1);
			// c.updateStaff(15, "lich2", 3928212356L, "leader", "M",
			// DBMSUtils.getSqlDate("01/11/2012"), "heaven", 9199618613L,
			// 100000, "Management", 1, 1);
			// c.deleteStaff("lich2", "3521 ivy commons");
			c.finalCLI();
		} catch (CustomDBMSException e) {
			e.printStackTrace();
		}

	}

}
