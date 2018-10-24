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
public class Client extends JFrame implements ActionListener {
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

    public Client() {}

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        //new Client();
        new ClientAbsoluteLayout();
    }
}

