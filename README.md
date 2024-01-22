create database bakerydb ; use bakerydb ;

create table user_info ( user_id int auto_increment primary key , user_name varchar(20) not null , user_pass varchar(20) not null , user_role varchar(10) not null );

create table order_info ( order_id int primary key auto_increment , user_id int , order_amt double check (order_amt >= 0), foreign key (user_id) references user_info(user_id) );

create table product_info ( product_id int primary key auto_increment , product_name varchar(20) , product_price double , product_qty int check (product_qty>=0) );

create table order_product ( order_id int , product_id int , product_qty int , foreign key (order_id) references order_info(order_id), foreign key (product_id) references product_info(product_id) );

insert into user_info(user_name , user_pass , user_role) values ('amol' , '123' , 'admin'), ('om' , '1234' , 'customer');

delimiter // create procedure insertUser(usrId int,pName varchar(20),ordQty int ) begin declare pQty int; select Product_qty into pQty from product_info where product_name=pName; if pQty>=ordQty then insert into order_info(user_id) values (usrId) ; select order_id from order_info where user_id = usrId order by order_id desc limit 1; end if; end // delimiter ;

delimiter // create procedure placeOrder(ordId int , pName varchar(20) , ordQty int) begin declare pId int ; declare pQty int;

select product_id into pId from product_info where product_name = pName ;
select product_qty into  pQty from product_info where product_name=pName;
if pQty>=ordQty then
	update product_info set product_qty=product_qty-ordQty where product_name=pName;
    insert into order_product values (ordId , pId , ordQty);
end if ;
end // delimiter ;

delimiter // create procedure cancelOrder(in pId int,in oId int,out sts boolean) begin declare pQty int; declare pPrice double; set sts=false; select Product_qty into pQty from order_product where product_Id=pId and order_id=oId; select product_price into pPrice from product_info where product_Id=pId; delete from order_product where product_Id=pId and order_id=oId;

update product_info set product_qty=Product_qty+pQty where product_Id=pId;
 update order_info set order_amt=order_amt-(pqty*pPrice) where  order_id=oId;
set sts=true;
end// delimiter ;

