package edu.ncsu.dbms.roles;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import edu.ncsu.dbms.constants.DBMSFieldsEnum;
import edu.ncsu.dbms.dbutil.DBMSUtils;
import edu.ncsu.dbms.exception.CustomDBMSException;
import edu.ncsu.dbms.logger.DBMSLogger;

/**
 * Contractor class is a staff that performs the Contractor's role. This include
 * adding new vendors and updating the vendor info
 * 
 */
public class Contractor extends Staff {

	public static final String VENDOR_ID_COLUMN = "VendorId";

	public static final String VENDOR_NAME_COLUMN = "name";

	public static final String VENDOR_ADDRESS_COLUMN = "address";

	public static final String VENDOR_PHONE_NO_COLUMN = "phone_number";

	public static final String VENDOR_CONTRACT_START_DATE_COLUMN = "ContractStartDate";

	public static final String VENDOR_CONTRACT_END_DATE_COLUMN = "ContractEndDate";

	public static final String VENDOR_BANK_ACCOUNT_COLUMN = "BankAccount_Number";

	private String addVendorQueryString = "begin Insert_vendors(?, ?, ?, ?, ?, ?); end;";

	private String updateVendorQueryString = "begin Update_vendors(?, ?, ?, ?, ?, ?, ?); end;";

	private String deleteVendorQueryString = "begin Delete_vendors_with_name(?, ?); end;";

	private String insertMerchandiseQuery = "begin Insert_Merchandise(?, ?, ?, ?, ?); end;";

	private String updateMerchandiseQuery = "begin Update_Merchandise(?, ?, ?, ?, ?, ?); end;";
	
	private String deleteMerchandiseQuery = "begin Delete_Merchandise(?); end;";

	private String getVendorWithNameAddressQueryString = "Select * from Vendors where name=? and address=?";

	private String getMerchandiseWithNameAuthorQueryString = "Select * from Merchandise where name=? and author=?";

	private DBMSLogger logger = DBMSLogger.getInstance();

	/**
	 * Constructor that takes the staff Id
	 * 
	 * @param staffId
	 * @throws CustomDBMSException
	 */
	public Contractor(int staffId) throws CustomDBMSException {

		super(staffId);

	}

	/**
	 * Method that adds the vendor information by calling a procedure
	 * 
	 * @param name
	 * @param address
	 * @param phoneNumber
	 * @param contractStartDate
	 * @param contractEndDate
	 * @param bankAccountNumber
	 * @return boolean
	 * @throws CustomDBMSException
	 */
	public boolean addVendor(String name, String address, long phoneNumber,
			Date contractStartDate, Date contractEndDate, long bankAccountNumber)
			throws CustomDBMSException {

		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement insertVendorsStatement = null;
		boolean success = true;

		logger.debug("Adding a new row in the vendors table with the name "
				+ name + " and phone number " + phoneNumber);

		try {
			// Get the prepared statement to call the procedure
			insertVendorsStatement = dbConnection
					.prepareCall(addVendorQueryString);

			// Set the parameters
			insertVendorsStatement.setString(1, name);
			insertVendorsStatement.setString(2, address);
			insertVendorsStatement.setLong(3, phoneNumber);
			insertVendorsStatement.setDate(4, contractStartDate);
			insertVendorsStatement.setDate(5, contractEndDate);
			insertVendorsStatement.setLong(6, bankAccountNumber);

			insertVendorsStatement.execute();

			// Verify if the passed bank account exists
			if (DBMSUtils.verifyBankAccount(bankAccountNumber)) {
				// The passed bank account is valid. Commit the transaction
				logger.debug("Vendor succesfully added");
				dbConnection.commit();
			} else {
				// The passed bank account is invalid. Rollback the transaction
				logger.warn("The credit card verification failed. Rolling back...");
				dbConnection.rollback();
				success = false;
			}

			dbConnectionUtil.closeConnection(dbConnection);

		} catch (SQLException e) {
			throw new CustomDBMSException("Got an sql exception...", e);
		}

		return success;

	}

