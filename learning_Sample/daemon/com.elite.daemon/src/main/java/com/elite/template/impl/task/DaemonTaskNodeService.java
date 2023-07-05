package com.elite.template.impl.task;

import cn.elibot.robot.plugin.contribution.task.*;
import com.elite.template.impl.style.Style;

import java.util.Locale;

public class DaemonTaskNodeService implements SwingTaskNodeService<DaemonTaskNodeContribution, DaemonTaskNodeView> {
    @Override
    public String getId() {
        return "daemon test";
    }

    @Override
    public String getTypeName(Locale locale) {
        return "Daemon Test";
    }

    @Override
    public void configureContribution(TaskNodeFeatures configuration) {

    }

    @Override
    public DaemonTaskNodeView createView(TaskNodeViewApiProvider viewApiProvider) {
        return new DaemonTaskNodeView(viewApiProvider, new Style());
    }

    @Override
    public DaemonTaskNodeContribution createNode(TaskApiProvider apiProvider, TaskNodeDataModelWrapper taskNodeDataModelWrapper, boolean isCloningOrLoading) {
        return new DaemonTaskNodeContribution(apiProvider, taskNodeDataModelWrapper);
    }
}
