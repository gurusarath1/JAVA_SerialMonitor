/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing_serialcomm;

import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 *
 * @author Guru Sarath
 */
public class Swing_SerialComm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
           EventQueue.invokeLater(() -> {
               
               new Window();
        
           });
        
    }
    
}
