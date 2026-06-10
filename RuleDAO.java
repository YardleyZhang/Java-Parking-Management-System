package com.parking.dao;

import com.parking.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RuleDAO {
    
    // 技能 1：查询所有收费规则
    public List<Object[]> getAllRules() {
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM rule ORDER BY rule_no";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("rule_no"), 
                    rs.getString("cartype_id"),
                    rs.getDouble("charge")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn); // 闭环释放资源
        }
        return list;
    }

    // 技能 2：添加规则
    public boolean addRule(String no, String cartypeId, double charge) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO rule VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no); 
            ps.setString(2, cartypeId); 
            ps.setDouble(3, charge);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    // 技能 3：修改规则金额 (根据规则编号和车型ID)
    public boolean updateRule(String no, String cartypeId, double charge) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE rule SET charge=? WHERE rule_no=? AND cartype_id=?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, charge); 
            ps.setString(2, no); 
            ps.setString(3, cartypeId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    // 技能 4：删除规则
    public boolean deleteRule(String no, String cartypeId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM rule WHERE rule_no=? AND cartype_id=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no);
            ps.setString(2, cartypeId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }
}