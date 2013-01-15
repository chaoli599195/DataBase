
CREATE   TABLE   Customers (
CustomerID  NUMBER  CHECK(CustomerID > 0) ,
SSN NUMBER(10,0) UNIQUE CHECK(SSN > 99999999),
gender CHAR(1) NOT NULL CHECK(gender = 'M' OR gender = 'F'),
DOB DATE NOT NULL,
name VARCHAR2(30) NOT NULL,
phone_number NUMBER(10,0) CHECK(phone_number > 999999999),
email VARCHAR2(30) NOT NULL,
address VARCHAR2(100) NOT NULL,
credit_card NUMBER(16,0) NOT NULL  CHECK(credit_card > 999999999999999),
PRIMARY KEY (CustomerID),
CONSTRAINT cust_name_and_address UNIQUE( name, address )
);

/*
CREATE TABLE Roles(
RoleName VARCHAR2(30),
Description VARCHAR2(100) NOT NULL,
PRIMARY KEY(RoleName) 
);*/

CREATE TABLE Staff(
StaffID NUMBER CHECK(StaffID > 0),
SSN NUMBER(10,0) UNIQUE NOT NULL CHECK(SSN > 99999999),
name VARCHAR2(30) NOT NULL,
job_title VARCHAR2(30),
gender CHAR(1) NOT NULL CHECK(gender = 'M' OR gender = 'F'),
DOB DATE NOT NULL,
address VARCHAR2(100) NOT NULL,
phone_number NUMBER(10,0) CHECK(phone_number > 999999999),
Salary NUMBER(10,2) NOT NULL,
PRIMARY KEY (StaffID),
CONSTRAINT staff_name_and_address UNIQUE( name, address )
);

CREATE TABLE Store(
StoreID NUMBER CHECK(StoreID >0),
name VARCHAR2(30) NOT NULL,
address VARCHAR2(100) NOT NULL,
phone_number NUMBER(10,0) CHECK(phone_number > 999999999),
PRIMARY KEY(StoreID)
);

CREATE TABLE Department(
DeptID NUMBER CHECK(DeptID > 0),
name VARCHAR2(30) NOT NULL,
PRIMARY KEY(DeptID)
);

CREATE TABLE Orders(
OrderID NUMBER CHECK (OrderID > 0),
DT DATE NOT NULL,
PRIMARY KEY(OrderID)
);


CREATE TABLE Vendors(
VendorID NUMBER CHECK(VendorID>0),
name VARCHAR2(30) NOT NULL,
address VARCHAR2(100) NOT NULL,
phone_number NUMBER(10,0) CHECK (phone_number > 999999999),
contractStartDate DATE NOT NULL,
contractEndDate  DATE NOT NULL,
bankAccount_number NUMBER NOT NULL CHECK(bankAccount_number > 0),
PRIMARY KEY(VendorID),
CHECK(contractEndDate > contractStartDate),
CONSTRAINT vendors_name_and_address UNIQUE( name, address )
);


CREATE TABLE Merchandise(
MerchandiseID NUMBER CHECK(MerchandiseID > 0),
name VARCHAR2(100) NOT NULL,
ISBN NUMBER(10,0) UNIQUE NOT NULL,
author VARCHAR2(30) NOT NULL,
price REAL NOT NULL CHECK(price > 0),
vendorID NUMBER,
PRIMARY KEY (MerchandiseID),
FOREIGN KEY(VendorID)
   REFERENCES Vendors(VendorID)
   ON DELETE CASCADE
);


CREATE TABLE MerchandiseStock(
MerchandiseID NUMBER, 
StoreID NUMBER,
quantity NUMBER NOT NULL,
PRIMARY KEY(MerchandiseID, StoreID),
FOREIGN KEY(MerchandiseID)
   REFERENCES Merchandise(MerchandiseID)
   ON DELETE CASCADE,
FOREIGN KEY(StoreID)
    REFERENCES Store(StoreID)
    ON DELETE CASCADE
);


