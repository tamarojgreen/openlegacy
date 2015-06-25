delimiter //
create procedure doStuffWithTwoNumbers(in n1 int, in n2 int)
begin
	select n1 + n2 as summ, n1 - n2 as sub, n1 * n2 as mul;
end //
delimiter ;