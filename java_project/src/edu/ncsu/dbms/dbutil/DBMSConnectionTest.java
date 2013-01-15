package edu.ncsu.dbms.dbutil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;

import oracle.jdbc.OracleTypes;
import oracle.net.nt.ConnStrategy;

import edu.ncsu.dbms.constants.DBMSConstants;

public class DBMSConnectionTest {

	public static void main(String[] args) throws Exception {

		DBMSConnectionUtil dbmsConnectionUtil = DBMSConnectionUtil
				.getInstance();
		Connection connection = dbmsConnectionUtil.getConnection();

		String jobquery = "begin Insert_Merchadise(?, ?, ?, ?, ?);end; ";
		CallableStatement callStmt = connection.prepareCall(jobquery);
		//callStmt.registerOutParameter(3, OracleTypes.CURSOR);
		callStmt.setString(1, "John");
		callStmt.setInt(2, 1211);
		callStmt.setString(3, "Shakespear");
		callStmt.setInt(2, 50);
		callStmt.setInt(2, 1);
		callStmt.execute();
		
		connection.close();

	}

}