	/**
	 * Method to update the vendor information
	 * 
	 * @param vendorId
	 * @param name
	 * @param address
	 * @param phoneNumber
	 * @param contractStartDate
	 * @param contractEndDate
	 * @param bankAccountNumber
	 * @return
	 * @throws CustomDBMSException
	 */
	public boolean updateVendor(int vendorId, String name, String address,
			long phoneNumber, Date contractStartDate, Date contractEndDate,
			long bankAccountNumber) throws CustomDBMSException {

		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement updateVendorsStatement = null;
		boolean success = true;

		logger.debug("Updating the row in the vendors table with the name "
				+ name + " and phone number " + phoneNumber);

		try {
			// Get the prepared statement to call the procedure
			updateVendorsStatement = dbConnection
					.prepareCall(updateVendorQueryString);

			// Set the parameters
			updateVendorsStatement.setInt(1, vendorId);
			updateVendorsStatement.setString(2, name);
			updateVendorsStatement.setString(3, address);
			updateVendorsStatement.setDouble(4, phoneNumber);
			updateVendorsStatement.setDate(5, contractStartDate);
			updateVendorsStatement.setDate(6, contractEndDate);
			updateVendorsStatement.setDouble(7, bankAccountNumber);

			updateVendorsStatement.execute();

			// Verify if the passed bank account exists
			if (DBMSUtils.verifyBankAccount(bankAccountNumber)) {
				// The passed bank account is valid. Commit the transaction
				dbConnection.commit();
				logger.debug("Vendor succesfully updated");
			} else {
				// The passed bank account is invalid. Rollback the transaction
				logger.warn("The credit card verification failed. Rolling back...");
				dbConnection.rollback();
				success = false;
			}

			dbConnectionUtil.closeConnection(dbConnection);

		} catch (SQLException e) {
			throw new CustomDBMSException("Got an sql exception...", e);
		}

		return success;

	}

