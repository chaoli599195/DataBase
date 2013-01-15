package edu.ncsu.dbms.dbutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.MinimalHTMLWriter;

import edu.ncsu.dbms.constants.DBMSConstants;
import edu.ncsu.dbms.constants.DBMSFieldsEnum;
import edu.ncsu.dbms.exception.CustomDBMSException;
import edu.ncsu.dbms.logger.DBMSLogger;

/**
 * Utility class for the project. Contains frequently used methods
 * 
 */

public class DBMSUtils {

	private static DBMSLogger logger = DBMSLogger.getInstance();

	/**
	 * Returns true if there is a match between the string with the passed regex
	 * 
	 * @param input
	 * @param pattern
	 * @return boolean
	 */
	public static boolean matchRegex(String input, String pattern) {

		Pattern regexPattern = null;
		Matcher matcher = null;
		boolean match = false;

		if (input == null || input.isEmpty()) {
			logger.error("The passed input to matchRegex is empty");
		}
		if (pattern == null || pattern.isEmpty()) {
			logger.error("The passed pattern to matchRegex is empty");
		}

		regexPattern = Pattern.compile(pattern);
		matcher = regexPattern.matcher(input);
		match = matcher.matches();

		return match;

	}

	/**
	 * Method that converts the passed string into SQL date format. The string
	 * is expected to be in the format "dd/MM/yyyy"
	 * 
	 * @param dateAsString
	 * @return java.sql.Date
	 * @throws CustomDBMSException
	 */
	public static java.sql.Date getSqlDate(String dateAsString)
			throws CustomDBMSException {

		Date passedDate = new Date();
		java.sql.Date returnSQLdate = null;
		DateFormat dateFormat = new SimpleDateFormat(DBMSConstants.DATE_FORMAT);
		dateFormat.setLenient(false);

		if (dateAsString == null) {
			throw new CustomDBMSException("Invalid string passed as parameter");
		}

		try {
			passedDate = dateFormat.parse(dateAsString);
		} catch (ParseException e) {
			throw new CustomDBMSException(
					"The passed date is of invalid format.. Expected format is "
							+ DBMSConstants.DATE_FORMAT);
		}

		returnSQLdate = new java.sql.Date(passedDate.getTime());

		return returnSQLdate;

	}

	public static String getStringFormOfSQLDate(java.sql.Date date)
			throws CustomDBMSException {

		DateFormat dateFormat = new SimpleDateFormat(DBMSConstants.DATE_FORMAT);
		dateFormat.setLenient(false);
		String dateText = null;

		if (date == null) {
			throw new CustomDBMSException("Invalid date passed as parameter");
		}

		dateText = dateFormat.format(date);

		return dateText;
	}

	/**
	 * Method that verifies the passed bank account
	 * 
	 * @param bankAccount
	 * @return boolean
	 */
	public static boolean verifyBankAccount(long bankAccount) {

		if (bankAccount == 11111111111111L) {
			return false;
		}
		return true;
	}

