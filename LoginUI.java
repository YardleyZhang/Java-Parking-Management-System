package com.parking.ui;

import com.parking.dao.AdminDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnReset;

    public LoginUI() {
        // 1. 窗口基础设置
        setTitle("停车场管理系统 - 管理员登录");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new GridLayout(3, 2, 10, 10)); 

        // 2. 绘制组件
        add(new JLabel("  管理员账号:", SwingConstants.CENTER));
        txtUsername = new JTextField();
        add(txtUsername);

        add(new JLabel("  管理员密码:", SwingConstants.CENTER));
        txtPassword = new JPasswordField();
        add(txtPassword);

        btnLogin = new JButton("登录");
        btnReset = new JButton("重置");
        
        JPanel panelBtn = new JPanel();
        panelBtn.add(btnLogin);
        panelBtn.add(btnReset);
        add(new JLabel("")); // 空白占位
        add(panelBtn);

        // 3. 业务逻辑绑定：点击登录按钮触发的闭环事件
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                
                // 基础校验
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "用户名或密码不能为空！");
                    return;
                }

                // 调用 DAO 层去跑数据库
                AdminDAO dao = new AdminDAO();
                if (dao.login(username, password)) {
                    JOptionPane.showMessageDialog(null, "登录成功！欢迎回来，阳光活力的管理员！");
                    
                    // 打通界面的闭环：打开主界面，关闭登录界面
                    new MainAdminUI().setVisible(true);
                    dispose(); 
                    
                } else {
                    JOptionPane.showMessageDialog(null, "账号或密码错误，请检查！");
                }
            }
        });

        // 4. 重置按钮逻辑
        btnReset.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
        });
    }

    // 程序的真正入口点
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}