	/**
	 * Method to delete vendor
	 * 
	 * @param name
	 * @param address
	 * @return
	 * @throws CustomDBMSException
	 */
	public boolean deleteVendor(String name, String address)
			throws CustomDBMSException {

		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement deleteVendorsStatement = null;
		boolean success = true;

		logger.debug("Deleting the row in the vendors table with the name "
				+ name + " and phone number " + address);

		try {
			// Get the prepared statement to call the procedure
			deleteVendorsStatement = dbConnection
					.prepareCall(deleteVendorQueryString);

			// Set the parameters
			deleteVendorsStatement.setString(1, name);
			deleteVendorsStatement.setString(2, address);

			deleteVendorsStatement.execute();
			dbConnection.commit();
			logger.debug("Vendor succesfully deleted");
			deleteVendorsStatement.close();

		} catch (SQLException e) {
			throw new CustomDBMSException("Got an sql exception...", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}

		return success;

	}

	/**
	 * Method to add the merchandise
	 * 
	 * @param name
	 * @param isbn
	 * @param author
	 * @param price
	 * @param vendorId
	 * @throws CustomDBMSException
	 */
	public void addMechandise(String name, long isbn, String author,
			float price, int vendorId) throws CustomDBMSException {

		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement insertMerchandiseStatement = null;

		try {
			// Get the prepared statement to call the procedure
			insertMerchandiseStatement = dbConnection
					.prepareCall(insertMerchandiseQuery);

			// Set the parameters
			insertMerchandiseStatement.setString(1, name);
			insertMerchandiseStatement.setLong(2, isbn);
			insertMerchandiseStatement.setString(3, author);
			insertMerchandiseStatement.setFloat(4, price);
			insertMerchandiseStatement.setInt(5, vendorId);
			insertMerchandiseStatement.execute();
			dbConnection.commit();
			insertMerchandiseStatement.close();
		} catch (SQLException e) {
			throw new CustomDBMSException("Got an sql exception...", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}

	}

	/**
	 * Method to update merchandise
	 * 
	 * @param merchandiseId
	 * @param name
	 * @param isbn
	 * @param author
	 * @param price
	 * @param vendorId
	 * @throws CustomDBMSException
	 */
	public void updateMechandise(int merchandiseId, String name, long isbn,
			String author, float price, int vendorId)
			throws CustomDBMSException {

		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement updateMerchandiseStatement = null;

		try {
			// Get the prepared statement to call the procedure
			updateMerchandiseStatement = dbConnection
					.prepareCall(updateMerchandiseQuery);

			// Set the parameters
			updateMerchandiseStatement.setInt(1, merchandiseId);
			updateMerchandiseStatement.setString(2, name);
			updateMerchandiseStatement.setLong(3, isbn);
			updateMerchandiseStatement.setString(4, author);
			updateMerchandiseStatement.setFloat(5, price);
			updateMerchandiseStatement.setInt(6, vendorId);
			updateMerchandiseStatement.execute();
			dbConnection.commit();
			updateMerchandiseStatement.close();
		} catch (SQLException e) {
			throw new CustomDBMSException("Got an sql exception...", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}

	}

	/**
	 * Method to delete merchandise
	 * 
	 * @param merchandiseId
	 * @throws CustomDBMSException
	 */
	public void deleteMechandise(int merchandiseId)
			throws CustomDBMSException {

		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement deleteMerchandiseStatement = null;

		try {
			// Get the prepared statement to call the procedure
			deleteMerchandiseStatement = dbConnection
					.prepareCall(deleteMerchandiseQuery);

			// Set the parameters
			deleteMerchandiseStatement.setInt(1, merchandiseId);
			deleteMerchandiseStatement.execute();
			dbConnection.commit();
			deleteMerchandiseStatement.close();
		} catch (SQLException e) {
			throw new CustomDBMSException("Got an sql exception while deleting the merchandise...", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}

	}
	
	/**
	 * Wrapper that calls that invokes the CLI for inserting a vendor
	 * 
	 * @throws CustomDBMSException
	 */
	public void addVendorCLI() throws CustomDBMSException {

		String name = null, address = null;
		String phoneNumberString = null, bankAccountNumberString = null, contractStartDateString = null, contractEndDateString = null;

		name = DBMSUtils.promptInputFromCLI("Enter the name of the Vendor",
				DBMSFieldsEnum.NAME);
		address = DBMSUtils.promptInputFromCLI(
				"Enter the address of the Vendor", DBMSFieldsEnum.ADDRESS);
		phoneNumberString = DBMSUtils.promptInputFromCLI(
				"Enter the phone number of the Vendor",
				DBMSFieldsEnum.PHONE_NUMBER);
		bankAccountNumberString = DBMSUtils.promptInputFromCLI(
				"Enter the bank account number of the Vendor",
				DBMSFieldsEnum.BANK_ACCOUNT);
		contractStartDateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the start contract date of the Vendor in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);
		contractEndDateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the end contract date of the Vendor in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE);

		addVendor(name, address, Long.parseLong(phoneNumberString),
				DBMSUtils.getSqlDate(contractStartDateString),
				DBMSUtils.getSqlDate(contractEndDateString),
				Long.parseLong(bankAccountNumberString));

	}

	/**
	 * Wrapper that invokes a CLI for update vendor
	 * 
	 * @throws CustomDBMSException
	 */
	public void updateVendorCLI() throws CustomDBMSException {

		String name = null, address = null;
		String phoneNumberString = null, bankAccountNumberString = null;
		String contractStartDateString = null, contractEndDateString = null;
		int vendorId = 0;
		long phoneNumber = 0;
		Date contractStartDate = null;
		Date contractEndDate = null;
		long bankAccountNumber = 0;
		PreparedStatement getVendorFromNameAddressStatement = null;

		ResultSet resultSet = null;

		name = DBMSUtils.promptInputFromCLI(
				"Enter the orignal name of the Vendor", DBMSFieldsEnum.NAME);
		address = DBMSUtils.promptInputFromCLI(
				"Enter the original address of the Vendor",
				DBMSFieldsEnum.ADDRESS);

		logger.debug("Retrieving the vendor information for name='" + name
				+ "' and address='" + address);

		Connection dbConnection = dbConnectionUtil.getConnection();
		try {
			getVendorFromNameAddressStatement = dbConnection
					.prepareStatement(getVendorWithNameAddressQueryString);
			getVendorFromNameAddressStatement.setString(1, name);
			getVendorFromNameAddressStatement.setString(2, address);
			resultSet = getVendorFromNameAddressStatement.executeQuery();
			if (resultSet.next()) {
				phoneNumber = resultSet.getLong(VENDOR_PHONE_NO_COLUMN);
				bankAccountNumber = resultSet
						.getLong(VENDOR_BANK_ACCOUNT_COLUMN);
				vendorId = resultSet.getInt(VENDOR_ID_COLUMN);
				contractStartDate = resultSet
						.getDate(VENDOR_CONTRACT_START_DATE_COLUMN);
				contractEndDate = resultSet
						.getDate(VENDOR_CONTRACT_END_DATE_COLUMN);
			} else {
				throw new CustomDBMSException(
						"Could not find any Vendors with the name '" + name
								+ "' and address '" + address + "'");
			}
			resultSet.close();
			getVendorFromNameAddressStatement.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an SQLException finding Vendors with the name '"
							+ name + "' and address '" + address + "'", e);

		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}

		name = DBMSUtils.promptInputFromCLI(
				"Enter the updated name of the Vendor", DBMSFieldsEnum.NAME,
				name);
		address = DBMSUtils.promptInputFromCLI(
				"Enter the updated address of the Vendor",
				DBMSFieldsEnum.ADDRESS, address);

		phoneNumberString = DBMSUtils.promptInputFromCLI(
				"Enter the updated phone number of the Vendor",
				DBMSFieldsEnum.PHONE_NUMBER, "" + phoneNumber);
		bankAccountNumberString = DBMSUtils.promptInputFromCLI(
				"Enter the updated bank account number of the Vendor",
				DBMSFieldsEnum.BANK_ACCOUNT, "" + bankAccountNumber);
		contractStartDateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the updated start contract date of the Vendor in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE,
						DBMSUtils.getStringFormOfSQLDate(contractStartDate));
		contractEndDateString = DBMSUtils
				.promptInputFromCLI(
						"Enter the updated end contract date of the Vendor in 'dd/mm/yyyy' format",
						DBMSFieldsEnum.DATE,
						DBMSUtils.getStringFormOfSQLDate(contractEndDate));

		updateVendor(vendorId, name, address,
				Long.parseLong(phoneNumberString),
				DBMSUtils.getSqlDate(contractStartDateString),
				DBMSUtils.getSqlDate(contractEndDateString),
				Long.parseLong(bankAccountNumberString));

	}

