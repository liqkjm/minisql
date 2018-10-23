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
    /**
     * 主要分为三个模块，左侧一个模块为树形结构（1），右侧两个模块，一个输入文本框（2），一个输出文本区（3）
     |------------------|
     |      |     2     |
     |   1  |-----------|
     |      |           |
     |      |     3     |
     |------------------|
     */

    /*TODO：通过 ？？？？  布局管理器管理布局*/



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

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("测试窗口");
        jFrame.setSize(500,300);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*创建JPanel内容面包容器*/
        JPanel jPanel = new JPanel(new BorderLayout());

        /*创建三个按钮*/
        JButton btn1 = new JButton("btn1");
        JButton btn2 = new JButton("btn2");
        JButton btn3 = new JButton("btn3");
        /*添加到东南西北三中某个方位*/
        jPanel.add(btn1, BorderLayout.WEST);
        jPanel.add(btn2, BorderLayout.NORTH);
        jPanel.add(btn3, BorderLayout.SOUTH);

        /*文本区 滚动面板*/
        // 创建文本区域组件
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);                         // 自动换行
        textArea.setFont(new Font(null, Font.PLAIN, 18));   // 设置字体

        // 创建滚动面板, 指定滚动显示的视图组件(textArea), 垂直滚动条一直显示, 水平滚动条从不显示
        JScrollPane scrollPane = new JScrollPane(
                textArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        jFrame.setContentPane(scrollPane);

        /*添加各个模块*/
        jFrame.setJMenuBar(new Menu().getJMenuBar());
//        jFrame.setContentPane(jPanel);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}

