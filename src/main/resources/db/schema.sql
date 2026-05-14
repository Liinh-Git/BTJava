-- ============================================================
--  HỆ THỐNG TÌM VIỆC LÀM - SCRIPT TẠO DATABASE VÀ DỮ LIỆU MẪU
--  Encoding: UTF-8
--  Tương thích: MySQL 8.0+
-- ============================================================

-- Quy ước ID: tất cả ID dùng VARCHAR(10), mỗi ID mẫu bên dưới đều đúng 10 ký tự.
-- User: U-00000001
-- Employer: EMP-000001
-- Candidate: CAN-000001
-- Recruitment: REC-000001
-- Application: APP-000001
-- CV: CV-0000001
-- Category: CAT-000001
-- Notification: NOT-000001
-- Education: EDU-000001
-- WorkExperience: WE-0000001
-- Certification: CERT-00001

CREATE DATABASE IF NOT EXISTS job_portal
	CHARACTER SET utf8mb4
	COLLATE utf8mb4_unicode_ci;

USE job_portal;

-- ------------------------------------------------------------
-- XÓA BẢNG CŨ (theo thứ tự phụ thuộc khóa ngoại)
-- ------------------------------------------------------------
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS certifications;
DROP TABLE IF EXISTS work_experiences;
DROP TABLE IF EXISTS educations;
DROP TABLE IF EXISTS cvs;
DROP TABLE IF EXISTS recruitments;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS candidates;
DROP TABLE IF EXISTS employers;
DROP TABLE IF EXISTS users;

-- ============================================================
-- TẠO CÁC BẢNG
-- ============================================================

-- Bảng người dùng (gộp cả Employer, Candidate, Admin qua cột role)
CREATE TABLE users (
	user_id       VARCHAR(10)  PRIMARY KEY,
	username      VARCHAR(50)  NOT NULL UNIQUE,
	password_hash VARCHAR(255) NOT NULL,
	full_name     VARCHAR(150) NOT NULL,
	phone_number  VARCHAR(15),
	date_of_birth DATE,
	gender        ENUM('MALE','FEMALE','OTHER'),
	email         VARCHAR(100) NOT NULL UNIQUE,
	address       VARCHAR(300),
	role          ENUM('CANDIDATE','EMPLOYER','ADMIN') NOT NULL,
	is_active     BOOLEAN      DEFAULT TRUE,
	created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP
);

