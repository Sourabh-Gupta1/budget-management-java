package com.company;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

public class Income extends JFrame {
    JButton submit;
    JLabel wIcon;
    JTextField amt,src,comments;
    public Income(String st) {
        super(st);
        initComponents();
    }

    private void initComponents() {
        submit = new JButton("SUBMIT");
        getRootPane().setBorder(BorderFactory.createMatteBorder(8,8,8,8,Color.gray));

        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getResource("back.png"));

        }
        catch (IOException io) {

        }
        wIcon = new JLabel(new ImageIcon(img));
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout(60,120));
        JPanel panBtn = new JPanel(new BorderLayout());
        panBtn.add(wIcon,BorderLayout.WEST);

        JPanel paneFields = new JPanel();
        paneFields.setLayout(new BoxLayout(paneFields,BoxLayout.PAGE_AXIS));

        amt = new JTextField(3);
        src = new JTextField(3);
        comments = new JTextField(3);

        paneFields.add(setFieldAndLabel(amt,"Amount      "));
        paneFields.add(setFieldAndLabel(src,"Source        "));
        paneFields.add(setFieldAndLabel(comments,"Comments  "));

        pane.add(panBtn,BorderLayout.NORTH);
        pane.add(paneFields,BorderLayout.CENTER);

        JPanel paneSubmit = new JPanel();
        paneSubmit.setLayout(new BorderLayout());
        paneSubmit.add(submit,BorderLayout.AFTER_LINE_ENDS);
        pane.add(paneSubmit,BorderLayout.SOUTH);
        addListeners();
    }

    private JPanel setFieldAndLabel(JTextField txtField,String txt) {
        JLabel lbl = new JLabel(txt,SwingConstants.LEFT);
        lbl.setFont(new Font("Serif",Font.PLAIN,25));
        lbl.setVerticalAlignment(SwingConstants.CENTER);

        JPanel pane = new JPanel(new BorderLayout(20,1));
        pane.add(lbl,BorderLayout.WEST);

        pane.add(txtField,BorderLayout.CENTER);
        return pane;
    }

    private void addListeners() {
        wIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                Main.frame.setVisible(true);
            }
        });

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amtStr = amt.getText();
                String srcStr = src.getText();
                String commentStr = comments.getText();
                amt.setText("");
                src.setText("");
                comments.setText("");
                Date date = new Date();
                if(validateStringForInteger(amtStr) && validateSource(srcStr) &&  validateComment(commentStr)) {
                    try {
                        App.preparedStatementIncome.setString(1, date.toString());
                        App.preparedStatementIncome.setInt(2,new Integer(amtStr));
                        App.preparedStatementIncome.setString(3,srcStr);
                        App.preparedStatementIncome.setString(4,commentStr);
                        App.preparedStatementIncome.executeUpdate();
                    }
                    catch (Exception ex) {
                        System.out.println("Insertion Error");
                    }
                }
                else {
                    JOptionPane optionPane = new JOptionPane("Some Fields Are Not Validated Properly!!",JOptionPane.ERROR_MESSAGE);
                    JDialog dialog = optionPane.createDialog("Error");
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                }
                setVisible(false);
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension dim = tk.getScreenSize();
                Main.frame = new App("Budget Management");
                Main.frame.setSize(500,500);
                int xPos = dim.width/2 - Main.frame.getWidth()/2;
                int yPos = dim.height/2 - Main.frame.getHeight()/2;
                Main.frame.setLocation(xPos,yPos);
                Main.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                Main.frame.setVisible(true);
            }

        });
    }

    private boolean validateStringForInteger(String str) {
        if(str.length() == 0) {
            return false;
        }

        for(int i=0;i<str.length();i++) {
            if(!(str.charAt(i) >= '0' && str.charAt(i) <= '9')) {
                return false;
            }
        }
        return true;
    }

    private boolean validateSource(String str) {
        return str.length() != 0;
    }

    private boolean validateComment(String str) {
        return str.length() != 0;
    }
}

