/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jeremiah Stillings
 * jstillings1@nc.rr.com
 */
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Escape extends JFrame {
    
    public Escape(){
        add(new Level());
        setResizable(false);
        pack();
        
        setTitle("The Great Escape");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
            @Override
    public void run() {                
                JFrame scene1 = new Escape();
                scene1.setVisible(true);                
            }
    });
            }
}
