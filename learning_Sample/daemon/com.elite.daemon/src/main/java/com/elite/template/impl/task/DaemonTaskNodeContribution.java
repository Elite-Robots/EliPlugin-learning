package com.elite.template.impl.task;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationAPIProvider;
import cn.elibot.robot.plugin.contribution.task.TaskApiProvider;
import cn.elibot.robot.plugin.contribution.task.TaskNodeContribution;
import cn.elibot.robot.plugin.contribution.task.TaskNodeDataModelWrapper;
import cn.elibot.robot.plugin.domain.script.ScriptWriter;
import cn.elibot.robot.plugin.domain.task.TaskExtensionNodeViewProvider;
import com.elite.template.impl.configure.DaemonConfigurationNodeContribution;

import javax.swing.*;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DaemonTaskNodeContribution implements TaskNodeContribution {
    private final TaskApiProvider taskApiProvider;
    private final ConfigurationAPIProvider configurationAPIProvider;
    private final TaskNodeDataModelWrapper model;
    private DaemonTaskNodeView view;
    private Timer timer;

    public DaemonTaskNodeContribution(TaskApiProvider apiProvider, TaskNodeDataModelWrapper taskNodeDataModelWrapper) {
        this.taskApiProvider = apiProvider;
        this.configurationAPIProvider = apiProvider.getConfigurationApi();
        this.model = taskNodeDataModelWrapper;


        // define default value
        setData("width", 50);
        setData("speed", 50);
        setData("force", 50);
    }

    public DaemonConfigurationNodeContribution getInstallation() {
        return configurationAPIProvider.getConfigurationNode(DaemonConfigurationNodeContribution.class);
    }

    @Override
    public String getTitle() {
        return "Daemon Test";
    }

    @Override
    public ImageIcon getIcon(boolean isUndefined) {
        return null;
    }

    @Override
    public String getDisplayOnTree(Locale locale) {
        //return "Daemon Test";
        return String.format("Daemon Test(width: %1$s)", getData("width"));
    }

    @Override
    public boolean isDefined() {
        return true;
    }

    @Override
    public void setTaskNodeContributionViewProvider(TaskExtensionNodeViewProvider provider) {
        this.view = (DaemonTaskNodeView) provider.get();
    }

    /**
     * this method will be invoked when the plugin view open
     */
    @Override
    public void onViewOpen() {
        this.view.updateView(this);
        if (this.timer == null) {
            this.timer = new java.util.Timer();
            this.timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("get state");
                    Integer[] status = getStatus();
                    view.refresh(status);
                }
            }, 0, 1000);
        }
    }

    private Integer[] getStatus() {
        Integer[] status = getInstallation().getXmlRpcInterface().getStatus();
        return status;
    }

    /**
     * this method will be invoked when the plugin view close
     */
    @Override
    public void onViewClose() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    /**
     * this method is used to generate python code of this node
     * @param scriptWriter
     */
    @Override
    public void generateScript(ScriptWriter scriptWriter) {
        scriptWriter.appendLine(String.format("dh_gripper.move_to(%1$s, %2$s, %3$s)", getData("width"), getData("speed"), getData("force")));
    }

    public Integer getData(String key) {
        return this.model.getInteger(key);
    }

    /**
     * store view data to data wrapper
     * @param key
     * @param valueOf
     */
    public void setData(String key, Integer valueOf) {
        this.model.setInteger(key, valueOf);
    }

    public void execute() {
        getInstallation().getXmlRpcInterface().moveTo(getData("width"), getData("speed"), getData("speed"));
    }

    public void initGripper() {
        getInstallation().getXmlRpcInterface().initGripper();
    }
}
