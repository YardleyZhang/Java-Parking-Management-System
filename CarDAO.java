package com.parking.dao;

import com.parking.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    
    // 技能 1：查询所有车辆信息
    public List<Object[]> getAllCars() {
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM car ORDER BY car_no";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("car_no"), rs.getString("car_tel"),
                    rs.getString("car_name"), rs.getString("car_department"),
                    rs.getString("cartype_id")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn); // 闭环释放资源
        }
        return list;
    }

    // 技能 2：添加车辆
    public boolean addCar(String no, String tel, String name, String dept, String typeId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO car VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no); ps.setString(2, tel); 
            ps.setString(3, name); ps.setString(4, dept); 
            ps.setString(5, typeId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    // 技能 3：修改车辆信息
    public boolean updateCar(String no, String tel, String name, String dept, String typeId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE car SET car_tel=?, car_name=?, car_department=?, cartype_id=? WHERE car_no=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tel); ps.setString(2, name); 
            ps.setString(3, dept); ps.setString(4, typeId); 
            ps.setString(5, no);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    // 技能 4：删除车辆
    public boolean deleteCar(String no) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM car WHERE car_no=?";
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