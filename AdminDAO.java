package com.parking.dao;

import com.parking.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {
    
    /**
     * 验证管理员登录
     * 遵循闭环逻辑：传入账号密码 -> 查询数据库 -> 返回 true/false -> 释放资源
     */
    public boolean login(String adminNo, String pass) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isSuccess = false;
        
        try {
            // 1. 拿到数据库连接
            conn = DBUtil.getConnection();
            
            // 2. 编写 SQL 语句（使用 ? 占位符，这是防范 SQL 注入的第一性原则）
            String sql = "SELECT * FROM admin WHERE admin_no = ? AND admin_pass = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, adminNo);
            ps.setString(2, pass);
            
            // 3. 执行查询
            rs = ps.executeQuery();
            if (rs.next()) {
                isSuccess = true; // 如果查到数据，说明账号密码完全匹配
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4. 无论成功失败，严格执行复盘清理，关闭资源
            DBUtil.close(rs, ps, conn);
        }
        return isSuccess;
    }
}