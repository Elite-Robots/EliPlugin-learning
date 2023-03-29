package com.elibot.eco.forcesensor.impl.common;

import java.util.Arrays;
import java.util.List;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.elibot.eco.forcesensor.impl.resource.ResourceSupport;

import cn.elibot.robot.plugin.ui.model.FontLibrary;

public class SensorDataPanel{

    private int locale = ResourceSupport.getLocaleProvider().getLocale().toString().equals("en") ? 0 : 1;

    private final String[] PANEL_NAME = { "Sensor Data", "传感器数据" };

    private final String FORCE_X_LABEL = "X (N):";
    private final String FORCE_Y_LABEL = "Y (N):";
    private final String FORCE_Z_LABEL = "Z (N):";
    private final String MOMENT_X_LABEL = "RX (Nm):";
    private final String MOMENT_Y_LABEL = "RY (Nm):";
    private final String MOMENT_Z_LABEL = "RZ (Nm):";

    private final Dimension DEFAULT_LABEL_SIZE = new Dimension(70, 30);
    private final Dimension DEFAULT_LABEL_VALUE_SIZE = new Dimension(60, 30);
    private final Dimension DEFAULT_HORI_GAP = new Dimension(5, 0);
    private final Dimension DEFAULT_VERT_GAP = new Dimension(0, 5);

    private JPanel panel = new JPanel();
    private JLabel forceXLabel = new JLabel("0", SwingConstants.RIGHT);
    private JLabel forceYLabel = new JLabel("0", SwingConstants.RIGHT);
    private JLabel forceZLabel = new JLabel("0", SwingConstants.RIGHT);
    private JLabel momentRXLabel = new JLabel("0", SwingConstants.RIGHT);
    private JLabel momentRYLabel = new JLabel("0", SwingConstants.RIGHT);
    private JLabel momentRZLabel = new JLabel("0", SwingConstants.RIGHT);

    private double forceX;
    private double forceY;
    private double forceZ;
    private double momentRX;
    private double momentRY;
    private double momentRZ;

    public SensorDataPanel() {
        this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
        this.panel.setAlignmentY(Component.TOP_ALIGNMENT);
        this.panel.setBorder(new TitledBorder(new EtchedBorder(), this.PANEL_NAME[locale]));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(createHoriBox(new JLabel(FORCE_X_LABEL), forceXLabel));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(createHoriBox(new JLabel(FORCE_Y_LABEL), forceYLabel));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(createHoriBox(new JLabel(FORCE_Z_LABEL), forceZLabel));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(createHoriBox(new JLabel(MOMENT_X_LABEL), momentRXLabel));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(createHoriBox(new JLabel(MOMENT_Y_LABEL), momentRYLabel));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
        this.panel.add(createHoriBox(new JLabel(MOMENT_Z_LABEL), momentRZLabel));
        this.panel.add(Box.createRigidArea(DEFAULT_VERT_GAP));
    }

    private Box createHoriBox(JLabel dataLabel, JLabel data) {
        Box horiBox = Box.createHorizontalBox();
        horiBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        dataLabel.setFont(FontLibrary.NORMAL_FONT);
        dataLabel.setMinimumSize(DEFAULT_LABEL_SIZE);
        dataLabel.setMaximumSize(DEFAULT_LABEL_SIZE);
        data.setMinimumSize(DEFAULT_LABEL_VALUE_SIZE);
        data.setMaximumSize(DEFAULT_LABEL_VALUE_SIZE);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        horiBox.add(dataLabel);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        horiBox.add(data);
        horiBox.add(Box.createRigidArea(DEFAULT_HORI_GAP));
        return horiBox;
    }


    public JPanel show(){
        return this.panel;
    }


    private String formatValue(double value) {
        return new DecimalFormat("#00.000").format(value);
    }

    public List<Double> getForce() {
        return Arrays.asList(forceX, forceY, forceZ);
    }

    public void setForce(double fx, double fy, double fz) {
        this.forceX = fx;
        this.forceY = fy;
        this.forceZ = fz;
        this.forceXLabel.setText(this.formatValue(fx));
        this.forceYLabel.setText(this.formatValue(fy));
        this.forceZLabel.setText(this.formatValue(fz));
    }

    public List<Double> getMoment() {
        return Arrays.asList(momentRX, momentRY, momentRZ);
    }

    public void setMoment(double mx, double my, double mz) {
        this.momentRX = mx;
        this.momentRY = my;
        this.momentRZ = mz;
        this.momentRXLabel.setText(this.formatValue(mx));
        this.momentRYLabel.setText(this.formatValue(my));
        this.momentRZLabel.setText(this.formatValue(mz));
    }

    public List<Double> getTorque() {
        return Arrays.asList(forceX, forceY, forceZ, momentRX, momentRY, momentRZ);
    }

    public void setTorque(double fx, double fy, double fz, double mx, double my, double mz) {
        this.forceX = fx;
        this.forceY = fy;
        this.forceZ = fz;
        this.forceXLabel.setText(this.formatValue(fx));
        this.forceYLabel.setText(this.formatValue(fy));
        this.forceZLabel.setText(this.formatValue(fz));
        this.momentRX = mx;
        this.momentRY = my;
        this.momentRZ = mz;
        this.momentRXLabel.setText(this.formatValue(mx));
        this.momentRYLabel.setText(this.formatValue(my));
        this.momentRZLabel.setText(this.formatValue(mz));
    }

}
