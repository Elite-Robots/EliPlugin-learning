package com.elibot.eco.forcesensor.impl.taskNode;

import java.awt.*;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import cn.elibot.robot.plugin.contribution.task.SwingTaskNodeView;
import cn.elibot.robot.plugin.contribution.task.TaskNodeViewApiProvider;

public class EnableFTDTaskNodeview implements SwingTaskNodeView<EnableFTDTaskNodeContribution>{

    private ResourceBundle i18n;
    private EnableFTDTaskNodeContribution contribution;

    private final ButtonGroup bGroup = new ButtonGroup();
    private JRadioButton enableRadioBtn = new JRadioButton();
    private JRadioButton disEnableRadioBtn = new JRadioButton();

    private final Dimension DEFAULT_VERT_GAP = new Dimension(0, 5);

    public EnableFTDTaskNodeview(TaskNodeViewApiProvider apiProvider,ResourceBundle i18n){
        this.i18n = i18n;
    }


    @Override
    public void buildUI(JPanel jPanel, EnableFTDTaskNodeContribution contribution) {
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jPanel.add(new JLabel(i18n.getString("taskNodeAnnotate")));
        jPanel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        jPanel.add(newRadioBtn(enableRadioBtn, i18n.getString("enableTaskRadioBtn")));
        jPanel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        jPanel.add(newRadioBtn(disEnableRadioBtn, i18n.getString("disableTaskRadioBtn")));
        jPanel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.initListener();
    }
    

    public void updataView(EnableFTDTaskNodeContribution contribution){
        this.contribution = contribution;
        this.enableRadioBtn.setSelected(this.contribution.getFTDEnable());
        this.disEnableRadioBtn.setSelected(!this.contribution.getFTDEnable());
    }

    private Box newRadioBtn(JRadioButton radioButton, String text) {
        Box box = Box.createHorizontalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        radioButton.setText(text);
        this.bGroup.add(radioButton);
        box.add(radioButton);
        return box;
    }

    private void initListener(){
        this.enableRadioBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enableRadioBtn.isSelected()){
                    contribution.setFTDEnable(true);
                }
            }});
        this.disEnableRadioBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (disEnableRadioBtn.isSelected()){
                    contribution.setFTDEnable(false);
                }
            }});
    }


}
