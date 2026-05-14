-- ============================================================
--  Há»† THá»NG TÃŒM VIá»†C LÃ€M - SCRIPT Táº O DATABASE VÃ€ Dá»® LIá»†U MáºªU
--  Encoding: UTF-8
--  TÆ°Æ¡ng thÃ­ch: MySQL 8.0+
-- ============================================================

-- Quy Æ°á»›c ID: táº¥t cáº£ ID dÃ¹ng VARCHAR(10), má»—i ID máº«u bÃªn dÆ°á»›i Ä‘á»u Ä‘Ãºng 10 kÃ½ tá»±.
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
-- XÃ“A Báº¢NG CÅ¨ (theo thá»© tá»± phá»¥ thuá»™c khÃ³a ngoáº¡i)
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
-- Táº O CÃC Báº¢NG
-- ============================================================

-- Báº£ng ngÆ°á»i dÃ¹ng (gá»™p cáº£ Employer, Candidate, Admin qua cá»™t role)
CREATE TABLE users (
	user_id       VARCHAR(10)  PRIMARY KEY,
	username      VARCHAR(50)  NOT NULL UNIQUE,
	password_hash VARCHAR(255) NOT NULL,
	full_name     VARCHAR(150) NOT NULL,
	phone_number  VARCHAR(15),
	date_of_birth DATE,
	gender        ENUM('MALE','FEMALE','OTHER'),
	email         VARCHAR(100) NOT NULL UNIQUE,
	address       VARCHAR(255),
	role          ENUM('CANDIDATE','EMPLOYER','ADMIN') NOT NULL,
	is_active     BOOLEAN      DEFAULT TRUE,
	created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP
);

