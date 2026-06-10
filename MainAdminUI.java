package com.parking.ui;

import com.parking.dao.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainAdminUI extends JFrame {
    
    private DefaultTableModel areaTableModel, channelTableModel, carTableModel, ruleTableModel, inLotTableModel, revenueTableModel;
    private JTable areaTable, channelTable, carTable, ruleTable, inLotTable, revenueTable;

    public MainAdminUI() {
        setTitle("停车场管理系统 - 管理员终极控制台");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. 车场面板
        JPanel panelArea = new JPanel(new BorderLayout());
        areaTableModel = new DefaultTableModel(new String[]{"车场编号", "车场名称", "地址", "总车位", "当前车位", "备注信息"}, 0);
        areaTable = new JTable(areaTableModel);
        panelArea.add(new JScrollPane(areaTable), BorderLayout.CENTER);
        JPanel panelAreaBtn = new JPanel();
        JButton btnAreaRef = new JButton("刷新"); JButton btnAreaAdd = new JButton("添加"); JButton btnAreaUpd = new JButton("修改"); JButton btnAreaDel = new JButton("删除");
        panelAreaBtn.add(btnAreaRef); panelAreaBtn.add(btnAreaAdd); panelAreaBtn.add(btnAreaUpd); panelAreaBtn.add(btnAreaDel);
        panelArea.add(panelAreaBtn, BorderLayout.SOUTH);
        btnAreaRef.addActionListener(e -> loadAreaTable());
        btnAreaAdd.addActionListener(e -> {
            JTextField tNo = new JTextField(10), tName = new JTextField(10), tAddr = new JTextField(15), tSum = new JTextField(5), tNow = new JTextField(5), tInf = new JTextField(10);
            JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
            p.add(new JLabel("车场编号:")); p.add(tNo); p.add(new JLabel("车场名称:")); p.add(tName); p.add(new JLabel("地      址:")); p.add(tAddr); p.add(new JLabel("总 车 位:")); p.add(tSum); p.add(new JLabel("当前车位:")); p.add(tNow); p.add(new JLabel("备注信息:")); p.add(tInf);
            if (JOptionPane.showConfirmDialog(this, p, "添加车场", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    int s = Integer.parseInt(tSum.getText()), n = Integer.parseInt(tNow.getText());
                    if (new AreaDAO().addArea(tNo.getText(), tName.getText(), tAddr.getText(), s, n, tInf.getText())) { loadAreaTable(); }
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "格式错误！"); }
            }
        });
        btnAreaDel.addActionListener(e -> {
            int r = areaTable.getSelectedRow(); if (r != -1) { if (new AreaDAO().deleteArea(areaTableModel.getValueAt(r, 0).toString())) loadAreaTable(); }
        });

        // 2. 通道面板
        JPanel panelChannel = new JPanel(new BorderLayout());
        channelTableModel = new DefaultTableModel(new String[]{"通道编号", "通道名称", "类型", "所属车场", "备注"}, 0);
        channelTable = new JTable(channelTableModel);
        panelChannel.add(new JScrollPane(channelTable), BorderLayout.CENTER);
        JPanel panelChanBtn = new JPanel();
        JButton btnChanRef = new JButton("刷新"); JButton btnChanAdd = new JButton("添加");
        panelChanBtn.add(btnChanRef); panelChanBtn.add(btnChanAdd);
        panelChannel.add(panelChanBtn, BorderLayout.SOUTH);
        btnChanRef.addActionListener(e -> loadChannelTable());
        btnChanAdd.addActionListener(e -> {
            JTextField tNo = new JTextField(10), tName = new JTextField(10), tArea = new JTextField(10), tInf = new JTextField(10); JComboBox<String> cbType = new JComboBox<>(new String[]{"入口", "出口"});
            JPanel p = new JPanel(new GridLayout(5, 2, 5, 5));
            p.add(new JLabel("通道编号:")); p.add(tNo); p.add(new JLabel("通道名称:")); p.add(tName); p.add(new JLabel("通道类型:")); p.add(cbType); p.add(new JLabel("所属车场:")); p.add(tArea); p.add(new JLabel("备注信息:")); p.add(tInf);
            if (JOptionPane.showConfirmDialog(this, p, "添加通道", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (new ChannelDAO().addChannel(tNo.getText(), tName.getText(), cbType.getSelectedItem().toString(), tArea.getText(), tInf.getText())) loadChannelTable();
            }
        });

        // 3. 车辆面板
        JPanel panelCar = new JPanel(new BorderLayout());
        carTableModel = new DefaultTableModel(new String[]{"车牌号", "手机号码", "车主名称", "部门", "车辆类型标识"}, 0);
        carTable = new JTable(carTableModel);
        panelCar.add(new JScrollPane(carTable), BorderLayout.CENTER);
        JPanel panelCarBtn = new JPanel();
        JButton btnCarRef = new JButton("刷新"); JButton btnCarAdd = new JButton("添加");
        panelCarBtn.add(btnCarRef); panelCarBtn.add(btnCarAdd);
        panelCar.add(panelCarBtn, BorderLayout.SOUTH);
        btnCarRef.addActionListener(e -> loadCarTable());
        btnCarAdd.addActionListener(e -> {
            JTextField tNo = new JTextField(10), tTel = new JTextField(10), tName = new JTextField(10), tDept = new JTextField(10); JComboBox<String> cbType = new JComboBox<>(new String[]{"T01", "T02", "T03"});
            JPanel p = new JPanel(new GridLayout(5, 2, 5, 5));
            p.add(new JLabel("车牌号:")); p.add(tNo); p.add(new JLabel("手机号码:")); p.add(tTel); p.add(new JLabel("车主名称:")); p.add(tName); p.add(new JLabel("所属部门:")); p.add(tDept); p.add(new JLabel("车辆类型:")); p.add(cbType);
            if (JOptionPane.showConfirmDialog(this, p, "登记新车辆", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (new CarDAO().addCar(tNo.getText(), tTel.getText(), tName.getText(), tDept.getText(), cbType.getSelectedItem().toString())) loadCarTable();
            }
        });

        // 4. 收费规则面板
        JPanel panelRule = new JPanel(new BorderLayout());
        ruleTableModel = new DefaultTableModel(new String[]{"规则编号", "车辆类型标识", "每小时收费(元)"}, 0);
        ruleTable = new JTable(ruleTableModel);
        panelRule.add(new JScrollPane(ruleTable), BorderLayout.CENTER);
        JPanel panelRuleBtn = new JPanel();
        JButton btnRuleRef = new JButton("刷新"); JButton btnRuleAdd = new JButton("添加");
        panelRuleBtn.add(btnRuleRef); panelRuleBtn.add(btnRuleAdd);
        panelRule.add(panelRuleBtn, BorderLayout.SOUTH);
        btnRuleRef.addActionListener(e -> loadRuleTable());
        btnRuleAdd.addActionListener(e -> {
            JTextField tNo = new JTextField(10), tCharge = new JTextField(10); JComboBox<String> cbType = new JComboBox<>(new String[]{"T01", "T02", "T03"});
            JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
            p.add(new JLabel("规则编号:")); p.add(tNo); p.add(new JLabel("车辆类型:")); p.add(cbType); p.add(new JLabel("收费(元/时):")); p.add(tCharge);
            if (JOptionPane.showConfirmDialog(this, p, "添加规则", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try { if (new RuleDAO().addRule(tNo.getText(), cbType.getSelectedItem().toString(), Double.parseDouble(tCharge.getText()))) loadRuleTable(); } catch(Exception ex){}
            }
        });

        // =======================================================
        // 5. 终极卡片：智能停车与收益结算中心 (双排架构布局)
        // =======================================================
        JPanel panelParkingMain = new JPanel(new GridLayout(2, 1, 10, 10)); // 上半部门管车辆，下半部门看钱

        // 上半部：在库车辆及出入场动作
        JPanel panelInLot = new JPanel(new BorderLayout());
        panelInLot.setBorder(BorderFactory.createTitledBorder("在库车辆管理及动态计费中心 (对应指标:6, 7, 9, 10)"));
        inLotTableModel = new DefaultTableModel(new String[]{"停车编号", "在库车牌号", "车型", "入场时间", "入场通道"}, 0);
        inLotTable = new JTable(inLotTableModel);
        panelInLot.add(new JScrollPane(inLotTable), BorderLayout.CENTER);
        
        JPanel panelInLotBtns = new JPanel();
        JButton btnInLotRef = new JButton("刷新在库列表");
        JButton btnEnterRegister = new JButton("模拟车辆 ➔【入场登记】");
        JButton btnExitSettle = new JButton("选中车辆 ➔【出场结算】");
        panelInLotBtns.add(btnInLotRef); panelInLotBtns.add(btnEnterRegister); panelInLotBtns.add(btnExitSettle);
        panelInLot.add(panelInLotBtns, BorderLayout.SOUTH);

        // 下半部：车场收益动态汇总
        JPanel panelRevenue = new JPanel(new BorderLayout());
        panelRevenue.setBorder(BorderFactory.createTitledBorder("各个停车场资金收益汇总表 (对应指标:8)"));
        revenueTableModel = new DefaultTableModel(new String[]{"车场编号", "车场名称", "当前累计运营收益 (元)"}, 0);
        revenueTable = new JTable(revenueTableModel);
        panelRevenue.add(new JScrollPane(revenueTable), BorderLayout.CENTER);
        JPanel panelRevBtns = new JPanel();
        JButton btnRevRef = new JButton("实时刷新收益看板");
        panelRevBtns.add(btnRevRef);
        panelRevenue.add(panelRevBtns, BorderLayout.SOUTH);

        panelParkingMain.add(panelInLot);
        panelParkingMain.add(panelRevenue);

        // =======================================================
        // 核心计费中心的事件闭环绑定
        // =======================================================
        btnInLotRef.addActionListener(e -> loadParkingCenterData());
        btnRevRef.addActionListener(e -> loadParkingCenterData());

        // 车辆入场交互实现
        btnEnterRegister.addActionListener(e -> {
            JTextField tPid = new JTextField("P" + System.currentTimeMillis() % 100000, 10); // 随机生成停车单号
            JTextField tCno = new JTextField("沪", 10);
            JComboBox<String> cbType = new JComboBox<>(new String[]{"T01", "T02", "T03"});
            // 自动采集当前精确的系统秒级时间进行防呆填充
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JTextField tTime = new JTextField(currentTime, 15);
            JTextField tChan = new JTextField("C01", 10);
            JTextField tImg = new JTextField("enter_snap.jpg", 10);

            JPanel p = new JPanel(new GridLayout(6, 2, 5, 5));
            p.add(new JLabel("生成停车编号:")); p.add(tPid); p.add(new JLabel("扫描车牌号码:")); p.add(tCno);
            p.add(new JLabel("识别车辆类型:")); p.add(cbType); p.add(new JLabel("入场系统时间:")); p.add(tTime);
            p.add(new JLabel("驶入通道编号:")); p.add(tChan); p.add(new JLabel("摄像抓拍存档:")); p.add(tImg);

            if (JOptionPane.showConfirmDialog(this, p, "道闸控制：车辆驶入登记", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (new ParkingDAO().vehicleEnter(tPid.getText(), tCno.getText(), cbType.getSelectedItem().toString(), tTime.getText(), tChan.getText(), tImg.getText())) {
                    JOptionPane.showMessageDialog(this, "道闸开启！入场登记成功，车位已自动更新。");
                    loadParkingCenterData();
                    loadAreaTable(); // 车位变化了，车场卡片一并联动刷新
                } else { JOptionPane.showMessageDialog(this, "入场失败！请确认通道编号存在。"); }
            }
        });

        // 车辆出场联动计费核心交互
        btnExitSettle.addActionListener(e -> {
            int r = inLotTable.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "请先在上方表格中用鼠标选中一辆正在出场的车辆！");
                return;
            }
            String pId = inLotTableModel.getValueAt(r, 0).toString();
            String cNo = inLotTableModel.getValueAt(r, 1).toString();

            String outTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JTextField tTime = new JTextField(outTime, 15);
            JTextField tChan = new JTextField("C02", 10);
            JTextField tImg = new JTextField("exit_snap.jpg", 10);

            JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
            p.add(new JLabel("出场当前时间:")); p.add(tTime);
            p.add(new JLabel("驶出通道编号:")); p.add(tChan);
            p.add(new JLabel("出口抓拍存档:")); p.add(tImg);

            if (JOptionPane.showConfirmDialog(this, p, "抬杆结算：车辆 [" + cNo + "] 准备驶离", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                double totalFee = new ParkingDAO().vehicleExitAndCalculateFee(pId, tTime.getText(), tChan.getText(), tImg.getText());
                if (totalFee >= 0) {
                    String finalMsg = String.format("【自动计费系统结算单】\n车牌号码: %s\n结算状态: 成功\n自动应收总金额: %.2f 元\n\n提示：资金已入库，起落杆已抬起，请安全离场！", cNo, totalFee);
                    JOptionPane.showMessageDialog(this, finalMsg, "智能出场收费单", JOptionPane.INFORMATION_MESSAGE);
                    loadParkingCenterData();
                    loadAreaTable(); // 联动刷新车库容量
                } else { JOptionPane.showMessageDialog(this, "出场失败！请确认通道编号是否存在。"); }
            }
        });

        tabbedPane.addTab("车场信息管理", panelArea);
        tabbedPane.addTab("出入口通道管理", panelChannel);
        tabbedPane.addTab("车辆信息管理", panelCar);
        tabbedPane.addTab("收费规则配置", panelRule);
        tabbedPane.addTab("停车计费与收益控制中心", panelParkingMain);
        add(tabbedPane);

        // 启动时的全域初始化加载
        loadAreaTable(); loadChannelTable(); loadCarTable(); loadRuleTable(); loadParkingCenterData();
    }

    private void loadAreaTable() {
        areaTableModel.setRowCount(0);
        for (Object[] row : new AreaDAO().getAllAreas()) { areaTableModel.addRow(row); }
    }
    private void loadChannelTable() {
        channelTableModel.setRowCount(0);
        for (Object[] row : new ChannelDAO().getAllChannels()) { channelTableModel.addRow(row); }
    }
    private void loadCarTable() {
        carTableModel.setRowCount(0);
        for (Object[] row : new CarDAO().getAllCars()) { carTableModel.addRow(row); }
    }
    private void loadRuleTable() {
        ruleTableModel.setRowCount(0);
        for (Object[] row : new RuleDAO().getAllRules()) { ruleTableModel.addRow(row); }
    }
    // 控制中心专属双表联动渲染
    private void loadParkingCenterData() {
        inLotTableModel.setRowCount(0);
        for (Object[] row : new ParkingDAO().getInLotVehicles()) { inLotTableModel.addRow(row); }
        
        revenueTableModel.setRowCount(0);
        for (Object[] row : new ParkingDAO().getAreaRevenues()) { revenueTableModel.addRow(row); }
    }
}