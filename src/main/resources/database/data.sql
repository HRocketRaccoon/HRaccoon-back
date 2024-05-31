-- code 테이블 초기 데이터. 데이터 정책에 따름.
INSERT INTO code (code_no, code_name) VALUES
-- 직책(position)
('PS001', '팀장'),
('PS002', '부서장'),
('PS000', '직책없음'),
-- 직위(rank)
('RK001', '사원'),
('RK002', '대리'),
('RK003', '과장'),
('RK004', '차장'),
('RK005', '부장'),
('RK006', '이사'),
('RK007', '사장'),
-- 부서(department)
('DP001', 'IT 사업부'),
('DP002', 'IOT사업부'),
('DP003', '경영기획부'),
('DP004', '인사총무지원부'),
-- 팀(team)
('TM001', 'IT영업팀'),
('TM002', '해외영업팀'),
('TM003', 'IT개발팀'),
('TM004', '개발지원팀'),
('TM005', 'IOT영업팀'),
('TM006', 'IOTPass영업팀'),
('TM007', 'IOT개발팀'),
('TM008', '경영지원팀'),
('TM009', '외주관리팀'),
('TM010', '인사지원팀'),
('TM011', '총무팀'),
-- 역량
('AB001','Python'),
('AB002','Java'),
('AB003','C'),
('AB004','C++'),
('AB005','C#'),
('AB006','JavaScript'),
('AB007','TypeScript'),
('AB008','Ruby'),
('AB009','PHP'),
('AB010','Swift'),
('AB011','Kotlin'),
('AB012','R'),
('AB013','Go'),
('AB014','Rust'),
('AB015','Scala'),
('AB016','Perl'),
('AB017','Haskell'),
('AB018','Objective-C'),
('AB019','MATLAB'),
('AB020','SQL');

-- user table
INSERT INTO user_detail (user_detail_no, user_join_date, user_leaving_date, user_leaving_reason, user_remain_vacation) VALUES
(1, '2020-01-01 00:00:00', NULL, NULL, 15),
(2, '2020-01-01 00:00:00', NULL, NULL, 15),
(3, '2020-01-01 00:00:00', NULL, NULL, 15),
(4, '2020-01-01 00:00:00', NULL, NULL, 15),
(5, '2020-01-01 00:00:00', NULL, NULL, 15),
(6, '2020-01-01 00:00:00', NULL, NULL, 15),
(7, '2020-01-01 00:00:00', NULL, NULL, 15),
(8, '2020-01-01 00:00:00', NULL, NULL, 15),
(9, '2020-01-01 00:00:00', NULL, NULL, 15),
(10, '2020-01-01 00:00:00', NULL, NULL, 15);

INSERT INTO users (user_id, user_password, user_name, user_mobile, user_address, user_gender, user_birth, user_email, user_department, user_position, user_team, user_rank, user_role, user_detail_no) VALUES
('user01', 'password01', '김철수', '010-1234-5678', '서울특별시 강남구', 'MALE', '1990-01-01', 'user01@example.com', 'DP001', 'PS001', 'TM001', 'RK001', 'USER', 1),
('user02', 'password02', '이영희', '010-2345-6789', '서울특별시 서초구', 'FEMALE', '1992-02-02', 'user02@example.com', 'DP002', 'PS001', 'TM001', 'RK002', 'USER', 2),
('user03', 'password03', '박민수', '010-3456-7890', '서울특별시 종로구', 'MALE', '1988-03-03', 'user03@example.com', 'DP002', 'PS001', 'TM001', 'RK003', 'USER', 3),
('user04', 'password04', '최지우', '010-4567-8901', '서울특별시 용산구', 'FEMALE', '1991-04-04', 'user04@example.com', 'DP004', 'PS002', 'TM010', 'RK001', 'USER', 4),
('user05', 'password05', '한지민', '010-5678-9012', '서울특별시 성북구', 'FEMALE', '1993-05-05', 'user05@example.com', 'DP001', 'PS000', 'TM001', 'RK002', 'USER', 5),
('user06', 'password06', '정우성', '010-6789-0123', '서울특별시 강동구', 'MALE', '1985-06-06', 'user06@example.com', 'DP003', 'PS003', 'TM002', 'RK004', 'USER', 6),
('user07', 'password07', '김하늘', '010-7890-1234', '서울특별시 동대문구', 'FEMALE', '1994-07-07', 'user07@example.com', 'DP004', 'PS000', 'TM010', 'RK001', 'USER', 7),
('user08', 'password08', '이동욱', '010-8901-2345', '서울특별시 영등포구', 'MALE', '1987-08-08', 'user08@example.com', 'DP001', 'PS000', 'TM003', 'RK004', 'USER', 8),
('user09', 'password09', '고아라', '010-9012-3456', '서울특별시 서대문구', 'FEMALE', '1995-09-09', 'user09@example.com', 'DP001', 'PS000', 'TM001', 'RK003', 'USER', 9),
('user10', 'password10', '조인성', '010-0123-4567', '서울특별시 관악구', 'MALE', '1990-10-10', 'user10@example.com', 'DP001', 'PS000', 'TM003', 'RK001', 'USER', 10);

