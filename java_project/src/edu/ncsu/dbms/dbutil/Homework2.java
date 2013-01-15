package edu.ncsu.dbms.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.ncsu.dbms.constants.DBMSConstants;
import edu.ncsu.dbms.exception.CustomDBMSException;


public class Homework2 {
private static final String jdbcURL = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";

// Put your oracle ID and password here
private static final String user = "syagna";
private static final String password = "qazwsx123";

private static Connection connection = null;
private static Statement statement = null;
private static ResultSet result = null;

public static void main(String[] args) {
initialize();

try {
boolean isOverWeight1 = checkOverWeight("Max");
// ************************************************************************	

// Set auto-commit to false so that the changes
// are not commited automatically tothe database.
connection.setAutoCommit(false);

// modifyALittleBit1();	// Failure case
modifyALittleBit2();	// Success case

boolean isOverWeight2 = checkOverWeight("Lucy");


if(isOverWeight1 == isOverWeight2) {
System.out.println("Success");
// The result was successful. Commit the changes to the table.
connection.commit();
}
else {
System.out.println("Failure");
// The result was failure. Rollback the changes
connection.rollback();
}

} catch (SQLException e) {
e.printStackTrace();
}
// ***********************************************************************
close();
}
private static void initialize() {
try {
/**
Class.forName("oracle.jdbc.driver.OracleDriver");
connection = DriverManager.getConnection(jdbcURL, user, password);
statement = connection.createStatement();
*/
    DBMSConnectionUtil dbmsConnectionUtil = DBMSConnectionUtil.getInstance();
	connection = dbmsConnectionUtil.getConnection();
	statement = connection.createStatement();
	
try {
statement.executeUpdate("DROP TABLE Cats");
statement.executeUpdate("DROP TABLE FatnessProperty");
} catch (SQLException e) {}

statement.executeUpdate("CREATE TABLE Cats (CName VARCHAR(20), " +
"Type VARCHAR(30), Age INTEGER, Weight FLOAT, Sex CHAR(1))");

statement.executeUpdate("INSERT INTO Cats VALUES ('Oscar', 'Egyptian Mau'," +
" 3, 23.4, 'F')");
statement.executeUpdate("INSERT INTO Cats VALUES ('Max', 'Turkish Van Cats'," +
" 2, 21.8, 'M')");
statement.executeUpdate("INSERT INTO Cats VALUES ('Tiger', 'Russian Blue'," +
" 1, 13.3, 'M')");
statement.executeUpdate("INSERT INTO Cats VALUES ('Sam', 'Persian Cats'," +
" 5, 24.3, 'M')");
statement.executeUpdate("INSERT INTO Cats VALUES ('Simba', 'American Bobtail'," +
" 3, 19.8, 'F')");
statement.executeUpdate("INSERT INTO Cats VALUES ('Lucy', 'Turkish Angora Cats'," +
"2, 22.4, 'F')");

statement.executeUpdate("CREATE TABLE FatnessProperty (Type VARCHAR(30), " +
"InitialWeight FLOAT, Rate FLOAT)");
statement.executeUpdate("INSERT INTO FatnessProperty VALUES ('Egyptian Mau', 5.0, 9.4)");
statement.executeUpdate("INSERT INTO FatnessProperty VALUES ('Turkish Van Cats', 4.0, 8.5)");
statement.executeUpdate("INSERT INTO FatnessProperty VALUES ('Russian Blue', 4.0, 10.2)");
statement.executeUpdate("INSERT INTO FatnessProperty VALUES ('Persian Cats', 3.0, 9.0)");
statement.executeUpdate("INSERT INTO FatnessProperty VALUES ('American Bobtail', 5.0, 11.1)");
statement.executeUpdate("INSERT INTO FatnessProperty VALUES ('Turkish Angora Cats', 4.0, 8.4)");
} catch (CustomDBMSException e) {
e.printStackTrace();
} catch (SQLException e) {
e.printStackTrace();
}
}
private static boolean checkOverWeight(String catName) {
try {
result = statement.executeQuery("SELECT Weight, (InitialWeight + Age * Rate) AS " +
"AverageWeight FROM Cats, FatnessProperty WHERE Cats.Type = FatnessProperty.Type AND Cats.CName " +
"LIKE '" + catName + "%'");

if(result.next()) {
return (result.getFloat("Weight") > result.getFloat("AverageWeight"));
}
throw new RuntimeException(catName + " cannot be found.");
} catch (SQLException e) {
e.printStackTrace();
}
return false;
}
private static void modifyALittleBit1() throws SQLException {
statement.executeUpdate("UPDATE Cats SET Weight = 19.3 WHERE CName LIKE 'Lucy%'");
statement.executeUpdate("UPDATE FatnessProperty SET Rate = 9.3 WHERE Type = 'Turkish Angora Cats'");
}
private static void modifyALittleBit2() throws SQLException {
statement.executeUpdate("UPDATE Cats SET Weight = 22.8 WHERE CName LIKE 'Lucy%'");
statement.executeUpdate("UPDATE FatnessProperty SET Rate = 9.3 WHERE Type = 'Turkish Angora Cats'");
}
private static void close() {
if(connection != null) {
try {
connection.close();
} catch (SQLException e) {
e.printStackTrace();
}
}
if(statement != null) {
try {
statement.close();
} catch (SQLException e) {
e.printStackTrace();
}
}
if(result != null) {
try {
result.close();
} catch (SQLException e) {
e.printStackTrace();
}
}
}
}
