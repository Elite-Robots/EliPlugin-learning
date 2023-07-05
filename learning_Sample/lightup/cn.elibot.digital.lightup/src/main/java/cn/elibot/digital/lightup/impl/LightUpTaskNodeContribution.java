package cn.elibot.digital.lightup.impl;

import cn.elibot.robot.plugin.contribution.task.TaskApiProvider;
import cn.elibot.robot.plugin.contribution.task.TaskNodeContribution;
import cn.elibot.robot.plugin.contribution.task.TaskNodeDataModelWrapper;
import cn.elibot.robot.plugin.domain.script.ScriptWriter;
import cn.elibot.robot.plugin.domain.task.TaskExtensionNodeViewProvider;
import cn.elibot.robot.plugin.domain.task.undoredo.UndoRedoManager;
import cn.elibot.robot.plugin.domain.task.undoredo.UndoableChanges;

import javax.swing.*;
import java.util.Locale;

/**
 * @author: Elite Zhang
 * @data: 2022/11/7 上午9:31
 */
public class LightUpTaskNodeContribution implements TaskNodeContribution {

  private final TaskApiProvider apiProvider;
  private final TaskNodeDataModelWrapper model;
  private LightUpTaskNodeView view;
  private final UndoRedoManager undoRedoManager;
  private static final String DEFAULT_DIGITAL_OUTPUT = "digital_output";
  private static final String DEFAULT_DURATION = "duration";

  public LightUpTaskNodeContribution(TaskApiProvider apiProvider,
                                     TaskNodeDataModelWrapper model){
    this.apiProvider = apiProvider;
    this.model = model;
    this.undoRedoManager = apiProvider.getUndoRedoManager();
    this.model.setInteger(DEFAULT_DIGITAL_OUTPUT,0);
    this.model.setInteger(DEFAULT_DURATION,1);
  }
  @Override
  public String getTitle() {
    return "Light Up";
  }

  @Override
  public ImageIcon getIcon(boolean isUndefined) {
    return null;
  }

  @Override
  public String getDisplayOnTree(Locale locale) {
    return "LightUp: Do"+getDigitalSelection()+" t="+getDuration()+" s";
  }

  @Override
  public boolean isDefined() {
    return true;
  }

  @Override
  public void setTaskNodeContributionViewProvider(TaskExtensionNodeViewProvider provider) {
    this.view = (LightUpTaskNodeView) provider.get();
  }

  @Override
  public void onViewOpen() {
    this.view.updateView(this);
  }

  @Override
  public void onViewClose() {

  }

  @Override
  public void generateScript(ScriptWriter scriptWriter) {
    scriptWriter.appendLine("set_standard_digital_out("+getDigitalSelection()+",True)");
    scriptWriter.appendLine("sleep("+getDuration()+")");
    scriptWriter.appendLine("set_standard_digital_out("+getDigitalSelection()+",False)");
  }
  public void setDigitalSelection(int output){
    undoRedoManager.recordChanges(new UndoableChanges() {
      @Override
      public void executeChanges() {
        model.setInteger(DEFAULT_DIGITAL_OUTPUT,output);
      }
    });
  }
  public void setDuration(int duration){
    undoRedoManager.recordChanges(new UndoableChanges() {
      @Override
      public void executeChanges() {
        model.setInteger(DEFAULT_DURATION,duration);
      }
    });
  }
  public int getDigitalSelection(){
    return this.model.getInteger(DEFAULT_DIGITAL_OUTPUT);
  }
  public int getDuration(){
    return this.model.getInteger(DEFAULT_DURATION);
  }
}
