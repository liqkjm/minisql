# minisql

尝试结合spring和java图形界面

暂时没有想到打包的方式，只能通过编译器启动，或者分别注释掉其中一个，打成jar包

如此对于本地客户端，那就没有什么问题了，但是web端可能还会有比较多的问题。

## 项目主要目录
- server    服务端代码，实现数据库的所有操作接口
- util  工具类，本地客户端
- controller    与前端交互

## 客户端

### Swing

#### java程序界面的构成：
1. 一个顶层容器
2. 顶层容器包含若干个中间容器
3. 每个中间容器包含若干个基本组件
4. 按照合理的布局方式将它们组织在一起
5. 基本组件可相应发生在其上的事件

##### 顶层容器
创建初始界面，为其他组件提供一个容器，以构建满足用户需求的操作界面。

- Container
    - JWindow   有边框容器
        - JDialog
        - JFrame
    - JPanel    无边框容器
        - JApplet
     
##### 中间容器
JPanel，JScrollPane,JSplitPane,JTabbedPanel,  JInternalFrame，BOX

##### 基本组件
- JLabel
- JButton，JCheckBox，JRadioButton
- JList，JComboBox
- JTestFiled，JPasswordFiled，JTextArea
- JToolBar，JToolTip，JProgressBar
- JSlider，JSpinner
- JFileChooser，JColorChooser
- JMenuBar、JMenuBar、JmenuItem....
- JTable
- JTree
- JOptionPane、JSeparator
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              

#### GridBagLayout 网格袋布局管理器

##### 构造方式以及常用方法
GridBayLayout.add(Commpent, Constrains)

##### GridBagConstraints中各个属性的具体含义：
@gridx,gridy
组件左上角所在的位置

@gridwidth,gridheight
组件占据的行数和列数

@weightx,weighty
组件大小变化的增量值

@fill
组件在所分配区域的填充方式

@anchor
组件在所分配区域的对其方式

@ipadx,ipady
内部填充

@insets
外部填充

##### 
