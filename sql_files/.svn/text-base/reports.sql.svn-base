create or replace
procedure generate_cust_purchase_hist(name_in IN customers.name%TYPE, address_in IN customers.address%TYPE, start_date IN orders.dt%type, end_date IN orders.dt%type, report_out OUT SYS_REFCURSOR)
as
cust_id_retrieved customers.customerid%TYPE;    
BEGIN
    select customerid into cust_id_retrieved from customers where name=name_in and address=address_in;
    DBMS_OUTPUT.PUT_LINE('Customer id ' || cust_id_retrieved);
    open report_out for 
      select place_order.customerid as customerid, orders.orderid as orderid, name, quantity, price*orderdetails.quantity as amount, dt as DATE_ORDERED 
        from place_order, orders, orderdetails, merchandise 
        where place_order.orderid=orderdetails.orderid and place_order.orderid=orders.orderid and orderdetails.merchandiseid=merchandise.merchandiseid and place_order.customerid=cust_id_retrieved and trunc(orders.dt) <= end_date and trunc(orders.dt) > start_date;    
end generate_cust_purchase_hist;
/


create or replace 
procedure generate_sales_assisted(name_in IN staff.name%type, address_in IN staff.address%type, start_date IN orders.dt%type, end_date IN orders.dt%type, report_out OUT SYS_REFCURSOR)
as
staff_id_retrieved place_order.staffid%TYPE;    
BEGIN
    select staffid into staff_id_retrieved from staff where name=name_in and address=address_in;
    open report_out for 
      select UNIQUE(customers.customerid), customers.name, address 
        from place_order, orders, customers
        where place_order.customerid=customers.customerid and place_order.orderid=orders.orderid and place_order.staffid=staff_id_retrieved and trunc(orders.dt) <= trunc(end_date) and trunc(orders.dt) > trunc(start_date);
END generate_sales_assisted; 
/
/*
create or replace 
procedure generate_sales_assisted(staffid_in IN staff.staffid%type, start_date IN orders.dt%type, end_date IN orders.dt%type, report_out OUT SYS_REFCURSOR)
as
BEGIN
    open report_out for 
      select UNIQUE(customers.customerid), customers.name, address 
        from place_order, orders, customers
        where place_order.customerid=customers.customerid and place_order.orderid=orders.orderid and place_order.staffid=staffid_in and trunc(orders.dt) <= trunc(end_date) and trunc(orders.dt) > trunc(start_date);
END generate_sales_assisted;
/
*/

create or replace 
procedure generate_vendors(report_out OUT SYS_REFCURSOR)
as
BEGIN
    open report_out for select * from vendors;
END generate_vendors;
/
--commit;
create or replace
procedure generate_staff_with_dept(deptname_in IN department.name%TYPE, report_out OUT SYS_REFCURSOR)
as
BEGIN
    open report_out for
      select staff.staffid as staffid, ssn, staff.name as name, department.name as dept_name, job_title, gender, dob, address, phone_number, salary  from staff,member_of, department 
       where staff.staffid=member_of.staffid and member_of.deptid=department.deptid and department.name = deptname_in;
END generate_staff_with_dept;
/
create or replace
procedure generate_sal_report_dept(deptname_in IN department.name%TYPE, report_out OUT SYS_REFCURSOR)
as
BEGIN
    open report_out for
      select department.name as DeptName ,min(salary) as MINSalary, max(salary) as MAXSalary, AVG(salary) as AVGSalary, SUM(salary) as BUDGET  from staff,member_of, department 
       where staff.staffid=member_of.staffid and member_of.deptid=department.deptid
       group by department.name;
END generate_sal_report_dept;
/