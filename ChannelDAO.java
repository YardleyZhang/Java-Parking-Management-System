package com.parking.dao;

import com.parking.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChannelDAO {
    
    // 技能 1：查询所有通道 (含所属车场编号)
    public List<Object[]> getAllChannels() {
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM channel ORDER BY channel_no";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("channel_no"), rs.getString("channel_name"),
                    rs.getString("channel_type"), rs.getString("area_no"),
                    rs.getString("channel_inf")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    // 技能 2：添加通道
    public boolean addChannel(String no, String name, String type, String areaNo, String inf) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO channel VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no); ps.setString(2, name); 
            ps.setString(3, type); ps.setString(4, areaNo); 
            ps.setString(5, inf);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    // 技能 3：修改通道信息
    public boolean updateChannel(String no, String name, String type, String areaNo, String inf) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE channel SET channel_name=?, channel_type=?, area_no=?, channel_inf=? WHERE channel_no=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name); ps.setString(2, type); 
            ps.setString(3, areaNo); ps.setString(4, inf); 
            ps.setString(5, no);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    // 技能 4：删除通道
    public boolean deleteChannel(String no) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM channel WHERE channel_no=?";
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