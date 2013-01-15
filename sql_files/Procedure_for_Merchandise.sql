-- Merchandise

create or replace 
procedure Insert_Merchandise(name IN merchandise.name%Type, isbn in merchandise.isbn%type, 
                            author IN merchandise.author%type, price IN merchandise.price%type, vendorId IN merchandise.vendorId%type)
as
begin
    Insert into merchandise(name, isbn, author,price,vendorID) 
    VALUES (name, isbn, author, price, vendorId);
end Insert_Merchandise;
/

create or replace 
procedure Update_Merchandise(inMerchandiseId IN merchandise.merchandiseid%TYPE, inName IN merchandise.name%Type, inIsbn in merchandise.isbn%type, 
                            inAuthor IN merchandise.author%type, inPrice IN merchandise.price%type, inVendorId IN merchandise.vendorId%type)
as
begin
    Update merchandise set name = inname, isbn = inIsbn, author = inAuthor, price = inPrice, vendorID = inVendorId 
    where merchandiseId = inMerchandiseId;
end Update_Merchandise;
/

create or replace 
procedure Delete_Merchandise(inMerchandiseId IN merchandise.merchandiseid%TYPE)
as
begin
    Delete from merchandise where merchandiseId = inMerchandiseId;
end Delete_Merchandise;
/

-- Vendors

create or replace 
procedure Insert_vendors(inName IN vendors.name%Type,  inAddress vendors.address%type, 
                            inPhone IN vendors.phone_number%type, inContractStartDate IN vendors.contractstartdate%type, 
                            inContractEndDate IN vendors.contractenddate%type, inBankAccNo IN vendors.bankaccount_number%type)
as
begin
    Insert into vendors(name, address, phone_number, contractstartdate, contractenddate, bankaccount_number) 
    VALUES (inName, inAddress, inPhone, inContractStartDate, inContractEndDate, inBankAccNo);
end Insert_vendors;
/

create or replace 
procedure Update_Vendors(inVendorId IN vendors.vendorid%type, inName IN vendors.name%Type,  inAddress vendors.address%type, 
                            inPhone IN vendors.phone_number%type, inContractStartDate IN vendors.contractstartdate%type, 
                            inContractEndDate IN vendors.contractenddate%type, inBankAccNo IN vendors.bankaccount_number%type)
as
begin
    Update vendors set name = inName, address = inAddress, phone_number = inPhone, 
    contractstartdate = inContractStartDate, contractenddate = inContractEndDate, 
    bankaccount_number = inBankAccNo where vendorid = inVendorId; 
end Update_Vendors;
/

create or replace 
procedure Delete_Vendors(inVendorId IN vendors.vendorid%type)
as
begin
    Delete from vendors where vendorid = inVendorId; 
end Delete_Vendors;
/

create or replace 
procedure Delete_Vendors_with_name(inVendorName IN vendors.name%type, inVendorAddress IN vendors.address%type)
as
begin
    Delete from vendors where name=inVendorName and address=inVendorAddress; 
end Delete_Vendors_with_name;
/
-- Merchandise Stocker

create or replace 
procedure Insert_MerchandiseStock(inMerchandiseId IN merchandisestock.merchandiseid%Type, inStoreId in merchandisestock.storeid%type, inQuantity merchandisestock.quantity%type)
as
begin
    Insert into merchandisestock(merchandiseid, storeid, quantity) 
    VALUES (inMerchandiseId, inStoreId, inQuantity);
end Insert_MerchandiseStock;
/

create or replace 
procedure Update_MerchandiseStock(inMerchandiseId IN merchandisestock.merchandiseid%Type, inStoreId in merchandisestock.storeid%type, inQuantity merchandisestock.quantity%type)
as
begin
    Update merchandisestock set quantity = inQuantity 
    where merchandiseid = inMerchandiseId and storeid = inStoreId;
end Update_MerchandiseStock;
/

create or replace 
procedure Delete_MerchandiseStock(inMerchandiseId IN merchandisestock.merchandiseid%Type, inStoreId in merchandisestock.storeid%type)
as
begin
    Delete from merchandisestock where merchandiseid = inMerchandiseId and storeid = inStoreId;
end Delete_MerchandiseStock;
