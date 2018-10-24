package com.liqkjm.minisql.util;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/10/21 0:19
 */
public class ClientGridBagLayout extends JFrame implements ActionListener {
    /**
     * 主要分为三个模块，左侧一个模块为树形结构（1），
     * 右侧两个模块，一个输入文本框（2），一个输出文本区（3），实现滚动，并且可表格显示
     * 右侧两个模块，通过分割面板，实现可拖曳功能
     |------------------|
     |      |     2     |
     |   1  |-----------|
     |      |           |
     |      |     3     |
     |------------------|
     */

    /*通过 GridBagLayout  布局管理器管理布局*/

    public ClientGridBagLayout() {
        JFrame jFrame = new JFrame("测试窗口");
        jFrame.setSize(500,300);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*菜单栏*/
        JMenuBar jMenuBar = new JMenuBar();

        /*一级菜单*/
        JMenu fileMenu = new JMenu("File");
        JMenu runMenu = new JMenu("Run");
        JMenu cleanMenu = new JMenu("Clean");

        /*添加一级菜单到菜单栏*/
        jMenuBar.add(fileMenu);
        jMenuBar.add(new JMenu("Edit"));
        jMenuBar.add(new JMenu("Config"));
        jMenuBar.add(new JMenu("About"));
        jMenuBar.add(runMenu);
        jMenuBar.add(cleanMenu);

        /*创建文件一级菜单的子菜单*/
        JMenuItem newfileMenu = new JMenuItem("New");
        JMenuItem openfileMenu = new JMenuItem("Open");
        JMenuItem closefileMenu = new JMenuItem("Close");
        JMenuItem exitfileMenu = new JMenuItem("Exit");
        /*添加到文件菜单下*/
        fileMenu.add(newfileMenu);
        fileMenu.add(openfileMenu);
        fileMenu.add(closefileMenu);
        fileMenu.addSeparator();  // 分割线
        fileMenu.add(exitfileMenu);

        /*创建Run下的子菜单*/
        JMenuItem runButtonItem = new JMenuItem("Run");
        JMenuItem stopButtonItem = new JMenuItem("Stop");

        runMenu.add(runButtonItem);
        runMenu.add(stopButtonItem);




        runButtonItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("run");
                // alsert("1");
            }
        });




        /*布局管理器*/
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints c = null;

        /*创建使用GridBagLayout布局的JPanel内容面包容器*/
        JPanel jPanel = new JPanel(gridBagLayout);

        /*三个模块*/

        /*模块一：树形结构*/
        JPanel jPanel1 = new JPanel();
        jPanel1.setBackground(Color.magenta);

        JTextArea testArea = new JTextArea();
        testArea.setLineWrap(true);                         // 自动换行
        testArea.setFont(new Font(null, Font.PLAIN, 18));   // 设置字体

        // 创建根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("中国");

        // 创建二级节点
        DefaultMutableTreeNode gdNode = new DefaultMutableTreeNode("广东");
        DefaultMutableTreeNode fjNode = new DefaultMutableTreeNode("福建");
        DefaultMutableTreeNode shNode = new DefaultMutableTreeNode("上海");
        DefaultMutableTreeNode twNode = new DefaultMutableTreeNode("台湾");

        // 把二级节点作为子节点添加到根节点
        rootNode.add(gdNode);
        rootNode.add(fjNode);
        rootNode.add(shNode);
        rootNode.add(twNode);

        JTree tree = new JTree(rootNode);

        // 设置树显示根节点句柄
        tree.setShowsRootHandles(true);

        // 设置树节点可编辑
        tree.setEditable(true);

        // 设置节点选中监听器
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println("当前被选中的节点: " + e.getPath());
            }
        });

        // 创建滚动面板，包裹树（因为树节点展开后可能需要很大的空间来显示，所以需要用一个滚动面板来包裹）
        JScrollPane treeScrollPane = new JScrollPane(tree);

        /*模块二：输入文本区*/
        // 创建文本区域组件
        JTextArea inputArea = new JTextArea("玩不来GUI啊，我有点崩溃....");
        inputArea.setLineWrap(true);                         // 自动换行
        inputArea.setFont(new Font(null, Font.PLAIN, 18));   // 设置字体

        JScrollPane inputScrollPane = new JScrollPane(
                inputArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        /*模块三：输出文本区*/
        JTextArea outputArea = new JTextArea();
        outputArea.setLineWrap(true);                         // 自动换行
        outputArea.setFont(new Font(null, Font.PLAIN, 18));   // 设置字体

        JScrollPane outputScrollPane = new JScrollPane(
                outputArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        //jPanel3.add(outputScrollPane);

        /*设置三个模块的布局方式*/
        /*添加组件和约束到布局管理器*/

        /*btn1显示区域占5行1列*/
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 5;
        c.weightx = 2;
        c.weighty = 5;
        c.fill = GridBagConstraints.BOTH;
        gridBagLayout.addLayoutComponent(testArea, c);


        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 2;
        c.weightx = 2;
        c.weighty = 2;
        c.fill = GridBagConstraints.BOTH;
        gridBagLayout.addLayoutComponent(inputScrollPane, c);

        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 3;
        c.weightx = 3;
        c.weighty = 3;
        c.fill = GridBagConstraints.BOTH;
        gridBagLayout.addLayoutComponent(outputScrollPane, c);

        jPanel.add(jPanel1);
        jPanel.add(inputScrollPane);
        jPanel.add(outputScrollPane);

        /*添加各个模块*/
        jFrame.setJMenuBar(jMenuBar);
        jFrame.setContentPane(jPanel);

        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        new ClientGridBagLayout();
    }
}

