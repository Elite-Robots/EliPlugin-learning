package cn.elibot.digital.lightup.impl;

import cn.elibot.robot.plugin.contribution.task.*;

import java.util.Locale;

/**
 * @author: Elite Zhang
 * @data: 2022/11/7 上午9:31
 */
public class LightUpTaskNodeService implements SwingTaskNodeService<LightUpTaskNodeContribution,LightUpTaskNodeView> {
  @Override
  public String getId() {
    return "LightUpTaskNode";
  }

  @Override
  public String getTypeName(Locale locale) {
    return "LightUp";
  }

  @Override
  public void configureContribution(TaskNodeFeatures configuration) {
    configuration.setChildrenAllowed(false);
    configuration.setUserInsertable(true);
  }

  @Override
  public LightUpTaskNodeView createView(TaskNodeViewApiProvider viewApiProvider) {
    return new LightUpTaskNodeView(viewApiProvider);
  }

  @Override
  public LightUpTaskNodeContribution createNode(TaskApiProvider apiProvider, TaskNodeDataModelWrapper taskNodeDataModelWrapper, boolean isCloningOrLoading) {
    return new LightUpTaskNodeContribution(apiProvider,taskNodeDataModelWrapper);
  }
}