	/**
	 * Wrapper for CLI that invokes delete vendor
	 * 
	 * @throws CustomDBMSException
	 */
	public void deleteVendorCLI() throws CustomDBMSException {

		String name = null, address = null;

		name = DBMSUtils.promptInputFromCLI(
				"Enter the name of the Vendor to be deleted",
				DBMSFieldsEnum.NAME);
		address = DBMSUtils.promptInputFromCLI(
				"Enter the address of the Vendor to be deleted",
				DBMSFieldsEnum.ADDRESS);

		deleteVendor(name, address);

	}

	/**
	 * Add merchandise to the book store chain from a vendor
	 * 
	 * @throws CustomDBMSException
	 */
	public void addMerchandiseCLI() throws CustomDBMSException {

		String name, isbnString;
		String author;
		String priceString;
		String vendorIdString;

		name = DBMSUtils.promptInputFromCLI(
				"Enter the name of the Book to be added", DBMSFieldsEnum.NAME);
		author = DBMSUtils.promptInputFromCLI(
				"Enter the name of the author of the book to be added",
				DBMSFieldsEnum.NAME);
		isbnString = DBMSUtils.promptInputFromCLI(
				"Enter the isbn of the book to be added", DBMSFieldsEnum.INT);
		priceString = DBMSUtils.promptInputFromCLI(
				"Enter the price of the book to be added",
				DBMSFieldsEnum.AMOUNT);
		vendorIdString = DBMSUtils.promptInputFromCLI(
				"Enter the Id of the Vendor of the merchandise to be added",
				DBMSFieldsEnum.INT);

		addMechandise(name, Integer.parseInt(isbnString), author,
				Float.parseFloat(priceString), Integer.parseInt(vendorIdString));

	}

	public void updateMerchandiseCLI() throws CustomDBMSException {

		String name, isbnString;
		String author;
		String priceString;
		int merchandiseId = 0;
		float price = 0;
		int vendorId = 0, isbn = 0;
		PreparedStatement getMerchandisePreparedStatement = null;
		ResultSet resultSet = null;
		Connection dbConnection = dbConnectionUtil.getConnection();

		name = DBMSUtils
				.promptInputFromCLI("Enter the name of the Book to be updated",
						DBMSFieldsEnum.NAME);
		author = DBMSUtils.promptInputFromCLI(
				"Enter the name of the author of the book to be updated",
				DBMSFieldsEnum.NAME);

		try {
			getMerchandisePreparedStatement = dbConnection
					.prepareStatement(getMerchandiseWithNameAuthorQueryString);
			getMerchandisePreparedStatement.setString(1, name);
			getMerchandisePreparedStatement.setString(2, author);
			resultSet = getMerchandisePreparedStatement.executeQuery();
			if (resultSet.next()) {
				merchandiseId = resultSet.getInt(1);
				name = resultSet.getString(2);
				isbn = resultSet.getInt(3);
				author = resultSet.getString(4);
				price = resultSet.getFloat(5);
				vendorId = resultSet.getInt(6);
			} else {
				throw new CustomDBMSException("Found no merchandise with name "
						+ name + " and author " + author);
			}
			resultSet.close();
			getMerchandisePreparedStatement.close();
		} catch (SQLException e) {
			throw new CustomDBMSException("Got an sql error while updating merchandise with name "
					+ name + " and author " + author, e);
		}finally{
			dbConnectionUtil.closeConnection(dbConnection);
		}

		name = DBMSUtils.promptInputFromCLI(
				"Enter the new name of the book to be updated", DBMSFieldsEnum.NAME,name);
		author = DBMSUtils.promptInputFromCLI(
				"Enter the new author of the book to be updated", DBMSFieldsEnum.NAME, author);
		isbnString = DBMSUtils.promptInputFromCLI(
				"Enter the new isbn of the book to be updated", DBMSFieldsEnum.INT, ""+isbn);
		priceString = DBMSUtils.promptInputFromCLI(
				"Enter the new price of the book to be updated",
				DBMSFieldsEnum.AMOUNT, ""+price);
	
		updateMechandise(merchandiseId, name, Integer.parseInt(isbnString), author,
				Float.parseFloat(priceString), vendorId);

	}

