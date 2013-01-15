create or replace
Procedure insert_vendorpurchases(merchandiseid_in IN vendorpurchases.merchandiseid%TYPE, staffid_in IN vendorpurchases.staffid%TYPE, quantity_in IN vendorpurchases.quantity%TYPE)
AS
   vendorpurchaseid_inserted vendorpurchases.vendorpurchaseid%TYPE;
   vendorpaymentid_inserted vendorpaymentaccount.vendorpaymentid%TYPE;
   purchased_amount vendorpaymentaccount.amount%TYPE;
   provider_vendor maintainedforvendors.vendorid%TYPE;
BEGIN
   select price*quantity_in into purchased_amount from merchandise where merchandise.merchandiseid=merchandiseid_in;
   select merchandise.vendorid into provider_vendor from merchandise where merchandise.merchandiseid=merchandiseid_in;
   insert into vendorpurchases(merchandiseid, staffid, dt, quantity) VALUES(merchandiseid_in, staffid_in, sysdate, quantity_in) RETURNING vendorpurchaseid into vendorpurchaseid_inserted;
   insert into vendorpaymentaccount(vendorpurchaseid, due_date, amount) values(vendorpurchaseid_inserted, last_day(sysdate), purchased_amount) RETURNING vendorpaymentid into vendorpaymentid_inserted;
   insert into maintainedforvendors(vendorpaymentid, vendorid) values(vendorpaymentid_inserted, provider_vendor);
END insert_vendorpurchases;
/
create or replace
procedure update_vendorpurchases(merchandiseid_in vendorpurchases.merchandiseid%TYPE, staffid_in IN vendorpurchases.staffid%TYPE, dt_in IN vendorpurchases.dt%type, quantity_in IN vendorpurchases.quantity%TYPE)
as
    purchased_amount vendorpaymentaccount.amount%TYPE;
    vendorpaymentid_retrieved vendorpaymentaccount.vendorpaymentid%TYPE;
BEGIN
    select price*quantity_in into purchased_amount from merchandise where merchandise.merchandiseid=merchandiseid_in;
    select vendorpaymentid into vendorpaymentid_retrieved from vendorpaymentaccount 
      where vendorpurchaseid=(select vendorpurchaseid from vendorpurchases where merchandiseid = merchandiseid_in and staffid = staffid_in and trunc(DT) = dt_in);
    UPDATE vendorpurchases SET quantity = quantity_in where merchandiseid = merchandiseid_in and staffid = staffid_in and trunc(DT) = dt_in;
    UPDATE vendorpaymentaccount SET amount = purchased_amount where vendorpaymentid=vendorpaymentid_retrieved;
END update_vendorpurchases;
/
create or replace
procedure update_vend_purch_date_paid(vendorid_in IN vendors.vendorid%TYPE, date_in IN vendorpaymentaccount.due_date%TYPE)
as
BEGIN
     update vendorpaymentaccount set date_paid=date_in where vendorpaymentid IN (select vendorpaymentid from maintainedforvendors where vendorid=vendorid_in) and date_paid IS NULL;
END update_vend_purch_date_paid;
/
create or replace
Procedure generate_vendors_total_bill(name_in IN vendors.name%TYPE, address_in IN vendors.address%TYPE, due_date_in IN vendorpaymentaccount.due_date%TYPE, bill_out OUT SYS_REFCURSOR, vendor_id_out OUT maintainedforvendors.vendorid%TYPE)
AS
  --  vendor_id_retrieved maintainedforvendors.vendorid%TYPE;    
BEGIN
    select vendorid into vendor_id_out from vendors where name=name_in and address=address_in;
    DBMS_OUTPUT.PUT_LINE('Vendor id ' || vendor_id_out);
    open bill_out for      
     select sum(vendorpaymentaccount.amount) from vendorpaymentaccount, maintainedforvendors 
       where vendorpaymentaccount.vendorpaymentid=maintainedforvendors.vendorpaymentid and trunc(vendorpaymentaccount.due_date)=due_date_in and maintainedforvendors.vendorid=vendor_id_out and vendorpaymentaccount.date_paid IS NULL  ;  
      
END generate_vendors_total_bill;
/
