/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing_serialcomm;

import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 *
 * @author Guru Sarath
 */
public class Menu_ {
    
    JMenuBar thisMenuBar = new JMenuBar();
    AboutMeDialogBox aboutMe = new AboutMeDialogBox();
    
    public Menu_(Window target)
    {
        
        
        JMenu FileMenu = new JMenu("FILE");

        Action exitAction = new AbstractAction("Exit")
        {
                    
            public void actionPerformed(ActionEvent ae)
            {
                System.exit(0);
            }
                    
        };
        
        
        Action aboutAction = new AbstractAction("----- ABOUT -----")
        {
                    
            public void actionPerformed(ActionEvent ae)
            {
                
                aboutMe.setVisible(true);
            }
                    
        };
        
        Action Refresh = new AbstractAction("Refresh COM PORTS")
        {
                    
            public void actionPerformed(ActionEvent ae)
            {

                target.ComPortLister.refresh();
                
            }
                    
        };
        
        Action ClearScreen = new AbstractAction("Clear Screen")
        {
                    
            public void actionPerformed(ActionEvent ae)
            {

                target.Serial_output.setText("");
                
            }
                    
        };
        
        Action About2 = new AbstractAction("T. Guru Sarath")
        {
                    
            public void actionPerformed(ActionEvent ae)
            {

                target.Serial_output.setText("T.Guru Sarath\n40002990\nBangalore\nIndia");
                
            }
                    
        };
        
        Action baud = new AbstractAction("Print standard baud rates")
        {
                    
            public void actionPerformed(ActionEvent ae)
            {

                target.Serial_output.setText("Standard Baud Rates\n110, 300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 38400, 57600, 115200, 128000 and 256000 bits per second");
                
            }
                    
        };
       
        FileMenu.add(aboutAction);
        FileMenu.add(About2);
        FileMenu.addSeparator();
        FileMenu.add(Refresh);
        FileMenu.add(baud);
        FileMenu.add(ClearScreen);
        FileMenu.addSeparator();
        FileMenu.add(exitAction);
        
        
        thisMenuBar.add(FileMenu);

        target.MainWindow.setJMenuBar(thisMenuBar);
        thisMenuBar.setVisible(true);
        
    }
    
}
