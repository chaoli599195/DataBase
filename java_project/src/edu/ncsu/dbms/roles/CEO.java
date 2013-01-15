package edu.ncsu.dbms.roles;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.*;

import edu.ncsu.dbms.constants.DBMSFieldsEnum;
import edu.ncsu.dbms.dbutil.DBMSUtils;
import edu.ncsu.dbms.exception.CustomDBMSException;
import edu.ncsu.dbms.logger.DBMSLogger;

/*
 * This class is for all operations done by CEO. Generating reports across all stores of bookstore chain
 * can only be done by CEO
 */
public class CEO extends Staff {
	private String generateCustomerReportString = "begin generate_cust_purchase_hist(?, ?, ?, ?, ?); end;";
	
	private String generateSalesAssistedReportString = "begin generate_sales_assisted(?, ?, ?, ?, ?); end;";

	private String generateVendorsString = "begin generate_vendors(?); end;";

	private String generateStaffReportForDeptString = "begin generate_staff_with_dept(?, ?); end;";
	
	private String generateStaffSalReportString = "begin generate_sal_report_dept(?, ?); end;";

	private DBMSLogger logger = DBMSLogger.getInstance();

	/**
	 * Constructor that takes the staff Id
	 * 
	 * @param staffId
	 * @throws CustomDBMSException
	 */
	public CEO(int staffId) throws CustomDBMSException {

		super(staffId);

	}

	/**
	 * CLI function for getting sales assited report
	 * @throws CustomDBMSException
	 */
	public void generateSalesAssistedReportCli() throws CustomDBMSException
	{
		String name = null, address = null;
		String startDateString = null, endDateString = null;

		try {
			name = DBMSUtils.promptInputFromCLI("Enter the name of the SalesPerson",
					DBMSFieldsEnum.NAME);
				address = DBMSUtils.promptInputFromCLI(
				"Enter the address of the SalesPerson", DBMSFieldsEnum.ADDRESS);
		startDateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the start date in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);
		endDateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the end date in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);

