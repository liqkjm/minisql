package com.liqkjm.minisql.util;

import javax.swing.*;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/10/23 9:35
 */
public class ClientGroupLayout {

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("测试分组布局管理器");
        jFrame.setSize(600,400);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        /*创建分组布局，并关联容器*/
        GroupLayout groupLayout = new GroupLayout(jPanel);
        /*设置容器的布局*/
        jPanel.setLayout(groupLayout);

        /*创建三个按钮*/
        JButton btn1 = new JButton("btn1");
        JButton btn2 = new JButton("btn2");
        JButton btn3 = new JButton("btn3");

        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();

        /*自动创建组件之间的间隙*/
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        // 水平并行（上下） btn01 和 btn02
        GroupLayout.ParallelGroup hParalGroup01 = groupLayout.createParallelGroup().addComponent(btn2).addComponent(btn3);

        // 水平串行
        GroupLayout.SequentialGroup hParalGroup = groupLayout.createSequentialGroup().addComponent(btn1).addGroup(hParalGroup01);
        // 指定布局的 水平组（水平坐标）
        groupLayout.setHorizontalGroup(hParalGroup);

        // 垂直串行
        GroupLayout.SequentialGroup vsequentialGroup = groupLayout.createSequentialGroup().addComponent(btn2).addComponent(btn3);

        // 垂直并行
        GroupLayout.ParallelGroup vSeqGroup = groupLayout.createParallelGroup().addComponent(btn1).addGroup(vsequentialGroup);


        groupLayout.setVerticalGroup(vSeqGroup);    // 指定布局的 垂直组（垂直坐标）


        jFrame.setJMenuBar(new Menu().getJMenuBar());
        jFrame.setContentPane(jPanel);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);




    }
}
