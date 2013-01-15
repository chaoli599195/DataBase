package edu.ncsu.dbms.roles;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.dbms.constants.DBMSConstants;
import edu.ncsu.dbms.constants.DBMSFieldsEnum;
import edu.ncsu.dbms.custom.objects.VendorMerchandiseOrder;
import edu.ncsu.dbms.dbutil.DBMSUtils;
import edu.ncsu.dbms.exception.CustomDBMSException;
import edu.ncsu.dbms.logger.DBMSLogger;

/**
 * Represents the merchandise stocker role. His role is to stock merchandise and
 * transfer from warehouse to stores
 * 
 */
public class MerchandiseStocker extends Staff {

	private static final int UPDATE_IN_STORE = 1;

	private static final int INSERT_IN_STORE = 2;

	private static final int NOT_AVAILABLE_IN_STORE = -1;

	private DBMSLogger logger = DBMSLogger.getInstance();

	private String getPlacedOrderQuery = "Select OrderDetails.OrderId, OrderDetails.MerchandiseId, "
			+ "Works_in.StoreId,  OrderDetails.quantity from OrderDetails, Place_order, "
			+ "works_in where OrderDetails.status = ?  and OrderDetails.orderId = Place_order.orderId "
			+ "and Place_order.staffId = Works_in.staffId";

	private String getMerchandiseStockDetailsFromStore = "Select * from MerchandiseStock where merchandiseId = ? and StoreId = ?";

	private String insertIntoMerchandiseStockQuery = "begin Insert_MerchandiseStock(?, ?, ?); end;";

	private String updateMerchandiseStockQuery = "begin Update_MerchandiseStock(?, ?, ?); end;";

	private String updateStatusOfOrder = "Update OrderDetails set status = ? where orderId = ? and merchandiseId = ?";

	private String insertVendorPaymentQuery = "begin insert_vendorpurchases(?, ?, ?); end;";

	public MerchandiseStocker(int staffId) throws CustomDBMSException {

		super(staffId);

	}

	/**
	 * Retrieve all the placed orders from the OrderDetails Table. This is used
	 * to order merchandise from the vendor
	 * 
	 * @return List
	 * @throws CustomDBMSException
	 */
	public List<VendorMerchandiseOrder> getPlacedOrderIds()
			throws CustomDBMSException {

		List<VendorMerchandiseOrder> orderIdList = new ArrayList<VendorMerchandiseOrder>();
		PreparedStatement getPlacedOrderDetailsStatement = null;
		ResultSet resultSet = null;

		Connection dbConnection = dbConnectionUtil.getConnection();

		try {
			getPlacedOrderDetailsStatement = dbConnection
					.prepareStatement(getPlacedOrderQuery);

			getPlacedOrderDetailsStatement.setString(1,
					DBMSConstants.CUSTOMER_ORDER_PLACE_SATUS);
			resultSet = getPlacedOrderDetailsStatement.executeQuery();
			while (resultSet.next()) {
				VendorMerchandiseOrder placedOrders = new VendorMerchandiseOrder(
						resultSet.getInt(1), resultSet.getInt(3),
						resultSet.getInt(2), resultSet.getInt(4));
				orderIdList.add(placedOrders);
			}

			getPlacedOrderDetailsStatement.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an SQL error while getting all the placed orders", e);
		} finally {
			this.dbConnectionUtil.closeConnection(dbConnection);
		}

		return orderIdList;

	}

	/**
	 * This method purchases the requested quantity from vendor and adds it to
	 * warehouse
	 * 
	 * @param merchandiseId
	 * @param quantity
	 * @throws CustomDBMSException
	 */
	public void purchaseFromVendorToWarehouse(int merchandiseId, int quantity,
			int update_or_insert) throws CustomDBMSException {

		Connection dbConnection = dbConnectionUtil.getConnection();

		try {
			if (update_or_insert == INSERT_IN_STORE) {
				insertIntoMerchandiseStock(merchandiseId, quantity
						* DBMSConstants.WAREHOUSE_ORDER_MULTIPLIER,
						DBMSConstants.WAREHOUSE_ID, dbConnection);

			} else {
				updateMerchandiseStock(merchandiseId, quantity
						* DBMSConstants.WAREHOUSE_ORDER_MULTIPLIER,
						DBMSConstants.WAREHOUSE_ID, dbConnection);
			}

			dbConnection.commit();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an SQL error while inserting into warehouse..", e);
		} finally {
			this.dbConnectionUtil.closeConnection(dbConnection);
		}

	}

