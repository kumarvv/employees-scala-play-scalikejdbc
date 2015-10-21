drop table if exists dept;
create sequence seq_dept_id start with 1;
create table dept (
  id bigint not null default nextval('seq_dept_id') primary key,
  name varchar(255) not null
);

insert into dept (name) values ('Features');
insert into dept (name) values ('Human Resources');
insert into dept (name) values ('Client Services');
insert into dept (name) values ('Customer Support');
insert into dept (name) values ('Management');
insert into dept (name) values ('Network Operations');

drop table if exists emp;
create sequence seq_emp_id start with 1;
create table emp (
  id bigint not null default nextval('seq_emp_id') primary key,
  name varchar(255) not null,
  dept_id bigint
);

insert into emp (name, dept_id) values ('Chris', 1);
insert into emp (name, dept_id) values ('Andrey', 1);
insert into emp (name, dept_id) values ('Manish', 1);
insert into emp (name, dept_id) values ('Luke', 1);
insert into emp (name, dept_id) values ('Alexey', 3);
insert into emp (name, dept_id) values ('Basu', 4);
insert into emp (name, dept_id) values ('Matt H', 4);
insert into emp (name, dept_id) values ('Scott', 5);
insert into emp (name, dept_id) values ('Al Sori', 6);
insert into emp (name, dept_id) values ('Mike', 1);
insert into emp (name, dept_id) values ('April M', 2);
