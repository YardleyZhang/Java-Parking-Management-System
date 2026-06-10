# 停车场管理系统

一个基于 Java Swing 和 Oracle 数据库的桌面应用程序，用于高校课程设计学习。

## 技术栈

- **前端**: Java Swing (GUI)
- **后端**: Java JDBC
- **数据库**: Oracle 11g+
- **开发工具**: Eclipse / IntelliJ IDEA

## 功能模块

| 模块 | 说明 |
|------|------|
| 管理员登录 | 安全验证管理员账号密码 |
| 车场管理 | 添加、修改、删除车场信息 |
| 通道管理 | 入口/出口通道配置 |
| 车辆管理 | 车辆信息登记与管理 |
| 收费规则 | 不同车型差异化定价 |
| 车辆入场 | 自动记录入场时间，联动更新车位数 |
| 车辆出场 | 自动计算停车费用 |

## 项目结构

```
ParkingSystem/
├── src/
│   └── com/parking/
│       ├── dao/        # 数据访问层
│       ├── ui/         # 界面层
│       └── util/       # 工具类
├── lib/                # Oracle JDBC 驱动
└── sql/                # 数据库脚本 (需创建)
```

## 环境配置

### 1. 数据库配置

修改 `src/com/parking/util/DBUtil.java` 中的数据库连接信息：

```java
private static final String URL = "jdbc:oracle:thin:@localhost:1521/Parking";
private static final String USER = "your_username";
private static final String PWD = "your_password";
```

### 2. 数据库建表

需要创建以下表：admin, area, channel, car, rule, enter

## 运行项目

1. 导入项目到 IDE
2. 配置 Oracle JDBC 驱动 (ojdbc8.jar)
3. 运行 `LoginUI.java` 中的 main 方法

## 依赖

- Oracle JDBC Driver (ojdbc8.jar)

## 学习要点

- DAO 模式 (Data Access Object)
- JDBC 数据库操作
- SQL 注入防范 (PreparedStatement)
- 事务管理与提交回滚
- Swing GUI 界面开发
- MVC 架构思想