	/**
	 * This method moves the merchandise from warehouse to store. It is used
	 * when a store doesn't have the merchandise to fulfill an order
	 * 
	 * @param storeId
	 * @param merchandiseId
	 * @param quantity
	 * @throws CustomDBMSException
	 */
	public void moveMerchandiseFromWarehouseToStore(int storeId,
			int merchandiseId, int quantity) throws CustomDBMSException {

		PreparedStatement getWarehouseMerchandiseDetailsStatement = null;
		ResultSet wareHouseResultSet = null;
		PreparedStatement getStoreMerchandiseDetailsStatement = null;
		ResultSet storeResultSet = null;
		int wareHouseQuantity = 0;
		int storeInventoryQuantiy = 0;

		Connection dbConnection = dbConnectionUtil.getConnection();

		try {
			// Get the ware house details about merchandise
			getWarehouseMerchandiseDetailsStatement = dbConnection
					.prepareStatement(getMerchandiseStockDetailsFromStore);
			getWarehouseMerchandiseDetailsStatement.setInt(1, merchandiseId);
			getWarehouseMerchandiseDetailsStatement.setInt(2,
					DBMSConstants.WAREHOUSE_ID);
			wareHouseResultSet = getWarehouseMerchandiseDetailsStatement
					.executeQuery();

			if (wareHouseResultSet.next()) {
				wareHouseQuantity = wareHouseResultSet.getInt(3);
				if (wareHouseQuantity - quantity < 0) {
					throw new CustomDBMSException(
							"The quantity available in warehouse for merchandise id "
									+ merchandiseId
									+ " is "
									+ wareHouseQuantity
									+ " which is less than the required quantity "
									+ quantity);
				}
			} else {
				throw new CustomDBMSException(
						"Could not find the merchandise with the id "
								+ merchandiseId + " in the warehouse");
			}
			wareHouseQuantity -= quantity;
			wareHouseResultSet.close();
			getWarehouseMerchandiseDetailsStatement.close();
			// Update WareHouse
			updateMerchandiseStock(merchandiseId, wareHouseQuantity,
					DBMSConstants.WAREHOUSE_ID, dbConnection);

			// Get store details about merchandise
			getStoreMerchandiseDetailsStatement = dbConnection
					.prepareStatement(getMerchandiseStockDetailsFromStore);
			getStoreMerchandiseDetailsStatement.setInt(1, merchandiseId);
			getStoreMerchandiseDetailsStatement.setInt(2, storeId);
			storeResultSet = getStoreMerchandiseDetailsStatement.executeQuery();
			if (storeResultSet.next()) {
				// Merchandise exists in the store. Update it
				storeInventoryQuantiy = storeResultSet.getInt(3);
				storeInventoryQuantiy += quantity;
				try {
					updateMerchandiseStock(merchandiseId,
							storeInventoryQuantiy, storeId, dbConnection);
				} catch (Exception e) {
					logger.warn("Caught exception '"
							+ e.getMessage()
							+ "' while updating store inventory.. Rolling back the trancastion");
					// Update of local inventory failed. Hence the changes made
					// to ware house has
					// to be rolled back.
					dbConnection.rollback();
					throw new CustomDBMSException(
							"Caught exception '"
									+ e.getMessage()
									+ "' while updating store inventory.. Rolling back the trancastion",
							e);
				}
			} else {
				try {
					// There is no merchandise in the store. Add a new entry.
					insertIntoMerchandiseStock(merchandiseId, quantity,
							storeId, dbConnection);
				} catch (Exception e) {
					logger.warn("Caught exception '"
							+ e.getMessage()
							+ "' while inserting into store inventory.. Rolling back the trancastion");
					// Update of local inventory failed. Hence the changes made
					// to ware house has
					// to be rolled back.
					dbConnection.rollback();
					throw new CustomDBMSException(
							"Caught exception '"
									+ e.getMessage()
									+ "' while inserting store inventory.. Rolling back the trancastion",
							e);
				}
			}

			dbConnection.commit();

		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an SQL error while inserting into warehouse..", e);

		} finally {
			this.dbConnectionUtil.closeConnection(dbConnection);
		}
	}

