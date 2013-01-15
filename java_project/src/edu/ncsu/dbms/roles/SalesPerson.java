package edu.ncsu.dbms.roles;

import java.awt.image.DataBufferUShort;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;

import edu.ncsu.dbms.constants.DBMSConstants;
import edu.ncsu.dbms.constants.DBMSFieldsEnum;
import edu.ncsu.dbms.dbutil.DBMSConnectionUtil;
import edu.ncsu.dbms.dbutil.DBMSUtils;
import edu.ncsu.dbms.exception.CustomDBMSException;
import edu.ncsu.dbms.logger.DBMSLogger;

/**
 * Inventory related classes
 */
/**
 * This class maintains information about book
 * 
 * @author Harsha
 * 
 */
class Merchandise {
	public String name;
	public long isbn;
	public String author;
	public float price;
	public int merchandiseId;

	/**
	 * This constructor checks merchandise and initializes book object
	 * 
	 * @param name
	 * @param author
	 * @throws CustomDBMSException
	 *             throws exception if the book doesnot exist
	 * @throws SQLException
	 */
	public Merchandise(String name, String author) throws CustomDBMSException,
			SQLException {
		this.name = name;
		this.author = author;

		// find if the book exists
		String bookQuery = "Select * from Merchandise where name=? and author=?";
		DBMSConnectionUtil dbUtil = DBMSConnectionUtil.getInstance();
		Connection con = dbUtil.getConnection();

		PreparedStatement ps = con.prepareStatement(bookQuery);

		ps.setString(1, name);
		ps.setString(2, author);

		ResultSet resultSet = ps.executeQuery();
		initialize(resultSet);

		resultSet.close();
		ps.close();
		con.close();
	}

	private void initialize(ResultSet resultSet) throws SQLException,
			CustomDBMSException {
		if (resultSet == null || !resultSet.next()) {
			throw new CustomDBMSException(
					"Given merchandise vendor is not registered");
		}
		merchandiseId = resultSet.getInt(1);
		name = resultSet.getString(2);
		isbn = resultSet.getLong(3);
		author = resultSet.getString(4);
		price = resultSet.getInt(5);
	}
}

/**
 * This class gives available inventory for a given merchandise
 * 
 * @author Harsha
 * 
 */
class MerchandiseStock {
	int merchandiseId;
	int storeId;
	int quantity;

	public MerchandiseStock(int merchandiseId, int storeId)
			throws CustomDBMSException, SQLException {
		this.merchandiseId = merchandiseId;
		this.storeId = storeId;

		// find quantity at that store
		String inventoryQuery = "Select * from MerchandiseStock where merchandiseid=? and storeid=?";
		DBMSConnectionUtil dbUtil = DBMSConnectionUtil.getInstance();
		Connection con = dbUtil.getConnection();

		PreparedStatement ps = con.prepareStatement(inventoryQuery);
		ps.setInt(1, merchandiseId);
		ps.setInt(2, storeId);

		ResultSet resultSet = ps.executeQuery();
		initialize(resultSet);

		resultSet.close();
		ps.close();
		con.close();
	}

	private void initialize(ResultSet resultSet) throws SQLException {
		if (resultSet != null && resultSet.next()) {
			merchandiseId = resultSet.getInt(1);
			storeId = resultSet.getInt(2);
			quantity = resultSet.getInt(3);
		}
	}
}

class OrderItem {
	Merchandise item;
	int quantity;
	boolean status;
}

/**
 * These classes will not enter any details in database
 */
class OrderDetails {
	List<OrderItem> items;
}

/**
 * This class acts as a SalesPerson. It acts as interface to handle customer
 * requests
 * 
 */
public class SalesPerson extends Staff {

	/**
	 * SQL queries for operations
	 */

	// registerCustomerQuery

