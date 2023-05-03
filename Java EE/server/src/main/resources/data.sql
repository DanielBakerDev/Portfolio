INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email)VALUES ('123 Maple
St','London','On', 'N1N-1N1','(555)555-5555','Trusted','ABC Supply Co.','abc@supply.com');
INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email) VALUES ('543
Sycamore Ave','Toronto','On', 'N1P-1N1','(999)555-5555','Trusted','Big Bills
Depot','bb@depot.com');
INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email) VALUES ('922 Oak
St','London','On', 'N1N-1N1','(555)555-5599','Un Trusted','Shady Sams','ss@underthetable.com');
INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email) VALUES ('178 Bridge
St','London','On', 'N6D-3H6','(911)555-5599','Trusted','Daniel Baker','daniel@underthetable.com');
--
-- INSERT INTO Vendor (Address1,City,Province,PostalCode,Phone,Type,Name,Email)
-- VALUES ('123 Oak Ave','London','On', 'N1N-1N1','(555)555-5551','Trusted','Case One','c1@here.com');
-- INSERT INTO Product_Category (id, Description) VALUES ('SHS','Shoes');
-- INSERT INTO Product_Category (id, Description) VALUES ('HTS','Hats');
-- INSERT INTO Product_Category (id, Description) VALUES ('SWT','Sweaters');
-- INSERT INTO Product_Category (id, Description) VALUES ('PNTS','Pants');

INSERT INTO Product (id,vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
VALUES ( '45X23' ,1,'Guccie shoes',12.0, 10, 19.99, 10, 20, 0, '', '');
INSERT INTO Product (id,vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
VALUES ( '45X24' ,1,'Flip Flops',2.0, 5, 11.99, 100, 40, 5, '', '');

INSERT INTO Product (id, vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
VALUES ('45X25' ,2,'Black Sweater',30.0, 10, 5.5, 20, 40, 0, '', '');
INSERT INTO Product (id,vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
VALUES ('45X26' ,2,'Black Sweater',30.0, 5, 15.50, 20, 40, 0, '', '');

INSERT INTO Product (id,vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
VALUES ('45X27' ,3,'Kaki Pants',15.5, 50.0, 5, 30, 45, 0, '', '');
INSERT INTO Product (id,vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
VALUES ('45X28' ,4,'Skinny Jeans',15.50, 60.99, 5, 30, 50, 0, '', '');

INSERT INTO Product (id,vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
VALUES ('45X29' ,4,'Flat Hat',12.50, 30.99, 6, 11, 52, 0, '', '');

INSERT INTO Product (id,vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
VALUES ( '45X30' ,4,'Flip Flops',2.0, 5, 11.99, 10, 40, 5, '', '');

-- INSERT INTO Product (id,vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
-- VALUES ( 'C1001' ,5,'Super Widget',109.99, 122.99, 10, 6, 15, 0, '', '');
--
-- INSERT INTO Product (id,vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
-- VALUES ( 'C1002' ,5,' POS Widget',19.99, 22.99, 10, 8, 15, 0, '', '');
-- INSERT INTO Product (vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
-- VALUES ( 1,'Guccie shoes',12.0, 10, 19.99, 100, 20, 0, '', '');
-- INSERT INTO Product (vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
-- VALUES ( 1,'Flip Flops',2.0, 5, 11.99, 100, 40, 5, '', '');
--
-- INSERT INTO Product (vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
-- VALUES (2,'Black Sweater',30.0, 10, 5.5, 20, 40, 0, '', '');
-- INSERT INTO Product (vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
-- VALUES (2,'Black Sweater',30.0, 5, 15.50, 20, 40, 0, '', '');
--
-- INSERT INTO Product (vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
-- VALUES (3,'Kaki Pants',15.5, 50.0, 5, 30, 45, 0, '', '');
-- INSERT INTO Product (vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
-- VALUES (3,'Skinny Jeans',15.50, 60.99, 5, 30, 50, 0, '', '');
--
-- INSERT INTO Product (vendorid,name,costprice,msrp,rop,eoq,qoh,qoo,qrcode,qrcodetxt)
-- VALUES (4,'Flat Hat',12.50, 30.99, 6, 33, 52, 0, '', '');