	/**
	 * Utility method that updates an entry for a merchandise in a inventory
	 * 
	 * @param merchandiseId
	 * @param quantity
	 * @param storeId
	 * @param dbConnection
	 * @throws CustomDBMSException
	 */
	public void updateMerchandiseStock(int merchandiseId, int quantity,
			int storeId, Connection dbConnection) throws CustomDBMSException {

		CallableStatement updateMechandiseStockStatement = null;

		try {
			updateMechandiseStockStatement = dbConnection
					.prepareCall(updateMerchandiseStockQuery);
			updateMechandiseStockStatement.setInt(1, merchandiseId);
			updateMechandiseStockStatement.setInt(2, storeId);
			updateMechandiseStockStatement.setInt(3, quantity);
			updateMechandiseStockStatement.execute();
			updateMechandiseStockStatement.close();

			System.out.println("Updated the merchandise of " + merchandiseId
					+ " of store " + storeId + " to quantity " + quantity);

			// dbConnection.commit();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an SQL error while updateing into merchandise stock with the store id "
							+ storeId + " and merchandise id " + merchandiseId
							+ "..", e);
		} finally {
			// this.dbConnectionUtil.closeConnection(dbConnection);
		}

	}

	/**
	 * Utility method that inserts an entry for a merchandise in a inventory
	 * 
	 * @param merchandiseId
	 * @param quantity
	 * @param storeId
	 * @param dbConnection
	 * @throws CustomDBMSException
	 */
	public void insertIntoMerchandiseStock(int merchandiseId, int quantity,
			int storeId, Connection dbConnection) throws CustomDBMSException {

		CallableStatement insertIntoMechandiseStockStatement = null;

		try {
			insertIntoMechandiseStockStatement = dbConnection
					.prepareCall(insertIntoMerchandiseStockQuery);
			insertIntoMechandiseStockStatement.setInt(1, merchandiseId);
			insertIntoMechandiseStockStatement.setInt(2, storeId);
			insertIntoMechandiseStockStatement.setInt(3, quantity);
			insertIntoMechandiseStockStatement.execute();
			insertIntoMechandiseStockStatement.close();

			// dbConnection.commit();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an SQL error while inserting into merchandise stock with the store id "
							+ storeId + " and merchandise id " + merchandiseId
							+ "..", e);
		} finally {
			// this.dbConnectionUtil.closeConnection(dbConnection);
		}

	}

	/**
	 * Changes order status of the customer to teh passed status
	 * 
	 * @param orderId
	 * @param merchandiseId
	 * @throws CustomDBMSException
	 */
	public void changeOrderStatus(int orderId, int merchandiseId, String status)
			throws CustomDBMSException {

		PreparedStatement updateOrderStatusStatement = null;
		Connection dbConnection = dbConnectionUtil.getConnection();

		try {
			updateOrderStatusStatement = dbConnection
					.prepareStatement(updateStatusOfOrder);

			updateOrderStatusStatement.setString(1, status);
			updateOrderStatusStatement.setInt(2, orderId);
			updateOrderStatusStatement.setInt(3, merchandiseId);

			updateOrderStatusStatement.executeUpdate();
			dbConnection.close();

			updateOrderStatusStatement.close();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an sql exception while updating customer status for id "
							+ orderId + " and merchandiseId " + merchandiseId
							+ "..", e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}

	}

	/**
	 * Insert vendor payment record
	 * 
	 * @param merchandiseId
	 * @param quantity
	 * @throws CustomDBMSException
	 */
	public void insertVendorPayment(int merchandiseId, int quantity)
			throws CustomDBMSException {

		CallableStatement insertIntoMechandiseStockStatement = null;
		Connection dbConnection = dbConnectionUtil.getConnection();

		try {
			insertIntoMechandiseStockStatement = dbConnection
					.prepareCall(insertVendorPaymentQuery);
			insertIntoMechandiseStockStatement.setInt(1, merchandiseId);
			insertIntoMechandiseStockStatement.setInt(2, this.staffId);
			insertIntoMechandiseStockStatement.setInt(3, quantity);
			insertIntoMechandiseStockStatement.execute();
			insertIntoMechandiseStockStatement.close();

			dbConnection.commit();
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got an SQL error while inserting into vendor payment accounts..",
					e);
		} finally {
			this.dbConnectionUtil.closeConnection(dbConnection);
		}

	}

	public int checkMerchandiseAvaliableInStore(int storeId, int merchandiseId)
			throws CustomDBMSException {

		boolean available = false;
		PreparedStatement getStoreMerchandiseDetailsStatement = null;
		ResultSet inventoryResultSet = null;
		int invQuantity = 0;

		Connection dbConnection = dbConnectionUtil.getConnection();

		try {
			// Get the ware house details about merchandise
			getStoreMerchandiseDetailsStatement = dbConnection
					.prepareStatement(getMerchandiseStockDetailsFromStore);
			getStoreMerchandiseDetailsStatement.setInt(1, merchandiseId);
			getStoreMerchandiseDetailsStatement.setInt(2, storeId);
			inventoryResultSet = getStoreMerchandiseDetailsStatement
					.executeQuery();
			if (inventoryResultSet.next()) {
				invQuantity = inventoryResultSet.getInt(3);
			} else {
				invQuantity = NOT_AVAILABLE_IN_STORE;
			}

			inventoryResultSet.close();
			getStoreMerchandiseDetailsStatement.close();

		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Got SQLexception while retrieving the merchandise "
							+ merchandiseId + " from Store id " + storeId, e);
		} finally {
			dbConnectionUtil.closeConnection(dbConnection);
		}

		return invQuantity;
	}

	public void fulfillOrdersCLI() throws CustomDBMSException {

		List<VendorMerchandiseOrder> placedOrders = null;
		int availableQuantityInWarehouse = 0;

		placedOrders = getPlacedOrderIds();
                System.out.println("Orders :::\n" + placedOrders);
		for (VendorMerchandiseOrder vendorOrder : placedOrders) {
			// CHeck the quantity availablke in store
			availableQuantityInWarehouse = checkMerchandiseAvaliableInStore(
					DBMSConstants.WAREHOUSE_ID, vendorOrder.getMerchandiseId());
			if (availableQuantityInWarehouse >= vendorOrder.getQuantity()) {
				// Merchandise available in store. Move from warehouse to store
				moveMerchandiseFromWarehouseToStore(vendorOrder.getStoreId(),
						vendorOrder.getMerchandiseId(),
						vendorOrder.getQuantity());
			} else {
				if (availableQuantityInWarehouse == NOT_AVAILABLE_IN_STORE) {
					// Data not available in store. Buy data into warehouse
					purchaseFromVendorToWarehouse(
							vendorOrder.getMerchandiseId(),
							vendorOrder.getQuantity(), INSERT_IN_STORE);
				} else {
					// Move data from warehouse to store
					purchaseFromVendorToWarehouse(
							vendorOrder.getMerchandiseId(),
							vendorOrder.getQuantity()
									+ availableQuantityInWarehouse,
							UPDATE_IN_STORE);
				}
				moveMerchandiseFromWarehouseToStore(vendorOrder.getStoreId(),
						vendorOrder.getMerchandiseId(),
						vendorOrder.getQuantity());
			}
			changeOrderStatus(vendorOrder.getOrderId(),
					vendorOrder.getMerchandiseId(),
					DBMSConstants.MERCHANDISE_IN_STORE_STATUS);

			insertVendorPayment(vendorOrder.getMerchandiseId(),
					vendorOrder.getQuantity());
		}

	}

	
	public void finalCLI() throws CustomDBMSException {

		String menuChoice = null;
		boolean done = false;

		while (!done) {
			DBMSUtils.printOnCLI("Choose the action");
			DBMSUtils.printOnCLI("1 Fulfil Orders");
			menuChoice = DBMSUtils.promptInputFromCLI("Enter your choice",
					DBMSFieldsEnum.INT);
			switch (Integer.parseInt(menuChoice)) {
			case 1:
				fulfillOrdersCLI();
				done = true;
				break;
			default:
				DBMSUtils.printOnCLI("Invalid choice. Please try again");
				break;
			}

		}

	}

	
	public static void main(String[] args) throws Exception {

		MerchandiseStocker m = new MerchandiseStocker(2);

		List<VendorMerchandiseOrder> orders = m.getPlacedOrderIds();
		System.out.println("Orders :::\n" + orders);
		m.fulfillOrdersCLI();

	}

}
