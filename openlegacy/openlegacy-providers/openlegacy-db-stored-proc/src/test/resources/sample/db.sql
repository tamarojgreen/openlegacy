# database

drop database if exists `rpc_test` // 

create database `rpc_test` //

use `rpc_test` //

# items

create table `items` (
    `id` int not null auto_increment, 
    `name` varchar(30) not null,
    `description` varchar(50) not null, 
    `weight` int not null,
    `shipping_method_id` int not null, 
    primary key (`id`)
) //

insert into `items`(`name`, `description`, `weight`, `shipping_method_id`) 
values 
    ('Kid Guitar', 'Kid Guitar - Musical Toys', 10, 1), 
    ('Ball Pool', 'Ball Pool - Novelty Toys', 20, 1), 
    ('Water Ball', 'Water Ball - Balls', 30, 1), 
    ('Frisbee', 'Dog Frisbee - Pet Toys', 1, 1), 
    ('Pink Bank', 'Pink Saving Bank - Ceramics', 2, 1) //
    
# shipping method

create table `shipping_method` (
    `id` int not null auto_increment, 
    `name` varchar(20) not null, 
    `days` int not null, 
    primary key (`id`)
) //

insert into `shipping_method` (`name`, `days`) values ('Air Mail', 3) //

# procedures

create procedure doStuffWithTwoNumbers(in param1 int, in param2 int)
begin
    select param1 + param2 as `sum`, param1 - param2 as `sub`, param1 * param2 as 'mul';
end //

create procedure sayHello(in param varchar(20))
begin
    select CONCAT('Hello, ', param);
end //

create procedure sayHelloIntoParam(in param varchar(20), out result varchar(30))
begin
    select CONCAT('Hello, ', param) into result;
end //

create procedure getAllItems()
begin
    select `id`, `name`, `description`, `weight` from `items`;
end //

create procedure getItemDetails(in itemId int)
begin
    select i.name, i.description, i.weight, s.name as shipping_method_name, s.days
    from `items` i join `shipping_method` s on i.shipping_method_id = s.id
    where i.id = itemId;
end //
