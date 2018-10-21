package com.liqkjm.minisql.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/10/21 0:19
 */
public class Client extends JFrame implements ActionListener {

    JLabel input_label, show_label;
    JPanel input_pannel, show_pannel, btn_pannel;
    JTextField input_text;
    JButton select_btn;

    final String start_message = "Welcome to HNUSQL";
    public Client() {
        input_label = new JLabel("输入命令");
        show_label = new JLabel("查询结果");
        select_btn = new JButton("SELECT");
        select_btn.addActionListener(this); // 对查询按钮设置监听

        input_pannel = new JPanel();
        show_pannel = new JPanel();
        btn_pannel = new JPanel();

        input_text = new JTextField(20);
        JLabel welcome_label = new JLabel(start_message); // 创建一个JLabel标签
        welcome_label.setHorizontalAlignment(SwingConstants.CENTER);
        welcome_label.setVerticalAlignment(SwingConstants.CENTER);// 使标签文字居中
        welcome_label.setBounds(0,75,400,2);


        // MessagePannel messsage =  new MessagePannel("Welcome to HNUSQL");
        // 创建一个匿名的FlowLayout对象

        show_pannel.add(welcome_label);
        this.add(show_pannel);

        input_pannel.add(new JLabel("Please input sql commands:"));
        input_pannel.add(input_text);
        this.add(input_pannel);

        btn_pannel.add(select_btn);
        this.add(btn_pannel);
        this.setLayout (new GridLayout(4,1));
        this.setTitle("HNUSQL");
        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