	// Insert_Customer(ssn IN Customers.ssn%TYPE, gender IN
	// customers.gender%TYPE, DOB IN customers.DOB%TYPE, cust_name IN
	// customers.name%TYPE, phone_number IN customers.phone_number%TYPE, email
	// IN customers.email%TYPE, address IN customers.address%TYPE, credit_card
	// IN customers.credit_card%TYPE)
	private String registerCustomerQuery = "begin Insert_Customer(?, ?, ?, ?, ?, ?, ?, ?); end;";
	private String updateCustomerQuery = "begin Update_Customer(?, ?, ?, ?, ?, ?, ?, ?, ?); end;";

	private DBMSLogger logger = DBMSLogger.getInstance();

	public SalesPerson(int staffId) throws CustomDBMSException {

		super(staffId);

	}

	/**
	 * Registers new customer Precondition: none Postcondition: a new customer
	 * is created in database or InvalidCustomerException is thrown if customer
	 * details are invalid or if a customer with these details already exist.
	 * 
	 * @params: customername, SSN, data of birth, gender, age, address and
	 *          phone#
	 * @returns: status of operation
	 */
	public boolean registerCustomer(long ssn, String gender, Date dob,
			String name, long phone, String emailId, String address,
			long creditCardNo) throws CustomDBMSException {
		// status of the operation
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement insertCustomerStatement = null;
		boolean success = true;

		logger.debug("Adding a new row in the customers table with the name "
				+ name + " and phone number " + phone);

		try {
			// Get the prepared statement to call the procedure
			insertCustomerStatement = dbConnection
					.prepareCall(registerCustomerQuery);

			// Set the parameters
			insertCustomerStatement.setLong(1, ssn);
			insertCustomerStatement.setString(2, gender);
			insertCustomerStatement.setDate(3, dob);
			insertCustomerStatement.setString(4, name);
			insertCustomerStatement.setLong(5, phone);
			insertCustomerStatement.setString(6, emailId);
			insertCustomerStatement.setString(7, address);
			insertCustomerStatement.setLong(8, creditCardNo);

			insertCustomerStatement.execute();

			// TODO: what to verify here? Verify if the passed bank account
			// exists
			if (DBMSUtils.verifyCreditCard(creditCardNo)) {
				// The passed bank account is valid. Commit the transaction
				logger.debug("Customer " + name
						+ " details successfully updated");
				dbConnection.commit();
			} else {
				// The passed bank account is invalid. Rollback the transaction
				// TODO: change the verification statement
				logger.warn("verification failed Rolling back...");
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
	 * This method is cli interface to register a customer
	 * 
	 * @throws CustomDBMSException
	 */
	public void addCustomerCLI() throws CustomDBMSException {
		String ssn;
		String gender;
		String dob;
		String name;
		String phone;
		String emailId;
		String address;
		String creditCardNo;

		name = DBMSUtils.promptInputFromCLI("Enter the name of the Customer",
				DBMSFieldsEnum.NAME);

		ssn = DBMSUtils.promptInputFromCLI("Enter the ssn number",
				DBMSFieldsEnum.SSN);
		address = DBMSUtils.promptInputFromCLI(
				"Enter the address of the Customer", DBMSFieldsEnum.ADDRESS);
		phone = DBMSUtils.promptInputFromCLI(
				"Enter the phone number of the Customer",
				DBMSFieldsEnum.PHONE_NUMBER);
		gender = DBMSUtils.promptInputFromCLI(
				"Enter the gender of the Customer", DBMSFieldsEnum.GENDER);
		dob = DBMSUtils.promptInputFromCLI(
				"Enter the date of birth of the Customer", DBMSFieldsEnum.DATE);
		emailId = DBMSUtils.promptInputFromCLI("Enter the emailId of Customer",
				DBMSFieldsEnum.EMAIL);
		creditCardNo = DBMSUtils.promptInputFromCLI(
				"Enter the credit card of customer",
				DBMSFieldsEnum.CREDIT_CARD_NUMBER);

		registerCustomer(Long.parseLong(ssn), gender,
				DBMSUtils.getSqlDate(dob), name, Long.parseLong(phone),
				emailId, address, Long.parseLong(creditCardNo));
	}

	/**
	 * Updates customer details Precondition: The customer is already
	 * registered. Postcondition: the customer is updated in database or
	 * InvalidCustomerException is thrown if customer details are invalid or if
	 * a customer the customer is not registered.
	 * 
	 * @params customerID and attributes to be updated
	 */
	public boolean updateCustomer(int customerId, long ssn, String gender,
			Date dob, String name, long phone, String emailId, String address,
			long creditCardNo) throws CustomDBMSException {
		// status of the operation
		Connection dbConnection = dbConnectionUtil.getConnection();
		CallableStatement updateCustomerStatement = null;
		boolean success = true;

		logger.debug("Updating a new row in the customers table with the name "
				+ name + " and address " + address);

		try {
			// Get the prepared statement to call the procedure
			updateCustomerStatement = dbConnection
					.prepareCall(updateCustomerQuery);

			// Set the parameters
			updateCustomerStatement.setInt(1, customerId);
			updateCustomerStatement.setLong(2, ssn);
			updateCustomerStatement.setString(3, gender);
			updateCustomerStatement.setDate(4, dob);
			updateCustomerStatement.setString(5, name);
			updateCustomerStatement.setLong(6, phone);
			updateCustomerStatement.setString(7, emailId);
			updateCustomerStatement.setString(8, address);
			updateCustomerStatement.setLong(9, creditCardNo);

			updateCustomerStatement.execute();

			// TODO: what to verify here? Verify if the passed bank account
			// exists
			if (DBMSUtils.verifyCreditCard(creditCardNo)) {
				// The passed bank account is valid. Commit the transaction
				logger.debug("Customer " + name + " successfully registered");
				dbConnection.commit();
			} else {
				// The passed bank account is invalid. Rollback the transaction
				// TODO: change the verification statement
				logger.warn("verification failed Rolling back...");
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
	 * This method updates customerId details
	 * 
	 * @param name
	 * @param address
	 * @throws CustomDBMSException
	 */
	public void updateCustomerCLI(String name, String address)
			throws CustomDBMSException {
		long ssn;
		String gender;
		Date dob;
		long phone;
		String emailId;
		long creditCardNo;
		int customerId;

		// fetch the details of based on name and address and update details
		String findCustomerQuery = "Select * from Customers where name=? and address=?";
		Connection dbConnection = dbConnectionUtil.getConnection();
		ResultSet resultSet;

		try {
			PreparedStatement getCustomerDetails = dbConnection
					.prepareStatement(findCustomerQuery);
			getCustomerDetails.setString(1, name);
			getCustomerDetails.setString(2, address);

			resultSet = getCustomerDetails.executeQuery();
			if (resultSet != null && resultSet.next()) {
				customerId = resultSet.getInt(1);
				ssn = resultSet.getLong(2);
				gender = resultSet.getString(3);
				dob = resultSet.getDate(4);
				phone = resultSet.getLong(6);
				emailId = resultSet.getString(7);
				creditCardNo = resultSet.getLong(9);

				// allow customer to update details
				name = DBMSUtils.promptInputFromCLI(
						"Enter the name of the Customer", DBMSFieldsEnum.NAME,
						name);

				String ssnString = DBMSUtils.promptInputFromCLI(
						"Enter the ssn number", DBMSFieldsEnum.SSN,
						String.valueOf(ssn));

				address = DBMSUtils.promptInputFromCLI(
						"Enter the address of the Customer",
						DBMSFieldsEnum.ADDRESS, address);

				String phoneString = DBMSUtils.promptInputFromCLI(
						"Enter the phone number of the Customer",
						DBMSFieldsEnum.PHONE_NUMBER, String.valueOf(phone));

				gender = DBMSUtils.promptInputFromCLI(
						"Enter the gender of the Customer",
						DBMSFieldsEnum.GENDER, gender);

				String dobString = DBMSUtils.promptInputFromCLI(
						"Enter the date of birth of the Customer",
						DBMSFieldsEnum.DATE,
						DBMSUtils.getStringFormOfSQLDate(dob));
				emailId = DBMSUtils.promptInputFromCLI(
						"Enter the emailId of Customer", DBMSFieldsEnum.EMAIL,
						emailId);

				String creditCardNoString = DBMSUtils.promptInputFromCLI(
						"Enter the credit card of customer",
						DBMSFieldsEnum.CREDIT_CARD_NUMBER,
						String.valueOf(creditCardNo));

				// update customer details
				updateCustomer(customerId, Long.parseLong(ssnString), gender,
						DBMSUtils.getSqlDate(dobString), name,
						Long.parseLong(phoneString), emailId, address,
						Long.parseLong(creditCardNoString));
			}
			getCustomerDetails.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an SQL error while getting the customer details for name "
							+ name + "and address " + address, e);
		} finally {
			this.dbConnectionUtil.closeConnection(dbConnection);
		}
	}

	/**
	 * Deletes customer from database Precondition: The customer is already
	 * registered. Postcondition: if customer is valid then he is and all his
	 * orders will be deleted else InvalidCustomerException is thrown
	 * 
	 * @params customerID and attributes to be updated
	 */
	public boolean deleteCustomer(int customerID) throws CustomDBMSException {
		boolean status = false;

		return status;
	}

	/**
	 * Places order on behalf of the customer Precondition: The customer is
	 * already registered Postcondition: If merchandise is valid then an order
	 * is placed and billing information is updated in database else if customer
	 * is invalid then InvalidCustomerException is thrown or else
	 * InvalidOrderException is thrown
	 * 
	 * @params customer ID and Merchandise list he wants to buy
	 * 
	 * @return order ID
	 * @throws SQLException
	 */

	public int placeOrder(String customerName, String address,
			OrderDetails order) throws CustomDBMSException, SQLException {
		int orderId = -1;

		Connection connection = DBMSConnectionUtil.getInstance()
				.getConnection();

		try {
			// check if the order can be fulfilled
			boolean canOrderBeFullFilled = true;
			Iterator<OrderItem> itr = order.items.iterator();
			while (itr.hasNext()) {
				// check merchandise stock for each inventory
				OrderItem item = itr.next();
				MerchandiseStock itemStock = new MerchandiseStock(
						item.item.merchandiseId, storeId);
				if (itemStock.quantity < item.quantity) {
					canOrderBeFullFilled = false;
					item.status = false;
				} else {
					// update inventory with new stock values
					int stock_new_quantity = itemStock.quantity - item.quantity;
					String updateStockQuery = "update MERCHANDISESTOCK set quantity=? where merchandiseid = ? and storeid = ?";

					PreparedStatement ps = connection
							.prepareStatement(updateStockQuery);
					ps.setInt(1, stock_new_quantity);
					ps.setInt(2, item.item.merchandiseId);
					ps.setInt(2, storeId);

					ps.executeUpdate();
					ps.close();
					// mark the item has delivered
					item.status = true;
				}
			}

			// check if entire order cannot be fullfilled
			if (canOrderBeFullFilled == false) {
				int customerId;
				boolean isNewCustomer = false;
				// see if customer is already registered
				PreparedStatement ps = connection
						.prepareStatement("select * from Customers where name = ? and address = ?");
				ps.setString(1, customerName);
				ps.setString(2, address);

				ResultSet rs = ps.executeQuery();
				if (rs != null && rs.next() != true) {
					// customer doesn't exist
					isNewCustomer = true;
					DBMSUtils
							.printOnCLI("Please enter your details to process complete order");
					addCustomerCLI();
					// issue request again to get customerId
					rs = ps.executeQuery();
					if(rs.next()){
					   customerId = rs.getInt(1);
					}
				}

				// get customer details from result set
				customerId = rs.getInt(1);

				// insert an order in orders table for unfulfilled items
				CallableStatement insertOrderStatement = connection
						.prepareCall("begin insert_order(?); end;");
				insertOrderStatement.registerOutParameter(1,
						OracleTypes.INTEGER);

				insertOrderStatement.executeUpdate();
				orderId = (Integer) insertOrderStatement.getObject(1);

				// insert order details in orderdetails table
				itr = order.items.iterator();
				float amount = 0;
				while (itr.hasNext()) {
					OrderItem pendingItem = itr.next();
					if (!pendingItem.status) {
						CallableStatement insertOrderDetailsStatement = connection
								.prepareCall("begin insert_orderdetails(?, ?, ?, ?); end;");
						insertOrderDetailsStatement.setInt(1, orderId);
						insertOrderDetailsStatement.setInt(2,
								pendingItem.item.merchandiseId);
						insertOrderDetailsStatement.setString(3,
								DBMSConstants.CUSTOMER_ORDER_PLACE_SATUS);
						insertOrderDetailsStatement.setInt(4,
								pendingItem.quantity);

						insertOrderDetailsStatement.executeUpdate();

						amount += pendingItem.quantity * pendingItem.item.price;
						insertOrderDetailsStatement.close();
					}
				}

				/*
				 * place an order based on customer in place_orders table if new
				 * customer insert into maintainedforcustomers table insert
				 * information in customerbilling accout
				 */
				CallableStatement updateCustomerBillingInfo = connection
						.prepareCall("begin insert_customerbilling_account(?, ?, ?, ?); end;");
				updateCustomerBillingInfo.setInt(1, staffId);
				updateCustomerBillingInfo.setInt(2, customerId);
				updateCustomerBillingInfo.setInt(3, orderId);
				updateCustomerBillingInfo.setFloat(4, amount);

				updateCustomerBillingInfo.executeUpdate();
				updateCustomerBillingInfo.close();
			}
			connection.commit();
			System.out.println("Successfully placed order!");
		} catch (Exception e) {
			System.out.println("Failed to process order");
                        e.printStackTrace();
			connection.rollback();
			return -1;
		} finally {
			connection.close();
		}

		return orderId;
	}

	/**
	 * This method takes order from customer
	 */
	public int placeOrderCLI() throws Exception {
		String customerName;
		String address;
		OrderDetails order;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// read customer name
		System.out.print("Enter your name:");
		customerName = br.readLine();

		// read address
		System.out.print("Enter your address:");
		address = br.readLine();

		// read order details
		order = new OrderDetails();
		order.items = new ArrayList<OrderItem>();

		System.out.print("Enter book title, author and quantity in each line");
		String line;
		while ((line = br.readLine()) != null) {
			// end of items
			if (line.isEmpty()) {
				if (order.items.size() == 0) {
					System.out.println("Invalid order");
					return -1;
				} else {
					System.out.println("Your order is placed");
					break;
				}

			}

			// read each item
			String bookTitle;
			String author;
			int quantity;

			bookTitle = line;
			author = br.readLine();
			quantity = Integer.parseInt(br.readLine());

			OrderItem item = new OrderItem();
			try {
				item.item = new Merchandise(bookTitle.trim(), author.trim());
				item.quantity = quantity;
				order.items.add(item);
			} catch (CustomDBMSException e) {
				System.out
						.println("Item is not present and hence your order cannot be processed further");
				System.out.println("Thank you");
				br.close();
				return -1;
			}
		}

		// check if order is not empty
		if (order.items.size() == 0) {
			br.close();
			System.out.println("No merchandise selected");
			return -1;
		}

		// handle request to place order
		return placeOrder(customerName, address, order);

	}

	/**
	 * This method cancels already place order
	 * 
	 * @param orderId
	 * @throws SQLException
	 * @throws CustomDBMSException
	 */
	public void cancelOrder(int orderId) throws CustomDBMSException {
		Connection connection = DBMSConnectionUtil.getInstance()
				.getConnection();

		try {
			// check for invalid order
			if (orderId == -1) {
				System.out.println("Invalied order");
				return;
			}

			// check if the order is fulfilled
			String fullFilledOrderQuery = "select status from orderdetails where status='placed' and orderId="
					+ orderId;
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(fullFilledOrderQuery);

			if (rs == null || !rs.next()) {
				System.out
						.println("Order has either been fulfilled or invalid");
				return;
			}

			if (rs.getString(1).equals(
					DBMSConstants.CUSTOMER_ORDER_FULFILLED_SATUS)) {
				System.out.println("Order is already fulfilled");
				return;
			}

			// delete orderdetails for the given orderid
			String deleteOrderDetailsQuery = "delete from orderdetails where orderid="
					+ orderId;
			st = connection.createStatement();
			st.executeUpdate(deleteOrderDetailsQuery);

			// delete placed orders for the given orderid
			String deletePlacedOrderQuery = "delete from place_order where orderid="
					+ orderId;
			st = connection.createStatement();
			st.executeUpdate(deletePlacedOrderQuery);

			// delete customer billing account
			String deleteCustomerBillingAccount = "delete from customerbilling_account where orderId="
					+ orderId;
			st = connection.createStatement();
			st.executeUpdate(deleteCustomerBillingAccount);

			// delete order for given order id
			String deleteOrder = "delete from orders where orderid=" + orderId;
			st = connection.createStatement();
			st.executeUpdate(deleteOrder);

			// commit transaction
			st.close();
			rs.close();

			connection.commit();
			System.out.println("successfully deleted order");
		} catch (Exception e) {
			System.out.println("Failed to delete order " + e.getMessage());
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * This method fulfills any pending orders
	 * 
	 * @throws CustomDBMSException
	 */
	public void fullFillOrders() throws CustomDBMSException {
		Connection connection = DBMSConnectionUtil.getInstance()
				.getConnection();

		try {
			// merchandise id and quantity to be reduced
			HashMap<Integer, Integer> merchandiseQuantityToBeReduced = new HashMap<Integer, Integer>();

			// get all the orders that are in fulfilled state and reduce the
			// merchandise amount by this quantity
			// check if the order is fulfilled
			String fullFilledOrderQuery = "select merchandiseid, quantity from orderdetails where status='"
					+ DBMSConstants.MERCHANDISE_IN_STORE_STATUS+"'";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(fullFilledOrderQuery);

			while (rs != null && rs.next()) {
				int merchandiseid = rs.getInt(1);
				int quantity = rs.getInt(2);

				if (merchandiseQuantityToBeReduced.get(merchandiseid) == null) {
					merchandiseQuantityToBeReduced.put(merchandiseid, quantity);
				} else {
					int prevQuantity = merchandiseQuantityToBeReduced
							.get(merchandiseid);
					merchandiseQuantityToBeReduced.put(merchandiseid, quantity
							+ prevQuantity);
				}
			}

			// reduce inventory of the amount in merchandise table
			Set<Integer> merchandiseSet = merchandiseQuantityToBeReduced
					.keySet();
			Iterator<Integer> merchandiseItr = merchandiseSet.iterator();
			String updateMerchandiseQuantity = "update MerchandiseStock set quantity = ? where merchandiseid = ?";
			PreparedStatement ps = connection
					.prepareStatement(updateMerchandiseQuantity);

			while (merchandiseItr.hasNext()) {
				int merchandiseId = merchandiseItr.next();
				int quantity = merchandiseQuantityToBeReduced
						.get(merchandiseId);

				ps.setInt(1, quantity);
				ps.setInt(2, merchandiseId);

				ps.executeUpdate();
			}

			// update order status to fullfilled
			String updateInstoreMerchandise = "update orderdetails set status='"
					+ DBMSConstants.CUSTOMER_ORDER_FULFILLED_SATUS
					+ "' where status='"
					+ DBMSConstants.MERCHANDISE_IN_STORE_STATUS+"'";
			st = connection.createStatement();
			st.executeUpdate(updateInstoreMerchandise);

			st.close();
			ps.close();
			rs.close();

			connection.commit();
		} catch (Exception e) {
			System.out.println("Failed to service request");
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void cancelOrderCLI() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		DBMSUtils.printOnCLI("Enter order to cancel:");
		try {
			int orderId = Integer.parseInt(br.readLine());
			cancelOrder(orderId);
		} catch (NumberFormatException e) {
			DBMSUtils.printOnCLI("Invalid order");
		} catch (IOException e) {
			DBMSUtils.printOnCLI("Failed to read orderid");
			e.printStackTrace();
		} catch (CustomDBMSException e) {
			DBMSUtils.printOnCLI("Failed to cancel orderid");
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Cancels customer a order Precondition: Its a valid order placed by the
	 * given customer Postcondition: If the order is placed by customer then
	 * order is deleted and billing information is deleted from database else if
	 * customer is invalid then InvalidCustomerException or else
	 * InvalidOrderException is thrown
	 * 
	 * @params customer ID and order ID
	 */
	// public boolean cancelOrder(int customerID, int orderID) throws
	// CustomDBMSException {
	// TODO
	// }
	public void finalCLI() throws CustomDBMSException {

		String menuChoice = null;
		boolean done = false;

		while (!done) {
			DBMSUtils.printOnCLI("Choose the action");
			DBMSUtils.printOnCLI("1 Place order");
			DBMSUtils.printOnCLI("2 Cancel order");
			DBMSUtils.printOnCLI("3 FullFil order");
			DBMSUtils.printOnCLI("4 Update customer");
			menuChoice = DBMSUtils.promptInputFromCLI("Enter your choice",
					DBMSFieldsEnum.INT);
			switch (Integer.parseInt(menuChoice)) {
			case 1:
				try {
					System.out.println(placeOrderCLI());
					done = true;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
			case 2:
				cancelOrderCLI();
				done = true;
				break;
			case 3:
				fullFillOrders();
				done = true;
				break;
			case 4:
				// read username and address of customer
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				DBMSUtils.printOnCLI("Enter name:");
				try {
					String name = br.readLine();
					String address = br.readLine();
					updateCustomerCLI(name, address);
					done = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				DBMSUtils.printOnCLI("Invalid choice. Please try again");
				break;
			}

		}
	}

	public static void main(String[] args) throws Exception {

		// Merchandise book = new Merchandise("Database Design", "Arthur Fry");
		// System.out.println(book.price);
		/*
		 * try { SalesPerson aSalesPerson = new SalesPerson(8);
		 * aSalesPerson.updateCustomerCLI("kiran", "NCSU"); } catch
		 * (CustomDBMSException e) { e.printStackTrace(); }
		 */
		// take order details from customer
		// Connection connection = DBMSConnectionUtil.getInstance()
		// .getConnection();
		// CallableStatement insertOrderStatement = connection
		// .prepareCall("begin insert_order(?); end;");
		// insertOrderStatement.registerOutParameter(1, OracleTypes.INTEGER);
		//
		// insertOrderStatement.executeUpdate();
		// int orderId = (Integer) insertOrderStatement.getObject(1);
		// System.out.println("Staff id: " + orderId);
		// connection.commit();
		// insertOrderStatement.close();
		// connection.close();

		SalesPerson aSalesPerson = new SalesPerson(7);
		// aSalesPerson.placeOrderCLI();
		// aSalesPerson.cancelOrder(41);
	}
}
