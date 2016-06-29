package com.spider.collection.main;

/**
 * Created by Administrator on 2015/12/30.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingTest extends JFrame
{

    public SwingTest()
    {
        // 初始化所有模块
        MenuTest menuTest = new MenuTest();
        BottomPanel bottomPanel = new BottomPanel();

        // 设置主框架的布局
        Container c = this.getContentPane();
        // c.setLayout(new BorderLayout())
        this.setJMenuBar(menuTest);

        c.add(bottomPanel,BorderLayout.SOUTH);

        // 利用无名内隐类，增加窗口事件
        this.addWindowListener(new WindowAdapter()
        {
            public void WindowClosing(WindowEvent e)
            {
                // 释放资源，退出程序
                dispose();
                System.exit(0);
            }
        });



        setSize(700,500);
        setTitle("分布式爬虫系统");
        // 隐藏frame的标题栏,此功暂时关闭，以方便使用window事件
        // setUndecorated(true);
        setLocation(200,150);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        show();
    }
    ////////////////////////////////////////////////////////////////////////////

    class MenuTest extends JMenuBar
    {
        private JDialog aboutDialog;


        public MenuTest()
        {
            JMenu fileMenu = new JMenu("文件");
            JMenuItem exitMenuItem = new JMenuItem("退出",KeyEvent.VK_E);
            JMenuItem aboutMenuItem = new JMenuItem("关于...",KeyEvent.VK_A);

            fileMenu.add(exitMenuItem);
            fileMenu.add(aboutMenuItem);

            this.add(fileMenu);


            aboutDialog = new JDialog();
            initAboutDialog();

            // 菜单事件
            exitMenuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    dispose();
                    System.exit(0);
                }
            });

            aboutMenuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    // "关于"对话框的处理
                    aboutDialog.show();
                }
            });

        }


        public JDialog getAboutDialog()
        {
            return aboutDialog;
        }


        public void initAboutDialog()
        {
            aboutDialog.setTitle("关于");

            Container con =aboutDialog.getContentPane();

            // Swing 中使用html语句
            Icon icon = new ImageIcon("smile.gif");
            JLabel aboutLabel = new JLabel("<html><b><font size=5>"+
                    "<center>Swing 爬虫系统！"+"<br>ucsmy",icon,JLabel.CENTER);

            //JLabel aboutLabel = new JLabel("Swing 组件大全简体版！",icon,JLabel.CENTER);
            con.add(aboutLabel,BorderLayout.CENTER);

            aboutDialog.setSize(450,225);
            aboutDialog.setLocation(300,300);
            aboutDialog.addWindowListener(new WindowAdapter()
            {
                public void WindowClosing(WindowEvent e)
                {
                    dispose();
                }
            });
        }
    }

    class BottomPanel extends JPanel
    {
        private JProgressBar pb;
        ////////////////////////////////////////
        //public class
        //////////////////////////////
        public BottomPanel()
        {
            pb = new JProgressBar();
            pb.setPreferredSize(new Dimension(680,20));

            // 设置定时器，用来控制进度条的处理
            Timer time = new Timer(1,new ActionListener()
            {
                int counter = 0;
                public void actionPerformed(ActionEvent e)
                {
                    counter++;
                    pb.setValue(counter);
                    Timer t = (Timer)e.getSource();

                    // 如果进度条达到最大值重新开发计数
                    if (counter == pb.getMaximum())
                    {
                        t.stop();
                        counter =0;
                        t.start();
                    }
                }
            });
            time.start();

            pb.setStringPainted(true);
            pb.setMinimum(0);
            pb.setMaximum(10000);
            pb.setBackground(Color.white);
            pb.setForeground(Color.red);

            this.add(pb);
        }


        public void setProcessBar(BoundedRangeModel rangeModel)
        {
            pb.setModel(rangeModel);
        }
    }



    public static void main(String args[])
    {
        // 设置主框架属性,此处没有使用，可打开看看效果
        //try
        //{
        //    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //}
        //catch  (Exception e){}
        new SwingTest();
        for (int i = 0; i < 1; i++) {
            SpiderApplication spiderApplication = new SpiderApplication();
            spiderApplication.getJob("");
            spiderApplication.init();
        }
    }
}