	public void deleteMerchandiseCLI() throws CustomDBMSException {

		String name;
		String author;
		int merchandiseId = 0;
		PreparedStatement getMerchandisePreparedStatement = null;
		ResultSet resultSet = null;
		Connection dbConnection = dbConnectionUtil.getConnection();

		name = DBMSUtils
				.promptInputFromCLI("Enter the name of the Book to be deleted",
						DBMSFieldsEnum.NAME);
		author = DBMSUtils.promptInputFromCLI(
				"Enter the name of the author of the book to be deleted",
				DBMSFieldsEnum.NAME);

		try {
			getMerchandisePreparedStatement = dbConnection
					.prepareStatement(getMerchandiseWithNameAuthorQueryString);
			getMerchandisePreparedStatement.setString(1, name);
			getMerchandisePreparedStatement.setString(2, author);
			resultSet = getMerchandisePreparedStatement.executeQuery();
			if (resultSet.next()) {
				merchandiseId = resultSet.getInt(1);
			} else {
				throw new CustomDBMSException("Found no merchandise with name "
						+ name + " and author " + author);
			}
			resultSet.close();
			getMerchandisePreparedStatement.close();
		} catch (SQLException e) {
			throw new CustomDBMSException("Got an sql error while deleteing merchandise with name "
					+ name + " and author " + author, e);
		}finally{
			dbConnectionUtil.closeConnection(dbConnection);
		}

		deleteMechandise(merchandiseId);
	}
	
	
	public void finalCLI() throws CustomDBMSException {

		String menuChoice = null;
		boolean done = false;

		while (!done) {
			DBMSUtils.printOnCLI("Choose the action");
			DBMSUtils.printOnCLI("1 Create Vendor");
			DBMSUtils.printOnCLI("2 Update Vendor");
			DBMSUtils.printOnCLI("3 Delete Vendor");
			DBMSUtils.printOnCLI("4 Add Merchandise");
			DBMSUtils.printOnCLI("5 Update Merchandise");
			DBMSUtils.printOnCLI("6 Delete Merchandise");
			menuChoice = DBMSUtils.promptInputFromCLI("Enter your choice",
					DBMSFieldsEnum.INT);
			switch (Integer.parseInt(menuChoice)) {
			case 1:
				addVendorCLI();
				done = true;
				break;
			case 2:
				updateVendorCLI();
				done = true;
				break;
			case 3:
				deleteVendorCLI();
				done = true;
				break;
			case 4:
				addMerchandiseCLI();
				done = true;
				break;
			case 5:
				updateMerchandiseCLI();
				done = true;
				break;
			case 6:
				deleteMerchandiseCLI();
				done = true;
				break;
			default:
				DBMSUtils.printOnCLI("Invalid choice. Please try again");
				break;
			}

		}

	}

	public static void main(String[] args) throws Exception {

		Contractor c = new Contractor(4);
		/**
		 * long a = 9199768743L; long b = 112233567890L;
		 * 
		 * c.addVendor("SapnaBookHouse", "85 logging ave. Raleigh, NC, 27865",
		 * a, DBMSUtils.getSqlDate("01/11/2012"),
		 * DBMSUtils.getSqlDate("01/11/2013"), b);
		 * 
		 * a = 9199768743L; b = 11111111111111L;
		 * 
		 * c.addVendor("WTF", "85 logging ave. Raleigh, NC, 27865", a,
		 * DBMSUtils.getSqlDate("01/11/2012"),
		 * DBMSUtils.getSqlDate("01/11/2013"), b);
		 */
		// c.addVendorCLI();
		// c.deleteVendorCLI();
		// c.addMerchandiseCLI();
		c.finalCLI();

	}

}
