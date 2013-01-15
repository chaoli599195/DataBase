create or replace 
Procedure insert_order(orderid_out OUT orders.orderid%TYPE)
AS
BEGIN
   insert into orders(dt) values(sysdate) RETURNING orderid into orderid_out;   
END insert_order;
/
create or replace
Procedure insert_orderdetails(orderid_in IN orderdetails.orderid%TYPE, merchandiseid_in IN orderdetails.merchandiseid%TYPE, status_in IN orderdetails.status%TYPE, quantity_in IN orderdetails.quantity%TYPE)
AS
BEGIN
   insert into orderdetails VALUES(orderid_in, merchandiseid_in, status_in, quantity_in);
END insert_orderdetails;
/
create or replace
Procedure insert_place_order(orderid_in IN place_order.orderid%TYPE, customerid_in IN place_order.customerid%TYPE, staffid_in IN place_order.staffid%TYPE)
AS
BEGIN
   insert into place_order VALUES(orderid_in, customerid_in, staffid_in);
END insert_place_order;
/
-- Inserts into three tables in which billing information is spread out
create or replace
Procedure insert_customerbilling_account(staffid_in IN billtocustomers.staffid%type, customerid_in IN maintainedforcustomer.customerid%TYPE, orderid_in IN customerbilling_account.orderid%TYPE, amount_in IN customerbilling_account.amount%type)
AS
customerbillingid_out customerbilling_account.customerbillingid%type;
BEGIN
    insert into place_order VALUES(orderid_in, customerid_in, staffid_in);
    insert into customerbilling_account(orderid, due_date, amount) values(orderid_in, last_day(sysdate), amount_in) returning customerbillingid into customerbillingid_out;
    insert into maintainedforcustomer(customerbillingid, customerid) VALUES(customerbillingid_out, customerid_in);
    insert into billtocustomers(customerbillingid, staffid) VALUES(customerbillingid_out, staffid_in);
END insert_customerbilling_account;
/

create or replace
Procedure update_order_date_paid(cust_id IN customers.customerid%TYPE, date_paid_in IN customerbilling_account.date_paid%TYPE)
AS
BEGIN
    update customerbilling_account set date_paid=date_paid_in where customerbillingid IN (select customerbillingid from maintainedforcustomer where customerid=cust_id) and date_paid IS NULL;
END update_order_date_paid;
/

create or replace
Procedure generate_customer_total_bill(name_in IN customers.name%TYPE, address_in IN customers.address%TYPE, due_date_in IN customerbilling_account.due_date%TYPE, bill_out OUT SYS_REFCURSOR, cust_id_out OUT maintainedforcustomer.customerid%TYPE)
AS
    cust_id_retrieved customers.customerid%TYPE;    
BEGIN
   -- cust_id_out:=cust_id_retrieved;
    select customerid into cust_id_out from customers where name=name_in and address=address_in;
    DBMS_OUTPUT.PUT_LINE('Customer id ' || cust_id_out);
    open bill_out for 
     select sum(customerbilling_account.amount) from customerbilling_account, maintainedforcustomer 
       where customerbilling_account.customerbillingid=maintainedforcustomer.customerbillingid and trunc(customerbilling_account.due_date)=due_date_in and maintainedforcustomer.customerid=cust_id_out and customerbilling_account.date_paid IS NULL  ;   
END generate_customer_total_bill;
/

/* if only orders_list is required for particular period
create or replace 
procedure generate_orders_list(name_in IN customers.name%TYPE, phone_in IN customers.phone_number%TYPE, due_date_in IN customerbilling_account.due_date%TYPE, orders_list_out OUT SYS_REFCURSOR)
AS
    cust_id_retrieved customers.customerid%TYPE;    
BEGIN
    select customerid into cust_id_retrieved from customers where name=name_in and phone_number=phone_in;
    DBMS_OUTPUT.PUT_LINE('Customer id ' || cust_id_retrieved);
    open orders_list_out for 
      select maintainedforcustomer.customerid, sum(customerbilling_account.amount), customerbilling_account.due_date from customerbilling_account, maintainedforcustomer 
       where customerbilling_account.customerbillingid=maintainedforcustomer.customerbillingid
       group by maintainedforcustomer.customerid, customerbilling_account.due_date
       having customerbilling_account.due_date=due_date_in and maintainedforcustomer.customerid=cust_id_retrieved;    
END;
*/
/*
Declare
  order_out orders.orderid%TYPE;
BEGIN
 insert_order(order_out);
 DBMS_OUTPUT.PUT_LINE('Inserted order id ' || order_out);
end;
/
select * from orders;
*/