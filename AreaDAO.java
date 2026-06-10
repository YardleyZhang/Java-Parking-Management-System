package com.parking.dao;

import com.parking.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO {
    
    // 技能 1：查询所有车场
    public List<Object[]> getAllAreas() {
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM area ORDER BY area_no";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("area_no"), rs.getString("area_name"),
                    rs.getString("area_address"), rs.getInt("area_numsum"),
                    rs.getInt("area_numnow"), rs.getString("area_inf")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    // 技能 2：添加新车场
    public boolean addArea(String no, String name, String address, int sum, int now, String inf) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO area VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no); ps.setString(2, name); ps.setString(3, address);
            ps.setInt(4, sum); ps.setInt(5, now); ps.setString(6, inf);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    // 技能 3：修改车场信息 (根据编号更新其他字段)
    public boolean updateArea(String no, String name, String address, int sum, int now, String inf) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE area SET area_name=?, area_address=?, area_numsum=?, area_numnow=?, area_inf=? WHERE area_no=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name); ps.setString(2, address);
            ps.setInt(3, sum); ps.setInt(4, now); ps.setString(5, inf);
            ps.setString(6, no); // WHERE 条件放最后
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    // 技能 4：删除车场 (底层触发器会自动级联删除关联的通道，形成闭环)
    public boolean deleteArea(String no) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM area WHERE area_no=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }
}