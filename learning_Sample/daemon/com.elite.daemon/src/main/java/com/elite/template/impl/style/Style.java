package com.elite.template.impl.style;

import cn.elibot.robot.plugin.ui.model.FontLibrary;

import javax.swing.*;
import java.awt.*;

public class Style {
    private static final int VERTICAL_0 = 0;
    private static final int VERTICAL_5 = 5;
    private static final int VERTICAL_10 = 10;
    private static final int VERTICAL_20 = 20;
    private static final int VERTICAL_30 = 30;
    private static final int VERTICAL_40 = 40;
    private static final int HORIZONTAL_0 = 0;
    private static final int HORIZONTAL_5 = 5;
    private static final int HORIZONTAL_10 = 10;
    private static final int HORIZONTAL_20 = 20;
    private static final int HORIZONTAL_50 = 50;
    private static final int BUTTON_LENGTH55 = 57;
    private static final int BUTTON_LENGTH80 = 80;
    private static final int BUTTON_LENGTH106 = 108;
    private static final int BUTTON_LENGTH120 = 120;
    private static final int BUTTON_LENGTH210 = 210;
    private static final int BUTTON_WIDTH = 30;
    private static final int BUTTON_WIDTH54 = 54;
    private static final int BUTTON_WIDTH90 = 90;
    private static final int FIELD_LENGTH100 = 100;
    private static final int FIELD_LENGTH150 = 150;
    private static final int FIELD_LENGTH200 = 200;
    private static final int FIELD_WIDTH30 = 30;
    private static final int LABEL_LENGTH_40 = 40;
    private static final int LABEL_LENGTH_50 = 50;
    private static final int LABEL_LENGTH_80 = 80;
    private static final int LABEL_LENGTH_100 = 100;
    private static final int LABEL_LENGTH_120 = 120;
    private static final int LABEL_LENGTH_150 = 150;
    private static final int LABEL_WIDTH = 30;
    private static final int LABEL_UNIT_LENGTH = 60;
    private static final int SLIDER_LENGTH = 200;
    private static final int SLIDER_WIDTH = 200;
    private static final int PROGRESS_MIN = 0;
    private static final int PROGRESS_MAX = 110;

    public int getVertical0() {
        return VERTICAL_0;
    }

    public int getVertical5() {
        return VERTICAL_5;
    }

    public int getVertical10() {
        return VERTICAL_10;
    }

    public int getVertical20() {
        return VERTICAL_20;
    }

    public int getVertical30() {
        return VERTICAL_30;
    }

    public int getVertical40() {
        return VERTICAL_40;
    }

    public int getHorizontal0() {
        return HORIZONTAL_0;
    }

    public int getHorizontal5() {
        return HORIZONTAL_5;
    }

    public int getHorizontal10() {
        return HORIZONTAL_10;
    }

    public int getHorizontal20() {
        return HORIZONTAL_20;
    }

    public int getHorizontal50() {
        return HORIZONTAL_50;
    }

    public int getProgressMin() {
        return PROGRESS_MIN;
    }

    public int getProgressMax() {
        return PROGRESS_MAX;
    }

    public Dimension getButtonSize210() {
        return new Dimension(BUTTON_LENGTH210, BUTTON_WIDTH);
    }

    public Dimension getButtonSize120() {
        return new Dimension(BUTTON_LENGTH120, BUTTON_WIDTH);
    }

    public Dimension getButtonSize80() {
        return new Dimension(BUTTON_LENGTH80, BUTTON_WIDTH);
    }

    public Dimension getButtonSize106_90() {
        return new Dimension(BUTTON_LENGTH106, BUTTON_WIDTH90);
    }

    public Dimension getButtonSize106_54() {
        return new Dimension(BUTTON_LENGTH106, BUTTON_WIDTH54);
    }

    public Dimension getButtonSize55_54() {
        return new Dimension(BUTTON_LENGTH55, BUTTON_WIDTH54);
    }

    public Dimension getLabelSize40() {
        return new Dimension(LABEL_LENGTH_40, LABEL_WIDTH);
    }

    public Dimension getLabelSize50() {
        return new Dimension(LABEL_LENGTH_50, LABEL_WIDTH);
    }

    public Dimension getLabelSize80() {
        return new Dimension(LABEL_LENGTH_80, LABEL_WIDTH);
    }

    public Dimension getLabelSize100() {
        return new Dimension(LABEL_LENGTH_100, LABEL_WIDTH);
    }

    public Dimension getLabelSize120() {
        return new Dimension(LABEL_LENGTH_120, LABEL_WIDTH);
    }
    public Dimension getLabelSize150() {
        return new Dimension(LABEL_LENGTH_150, LABEL_WIDTH);
    }
    public Dimension getLabelUnitSize() {
        return new Dimension(LABEL_UNIT_LENGTH, LABEL_WIDTH);
    }

    public Dimension getTextFieldSize100() {
        return new Dimension(FIELD_LENGTH100, FIELD_WIDTH30);
    }

    public Dimension getTextFieldSize150() {
        return new Dimension(FIELD_LENGTH200, FIELD_WIDTH30);
    }

    public Dimension getTextFieldSize200() {
        return new Dimension(FIELD_LENGTH150, FIELD_WIDTH30);
    }

    public Dimension getSliderSize() {
        return new Dimension(SLIDER_LENGTH, SLIDER_WIDTH);
    }

    public Dimension getProgressBarSize() {
        return new Dimension(SLIDER_LENGTH, SLIDER_WIDTH);
    }

    public Component createSpace(int length, int width) {
        return Box.createRigidArea(new Dimension(length, width));
    }

    public void setFixedSize(Component component, Dimension size) {
        component.setMinimumSize(size);
        component.setPreferredSize(size);
        component.setMaximumSize(size);
    }

    public void setFixedSize(Component component, int width, int height) {
        Dimension dimension = new Dimension(width, height);
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
    }

    /**
     * Head description
     */
    public Box createHeadDescription(String desc) {
        Box box = Box.createVerticalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = new JLabel(desc);
        label.setFont(FontLibrary.H4_BOLD_FONT);
        box.add(label);
        return box;
    }

    /**
     * description
     */
    public Box createDescription(String desc) {
        Box box = Box.createVerticalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = new JLabel(desc);
        label.setFont(FontLibrary.H5_FONT);
        box.add(label);
        return box;
    }
}
