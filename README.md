1. Tài khoản đăng nhập thử nghiệm:
- Admin:
    + Username: admin
    + Mật khẩu: MatKhau@123
- Nhà tuyển dụng:
    + Username: fpt_hr, vng_recruit, viettel_td
    + Mật khẩu: MatKhau@123
- Ứng viên tìm việc:
    + Username: nguyen_mai, tran_hung, le_lan, hoang_duc, vo_thu
    + Mật khẩu: MatKhau@123

2. Hướng dẫn set up database trên MySQL:
- Tạo database mới có tên là "job_portal".
- Sử dụng file "schema.sql" để import dữ liệu vào database vừa tạo:
    + Copy nội dung của file "schema.sql", query vào MySQL để tạo bảng và chèn dữ liệu mẫu.
    + Hoặc là import trực tiếp file "schema.sql" thông qua công cụ quản lý database như MySQL Workbench hoặc phpMyAdmin.
- Cấu hình kết nối database trong ứng dụng:
    + Mở file cấu hình (config.properties) để dùng mặc định hoặc tự config tùy nhu cầu.


LƯU Ý: Chạy ứng dụng ở entry point (Main.java) để bắt đầu sử dụng hệ thống.