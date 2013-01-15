create or replace 
Procedure Insert_Staff(staff_ssn IN Staff.ssn%TYPE,staff_name IN staff.name%type, staff_job_title IN staff.job_title%TYPE, staff_gender IN staff.gender%TYPE, staff_DOB IN staff.DOB%TYPE, staff_address IN staff.address%TYPE, staff_phone_number IN staff.phone_number%TYPE, staff_salary IN staff.salary%TYPE, staff_deptname_in IN department.name%TYPE, staff_storeid IN works_in.storeid%type, mgr_staffid IN managedby.managerstaffid%TYPE)
AS
  staff_id_inserted staff.staffid%TYPE;
  dept_id_retrieved department.deptid%TYPE;
BEGIN
   select deptid into dept_id_retrieved from department where name=staff_deptname_in;
   INSERT INTO staff (SSN, name, job_title, gender, DOB, address, phone_number, salary) VALUES (staff_ssn, staff_name, staff_job_title, staff_gender, staff_DOB, staff_address, staff_phone_number, staff_salary) RETURNING staffid into staff_id_inserted;
   insert into member_of(staffid, deptid) VALUES(staff_id_inserted, dept_id_retrieved);   
   insert into works_in(staffid, storeid) values(staff_id_inserted, staff_storeid);
   insert into managedby values(staff_id_inserted, mgr_staffid);
END Insert_Staff;
/
create or replace 
Procedure Update_Staff(staff_id IN Staff.staffid%TYPE, staff_ssn IN Staff.ssn%TYPE,staff_name IN staff.name%type, staff_job_title IN staff.job_title%TYPE, staff_gender IN staff.gender%TYPE, staff_DOB IN staff.DOB%TYPE, staff_address IN staff.address%TYPE, staff_phone_number IN staff.phone_number%TYPE, staff_salary IN staff.salary%TYPE, staff_deptname_in IN department.name%TYPE, mgr_staffid IN managedby.managerstaffid%TYPE)
AS
   dept_id_retrieved department.deptid%TYPE;
BEGIN
   select deptid into dept_id_retrieved from department where name=staff_deptname_in;
   Update staff set SSN=staff_ssn, name=staff_name, job_title=staff_job_title, gender=staff_gender, DOB=staff_DOB, address=staff_address, phone_number=staff_phone_number, salary=staff_salary where staffid=staff_id;
   Update member_of set deptid=dept_id_retrieved where staffid=staff_id;
   Update managedby set managerstaffid=mgr_staffid where employeestaffid=staff_id;
END Update_Staff;
/
create or replace Procedure Delete_Staff(staff_id IN staff.staffid%TYPE)
AS
BEGIN
    Delete from staff where staff.staffid=staff_id;
END Delete_Staff;
/
create or replace Procedure Delete_staff_with_name(name_in IN staff.name%TYPE, address_in IN staff.address%TYPE)
AS
BEGIN
    delete from staff where name=name_in and address=address_in;
END Delete_staff_with_name;