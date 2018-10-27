package com.liqkjm.minisql.util;

import com.liqkjm.minisql.server.interpreter.Interpreter;

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
 * @Date 2018/10/24 23:16
 */
public class ClientAbsoluteLayout extends JFrame implements ActionListener{

    private JFrame jFrame;  // 顶级容器
    private JPanel jPanel;  // 中间容器
    private JMenuBar jMenuBar; // 菜单栏
    private JScrollPane treeScrollPane; // 左侧列表栏
    private JTextArea inputArea;
    private JScrollPane inputScrollPane; // 输入文本区
    private JTextArea outputArea;
    private JScrollPane outputScrollPane; // 输出文本区

    /*按钮组*/
    private JMenuItem runButtonItem;
    private JButton runButton ;
    private JButton cleanButton;
    /*翻译器*/
    private Interpreter interpreter = new Interpreter();
    // private Interpreter interpreter = MinisqlApplication.interpreter;

    public ClientAbsoluteLayout() {
        jFrame = new JFrame("绝对布局窗口");
        jFrame.setSize(615,460);
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
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("data");

        // 动态创建二级节点
        /**
         * TODO: 获取文件列表，并在变化时，更新列表
         */
        String[] databases = {"database1", "database2","testDatabase"};
        int databaseLength = databases.length;
        DefaultMutableTreeNode[] databaseNode = new DefaultMutableTreeNode[databaseLength];
        for(int i = 0; i < databaseLength; i++){
            databaseNode[i] = new DefaultMutableTreeNode(databases[i]);
            rootNode.add(databaseNode[i]);
            /*动态创建三级节点*/
            String[] tables = {"table1", "table2","table3"};
            int tableLength = tables.length;
            DefaultMutableTreeNode[] tableNode = new DefaultMutableTreeNode[tableLength];
            for(int j = 0; j < tableLength; j++) {
                tableNode[j] = new DefaultMutableTreeNode(tables[i]);
                databaseNode[i].add(tableNode[j]);
            }
        }

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

        inputArea = new JTextArea("slect * from student where sno = 1 ;\n" +
                "create database test;\n" +
                "show databases;");
        inputArea.setLineWrap(true);                         // 自动换行
        inputArea.setFont(new Font(null, Font.PLAIN, 16));   // 设置字体

        inputScrollPane = new JScrollPane(
                inputArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        runButton = new JButton("RUN");
        cleanButton = new JButton("CLEAN");

        /**
         * TODO：获取输入命令，并调用服务端接口，得到对应数据，并输出
         */
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputMessage = inputArea.getText();
                System.out.println("run绑定事件 \n" + inputMessage);
                String outputMessage = interpreter.getResult(inputMessage);
                outputArea.setText(outputMessage);
            }
        });

        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clean");
                inputArea.setText("");
            }
        });

        /*模块三：输出文本区*/
        outputArea = new JTextArea("Welcome MiniSQL~  HELP YOURSELF");
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
        treeScrollPane.setSize(150,400);

        inputScrollPane.setLocation(150,0);
        inputScrollPane.setSize(350,150);

        runButton.setLocation(500,0);
        runButton.setSize(100,75);
        cleanButton.setLocation(500,75);
        cleanButton.setSize(100,75);

        outputScrollPane.setLocation(150,150);
        outputScrollPane.setSize(450,250);

        jPanel.add(treeScrollPane);
        jPanel.add(inputScrollPane);
        jPanel.add(runButton);
        jPanel.add(cleanButton);
        jPanel.add(outputScrollPane);

        jFrame.setContentPane(jPanel);
        jFrame.setJMenuBar(new Menu().getJMenuBar());
        jFrame.setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("inputArea" + inputArea.getText());

        if(e.getSource() == runButton) {
            System.out.println("全局监听函数输出");
            outputArea.setText(inputArea.getText());
        }
    }
    public static void main(String[] args) {
        ClientAbsoluteLayout client = new ClientAbsoluteLayout();
        client.runButtonItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("input"+client.inputArea);
            }
        });
    }
}
