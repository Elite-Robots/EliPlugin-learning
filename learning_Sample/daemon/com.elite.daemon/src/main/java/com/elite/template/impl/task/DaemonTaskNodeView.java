package com.elite.template.impl.task;

import cn.elibot.robot.plugin.contribution.task.SwingTaskNodeView;
import cn.elibot.robot.plugin.contribution.task.TaskNodeViewApiProvider;
import cn.elibot.robot.plugin.ui.SwingService;
import cn.elibot.robot.plugin.ui.model.BaseKeyboardCallback;
import com.elite.template.impl.resource.ResourceSupport;
import com.elite.template.impl.style.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DaemonTaskNodeView implements SwingTaskNodeView<DaemonTaskNodeContribution> {
    private final Style style;
    private DaemonTaskNodeContribution contribution;
    private JTextField widthField = new JTextField();
    private JTextField speedField = new JTextField();
    private JTextField forceField = new JTextField();
    private JLabel currentWidthLabel = new JLabel("-");
    private JLabel currentSpeedLabel = new JLabel("-");
    private JLabel currentForceLabel = new JLabel("-");

    public DaemonTaskNodeView(TaskNodeViewApiProvider viewApiProvider, Style style) {
        this.style = style;

    }

    @Override
    public void buildUI(JPanel jPanel, DaemonTaskNodeContribution daemonTaskNodeContribution) {
        this.contribution = daemonTaskNodeContribution;

        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.add(style.createSpace(style.getHorizontal0(), style.getVertical10()));
        jPanel.add(style.createHeadDescription(ResourceSupport.getResourceBundle().getString("Setting")));
        jPanel.add(createSetting(this.widthField, "Width", "width", 0, 1000));
        jPanel.add(createSetting(this.speedField, "Speed", "speed", 0, 1000));
        jPanel.add(createSetting(this.forceField, "Force", "force", 0, 1000));
        Box horizontalBox = Box.createHorizontalBox();
        JButton executeBtn = new JButton(ResourceSupport.getResourceBundle().getString("Execute"));
        style.setFixedSize(executeBtn, style.getButtonSize120());
        executeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                contribution.execute();

            }
        });
        JButton initGripperBtn = new JButton(ResourceSupport.getResourceBundle().getString("Init_Gripper"));
        style.setFixedSize(initGripperBtn, style.getButtonSize120());
        initGripperBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                contribution.initGripper();

            }
        });
        horizontalBox.add(executeBtn);
        horizontalBox.add(style.createSpace(style.getHorizontal5(), style.getVertical0()));
        horizontalBox.add(initGripperBtn);
        horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        jPanel.add(horizontalBox);

        jPanel.add(style.createSpace(style.getHorizontal0(), style.getVertical40()));
        jPanel.add(style.createHeadDescription(ResourceSupport.getResourceBundle().getString("Feedback")));
        jPanel.add(createFeedback(this.currentWidthLabel, "Current_Width"));
        jPanel.add(createFeedback(this.currentSpeedLabel, "Current_Speed"));
        jPanel.add(createFeedback(this.currentForceLabel, "Current_Force"));
    }

    private Box createFeedback(JLabel labelComponent, String name) {
        Box verticalBox = Box.createVerticalBox();
        Box horizontalBox = Box.createHorizontalBox();
        JLabel jLabel = new JLabel(ResourceSupport.getResourceBundle().getString(name));
        style.setFixedSize(jLabel, style.getLabelSize100());
        horizontalBox.add(jLabel);
        style.setFixedSize(labelComponent, style.getLabelSize100());
        horizontalBox.add(labelComponent);
        horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        verticalBox.add(horizontalBox);
        return verticalBox;
    }

    private Box createSetting(JTextField fieldComponent, String labelText, String key, Integer min, Integer max) {
        Box verticalBox = Box.createVerticalBox();
        Box horizontalBox = Box.createHorizontalBox();
        JLabel jLabel = new JLabel(ResourceSupport.getResourceBundle().getString(labelText));
        style.setFixedSize(jLabel, style.getLabelSize120());
        style.setFixedSize(fieldComponent, style.getTextFieldSize100());
        fieldComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                SwingService.keyboardService.showNumberKeyboard(fieldComponent, contribution.getData(key), min, max, new BaseKeyboardCallback() {
                    @Override
                    public void onOk(Object value) {
                        contribution.setData(key, Integer.valueOf((String) value));
                    }
                });
            }
        });
        horizontalBox.add(jLabel);
        horizontalBox.add(fieldComponent);
        horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        verticalBox.add(horizontalBox);
        verticalBox.add(style.createSpace(style.getHorizontal0(), style.getVertical5()));
        return verticalBox;
    }

    public void updateView(DaemonTaskNodeContribution daemonTaskNodeContribution) {
        this.contribution = daemonTaskNodeContribution;

        // restore
        this.widthField.setText(String.valueOf(contribution.getData("width")));
        this.speedField.setText(String.valueOf(contribution.getData("speed")));
        this.forceField.setText(String.valueOf(contribution.getData("force")));

    }

    /**
     *  refresh view by gripper status
     * @param status
     */
    public void refresh(Integer[] status) {
        this.currentWidthLabel.setText(String.valueOf(status[0]));
        this.currentForceLabel.setText(String.valueOf(status[1]));
        this.currentSpeedLabel.setText(String.valueOf(status[2]));
    }
}
