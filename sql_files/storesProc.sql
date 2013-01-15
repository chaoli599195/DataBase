create or replace 
procedure insert_store(name_in IN store.name%TYPE, address_in IN store.address%type, phone_number_in IN store.phone_number%TYPE)
as
BEGIN
    insert into store(name, address, phone_number) values(name_in, address_in, phone_number_in);
END insert_store;
/
create or replace
procedure update_store(storeid_in IN store.storeid%TYPE, name_in IN store.name%TYPE, address_in IN store.address%type, phone_number_in IN store.phone_number%TYPE)
as
BEGIN
    update store set name=name_in, address=address_in, phone_number=phone_number_in where storeid=storeid_in;
END update_store;
/
create or replace
procedure delete_store(storeid_in IN store.storeid%TYPE)
as
BEGIN
     delete from store where storeid=storeid_in;
END delete_store;
/