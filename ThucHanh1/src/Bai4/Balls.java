/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Bai4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Balls extends Thread {

    private JPanel box;                 
    private static final int XSIZE = 25; 
    private static final int YSIZE = 25; 
    private int x = 0;                   
    private int y = 0;                   
    private int dx = 2;                  
    private int dy = 2;                  

    public Balls(JPanel p) {
        box = p;
    }

    public void draw() {
        Graphics g = box.getGraphics();
        g.setColor(Color.RED);            
        g.fillOval(x, y, XSIZE, YSIZE);   
        g.dispose();
    }

    public void clear() {
        Graphics g = box.getGraphics();
        g.setColor(box.getBackground());  
        g.fillOval(x, y, XSIZE, YSIZE);   
        g.dispose();
    }

    public void move() {
        clear();

        x += dx;
        y += dy;

        Dimension d = box.getSize();
        if (x < 0) {
            x = 0;
            dx = -dx;
        }

        if (x + XSIZE >= d.getWidth()) {
            x = d.width - XSIZE;
            dx = -dx;
        }

        if (y < 0) {
            y = 0;
            dy = -dy;
        }

        if (y + YSIZE >= d.getHeight()) {
            y = d.height - YSIZE;
            dy = -dy;
        }

        draw();
    }

    
    public void run() {
    draw(); 
    for (int i = 0; i < 500; i++) { 
        move(); 
        try {
            Thread.sleep(20); 
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    clear();
}
}