CREATE TABLE OrderDetails(
OrderID NUMBER,
MerchandiseID NUMBER,
Status VARCHAR2(10) NOT NULL,
Quantity NUMBER NOT NULL,
PRIMARY KEY(OrderID, MerchandiseID),
FOREIGN KEY(OrderID)
   REFERENCES Orders(OrderID)
   ON DELETE CASCADE,
FOREIGN KEY(MerchandiseID)
   REFERENCES Merchandise(MerchandiseID)
   ON DELETE CASCADE
);


CREATE TABLE Place_Order(
OrderID NUMBER,
CustomerID NUMBER,
StaffID NUMBER,
PRIMARY KEY(OrderID),
FOREIGN KEY(OrderID)
   REFERENCES Orders(OrderID)
   ON DELETE CASCADE,
FOREIGN KEY(CustomerID)
   REFERENCES Customers(CustomerID)
   ON DELETE CASCADE,
FOREIGN KEY(StaffID)
   REFERENCES Staff(StaffID)
   ON DELETE CASCADE
);


CREATE TABLE Works_in(
StaffID NUMBER,
StoreID NUMBER,
PRIMARY KEY(StaffID),
FOREIGN KEY(StaffID)
   REFERENCES Staff(StaffID)
   ON DELETE CASCADE,
FOREIGN KEY(StoreID)
   REFERENCES Store(StoreID)
   ON DELETE CASCADE
);

CREATE TABLE Member_of(
StaffID NUMBER,
DeptID NUMBER,
PRIMARY KEY(StaffID),
FOREIGN KEY(StaffID)
   REFERENCES Staff(StaffID)
   ON DELETE CASCADE,
FOREIGN KEY(DeptID)
   REFERENCES Department(DeptID)
   ON DELETE CASCADE
);



CREATE TABLE VendorPurchases(
VendorPurchaseID NUMBER,
MerchandiseID NUMBER,
StaffID NUMBER,
DT DATE NOT NULL,
quantity NUMBER NOT NULL,
PRIMARY KEY(VendorPurchaseID),
FOREIGN KEY(MerchandiseID )
   REFERENCES Merchandise(MerchandiseID)
   ON DELETE CASCADE,
FOREIGN KEY(StaffID)
   REFERENCES Staff(StaffID)
   ON DELETE CASCADE
);


CREATE TABLE ManagedBy(
EmployeeStaffID NUMBER ,
ManagerStaffID NUMBER NOT NULL CHECK (ManagerStaffID>0),
PRIMARY KEY(EmployeeStaffID),
FOREIGN KEY(EmployeeStaffID)
   REFERENCES Staff(StaffID)
   ON DELETE CASCADE
);


CREATE TABLE CustomerBilling_account(
CustomerBillingID NUMBER CHECK(CustomerBillingID>0),
orderid NUMBER,
due_date DATE NOT NULL,
amount REAL NOT NULL,
date_paid DATE,
PRIMARY KEY(CustomerBillingID),
FOREIGN KEY(orderid) 
references Orders(orderid)
);

CREATE TABLE VendorPaymentAccount(
VendorPaymentID NUMBER CHECK(VendorPaymentID > 0),
VendorPurchaseID NUMBER,
due_date DATE NOT NULL,
amount REAL NOT NULL,
date_paid DATE,
PRIMARY KEY(VendorPaymentID),
FOREIGN KEY(VendorPurchaseID) 
 references VendorPurchases(VendorPurchaseID)
);

CREATE TABLE MaintainedForVendors(
VendorPaymentID NUMBER,
VendorID NUMBER,
PRIMARY KEY(VendorPaymentID),
FOREIGN KEY(VendorPaymentID)
   REFERENCES VendorPaymentAccount(VendorPaymentID)
   ON DELETE CASCADE,
FOREIGN KEY(VendorID)
   REFERENCES Vendors(VendorID)
   ON DELETE CASCADE
);


