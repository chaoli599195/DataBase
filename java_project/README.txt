This file gives instructions to compile and run

Files
------------------------------

System Configuration
------------------------------
1. EOS box( Linux RHEL5)
2. jdk-1.6 installed and javac should be added to path


Preinstall
----------------------------
1) Run db scripts in the following order(present sql_files/)
	a. create_tables.sql
	b. all other sql files except datainstall.sql
	c. datainstall.sql
2) Update username and password in BookStore_Management/src/edu/ncsu/dbms/constants/DBMSConstants.java
	DBMS_USER_NAME and DBMS_PASSWORD fields respectively
		
Compile
----------------------------
To compile run the following command from eos terminal

cd BookStore_Management
javac -cp ojdbc14.jar -d bin src/edu/ncsu/dbms/constants/*.java src/edu/ncsu/dbms/custom/objects/*.java src/edu/ncsu/dbms/dbutil/*.java src/edu/ncsu/dbms/exception/*.java src/edu/ncsu/dbms/logger/*.java src/edu/ncsu/dbms/main/*.java src/edu/ncsu/dbms/roles/*.java


Run
----------------------------
To launch application run following command from eos terminal

cd BookStore_Management
java -cp ojdbc14.jar:bin edu.ncsu.dbms.main.LauncherClass

Sample Run
-----------------------------
eos%
eos%
eos% java -cp ojdbc14.jar:bin edu.ncsu.dbms.main.LauncherClass
--->Welcome to the Book Management STore of dbAmateurs...
--->Please enter your staff id.
3
--->Choose a role
--->1 Sales Person Role
--->2 Contractor Role
--->3 CEO role
--->4 Accountant Role
--->5 Merchandise Stocker
--->Enter your choice
2
--->Choose the action
--->1 Create Vendor
--->2 Update Vendor
--->3 Delete Vendor
--->4 Add Merchandise
--->5 Update Merchandise
--->Enter your choice
1
--->Enter the name of the Vendor
w
--->Enter the address of the Vendor
q
--->Enter the phone number of the Vendor
1234567890
--->Enter the bank account number of the Vendor
123456789012
--->Enter the start contract date of the Vendor in 'dd/mm/yyyy' format
11/11/2012
--->Enter the end contract date of the Vendor in 'dd/mm/yyyy' format
11/12/2034
eos%
eos%


