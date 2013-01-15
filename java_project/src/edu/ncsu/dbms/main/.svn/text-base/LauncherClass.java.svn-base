package edu.ncsu.dbms.main;

import edu.ncsu.dbms.constants.DBMSFieldsEnum;
import edu.ncsu.dbms.constants.DBMSLoggingLevelsEnum;
import edu.ncsu.dbms.dbutil.DBMSUtils;
import edu.ncsu.dbms.exception.CustomDBMSException;
import edu.ncsu.dbms.logger.DBMSLogger;
import edu.ncsu.dbms.roles.Accountant;
import edu.ncsu.dbms.roles.CEO;
import edu.ncsu.dbms.roles.Contractor;
import edu.ncsu.dbms.roles.Manager;
import edu.ncsu.dbms.roles.MerchandiseStocker;
import edu.ncsu.dbms.roles.SalesPerson;
import edu.ncsu.dbms.roles.Staff;

/**
 * Launcher class for our application
 * 
 * 
 * 
 */
public class LauncherClass {

	private static DBMSLogger logger = DBMSLogger
			.getInstance(DBMSLoggingLevelsEnum.ERROR);

	public static void main(String[] args) {

		String staffIdString = null;
		Staff staff = null;

		DBMSUtils
				.printOnCLI("Welcome to the Book Management STore of dbAmateurs...");

		String menuChoice = null;
		boolean done = false;

		try {

			//Get the staff id from user
			staffIdString = DBMSUtils.promptInputFromCLI(
					"Please enter your staff id.", DBMSFieldsEnum.INT);
            // Get the role from user
			while (!done) {
				DBMSUtils.printOnCLI("Choose a role");
				DBMSUtils.printOnCLI("1 Sales Person Role");
				DBMSUtils.printOnCLI("2 Contractor Role");
				DBMSUtils.printOnCLI("3 CEO role");
				DBMSUtils.printOnCLI("4 Accountant Role");
				DBMSUtils.printOnCLI("5 Merchandise Stocker");
				DBMSUtils.printOnCLI("6 Store Manager");
				menuChoice = DBMSUtils.promptInputFromCLI("Enter your choice",
						DBMSFieldsEnum.INT);
				switch (Integer.parseInt(menuChoice)) {
				case 1:
					staff = new SalesPerson(Integer.parseInt(staffIdString));
					done = true;
					break;
				case 2:
					staff = new Contractor(Integer.parseInt(staffIdString));
					done = true;
					break;
				case 3:
					staff = new CEO(Integer.parseInt(staffIdString));
					done = true;
					break;
				case 4:
					staff = new Accountant(Integer.parseInt(staffIdString));
					done = true;
					break;
				case 5:
					staff = new MerchandiseStocker(
							Integer.parseInt(staffIdString));
					done = true;
					break;
				case 6:
					staff = new Manager(
							Integer.parseInt(staffIdString));
					done = true;
					break;
				default:
					DBMSUtils.printOnCLI("Invalid choice. Please try again");
					break;
				}

			}
            // Call the role's CLI
			staff.finalCLI();

		} catch (Exception e) {
			//e.printStackTrace();
			DBMSUtils.printOnCLI("Got the error - '" + e.getMessage()
					+ "'. Please try again...");
		}

	}

}
