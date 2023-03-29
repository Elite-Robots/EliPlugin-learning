package com.elibot.eco.forcesensor.impl.taskNode;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import cn.elibot.robot.plugin.contribution.task.TaskApiProvider;
import cn.elibot.robot.plugin.contribution.task.TaskNodeContribution;
import cn.elibot.robot.plugin.contribution.task.TaskNodeDataModelWrapper;
import cn.elibot.robot.plugin.domain.script.ScriptWriter;
import cn.elibot.robot.plugin.domain.task.TaskExtensionNodeViewProvider;

public class EnableFTDTaskNodeContribution implements TaskNodeContribution {

    private ResourceBundle i18n;
    private TaskNodeDataModelWrapper dataModelWrapper;
    private EnableFTDTaskNodeview view;
    private TaskApiProvider taskApiProvider;

    public EnableFTDTaskNodeContribution(TaskApiProvider apiProvider,
            TaskNodeDataModelWrapper taskNodeDataModelWrapper, boolean isCloningOrLoading, ResourceBundle i18n) {
        this.taskApiProvider = apiProvider;
        this.dataModelWrapper = taskNodeDataModelWrapper;
        this.i18n = i18n;
        this.setFTDEnable(false);
    }

    @Override
    public void onViewOpen() {
        this.view.updataView(this);
    }

    @Override
    public void onViewClose() {

    }

    @Override
    public void generateScript(ScriptWriter scriptWriter) {
        String enable = this.getFTDEnable() ? "True" : "False";
        scriptWriter.appendLine(i18n.getString("globalEFTDFuncTitle") + "_enable_sensor_data(" + enable + ")");
    }

    @Override
    public String getTitle() {
        return i18n.getString("sensorBrand") + "-" + i18n.getString("enableFTDTaskNodeName");
    }

    @Override
    public ImageIcon getIcon(boolean isUndefined) {
        return null;
    }

    @Override
    public String getDisplayOnTree(Locale locale) {
        if (getFTDEnable()) {
            return i18n.getString("sensorBrand") + "-" + i18n.getString("enableFTDTaskNodeName") + " : "
                    + i18n.getString("enable");
        } else {
            return i18n.getString("sensorBrand") + "-" + i18n.getString("enableFTDTaskNodeName") + " : "
                    + i18n.getString("disable");
        }
    }

    @Override
    public boolean isDefined() {
        return true;
    }

    @Override
    public void setTaskNodeContributionViewProvider(TaskExtensionNodeViewProvider provider) {
        this.view = (EnableFTDTaskNodeview) provider.get();
    }

    public void setFTDEnable(boolean enable) {
        // use undoRedoManager recoed data changes
        this.taskApiProvider.getUndoRedoManager()
                .recordChanges(() -> this.dataModelWrapper.setBoolean("enableFTData", enable));
    }

    public boolean getFTDEnable() {
        return this.dataModelWrapper.getBoolean("enableFTData");
    }
}
