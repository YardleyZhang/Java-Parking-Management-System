-- ==========================================
-- 停车场管理系统 - Oracle 数据库建表脚本
-- ==========================================

-- 1. 管理员表
CREATE TABLE admin (
    admin_no VARCHAR2(20) PRIMARY KEY,
    admin_pass VARCHAR2(50) NOT NULL
);

-- 2. 车场表
CREATE TABLE area (
    area_no VARCHAR2(20) PRIMARY KEY,
    area_name VARCHAR2(100) NOT NULL,
    area_address VARCHAR2(200),
    area_numsum NUMBER(10) NOT NULL,
    area_numnow NUMBER(10) NOT NULL,
    area_inf VARCHAR2(200)
);

-- 3. 通道表
CREATE TABLE channel (
    channel_no VARCHAR2(20) PRIMARY KEY,
    channel_name VARCHAR2(100) NOT NULL,
    channel_type VARCHAR2(10) NOT NULL CHECK (channel_type IN ('入口', '出口')),
    area_no VARCHAR2(20) NOT NULL,
    channel_inf VARCHAR2(200),
    FOREIGN KEY (area_no) REFERENCES area(area_no)
);

-- 4. 车辆表
CREATE TABLE car (
    car_no VARCHAR2(20) PRIMARY KEY,
    car_tel VARCHAR2(20),
    car_name VARCHAR2(50),
    car_department VARCHAR2(100),
    cartype_id VARCHAR2(10) NOT NULL
);

-- 5. 收费规则表
CREATE TABLE rule (
    rule_no VARCHAR2(20) PRIMARY KEY,
    cartype_id VARCHAR2(10) NOT NULL,
    charge NUMBER(10, 2) NOT NULL
);

-- 6. 入场记录表
CREATE TABLE enter (
    park_id VARCHAR2(20) PRIMARY KEY,
    car_no VARCHAR2(20) NOT NULL,
    cartype_id VARCHAR2(10) NOT NULL,
    enter_time VARCHAR2(30) NOT NULL,
    channel_no VARCHAR2(20) NOT NULL,
    enter_img VARCHAR2(200),
    FOREIGN KEY (car_no) REFERENCES car(car_no),
    FOREIGN KEY (channel_no) REFERENCES channel(channel_no)
);

-- 7. 出场记录表
CREATE TABLE out_record (
    park_id VARCHAR2(20) PRIMARY KEY,
    out_time VARCHAR2(30) NOT NULL,
    channel_no VARCHAR2(20) NOT NULL,
    out_img VARCHAR2(200),
    total_fee NUMBER(10, 2) NOT NULL,
    FOREIGN KEY (park_id) REFERENCES enter(park_id),
    FOREIGN KEY (channel_no) REFERENCES channel(channel_no)
);

-- ==========================================
-- 初始化测试数据
-- ==========================================

-- 插入管理员
INSERT INTO admin VALUES ('admin', '123456');

-- 插入车场
INSERT INTO area VALUES ('A001', '主校区停车场', 'XX大学主校区南门', 200, 0, '地下停车场');

-- 插入通道
INSERT INTO channel VALUES ('C001', '入口1', '入口', 'A001', '主入口');
INSERT INTO channel VALUES ('C002', '出口1', '出口', 'A001', '主出口');

-- 插入收费规则
INSERT INTO rule VALUES ('R001', 'T01', 5.00);
INSERT INTO rule VALUES ('R002', 'T02', 8.00);
INSERT INTO rule VALUES ('R003', 'T03', 10.00);

-- 提交
COMMIT;
