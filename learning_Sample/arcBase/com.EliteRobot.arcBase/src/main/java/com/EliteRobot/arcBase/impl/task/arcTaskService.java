package com.EliteRobot.arcBase.impl.task;

import cn.elibot.robot.plugin.contribution.task.*;
import com.EliteRobot.arcBase.impl.resource.ResourceSupport;

import java.util.Locale;

public class arcTaskService implements SwingTaskNodeService<arcTaskContribution,arcTaskView> {
    @Override
    public String getId() {
        return "arc";
    }

    @Override
    public String getTypeName(Locale locale) {
        return ResourceSupport.getResourceBundle(locale).getString("arc");
    }

    @Override
    public void configureContribution(TaskNodeFeatures configuration) {

    }

    @Override
    public arcTaskView createView(TaskNodeViewApiProvider viewApiProvider) {
        return new arcTaskView(viewApiProvider);
    }

    @Override
    public arcTaskContribution createNode(TaskApiProvider apiProvider, TaskNodeDataModelWrapper taskNodeDataModelWrapper, boolean isCloningOrLoading) {
        return new arcTaskContribution(apiProvider,taskNodeDataModelWrapper);
    }
}
