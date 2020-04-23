package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Summary extends JFrame {
    JLabel wIcon;
    JLabel inc[];
    JLabel exp[];
    public Summary(String str) {
        super(str);
        inc = new JLabel[1000];
        exp = new JLabel[1000];
        initComponents();
    }

    private void initComponents() {

        getRootPane().setBorder(BorderFactory.createMatteBorder(8,8,8,8,Color.gray));
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getResource("back.png"));

        }
        catch (IOException io) {
            System.out.println("Image Uploading Error");
        }


        wIcon = new JLabel(new ImageIcon(img));
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        JPanel panBtn = new JPanel(new BorderLayout());
        panBtn.add(wIcon,BorderLayout.WEST);

        JPanel paneOuter = new JPanel(new BorderLayout());;
        paneOuter.add(panBtn,BorderLayout.NORTH);


        JPanel paneIncome = new JPanel();
        JPanel paneExpenditure = new JPanel();

        JPanel paneBoxes = new JPanel();
        paneBoxes.setLayout(new BoxLayout(paneBoxes,BoxLayout.Y_AXIS));

        paneIncome.setLayout(new BoxLayout(paneIncome,BoxLayout.Y_AXIS));
        paneIncome.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.BLACK)
                ,"INCOME"));

        paneExpenditure.setLayout(new BoxLayout(paneExpenditure,BoxLayout.Y_AXIS));
        paneExpenditure.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.BLACK),
                "EXPENDITURE"));

        JPanel paneSummary = new JPanel();
        paneSummary.setLayout(new BoxLayout(paneSummary,BoxLayout.X_AXIS));
        paneSummary.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.BLACK)
                ,"SUMMARY"));


        paneBoxes.add(Box.createRigidArea(new Dimension(0,20)));
        paneBoxes.add(paneIncome);
        paneBoxes.add(Box.createRigidArea(new Dimension(0,30)));
        paneBoxes.add(paneExpenditure);

        paneOuter.add(paneSummary, BorderLayout.SOUTH);
        paneOuter.add(paneBoxes,BorderLayout.EAST);
        try {
            App.preparedStatementRetrieval = App.connect.prepareStatement("select * from income;");
            App.resultSet = App.preparedStatementRetrieval.executeQuery();

            while(App.resultSet.next()) {
                int id = App.resultSet.getInt("Id");
                String date = App.resultSet.getString("Date");
                Integer amount = App.resultSet.getInt("Amount");
                String source = App.resultSet.getString("Source");
                String comment = App.resultSet.getString("Comment");

                String f = "On " + date + " " + "you were credited " + amount.toString() + "Rs by/for " + source + " -> " + comment;
                inc[id] = new JLabel(f);
                inc[id].setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
                inc[id].setFont(new Font("Serif",Font.PLAIN,17));
                paneIncome.add(inc[id]);
                paneIncome.add(Box.createRigidArea(new Dimension(0,10)));
            }

            App.preparedStatementRetrieval = App.connect.prepareStatement("select * from expenditure");
            App.resultSet = App.preparedStatementRetrieval.executeQuery();

            while(App.resultSet.next()) {
                int id = App.resultSet.getInt("Id");
                String date = App.resultSet.getString("Date");
                Integer amount = App.resultSet.getInt("Amount");
                String source = App.resultSet.getString("Type");
                String comment = App.resultSet.getString("Comment");

                String f = "On " + date + " " + "you were debited " + amount.toString() + "Rs by/for " + source + " -> " + comment;
                exp[id] = new JLabel(f);
                exp[id].setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
                exp[id].setFont(new Font("Serif",Font.PLAIN,17));
                paneExpenditure.add(exp[id]);
                paneExpenditure.add(Box.createRigidArea(new Dimension(0,10)));
            }

            App.preparedStatementRetrieval = App.connect.prepareStatement("select sum(Amount) from income");
            App.resultSet = App.preparedStatementRetrieval.executeQuery();

            while(App.resultSet.next()) {
                Integer amount = App.resultSet.getInt("sum(Amount)");
                JLabel i = new JLabel("INCOME : " + amount.toString());
                i.setFont(new Font("Serif",Font.PLAIN,17));
                i.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
                paneSummary.add(Box.createRigidArea(new Dimension(150,0)));
                paneSummary.add(i);
            }

            App.preparedStatementRetrieval = App.connect.prepareStatement("select sum(Amount) from expenditure");
            App.resultSet = App.preparedStatementRetrieval.executeQuery();

            while(App.resultSet.next()) {
                Integer amount = App.resultSet.getInt("sum(Amount)");
                JLabel i = new JLabel("EXPENDITURE : " + amount.toString());
                i.setFont(new Font("Serif",Font.PLAIN,17));
                i.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
                paneSummary.add(Box.createRigidArea(new Dimension(150,0)));
                paneSummary.add(i);
                paneSummary.add(Box.createRigidArea(new Dimension(50,0)));
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }

        JScrollPane scrollPane = new JScrollPane(paneOuter,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
                ,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setContentPane(scrollPane);

        addActionListeners();
    }

    private void addActionListeners() {
        wIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                Main.frame.setVisible(true);
            }
        });

        for(int i=0;i<1000;i++) {
            if(inc[i] != null) {
                final Integer x = i;
                inc[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        inc[x] = null;
                        try {
                            App.statement.executeUpdate("delete from income where id = " + x.toString());
                            setVisible(false);
                            Toolkit tk = Toolkit.getDefaultToolkit();
                            Dimension dim = tk.getScreenSize();
                            Main.frame = new App("Budget Management");
                            Main.frame.setSize(500,500);
                            int xPos = dim.width/2 - Main.frame.getWidth()/2;
                            int yPos = dim.height/2 - Main.frame.getHeight()/2;
                            Main.frame.setLocation(xPos,yPos);
                            Main.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                            Main.frame.sum.setVisible(true);
                        }
                        catch (Exception ex) {
                            System.out.println("DELETION ERROR");
                        }
                    }
                });
            }
        }

        for(int i=0;i<1000;i++) {
            if(exp[i] != null) {
                final Integer x = i;
                exp[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        exp[x] = null;
                        try {
                            App.statement.executeUpdate("delete from expenditure where id = " + x.toString());
                            setVisible(false);
                            Toolkit tk = Toolkit.getDefaultToolkit();
                            Dimension dim = tk.getScreenSize();
                            Main.frame = new App("Budget Management");
                            Main.frame.setSize(500,500);
                            int xPos = dim.width/2 - Main.frame.getWidth()/2;
                            int yPos = dim.height/2 - Main.frame.getHeight()/2;
                            Main.frame.setLocation(xPos,yPos);
                            Main.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                            Main.frame.sum.setVisible(true);
                        }
                        catch (Exception ex) {
                            System.out.println("DELETION ERROR");
                        }
                    }
                });
            }
        }
    }
}