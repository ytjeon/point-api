
set schema point;
insert into point_event_mst(
                             point_event_name
                           ,point_accu_type
                           ,expire_days
                           ,max_unit_point
)
values(
          '일반 적립 포인트',
          '1',
          365,
          10000
      ) ,

      ('관리자 지급 포인트',
       '2',
       365,
       100000
      )
    ;





set schema dms;

insert into user_mst(
    user_no,
    user_id,
    user_name
)
values(
    101,'gildong' ,'홍길동')
     ,(202,'suzy', '배수지')
     ,(303,'samsun','김삼순')
     ,(404,'mavly','마동석')
     ,(505,'psinsa','박신사')
;

set schema point;