INSERT INTO ability (ability_name, user_no) VALUES
('ABP001', 2),
('ABP002', 2),
('ABP003', 2),
('ABP001', 4),
('ABP001', 5),
('ABP001', 6),
('ABP002', 7),
('ABP002', 8),
('ABP002', 9),
('ABP004', 8),
('ABP004', 9),
('ABP007', 10);

-- approval table
-- Insert into ApprovalDetail table
INSERT INTO approval_detail (approval_detail_no, approval_detail_content, approval_detail_start_date, approval_detail_end_date, approval_detail_response_date, approval_detail_response_content)
VALUES
    (1, '상세 내용 1', '2024-05-01 09:00:00', '2024-05-10 18:00:00', NULL, NULL),
    (2, '상세 내용 2', '2024-06-01 09:00:00', '2024-06-15 18:00:00', NULL, NULL),
    (3, '상세 내용 3', '2024-07-01 09:00:00', '2024-07-20 18:00:00', NULL, NULL);

-- Insert into Approval table
INSERT INTO approval (approval_no, approval_type, approval_submit_date, approval_status, approval_authority, user_no, approval_detail_no)
VALUES
    (1, '휴가', '2024-04-20 10:00:00', '승인', '팀장', 1, 1),
    (2, '출장', '2024-05-15 14:00:00', '승인', '부서장', 2, 2),
    (3, '사내 교육', '2024-06-20 09:00:00', '반려', '팀장', 3, 3);

INSERT INTO Seat (seat_location, seat_office) VALUES
('JSL001', 'OJS01'),
('JSL002', 'OJS01'),
('JSL003', 'OJS01'),
('JSL004', 'OJS01'),
('JSL005', 'OJS01'),
('JSL006', 'OJS01'),
('JSL007', 'OJS01'),
('JS1001', 'OJS01'),
('JS1002', 'OJS01'),
('JS1003', 'OJS01'),
('JS1004', 'OJS01'),
('JS2001', 'OJS01'),
('JS2002', 'OJS01'),
('JS2003', 'OJS01'),
('JS3004', 'OJS01'),
('JS3005', 'OJS01'),
('JS3006', 'OJS01'),
('JS3007', 'OJS01'),
('JS3008', 'OJS01'),
('JS3009', 'OJS01'),


-- 마포 오피스 좌석 데이터
('MPL001', 'OMP02'),
('MPL002', 'OMP02'),
('MPL003', 'OMP02'),
('MP1001', 'OMP02'),
('MP1002', 'OMP02'),
('MP2001', 'OMP02'),
('MP2002', 'OMP02'),
('MP2003', 'OMP02'),
('MP3001', 'OMP02'),
('MP3002', 'OMP02'),

-- 성북 오피스 좌석 데이터
('SBL001', 'OSB03'),
('SB1001', 'OSB03'),
('SB2001', 'OSB03');

INSERT INTO seat_status (seat_status_yn, seat_no) VALUES
(false, 1),
(false, 2),
(false, 3),
(false, 4),
(false, 5),
(false, 6),
(false, 7),
(false, 8),
(false, 9),
(false, 10),
(false, 11),
(false, 12),
(false, 13),
(false, 14),
(false, 15),
(false, 16),
(false, 17),
(false, 18),
(false, 19),
(false, 20),
(false, 21),
(false, 22),
(false, 23),
(false, 24),
(false, 25),
(false, 26),
(false, 27),
(false, 28),
(false, 29),
(false, 30),
(false, 31),
(false, 32),
(false, 33);