-- Thông tin riêng của nhà tuyển dụng
CREATE TABLE employers (
	employer_id         VARCHAR(10)  PRIMARY KEY,
	user_id             VARCHAR(10)  NOT NULL UNIQUE,
	company_name        VARCHAR(200) NOT NULL,
	company_address     VARCHAR(300),
	company_description TEXT,
	FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Thông tin riêng của ứng viên
CREATE TABLE candidates (
	candidate_id VARCHAR(10) PRIMARY KEY,
	user_id      VARCHAR(10) NOT NULL UNIQUE,
	FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Danh mục ngành nghề
CREATE TABLE categories (
	category_id   VARCHAR(10)  PRIMARY KEY,
	category_name VARCHAR(150) NOT NULL
);

-- Tin tuyển dụng
CREATE TABLE recruitments (
	recruitment_id      VARCHAR(10)   PRIMARY KEY,
	employer_id         VARCHAR(10)   NOT NULL,
	category_id         VARCHAR(10)   NOT NULL,
	title               VARCHAR(250)  NOT NULL,
	description         TEXT,
	job_type            ENUM('FULLTIME','PARTTIME','INTERNSHIP') NOT NULL,
	status              ENUM('OPEN','CLOSED','EXPIRED')          DEFAULT 'OPEN',
	admin_status        ENUM('PENDING','APPROVED','REJECTED')    DEFAULT 'PENDING',
	salary              DOUBLE,
	location            VARCHAR(200),
	experience_required VARCHAR(150),
	created_date        DATETIME DEFAULT CURRENT_TIMESTAMP,
	due_date            DATETIME,
	FOREIGN KEY (employer_id) REFERENCES employers(employer_id),
	FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- CV của ứng viên (1 ứng viên - 1 CV)
CREATE TABLE cvs (
	cv_id            VARCHAR(10)  PRIMARY KEY,
	candidate_id     VARCHAR(10)  NOT NULL UNIQUE,
	objective        TEXT,
	skills           TEXT,
	desired_position VARCHAR(200),
	location         VARCHAR(200),
	desired_salary   DOUBLE,
	last_updated     DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (candidate_id) REFERENCES candidates(candidate_id) ON DELETE CASCADE
);

-- Học vấn (1 CV - nhiều mục học vấn)
CREATE TABLE educations (
	education_id VARCHAR(10)  PRIMARY KEY,
	cv_id        VARCHAR(10)  NOT NULL,
	school       VARCHAR(250),
	degree       VARCHAR(150),
	major        VARCHAR(150),
	start_year   INT,
	end_year     INT,
	description  TEXT,
	FOREIGN KEY (cv_id) REFERENCES cvs(cv_id) ON DELETE CASCADE
);

-- Kinh nghiệm làm việc (1 CV - nhiều kinh nghiệm)
CREATE TABLE work_experiences (
	experience_id VARCHAR(10)  PRIMARY KEY,
	cv_id         VARCHAR(10)  NOT NULL,
	company_name  VARCHAR(250),
	position      VARCHAR(150),
	start_date    DATE,
	end_date      DATE,
	description   TEXT,
	FOREIGN KEY (cv_id) REFERENCES cvs(cv_id) ON DELETE CASCADE
);

-- Chứng chỉ (1 CV - nhiều chứng chỉ)
CREATE TABLE certifications (
	cert_id    VARCHAR(10)  PRIMARY KEY,
	cv_id      VARCHAR(10)  NOT NULL,
	name       VARCHAR(250),
	issuer     VARCHAR(250),
	issue_date DATE,
	FOREIGN KEY (cv_id) REFERENCES cvs(cv_id) ON DELETE CASCADE
);

-- Đơn ứng tuyển (1 ứng viên không ứng tuyển 2 lần vào cùng 1 vị trí)
CREATE TABLE applications (
	application_id VARCHAR(10) PRIMARY KEY,
	candidate_id   VARCHAR(10) NOT NULL,
	recruitment_id VARCHAR(10) NOT NULL,
	status         ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
	applied_date   DATETIME DEFAULT CURRENT_TIMESTAMP,
	UNIQUE KEY uq_application (candidate_id, recruitment_id),
	FOREIGN KEY (candidate_id)   REFERENCES candidates(candidate_id),
	FOREIGN KEY (recruitment_id) REFERENCES recruitments(recruitment_id)
);

-- Thông báo hệ thống
CREATE TABLE notifications (
	notification_id VARCHAR(10) PRIMARY KEY,
	sender_id       VARCHAR(10) NOT NULL,
	receiver_id     VARCHAR(10) NOT NULL,
	content         TEXT,
	date_time       DATETIME DEFAULT CURRENT_TIMESTAMP,
	is_read         BOOLEAN  DEFAULT FALSE,
	FOREIGN KEY (sender_id)   REFERENCES users(user_id),
	FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);

-- ============================================================
-- CHÈN DỮ LIỆU MẪU
-- ============================================================
-- Ghi chú: password_hash mẫu dùng SHA-256 của chuỗi "MatKhau@123"
-- để khớp với org.jobportal.utils.PasswordUtils hiện tại.

-- ------------------------------------------------------------
-- USERS
-- ------------------------------------------------------------
INSERT INTO users (user_id, username, password_hash, full_name, phone_number,
				   date_of_birth, gender, email, address, role, is_active, created_at) VALUES
-- Admin
('U-00000001', 'admin',              '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Nguyễn Văn Quản Trị',  '0900000001', '1985-01-15', 'MALE',   'admin@timviec.vn',
 'Số 10 Phạm Văn Bạch, Cầu Giấy, Hà Nội',
 'ADMIN',     TRUE, '2026-01-01 08:00:00'),
-- Nhà tuyển dụng
('U-00000002', 'fpt_hr',             '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Trần Thị Bích Ngọc',   '0912345678', '1988-03-22', 'FEMALE', 'tuyendung@fpt.com.vn',
 'Số 17 Duy Tân, Cầu Giấy, Hà Nội',
 'EMPLOYER',  TRUE, '2026-01-05 09:00:00'),
('U-00000003', 'vng_recruit',        '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Lê Quang Huy',          '0923456789', '1990-07-10', 'MALE',   'tuyendung@vng.com.vn',
 'Số 182-184 Lê Đại Hành, Phường 15, Quận 11, TP. Hồ Chí Minh',
 'EMPLOYER',  TRUE, '2026-01-06 09:30:00'),
('U-00000004', 'viettel_td',         '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Phạm Thị Ngọc Ánh',    '0934567890', '1987-11-05', 'FEMALE', 'tuyendung@viettel.com.vn',
 'Số 1 Giang Văn Minh, Ba Đình, Hà Nội',
 'EMPLOYER',  TRUE, '2026-01-07 10:00:00'),
-- Ứng viên
('U-00000005', 'nguyen_mai',         '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Nguyễn Thị Mai',        '0945111222', '1999-05-14', 'FEMALE', 'mai.nguyen99@gmail.com',
 'Số 45 Xuân Thủy, Cầu Giấy, Hà Nội',
 'CANDIDATE', TRUE, '2026-01-10 10:00:00'),
('U-00000006', 'tran_hung',          '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Trần Văn Hùng',         '0956222333', '1998-08-20', 'MALE',   'hung.tran98@gmail.com',
 'Số 78 Nguyễn Trãi, Quận 1, TP. Hồ Chí Minh',
 'CANDIDATE', TRUE, '2026-01-11 11:00:00'),
('U-00000007', 'le_lan',             '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Lê Thị Lan',            '0967333444', '2000-02-28', 'FEMALE', 'lan.le2000@gmail.com',
 'Số 12 Giải Phóng, Hai Bà Trưng, Hà Nội',
 'CANDIDATE', TRUE, '2026-01-12 08:30:00'),
('U-00000008', 'hoang_duc',          '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Hoàng Minh Đức',        '0978444555', '1997-12-01', 'MALE',   'duc.hoang97@gmail.com',
 'Số 33 Trần Phú, Hà Đông, Hà Nội',
 'CANDIDATE', TRUE, '2026-01-13 14:00:00'),
('U-00000009', 'vo_thu',             '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Võ Thị Thu',            '0989555666', '2001-06-15', 'FEMALE', 'thu.vo2001@gmail.com',
 'Số 89 Lê Duẩn, Hải Châu, Đà Nẵng',
 'CANDIDATE', TRUE, '2026-01-14 09:00:00');

-- ------------------------------------------------------------
-- EMPLOYERS
-- ------------------------------------------------------------
INSERT INTO employers (employer_id, user_id, company_name, company_address, company_description) VALUES
('EMP-000001', 'U-00000002',
 'Công ty Cổ phần FPT Software',
 'Tòa nhà FPT, Số 17 Duy Tân, Cầu Giấy, Hà Nội',
 'FPT Software là công ty thành viên của Tập đoàn FPT, chuyên cung cấp các giải pháp công nghệ thông tin, phần mềm và dịch vụ CNTT cho khách hàng trong nước và quốc tế. Với hơn 20.000 nhân viên toàn cầu, FPT Software là một trong những nhà cung cấp dịch vụ CNTT lớn nhất Việt Nam.'),
('EMP-000002', 'U-00000003',
 'Công ty Cổ phần VNG',
 'Số 182-184 Lê Đại Hành, Phường 15, Quận 11, TP. Hồ Chí Minh',
 'VNG là công ty công nghệ hàng đầu Việt Nam với hơn 4.000 nhân viên. Các sản phẩm nổi tiếng bao gồm Zalo, ZaloPay, và nhiều nền tảng game, giải trí, thương mại điện tử. VNG hướng tới mục tiêu trở thành công ty công nghệ mang tầm vóc khu vực.'),
('EMP-000003', 'U-00000004',
 'Tập đoàn Công nghiệp - Viễn thông Quân đội (Viettel)',
 'Số 1 Giang Văn Minh, Ba Đình, Hà Nội',
 'Viettel là tập đoàn viễn thông và công nghệ lớn nhất Việt Nam, hoạt động tại 11 quốc gia với hơn 100 triệu khách hàng. Viettel tiên phong trong phát triển hạ tầng 5G và các giải pháp chuyển đổi số quốc gia.');

-- ------------------------------------------------------------
-- CANDIDATES
-- ------------------------------------------------------------
INSERT INTO candidates (candidate_id, user_id) VALUES
('CAN-000001', 'U-00000005'),
('CAN-000002', 'U-00000006'),
('CAN-000003', 'U-00000007'),
('CAN-000004', 'U-00000008'),
('CAN-000005', 'U-00000009');

-- ------------------------------------------------------------
-- CATEGORIES
-- ------------------------------------------------------------
INSERT INTO categories (category_id, category_name) VALUES
('CAT-000001', 'Công nghệ Thông tin'),
('CAT-000002', 'Kinh doanh / Bán hàng'),
('CAT-000003', 'Kế toán / Tài chính'),
('CAT-000004', 'Marketing / Truyền thông'),
('CAT-000005', 'Nhân sự / Hành chính'),
('CAT-000006', 'Thiết kế Đồ họa / UI-UX'),
('CAT-000007', 'Kỹ thuật / Viễn thông'),
('CAT-000008', 'Giáo dục / Đào tạo');

-- ------------------------------------------------------------
-- RECRUITMENTS (15 tin tuyển dụng)
-- ------------------------------------------------------------
INSERT INTO recruitments
  (recruitment_id, employer_id, category_id, title, description,
   job_type, status, admin_status, salary, location, experience_required,
   created_date, due_date)
VALUES
-- === FPT Software (5 tin) ===
('REC-000001','EMP-000001','CAT-000001',
 'Lập trình viên Java Backend (Spring Boot)',
 'Tham gia phát triển và bảo trì các ứng dụng backend quy mô lớn cho khách hàng Nhật Bản. Công nghệ sử dụng: Java 17, Spring Boot, Hibernate, MySQL, Redis, Docker. Môi trường làm việc Agile/Scrum.',
 'FULLTIME','OPEN','APPROVED',18000000,'Hà Nội','1-3 năm kinh nghiệm','2026-02-01 08:00:00','2026-08-31 23:59:59'),

('REC-000002','EMP-000001','CAT-000001',
 'Kỹ sư Kiểm thử Phần mềm (Manual & Automation Tester)',
 'Xây dựng kế hoạch kiểm thử, viết test case, thực hiện kiểm thử chức năng và phi chức năng. Ưu tiên ứng viên biết Selenium, JUnit hoặc TestNG.',
 'FULLTIME','OPEN','APPROVED',14000000,'Hà Nội','1-2 năm kinh nghiệm','2026-02-05 08:00:00','2026-08-20 23:59:59'),

('REC-000003','EMP-000001','CAT-000001',
 'Thực tập sinh Phát triển Phần mềm',
 'Hỗ trợ nhóm phát triển trong coding, testing và tài liệu hóa. Được đào tạo thực tế bởi các chuyên gia. Có cơ hội được nhận vào làm chính thức sau kỳ thực tập.',
 'INTERNSHIP','OPEN','APPROVED',4000000,'Hà Nội','Chưa có kinh nghiệm (sinh viên năm 3-4)','2026-02-10 08:00:00','2026-09-30 23:59:59'),

('REC-000004','EMP-000001','CAT-000004',
 'Chuyên viên Marketing Digital',
 'Lên kế hoạch và triển khai chiến dịch marketing online cho thương hiệu FPT Software. Quản lý các kênh mạng xã hội, SEO, Google Ads, Facebook Ads. Phân tích và báo cáo hiệu quả chiến dịch.',
 'FULLTIME','OPEN','APPROVED',12000000,'Hà Nội','2 năm kinh nghiệm','2026-02-15 08:00:00','2026-08-30 23:59:59'),

('REC-000005','EMP-000001','CAT-000005',
 'Chuyên viên Tuyển dụng IT',
 'Thực hiện quy trình tuyển dụng end-to-end: đăng tin, sàng lọc hồ sơ, phỏng vấn và onboarding. Có kiến thức nền về CNTT là lợi thế lớn.',
 'FULLTIME','CLOSED','APPROVED',13000000,'Hà Nội','1-3 năm kinh nghiệm','2026-01-10 08:00:00','2026-03-15 23:59:59'),

-- === VNG (5 tin) ===
('REC-000006','EMP-000002','CAT-000001',
 'Lập trình viên Frontend ReactJS',
 'Phát triển và tối ưu giao diện người dùng cho các sản phẩm triệu người dùng của VNG (Zalo, ZaloPay). Yêu cầu: ReactJS, Redux Toolkit, TypeScript, hiểu biết về Web Performance.',
 'FULLTIME','OPEN','APPROVED',22000000,'TP. Hồ Chí Minh','2-4 năm kinh nghiệm','2026-02-01 09:00:00','2026-08-15 23:59:59'),

('REC-000007','EMP-000002','CAT-000001',
 'Kỹ sư DevOps / Cloud Infrastructure',
 'Thiết kế, xây dựng và vận hành hệ thống CI/CD, quản lý hạ tầng cloud (AWS/GCP). Đảm bảo độ tin cậy và hiệu năng hệ thống cho hàng triệu người dùng đồng thời.',
 'FULLTIME','OPEN','APPROVED',28000000,'TP. Hồ Chí Minh','3-5 năm kinh nghiệm','2026-02-03 09:00:00','2026-08-10 23:59:59'),

('REC-000008','EMP-000002','CAT-000006',
 'UI/UX Designer (Figma)',
 'Nghiên cứu người dùng, thiết kế wireframe, prototype và giao diện hoàn chỉnh cho ứng dụng di động và web. Làm việc trực tiếp với Product Manager và kỹ sư phát triển.',
 'FULLTIME','OPEN','APPROVED',18000000,'TP. Hồ Chí Minh','2-3 năm kinh nghiệm','2026-02-07 09:00:00','2026-08-25 23:59:59'),

('REC-000009','EMP-000002','CAT-000002',
 'Chuyên viên Kinh doanh B2B (ZaloPay)',
 'Tìm kiếm và phát triển đối tác/khách hàng doanh nghiệp cho nền tảng thanh toán ZaloPay. Đàm phán hợp đồng, hỗ trợ tích hợp API cho đối tác.',
 'FULLTIME','OPEN','APPROVED',16000000,'TP. Hồ Chí Minh','2-3 năm kinh nghiệm','2026-02-10 09:00:00','2026-08-31 23:59:59'),

('REC-000010','EMP-000002','CAT-000001',
 'Thực tập sinh Data Analyst',
 'Hỗ trợ phân tích dữ liệu người dùng, xây dựng báo cáo và dashboard bằng SQL, Python và Metabase. Được hướng dẫn trực tiếp bởi Data Science team.',
 'INTERNSHIP','OPEN','APPROVED',5000000,'TP. Hồ Chí Minh','Chưa có kinh nghiệm','2026-02-12 09:00:00','2026-09-15 23:59:59'),

-- === Viettel (5 tin) ===
('REC-000011','EMP-000003','CAT-000007',
 'Kỹ sư Mạng Viễn thông (4G/5G)',
 'Thiết kế, triển khai và vận hành hệ thống mạng vô tuyến 4G LTE và 5G NR. Phối hợp với đối tác thiết bị Ericsson, Nokia, Huawei trong các dự án mở rộng mạng lưới.',
 'FULLTIME','OPEN','APPROVED',24000000,'Hà Nội','2-5 năm kinh nghiệm','2026-02-01 10:00:00','2026-08-01 23:59:59'),

('REC-000012','EMP-000003','CAT-000001',
 'Chuyên viên An ninh Mạng (Cybersecurity)',
 'Giám sát SOC 24/7, phát hiện và xử lý các sự cố bảo mật, thực hiện pentest định kỳ. Yêu cầu: CEH hoặc OSCP là lợi thế lớn.',
 'FULLTIME','OPEN','APPROVED',22000000,'Hà Nội','3 năm kinh nghiệm','2026-02-05 10:00:00','2026-08-15 23:59:59'),

('REC-000013','EMP-000003','CAT-000003',
 'Kế toán Tổng hợp',
 'Thực hiện kế toán tổng hợp, lập báo cáo tài chính theo chuẩn mực kế toán Việt Nam, quyết toán thuế TNDN, GTGT. Sử dụng thành thạo phần mềm kế toán MISA hoặc SAP.',
 'FULLTIME','OPEN','APPROVED',14000000,'Hà Nội','2-3 năm kinh nghiệm','2026-02-08 10:00:00','2026-08-20 23:59:59'),

('REC-000014','EMP-000003','CAT-000002',
 'Nhân viên Kinh doanh Dịch vụ Viễn thông',
 'Tư vấn và kinh doanh các gói cước di động, băng thông rộng và dịch vụ CNTT của Viettel cho khách hàng cá nhân và doanh nghiệp tại khu vực Đà Nẵng.',
 'FULLTIME','OPEN','APPROVED',10000000,'Đà Nẵng','Không yêu cầu kinh nghiệm','2026-02-10 10:00:00','2026-08-31 23:59:59'),

('REC-000015','EMP-000003','CAT-000007',
 'Thực tập sinh Kỹ thuật Viễn thông',
 'Hỗ trợ đội kỹ thuật trong các dự án triển khai hạ tầng mạng. Cơ hội tiếp cận công nghệ 5G tiên tiến, được đào tạo bài bản và có cơ hội ký hợp đồng chính thức.',
 'INTERNSHIP','OPEN','APPROVED',3500000,'Hà Nội','Sinh viên năm 3-4 ngành Viễn thông/Điện tử','2026-02-15 10:00:00','2026-09-30 23:59:59');

-- ------------------------------------------------------------
-- CVs (5 CV cho 5 ứng viên)
-- ------------------------------------------------------------
INSERT INTO cvs (cv_id, candidate_id, objective, skills, desired_position, desired_salary, last_updated) VALUES
('CV-0000001','CAN-000001',
 'Tôi là sinh viên ngành Công nghệ Thông tin năm cuối tại ĐH Bách Khoa Hà Nội, đam mê lập trình backend Java và mong muốn được làm việc trong môi trường chuyên nghiệp để phát triển kỹ năng lập trình và thiết kế hệ thống.',
 'Java, Spring Boot, Hibernate, MySQL, RESTful API, Git, Postman, Linux cơ bản',
 'Lập trình viên Java Backend', 15000000, '2026-01-20 10:00:00'),

('CV-0000002','CAN-000002',
 'Kỹ sư phần mềm với 2 năm kinh nghiệm phát triển ứng dụng web full-stack. Có khả năng làm việc độc lập lẫn theo nhóm, đam mê viết code sạch và tối ưu hiệu năng hệ thống.',
 'JavaScript, TypeScript, ReactJS, NodeJS, ExpressJS, PostgreSQL, Docker, AWS, Git, CI/CD',
 'Lập trình viên Full-Stack / Frontend', 24000000, '2026-01-18 14:00:00'),

('CV-0000003','CAN-000003',
 'Sinh viên Marketing năm cuối năng động và sáng tạo, có kinh nghiệm thực tập tại công ty truyền thông. Luôn cập nhật xu hướng digital marketing và có khả năng tạo nội dung thu hút.',
 'Facebook Ads, Google Ads, SEO/SEM, Content Marketing, Canva, Adobe Photoshop cơ bản, Quản lý fanpage, Phân tích dữ liệu Google Analytics',
 'Chuyên viên Marketing Digital / Content Creator', 12000000, '2026-01-22 08:00:00'),

('CV-0000004','CAN-000004',
 'Kỹ sư Viễn thông với hơn 3 năm kinh nghiệm vận hành hệ thống mạng di động LTE. Đã triển khai thành công dự án nâng cấp mạng lưới cho hơn 200 trạm BTS tại miền Bắc.',
 'Mạng LTE/5G, Cisco CCNA, Quản trị Router/Switch, Python scripting, Linux, Wireshark, Phân tích lưu lượng mạng',
 'Kỹ sư Mạng Viễn thông / Network Engineer', 22000000, '2026-01-15 11:00:00'),

('CV-0000005','CAN-000005',
 'Sinh viên năm 3 ngành Kế toán - Kiểm toán tại ĐH Kinh tế Đà Nẵng. Cẩn thận, trung thực và có tinh thần trách nhiệm cao. Đang tìm kiếm vị trí thực tập để tích lũy kinh nghiệm thực tế.',
 'Kế toán tài chính, Excel nâng cao (VLOOKUP, PivotTable), Phần mềm MISA, Tiếng Anh đọc hiểu tài liệu kế toán',
 'Thực tập sinh / Nhân viên Kế toán', 8000000, '2026-01-25 09:00:00');

-- ------------------------------------------------------------
-- EDUCATIONS (8 mục học vấn)
-- ------------------------------------------------------------
INSERT INTO educations (education_id, cv_id, school, degree, major, start_year, end_year, description) VALUES
('EDU-000001','CV-0000001','Đại học Bách Khoa Hà Nội','Kỹ sư','Công nghệ Thông tin',2022,2026,
 'GPA: 3.2/4.0. Học bổng khuyến học năm 2024. Tham gia CLB Lập trình BKCLUB.'),
('EDU-000002','CV-0000001','THPT Nguyễn Gia Thiều, Long Biên','Tú tài',NULL,2019,2022,
 'Học sinh Giỏi, đạt giải Ba môn Tin học cấp Thành phố năm 2021.'),
('EDU-000003','CV-0000002','Đại học Khoa học Tự nhiên TP.HCM','Cử nhân','Khoa học Máy tính',2019,2023,
 'GPA: 3.5/4.0. Tốt nghiệp loại Giỏi. Đề tài luận văn: Xây dựng hệ thống gợi ý sản phẩm dựa trên Collaborative Filtering.'),
('EDU-000004','CV-0000003','Đại học Kinh tế Quốc dân, Hà Nội','Cử nhân','Quản trị Kinh doanh - Marketing',2022,2026,
 'GPA: 3.1/4.0. Thành viên CLB Marketing MKT-NEU.'),
('EDU-000005','CV-0000004','Học viện Công nghệ Bưu chính Viễn thông','Kỹ sư','Điện tử Viễn thông',2018,2023,
 'GPA: 3.4/4.0. Đề tài tốt nghiệp loại Xuất sắc: "Nghiên cứu và mô phỏng hệ thống anten Massive MIMO cho 5G".'),
('EDU-000006','CV-0000004','Cao đẳng Kỹ thuật Lý Tự Trọng, TP.HCM','Cao đẳng','Điện tử - Viễn thông',2015,2018,
 'Tốt nghiệp loại Giỏi.'),
('EDU-000007','CV-0000005','Đại học Kinh tế - Đại học Đà Nẵng','Cử nhân','Kế toán - Kiểm toán',2023,2027,
 'GPA: 3.0/4.0 (đang học). Dự kiến tốt nghiệp tháng 6/2027.'),
('EDU-000008','CV-0000002','Trường THPT Chuyên Lê Hồng Phong, TP.HCM','Tú tài',NULL,2016,2019,
 'Lớp chuyên Toán - Tin. Đạt giải Nhì môn Tin học cấp Tỉnh.');

-- ------------------------------------------------------------
-- WORK EXPERIENCES (6 kinh nghiệm)
-- ------------------------------------------------------------
INSERT INTO work_experiences (experience_id, cv_id, company_name, position, start_date, end_date, description) VALUES
('WE-0000001','CV-0000002','Công ty TNHH Giải pháp Phần mềm Tinh Vân','Lập trình viên Frontend',
 '2023-07-01','2024-12-31',
 'Phát triển giao diện web cho hệ thống quản lý nội bộ bằng ReactJS và Redux. Tối ưu hiệu năng, giảm thời gian tải trang trung bình 40%. Hỗ trợ viết unit test với Jest.'),

('WE-0000002','CV-0000002','Công ty Cổ phần KiotViet','Kỹ sư Phần mềm Full-Stack',
 '2025-01-01', NULL,
 'Phát triển tính năng mới cho hệ thống POS phục vụ hơn 150.000 cửa hàng. Thiết kế và triển khai RESTful API với NodeJS. Tham gia xây dựng kiến trúc microservices.'),

('WE-0000003','CV-0000004','Công ty Viễn thông Hà Nội (HaNoi Telecom)','Kỹ sư Mạng',
 '2023-06-01','2025-05-31',
 'Vận hành và bảo trì hệ thống mạng LTE cho 80 trạm BTS tại Hà Nội và các tỉnh lân cận. Xử lý sự cố, đảm bảo uptime 99.9%. Tham gia dự án mở rộng vùng phủ sóng 4G.'),

('WE-0000004','CV-0000004','Công ty Cổ phần Dịch vụ Kỹ thuật Viễn thông (Telcom)','Kỹ sư Hệ thống Mạng Cấp cao',
 '2025-06-01', NULL,
 'Thiết kế và triển khai hạ tầng mạng 5G tại khu công nghiệp Thăng Long. Nghiên cứu giải pháp Network Slicing, tối ưu hóa băng thông cho IoT applications.'),

('WE-0000005','CV-0000003','Công ty TNHH Truyền thông Admicro (VCCorp)','Thực tập sinh Marketing',
 '2025-06-01','2025-08-31',
 'Quản lý fanpage với 60.000 followers, lên kế hoạch nội dung hàng tuần. Tăng tương tác organic 30% trong 3 tháng. Hỗ trợ triển khai và tối ưu chiến dịch Facebook Ads.'),

('WE-0000006','CV-0000001','Công ty TNHH Phần mềm Rikkeisoft','Thực tập sinh Lập trình Java',
 '2025-07-01','2025-12-31',
 'Hỗ trợ phát triển module quản lý đơn hàng cho hệ thống ERP của khách hàng Nhật Bản. Học và áp dụng Spring Boot, Hibernate, thiết kế REST API. Tham gia code review và viết tài liệu kỹ thuật.');

-- ------------------------------------------------------------
-- CERTIFICATIONS (8 chứng chỉ)
-- ------------------------------------------------------------
INSERT INTO certifications (cert_id, cv_id, name, issuer, issue_date) VALUES
('CERT-00001','CV-0000004','Cisco Certified Network Associate (CCNA)','Cisco Systems','2024-03-15'),
('CERT-00002','CV-0000004','Huawei Certified ICT Associate - 5G (HCIA-5G)','Huawei','2025-08-20'),
('CERT-00003','CV-0000002','AWS Certified Developer - Associate (DVA-C02)','Amazon Web Services','2025-05-10'),
('CERT-00004','CV-0000002','Meta Front-End Developer Professional Certificate','Meta (Coursera)','2024-11-30'),
('CERT-00005','CV-0000001','Oracle Certified Associate, Java SE 11 Programmer','Oracle','2025-09-01'),
('CERT-00006','CV-0000003','Google Analytics Individual Qualification (GAIQ)','Google','2025-07-15'),
('CERT-00007','CV-0000003','Meta Blueprint - Certified Digital Marketing Associate','Meta','2025-10-01'),
('CERT-00008','CV-0000005','Chứng chỉ Nghiệp vụ Kế toán Thực hành','Trung tâm Đào tạo Kế toán Việt Nam','2025-12-15');

-- ------------------------------------------------------------
-- APPLICATIONS (18 đơn ứng tuyển)
-- ------------------------------------------------------------
INSERT INTO applications (application_id, candidate_id, recruitment_id, status, applied_date) VALUES
-- Nguyễn Thị Mai (CAN-000001) ứng tuyển
('APP-000001','CAN-000001','REC-000001','PENDING',   '2026-03-15 10:00:00'),
('APP-000002','CAN-000001','REC-000003','APPROVED',  '2026-03-16 11:00:00'),
('APP-000003','CAN-000001','REC-000015','PENDING',   '2026-03-28 08:00:00'),
-- Trần Văn Hùng (CAN-000002) ứng tuyển
('APP-000004','CAN-000002','REC-000001','APPROVED',  '2026-03-14 14:00:00'),
('APP-000005','CAN-000002','REC-000006','PENDING',   '2026-03-16 15:00:00'),
('APP-000006','CAN-000002','REC-000007','REJECTED',  '2026-03-18 10:00:00'),
('APP-000007','CAN-000002','REC-000010','APPROVED',  '2026-03-20 13:00:00'),
('APP-000008','CAN-000002','REC-000002','PENDING',   '2026-03-22 09:00:00'),
-- Lê Thị Lan (CAN-000003) ứng tuyển
('APP-000009','CAN-000003','REC-000004','PENDING',   '2026-03-20 08:00:00'),
('APP-000010','CAN-000003','REC-000008','APPROVED',  '2026-03-22 09:00:00'),
('APP-000011','CAN-000003','REC-000009','REJECTED',  '2026-03-23 10:00:00'),
-- Hoàng Minh Đức (CAN-000004) ứng tuyển
('APP-000012','CAN-000004','REC-000011','APPROVED',  '2026-03-15 11:00:00'),
('APP-000013','CAN-000004','REC-000012','PENDING',   '2026-03-17 14:00:00'),
('APP-000014','CAN-000004','REC-000007','REJECTED',  '2026-03-19 15:00:00'),
('APP-000015','CAN-000004','REC-000001','PENDING',   '2026-03-25 10:00:00'),
-- Võ Thị Thu (CAN-000005) ứng tuyển
('APP-000016','CAN-000005','REC-000013','PENDING',   '2026-03-25 09:00:00'),
('APP-000017','CAN-000005','REC-000014','PENDING',   '2026-03-26 10:00:00'),
('APP-000018','CAN-000005','REC-000005','PENDING',   '2026-03-27 11:00:00');

-- ------------------------------------------------------------
-- NOTIFICATIONS (15 thông báo)
-- ------------------------------------------------------------
INSERT INTO notifications (notification_id, sender_id, receiver_id, content, date_time, is_read) VALUES
-- Thông báo duyệt đơn
('NOT-000001','U-00000002','U-00000005','Đơn ứng tuyển vị trí "Thực tập sinh Phát triển Phần mềm" của bạn đã được chấp thuận. Chúng tôi sẽ liên hệ để sắp xếp lịch phỏng vấn trong 3 ngày tới.','2026-03-17 09:00:00',TRUE),
('NOT-000002','U-00000002','U-00000006','Đơn ứng tuyển vị trí "Lập trình viên Java Backend" của bạn đã được chấp thuận. Chúc mừng! HR sẽ gửi email chi tiết về buổi phỏng vấn kỹ thuật.','2026-03-15 16:00:00',TRUE),
('NOT-000003','U-00000003','U-00000006','Rất tiếc, đơn ứng tuyển vị trí "Kỹ sư DevOps / Cloud Infrastructure" chưa phù hợp với yêu cầu hiện tại. Chúc bạn tìm được công việc phù hợp!','2026-03-20 10:00:00',FALSE),
('NOT-000004','U-00000003','U-00000007','Đơn ứng tuyển vị trí "UI/UX Designer" của bạn đã được duyệt. Vui lòng chuẩn bị portfolio và tham gia buổi phỏng vấn vào ngày 28/04/2026.','2026-03-23 14:00:00',TRUE),
('NOT-000005','U-00000003','U-00000007','Rất tiếc, hồ sơ ứng tuyển vị trí "Chuyên viên Kinh doanh B2B" của bạn không đáp ứng yêu cầu về kinh nghiệm bán hàng B2B. Cảm ơn bạn đã quan tâm đến VNG.','2026-03-25 08:30:00',FALSE),
('NOT-000006','U-00000004','U-00000008','Chúc mừng! Đơn ứng tuyển vị trí "Kỹ sư Mạng Viễn thông (4G/5G)" của bạn đã được duyệt. Hẹn gặp bạn tại buổi phỏng vấn ngày 01/05/2026 lúc 9h00.','2026-03-16 15:00:00',TRUE),
('NOT-000007','U-00000004','U-00000008','Đơn ứng tuyển vị trí "Kỹ sư DevOps" tại VNG của bạn chưa được chấp nhận. Kinh nghiệm mạng viễn thông chưa phù hợp với yêu cầu Cloud Infrastructure.','2026-03-21 09:00:00',TRUE),
-- Thông báo từ Admin
('NOT-000008','U-00000001','U-00000002','Tin tuyển dụng "Chuyên viên Tuyển dụng IT" của FPT Software đã bị đóng theo yêu cầu hệ thống do hết hạn đăng tuyển.','2026-03-16 08:00:00',TRUE),
('NOT-000009','U-00000001','U-00000003','Thông báo: Hệ thống sẽ bảo trì vào ngày 01/04/2026 từ 23:00 đến 02:00. Trong thời gian này, các chức năng đăng tin và xét duyệt hồ sơ tạm thời không khả dụng.','2026-03-28 17:00:00',TRUE),
('NOT-000010','U-00000001','U-00000004','Tin tuyển dụng "Nhân viên Kinh doanh Dịch vụ Viễn thông" (Đà Nẵng) của Viettel đã được quản trị viên phê duyệt và hiển thị công khai trên hệ thống.','2026-02-11 08:00:00',TRUE),
-- Thông báo hệ thống cho ứng viên
('NOT-000011','U-00000001','U-00000005','Chào mừng bạn đã tạo tài khoản thành công trên Hệ thống Tìm Việc Làm! Hãy hoàn thiện CV để tăng cơ hội được nhà tuyển dụng chú ý.','2026-01-10 10:05:00',TRUE),
('NOT-000012','U-00000001','U-00000009','Chào mừng bạn đã tạo tài khoản thành công! Hiện có 5 vị trí phù hợp với ngành Kế toán / Tài chính đang tuyển dụng. Khám phá ngay!','2026-01-14 09:05:00',FALSE),
-- Thông báo ứng viên mới cho nhà tuyển dụng
('NOT-000013','U-00000001','U-00000002','Có 3 ứng viên mới ứng tuyển vào vị trí "Lập trình viên Java Backend" trong hôm nay. Vui lòng xem xét và phản hồi sớm.','2026-03-15 18:00:00',FALSE),
('NOT-000014','U-00000001','U-00000003','Tin tuyển dụng "Lập trình viên Frontend ReactJS" của bạn sắp hết hạn vào ngày 15/08/2026. Hãy gia hạn hoặc cập nhật tin để tiếp tục nhận hồ sơ.','2026-05-08 08:00:00',FALSE),
('NOT-000015','U-00000001','U-00000008','Nhắc nhở: Bạn có 1 đơn ứng tuyển chưa nhận được phản hồi quá 14 ngày. Bạn có thể liên hệ trực tiếp nhà tuyển dụng hoặc tìm kiếm cơ hội khác phù hợp hơn.','2026-04-05 09:00:00',FALSE);

-- ============================================================
-- KIỂM TRA DỮ LIỆU (tùy chọn - chạy sau khi import xong)
-- ============================================================
-- SELECT 'users'         AS bang, COUNT(*) AS so_ban_ghi FROM users
-- UNION ALL
-- SELECT 'employers',     COUNT(*) FROM employers
-- UNION ALL
-- SELECT 'candidates',    COUNT(*) FROM candidates
-- UNION ALL
-- SELECT 'categories',    COUNT(*) FROM categories
-- UNION ALL
-- SELECT 'recruitments',  COUNT(*) FROM recruitments
-- UNION ALL
-- SELECT 'cvs',           COUNT(*) FROM cvs
-- UNION ALL
-- SELECT 'educations',    COUNT(*) FROM educations
-- UNION ALL
-- SELECT 'work_experiences', COUNT(*) FROM work_experiences
-- UNION ALL
-- SELECT 'certifications',COUNT(*) FROM certifications
-- UNION ALL
-- SELECT 'applications',  COUNT(*) FROM applications
-- UNION ALL
-- SELECT 'notifications', COUNT(*) FROM notifications;

-- Kiểm tra độ dài ID:
-- SELECT user_id, CHAR_LENGTH(user_id) FROM users WHERE CHAR_LENGTH(user_id) <> 10;
-- SELECT employer_id, CHAR_LENGTH(employer_id) FROM employers WHERE CHAR_LENGTH(employer_id) <> 10;
-- SELECT candidate_id, CHAR_LENGTH(candidate_id) FROM candidates WHERE CHAR_LENGTH(candidate_id) <> 10;
-- SELECT category_id, CHAR_LENGTH(category_id) FROM categories WHERE CHAR_LENGTH(category_id) <> 10;
-- SELECT recruitment_id, CHAR_LENGTH(recruitment_id) FROM recruitments WHERE CHAR_LENGTH(recruitment_id) <> 10;
-- SELECT cv_id, CHAR_LENGTH(cv_id) FROM cvs WHERE CHAR_LENGTH(cv_id) <> 10;
-- SELECT education_id, CHAR_LENGTH(education_id) FROM educations WHERE CHAR_LENGTH(education_id) <> 10;
-- SELECT experience_id, CHAR_LENGTH(experience_id) FROM work_experiences WHERE CHAR_LENGTH(experience_id) <> 10;
-- SELECT cert_id, CHAR_LENGTH(cert_id) FROM certifications WHERE CHAR_LENGTH(cert_id) <> 10;
-- SELECT application_id, CHAR_LENGTH(application_id) FROM applications WHERE CHAR_LENGTH(application_id) <> 10;
-- SELECT notification_id, CHAR_LENGTH(notification_id) FROM notifications WHERE CHAR_LENGTH(notification_id) <> 10;