CREATE TABLE MaintainedForCustomer(
CustomerBillingID NUMBER,
CustomerID NUMBER,
PRIMARY KEY(CustomerBillingID),
FOREIGN KEY(CustomerBillingID)
   REFERENCES CustomerBilling_account(CustomerBillingID)
   ON DELETE CASCADE,
FOREIGN KEY(CustomerID)
   REFERENCES Customers(CustomerID)
   ON DELETE CASCADE
);

CREATE TABLE BillToCustomers(
CustomerBillingID NUMBER,
StaffID NUMBER,
PRIMARY KEY(CustomerBillingID),
FOREIGN KEY(CustomerBillingID)
   REFERENCES CustomerBilling_account(CustomerBillingID)
   ON DELETE CASCADE,
FOREIGN KEY(StaffID)
   REFERENCES Staff(StaffID)
   ON DELETE CASCADE
);

CREATE TABLE CheckToVendors(
VendorPaymentID NUMBER,
StaffID NUMBER REFERENCES Staff(StaffID),
PRIMARY KEY(VendorPaymentID),
FOREIGN KEY(VendorPaymentID)
    REFERENCES VendorPaymentAccount(VendorPaymentID) 
   ON DELETE CASCADE,
FOREIGN KEY(StaffID)
   REFERENCES Staff(StaffID)
   ON DELETE CASCADE
);

-- Sequences and triggers for Primary keys

CREATE SEQUENCE Customer_Id_Seq 
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER customer_insert_trigger
BEFORE INSERT
ON Customers
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT Customer_Id_seq.nextval INTO :NEW.CustomerID FROM dual;
END;
/


CREATE SEQUENCE Staff_Id_Seq 
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER staff_insert_trigger
BEFORE INSERT
ON Staff
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT Staff_Id_seq.nextval INTO :NEW.StaffID FROM dual;
END;
/


CREATE SEQUENCE Merchandise_Id_Seq 
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER merchandise_insert_trigger
BEFORE INSERT
ON Merchandise
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT Merchandise_Id_seq.nextval INTO :NEW.MerchandiseID FROM dual;
END;
/


CREATE SEQUENCE Store_Id_Seq 
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER store_insert_trigger
BEFORE INSERT
ON Store
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT Store_Id_seq.nextval INTO :NEW.StoreID FROM dual;
END;
/



CREATE SEQUENCE Department_Id_Seq 
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER department_insert_trigger
BEFORE INSERT
ON Department
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT Department_Id_seq.nextval INTO :NEW.DeptID FROM dual;
END;
/


CREATE SEQUENCE Vendor_Id_Seq 
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER vendor_insert_trigger
BEFORE INSERT
ON Vendors
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT Vendor_Id_seq.nextval INTO :NEW.VendorID FROM dual;
END;
/


CREATE SEQUENCE Order_Id_Seq 
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER order_insert_trigger
BEFORE INSERT
ON Orders
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT Order_Id_seq.nextval INTO :NEW.OrderID FROM dual;
END;
/


CREATE SEQUENCE customerbilling_Id_Seq 
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER customerbilling_insert_trigger
BEFORE INSERT
ON Customerbilling_account
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT customerbilling_Id_seq.nextval INTO :NEW.CustomerBillingID FROM dual;
END;
/

CREATE SEQUENCE Vendoraccount_Id_Seq 
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER vendoraccount_insert_trigger
BEFORE INSERT
ON VendorPaymentAccount
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT Vendoraccount_Id_seq.nextval INTO :NEW.VendorPaymentID FROM dual;
END;
/

CREATE SEQUENCE VendorPurchase_Id_Seq
start with 1
increment by 1;

CREATE OR REPLACE TRIGGER vendorpurchase_insert_trigger
BEFORE INSERT
ON VendorPurchases
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT VendorPurchase_Id_seq.nextval INTO :NEW.VendorPurchaseID FROM dual;
END;
/