		generateSalesAssistedReport(name, address,
				DBMSUtils.getSqlDate(startDateString),
				DBMSUtils.getSqlDate(endDateString));
		} catch (CustomDBMSException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
			
		}
		
	}
	
	/**
	 * Func for generating sales assisted report
	 * @param name
	 * @param address
	 * @param start_date
	 * @param end_date
	 * @throws CustomDBMSException
	 */
	public void generateSalesAssistedReport(String name, String address,
			Date start_date, Date end_date) throws CustomDBMSException {
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement callStmt = null;

	
		try {
			callStmt = dbConnection.prepareCall(generateSalesAssistedReportString);

			callStmt.registerOutParameter(5, OracleTypes.CURSOR);
			callStmt.setString(1, name);
			callStmt.setString(2, address);
			callStmt.setDate(3, start_date);
			callStmt.setDate(4, end_date);

			callStmt.execute();

			// return the result set
			ResultSet rset = (ResultSet) callStmt.getObject(5);

		// print the results, all the columns in each row
			System.out
					.println("customerid\t name\t address\n");
			 while (rset.next()) {
				System.out.println(Long.toString(rset.getLong(1)) + "\t"
						+ rset.getString(2) + "\t" + rset.getString(3) + "\n" );
			} 
			rset.close();
			callStmt.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}
	}
	
	/**
	 * CLI func for generating Staff report grouped by dept name
	 * @throws CustomDBMSException
	 */
	public void generateStaffReportCli() throws CustomDBMSException {
		String name = null;

		try {
			name = DBMSUtils.promptInputFromCLI("Enter the dept name",
					DBMSFieldsEnum.NAME);
				
			generateStaffReport(name);
		} catch (CustomDBMSException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
			
		}
	}
	
	/**
	 * Func for generating staff report grouped by dept.
	 * @param dept_name
	 * @throws CustomDBMSException
	 */
	public void generateStaffReport(String dept_name) throws CustomDBMSException {
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement callStmt = null;
		CallableStatement callStmt1 = null;

	
		try {
			callStmt = dbConnection.prepareCall(generateStaffReportForDeptString);

			callStmt.registerOutParameter(2, OracleTypes.CURSOR);
			callStmt.setString(1, dept_name);
			
			callStmt.execute();

			// return the result set
			ResultSet rset = (ResultSet) callStmt.getObject(2);

		// print the results, all the columns in each row
			System.out
					.println("staffid\t ssn\t name\t dept_name\t job_title\t gender\t dob\t address\t phone_number\t salary\n");
			 while (rset.next()) {
				System.out.println(Long.toString(rset.getLong(1)) + "\t"
						+ Long.toString(rset.getLong(2)) + "\t" + rset.getString(3) + "\t"
				        + rset.getString(4) + "\t" + rset.getString(5) + "\t" + rset.getString(6) + "\t" 
				        + rset.getDate(7).toString() + "\t" + rset.getString(8) + "\t" 
				        + Long.toString(rset.getLong(9)) + "\t" + Float.toString(rset.getFloat(10)) + "\n" );
			} 
			rset.close();
			callStmt.close();
			
			callStmt1 = dbConnection.prepareCall(generateStaffSalReportString);

			callStmt1.registerOutParameter(2, OracleTypes.CURSOR);
			callStmt1.setString(1, dept_name);
			
			callStmt1.execute();

			// return the result set
			ResultSet rset1 = (ResultSet) callStmt1.getObject(2);

		// print the results, all the columns in each row
			System.out
					.println("Dept_name\t MINSalary\t MAXSalary\t ACGSalary\t Budget\n");
			 while (rset1.next()) {
				System.out.println((rset1.getString(1)) + "\t"
						+ Float.toString(rset1.getFloat(2)) + "\t" + Float.toString(rset1.getFloat(3)) + "\t"
				        + Float.toString(rset1.getFloat(4)) + "\t" + Float.toString(rset1.getFloat(5)) + "\n" );
			} 
			rset1.close();
			callStmt1.close();
			
			
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}
	}
	
	/**
	 * Function for generating vendor report. Vendors List is global and hence will be
	 * printing all vendors' details
	 * @throws CustomDBMSException
	 */
	public void generateVendorsReport() throws CustomDBMSException {
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement callStmt = null;

		try {
			callStmt = dbConnection.prepareCall(generateVendorsString);

			callStmt.registerOutParameter(1, OracleTypes.CURSOR);

			callStmt.execute();

			// return the result set
			ResultSet rset = (ResultSet) callStmt.getObject(1);

		// print the results, all the columns in each row
			System.out
					.println("VENDORID\t NAME\t ADDRESS\t PHONE\t CONTRACTSTARTDATE\t CONTRACTENDDATE\t BANKACCOUNT_NUMBER\n");
			 while (rset.next()) {
				System.out.println(Long.toString(rset.getLong(1)) + "\t"
						+ rset.getString(2) + "\t" + rset.getString(3) + "\t"
						+ Long.toString(rset.getLong(4)) + "\t"
						+ (rset.getDate(5)).toString() + "\t"
						+ rset.getDate(6).toString() + "\t"
						+ Long.toString(rset.getLong(7)) + "\n");

			} 
			rset.close();
			callStmt.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}
	}

	/**
	 * CLI function for Generating Customer report
	 * @throws CustomDBMSException
	 */
	public void generateCustomerReportCli() throws CustomDBMSException
	{
		String name = null, address = null;
		String startDateString = null, endDateString = null;

		try {
			name = DBMSUtils.promptInputFromCLI("Enter the name of the Customer",
					DBMSFieldsEnum.NAME);
				address = DBMSUtils.promptInputFromCLI(
				"Enter the address of the Customer", DBMSFieldsEnum.ADDRESS);
		startDateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the start date in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);
		endDateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the end date in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);

		generateCustomerReport(name, address,
				DBMSUtils.getSqlDate(startDateString),
				DBMSUtils.getSqlDate(endDateString));
		} catch (CustomDBMSException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
			
		}
		
	}
	
	/**
	 * Function for generating particular customer report. Name and address is used as
	 * unique key to identify a customer.
	 * @param name
	 * @param address
	 * @param start_date
	 * @param end_date
	 * @throws CustomDBMSException
	 */
	public void generateCustomerReport(String name, String address,
			Date start_date, Date end_date) throws CustomDBMSException {
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement callStmt = null;

		logger.debug("Generating customer report for " + name);
		try {
			callStmt = dbConnection.prepareCall(generateCustomerReportString);

			callStmt.registerOutParameter(5, OracleTypes.CURSOR);
			callStmt.setString(1, name);
			callStmt.setString(2, address);
			callStmt.setDate(3, start_date);
			callStmt.setDate(4, end_date);

			callStmt.execute();

			// return the result set
			ResultSet rset = (ResultSet) callStmt.getObject(5);

		// print the results, all the columns in each row
			System.out
					.println("customerid\t orderid\t name\t quantity\t amount\t date_ordered\n");
			 while (rset.next()) {
				System.out.println(Long.toString(rset.getLong(1)) + "\t"
						+ Long.toString(rset.getLong(2)) + "\t" + rset.getString(3) + "\t"
						+ Long.toString(rset.getLong(4)) + "\t"
						+ Float.toString(rset.getFloat(5)) + "\t"
						+ rset.getDate(6).toString() + "\n"	);
			} 
			rset.close();
			callStmt.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}
		
	}

	/**
	 * Main CLI function for this CEO class. 
	 */
	public void finalCLI() throws CustomDBMSException {

		String menuChoice = null;
		boolean done = false;

		while (!done) {
			DBMSUtils.printOnCLI("Choose the action");
			DBMSUtils.printOnCLI("1 Generate Vendor Report");
			DBMSUtils.printOnCLI("2 Generate Staff Report based on dept");
			DBMSUtils.printOnCLI("3 Generate Customer report(Purchase history)");
			DBMSUtils.printOnCLI("4 Generate SalesPerson assisted report");
			menuChoice = DBMSUtils.promptInputFromCLI("Enter your choice",
					DBMSFieldsEnum.INT);
			switch (Integer.parseInt(menuChoice)) {
			case 1:
				generateVendorsReport();
				done = true;
				break;
			case 2:
				generateStaffReportCli();
				done = true;
				break;
			case 3:
				generateCustomerReportCli();
				done = true;
				break;
			case 4:
				generateSalesAssistedReportCli();
				done = true;
				break;
			default:
				DBMSUtils.printOnCLI("Invalid choice. Please try again");
				break;
			}

		}

	}
	
	public static void main(String[] args) throws Exception{
		CEO c;
		try {
			c = new CEO(1);
		//	c.generateVendorsReport();
		//	c.generateCustomerReport("Bob", "101 Russet St.", DBMSUtils.getSqlDate("30/11/2012"), DBMSUtils.getSqlDate("2/12/2012"));
			c.generateStaffReport("Sales");
			//c.generateSalesAssistedReport("George Carlin", "54 Purple road", DBMSUtils.getSqlDate("30/11/2012"), DBMSUtils.getSqlDate("2/12/2012"));
			
		//	c.generateSalesAssistedReportCli();
		//	c.generateStaffReportCli();
		//  c.generateCustomerReportCli();
		//	c.finalCLI();
		} catch (CustomDBMSException e) {
			e.printStackTrace();
		}
	}
}
