package com.elibot.eco.forcesensor.impl.common;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.elibot.eco.forcesensor.impl.resource.ResourceSupport;

import cn.elibot.robot.plugin.domain.system.toolio.ToolIoInterfaceLocker;
import cn.elibot.robot.plugin.domain.system.toolio.ToolIoInterfaceLockerEvent;
import cn.elibot.robot.plugin.domain.system.toolio.ToolIoLockerInterface;
import cn.elibot.robot.plugin.domain.system.toolio.domain.AnalogDomain;
import cn.elibot.robot.plugin.domain.system.toolio.domain.BaudRate;
import cn.elibot.robot.plugin.domain.system.toolio.domain.Parity;
import cn.elibot.robot.plugin.domain.system.toolio.domain.StopBits;
import cn.elibot.robot.plugin.domain.system.toolio.domain.ToolAnalogIoWorkMode;
import cn.elibot.robot.plugin.domain.system.toolio.domain.ToolCommConfig;
import cn.elibot.robot.plugin.domain.system.toolio.domain.ToolDigitalIo;
import cn.elibot.robot.plugin.domain.system.toolio.domain.ToolDigitalIoWorkMode;
import cn.elibot.robot.plugin.domain.system.toolio.domain.ToolVoltage;
import cn.elibot.robot.plugin.ui.SwingService;
import cn.elibot.robot.plugin.ui.model.FontLibrary;
import cn.elibot.robot.plugin.ui.model.MessageType;

public class SerialConnectParamsPanel {

    private int locale = ResourceSupport.getLocaleProvider().getLocale().toString().equals("en") ? 0 : 1;
    private final String[] PANEL_NAME = { "Serial Connect Params:", "串口连接参数" };
    private final String[] DEV_LISTS_NAME = { "Port      ", "串口号" };
    private final String[] PARITY_NAME = { "Parity    ", "校验位" };
    private final String[] BAUDRATE_NAME = { "Baudrate", "波特率" };
    private final String[] STOPBITS_NAME = { "StopBits", "停止位" };
    private final String[] DATABITS_NAME = { "DataBits", "数据位" };
    private final String[] CONNECT_NAME = { " Connect ", " 连 接 " };
    private final String[] DISCONNECT_NAME = { "DisConnect", "断开连接" };
    private final String[] CONNECTED_LABEL = { " Connected ", " 已连接 " };
    private final String[] DISCONNECTED_LABEL = { "Not Connected ", " 未连接 " };
    private final String[] REFRESH_NAME = { "Refresh", "刷新" };
    private final String[] TCI_SELECT_TIP = { "Please select the corresponding user Tool IO Locker in Configuration-Tool IO", "请在配置-工具IO中选择对应的用户Tool IO Locker" };

    private final Dimension DEFAULT_COMBOBOX_SIZE = new Dimension(120, 30);
    private final Dimension DEFAULT_LABEL_SIZE = new Dimension(70, 30);
    private final Dimension DEFAULT_HORI_GAP = new Dimension(5, 0);
    private final Dimension DEFAULT_VERT_GAP = new Dimension(0, 5);

    private final String RS485 = "RS485";
    private final String RS485_DEV_FLAG = "ELITE";
    private final String TCI = "TCI";
    private final String TCI_DEV_FLAG = "TCI";
    private final Integer[] baudrate = { 2400, 4800, 9600, 19200, 38400, 57600, 115200, 460800, 1000000, 2000000 };
    private final String[] parity = { "N", "O", "E" };
    private final Integer[] stopBits = { 1, 2 };
    private final Integer[] dataBits = { 7, 8 };

    private JLabel connnectStatusLabel = new JLabel(this.DISCONNECTED_LABEL[locale]);
    private boolean connnectStatus = false;

    private JPanel panel = new JPanel();
    public JComboBox<String> devComboBox = new JComboBox<String>(findDevPortList());
    public JComboBox<Integer> baudrateComboBox = new JComboBox<Integer>(baudrate);
    public JComboBox<Integer> dataBitsComboBox = new JComboBox<Integer>(dataBits);
    public JComboBox<String> parityComboBox = new JComboBox<String>(parity);
    public JComboBox<Integer> stopBitsComboBox = new JComboBox<Integer>(stopBits);
    public JButton connectBtn = new JButton(this.CONNECT_NAME[locale]);

    private TCILocker tciLocker = new TCILocker();

    public SerialConnectParamsPanel() {
        this.panelUI();
    }

    public SerialConnectParamsPanel(int baudrate, int dataBits, int stopBits, String parity) {
        this.panelUI();
        this.setDefaultConnectParams(baudrate, dataBits, stopBits, parity);
    }

