package com.liqkjm.minisql.util;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/10/24 23:16
 */
public class ClientAbsoluteLayout extends JFrame implements ActionListener{

    private JFrame jFrame;  // 顶级容器
    private JPanel jPanel;  // 中间容器
    private JMenuBar jMenuBar; // 菜单栏
    private JScrollPane treeScrollPane; // 左侧列表栏
    private JScrollPane inputScrollPane; // 输入文本区
    private JScrollPane outputScrollPane; // 输出文本区
    private JTextArea inputArea;
    private JTextArea outputArea;

    private JMenuItem runButtonItem;


    public ClientAbsoluteLayout() {
        jFrame = new JFrame("绝对布局窗口");
        jFrame.setSize(570,460);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*菜单栏*/
        jMenuBar = new JMenuBar();

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
        runButtonItem = new JMenuItem("Run");
        JMenuItem stopButtonItem = new JMenuItem("Stop");

        runMenu.add(runButtonItem);
        runMenu.add(stopButtonItem);

        /*runButtonItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("run" + inputArea.getText());

            }
        });*/

        /*模块一：树形结构*/
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
        treeScrollPane = new JScrollPane(tree);

        /*模块二：输入文本区*/
        // 创建文本区域组件
        inputArea = new JTextArea("傻逼队友，我有点崩溃....");
        inputArea.setLineWrap(true);                         // 自动换行
        inputArea.setFont(new Font(null, Font.PLAIN, 18));   // 设置字体

        inputScrollPane = new JScrollPane(
                inputArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        /*模块三：输出文本区*/
        outputArea = new JTextArea();
        outputArea.setLineWrap(true);                         // 自动换行
        outputArea.setFont(new Font(null, Font.PLAIN, 18));   // 设置字体

        outputScrollPane = new JScrollPane(
                outputArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );



        /*指定布局为null，使用绝对布局的内容面板*/
        jPanel = new JPanel(null);

        treeScrollPane.setLocation(0,0);
        treeScrollPane.setSize(150,500);

        inputScrollPane.setLocation(150,0);
        inputScrollPane.setSize(400,150);

        outputScrollPane.setLocation(150,150);
        outputScrollPane.setSize(400,350);

        jPanel.add(treeScrollPane);
        jPanel.add(inputScrollPane);
        jPanel.add(outputScrollPane);

        jFrame.setContentPane(jPanel);
        jFrame.setJMenuBar(new Menu().getJMenuBar());
        jFrame.setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("inputArea" + inputArea.getText());

        if(e.getSource() == runButtonItem) {
            System.out.println("监听事件");
            outputArea.setText(inputArea.getText());
        }
    }
    public static void main(String[] args) {
        new ClientAbsoluteLayout();
    }
}
