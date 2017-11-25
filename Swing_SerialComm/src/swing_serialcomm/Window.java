/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing_serialcomm;

import com.fazecast.jSerialComm.SerialPort;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.Border;

/**
 *
 * @author Guru Sarath
 */
public class Window implements GLOBAL_CONSTANTS{
    
        JFrame MainWindow = new JFrame();
        
        
        public static SerialPort chosenPort;
        public static boolean runThread = false; 
        
        public SerialPort[] allPorts = null;
        public JComboBox<String> portList = new JComboBox<>();
        public ComPortRefresh ComPortLister = new ComPortRefresh(this);
        public JTextArea Serial_output = null;
        public int NumberOfPorts = 0;
        public String DiscriptivePortName = null;
        
        public int baudRate = 9600;

        
        public Window()
        {
  
            Menu_ menus = new Menu_(this);
            
            MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            MainWindow.setSize(ScreenWidth/2, ScreenHeight/2);
            MainWindow.setLayout(new BorderLayout());
            
            Serial_output = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(Serial_output);
            JPanel TopPanel = new JPanel();
            JPanel BottomPanel = new JPanel();
            
            JPanel MainControl = new JPanel();
            Border MainControl_Border = BorderFactory.createTitledBorder("Main Controls");
            MainControl.setBorder(MainControl_Border);
            
            JPanel SubControl = new JPanel();
            Border SubControl_Border = BorderFactory.createTitledBorder("COM PORT settings");
            SubControl.setBorder(SubControl_Border);
            
            JButton Connect = new JButton("Connect");
            JCheckBox no_newLine = new JCheckBox("Ignore New Line");
            JCheckBox clearAfterSend = new JCheckBox("Auto Clear Data");
            clearAfterSend.setSelected(true);
                        
            JLabel lbl_bottom = new JLabel("DATA : ");
            JButton SendData = new JButton("Send");
            JTextField TextToSend = new JTextField("",50);
            SendData.setEnabled(false);
            TextToSend.setEnabled(false);
            
            JLabel BaudRateLabel = new JLabel("BAUD: ");
            JTextField BaudRateField = new JTextField("9600",4);
            JLabel SeperatorLabel = new JLabel("Seperator: ");
            JTextField SeperatorField = new JTextField("",1);
            SeperatorField.setEnabled(false);
            SeperatorLabel.setEnabled(false);
            
            SendData.addActionListener((ActionEvent ae) -> {
                
                int size = TextToSend.getText().getBytes().length;
                byte[] toSend = new byte[size];
                toSend = TextToSend.getText().getBytes();
                chosenPort.writeBytes(toSend, size);
                
                if(clearAfterSend.isSelected())
                {
                    TextToSend.setText("");
                }
            
            });
            
            
            
            no_newLine.addActionListener((ActionEvent ae) -> {
            
                if(no_newLine.isSelected())
                {
                    SeperatorField.setEnabled(true);
                    SeperatorLabel.setEnabled(true);

                } else {

                    SeperatorField.setEnabled(false);
                    SeperatorLabel.setEnabled(false);
                }
                
            
            });
            
            ComPortLister.refresh();
 
            ActionListener button_action = (ActionEvent ae) -> {
                
                
                if(portList.getSelectedItem() == null && !(Connect.getText().equals("Refresh")))
                {
                    Serial_output.append("\n ----------- NO PORT SELECTED -----------\n(Connect a serial device and try again)\n");
                    Serial_output.setCaretPosition(Serial_output.getText().length());
                    Connect.setText("Refresh");
                }
                else if(Connect.getText().equals("Refresh"))
                {
                    
                    ComPortLister.refresh();

                    if(portList.getSelectedItem() != null) Connect.setText("Connect");
                    else Serial_output.append("\n ----------- No Serial Device Found :( -----------\n(Connect a serial device and try again)\n");
                    Serial_output.setCaretPosition(Serial_output.getText().length());
                    
                    
                }
                else if(Connect.getText().equals("Connect"))
                {
                    chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                    chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 10, 10);
                    
                    try{
                        baudRate = Integer.parseInt(BaudRateField.getText());
                        setUpSelectedPort(baudRate);
                        
                    } catch (Exception e)
                    {
                        Serial_output.append("\n ----------- INVALID BAUD -----------\n");
                        Serial_output.append("\n ----------- Starting Port with Default Baud Rate - 9600 -----------\n");
                        BaudRateField.setText("9600");
                        baudRate = 9600;
                        setUpSelectedPort(9600);
                    }
                    
       
                    if(chosenPort.openPort())
                    {
                        Connect.setText("Disconnect");
                        portList.setEnabled(false);
                        SendData.setEnabled(true);
                        TextToSend.setEnabled(true);

                        DiscriptivePortName = chosenPort.getSystemPortName() + "   " + chosenPort.getDescriptivePortName();
                        Serial_output.append("\n  ------- "+ DiscriptivePortName + "    CONNECTED :) ------------\n");
                    } else {
                    
                        Connect.setText("Refresh");
                        Serial_output.append("\n ----------- Could not Connect to selected Port :( -----------\n");
                        Serial_output.setCaretPosition(Serial_output.getText().length());
                    }
                    
                    Runnable ComScanner = () -> {
                        
                        Scanner read_from_comm = null;
                        
                        try{
                                read_from_comm = new Scanner(chosenPort.getInputStream());
                            
                                runThread = true;
                        
                                while(read_from_comm.hasNextLine() && runThread )
                                {
                                    StringBuffer textFromComm = new StringBuffer();
                            
                                    if(no_newLine.isSelected())
                                    {
                                        textFromComm.append(read_from_comm.nextLine());
                                        textFromComm.append(SeperatorField.getText());
                                    }
                                    else
                                        textFromComm.append(read_from_comm.nextLine() + "\n");
                            
                                    Serial_output.append(textFromComm.toString());
                            
                                    Serial_output.setCaretPosition(Serial_output.getText().length());
                            
                                    AutoSave(Serial_output.getText(),".\\SerialOUT.TXT");
                                    
                                    
                                }
                                
                                read_from_comm.close();
                            
                            } 
                                catch(Exception e)
                                {
                                    Serial_output.append("\n ----------- Could not get input stream !! -----------\n" + e.toString());
                                    Serial_output.setCaretPosition(Serial_output.getText().length());
                                    Connect.setText("Refresh");
                                }
  
                    
                    };
                    
                    Thread scanCom = new Thread(ComScanner);
                    
                    scanCom.start();
                   
                } else if(Connect.getText().equals("Disconnect"))
                {
                    runThread = false;
                  
                    chosenPort.closePort();
                    Connect.setText("Connect");
                    portList.setEnabled(true);
                    SendData.setEnabled(false);
                    TextToSend.setEnabled(false);
                    
                }
            
            };
            
