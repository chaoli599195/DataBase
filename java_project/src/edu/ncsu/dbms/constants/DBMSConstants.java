package edu.ncsu.dbms.constants;

public class DBMSConstants {

	public static final String JDBC_URL = "jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl";

	public static final String DBMS_USER_NAME = "syagna";

	public static final String DBMS_PASSWORD = "qazwsx123";

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	public static final long MAX_BANK_ACCOUNT_NUMBER = 999999999999L;

	public static final long MIN_BANK_ACCOUNT_NUMBER = 100000000000L;

	public static final long MAX_CREDIT_CARD_NUMBER = 9999999999999999L;

	public static final long MIN_CREDIT_CARD_NUMBER = 1000000000000000L;

	public static final long MAX_PHONE_NUMBER = 9999999999L;

	public static final long MIN_PHONE_NUMBER = 1000000000L;

	public static final long MAX_SSL_NUMBER = 1000000000L;

	public static final long MIN_SSL_NUMBER = 99999999L;

	public static final String CLI_PATTERN = "--->";

	public static final String VALID_DATE_REGEX_PATTERN = "\\d{1,2}/\\d{1,2}/\\d{4}";

	public static final String DATE_DELIMITER = "/";

	public static final String CUSTOMER_ORDER_PLACE_SATUS = "placed";

	public static final String MERCHANDISE_IN_STORE_STATUS = "instore";
	
	public static final String CUSTOMER_ORDER_FULFILLED_SATUS = "fulfilled";

	public static final int WAREHOUSE_ID = 1;

	public static final int WAREHOUSE_ORDER_MULTIPLIER = 1;

}
