package com.EliteRobot.arcBase.impl.task;

import cn.elibot.robot.plugin.contribution.task.SwingTaskNodeView;
import cn.elibot.robot.plugin.contribution.task.TaskNodeViewApiProvider;
import cn.elibot.robot.plugin.ui.SwingService;
import cn.elibot.robot.plugin.ui.model.BaseKeyboardCallback;

import com.EliteRobot.arcBase.impl.resource.ResourceSupport;
import com.EliteRobot.arcBase.impl.configuration.arcConfigContribution;

//  public DHPgcConfigurationNodeContribution getInstallation() {
//        return configurationAPIProvider.getConfigurationNode(DHPgcConfigurationNodeContribution.class);
//    }

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class arcTaskView implements SwingTaskNodeView<arcTaskContribution> {

    private JComboBox<String> arcInstructionBox = new JComboBox<String>();

    private JLabel testLabel = new JLabel(ResourceSupport.getDefaultResourceBundle().getString("switch_off"));
    private boolean bRealWeld = false;

    private JButton Gas = new JButton(ResourceSupport.getDefaultResourceBundle().getString("Gas"));
    private JButton WireFwd = new JButton(ResourceSupport.getDefaultResourceBundle().getString("WireFwd"));
    private JButton WireBwd = new JButton(ResourceSupport.getDefaultResourceBundle().getString("WireBwd"));

    private TaskNodeViewApiProvider viewApiProvider;
    private arcTaskContribution contribution;

    private JTextField current = new JTextField();
    private JTextField voltage = new JTextField();
    private JCheckBox realWeld;


    public arcTaskView(TaskNodeViewApiProvider viewApiProvider) {
        this.viewApiProvider = viewApiProvider;

    }

    @Override
    public void buildUI(JPanel jPanel, arcTaskContribution contribution) {
        this.contribution = contribution;
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.add(createSpacer(5));

        jPanel.add(createArcInstructionComboBox(arcInstructionBox));
        jPanel.add(createSpacer(30));

        jPanel.add(createSetting2(current, "current", 30.0, 350.0));
        jPanel.add(createSpacer(10));

        jPanel.add(createSetting2(voltage, "voltage", 10.0, 40.0));
        jPanel.add(createSpacer(100));

        jPanel.add(createManualSetting());

        /*jPanel.add(createToggle());
        jPanel.add(createSpacer(70));*/
        JPanel space = new JPanel();
        space.setAlignmentX(Component.LEFT_ALIGNMENT);
        jPanel.add(space);
        jPanel.add(createCheckBox());
    }

    private Box createToggle() {
        Box box3 = Box.createHorizontalBox();
        box3.setAlignmentX(Component.LEFT_ALIGNMENT);
        box3.setPreferredSize(new Dimension(200, 60));
        box3.setMaximumSize(box3.getPreferredSize());
        JPanel s10 = SwingService.switchButtonService.createSwitchButton(new Dimension(50, 20));
        contribution.setRealWeld(bRealWeld);
        s10.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (SwingService.switchButtonService.fetchSwitchState(s10)) {
                    testLabel.setText(ResourceSupport.getDefaultResourceBundle().getString("switch_on"));
                    bRealWeld = true;
                } else {
                    testLabel.setText(ResourceSupport.getDefaultResourceBundle().getString("switch_off"));
                    bRealWeld = false;
                }
                contribution.setRealWeld(bRealWeld);
            }
        });
        box3.add(Box.createRigidArea(new Dimension(30, 0)));
        box3.add(testLabel);
        box3.add(s10);
        return box3;

        /*JPanel s3 = SwingService.switchButtonService.createSwitchButton(new Dimension(100, 40), Color.GREEN, Color.LIGHT_GRAY);
        JPanel s3 = SwingService.switchButtonService.createSwitchButton(new Dimension(100, 40));
        s3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                s3.se
            }
        }
    });
    JToggleButton s1 = new JToggleButton();*/
    }

    private JCheckBox createCheckBox() {
        realWeld = new JCheckBox(ResourceSupport.getDefaultResourceBundle().getString("switch_on"));
        realWeld.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (realWeld.isSelected()) {
                    bRealWeld = true;
                } else {
                    bRealWeld = false;
                }
                contribution.setRealWeld(bRealWeld);
            }
        });
        return realWeld;
    }

    private Box createManualSetting() {
        Box box = Box.createHorizontalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(createButton(Gas, "doGas"));
        box.add(Box.createRigidArea(new Dimension(40, 0)));
        box.add(createButton(WireFwd, "doWireFwd"));
        box.add(Box.createRigidArea(new Dimension(40, 0)));
        box.add(createButton(WireBwd, "doWireBwd"));
        return box;
    }

    private Box createArcInstructionComboBox(final JComboBox<String> combo) {
        //创建单行文字及comboBox
        Box box1 = Box.createHorizontalBox();
        box1.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel Label = new JLabel(ResourceSupport.getDefaultResourceBundle().getString("selectArcInstr"));
        setFixedSize(Label, 120, 30);
        combo.setPreferredSize(new Dimension(104, 30));
        combo.setMaximumSize(combo.getPreferredSize());
        //setFixedSize(combo, 100, 30);
        combo.addItem(ResourceSupport.getDefaultResourceBundle().getString("arcOn"));
        combo.addItem(ResourceSupport.getDefaultResourceBundle().getString("arcOff"));
        combo.addItem(ResourceSupport.getDefaultResourceBundle().getString("arcSet"));

        combo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    //testLabel.setText(String.valueOf(e.getItem()));
                    contribution.setArcInstru(String.valueOf(e.getItem()));
                }
            }
        });

        box1.add(Label);
        box1.add(Box.createRigidArea(new Dimension(5, 0)));
        box1.add(combo);
        // box.add(testLabel);
        return box1;
    }

    public void updateUI(arcTaskContribution contribution) {
        this.contribution = contribution;
        arcInstructionBox.setSelectedItem(contribution.getArcInstru());
        current.setText(String.valueOf(contribution.getDataDouble("current")));
        voltage.setText(String.valueOf(contribution.getDataDouble("voltage")));
        realWeld.setSelected(contribution.getRealWeld());
    }


    private Box createSetting2(JTextField jTextField, String name, Double min, Double max) {
        Box box = Box.createHorizontalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel jLabel = new JLabel(ResourceSupport.getDefaultResourceBundle().getString(name));
        setFixedSize(jTextField, 100, 30);
        setFixedSize(jLabel, 120, 30);
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
        box.add(jLabel);
        box.add(Box.createRigidArea(new Dimension(5, 0)));
        box.add(jTextField);
        return box;
    }

    private Box createDescription(String desc) {
        //创建单行文字描述
        Box box = Box.createHorizontalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel Label = new JLabel(desc);

        box.add(Label);
        return box;
    }

    private Box createButton(JButton button, String name) {
        //创建button
        Box box = Box.createVerticalBox();
        //box.setPreferredSize(new Dimension(50,50));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setPreferredSize(new Dimension(100, 100));
        button.setMaximumSize(button.getPreferredSize());
        button.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        Font font = new Font("宋体", Font.BOLD, 20);
        button.setFont(font);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {

                Integer num = contribution.getSignal(name);
                String s = "sec t1():\n";
                s += "  set_standard_digital_out(" + num + ",True)\n";
                s += "end\n";
                viewApiProvider.getTaskApiProvider().getRpcService().runScript(s);
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                Integer num = contribution.getSignal(name);
                String s = "sec t1():\n";
                s += "  set_standard_digital_out(" + num + ",False)\n";
                s += "end\n";
                viewApiProvider.getTaskApiProvider().getRpcService().runScript(s);
            }
        });
        box.add(button);
        box.add(createSpacer(5));
        return box;
    }


    private Component createSpacer(int height) {
        // 添加垂直空白
        Component rigidArea = Box.createRigidArea(new Dimension(0, height));
        return rigidArea;
    }

    public void setFixedSize(JComponent component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        component.setMaximumSize(new Dimension(width, height));
        component.setMinimumSize(new Dimension(width, height));
    }
}
