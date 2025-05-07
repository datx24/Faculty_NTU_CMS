# Faculty_NTU_CMS
 Đồ án phát triển ứng dụng web 2

hướng dẫn db migration: (src/main/resources/dbmigrations)

- đối với chạy lần đầu:
    1. setup mysql và tạo db : create database facultyntucms, use facultyntucms
    2. nạp file sql theo thứ tự: schema.sql, tschema_update_1_may.sql, tschema_update_4_may.sql

- đối với anh Đạt (đã có db trong máy và dữ liệu trong máy, chạy script update db): 

    git pull về và chạy 2 file tschema_update_1_may.sql, tschema_update_4_may.sql lần lượt vào mysql ( USE facultyntucms)

nhớ chạy 1 lần thôi nha ae, lỗi thì drop toàn bộ db làm lại từ đầu :)) 
## cái này là đang tái hiện chức năng migration của các thư viện ORM nên sơ sài, ae thông cảm, nếu thấy rườm rà, có thể drop db và chạy file db hoàn chỉnh [ở đây ](./src/test/java/faculty/ntu/cms/fulldbschema.sql), không cần làm db migration
ps: với cả để mật khẩu cho mysql đi , 123 cũng được


