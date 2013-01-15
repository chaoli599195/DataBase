create or replace 
Procedure DeleteTable(table_name IN VARCHAR)
AS
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE ' || table_name;
  DBMS_OUTPUT.PUT_LINE('Dropped ' || table_name);
EXCEPTION
  WHEN OTHERS THEN
     IF SQLCODE != -942 THEN
        RAISE;
     ELSE   
         DBMS_OUTPUT.PUT_LINE('Table ' || table_name || ' does not exist..');
     END IF;
END DeleteTable;
/
/* Delete chain for Merchandise */
EXEC deletetable('OrderDetails');
EXEC deletetable('merchandisestock');


/* Delete chain for Vendors */
EXEC deletetable('MaintainedForVendors');
EXEC deletetable('CheckToVendors');


/* Delete chain for Store */
EXEC deletetable('Works_in');
EXEC deletetable('Store');

/* Delete chain related to customers */
EXEC deletetable('place_order');
EXEC deletetable('MaintainedForCustomer');
EXEC deletetable('BillToCustomers');
EXEC deletetable('CustomerBilling_account');
EXEC deletetable('Customers');

/* Delete chain for Staff */
EXEC deletetable('Member_of');
EXEC deletetable('ManagedBy');


EXEC deletetable('Department');
EXEC deletetable('Orders');

EXEC deletetable('VendorPaymentAccount');
EXEC deletetable('VendorPurchases');
EXEC deletetable('merchandise');
EXEC deletetable('vendors');
EXEC deletetable('Staff');
EXEC deletetable('Roles');

-- Drop sequences
Drop sequence Customer_Id_Seq;
Drop sequence Staff_Id_Seq;
Drop sequence Merchandise_Id_Seq;
Drop sequence Store_Id_Seq;
Drop sequence Department_Id_Seq;
Drop sequence Vendor_Id_Seq;
Drop sequence Order_Id_Seq;
Drop sequence customerbilling_Id_Seq;
Drop sequence Vendoraccount_Id_seq;
Drop sequence Vendorpurchase_Id_seq;


