package com.EliteRobot.arcBase.impl.configuration;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationViewAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.SwingConfigurationNodeView;
import cn.elibot.robot.plugin.ui.SwingService;
import cn.elibot.robot.plugin.ui.model.BaseKeyboardCallback;
import cn.elibot.robot.plugin.ui.model.FontLibrary;
import com.EliteRobot.arcBase.impl.resource.ResourceSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class arcConfigView implements SwingConfigurationNodeView<arcConfigContribution> {
    private ConfigurationViewAPIProvider viewApiProvider;
    private arcConfigContribution contribution;
    private JTextField doArcOn = new JTextField(3);
    private JTextField doGas = new JTextField(3);
    private JTextField doWireFwd = new JTextField(3);
    private JTextField doWireBwd = new JTextField(3);
    private JTextField diArcEst = new JTextField(3);
    private JTextField diArcReady = new JTextField(3);

    private JTextField aoCurrent = new JTextField(3);
    private JTextField aoVoltage = new JTextField(3);

    private JTextField minCurrent = new JTextField(3);
    private JTextField aominCurrent = new JTextField(3);
    private JTextField maxCurrent = new JTextField(3);
    private JTextField aomaxCurrent = new JTextField(3);


    private JTextField minVoltage = new JTextField(3);
    private JTextField aominVoltage = new JTextField(3);
    private JTextField maxVoltage = new JTextField(3);
    private JTextField aomaxVoltage = new JTextField(3);

    private JTextField preGas = new JTextField(3);
    private JTextField arcEstTime = new JTextField(3);
    private JTextField postGas = new JTextField(3);
    private JCheckBox monitor_di_arc_ready;

    public arcConfigView(ConfigurationViewAPIProvider viewApiProvider) {
        this.viewApiProvider = viewApiProvider;

    }

    @Override
    public void buildUI(JPanel jPanel, arcConfigContribution contribution) {
        this.contribution = contribution;

        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        //jPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        // box.setAlignmentX(Component.LEFT_ALIGNMENT);
        jPanel.add(createDescription(ResourceSupport.getDefaultResourceBundle().getString("arcSignalSetting")));
        jPanel.add(createSpacer(5));
        jPanel.add(createHeader());
        jPanel.add(createSetting("DO", doArcOn, "doArcOn"));
        jPanel.add(createSetting("DO", doGas, "doGas"));
        jPanel.add(createSetting("DO", doWireFwd, "doWireFwd"));
        jPanel.add(createSetting("DO", doWireBwd, "doWireBwd"));
        jPanel.add(createSetting("DI", diArcEst, "diArcEst"));
        jPanel.add(createSettingWithCheckBox("DI", diArcReady, "diArcReady"));
        jPanel.add(createSetting("AO", aoCurrent, "current"));
        jPanel.add(createSetting("AO", aoVoltage, "voltage"));

        jPanel.add(createDescription("---------------------------------------------"));
        //jPanel.add(createDescription(ResourceSupport.getDefaultResourceBundle().getString("arcCurrentSetting")));
        jPanel.add(createBox());
        jPanel.add(createDescription("---------------------------------------------"));
        //jPanel.add(createSpacer(5));
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        /*jPanel.add(createSetting2(preGas, "preGas", 0.0, 3.0));
        jPanel.add(createSetting2(arcEstTime, "arcEstTime", 0.0, 2.0));
        jPanel.add(createSetting2(postGas, "postGas", 0.0, 3.0));*/
        horizontalBox.add(createSetting2(preGas, "preGas", 0.0, 3.0));
        horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));
        horizontalBox.add(createSetting2(arcEstTime, "arcEstTime", 0.0, 2.0));
        horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));
        horizontalBox.add(createSetting2(postGas, "postGas", 0.0, 3.0));
        jPanel.add(horizontalBox);
    }

    private Box createBox() {
        Box box = Box.createHorizontalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box box1 = Box.createVerticalBox();
        box1.setAlignmentX(Component.LEFT_ALIGNMENT);
        //box1.setAlignmentY(Component.TOP_ALIGNMENT);
        box1.setPreferredSize(new Dimension(300, 260));
        box1.add(createDescription(ResourceSupport.getDefaultResourceBundle().getString("arcCurrentSetting")));
        box1.add(createSetting2(minCurrent, "minCurrent", 0.0, 500.0));
        box1.add(createSetting2(aominCurrent, "aominCurrent", 0.0, 10.0));
        box1.add(createSetting2(maxCurrent, "maxCurrent", 0.0, 500.0));
        box1.add(createSetting2(aomaxCurrent, "aomaxCurrent", 0.0, 10.0));
        //box1.add(createSpacer(5));

        Box box2 = Box.createVerticalBox();
        box2.setAlignmentX(Component.LEFT_ALIGNMENT);
        //box1.setAlignmentY(Component.TOP_ALIGNMENT);
        box2.setPreferredSize(new Dimension(300, 260));
        box2.add(createDescription(ResourceSupport.getDefaultResourceBundle().getString("arcVoltageSetting")));
        box2.add(createSetting2(minVoltage, "minVoltage", -100.0, 100.0));
        box2.add(createSetting2(aominVoltage, "aominVoltage", -100.0, 100.0));
        box2.add(createSetting2(maxVoltage, "maxVoltage", -100.0, 100.0));
        box2.add(createSetting2(aomaxVoltage, "aomaxVoltage", -100.0, 100.0));

        box.add(box1);
        box.add(Box.createRigidArea(new Dimension(30, 0)));
        box.add(box2);
        return box;

    }

    private Box createDescription(String desc) {
        //创建单行文字描述
        Box box = Box.createHorizontalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel Label = new JLabel(desc);
        Font font = new Font("宋体", Font.BOLD, 20);
        Label.setFont(font);

        box.add(Label);
        return box;
    }

    private Box createHeader() {
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel function = new JLabel(ResourceSupport.getDefaultResourceBundle().getString("function"));
        function.setFont(FontLibrary.H4_BOLD_FONT);
        setFixedSize(function, 100, 30);
        JLabel signal_type = new JLabel(ResourceSupport.getDefaultResourceBundle().getString("signal_type"));
        signal_type.setFont(FontLibrary.H4_BOLD_FONT);
        setFixedSize(signal_type, 100, 30);
        JLabel signal_address = new JLabel(ResourceSupport.getDefaultResourceBundle().getString("signal_address"));
        signal_address.setFont(FontLibrary.H4_BOLD_FONT);
        setFixedSize(signal_address, 150, 30);

        horizontalBox.add(function);
        horizontalBox.add(Box.createRigidArea(new Dimension(30, 0)));
        horizontalBox.add(signal_type);
        horizontalBox.add(Box.createRigidArea(new Dimension(30, 0)));
        horizontalBox.add(signal_address);
        return horizontalBox;
    }

    private Box createSetting(String Type, JTextField jTextField, String name) {
        Box box1 = Box.createVerticalBox();
        box1.setAlignmentX(Component.LEFT_ALIGNMENT);
        Box box = Box.createHorizontalBox();
        if (Type == "DI") {
            Type = Type + "  ";
        }
        JLabel jLabelType = new JLabel(Type + " ");
        setFixedSize(jLabelType, 100, 30);
        JLabel jLabel = new JLabel(ResourceSupport.getDefaultResourceBundle().getString(name));
        setFixedSize(jLabel, 100, 30);
        setFixedSize(jTextField, 100, 30);
        jTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                SwingService.keyboardService.showNumberKeyboard(jTextField, contribution.getData(name), 0, 15, new BaseKeyboardCallback() {
                    @Override
                    public void onOk(Object value) {
                        if (value instanceof String) {
                            contribution.setData(name, Integer.valueOf((String) value));
                            jTextField.setText(String.valueOf(Integer.valueOf((String) value)));
                        }
                    }
                });
            }
        });
        box.add(jLabel);
        box.add(Box.createRigidArea(new Dimension(30, 0)));
        box.add(jLabelType);
        box.add(Box.createRigidArea(new Dimension(30, 0)));
        box.add(jTextField);

        box1.add(box);
        box1.add(createSpacer(5));
        return box1;
    }

    private Box createSetting2(JTextField jTextField, String name, Double min, Double max) {
        Box box1 = Box.createVerticalBox();
        box1.setAlignmentX(Component.LEFT_ALIGNMENT);
        Box box = Box.createHorizontalBox();

        JLabel jLabel = new JLabel(" " + ResourceSupport.getDefaultResourceBundle().getString(name) + " ");
        setFixedSize(jTextField, 50, 30);
        jTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

                SwingService.keyboardService.showNumberKeyboard(jTextField, contribution.getDataDouble(name), min, max, 1, new BaseKeyboardCallback() {
                    @Override
                    public void onOk(Object value) {
                        if (value instanceof String) {
                            contribution.setDataDouble(name, Double.valueOf((String) value));
                            //jTextField.setText(String.valueOf(Integer.valueOf((String) value)));
                        }
                    }
                });
            }
        });
        // Style.setFixedSize(sampleField, Style.getTextFieldSize100());
        //box.add(jLabelType);
        box.add(jTextField);
        box.add(jLabel);
        box1.add(box);
        box1.add(createSpacer(5));
        return box1;
    }

    private Box createSettingWithCheckBox(String Type, JTextField jTextField, String name) {
        Box box1 = Box.createVerticalBox();
        box1.setAlignmentX(Component.LEFT_ALIGNMENT);
        Box box = Box.createHorizontalBox();
        if (Type == "DI") {
            Type = Type + "  ";
        }
        JLabel jLabelType = new JLabel(Type + " ");
        setFixedSize(jLabelType, 100, 30);
        JLabel jLabel = new JLabel(ResourceSupport.getDefaultResourceBundle().getString(name));
        setFixedSize(jLabel, 100, 30);
        setFixedSize(jTextField, 100, 30);
        jTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                SwingService.keyboardService.showNumberKeyboard(jTextField, contribution.getData(name), 0, 15, new BaseKeyboardCallback() {
                    @Override
                    public void onOk(Object value) {
                        if (value instanceof String) {
                            contribution.setData(name, Integer.valueOf((String) value));
                            jTextField.setText(String.valueOf(Integer.valueOf((String) value)));
                        }
                    }
                });
            }
        });

        monitor_di_arc_ready = new JCheckBox(ResourceSupport.getDefaultResourceBundle().getString("Monitor_DI_Arc_Ready"));
        monitor_di_arc_ready.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (monitor_di_arc_ready.isSelected()) {
                    contribution.setData("monitor", 1);
                } else {
                    contribution.setData("monitor", 0);
                }
            }
        });
        box.add(jLabel);
        box.add(Box.createRigidArea(new Dimension(30, 0)));
        box.add(jLabelType);
        box.add(Box.createRigidArea(new Dimension(30, 0)));
        box.add(jTextField);
        box.add(Box.createRigidArea(new Dimension(5, 0)));
        box.add(monitor_di_arc_ready);

        box1.add(box);
        box1.add(createSpacer(5));
        return box1;
    }

    public void updateUI(arcConfigContribution contribution) {
        doArcOn.setText(String.valueOf(contribution.getData("doArcOn")));
        doGas.setText(String.valueOf(contribution.getData("doGas")));
        doWireFwd.setText(String.valueOf(contribution.getData("doWireFwd")));
        doWireBwd.setText(String.valueOf(contribution.getData("doWireBwd")));
        diArcEst.setText(String.valueOf(contribution.getData("diArcEst")));
        diArcReady.setText(String.valueOf(contribution.getData("diArcReady")));
        if (contribution.getData("monitor") == 1) {
            monitor_di_arc_ready.setSelected(true);
        } else {
            monitor_di_arc_ready.setSelected(false);
        }
        aoCurrent.setText(String.valueOf(contribution.getData("aoCurrent")));
        aoVoltage.setText(String.valueOf(contribution.getData("aoVoltage")));

        minCurrent.setText(String.valueOf(contribution.getDataDouble("minCurrent")));
        aominCurrent.setText(String.valueOf(contribution.getDataDouble("aominCurrent")));
        maxCurrent.setText(String.valueOf(contribution.getDataDouble("maxCurrent")));
        aomaxCurrent.setText(String.valueOf(contribution.getDataDouble("aomaxCurrent")));
        minVoltage.setText(String.valueOf(contribution.getDataDouble("minVoltage")));
        aominVoltage.setText(String.valueOf(contribution.getDataDouble("aominVoltage")));
        maxVoltage.setText(String.valueOf(contribution.getDataDouble("maxVoltage")));
        aomaxVoltage.setText(String.valueOf(contribution.getDataDouble("aomaxVoltage")));

        preGas.setText(String.valueOf(contribution.getDataDouble("preGas")));
        postGas.setText(String.valueOf(contribution.getDataDouble("postGas")));
        arcEstTime.setText(String.valueOf(contribution.getDataDouble("arcEstTime")));
    }

