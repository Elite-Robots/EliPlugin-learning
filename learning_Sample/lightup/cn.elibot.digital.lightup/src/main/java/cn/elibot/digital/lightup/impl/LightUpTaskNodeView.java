package cn.elibot.digital.lightup.impl;

import cn.elibot.robot.plugin.contribution.task.SwingTaskNodeView;
import cn.elibot.robot.plugin.contribution.task.TaskNodeViewApiProvider;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author: Elite Zhang
 * @data: 2022/11/7 上午9:31
 */
public class LightUpTaskNodeView implements SwingTaskNodeView<LightUpTaskNodeContribution> {

  private final TaskNodeViewApiProvider viewApiProvider;
  private final JComboBox<Integer> ioComboBox = new JComboBox<Integer>();
  private final JSlider durationSlider = new JSlider();
  private LightUpTaskNodeContribution contribution;
  public LightUpTaskNodeView(TaskNodeViewApiProvider viewApiProvider){
    this.viewApiProvider = viewApiProvider;
  }
  @Override
  public void buildUI(JPanel jPanel, LightUpTaskNodeContribution contribution) {
    this.contribution = contribution;
    jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
    jPanel.add(createDescription("Choose which output to light up:"));
    jPanel.add(createSpace(0,15));
    jPanel.add(createIOComboBox(ioComboBox,contribution));
    jPanel.add(createSpace(0,15));
    jPanel.add(createDescription("Select the duration of the light up:"));
    jPanel.add(createSpace(0,15));
    jPanel.add(createDurationSlider(durationSlider,0,10,contribution));
    jPanel.add(createSpace(0,15));
  }
  public void updateView(LightUpTaskNodeContribution contribution){
    this.contribution = contribution;
    setIoComboBoxItems(getOutputItems());
    setIoComboBoxSelection(LightUpTaskNodeView.this.contribution.getDigitalSelection());
    setDurationSlider(LightUpTaskNodeView.this.contribution.getDuration());
  }
  private Box createIOComboBox(final JComboBox<Integer> combo,
                               LightUpTaskNodeContribution contribution){
    Box box = Box.createHorizontalBox();
    box.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel label = new JLabel("Digital out");
    combo.setPreferredSize(new Dimension(100,30));
    combo.setMaximumSize(combo.getPreferredSize());
    combo.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getStateChange()==ItemEvent.SELECTED){
          LightUpTaskNodeView.this.contribution.setDigitalSelection((Integer)itemEvent.getItem());
        }
      }
    });
    box.add(label);
    box.add(combo);
    return box;
  }
  public void setIoComboBoxItems(Integer[] items){
    ioComboBox.removeAllItems();
    ioComboBox.setModel(new DefaultComboBoxModel<Integer>(items));
  }
  public void setIoComboBoxSelection(Integer item){
    ioComboBox.setSelectedItem(item);
  }
  public Integer[] getOutputItems(){
    Integer[]items = new Integer[16];
    for(int i=0;i<16;i++){
      items[i] = i;
    }
    return items;
  }
  private Box createDurationSlider(final JSlider slider,int min,int max,
                                   LightUpTaskNodeContribution contribution){
    Box box = Box.createHorizontalBox();
    box.setAlignmentX(Component.LEFT_ALIGNMENT);

    slider.setMinimum(min);
    slider.setMaximum(max);
    slider.setOrientation(JSlider.HORIZONTAL);
    slider.setPreferredSize(new Dimension(280,30));
    slider.setMaximumSize(slider.getPreferredSize());
    final JLabel value = new JLabel(Integer.toString(slider.getValue())+" s");
    slider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent changeEvent) {
        int newValue = slider.getValue();
        value.setText(Integer.toString(newValue)+" s");
        LightUpTaskNodeView.this.contribution.setDuration(newValue);
      }
    });
    box.add(slider);
    box.add(value);
    return box;
  }
  public void setDurationSlider(int value){
    durationSlider.setValue(value);
  }
  private Box createDescription(String desc){
    Box box = Box.createVerticalBox();
    box.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel descLabel = new JLabel(desc);
    box.add(descLabel);
    return box;
  }
  private Component createSpace(int length,int width){
    return Box.createRigidArea(new Dimension(length,width));
  }
}