    private void panelUI() {
        this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
        this.panel.setAlignmentY(Component.TOP_ALIGNMENT);
        this.panel.setBorder(new TitledBorder(new EtchedBorder(), this.PANEL_NAME[locale]));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(this.createComboBox(this.DEV_LISTS_NAME[locale], devComboBox));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(this.createComboBox(this.BAUDRATE_NAME[locale], baudrateComboBox));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(this.createComboBox(this.DATABITS_NAME[locale], dataBitsComboBox));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(this.createComboBox(this.PARITY_NAME[locale], parityComboBox));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(this.createComboBox(this.STOPBITS_NAME[locale], stopBitsComboBox));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(createConnectBtnBox());
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.defaultSerialSetting();
    }

    
    private void defaultSerialSetting() {
        /**
         * Set Serial Panel Default Connect Params & Add ActionListener to devComboBox
         * 
         */
        int baudRate = 9600;
        int parity = 8;
        int stopBits = 1;
        this.baudrateComboBox.setSelectedItem(baudRate);
        this.dataBitsComboBox.setSelectedItem(parity);
        this.stopBitsComboBox.setSelectedItem(stopBits);
        this.parityComboBox.setSelectedItem("N");
        this.devComboBox.setSelectedIndex(-1);
        this.tciLocker.setSerialCommConfig(baudRate, this.parityComboBox.getSelectedIndex(), stopBits);
        this.devComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (devComboBox.getSelectedItem().toString().equals(REFRESH_NAME[locale])) {
                    // refresh serial device
                    int removeItemsNum = devComboBox.getItemCount();
                    for (int i = 1; i < removeItemsNum; i++) {
                        devComboBox.removeItemAt(1);
                    }
                    String[] addItems = findDevPortList();
                    for (int j = 1; j < addItems.length; j++) {
                        devComboBox.addItem(addItems[j]);
                    }
                    devComboBox.setSelectedIndex(-1);
                    switchAllComboBox(true);

                } else if (devComboBox.getSelectedItem().toString().equals(TCI_DEV_FLAG)){
                    // TCI Tip 
                    int baudRate = Integer.parseInt(baudrateComboBox.getSelectedItem().toString());
                    int parityIndex = parityComboBox.getSelectedIndex();
                    int stopBits = Integer.parseInt(stopBitsComboBox.getSelectedItem().toString());
                    tciLocker.setSerialCommConfig(baudRate, parityIndex, stopBits);
                    switchAllComboBox(false);
                    SwingService.messageService.showMessage(TCI_DEV_FLAG, TCI_SELECT_TIP[locale], MessageType.INFO);
                }
                
            }
        });
    }

    public boolean setDefaultConnectParams(int baudrate, int dataBits, int stopBits, String parity){
        /**
         * You Can Set Default Connect Params After new SerialConnectParamsPanel()
         */
        boolean isValidBaudrate = Arrays.asList(this.baudrate).contains(baudrate);
        boolean isValidDataBits = Arrays.asList(this.dataBits).contains(dataBits);
        boolean isValidStopBits = Arrays.asList(this.stopBits).contains(stopBits);
        boolean isValidParity = Arrays.asList(this.parity).contains(parity);
        if (isValidBaudrate && isValidDataBits && isValidParity && isValidStopBits){
            this.baudrateComboBox.setSelectedItem(baudrate);
            this.dataBitsComboBox.setSelectedItem(dataBits);
            this.stopBitsComboBox.setSelectedItem(stopBits);
            this.parityComboBox.setSelectedItem(parity);
            return true;
        }
        return false;
    }

    public ArrayList<Object> getConnectParams(){
        ArrayList<Object> params = new ArrayList<Object>();
        int baudRate = Integer.parseInt(this.baudrateComboBox.getSelectedItem().toString());
        int dataBits = Integer.parseInt(this.dataBitsComboBox.getSelectedItem().toString());
        int stopBits = Integer.parseInt(this.stopBitsComboBox.getSelectedItem().toString());
        String parity = this.parityComboBox.getSelectedItem().toString();
        params.add(baudRate);
        params.add(dataBits);
        params.add(stopBits);
        params.add(parity);
        return params;
    }



    private Box createComboBox(String label,JComboBox comboBox){
        /**
         * create a base box that contains a label and a comboBox
         * 
         */
        Box horiBox = Box.createHorizontalBox();
        horiBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel name = new JLabel(label);
        name.setFont(FontLibrary.NORMAL_FONT);
        name.setMinimumSize(DEFAULT_LABEL_SIZE);
        name.setMaximumSize(DEFAULT_LABEL_SIZE);
        comboBox.setMinimumSize(DEFAULT_COMBOBOX_SIZE);
        comboBox.setMaximumSize(DEFAULT_COMBOBOX_SIZE);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        horiBox.add(name);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        horiBox.add(comboBox);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        return horiBox;
    }


    private Box createConnectBtnBox() {
        Box horiBox = Box.createHorizontalBox();
        horiBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        stopBitsComboBox.setMinimumSize(DEFAULT_COMBOBOX_SIZE);
        stopBitsComboBox.setMaximumSize(DEFAULT_COMBOBOX_SIZE);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        SwingService.buttonUiService.setPrimaryUi(connectBtn);
        horiBox.add(connectBtn);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        horiBox.add(connnectStatusLabel);
        return horiBox;
    }

    public void switchConnectStatus(boolean status) {
        /**
         * Switch connectStatus and modify style
         */
        this.connnectStatus = status;
        this.devComboBox.setEnabled(!status);
        this.switchAllComboBox(!status);
        if (!this.connnectStatus) {
            this.connectBtn.setText(this.CONNECT_NAME[locale]);
            SwingService.buttonUiService.setPrimaryUi(connectBtn);
            this.connnectStatusLabel.setText(this.DISCONNECTED_LABEL[locale]);
        } else {
            this.connectBtn.setText(this.DISCONNECT_NAME[locale]);
            SwingService.buttonUiService.setDangerUi(connectBtn);
            this.connnectStatusLabel.setText(this.CONNECTED_LABEL[locale]);
        }
    }

    private void switchAllComboBox(boolean enable){
        /**
         * enable the all comboBox
         */
        this.baudrateComboBox.setEnabled(enable);
        this.dataBitsComboBox.setEnabled(enable);
        this.parityComboBox.setEnabled(enable);
        this.stopBitsComboBox.setEnabled(enable);
    }


    public boolean getConnectStatus(){
        /**
         * get the connect status
         */
        return this.connnectStatus;
    }


    private String[] findDevPortList() {
        /**
         * find all serial port by a process(ls /dev | grep "tty")
         */
        String[] commands = { "/bin/sh", "-c", "ls /dev | grep \"tty\" " };
        List<String> devLists = new ArrayList<String>();
        devLists.add(REFRESH_NAME[locale]);
        // devLists.add("TCI");
        // devLists.add("TEST2");
        // devLists.add("TEST3");
        // devLists.add("COM8");
        try {
            Process process = Runtime.getRuntime().exec(commands);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("USB") != -1) {
                    System.out.println(line);
                    devLists.add(line.substring(3));
                } else if (line.indexOf(RS485_DEV_FLAG) != -1) {
                    System.out.println(line);
                    devLists.add(RS485);
                } else if (line.indexOf(TCI_DEV_FLAG) != -1) {
                    System.out.println(line);
                    devLists.add(TCI);
                }
            }
            // waitFor 阻塞等待 异步进程结束，并返回执行状态，0代表命令执行正常结束。
            process.waitFor();
            // System.out.println(process.waitFor());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Find All Serial Dev:"+devLists);
        return devLists.toArray(new String[devLists.size()]);
    }

    public JPanel show(){
        return this.panel;
    }

    public ToolIoLockerInterface getTCILocker(){
        /** 
         * get inner TCILocker
         */
        return this.tciLocker;
    }


    class TCILocker implements ToolIoLockerInterface {
        /**
         * serial connect panel default TCILocker
         */
        private BaudRate baudRate;
        private Parity parity;
        private StopBits stopBits;
        private ToolIoInterfaceLocker toolIOControllerLocker;


        public void setSerialCommConfig(int baudRate, int parity, int stopBits){
            /**
             * set the TCILocker() serial config params
             */
            this.baudRate = BaudRate.byId(baudRate);
            this.parity = Parity.byId(parity);
            this.stopBits = StopBits.byId(stopBits);
            if (this.toolIOControllerLocker != null && this.toolIOControllerLocker.hasLocked()){
                ToolCommConfig toolCommConfig = new ToolCommConfig(true, this.baudRate, this.parity, this.stopBits, Boolean.FALSE);
                toolIOControllerLocker.setToolCommConfig(toolCommConfig);
            }
        }

        @Override
        public void onLockGranted(ToolIoInterfaceLockerEvent toolIoInterfaceLockerEvent) {
            this.toolIOControllerLocker = toolIoInterfaceLockerEvent.getInterfaceLockerResource();
            toolIOControllerLocker.setToolVoltage(ToolVoltage.TOOL_VOLTAGE_24V);
            ToolCommConfig toolCommConfig = new ToolCommConfig(true, this.baudRate, this.parity, this.stopBits, Boolean.FALSE);
            toolIOControllerLocker.setToolCommConfig(toolCommConfig);
            toolIOControllerLocker.setToolAnalogInputDomain(AnalogDomain.VOLTAGE);
            toolIOControllerLocker.setToolAnalogOutputDomain(AnalogDomain.VOLTAGE);
            toolIOControllerLocker.setToolAnalogIoWorkMode(ToolAnalogIoWorkMode.USART_MODE);
            toolIOControllerLocker.setToolDigitalIoWorkMode(ToolDigitalIoWorkMode.SINGLE_PIN_MODE);
            for (int i = 0; i < 4; i++) {
                toolIOControllerLocker.setToolDigitalIoConfig(i, new ToolDigitalIo());
            }
        }

        @Override
        public void onLockToBeRevoked(ToolIoInterfaceLockerEvent toolIoInterfaceLockerEvent) {

        }
        
    }
}
