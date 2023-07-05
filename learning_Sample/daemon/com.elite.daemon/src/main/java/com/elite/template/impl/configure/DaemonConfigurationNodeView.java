package com.elite.template.impl.configure;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationViewAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.SwingConfigurationNodeView;
import cn.elibot.robot.plugin.ui.SwingService;
import cn.elibot.robot.plugin.ui.model.BaseKeyboardCallback;
import com.elite.template.impl.resource.ResourceSupport;
import com.elite.template.impl.style.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DaemonConfigurationNodeView implements SwingConfigurationNodeView<DaemonConfigurationNodeContribution> {
    private ConfigurationViewAPIProvider viewApiProvider;
    private Style style;
    private DaemonConfigurationNodeContribution contribution;
    private JTextField widthField = new JTextField();

    public DaemonConfigurationNodeView(ConfigurationViewAPIProvider viewApiProvider, Style style) {
        this.viewApiProvider = viewApiProvider;
        this.style = style;
    }


    public DaemonConfigurationNodeView() {
    }

    @Override
    public void buildUI(JPanel viewPanel, DaemonConfigurationNodeContribution daemonConfigurationNodeContribution) {
        this.contribution = daemonConfigurationNodeContribution;

        viewPanel.setLayout(new BorderLayout());
        viewPanel.add(new JLabel(ResourceSupport.getResourceBundle().getString("Test_String")));
    }


    public void updateView(DaemonConfigurationNodeContribution daemonConfigurationNodeContribution) {
        this.contribution = daemonConfigurationNodeContribution;
    }
}
