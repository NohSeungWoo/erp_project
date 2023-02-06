---------- **** Final Project **** ----------

show user;
-- USER이(가) "FINALORAUSER1"입니다.

commit;

-- 부서 테이블
create table tbl_department
(departNo         number          not null,   -- 부서번호
 departmentName   varchar2(100)   not null,   -- 부서명
 managerID        varchar2(100)   not null,   -- 부서장사원번호

 constraint PK_tbl_department_departNo primary key(departNo)
);

-- 직급 테이블
create table tbl_position
(positionNo     number          not null,   -- 직급번호
 positionName   varchar2(100)   not null,   -- 직책명,
 
 constraint PK_tbl_position_positionNo primary key(positionNo)
);

-- 직원 테이블
create table tbl_employee
(employeeID        varchar2(100)            not null,    -- 사원번호
 fk_departNo       number                   not null,    -- 부서번호(foreign key)
 fk_positionNo     number                   not null,    -- 직급번호(foreign key)
 name              varchar2(100)            not null,    -- 사원명
 password          varchar2(200)            not null,    -- 비밀번호
 mobile            varchar2(100)            not null,    -- 연락처
 email             varchar2(200)            not null,    -- 이메일
 hiredate          date default sysdate     not null,    -- 입사일자
 retiredate        date,                                 -- 퇴사일자
 retire            number(1) default 0      not null,    -- 퇴사여부(0이면, 재직중, 1이면 퇴사)
 profilename       varchar2(255),                        -- WAS(톰캣)에 저장될 파일명(2021110809271535243254235235234.png)  
 orgProfilename    varchar2(255),                        -- 진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명 
 fileSize          number,                               -- 파일크기
 salary            number                   not null,    -- 급여
 dayoff            number default 0         not null,    -- 연차개수
 admin             number(1) default 0      not null,    -- 관리자 권한 여부
 postcode          number                   not null,    -- 우편번호
 address           varchar2(200)            not null,    -- 주소
 detailAddress     varchar2(200)            not null,    -- 상세주소
 extraAddress      varchar2(200),                        -- 추가주소
    
 constraint PK_tbl_emp_employeeNo primary key(employeeNo),
 constraint FK_tbl_emp_fk_departNo foreign key(fk_departNo) references tbl_department(departNo),
 constraint FK_tbl_emp_fk_positionNo foreign key(fk_positionNo) references tbl_position(positionNo),
 constraint CK_tbl_emp_retiredate check(retiredate > hiredate),
 constraint CK_tbl_emp_retire check(retire in (0, 1)),
 constraint CK_tbl_emp_salary check(salary > 0),
 constraint CK_tbl_emp_dayoff check(dayoff > 0),
 constraint CK_tbl_emp_admin check(admin in (0, 1))
 );

select * from tbl_employee;

create sequence departSeq
start with 101
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tbl_department(departNo, departmentName, managerID)
values(departSeq.nextval, '영업', '211059005');

rollback;

select * from tbl_department
order by departNo;

update tbl_department set managerId = '211049004'
where departNo = 104;


create sequence positionSeq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tbl_position(positionNo, positionName)
values(positionSeq.nextval, '대표이사');

select * from tbl_position;

create sequence employeSeq
start with 9001
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tbl_employee(employeeNo, fk_departNo, fk_positionNo, name, password ,mobile, email, hiredate, retiredate, retire, salary, dayoff, admin)
values(to_char(sysdate, 'yy')||101||employeSeq.nextval, 101, 2, '이순신', '211019001', '01012345678', 'leess@gmail.com', '2021-01-01', default, 0, 6000, 10, 1);

insert into tbl_employee(employeeNo, fk_departNo, fk_positionNo, name, password ,mobile, email, hiredate, retiredate, retire, salary, dayoff, admin)
values(to_char(sysdate, 'yy')||102||employeSeq.nextval, 102, 2, '엄정화', '211019002', '01012345678', 'eomjh@gmail.com', '2020-01-01', default, 0, 5000, 10, 1);

insert into tbl_employee(employeeNo, fk_departNo, fk_positionNo, name, password ,mobile, email, hiredate, retiredate, retire, salary, dayoff, admin)
values(to_char(sysdate, 'yy')||103||employeSeq.nextval, 103, 2, '서강준', '211019003', '01012123678', 'seokj@gmail.com', '2021-01-01', default, 0, 4000, 10, 1);

insert into tbl_employee(employeeNo, fk_departNo, fk_positionNo, name, password ,mobile, email, hiredate, retiredate, retire, salary, dayoff, admin)
values(to_char(sysdate, 'yy')||104||employeSeq.nextval, 104, 2, '이혜리', '211049004', '01012567678', 'leehl@gmail.com', '2021-03-01', default, 0, 4000, 10, 1);

insert into tbl_employee(employeeNo, fk_departNo, fk_positionNo, name, password ,mobile, email, hiredate, retiredate, retire, salary, dayoff, admin)
values('211059005', 104, 2, '차은우', '211059005', '01012567678', 'chaew@gmail.com', '2021-04-01', default, 0, 3500, 10, 1);

