package com.parking.dao;

import com.parking.util.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ParkingDAO {

    /**
     * 核心技能 1：车辆入场登记 (对应需求书 6)
     * 闭环逻辑：插入入场记录 -> 联动修改该车场当前在库车辆数 +1 -> 事务提交
     */
    public boolean vehicleEnter(String parkId, String carNo, String cartypeId, String enterTime, String channelNo, String enterImg) {
        Connection conn = null;
        PreparedStatement psEnter = null;
        PreparedStatement psArea = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务，保证数据强一致性闭环

            // 1. 录入入场表
            String sqlEnter = "INSERT INTO enter VALUES (?, ?, ?, ?, ?, ?)";
            psEnter = conn.prepareStatement(sqlEnter);
            psEnter.setString(1, parkId);
            psEnter.setString(2, carNo);
            psEnter.setString(3, cartypeId);
            psEnter.setString(4, enterTime);
            psEnter.setString(5, channelNo);
            psEnter.setString(6, enterImg);
            psEnter.executeUpdate();

            // 2. 自动让该通道所属的车场“当前车辆数”加 1
            String sqlArea = "UPDATE area SET area_numnow = area_numnow + 1 WHERE area_no = (SELECT area_no FROM channel WHERE channel_no = ?)";
            psArea = conn.prepareStatement(sqlArea);
            psArea.setString(1, channelNo);
            psArea.executeUpdate();

            conn.commit(); // 双表协同成功，原子提交
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, psEnter, null);
            DBUtil.close(null, psArea, conn);
        }
    }

    /**
     * 核心技能 2：车辆出场结算并自动计费 (对应需求书 7, 10)
     * 闭环逻辑：查入场时间与费率 -> 换算时间差并向上取整 -> 录入出场历史 -> 该车场当前车位数 -1 -> 返回总金额
     */
    public double vehicleExitAndCalculateFee(String parkId, String outTime, String channelNo, String outImg) {
        Connection conn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psOut = null;
        PreparedStatement psArea = null;
        ResultSet rs = null;
        double totalFee = -1;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            // 1. 跨表联合查询该车辆的入场时间及该车型的每小时收费标准
            String sqlSelect = "SELECT e.enter_time, r.charge FROM enter e JOIN rule r ON e.cartype_id = r.cartype_id WHERE e.park_id = ?";
            psSelect = conn.prepareStatement(sqlSelect);
            psSelect.setString(1, parkId);
            rs = psSelect.executeQuery();

            if (rs.next()) {
                String enterTimeStr = rs.getString("enter_time");
                double chargePerHour = rs.getDouble("charge");

                // 2. 复杂的字符串时间解析与运算
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime enterTime = LocalDateTime.parse(enterTimeStr, formatter);
                LocalDateTime exitTime = LocalDateTime.parse(outTime, formatter);

                // 计算相差的纯分钟数
                long minutes = ChronoUnit.MINUTES.between(enterTime, exitTime);
                long hours = minutes / 60;
                if (minutes % 60 > 0) {
                    hours++; // 不足 1 小时按 1 小时算（向上取整逻辑）
                }
                if (hours < 0) hours = 0;

                totalFee = hours * chargePerHour; // 动态计算出最终总费用

                // 3. 写入出场纪录表
                String sqlOut = "INSERT INTO out_record VALUES (?, ?, ?, ?, ?)";
                psOut = conn.prepareStatement(sqlOut);
                psOut.setString(1, parkId);
                psOut.setString(2, outTime);
                psOut.setString(3, channelNo);
                psOut.setString(4, outImg);
                psOut.setDouble(5, totalFee);
                psOut.executeUpdate();

                // 4. 联动让该车场的“当前车辆数”减 1
                String sqlArea = "UPDATE area SET area_numnow = area_numnow - 1 WHERE area_no = (SELECT area_no FROM channel WHERE channel_no = ?)";
                psArea = conn.prepareStatement(sqlArea);
                psArea.setString(1, channelNo);
                psArea.executeUpdate();

                conn.commit(); // 闭环完成，全盘提交
            }
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            totalFee = -1;
        } finally {
            DBUtil.close(rs, psSelect, null);
            DBUtil.close(null, psOut, null);
            DBUtil.close(null, psArea, conn);
        }
        return totalFee;
    }

    /**
     * 核心技能 3：在库车辆动态动态查询 (对应需求书 9)
     * 逻辑：只要在 enter 表里有记录，但在 out_record 表里还没结账的，就是当前“在库车辆”
     */
    public List<Object[]> getInLotVehicles() {
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT e.park_id, e.car_no, t.cartype_name, e.enter_time, c.channel_name " +
                         "FROM enter e " +
                         "JOIN cartype t ON e.cartype_id = t.cartype_id " +
                         "JOIN channel c ON e.channel_no = c.channel_no " +
                         "WHERE e.park_id NOT IN (SELECT park_id FROM out_record) ORDER BY e.enter_time DESC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("park_id"), rs.getString("car_no"),
                    rs.getString("cartype_name"), rs.getString("enter_time"),
                    rs.getString("channel_name")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 核心技能 4：各个车场总收益实时汇总 (对应需求书 8)
     */
    public List<Object[]> getAreaRevenues() {
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            // 左外连接聚合统计各个车场的实收停车费总和
            String sql = "SELECT a.area_no, a.area_name, NVL(SUM(o.reality_charge), 0) AS total_money " +
                         "FROM area a " +
                         "LEFT JOIN channel c ON a.area_no = c.area_no " +
                         "LEFT JOIN out_record o ON c.channel_no = o.channel_no " +
                         "GROUP BY a.area_no, a.area_name ORDER BY a.area_no";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("area_no"), rs.getString("area_name"), rs.getDouble("total_money")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }
}