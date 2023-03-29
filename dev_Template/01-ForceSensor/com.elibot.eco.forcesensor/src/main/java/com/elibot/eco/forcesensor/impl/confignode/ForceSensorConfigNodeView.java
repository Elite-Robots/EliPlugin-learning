package com.elibot.eco.forcesensor.impl.confignode;

import java.util.ResourceBundle;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.event.MouseAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import com.elibot.eco.forcesensor.impl.common.EXmlRpcClient;
import com.elibot.eco.forcesensor.impl.common.SensorDataPanel;
import com.elibot.eco.forcesensor.impl.common.SerialConnectParamsPanel;
import com.elibot.eco.forcesensor.impl.resource.ImageHelper;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationViewAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.SwingConfigurationNodeView;
import cn.elibot.robot.plugin.ui.SwingService;

public class ForceSensorConfigNodeView implements SwingConfigurationNodeView<ForceSensorConfigNodeContribution> {

    private ResourceBundle i18n;
    private ConfigurationViewAPIProvider viewApiProvider;

    private SensorDataPanel sensorDataPanel = new SensorDataPanel();
    public SerialConnectParamsPanel serialConnectParamsPanel = new SerialConnectParamsPanel(115200, 8, 1, "E");

    private JPanel monitorEnableBtn;

    private final Dimension DEFAULT_HORI_GAP = new Dimension(5, 0);
    private final Dimension DEFAULT_VERT_GAP = new Dimension(0, 5);

    private EXmlRpcClient xmlClient = new EXmlRpcClient(9104);
    private MonitorThread monitorThread;

    public ForceSensorConfigNodeView(ConfigurationViewAPIProvider viewApiProvider, ResourceBundle i18n) {
        this.viewApiProvider = viewApiProvider;
        this.i18n = i18n;
    }

    @Override
    public void buildUI(JPanel viewPanel, ForceSensorConfigNodeContribution contribution) {
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
        viewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        viewPanel.add(topLayout());
        viewPanel.add(bottomLayout());
        listenerInit();
        this.monitorEnableBtn.setEnabled(false);
    }
    
    private Box topLayout() {
        Box horiBox = Box.createHorizontalBox();
        // horiBox.setBorder(BorderFactory.createLineBorder(Color.red));
        horiBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        horiBox.add(serialConnectParamsPanel.show());
        horiBox.add(sensorDataPanel.show());
        horiBox.add(createSettingsPanel());
        return horiBox;
    }

    private Box bottomLayout() {
        Box horiBox = Box.createHorizontalBox();
        horiBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        Box imgBox = createImg("logo.png", 1.5);
        imgBox.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        horiBox.add(imgBox);
        return horiBox;
    }

    private JPanel createSettingsPanel() {

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.red));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.setBorder(new TitledBorder(new EtchedBorder(), this.i18n.getString("settingPanelName")));
        panel.add(createMonitorSwitchBtn());
        panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        return panel;
    }

    private Box createMonitorSwitchBtn() {
        Box horiBox = Box.createHorizontalBox();
        horiBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        horiBox.add(new JLabel(this.i18n.getString("monitorBtnName")));
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        monitorEnableBtn = SwingService.switchButtonService.createSwitchButton(new Dimension(60, 30));
        monitorEnableBtn.setPreferredSize(new Dimension(60, 30));
        monitorEnableBtn.setMaximumSize(new Dimension(60, 30));
        monitorEnableBtn.setMinimumSize(new Dimension(60, 30));
        horiBox.add(monitorEnableBtn);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        return horiBox;
    }

    public static Box createImg(String imgName, Double scale) {
        Box horiBox = Box.createHorizontalBox();
        // horiBox.setBorder(BorderFactory.createLineBorder(Color.red));
        horiBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        horiBox.add(Box.createHorizontalGlue());
        Box vertBox = Box.createVerticalBox();
        vertBox.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        vertBox.add(Box.createVerticalGlue());
        ImageIcon img = ImageHelper.loadImage(imgName);
        if (img != null) {
            JLabel imgLabel = new JLabel();
            int newWidth = (int) Math.round(img.getIconWidth() * scale);
            int newLength = (int) Math.round(img.getIconHeight() * scale);
            imgLabel.setIcon(new ImageIcon(img.getImage().getScaledInstance(newWidth, newLength, Image.SCALE_SMOOTH)));
            vertBox.add(imgLabel);
        }
        horiBox.add(vertBox);
        return horiBox;
    }

    public void listenerInit() {
        this.serialConnectParamsPanel.connectBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent arg0) {
                if (serialConnectParamsPanel.getConnectStatus() == false) {
                        
                    String serialName = (String) serialConnectParamsPanel.devComboBox.getSelectedItem();
                    int baudrate = (int) serialConnectParamsPanel.baudrateComboBox.getSelectedItem();
                    int dataBits = (int) serialConnectParamsPanel.dataBitsComboBox.getSelectedItem();
                    int stopBits = (int) serialConnectParamsPanel.stopBitsComboBox.getSelectedItem();
                    String parity = (String) serialConnectParamsPanel.parityComboBox.getSelectedItem();
                    Boolean setParamsResp = (Boolean) xmlClient
                            .request("setParams", serialName, baudrate, parity, stopBits, dataBits).get(0);
                    if (setParamsResp) {
                        Boolean connectResp = (Boolean) xmlClient.request("connect").get(0);
                        if (connectResp) {
                            serialConnectParamsPanel.switchConnectStatus(true);
                            monitorEnableBtn.setEnabled(true);
                        }

                    }

                } else {
                    Boolean disConnectResp = (Boolean) xmlClient.request("disconnect").get(0);
                    if (disConnectResp) {
                        serialConnectParamsPanel.switchConnectStatus(false);
                        monitorEnableBtn.setEnabled(false);
                    }
                }
            }
        });
        monitorEnableBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent arg0) {
                if (monitorEnableBtn.isEnabled()){

                    if (SwingService.switchButtonService.fetchSwitchState(monitorEnableBtn)) {
                        Boolean monitor = (Boolean) xmlClient.request("monitor", true).get(0);
                        if (monitor) {
                            monitor(true);
                        }
                    } else {
                        monitor(false);
                        Boolean monitor = (Boolean) xmlClient.request("monitor", false).get(0);
                    }
                }
            }
        });
    }

    private void monitor(boolean enable) {
        if (enable) {
            this.monitorThread = new MonitorThread();
            this.monitorThread.start();
        } else {
            if (this.monitorThread.isAlive()) {
                this.monitorThread.stopThread();
            }
        }
        this.serialConnectParamsPanel.connectBtn.setEnabled(!enable);
    }

    class MonitorThread extends Thread {

        private boolean loop = true;
        private Socket client;

        public void stopThread() {
            try {
                this.loop = false;
                this.client.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                this.client = new Socket("127.0.0.1", 9105);
                while (this.loop) {
                    System.out.println("-------");
                    InputStream inFromServer = client.getInputStream();
                    BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(inFromServer, "UTF-8"));
                    String data = mBufferedReader.readLine();
                    String[] sensorData = data.split(",");
                    if (sensorData.length == 6 && sensorData[0] != "") {
                        System.out.println("服务器响应： " + data);
                        sensorDataPanel.setTorque(Double.parseDouble(sensorData[0]), Double.parseDouble(sensorData[1]),
                                Double.parseDouble(sensorData[2]), Double.parseDouble(sensorData[3]),
                                Double.parseDouble(sensorData[4]), Double.parseDouble(sensorData[5]));
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } 
            try {
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
