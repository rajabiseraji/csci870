/*
 * @author  : Rajan Khullar
 * @created : 09/08/16
 * @updated : 10/10/16
 */

select new.actor('nydevteam', '', 'nydevteam@gmail.com');
select new.actor('Rajan', 'Khullar', 'rkhullar@nyit.edu');

insert into dbo.building(abbr) values ('ANY'), ('EGGC'), ('MC16'), ('MC26');
select new.location('ANY', 0::smallint, 'ANY')