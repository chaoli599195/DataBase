package edu.ncsu.dbms.roles;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

import edu.ncsu.dbms.constants.DBMSFieldsEnum;
import edu.ncsu.dbms.dbutil.DBMSUtils;
import edu.ncsu.dbms.exception.CustomDBMSException;

/* This class handles generating customer bill, vendor bill and when the bill is paid the date paid field is
 * updated.
 */
public class Accountant extends Staff {
	private String generateCustomerTotalBillString = "begin generate_customer_total_bill(?, ?, ?, ?, ?); end;";
	private String generateVendorTotalBillString = "begin generate_vendors_total_bill(?, ?, ?, ?, ?); end;";
	private String updateCustomerOrderDatePaidString = "begin update_order_date_paid(?, ?); end;";
	private String updateVendorOrderDatePaidString = "begin update_vend_purch_date_paid(?, ?); end;";
	

	/**
	 * Constructor that takes the staff Id
	 * 
	 * @param staffId
	 * @throws CustomDBMSException
	 */
	public Accountant(int staffId) throws CustomDBMSException {

		super(staffId);

	}
	
	/**
	 * CLI function for updating customer order.
	 * @throws CustomDBMSException
	 */
	public void updateCustomerOrderDatePaidCli() throws CustomDBMSException
	{
		String cust_id;
		String dateString = null;

		try {
			cust_id = DBMSUtils.promptInputFromCLI("Enter the customer id",
					DBMSFieldsEnum.INT);
			dateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the paid date in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);
			updateCustomerOrderDatePaid(Integer.parseInt(cust_id), DBMSUtils.getSqlDate(dateString));
		
		} catch (CustomDBMSException e) {
			throw new CustomDBMSException(
					"Got an sql exception during updateCustomerOrderDatePaid...", e);
			
		}		
	}
	/**
	 * Function for updating customer order date paid. Date is updated for all 
	 * pending order i.e., when the date_paid is null.
	 * @param cust_id
	 * @param date
	 * @throws CustomDBMSException
	 */
	public void updateCustomerOrderDatePaid(int cust_id, Date date ) throws CustomDBMSException
	{
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement callStmt = null;

	
		try {
			callStmt = dbConnection.prepareCall(updateCustomerOrderDatePaidString);

			callStmt.setInt(1, cust_id);
			callStmt.setDate(2, date);
						
			callStmt.execute();

			dbConnection.commit();
			callStmt.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
		} finally {
			
			dbConnectionUtil.closeConnection(dbConnection);
		}
	}
	
	/**
	 * CLI for updateVendorOrderDatePaid
	 * 
	 * @throws CustomDBMSException
	 */
	public void updateVendorOrderDatePaidCli() throws CustomDBMSException
	{
		String vendor_id;
		String dateString = null;

		try {
			vendor_id = DBMSUtils.promptInputFromCLI("Enter the vendor id",
					DBMSFieldsEnum.INT);
			dateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the paid date in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);
			updateVendorOrderDatePaid(Integer.parseInt(vendor_id), DBMSUtils.getSqlDate(dateString));
		
		} catch (CustomDBMSException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
			
		}		
	}
	
	/**
	 * Function for updating vendor purchase date paid. Date is updated for all 
	 * pending order that is due current month.i.e., when the date_paid is null.
	 * @param vendor_id
	 * @param date
	 * @throws CustomDBMSException
	 */
	public void updateVendorOrderDatePaid(int vendor_id, Date date ) throws CustomDBMSException
	{
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement callStmt = null;

	
		try {
			callStmt = dbConnection.prepareCall(updateVendorOrderDatePaidString);

			callStmt.setInt(1, vendor_id);
			callStmt.setDate(2, date);
						
			callStmt.execute();

			dbConnection.commit();
			callStmt.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
		} finally {
			
			dbConnectionUtil.closeConnection(dbConnection);
		}
	}
	
	/**
	 * CLI for generating customer bill.
	 * @throws CustomDBMSException
	 */
	public void generateCustomerTotalBillCli() throws CustomDBMSException
	{
		String name = null, address = null;
		String dateString = null;

		try {
			name = DBMSUtils.promptInputFromCLI("Enter the name of the Customer",
					DBMSFieldsEnum.NAME);
				address = DBMSUtils.promptInputFromCLI(
				"Enter the address of the Customer", DBMSFieldsEnum.ADDRESS);
		dateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the due date in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);
		generateCustomerTotalBill(name, address, DBMSUtils.getSqlDate(dateString));
		
		} catch (CustomDBMSException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
			
		}		
	}
	
	/**
	 * Function for generating customer total bill. Total bill for current month unpaid orders
	 * are summed up.
	 * @param name
	 * @param address
	 * @param date
	 * @throws CustomDBMSException
	 */
	public void generateCustomerTotalBill(String name, String address, Date date) throws CustomDBMSException
	{
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement callStmt = null;

	
		try {
			callStmt = dbConnection.prepareCall(generateCustomerTotalBillString);

			callStmt.registerOutParameter(5, OracleTypes.INTEGER);
			callStmt.registerOutParameter(4, OracleTypes.CURSOR);
			callStmt.setString(1, name);
			callStmt.setString(2, address);
			callStmt.setDate(3, date);
			
			callStmt.execute();

			// return the result set
			ResultSet rset = (ResultSet) callStmt.getObject(4);
			Integer cust_id = (Integer)callStmt.getObject(5);

		// print the results, all the columns in each row
			System.out
					.println("customerid\t amount\t due_date\n");
			 while (rset.next()) {
				System.out.println( cust_id + "\t"
						+ Float.toString(rset.getFloat(1)) + "\t" + date.toString() + "\n" );
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
	 * Cli for generating vendor total bill.
	 * @throws CustomDBMSException
	 */
	public void generateVendorTotalBillCli() throws CustomDBMSException
	{
		String name = null, address = null;
		String dateString = null;

		try {
			name = DBMSUtils.promptInputFromCLI("Enter the name of the Vendor",
					DBMSFieldsEnum.NAME);
				address = DBMSUtils.promptInputFromCLI(
				"Enter the address of the Vendor", DBMSFieldsEnum.ADDRESS);
		dateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the due date in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);
		generateVendorTotalBill(name, address, DBMSUtils.getSqlDate(dateString));
		
		} catch (CustomDBMSException e) {
			throw new CustomDBMSException(
					"Got an sql exception during generateVendors...", e);
			
		}		
	}

    /**
     * Function for generating vendor total bill. Total bill for current month unpaid orders
	 * are summed up.
     * @param name
     * @param address
     * @param date
     * @throws CustomDBMSException
     */
	public void generateVendorTotalBill(String name, String address, Date date) throws CustomDBMSException
	{
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement callStmt = null;

	
		try {
			callStmt = dbConnection.prepareCall(generateVendorTotalBillString);

			callStmt.registerOutParameter(5, OracleTypes.INTEGER);
			callStmt.registerOutParameter(4, OracleTypes.CURSOR);
			callStmt.setString(1, name);
			callStmt.setString(2, address);
			callStmt.setDate(3, date);
			
			callStmt.execute();

			// return the result set
			ResultSet rset = (ResultSet) callStmt.getObject(4);
			Integer vendor_id = (Integer)callStmt.getObject(5);

		// print the results, all the columns in each row
			System.out
					.println("Vendorid\t amount\t due_date\n");
			 while (rset.next()) {
				System.out.println( vendor_id + "\t"
						+ Float.toString(rset.getFloat(1)) + "\t" + date.toString() + "\n" );
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
	 * Main CLI function for this Accountant class. 
	 */
	
	public void finalCLI() throws CustomDBMSException {

		String menuChoice = null;
		boolean done = false;

		while (!done) {
			DBMSUtils.printOnCLI("Choose the action");
			DBMSUtils.printOnCLI("1 Generate Customer Bill");
			DBMSUtils.printOnCLI("2 Generate Vendor Bill");
			DBMSUtils.printOnCLI("3 Update customer paid date");
			DBMSUtils.printOnCLI("4 Update vendor paid date");
			menuChoice = DBMSUtils.promptInputFromCLI("Enter your choice",
					DBMSFieldsEnum.INT);
			switch (Integer.parseInt(menuChoice)) {
			case 1:
				generateCustomerTotalBillCli();
				done = true;
				break;
			case 2:
				generateVendorTotalBillCli();
				done = true;
				break;
			case 3:
				updateCustomerOrderDatePaidCli();
				done = true;
				break;
			case 4:
				updateVendorOrderDatePaidCli();
				done = true;
				break;
			default:
				DBMSUtils.printOnCLI("Invalid choice. Please try again");
				break;
			}

		}

	}
	
	public static void main(String[] args) throws Exception
	{
		try {
		Accountant acc = new Accountant(5);
		//acc.generateCustomerTotalBill("Bob", "101 Russet St.", DBMSUtils.getSqlDate("31/12/2012"));
		acc.generateVendorTotalBill("Print and Go", "432 Letter Lane", DBMSUtils.getSqlDate("31/12/2012"));
		//acc.updateCustomerOrderDatePaid(4, DBMSUtils.getSqlDate("3/12/2012"));
		acc.updateVendorOrderDatePaid(2, DBMSUtils.getSqlDate("2/12/2012") );
		//acc.generateCustomerTotalBill("Susie", "102 Golden Lane", DBMSUtils.getSqlDate("31/12/2012"));
	//	acc.finalCLI();
		}
		catch(CustomDBMSException e)
		{
			e.printStackTrace();
		}
		
	}
}