insert into tbl_employee(employeeNo, fk_departNo, fk_positionNo, name, password ,mobile, email, hiredate, retiredate, retire, salary, dayoff, admin)
values(to_char(sysdate, 'yy')||101||employeSeq.nextval, 101, 3, '이순신2', '211019006', '01012569898', 'leess1@gmail.com', '2021-04-01', default, 0, 3300, 10, 0);

insert into tbl_employee(employeeNo, fk_departNo, fk_positionNo, name, password ,mobile, email, hiredate, retiredate, retire, salary, dayoff, admin)
values(to_char(sysdate, 'yy')||101||employeSeq.nextval, 101, 3, '이순신2', '211019006', '01012569898', 'leess1@gmail.com', '2021-04-01', default, 0, 3300, 10, 0);


select * 
from tbl_employee
order by fk_positionNo, employeeID asc;

select employeeid, fk_departno, departmentname, fk_positionno, positionname, name, mobile, email
from 
(
    select employeeid, fk_departno, departmentname, fk_positionno, name, mobile, email, retire
    from tbl_employee E
    JOIN tbl_department D
    ON E.fk_departno = D.departno
) V
JOIN tbl_position P
ON V.fk_positionno = P.positionno
where V.retire = 0
order by fk_positionNo, fk_departno, employeeID asc;
-- employeeid, departno, departmentname, fk_positionno, name, mobile, email,  

select count(*)
from tbl_employee
where retire = 0;

select employeeid, fk_departno, departmentname, 
	   fk_positionno, positionname, name, mobile, email
from
(
    select row_number() over(order by fk_positionNo, fk_departno, employeeID) AS RNO,
           employeeid, fk_departno, departmentname, 
           fk_positionno, positionname, name, mobile, email
    from 
    (
        select employeeid, fk_departno, departmentname, fk_positionno, 
               name, mobile, email, retire
        from tbl_employee E
        JOIN tbl_department D
        ON E.fk_departno = D.departno
    ) V
    JOIN tbl_position P
    ON V.fk_positionno = P.positionno
    where V.retire = 0
    and positionname = '팀장'
    and departmentname = '인사'
    
)
where rno between 1 and 5
and name = '이순신';

select departmentname
from tbl_department
order by departno;

select positionno, positionname
from tbl_position
order by positionno;

select name
from tbl_employee
where retire = 0
and name like '%'|| '이' ||'%';

select *
from tbl_employee
where retire = 0
and email = 'eomjh@gmail.com' 
and password = 211029002;


select employeeid, fk_departNo, fk_positionNo, name, password, mobile, email,
       hiredate, retiredate, retire, profilename, orgProfilename, fileSize, 
       salary, dayoff, admin, postcode, address, detailAddress, extraAddress
from tbl_employee
where retire = 0
and email = 'eomjh@gmail.com' 
and password = 211029002;
commit;


-------------------------------------------------------------------------------------
-- *** 일정테이블 *** --

drop table tbl_schedule;

create table tbl_schedule (
  seq           number,
  subject       varchar2(50)  NOT NULL,    -- 일정 제목
  startDate     varchar2(20)  NOT NULL,    -- 일정 시작 날짜
  endDate       varchar2(20)  NOT NULL,    -- 일정 종료 날짜
  memo          varchar2(500) NOT NULL,    -- 일정 메모
  fk_employeeID    varchar2(100) NOT NULL, -- 사원번호 (f key)
  fk_departNo       number    not null    -- 부서번호(foreign key)
  ,constraint PK_tbl_schedule_num primary key(seq)
  ,constraint FK_tbl_schedule_employeeID foreign key(fk_employeeID) references tbl_employee (employeeID)
  ,constraint FK_tbl_schedule_departNo foreign key(fk_departNo) references tbl_department (departNo));
  
create sequence scheduleSeq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache; 

commit;

INSERT INTO tbl_schedule (seq,subject,startDate,endDate,memo,fk_employeeID,fk_departNo)
                  values (scheduleSeq.nextval,'ㅎㅇ','2021-11-26','2021-11-27','ㅎㅇㅎㅇ','211019001',104);



select tbl_schedule



  
  
  {subject: "ㅁㅁㅁ", startDate: "2021-11-29", endDate: "2021-11-29", memo: "ㅁㅁㅋㅇㅋㅁ"}
endDate: "2021-11-29"
memo: "ㅁㅁㅋㅇㅋㅁ"
startDate: "2021-11-29"
subject: "ㅁㅁㅁ"

INSERT INTO
	    		tbl_schedule(num, fk_employeeID,subject,startDate,endDate,memo)
	    	VALUES
	    		(1,'211019001','ㅁㅁㅁ','2021-11-29','2021-11-29','ㅁㅁㅋㅇㅋㅁ')
;

select * from tbl_schedule;

    		SELECT
    			seq,subject,startDate,endDate,memo,fk_employeeID,fk_departNo
    		FROM
    			tbl_schedule
    		WHERE
    			seq = 5
    		ORDER BY
    			startDate
    		DESC