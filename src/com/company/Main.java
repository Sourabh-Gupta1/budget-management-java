package com.company;

import java.awt.*;
import java.awt.Image;
import javax.swing.*;

public class Main {
    static public App frame;

    public static void main(String[] args) {
        frame = new App("Budget Management");
        frame.setSize(500,500);

        Toolkit tk = Toolkit.getDefaultToolkit();

        Dimension dim = tk.getScreenSize();
        int xPos = dim.width/2 - frame.getWidth()/2;
        int yPos = dim.height/2 - frame.getHeight()/2;

        frame.setLocation(xPos,yPos);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}