-- ThÃ´ng tin riÃªng cá»§a nhÃ  tuyá»ƒn dá»¥ng
CREATE TABLE employers (
	employer_id         VARCHAR(10)  PRIMARY KEY,
	user_id             VARCHAR(10)  NOT NULL UNIQUE,
	company_name        VARCHAR(200) NOT NULL,
	company_address     VARCHAR(300),
	company_description TEXT,
	FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ThÃ´ng tin riÃªng cá»§a á»©ng viÃªn
CREATE TABLE candidates (
	candidate_id VARCHAR(10) PRIMARY KEY,
	user_id      VARCHAR(10) NOT NULL UNIQUE,
	FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Danh má»¥c ngÃ nh nghá»
CREATE TABLE categories (
	category_id   VARCHAR(10)  PRIMARY KEY,
	category_name VARCHAR(150) NOT NULL
);

-- Tin tuyá»ƒn dá»¥ng
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

-- CV cá»§a á»©ng viÃªn (1 á»©ng viÃªn - 1 CV)
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

-- Há»c váº¥n (1 CV - nhiá»u má»¥c há»c váº¥n)
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

-- Kinh nghiá»‡m lÃ m viá»‡c (1 CV - nhiá»u kinh nghiá»‡m)
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

-- Chá»©ng chá»‰ (1 CV - nhiá»u chá»©ng chá»‰)
CREATE TABLE certifications (
	cert_id    VARCHAR(10)  PRIMARY KEY,
	cv_id      VARCHAR(10)  NOT NULL,
	name       VARCHAR(250),
	issuer     VARCHAR(250),
	issue_date DATE,
	FOREIGN KEY (cv_id) REFERENCES cvs(cv_id) ON DELETE CASCADE
);

-- ÄÆ¡n á»©ng tuyá»ƒn (1 á»©ng viÃªn khÃ´ng á»©ng tuyá»ƒn 2 láº§n vÃ o cÃ¹ng 1 vá»‹ trÃ­)
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

-- ThÃ´ng bÃ¡o há»‡ thá»‘ng
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
-- CHÃˆN Dá»® LIá»†U MáºªU
-- ============================================================
-- Ghi chÃº: password_hash máº«u dÃ¹ng SHA-256 cá»§a chuá»—i "MatKhau@123"
-- Ä‘á»ƒ khá»›p vá»›i org.jobportal.utils.PasswordUtils hiá»‡n táº¡i.

-- ------------------------------------------------------------
-- USERS
-- ------------------------------------------------------------
INSERT INTO users (user_id, username, password_hash, full_name, phone_number,
				   date_of_birth, gender, email, address, role, is_active, created_at) VALUES
-- Admin
('U-00000001', 'admin',              '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Nguyá»…n VÄƒn Quáº£n Trá»‹',  '0900000001', '1985-01-15', 'MALE',   'admin@timviec.vn',          'Ha Noi',                               'ADMIN',     TRUE, '2024-01-01 08:00:00'),
-- NhÃ  tuyá»ƒn dá»¥ng
('U-00000002', 'fpt_hr',             '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Tráº§n Thá»‹ BÃ­ch Ngá»c',   '0912345678', '1988-03-22', 'FEMALE', 'tuyendung@fpt.com.vn',      'Cau Giay, Ha Noi',                     'EMPLOYER',  TRUE, '2024-01-05 09:00:00'),
('U-00000003', 'vng_recruit',        '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'LÃª Quang Huy',          '0923456789', '1990-07-10', 'MALE',   'tuyendung@vng.com.vn',      'Quan 11, TP. Ho Chi Minh',            'EMPLOYER',  TRUE, '2024-01-06 09:30:00'),
('U-00000004', 'viettel_td',         '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Pháº¡m Thá»‹ Ngá»c Ãnh',    '0934567890', '1987-11-05', 'FEMALE', 'tuyendung@viettel.com.vn',  'Ba Dinh, Ha Noi',                      'EMPLOYER',  TRUE, '2024-01-07 10:00:00'),
-- á»¨ng viÃªn
('U-00000005', 'nguyen_mai',         '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Nguyá»…n Thá»‹ Mai',        '0945111222', '1999-05-14', 'FEMALE', 'mai.nguyen99@gmail.com',    'Long Bien, Ha Noi',                    'CANDIDATE', TRUE, '2024-01-10 10:00:00'),
('U-00000006', 'tran_hung',          '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'Tráº§n VÄƒn HÃ¹ng',         '0956222333', '1998-08-20', 'MALE',   'hung.tran98@gmail.com',     'Quan 7, TP. Ho Chi Minh',             'CANDIDATE', TRUE, '2024-01-11 11:00:00'),
('U-00000007', 'le_lan',             '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'LÃª Thá»‹ Lan',            '0967333444', '2000-02-28', 'FEMALE', 'lan.le2000@gmail.com',      'Dong Da, Ha Noi',                      'CANDIDATE', TRUE, '2024-01-12 08:30:00'),
('U-00000008', 'hoang_duc',          '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'HoÃ ng Minh Äá»©c',        '0978444555', '1997-12-01', 'MALE',   'duc.hoang97@gmail.com',     'Ba Dinh, Ha Noi',                      'CANDIDATE', TRUE, '2024-01-13 14:00:00'),
('U-00000009', 'vo_thu',             '58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486',
 'VÃµ Thá»‹ Thu',            '0989555666', '2001-06-15', 'FEMALE', 'thu.vo2001@gmail.com',      'Hai Chau, Da Nang',                    'CANDIDATE', TRUE, '2024-01-14 09:00:00');

-- ------------------------------------------------------------
-- EMPLOYERS
-- ------------------------------------------------------------
INSERT INTO employers (employer_id, user_id, company_name, company_address, company_description) VALUES
('EMP-000001', 'U-00000002',
 'CÃ´ng ty Cá»• pháº§n FPT Software',
 'TÃ²a nhÃ  FPT, Sá»‘ 17 Duy TÃ¢n, Cáº§u Giáº¥y, HÃ  Ná»™i',
 'FPT Software lÃ  cÃ´ng ty thÃ nh viÃªn cá»§a Táº­p Ä‘oÃ n FPT, chuyÃªn cung cáº¥p cÃ¡c giáº£i phÃ¡p cÃ´ng nghá»‡ thÃ´ng tin, pháº§n má»m vÃ  dá»‹ch vá»¥ CNTT cho khÃ¡ch hÃ ng trong nÆ°á»›c vÃ  quá»‘c táº¿. Vá»›i hÆ¡n 20.000 nhÃ¢n viÃªn toÃ n cáº§u, FPT Software lÃ  má»™t trong nhá»¯ng nhÃ  cung cáº¥p dá»‹ch vá»¥ CNTT lá»›n nháº¥t Viá»‡t Nam.'),
('EMP-000002', 'U-00000003',
 'CÃ´ng ty Cá»• pháº§n VNG',
 'Sá»‘ 182-184 LÃª Äáº¡i HÃ nh, PhÆ°á»ng 15, Quáº­n 11, TP. Há»“ ChÃ­ Minh',
 'VNG lÃ  cÃ´ng ty cÃ´ng nghá»‡ hÃ ng Ä‘áº§u Viá»‡t Nam vá»›i hÆ¡n 4.000 nhÃ¢n viÃªn. CÃ¡c sáº£n pháº©m ná»•i tiáº¿ng bao gá»“m Zalo, ZaloPay, vÃ  nhiá»u ná»n táº£ng game, giáº£i trÃ­, thÆ°Æ¡ng máº¡i Ä‘iá»‡n tá»­. VNG hÆ°á»›ng tá»›i má»¥c tiÃªu trá»Ÿ thÃ nh cÃ´ng ty cÃ´ng nghá»‡ mang táº§m vÃ³c khu vá»±c.'),
('EMP-000003', 'U-00000004',
 'Táº­p Ä‘oÃ n CÃ´ng nghiá»‡p - Viá»…n thÃ´ng QuÃ¢n Ä‘á»™i (Viettel)',
 'Sá»‘ 1 Giang VÄƒn Minh, Ba ÄÃ¬nh, HÃ  Ná»™i',
 'Viettel lÃ  táº­p Ä‘oÃ n viá»…n thÃ´ng vÃ  cÃ´ng nghá»‡ lá»›n nháº¥t Viá»‡t Nam, hoáº¡t Ä‘á»™ng táº¡i 11 quá»‘c gia vá»›i hÆ¡n 100 triá»‡u khÃ¡ch hÃ ng. Viettel tiÃªn phong trong phÃ¡t triá»ƒn háº¡ táº§ng 5G vÃ  cÃ¡c giáº£i phÃ¡p chuyá»ƒn Ä‘á»•i sá»‘ quá»‘c gia.');

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
('CAT-000001', 'CÃ´ng nghá»‡ ThÃ´ng tin'),
('CAT-000002', 'Kinh doanh / BÃ¡n hÃ ng'),
('CAT-000003', 'Káº¿ toÃ¡n / TÃ i chÃ­nh'),
('CAT-000004', 'Marketing / Truyá»n thÃ´ng'),
('CAT-000005', 'NhÃ¢n sá»± / HÃ nh chÃ­nh'),
('CAT-000006', 'Thiáº¿t káº¿ Äá»“ há»a / UI-UX'),
('CAT-000007', 'Ká»¹ thuáº­t / Viá»…n thÃ´ng'),
('CAT-000008', 'GiÃ¡o dá»¥c / ÄÃ o táº¡o');

-- ------------------------------------------------------------
-- RECRUITMENTS (15 tin tuyá»ƒn dá»¥ng)
-- ------------------------------------------------------------
INSERT INTO recruitments
  (recruitment_id, employer_id, category_id, title, description,
   job_type, status, admin_status, salary, location, experience_required,
   created_date, due_date)
VALUES
-- === FPT Software (5 tin) ===
('REC-000001','EMP-000001','CAT-000001',
 'Láº­p trÃ¬nh viÃªn Java Backend (Spring Boot)',
 'Tham gia phÃ¡t triá»ƒn vÃ  báº£o trÃ¬ cÃ¡c á»©ng dá»¥ng backend quy mÃ´ lá»›n cho khÃ¡ch hÃ ng Nháº­t Báº£n. CÃ´ng nghá»‡ sá»­ dá»¥ng: Java 17, Spring Boot, Hibernate, MySQL, Redis, Docker. MÃ´i trÆ°á»ng lÃ m viá»‡c Agile/Scrum.',
 'FULLTIME','OPEN','APPROVED',18000000,'HÃ  Ná»™i','1-3 nÄƒm kinh nghiá»‡m','2024-02-01 08:00:00','2024-05-31 23:59:59'),

('REC-000002','EMP-000001','CAT-000001',
 'Ká»¹ sÆ° Kiá»ƒm thá»­ Pháº§n má»m (Manual & Automation Tester)',
 'XÃ¢y dá»±ng káº¿ hoáº¡ch kiá»ƒm thá»­, viáº¿t test case, thá»±c hiá»‡n kiá»ƒm thá»­ chá»©c nÄƒng vÃ  phi chá»©c nÄƒng. Æ¯u tiÃªn á»©ng viÃªn biáº¿t Selenium, JUnit hoáº·c TestNG.',
 'FULLTIME','OPEN','APPROVED',14000000,'HÃ  Ná»™i','1-2 nÄƒm kinh nghiá»‡m','2024-02-05 08:00:00','2024-05-20 23:59:59'),

('REC-000003','EMP-000001','CAT-000001',
 'Thá»±c táº­p sinh PhÃ¡t triá»ƒn Pháº§n má»m',
 'Há»— trá»£ nhÃ³m phÃ¡t triá»ƒn trong coding, testing vÃ  tÃ i liá»‡u hÃ³a. ÄÆ°á»£c Ä‘Ã o táº¡o thá»±c táº¿ bá»Ÿi cÃ¡c chuyÃªn gia. CÃ³ cÆ¡ há»™i Ä‘Æ°á»£c nháº­n vÃ o lÃ m chÃ­nh thá»©c sau ká»³ thá»±c táº­p.',
 'INTERNSHIP','OPEN','APPROVED',4000000,'HÃ  Ná»™i','ChÆ°a cÃ³ kinh nghiá»‡m (sinh viÃªn nÄƒm 3-4)','2024-02-10 08:00:00','2024-06-30 23:59:59'),

('REC-000004','EMP-000001','CAT-000004',
 'ChuyÃªn viÃªn Marketing Digital',
 'LÃªn káº¿ hoáº¡ch vÃ  triá»ƒn khai chiáº¿n dá»‹ch marketing online cho thÆ°Æ¡ng hiá»‡u FPT Software. Quáº£n lÃ½ cÃ¡c kÃªnh máº¡ng xÃ£ há»™i, SEO, Google Ads, Facebook Ads. PhÃ¢n tÃ­ch vÃ  bÃ¡o cÃ¡o hiá»‡u quáº£ chiáº¿n dá»‹ch.',
 'FULLTIME','OPEN','APPROVED',12000000,'HÃ  Ná»™i','2 nÄƒm kinh nghiá»‡m','2024-02-15 08:00:00','2024-05-30 23:59:59'),

('REC-000005','EMP-000001','CAT-000005',
 'ChuyÃªn viÃªn Tuyá»ƒn dá»¥ng IT',
 'Thá»±c hiá»‡n quy trÃ¬nh tuyá»ƒn dá»¥ng end-to-end: Ä‘Äƒng tin, sÃ ng lá»c há»“ sÆ¡, phá»ng váº¥n vÃ  onboarding. CÃ³ kiáº¿n thá»©c ná»n vá» CNTT lÃ  lá»£i tháº¿ lá»›n.',
 'FULLTIME','CLOSED','APPROVED',13000000,'HÃ  Ná»™i','1-3 nÄƒm kinh nghiá»‡m','2024-01-10 08:00:00','2024-03-15 23:59:59'),

-- === VNG (5 tin) ===
('REC-000006','EMP-000002','CAT-000001',
 'Láº­p trÃ¬nh viÃªn Frontend ReactJS',
 'PhÃ¡t triá»ƒn vÃ  tá»‘i Æ°u giao diá»‡n ngÆ°á»i dÃ¹ng cho cÃ¡c sáº£n pháº©m triá»‡u ngÆ°á»i dÃ¹ng cá»§a VNG (Zalo, ZaloPay). YÃªu cáº§u: ReactJS, Redux Toolkit, TypeScript, hiá»ƒu biáº¿t vá» Web Performance.',
 'FULLTIME','OPEN','APPROVED',22000000,'TP. Há»“ ChÃ­ Minh','2-4 nÄƒm kinh nghiá»‡m','2024-02-01 09:00:00','2024-05-15 23:59:59'),

('REC-000007','EMP-000002','CAT-000001',
 'Ká»¹ sÆ° DevOps / Cloud Infrastructure',
 'Thiáº¿t káº¿, xÃ¢y dá»±ng vÃ  váº­n hÃ nh há»‡ thá»‘ng CI/CD, quáº£n lÃ½ háº¡ táº§ng cloud (AWS/GCP). Äáº£m báº£o Ä‘á»™ tin cáº­y vÃ  hiá»‡u nÄƒng há»‡ thá»‘ng cho hÃ ng triá»‡u ngÆ°á»i dÃ¹ng Ä‘á»“ng thá»i.',
 'FULLTIME','OPEN','APPROVED',28000000,'TP. Há»“ ChÃ­ Minh','3-5 nÄƒm kinh nghiá»‡m','2024-02-03 09:00:00','2024-05-10 23:59:59'),

('REC-000008','EMP-000002','CAT-000006',
 'UI/UX Designer (Figma)',
 'NghiÃªn cá»©u ngÆ°á»i dÃ¹ng, thiáº¿t káº¿ wireframe, prototype vÃ  giao diá»‡n hoÃ n chá»‰nh cho á»©ng dá»¥ng di Ä‘á»™ng vÃ  web. LÃ m viá»‡c trá»±c tiáº¿p vá»›i Product Manager vÃ  ká»¹ sÆ° phÃ¡t triá»ƒn.',
 'FULLTIME','OPEN','APPROVED',18000000,'TP. Há»“ ChÃ­ Minh','2-3 nÄƒm kinh nghiá»‡m','2024-02-07 09:00:00','2024-05-25 23:59:59'),

('REC-000009','EMP-000002','CAT-000002',
 'ChuyÃªn viÃªn Kinh doanh B2B (ZaloPay)',
 'TÃ¬m kiáº¿m vÃ  phÃ¡t triá»ƒn Ä‘á»‘i tÃ¡c/khÃ¡ch hÃ ng doanh nghiá»‡p cho ná»n táº£ng thanh toÃ¡n ZaloPay. ÄÃ m phÃ¡n há»£p Ä‘á»“ng, há»— trá»£ tÃ­ch há»£p API cho Ä‘á»‘i tÃ¡c.',
 'FULLTIME','OPEN','APPROVED',16000000,'TP. Há»“ ChÃ­ Minh','2-3 nÄƒm kinh nghiá»‡m','2024-02-10 09:00:00','2024-05-31 23:59:59'),

('REC-000010','EMP-000002','CAT-000001',
 'Thá»±c táº­p sinh Data Analyst',
 'Há»— trá»£ phÃ¢n tÃ­ch dá»¯ liá»‡u ngÆ°á»i dÃ¹ng, xÃ¢y dá»±ng bÃ¡o cÃ¡o vÃ  dashboard báº±ng SQL, Python vÃ  Metabase. ÄÆ°á»£c hÆ°á»›ng dáº«n trá»±c tiáº¿p bá»Ÿi Data Science team.',
 'INTERNSHIP','OPEN','APPROVED',5000000,'TP. Há»“ ChÃ­ Minh','ChÆ°a cÃ³ kinh nghiá»‡m','2024-02-12 09:00:00','2024-06-15 23:59:59'),

-- === Viettel (5 tin) ===
('REC-000011','EMP-000003','CAT-000007',
 'Ká»¹ sÆ° Máº¡ng Viá»…n thÃ´ng (4G/5G)',
 'Thiáº¿t káº¿, triá»ƒn khai vÃ  váº­n hÃ nh há»‡ thá»‘ng máº¡ng vÃ´ tuyáº¿n 4G LTE vÃ  5G NR. Phá»‘i há»£p vá»›i Ä‘á»‘i tÃ¡c thiáº¿t bá»‹ Ericsson, Nokia, Huawei trong cÃ¡c dá»± Ã¡n má»Ÿ rá»™ng máº¡ng lÆ°á»›i.',
 'FULLTIME','OPEN','APPROVED',24000000,'HÃ  Ná»™i','2-5 nÄƒm kinh nghiá»‡m','2024-02-01 10:00:00','2024-05-01 23:59:59'),

('REC-000012','EMP-000003','CAT-000001',
 'ChuyÃªn viÃªn An ninh Máº¡ng (Cybersecurity)',
 'GiÃ¡m sÃ¡t SOC 24/7, phÃ¡t hiá»‡n vÃ  xá»­ lÃ½ cÃ¡c sá»± cá»‘ báº£o máº­t, thá»±c hiá»‡n pentest Ä‘á»‹nh ká»³. YÃªu cáº§u: CEH hoáº·c OSCP lÃ  lá»£i tháº¿ lá»›n.',
 'FULLTIME','OPEN','APPROVED',22000000,'HÃ  Ná»™i','3 nÄƒm kinh nghiá»‡m','2024-02-05 10:00:00','2024-05-15 23:59:59'),

('REC-000013','EMP-000003','CAT-000003',
 'Káº¿ toÃ¡n Tá»•ng há»£p',
 'Thá»±c hiá»‡n káº¿ toÃ¡n tá»•ng há»£p, láº­p bÃ¡o cÃ¡o tÃ i chÃ­nh theo chuáº©n má»±c káº¿ toÃ¡n Viá»‡t Nam, quyáº¿t toÃ¡n thuáº¿ TNDN, GTGT. Sá»­ dá»¥ng thÃ nh tháº¡o pháº§n má»m káº¿ toÃ¡n MISA hoáº·c SAP.',
 'FULLTIME','OPEN','APPROVED',14000000,'HÃ  Ná»™i','2-3 nÄƒm kinh nghiá»‡m','2024-02-08 10:00:00','2024-05-20 23:59:59'),

('REC-000014','EMP-000003','CAT-000002',
 'NhÃ¢n viÃªn Kinh doanh Dá»‹ch vá»¥ Viá»…n thÃ´ng',
 'TÆ° váº¥n vÃ  kinh doanh cÃ¡c gÃ³i cÆ°á»›c di Ä‘á»™ng, bÄƒng thÃ´ng rá»™ng vÃ  dá»‹ch vá»¥ CNTT cá»§a Viettel cho khÃ¡ch hÃ ng cÃ¡ nhÃ¢n vÃ  doanh nghiá»‡p táº¡i khu vá»±c ÄÃ  Náºµng.',
 'FULLTIME','OPEN','APPROVED',10000000,'ÄÃ  Náºµng','KhÃ´ng yÃªu cáº§u kinh nghiá»‡m','2024-02-10 10:00:00','2024-05-31 23:59:59'),

('REC-000015','EMP-000003','CAT-000007',
 'Thá»±c táº­p sinh Ká»¹ thuáº­t Viá»…n thÃ´ng',
 'Há»— trá»£ Ä‘á»™i ká»¹ thuáº­t trong cÃ¡c dá»± Ã¡n triá»ƒn khai háº¡ táº§ng máº¡ng. CÆ¡ há»™i tiáº¿p cáº­n cÃ´ng nghá»‡ 5G tiÃªn tiáº¿n, Ä‘Æ°á»£c Ä‘Ã o táº¡o bÃ i báº£n vÃ  cÃ³ cÆ¡ há»™i kÃ½ há»£p Ä‘á»“ng chÃ­nh thá»©c.',
 'INTERNSHIP','OPEN','APPROVED',3500000,'HÃ  Ná»™i','Sinh viÃªn nÄƒm 3-4 ngÃ nh Viá»…n thÃ´ng/Äiá»‡n tá»­','2024-02-15 10:00:00','2024-06-30 23:59:59');

-- ------------------------------------------------------------
-- CVs (5 CV cho 5 á»©ng viÃªn)
-- ------------------------------------------------------------
INSERT INTO cvs (cv_id, candidate_id, objective, skills, desired_position, desired_salary, last_updated) VALUES
('CV-0000001','CAN-000001',
 'TÃ´i lÃ  sinh viÃªn ngÃ nh CÃ´ng nghá»‡ ThÃ´ng tin nÄƒm cuá»‘i táº¡i ÄH BÃ¡ch Khoa HÃ  Ná»™i, Ä‘am mÃª láº­p trÃ¬nh backend Java vÃ  mong muá»‘n Ä‘Æ°á»£c lÃ m viá»‡c trong mÃ´i trÆ°á»ng chuyÃªn nghiá»‡p Ä‘á»ƒ phÃ¡t triá»ƒn ká»¹ nÄƒng láº­p trÃ¬nh vÃ  thiáº¿t káº¿ há»‡ thá»‘ng.',
 'Java, Spring Boot, Hibernate, MySQL, RESTful API, Git, Postman, Linux cÆ¡ báº£n',
 'Láº­p trÃ¬nh viÃªn Java Backend', 15000000, '2024-01-20 10:00:00'),

('CV-0000002','CAN-000002',
 'Ká»¹ sÆ° pháº§n má»m vá»›i 2 nÄƒm kinh nghiá»‡m phÃ¡t triá»ƒn á»©ng dá»¥ng web full-stack. CÃ³ kháº£ nÄƒng lÃ m viá»‡c Ä‘á»™c láº­p láº«n theo nhÃ³m, Ä‘am mÃª viáº¿t code sáº¡ch vÃ  tá»‘i Æ°u hiá»‡u nÄƒng há»‡ thá»‘ng.',
 'JavaScript, TypeScript, ReactJS, NodeJS, ExpressJS, PostgreSQL, Docker, AWS, Git, CI/CD',
 'Láº­p trÃ¬nh viÃªn Full-Stack / Frontend', 24000000, '2024-01-18 14:00:00'),

('CV-0000003','CAN-000003',
 'Sinh viÃªn Marketing nÄƒm cuá»‘i nÄƒng Ä‘á»™ng vÃ  sÃ¡ng táº¡o, cÃ³ kinh nghiá»‡m thá»±c táº­p táº¡i cÃ´ng ty truyá»n thÃ´ng. LuÃ´n cáº­p nháº­t xu hÆ°á»›ng digital marketing vÃ  cÃ³ kháº£ nÄƒng táº¡o ná»™i dung thu hÃºt.',
 'Facebook Ads, Google Ads, SEO/SEM, Content Marketing, Canva, Adobe Photoshop cÆ¡ báº£n, Quáº£n lÃ½ fanpage, PhÃ¢n tÃ­ch dá»¯ liá»‡u Google Analytics',
 'ChuyÃªn viÃªn Marketing Digital / Content Creator', 12000000, '2024-01-22 08:00:00'),

('CV-0000004','CAN-000004',
 'Ká»¹ sÆ° Viá»…n thÃ´ng vá»›i hÆ¡n 3 nÄƒm kinh nghiá»‡m váº­n hÃ nh há»‡ thá»‘ng máº¡ng di Ä‘á»™ng LTE. ÄÃ£ triá»ƒn khai thÃ nh cÃ´ng dá»± Ã¡n nÃ¢ng cáº¥p máº¡ng lÆ°á»›i cho hÆ¡n 200 tráº¡m BTS táº¡i miá»n Báº¯c.',
 'Máº¡ng LTE/5G, Cisco CCNA, Quáº£n trá»‹ Router/Switch, Python scripting, Linux, Wireshark, PhÃ¢n tÃ­ch lÆ°u lÆ°á»£ng máº¡ng',
 'Ká»¹ sÆ° Máº¡ng Viá»…n thÃ´ng / Network Engineer', 22000000, '2024-01-15 11:00:00'),

('CV-0000005','CAN-000005',
 'Sinh viÃªn nÄƒm 3 ngÃ nh Káº¿ toÃ¡n - Kiá»ƒm toÃ¡n táº¡i ÄH Kinh táº¿ ÄÃ  Náºµng. Cáº©n tháº­n, trung thá»±c vÃ  cÃ³ tinh tháº§n trÃ¡ch nhiá»‡m cao. Äang tÃ¬m kiáº¿m vá»‹ trÃ­ thá»±c táº­p Ä‘á»ƒ tÃ­ch lÅ©y kinh nghiá»‡m thá»±c táº¿.',
 'Káº¿ toÃ¡n tÃ i chÃ­nh, Excel nÃ¢ng cao (VLOOKUP, PivotTable), Pháº§n má»m MISA, Tiáº¿ng Anh Ä‘á»c hiá»ƒu tÃ i liá»‡u káº¿ toÃ¡n',
 'Thá»±c táº­p sinh / NhÃ¢n viÃªn Káº¿ toÃ¡n', 8000000, '2024-01-25 09:00:00');

-- ------------------------------------------------------------
-- EDUCATIONS (8 má»¥c há»c váº¥n)
-- ------------------------------------------------------------
INSERT INTO educations (education_id, cv_id, school, degree, major, start_year, end_year, description) VALUES
('EDU-000001','CV-0000001','Äáº¡i há»c BÃ¡ch Khoa HÃ  Ná»™i','Ká»¹ sÆ°','CÃ´ng nghá»‡ ThÃ´ng tin',2020,2024,
 'GPA: 3.2/4.0. Há»c bá»•ng khuyáº¿n há»c nÄƒm 2022. Tham gia CLB Láº­p trÃ¬nh BKCLUB.'),
('EDU-000002','CV-0000001','THPT Nguyá»…n Gia Thiá»u, Long BiÃªn','TÃº tÃ i',NULL,2017,2020,
 'Há»c sinh Giá»i, Ä‘áº¡t giáº£i Ba mÃ´n Tin há»c cáº¥p ThÃ nh phá»‘ nÄƒm 2019.'),
('EDU-000003','CV-0000002','Äáº¡i há»c Khoa há»c Tá»± nhiÃªn TP.HCM','Cá»­ nhÃ¢n','Khoa há»c MÃ¡y tÃ­nh',2017,2021,
 'GPA: 3.5/4.0. Tá»‘t nghiá»‡p loáº¡i Giá»i. Äá» tÃ i luáº­n vÄƒn: XÃ¢y dá»±ng há»‡ thá»‘ng gá»£i Ã½ sáº£n pháº©m dá»±a trÃªn Collaborative Filtering.'),
('EDU-000004','CV-0000003','Äáº¡i há»c Kinh táº¿ Quá»‘c dÃ¢n, HÃ  Ná»™i','Cá»­ nhÃ¢n','Quáº£n trá»‹ Kinh doanh - Marketing',2020,2024,
 'GPA: 3.1/4.0. ThÃ nh viÃªn CLB Marketing MKT-NEU.'),
('EDU-000005','CV-0000004','Há»c viá»‡n CÃ´ng nghá»‡ BÆ°u chÃ­nh Viá»…n thÃ´ng','Ká»¹ sÆ°','Äiá»‡n tá»­ Viá»…n thÃ´ng',2016,2021,
 'GPA: 3.4/4.0. Äá» tÃ i tá»‘t nghiá»‡p loáº¡i Xuáº¥t sáº¯c: "NghiÃªn cá»©u vÃ  mÃ´ phá»ng há»‡ thá»‘ng anten Massive MIMO cho 5G".'),
('EDU-000006','CV-0000004','Cao Ä‘áº³ng Ká»¹ thuáº­t LÃ½ Tá»± Trá»ng, TP.HCM','Cao Ä‘áº³ng','Äiá»‡n tá»­ - Viá»…n thÃ´ng',2013,2016,
 'Tá»‘t nghiá»‡p loáº¡i Giá»i.'),
('EDU-000007','CV-0000005','Äáº¡i há»c Kinh táº¿ - Äáº¡i há»c ÄÃ  Náºµng','Cá»­ nhÃ¢n','Káº¿ toÃ¡n - Kiá»ƒm toÃ¡n',2021,2025,
 'GPA: 3.0/4.0 (Ä‘ang há»c). Dá»± kiáº¿n tá»‘t nghiá»‡p thÃ¡ng 6/2025.'),
('EDU-000008','CV-0000002','TrÆ°á»ng THPT ChuyÃªn LÃª Há»“ng Phong, TP.HCM','TÃº tÃ i',NULL,2014,2017,
 'Lá»›p chuyÃªn ToÃ¡n - Tin. Äáº¡t giáº£i NhÃ¬ mÃ´n Tin há»c cáº¥p Tá»‰nh.');

-- ------------------------------------------------------------
-- WORK EXPERIENCES (6 kinh nghiá»‡m)
-- ------------------------------------------------------------
INSERT INTO work_experiences (experience_id, cv_id, company_name, position, start_date, end_date, description) VALUES
('WE-0000001','CV-0000002','CÃ´ng ty TNHH Giáº£i phÃ¡p Pháº§n má»m Tinh VÃ¢n','Láº­p trÃ¬nh viÃªn Frontend',
 '2021-07-01','2022-12-31',
 'PhÃ¡t triá»ƒn giao diá»‡n web cho há»‡ thá»‘ng quáº£n lÃ½ ná»™i bá»™ báº±ng ReactJS vÃ  Redux. Tá»‘i Æ°u hiá»‡u nÄƒng, giáº£m thá»i gian táº£i trang trung bÃ¬nh 40%. Há»— trá»£ viáº¿t unit test vá»›i Jest.'),

('WE-0000002','CV-0000002','CÃ´ng ty Cá»• pháº§n KiotViet','Ká»¹ sÆ° Pháº§n má»m Full-Stack',
 '2023-01-01', NULL,
 'PhÃ¡t triá»ƒn tÃ­nh nÄƒng má»›i cho há»‡ thá»‘ng POS phá»¥c vá»¥ hÆ¡n 150.000 cá»­a hÃ ng. Thiáº¿t káº¿ vÃ  triá»ƒn khai RESTful API vá»›i NodeJS. Tham gia xÃ¢y dá»±ng kiáº¿n trÃºc microservices.'),

('WE-0000003','CV-0000004','CÃ´ng ty Viá»…n thÃ´ng HÃ  Ná»™i (HaNoi Telecom)','Ká»¹ sÆ° Máº¡ng',
 '2021-06-01','2023-05-31',
 'Váº­n hÃ nh vÃ  báº£o trÃ¬ há»‡ thá»‘ng máº¡ng LTE cho 80 tráº¡m BTS táº¡i HÃ  Ná»™i vÃ  cÃ¡c tá»‰nh lÃ¢n cáº­n. Xá»­ lÃ½ sá»± cá»‘, Ä‘áº£m báº£o uptime 99.9%. Tham gia dá»± Ã¡n má»Ÿ rá»™ng vÃ¹ng phá»§ sÃ³ng 4G.'),

('WE-0000004','CV-0000004','CÃ´ng ty Cá»• pháº§n Dá»‹ch vá»¥ Ká»¹ thuáº­t Viá»…n thÃ´ng (Telcom)','Ká»¹ sÆ° Há»‡ thá»‘ng Máº¡ng Cáº¥p cao',
 '2023-06-01', NULL,
 'Thiáº¿t káº¿ vÃ  triá»ƒn khai háº¡ táº§ng máº¡ng 5G táº¡i khu cÃ´ng nghiá»‡p ThÄƒng Long. NghiÃªn cá»©u giáº£i phÃ¡p Network Slicing, tá»‘i Æ°u hÃ³a bÄƒng thÃ´ng cho IoT applications.'),

('WE-0000005','CV-0000003','CÃ´ng ty TNHH Truyá»n thÃ´ng Admicro (VCCorp)','Thá»±c táº­p sinh Marketing',
 '2023-06-01','2023-08-31',
 'Quáº£n lÃ½ fanpage vá»›i 60.000 followers, lÃªn káº¿ hoáº¡ch ná»™i dung hÃ ng tuáº§n. TÄƒng tÆ°Æ¡ng tÃ¡c organic 30% trong 3 thÃ¡ng. Há»— trá»£ triá»ƒn khai vÃ  tá»‘i Æ°u chiáº¿n dá»‹ch Facebook Ads.'),

('WE-0000006','CV-0000001','CÃ´ng ty TNHH Pháº§n má»m Rikkeisoft','Thá»±c táº­p sinh Láº­p trÃ¬nh Java',
 '2023-07-01','2023-12-31',
 'Há»— trá»£ phÃ¡t triá»ƒn module quáº£n lÃ½ Ä‘Æ¡n hÃ ng cho há»‡ thá»‘ng ERP cá»§a khÃ¡ch hÃ ng Nháº­t Báº£n. Há»c vÃ  Ã¡p dá»¥ng Spring Boot, Hibernate, thiáº¿t káº¿ REST API. Tham gia code review vÃ  viáº¿t tÃ i liá»‡u ká»¹ thuáº­t.');

-- ------------------------------------------------------------
-- CERTIFICATIONS (8 chá»©ng chá»‰)
-- ------------------------------------------------------------
INSERT INTO certifications (cert_id, cv_id, name, issuer, issue_date) VALUES
('CERT-00001','CV-0000004','Cisco Certified Network Associate (CCNA)','Cisco Systems','2022-03-15'),
('CERT-00002','CV-0000004','Huawei Certified ICT Associate - 5G (HCIA-5G)','Huawei','2023-08-20'),
('CERT-00003','CV-0000002','AWS Certified Developer - Associate (DVA-C02)','Amazon Web Services','2023-05-10'),
('CERT-00004','CV-0000002','Meta Front-End Developer Professional Certificate','Meta (Coursera)','2022-11-30'),
('CERT-00005','CV-0000001','Oracle Certified Associate, Java SE 11 Programmer','Oracle','2023-09-01'),
('CERT-00006','CV-0000003','Google Analytics Individual Qualification (GAIQ)','Google','2023-07-15'),
('CERT-00007','CV-0000003','Meta Blueprint - Certified Digital Marketing Associate','Meta','2023-10-01'),
('CERT-00008','CV-0000005','Chá»©ng chá»‰ Nghiá»‡p vá»¥ Káº¿ toÃ¡n Thá»±c hÃ nh','Trung tÃ¢m ÄÃ o táº¡o Káº¿ toÃ¡n Viá»‡t Nam','2023-12-15');

-- ------------------------------------------------------------
-- APPLICATIONS (18 Ä‘Æ¡n á»©ng tuyá»ƒn)
-- ------------------------------------------------------------
INSERT INTO applications (application_id, candidate_id, recruitment_id, status, applied_date) VALUES
-- Nguyá»…n Thá»‹ Mai (CAN-000001) á»©ng tuyá»ƒn
('APP-000001','CAN-000001','REC-000001','PENDING',   '2024-02-15 10:00:00'),
('APP-000002','CAN-000001','REC-000003','APPROVED',  '2024-02-16 11:00:00'),
('APP-000003','CAN-000001','REC-000015','PENDING',   '2024-02-28 08:00:00'),
-- Tráº§n VÄƒn HÃ¹ng (CAN-000002) á»©ng tuyá»ƒn
('APP-000004','CAN-000002','REC-000001','APPROVED',  '2024-02-14 14:00:00'),
('APP-000005','CAN-000002','REC-000006','PENDING',   '2024-02-16 15:00:00'),
('APP-000006','CAN-000002','REC-000007','REJECTED',  '2024-02-18 10:00:00'),
('APP-000007','CAN-000002','REC-000010','APPROVED',  '2024-02-20 13:00:00'),
('APP-000008','CAN-000002','REC-000002','PENDING',   '2024-02-22 09:00:00'),
-- LÃª Thá»‹ Lan (CAN-000003) á»©ng tuyá»ƒn
('APP-000009','CAN-000003','REC-000004','PENDING',   '2024-02-20 08:00:00'),
('APP-000010','CAN-000003','REC-000008','APPROVED',  '2024-02-22 09:00:00'),
('APP-000011','CAN-000003','REC-000009','REJECTED',  '2024-02-23 10:00:00'),
-- HoÃ ng Minh Äá»©c (CAN-000004) á»©ng tuyá»ƒn
('APP-000012','CAN-000004','REC-000011','APPROVED',  '2024-02-15 11:00:00'),
('APP-000013','CAN-000004','REC-000012','PENDING',   '2024-02-17 14:00:00'),
('APP-000014','CAN-000004','REC-000007','REJECTED',  '2024-02-19 15:00:00'),
('APP-000015','CAN-000004','REC-000001','PENDING',   '2024-02-25 10:00:00'),
-- VÃµ Thá»‹ Thu (CAN-000005) á»©ng tuyá»ƒn
('APP-000016','CAN-000005','REC-000013','PENDING',   '2024-02-25 09:00:00'),
('APP-000017','CAN-000005','REC-000014','PENDING',   '2024-02-26 10:00:00'),
('APP-000018','CAN-000005','REC-000005','PENDING',   '2024-02-27 11:00:00');

-- ------------------------------------------------------------
-- NOTIFICATIONS (15 thÃ´ng bÃ¡o)
-- ------------------------------------------------------------
INSERT INTO notifications (notification_id, sender_id, receiver_id, content, date_time, is_read) VALUES
-- ThÃ´ng bÃ¡o duyá»‡t Ä‘Æ¡n
('NOT-000001','U-00000002','U-00000005','ÄÆ¡n á»©ng tuyá»ƒn vá»‹ trÃ­ "Thá»±c táº­p sinh PhÃ¡t triá»ƒn Pháº§n má»m" cá»§a báº¡n Ä‘Ã£ Ä‘Æ°á»£c cháº¥p thuáº­n. ChÃºng tÃ´i sáº½ liÃªn há»‡ Ä‘á»ƒ sáº¯p xáº¿p lá»‹ch phá»ng váº¥n trong 3 ngÃ y tá»›i.','2024-02-17 09:00:00',TRUE),
('NOT-000002','U-00000002','U-00000006','ÄÆ¡n á»©ng tuyá»ƒn vá»‹ trÃ­ "Láº­p trÃ¬nh viÃªn Java Backend" cá»§a báº¡n Ä‘Ã£ Ä‘Æ°á»£c cháº¥p thuáº­n. ChÃºc má»«ng! HR sáº½ gá»­i email chi tiáº¿t vá» buá»•i phá»ng váº¥n ká»¹ thuáº­t.','2024-02-15 16:00:00',TRUE),
('NOT-000003','U-00000003','U-00000006','Ráº¥t tiáº¿c, Ä‘Æ¡n á»©ng tuyá»ƒn vá»‹ trÃ­ "Ká»¹ sÆ° DevOps / Cloud Infrastructure" chÆ°a phÃ¹ há»£p vá»›i yÃªu cáº§u hiá»‡n táº¡i. ChÃºc báº¡n tÃ¬m Ä‘Æ°á»£c cÃ´ng viá»‡c phÃ¹ há»£p!','2024-02-20 10:00:00',FALSE),
('NOT-000004','U-00000003','U-00000007','ÄÆ¡n á»©ng tuyá»ƒn vá»‹ trÃ­ "UI/UX Designer" cá»§a báº¡n Ä‘Ã£ Ä‘Æ°á»£c duyá»‡t. Vui lÃ²ng chuáº©n bá»‹ portfolio vÃ  tham gia buá»•i phá»ng váº¥n vÃ o ngÃ y 28/02/2024.','2024-02-23 14:00:00',TRUE),
('NOT-000005','U-00000003','U-00000007','Ráº¥t tiáº¿c, há»“ sÆ¡ á»©ng tuyá»ƒn vá»‹ trÃ­ "ChuyÃªn viÃªn Kinh doanh B2B" cá»§a báº¡n khÃ´ng Ä‘Ã¡p á»©ng yÃªu cáº§u vá» kinh nghiá»‡m bÃ¡n hÃ ng B2B. Cáº£m Æ¡n báº¡n Ä‘Ã£ quan tÃ¢m Ä‘áº¿n VNG.','2024-02-25 08:30:00',FALSE),
('NOT-000006','U-00000004','U-00000008','ChÃºc má»«ng! ÄÆ¡n á»©ng tuyá»ƒn vá»‹ trÃ­ "Ká»¹ sÆ° Máº¡ng Viá»…n thÃ´ng (4G/5G)" cá»§a báº¡n Ä‘Ã£ Ä‘Æ°á»£c duyá»‡t. Háº¹n gáº·p báº¡n táº¡i buá»•i phá»ng váº¥n ngÃ y 01/03/2024 lÃºc 9h00.','2024-02-16 15:00:00',TRUE),
('NOT-000007','U-00000004','U-00000008','ÄÆ¡n á»©ng tuyá»ƒn vá»‹ trÃ­ "Ká»¹ sÆ° DevOps" táº¡i VNG cá»§a báº¡n chÆ°a Ä‘Æ°á»£c cháº¥p nháº­n. Kinh nghiá»‡m máº¡ng viá»…n thÃ´ng chÆ°a phÃ¹ há»£p vá»›i yÃªu cáº§u Cloud Infrastructure.','2024-02-21 09:00:00',TRUE),
-- ThÃ´ng bÃ¡o tá»« Admin
('NOT-000008','U-00000001','U-00000002','Tin tuyá»ƒn dá»¥ng "ChuyÃªn viÃªn Tuyá»ƒn dá»¥ng IT" cá»§a FPT Software Ä‘Ã£ bá»‹ Ä‘Ã³ng theo yÃªu cáº§u há»‡ thá»‘ng do háº¿t háº¡n Ä‘Äƒng tuyá»ƒn.','2024-03-16 08:00:00',TRUE),
('NOT-000009','U-00000001','U-00000003','ThÃ´ng bÃ¡o: Há»‡ thá»‘ng sáº½ báº£o trÃ¬ vÃ o ngÃ y 01/03/2024 tá»« 23:00 Ä‘áº¿n 02:00. Trong thá»i gian nÃ y, cÃ¡c chá»©c nÄƒng Ä‘Äƒng tin vÃ  xÃ©t duyá»‡t há»“ sÆ¡ táº¡m thá»i khÃ´ng kháº£ dá»¥ng.','2024-02-28 17:00:00',TRUE),
('NOT-000010','U-00000001','U-00000004','Tin tuyá»ƒn dá»¥ng "NhÃ¢n viÃªn Kinh doanh Dá»‹ch vá»¥ Viá»…n thÃ´ng" (ÄÃ  Náºµng) cá»§a Viettel Ä‘Ã£ Ä‘Æ°á»£c quáº£n trá»‹ viÃªn phÃª duyá»‡t vÃ  hiá»ƒn thá»‹ cÃ´ng khai trÃªn há»‡ thá»‘ng.','2024-02-11 08:00:00',TRUE),
-- ThÃ´ng bÃ¡o há»‡ thá»‘ng cho á»©ng viÃªn
('NOT-000011','U-00000001','U-00000005','ChÃ o má»«ng báº¡n Ä‘Ã£ táº¡o tÃ i khoáº£n thÃ nh cÃ´ng trÃªn Há»‡ thá»‘ng TÃ¬m Viá»‡c LÃ m! HÃ£y hoÃ n thiá»‡n CV Ä‘á»ƒ tÄƒng cÆ¡ há»™i Ä‘Æ°á»£c nhÃ  tuyá»ƒn dá»¥ng chÃº Ã½.','2024-01-10 10:05:00',TRUE),
('NOT-000012','U-00000001','U-00000009','ChÃ o má»«ng báº¡n Ä‘Ã£ táº¡o tÃ i khoáº£n thÃ nh cÃ´ng! Hiá»‡n cÃ³ 5 vá»‹ trÃ­ phÃ¹ há»£p vá»›i ngÃ nh Káº¿ toÃ¡n / TÃ i chÃ­nh Ä‘ang tuyá»ƒn dá»¥ng. KhÃ¡m phÃ¡ ngay!','2024-01-14 09:05:00',FALSE),
-- ThÃ´ng bÃ¡o á»©ng viÃªn má»›i cho nhÃ  tuyá»ƒn dá»¥ng
('NOT-000013','U-00000001','U-00000002','CÃ³ 3 á»©ng viÃªn má»›i á»©ng tuyá»ƒn vÃ o vá»‹ trÃ­ "Láº­p trÃ¬nh viÃªn Java Backend" trong hÃ´m nay. Vui lÃ²ng xem xÃ©t vÃ  pháº£n há»“i sá»›m.','2024-02-15 18:00:00',FALSE),
('NOT-000014','U-00000001','U-00000003','Tin tuyá»ƒn dá»¥ng "Láº­p trÃ¬nh viÃªn Frontend ReactJS" cá»§a báº¡n sáº¯p háº¿t háº¡n vÃ o ngÃ y 15/05/2024. HÃ£y gia háº¡n hoáº·c cáº­p nháº­t tin Ä‘á»ƒ tiáº¿p tá»¥c nháº­n há»“ sÆ¡.','2024-05-08 08:00:00',FALSE),
('NOT-000015','U-00000001','U-00000008','Nháº¯c nhá»Ÿ: Báº¡n cÃ³ 1 Ä‘Æ¡n á»©ng tuyá»ƒn chÆ°a nháº­n Ä‘Æ°á»£c pháº£n há»“i quÃ¡ 14 ngÃ y. Báº¡n cÃ³ thá»ƒ liÃªn há»‡ trá»±c tiáº¿p nhÃ  tuyá»ƒn dá»¥ng hoáº·c tÃ¬m kiáº¿m cÆ¡ há»™i khÃ¡c phÃ¹ há»£p hÆ¡n.','2024-03-05 09:00:00',FALSE);

-- ============================================================
-- KIá»‚M TRA Dá»® LIá»†U (tÃ¹y chá»n - cháº¡y sau khi import xong)
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

-- Kiá»ƒm tra Ä‘á»™ dÃ i ID:
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

