package com.parking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    // 数据库连接的三要素：URL、用户名、密码
    // 注意：这里用 /Parking 代表连接到我们刚刚建好的服务名
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/Parking";
    private static final String USER = "system"; // 你数据库的超级管理员账号
    private static final String PWD = "";    // 数据库密码

    // 静态代码块：类加载时自动注册驱动，符合单次执行的第一性原理
    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("致命错误：找不到 Oracle JDBC 驱动！请检查 ojdbc8.jar 是否 Add to Build Path");
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }

    /**
     * 释放资源，形成内存管理的完美闭环
     */
    public static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // 单元测试与复盘：写完基建，立刻原地测试是否畅通
    // ==========================================
    public static void main(String[] args) {
        try {
            Connection conn = DBUtil.getConnection();
            if (conn != null) {
                System.out.println("太棒了！Java 已经成功连上了 Oracle 数据库 (Parking)！");
                conn.close(); // 每次测试完记得顺手关门
            }
        } catch (SQLException e) {
            System.err.println("连接失败，请检查数据库服务是否开启，或账号密码是否正确！");
            e.printStackTrace();
        }
    }
}