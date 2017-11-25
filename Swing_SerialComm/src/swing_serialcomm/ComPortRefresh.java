/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing_serialcomm;

import com.fazecast.jSerialComm.SerialPort;

/**
 *
 * @author Guru Sarath
 */
public class ComPortRefresh
{
    private Window mainWindow = null;
    
    public ComPortRefresh (Window mainWindow)
    {
        
        this.mainWindow = mainWindow;

    }
    
    public void refresh()
    {
            mainWindow.portList.removeAllItems();
            mainWindow.NumberOfPorts = 0;
        
            mainWindow.allPorts = SerialPort.getCommPorts();
        
            for(SerialPort port : mainWindow.allPorts)
            {
                mainWindow.portList.addItem(port.getSystemPortName());
                mainWindow.NumberOfPorts ++;
            }
            
            mainWindow.updateTitleBar();
            
        
    }
    
    
}