	/**
	 * This method verifies the passed credit card details
	 * 
	 * @param creditCardNo
	 * @return
	 */
	public static boolean verifyCreditCard(long creditCardNo) {
		if (creditCardNo == 1111111111111111L) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if passed string is valid int or not
	 * 
	 * @param passedString
	 * @return boolean
	 */
	public static boolean validateStringAsInt(String passedString) {

		boolean valid = true;

		valid = validateString(passedString);
		if (valid) {
			try {
				Integer.parseInt(passedString);
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		return valid;

	}

	/**
	 * Checks if passed string is valid float or not
	 * 
	 * @param passedString
	 * @return boolean
	 */
	public static boolean validateStringAsFloat(String passedString) {

		boolean valid = true;

		valid = validateString(passedString);
		if (valid) {
			try {
				Float.parseFloat(passedString);
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		return valid;

	}

	/**
	 * Checks if passed string is valid or not
	 * 
	 * @param passedString
	 * @return boolean
	 */
	public static boolean validateString(String passedString) {

		boolean valid = true;

		if (passedString == null || passedString.isEmpty()) {
			valid = false;
		}

		return valid;

	}

	/**
	 * Validates the passed string as name field
	 * 
	 * @param passedName
	 * @return boolean
	 */
	public static boolean validateName(String passedName) {

		boolean valid = true;
		valid = validateString(passedName);

		return valid;

	}

	/**
	 * Validates the passed string as address field
	 * 
	 * @param passedAddress
	 * @return
	 */
	public static boolean validateAddress(String passedAddress) {

		boolean valid = true;
		valid = validateString(passedAddress);

		return valid;

	}

	/**
	 * Validates the passed string as bank account number field
	 * 
	 * @param bankAccountString
	 * @return boolean
	 */
	public static boolean validateBankAccount(String bankAccountString) {

		boolean valid = true;
		long bankAccountNum = 0;

		valid = validateString(bankAccountString);
		if (valid) {
			try {
				bankAccountNum = Long.parseLong(bankAccountString);
				if (bankAccountNum < DBMSConstants.MIN_BANK_ACCOUNT_NUMBER
						|| bankAccountNum > DBMSConstants.MAX_BANK_ACCOUNT_NUMBER) {
					valid = false;
				}
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		return valid;

	}

	/**
	 * Validates the passed string as credit card number field
	 * 
	 * @param creditCardNumberString
	 * @return boolean
	 */
	public static boolean validateCreditCardNumber(String creditCardNumberString) {

		boolean valid = true;
		long creditCardNum = 0;

		valid = validateString(creditCardNumberString);
		if (valid) {
			try {
				creditCardNum = Long.parseLong(creditCardNumberString);
				if (creditCardNum < DBMSConstants.MIN_CREDIT_CARD_NUMBER
						|| creditCardNum > DBMSConstants.MAX_CREDIT_CARD_NUMBER) {
					valid = false;
				}
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		return valid;

	}

	/**
	 * Validates the passed string as phone number field
	 * 
	 * @param phoneNumberString
	 * @return boolean
	 */
	public static boolean validatePhoneNumber(String phoneNumberString) {

		boolean valid = true;
		long creditCardNum = 0;

		valid = validateString(phoneNumberString);
		if (valid) {
			try {
				creditCardNum = Long.parseLong(phoneNumberString);
				if (creditCardNum < DBMSConstants.MIN_PHONE_NUMBER
						|| creditCardNum > DBMSConstants.MAX_PHONE_NUMBER) {
					valid = false;
				}
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		return valid;

	}

	/**
	 * Validates the passed string as amount field
	 * 
	 * @param amountString
	 * @return boolean
	 */
	public static boolean validateAmount(String amountString) {

		boolean valid = true;
		float amount = 0;

		valid = validateString(amountString);
		if (valid) {
			try {
				amount = Float.parseFloat(amountString);
				if (amount < 0) {
					valid = false;
				}
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		return valid;

	}

	/**
	 * Validates the passed string as quantity field
	 * 
	 * @param quantityString
	 * @return boolean
	 */
	public static boolean validateQuantity(String quantityString) {

		boolean valid = true;
		int quantity = 0;

		valid = validateString(quantityString);
		if (valid) {
			try {
				quantity = Integer.parseInt(quantityString);
				if (quantity < 0) {
					valid = false;
				}
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		return valid;

	}

	/**
	 * Validates the passed string as date field
	 * 
	 * @param dateAsString
	 * @return boolean
	 */
	public static boolean validateDate(String dateAsString) {

		boolean valid = true;
		DateFormat dateFormat = new SimpleDateFormat(DBMSConstants.DATE_FORMAT);
		dateFormat.setLenient(false);

		valid = validateString(dateAsString);
		if (valid) {
			try {
				dateFormat.parse(dateAsString);
			} catch (ParseException e) {
				valid = false;
			}
			if (!matchRegex(dateAsString,
					DBMSConstants.VALID_DATE_REGEX_PATTERN)) {
				valid = false;
			}
		}

		return valid;

	}

	public static void printOnCLI(String message) {

		System.out.println(DBMSConstants.CLI_PATTERN + message);

	}

	public static String promptInputFromCLI(String message, DBMSFieldsEnum field)
			throws CustomDBMSException {

		boolean done = false;
		String input = null;
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				System.in));

		while (!done) {
			printOnCLI(message);
			try {
				input = inputReader.readLine();
				if(input != null){
					input = input.trim();
				}
			} catch (IOException e) {
				throw new CustomDBMSException(
						"Got an IO error while prompting the user...", e);
			}
			switch (field) {

			case NAME:
				done = validateName(input);
				break;
			case ADDRESS:
				done = validateAddress(input);
				break;
			case DATE:
				done = validateDate(input);
				break;
			case BANK_ACCOUNT:
				done = validateBankAccount(input);
				break;
			case CREDIT_CARD_NUMBER:
				done = validateCreditCardNumber(input);
				break;
			case AMOUNT:
				done = validateAmount(input);
				break;
			case QUANTITY:
				done = validateQuantity(input);
				break;
			case PHONE_NUMBER:
				done = validatePhoneNumber(input);
				break;
			case GENDER:
				done = validateGender(input);
				break;
			case EMAIL:
				done = validateEmail(input);
				break;
			case SSN:
				done = validateSSN(input);
				break;
			case INT:
				done = validateStringAsInt(input);
				break;
			case FLOAT:
				done = validateStringAsFloat(input);
				break;
			}
			if (!done) {
				printOnCLI("Passed input '" + input
						+ "' is invalid for the filed " + field.toString()
						+ ". Please try again");
			}
		}

		return input;

	}

	// validate SSN
	public static boolean validateSSN(String input) {
		boolean valid = true;
		long creditCardNum = 0;

		valid = validateString(input);
		if (valid) {
			try {
				creditCardNum = Long.parseLong(input);
				if (creditCardNum < DBMSConstants.MIN_SSL_NUMBER
						|| creditCardNum > DBMSConstants.MAX_SSL_NUMBER) {
					valid = false;
				}
			} catch (NumberFormatException e) {
				valid = false;
			}
		}

		return valid;
	}

	// This method checks if email is id
	public static boolean validateEmail(String input) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	public static boolean validateGender(String input) {
		if (input == null || input.trim().equals("")
				|| input.trim().length() != 1
				|| (input.charAt(0) != 'M' && input.charAt(0) != 'F')) {
			return false;
		}
		return true;
	}

	public static String promptInputFromCLI(String message,
			DBMSFieldsEnum field, String defaultInput)
			throws CustomDBMSException {

		boolean done = false;
		boolean takeDefault = false;
		String input = null;
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				System.in));

		while (!done) {

			printOnCLI(message + ". Press enter to retain the existing value '"
					+ defaultInput + "'");
			try {
				input = inputReader.readLine();
				if(input != null){
					input = input.trim();
				}
			} catch (IOException e) {
				throw new CustomDBMSException(
						"Got an IO error while prompting the user...", e);
			}

			switch (field) {

			case NAME:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateName(input);
				}
				break;
			case ADDRESS:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateAddress(input);
				}
				break;
			case DATE:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateDate(input);
				}
				break;
			case BANK_ACCOUNT:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateBankAccount(input);
				}
				break;
			case CREDIT_CARD_NUMBER:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateCreditCardNumber(input);
				}
				break;
			case AMOUNT:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateAmount(input);
				}
				break;
			case QUANTITY:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateQuantity(input);
				}
				break;
			case PHONE_NUMBER:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validatePhoneNumber(input);
				}
				break;
			case GENDER:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateGender(input);
				}
				break;
			case EMAIL:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateEmail(input);
				}
				break;
			case SSN:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateSSN(input);
				}
				break;
			case INT:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateStringAsInt(input);
				}
				break;
			case FLOAT:
				takeDefault = validateString(input);
				if (takeDefault) {
					done = validateStringAsFloat(input);
				}
				break;
			}
			if (!takeDefault) {
				printOnCLI("Nothing entered. Taking the default value "
						+ defaultInput);
				input = defaultInput;
				break;
			}
			if (!done) {
				printOnCLI("Passed input '" + input
						+ "' is invalid for the filed " + field.toString()
						+ ". Please try again");
			}
		}

		return input;

	}

	public static void main(String[] args) {

		if (validateDate("11/1/12354")) {
			System.out.println("Date is valid");
		} else {
			System.out.println("Date is invalid");
		}

	}

}
