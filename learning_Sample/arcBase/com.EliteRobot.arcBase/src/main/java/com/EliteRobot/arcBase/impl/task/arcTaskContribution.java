package com.EliteRobot.arcBase.impl.task;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationAPIProvider;
import cn.elibot.robot.plugin.contribution.task.TaskApiProvider;
import cn.elibot.robot.plugin.contribution.task.TaskNodeContribution;
import cn.elibot.robot.plugin.contribution.task.TaskNodeDataModelWrapper;
import cn.elibot.robot.plugin.domain.script.ScriptWriter;
import cn.elibot.robot.plugin.domain.task.TaskExtensionNodeViewProvider;
import com.EliteRobot.arcBase.impl.configuration.arcConfigContribution;
import com.EliteRobot.arcBase.impl.resource.ResourceSupport;
import com.sun.org.apache.bcel.internal.generic.SWITCH;

import javax.swing.*;
import java.util.Locale;

public class arcTaskContribution implements TaskNodeContribution {

    private ConfigurationAPIProvider configurationAPIProvider;
    private TaskApiProvider apiProvider;
    private TaskNodeDataModelWrapper modelWrapper;
    private arcConfigContribution arcConfig;
    private arcTaskView view;


    public arcTaskContribution(TaskApiProvider apiProvider, TaskNodeDataModelWrapper taskNodeDataModelWrapper) {
        this.apiProvider = apiProvider;
        this.modelWrapper = taskNodeDataModelWrapper;
        this.configurationAPIProvider = apiProvider.getConfigurationApi();
        this.arcConfig = getInstallation();

        setArcInstru(ResourceSupport.getDefaultResourceBundle().getString("arcOn"));
        setDataDouble("current", 30.0);
        setDataDouble("voltage", 12.0);
        //setArcInstru(ResourceSupport.getDefaultResourceBundle().getString("arcOn"));
    }

    public arcConfigContribution getInstallation() {
        return configurationAPIProvider.getConfigurationNode(arcConfigContribution.class);
    }

    public Integer getSignal(String name) {
        return arcConfig.getData(name);
    }

    public void setRealWeld(boolean b) {
        //modelWrapper.setBoolean("bRealWeld",b);
        /*apiProvider.getUndoRedoManager().recordChanges(() -> {
            getInstallation().setRealWeld(b);
            modelWrapper.setBoolean("bRealWeld", b);
        });*/
        getInstallation().setRealWeld(b);
    }

    public boolean getRealWeld() {
        System.out.println("get real weld");
        /*getInstallation().setRealWeld(modelWrapper.getBoolean("bRealWeld"));
        return modelWrapper.getBoolean("bRealWeld");*/
        return getInstallation().getRealWeld();
    }

    public void setArcInstru(String s) {
        //modelWrapper.setString("arcInstru",s);
        apiProvider.getUndoRedoManager().recordChanges(() -> {
            modelWrapper.setString("arcInstru", s);
        });
    }

    public String getArcInstru() {
        return modelWrapper.getString("arcInstru");
    }

    public void setDataDouble(String name, double num) {
        //modelWrapper.setDouble(name,num);
        apiProvider.getUndoRedoManager().recordChanges(() -> {
            modelWrapper.setDouble(name, num);
        });
    }

    public Double getDataDouble(String name) {
        return modelWrapper.getDouble(name);
    }

    public Double getConfigDataDouble(String name) {
        return arcConfig.getDataDouble(name);
    }

    @Override
    public String getTitle() {
        return ResourceSupport.getDefaultResourceBundle().getString("arc");
    }

    @Override
    public ImageIcon getIcon(boolean isUndefined) {
        return null;
    }

    @Override
    public String getDisplayOnTree(Locale locale) {
        return getArcInstru();
    }

    @Override
    public boolean isDefined() {
        return true;
    }

    @Override
    public void setTaskNodeContributionViewProvider(TaskExtensionNodeViewProvider provider) {
        this.view = (arcTaskView) provider.get();

    }

    @Override
    public void onViewOpen() {
        view.updateUI(this);
    }

    @Override
    public void onViewClose() {

    }

    private double cal(double x1, double x2, double y1, double y2, double value) {
        double k = (y1 - y2) / (x1 - x2);
        return (k * (value - x1) + y1) / 10;
    }

    @Override
    public void generateScript(ScriptWriter scriptWriter) {
        String arcOn = ResourceSupport.getDefaultResourceBundle().getString("arcOn");
        String arcOff = ResourceSupport.getDefaultResourceBundle().getString("arcOff");
        String arcSet = ResourceSupport.getDefaultResourceBundle().getString("arcSet");

        String Instruction = getArcInstru();

        double out_curr_ao = cal(getConfigDataDouble("minCurrent"), getConfigDataDouble("maxCurrent"), getConfigDataDouble("aominCurrent"), getConfigDataDouble("aomaxCurrent"), getDataDouble("current"));
        double out_voltage_ao = cal(getConfigDataDouble("minVoltage"), getConfigDataDouble("maxVoltage"), getConfigDataDouble("aominVoltage"), getConfigDataDouble("aomaxVoltage"), getDataDouble("voltage"));

        if (Instruction == arcOn) {
            scriptWriter.appendLine("set_standard_analog_out(" + getSignal("aoCurrent") + "," + out_curr_ao + ")");
            scriptWriter.appendLine("set_standard_analog_out(" + getSignal("aoVoltage") + "," + out_voltage_ao + ")");

            if (getRealWeld() == true) {
                scriptWriter.appendLine("set_standard_digital_out(" + getSignal("doGas") + ",True)");
                scriptWriter.appendLine("sleep(" + getConfigDataDouble("preGas") + ")");
                scriptWriter.appendLine("set_standard_digital_out(" + getSignal("doArcOn") + ",True)");
                scriptWriter.appendLine("waitdi(" + getSignal("diArcEst") + ",True," + getConfigDataDouble("arcEstTime") + ")");
            }
        }

        if (Instruction == arcSet) {
            scriptWriter.appendLine("set_standard_analog_out(" + getSignal("aoCurrent") + "," + out_curr_ao + ")");
            scriptWriter.appendLine("set_standard_analog_out(" + getSignal("aoVoltage") + "," + out_voltage_ao + ")");

        }

        if (Instruction == arcOff) {
            scriptWriter.appendLine("set_standard_analog_out(" + getSignal("aoCurrent") + "," + out_curr_ao + ")");
            scriptWriter.appendLine("set_standard_analog_out(" + getSignal("aoVoltage") + "," + out_voltage_ao + ")");

            scriptWriter.appendLine("set_standard_digital_out(" + getSignal("doArcOn") + ",False)");
            scriptWriter.appendLine("sleep(" + getConfigDataDouble("postGas") + ")");
            scriptWriter.appendLine("set_standard_digital_out(" + getSignal("doGas") + ",False)");
        }
    }

    @Override
    public void onModelChanged() {
        System.out.println("model changed");
    }
}