//    private Box createInputPara()
//    {
//        Box box = Box.createVerticalBox();
//        box.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        //doArcOn
//        Box doArcOnBox = Box.createHorizontalBox();
//        JLabel doArcOnLabel = new JLabel(ResourceSupport.getDefaultResourceBundle().getString("doArcOn"));
//
//        doArcOn = new JTextField(5);
//        doArcOn.setPreferredSize(new Dimension(104,30));
//        doArcOn.setMaximumSize(doArcOn.getPreferredSize());
//        doArcOn.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent mouseEvent) {
//                SwingService.keyboardService.showNumberKeyboard(doArcOn, contribution.getData("doArcOn"), 0, 15, new BaseKeyboardCallback() {
//                    @Override
//                    public void onOk(Object value) {
//                        if (value instanceof String) {
//                            contribution.setData("doArcOn",Integer.valueOf((String) value));
//                        }
//                    }
//                });
//            }
//        });
//        // Style.setFixedSize(sampleField, Style.getTextFieldSize100());
//        doArcOnBox.add(doArcOnLabel);
//        doArcOnBox.add(doArcOn);
//        box.add(doArcOnBox);
//        box.add(createSpacer(5));
//
//        //doGas
//        Box doGasBox = Box.createHorizontalBox();
//        JLabel doGasLabel = new JLabel(ResourceSupport.getDefaultResourceBundle().getString("doGas"));
//
//        doGas = new JTextField(5);
//        doGas.setPreferredSize(new Dimension(104,30));
//        doGas.setMaximumSize(doArcOn.getPreferredSize());
//        doGas.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent mouseEvent) {
//                SwingService.keyboardService.showNumberKeyboard(doGas, contribution.getData("doGas"), 0, 15, new BaseKeyboardCallback() {
//                    @Override
//                    public void onOk(Object value) {
//                        if (value instanceof String) {
//                            contribution.setData("doGas",Integer.valueOf((String) value));
//                        }
//                    }
//                });
//            }
//        });
//        // Style.setFixedSize(sampleField, Style.getTextFieldSize100());
//        doArcOnBox.add(doGasLabel);
//        doArcOnBox.add(doGas);
//        box.add(doGasBox);
//        box.add(createSpacer(5));
//        box.add(doGasBox);
//        box.add(createSpacer(5));
//
//        //doWireFwd
//        Box doGasBox = Box.createHorizontalBox();
//        JLabel doGasLabel = new JLabel(ResourceSupport.getDefaultResourceBundle().getString("doGas"));
//
//        doGas = new JTextField(5);
//        doGas.setPreferredSize(new Dimension(104,30));
//        doGas.setMaximumSize(doArcOn.getPreferredSize());
//        doGas.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent mouseEvent) {
//                SwingService.keyboardService.showNumberKeyboard(doGas, contribution.getData("doGas"), 0, 15, new BaseKeyboardCallback() {
//                    @Override
//                    public void onOk(Object value) {
//                        if (value instanceof String) {
//                            contribution.setData("doGas",Integer.valueOf((String) value));
//                        }
//                    }
//                });
//            }
//        });
//        // Style.setFixedSize(sampleField, Style.getTextFieldSize100());
//        doArcOnBox.add(doGasLabel);
//        doArcOnBox.add(doGas);
//        box.add(doGasBox);
//        box.add(createSpacer(5));
//        box.add(doGasBox);
//        box.add(createSpacer(5));
//
//        return box;
//
//    }

    private Component createSpacer(int height) {
        // 添加垂直空白
        return Box.createRigidArea(new Dimension(0, height));
    }

    public void setFixedSize(JComponent component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        component.setMaximumSize(new Dimension(width, height));
        component.setMinimumSize(new Dimension(width, height));
    }
}
