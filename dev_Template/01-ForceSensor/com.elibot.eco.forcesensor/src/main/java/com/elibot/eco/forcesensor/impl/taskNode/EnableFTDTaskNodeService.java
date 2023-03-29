package com.elibot.eco.forcesensor.impl.taskNode;

import java.util.Locale;
import java.util.ResourceBundle;

import com.elibot.eco.forcesensor.impl.resource.ResourceSupport;

import cn.elibot.robot.plugin.contribution.task.SwingTaskNodeService;
import cn.elibot.robot.plugin.contribution.task.TaskApiProvider;
import cn.elibot.robot.plugin.contribution.task.TaskNodeDataModelWrapper;
import cn.elibot.robot.plugin.contribution.task.TaskNodeFeatures;
import cn.elibot.robot.plugin.contribution.task.TaskNodeViewApiProvider;

public class EnableFTDTaskNodeService implements SwingTaskNodeService<EnableFTDTaskNodeContribution,EnableFTDTaskNodeview>{

    private ResourceBundle i18n = ResourceSupport.getDefaultResourceBundle();

    @Override
    public String getId() {
        return i18n.getString("sensorBrand")+"EnableFTDTaskNode";
    }

    @Override
    public String getTypeName(Locale locale) {
        return i18n.getString("sensorBrand")+"-"+i18n.getString("enableFTDTaskNodeName");
    }

    @Override
    public void configureContribution(TaskNodeFeatures configuration) {
        configuration.setChildrenAllowed(false);
        configuration.setUserInsertable(true);
    }

    @Override
    public EnableFTDTaskNodeview createView(TaskNodeViewApiProvider viewApiProvider) {
        return new EnableFTDTaskNodeview(viewApiProvider,i18n);
    }

    @Override
    public EnableFTDTaskNodeContribution createNode(TaskApiProvider apiProvider,
            TaskNodeDataModelWrapper taskNodeDataModelWrapper, boolean isCloningOrLoading) {
        return new EnableFTDTaskNodeContribution(apiProvider,taskNodeDataModelWrapper,isCloningOrLoading,i18n);
    }
    
}
