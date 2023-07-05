package com.EliteRobot.arcBase.impl.configuration;

import cn.elibot.robot.commons.lang.Filter;
import cn.elibot.robot.plugin.contribution.configuration.ConfigurationAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.ConfigurationNodeContribution;
import cn.elibot.robot.plugin.dialog.CloseDialogPluginListener;
import cn.elibot.robot.plugin.dialog.ConfirmationDialogPluginListener;
import cn.elibot.robot.plugin.dialog.DialogPluginMessage;
import cn.elibot.robot.plugin.dialog.DialogReturnPluginValue;
import cn.elibot.robot.plugin.domain.data.DataModelWrapper;
import cn.elibot.robot.plugin.domain.io.DigitalIO;
import cn.elibot.robot.plugin.domain.io.IO;
import cn.elibot.robot.plugin.domain.io.IOFilterFactory;
import cn.elibot.robot.plugin.domain.script.ScriptWriter;
import cn.elibot.robot.plugin.ui.SwingService;
import com.EliteRobot.arcBase.impl.resource.ResourceSupport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;


public class arcConfigContribution implements ConfigurationNodeContribution {
    private ConfigurationAPIProvider configurationApiProvider;
    private arcConfigView View;
    private DataModelWrapper modelWrapper;
    private Timer timer1;
    private boolean isRunning = false;
    private boolean isRunningPre = false;


    public arcConfigContribution(ConfigurationAPIProvider configurationApiProvider, arcConfigView configurationNodeView, DataModelWrapper context) {
        this.configurationApiProvider = configurationApiProvider;
        this.View = configurationNodeView;
        this.modelWrapper = context;
        setRealWeld(false);


        setData("doArcOn", 0);
        setData("doGas", 1);
        setData("doWireFwd", 2);
        setData("doWireBwd", 3);
        setData("diArcEst", 0);
        setData("diArcReady", 0);
        setData("monitor", 0);

        setData("aoCurrent", 0);
        setData("aoVoltage", 1);


        setDataDouble("minCurrent", 30);
        setDataDouble("aominCurrent", 0);
        setDataDouble("maxCurrent", 350);
        setDataDouble("aomaxCurrent", 10);
        setDataDouble("minVoltage", 12);
        setDataDouble("aominVoltage", 0);
        setDataDouble("maxVoltage", 40);
        setDataDouble("aomaxVoltage", 10);

        setDataDouble("preGas", 0.5);
        setDataDouble("arcEstTime", 1.0);
        setDataDouble("postGas", 0.5);
        refresh();
    }

    public void refresh() {
        timer1 = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isRunning = configurationApiProvider.getTaskStateApiProvider().isTaskRunning();
                if (isRunning == true && getRealWeld() && getData("monitor") == 1) {
                    pauseTask();
                }
                if ((isRunning == false) && (isRunningPre == true)) {
                    pauseFunction();
                }

                isRunningPre = isRunning;
            }
        });
        timer1.start();
    }

    public void pauseTask() {
        /*String s = "sec stopTask():\n";
        s += String.format("  if get_standard_digital_in(%1$s) == False:\n", getData("diArcReady"));
        s += "      textmsg(\"stop task\")\n";
        s += "      exit(1)\n";
        s += "end\n";
        configurationApiProvider.getRpcService().runScript(s);*/

        try {
            if (((DigitalIO) getStandardDigitalIOById(getData("diArcReady"))).getValue() == false) {
                String s1 = "def stopTask():\n";
                s1 += "      textmsg(\"stop task\")\n";
                s1 += "end\n";
                configurationApiProvider.getRpcService().runScript(s1);

                DialogPluginMessage dialogPluginMessage = new DialogPluginMessage.Builder()
                        .setTitle(ResourceSupport.getDefaultResourceBundle().getString("Error"))
                        //.setHeader(ResourceSupport.getResourceBundle().getString("dialog_info_title"))
                        .setMessage(ResourceSupport.getDefaultResourceBundle().getString("Arc_Error"))
                        .setMessageType(DialogPluginMessage.MessageType.ERROR_MESSAGE).build();
                SwingService.messageDialogService.showCloseDialog(dialogPluginMessage, new CloseDialogPluginListener() {
                    @Override
                    public void onClose() {
                        super.onClose();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseFunction() {
        String s = "sec t2():\n";
        s += "  set_standard_digital_out(" + getData("doArcOn") + ",False)\n";
        s += "  set_standard_digital_out(" + getData("doGas") + ",False)\n";
        s += "end\n";
        configurationApiProvider.getRpcService().runScript(s);
    }

    public void setData(String name, Integer num) {
        modelWrapper.setInteger(name, num);
    }

    public Integer getData(String name) {
        return modelWrapper.getInteger(name);
    }

    public void setDataDouble(String name, double num) {
        modelWrapper.setDouble(name, num);
    }

    public Double getDataDouble(String name) {
        return modelWrapper.getDouble(name);
    }


    @Override
    public void onViewOpen() {
        View.updateUI(this);
    }

    @Override
    public void onViewClose() {

    }

    public void setRealWeld(boolean b) {
        modelWrapper.setBoolean("bRealWeld", b);
    }

    public Boolean getRealWeld() {
        return modelWrapper.getBoolean("bRealWeld");
    }

    @Override
    public void generateScript(ScriptWriter scriptWriter) {
        String s = "";
        s += "  def waitdi(sig_name, value, t):\n";
        s += "      global arcflag1\n";
        s += "      arcflag1 = 0\n";

        s += "      def waittime():\n";
        s += "          global arcflag1\n";
        s += "          sleep(t)\n";
        s += "          arcflag1 = 2\n";

        s += "      th_id1 = start_thread(waittime, ())\n";
        s += "      while True:\n";
        s += "          if get_standard_digital_in(sig_name) == value:\n";
        s += "              stop_thread(th_id1)\n";
        s += "              arcflag1 = 1\n";
        s += "              break\n";
        s += "          if arcflag1 == 2:\n";
        s += "              break\n";
        s += "          sleep(0.01)\n";
        s += "          sync()\n";

        s += "      if arcflag1 == 2:\n";
        s += "          popup(s=\"No Arc Est Signal\", error=True,title=\"Arc On Error\", blocking=True)\n";
        s += "          halt()\n";

        scriptWriter.appendRaw(s);
    }


    public DigitalIO getStandardDigitalIOById(int id) {
        Filter<IO> digitalInputFilter = IOFilterFactory.createDigitalInputFilter();
        Collection<IO> iOs = configurationApiProvider.getIOModel().getIOs(digitalInputFilter);
        AtomicReference<IO> returnIO = new AtomicReference<>();
        iOs.forEach(io -> {
            if (io.getName().equals(String.format("digital_in[%1$s]", id))) {
                returnIO.set(io);
            }
        });
        return (DigitalIO) returnIO.get();
    }
}
