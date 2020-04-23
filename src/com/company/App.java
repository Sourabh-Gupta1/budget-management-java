package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;

public class App extends JFrame{
    static public Connection connect = null;
    static public Statement statement = null;
    static public PreparedStatement preparedStatementIncome = null;
    static public PreparedStatement preparedStatementExpenditure = null;
    static public PreparedStatement preparedStatementRetrieval = null;
    static public ResultSet resultSet = null;

    JButton inc , exp , summ;
    Income i = new Income("Income");
    Expenditure ex = new Expenditure("Expenditure");
    static Summary sum;
    Object[] data = {};
    public App(String str) {
        super(str);
        initComponents();
    }

    private void initComponents() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/budgetApp?"
                    + "user=sqluser&password=sqluserpw");
            preparedStatementIncome = connect
                    .prepareStatement("insert into  budgetApp.income values (default, ?, ?, ?, ? )");
            preparedStatementExpenditure = connect
                    .prepareStatement("insert into  budgetApp.expenditure values (default, ?, ?, ?, ?)");
            statement = connect.createStatement();

        }
        catch(Exception e) {
            System.out.println("Connection Error");
        }


        sum = new Summary("Summary");

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();

        int xPos;
        int yPos;

        i.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        i.setSize(500,500);
        xPos = dim.width/2 - i.getWidth()/2;
        yPos = dim.height/2 - i.getHeight()/2;
        i.setLocation(xPos,yPos);

        ex.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ex.setSize(500,500);
        xPos = dim.width/2 - ex.getWidth()/2;
        yPos = dim.height/2 - ex.getHeight()/2;
        ex.setLocation(xPos,yPos);

        sum.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        sum.setSize(800,400);
        xPos = dim.width/2 - sum.getWidth()/2;
        yPos = dim.height/2 - sum.getHeight()/2;
        sum.setLocation(xPos,yPos);

        getRootPane().setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, Color.GRAY));
        inc = new JButton("INCOME");
        inc.setPreferredSize(new Dimension(100,50));
        inc.setFont(new Font("Serif",Font.PLAIN,20));
        exp = new JButton("EXPENDITURE");
        exp.setPreferredSize(new Dimension(150,50));
        exp.setFont(new Font("Serif",Font.PLAIN,20));
        summ = new JButton("SUMMARY");
        summ.setPreferredSize(new Dimension(150,50));
        summ.setFont(new Font("Serif",Font.PLAIN,20));

        JPanel paneTitle = new JPanel(new BorderLayout());
        JLabel title = new JLabel("BUDGET MANAGEMENT APP",SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.darkGray);
        title.setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.BLACK));
        title.setPreferredSize(new Dimension(300,50));
        paneTitle.add(title,BorderLayout.CENTER);

        JPanel panOuter = new JPanel(new BorderLayout(50,60));
        JPanel panLeft = new JPanel(new BorderLayout());
        panLeft.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panRight = new JPanel(new BorderLayout());
        panRight.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panBottom = new JPanel(); // default is FlowLayout
        panBottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panInput = new JPanel(new BorderLayout());
        panInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        JPanel panBtn = new JPanel(new FlowLayout(FlowLayout.CENTER,40,10));
        panBtn.add(panLeft);
        panBtn.add(panRight);

        panInput.setBackground(Color.BLACK);
        panInput.add(panBtn, BorderLayout.NORTH);
        panInput.add(panBottom, BorderLayout.CENTER);

        panOuter.add(paneTitle, BorderLayout.NORTH);
        panOuter.add(panInput, BorderLayout.CENTER);
        panOuter.add(Box.createRigidArea(new Dimension(100,100)),BorderLayout.SOUTH);

        panLeft.add(inc, BorderLayout.NORTH);
        panRight.add(exp, BorderLayout.NORTH);
        panBottom.add(summ);


        setContentPane(panOuter);

        addListeners();
    }

    private void addListeners() {
        inc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                i.setVisible(true);
            }
        });

        exp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                ex.setVisible(true);
            }
        });

        summ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                sum.setVisible(true);
            }
        });
    }

}