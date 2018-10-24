package com.liqkjm.minisql.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/10/23 8:30
 */
public class Menu extends JMenuBar{
    public Menu() {

    }
    public JMenuBar getJMenuBar() {
        /*JFrame jFrame = new JFrame();
        jFrame.setSize(400,300);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);*/
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
        jMenuBar.add(new JMenu("Help"));
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

        return jMenuBar;
    }

    /**
     * 测试菜单组件，目前通过get方式返回JMenuBar实例。
     * TODO： 通过继承JMenuBar,创建其子类，直接通过构造方式重写其样式，重写的方式存在问题，不应该在构造方式中
     * 创建一个JFMenuBar，而是应该重写...
     * @param args
     */
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("测试菜单栏");
        jFrame.setSize(400,300);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        /*把菜单栏设置到窗口*/

        JMenuBar jMenuBar = new Menu().getJMenuBar();
        jFrame.setJMenuBar(jMenuBar);
        jFrame.setVisible(true);
    }
}
