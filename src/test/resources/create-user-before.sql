delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '$2a$08$qaqwRWIVRD.iXYcoV2G96e19Wq7MTNb52o./Ug2WPunYjAL7u61iK', 'admin'),
(2, true, '$2a$08$nwfeAqS9p7W4UI2sQBPN3.aLXV0iAXxPLjd2vgfdsyhUE2JF.SUFK', 'user');

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');