            class Key_Pressed implements KeyListener
            {

                public Key_Pressed() {}
    
                @Override
                public void keyTyped(KeyEvent e){;}


                @Override
                public void keyPressed(KeyEvent e){;}
                    

                @Override
                public void keyReleased(KeyEvent e)
                {
    
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {                
                        int size = TextToSend.getText().getBytes().length;
                        byte[] toSend = new byte[size];
                        toSend = TextToSend.getText().getBytes();
                        chosenPort.writeBytes(toSend, size);
                        
                        if(clearAfterSend.isSelected())
                        {
                            TextToSend.setText("");
                        }
                        
                    }
    
                }
    
            }
            
            
            TextToSend.addKeyListener(new Key_Pressed());
            Connect.addActionListener(button_action);
            Serial_output.setLineWrap(true);
            
            MainControl.add(portList);
            MainControl.add(Connect);            
            MainControl.add(no_newLine);
            MainControl.add(clearAfterSend);
            
            SubControl.add(BaudRateLabel);
            SubControl.add(BaudRateField);
            SubControl.add(SeperatorLabel);
            SubControl.add(SeperatorField);
            
            TopPanel.add(MainControl);
            TopPanel.add(SubControl);

            BottomPanel.add(lbl_bottom);
            BottomPanel.add(TextToSend);
            BottomPanel.add(SendData);
            
            
            MainWindow.add(TopPanel, BorderLayout.NORTH);
            MainWindow.add(scrollPane, BorderLayout.CENTER);
            MainWindow.add(BottomPanel, BorderLayout.SOUTH);
            
            updateTitleBar();
            MainWindow.setLocation(ScreenWidth/3, ScreenHeight/3);
            MainWindow.setVisible(true);
            
        }
    
    public void updateTitleBar()
    {
        MainWindow.setTitle(MainWindowTitle + NumberOfPorts);
    }
    
    public void setUpSelectedPort(int baud)
    {
        if (baud < 100 || baud > 256000)
        {
            Serial_output.append("\n ----------- INVALID BAUD -----------\n");
            Serial_output.append("\n ----------- Starting Port with Default Baud Rate - 9600 -----------\n");
            baud = 9600;
        
        }
        
        chosenPort.setBaudRate(baud);
    }
    
    public static void AutoSave(String textToAppend, String Target)
    {
        try {
            FileWriter f = new FileWriter(Target);
            BufferedWriter f_write = new BufferedWriter(f);
            
            f_write.append(textToAppend);
            
            f_write.close();
            f.close();
            
            
        } catch (IOException ex) {
           
        }
        
        
    }
}
