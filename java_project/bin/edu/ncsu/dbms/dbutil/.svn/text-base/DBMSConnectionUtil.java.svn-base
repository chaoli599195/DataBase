package edu.ncsu.dbms.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.ncsu.dbms.constants.DBMSConstants;
import edu.ncsu.dbms.exception.CustomDBMSException;

/**
 * This class is a Singleton class that handles all DBCOnnection requests
 * 
 */

public class DBMSConnectionUtil {

	private static DBMSConnectionUtil dbmsConnectionUtil = null;

	private DBMSConnectionUtil() {

	}

	/**
	 * Return the singleton instance for the class
	 * 
	 * @return {@link DBMSConnectionUtil}
	 */
	public static DBMSConnectionUtil getInstance() {

		if (dbmsConnectionUtil == null) {
			dbmsConnectionUtil = new DBMSConnectionUtil();
		}

		return dbmsConnectionUtil;

	}

	/**
	 * Gets a connection for the DB
	 * 
	 * @param DBMS_USER_NAME
	 * @param DBMS_PASSWORD
	 * @return {@link Connection}
	 * @throws CustomDBMSException
	 */
	public Connection getConnection()
			throws CustomDBMSException {

		Connection connection = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(
					DBMSConstants.JDBC_URL, DBMSConstants.DBMS_USER_NAME, DBMSConstants.DBMS_PASSWORD);
			connection.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			throw new CustomDBMSException("Unable to find the Oracle driver", e);
		} catch (SQLException e) {
			throw new CustomDBMSException(
					"Caught an SQL exception while creating a connection", e);
		}

		return connection;
		
	}
	
	public void closeConnection(Connection dbConnection){
		
		if(dbConnection != null){
			try {
				dbConnection.close();
			} catch (SQLException e) {
			    
			}
		}
		
	}

}
