create or replace 
procedure insert_dept(name_in IN department.name%TYPE)
as
BEGIN
    insert into department(name) values(name_in);
END insert_dept;
/
create or replace
procedure update_dept(deptid_in IN department.deptid%TYPE, name_in IN department.name%TYPE)
as
BEGIN
    update department set name=name_in where deptid=deptid_in;
END update_dept;
/
create or replace
procedure delete_dept(deptid_in IN department.deptid%TYPE)
as
BEGIN
   delete from department where deptid=deptid_in;
END delete_dept;
/