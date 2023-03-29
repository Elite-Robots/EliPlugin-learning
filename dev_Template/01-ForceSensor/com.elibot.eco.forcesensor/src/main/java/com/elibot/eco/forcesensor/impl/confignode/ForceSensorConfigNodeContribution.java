package com.elibot.eco.forcesensor.impl.confignode;

import java.util.ArrayList;
import java.util.ResourceBundle;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.ConfigurationNodeContribution;
import cn.elibot.robot.plugin.domain.data.DataModelWrapper;
import cn.elibot.robot.plugin.domain.script.ScriptWriter;

public class ForceSensorConfigNodeContribution implements ConfigurationNodeContribution {

    private ConfigurationAPIProvider apiProvider;
    private ForceSensorConfigNodeView view;
    private DataModelWrapper dataModelWrapper;
    private ResourceBundle i18n;

    public ForceSensorConfigNodeContribution(ConfigurationAPIProvider apiProvider, ForceSensorConfigNodeView view,
            DataModelWrapper dataModelWrapper, ResourceBundle i18n) {
        this.apiProvider = apiProvider;
        this.view = view;
        this.dataModelWrapper = dataModelWrapper;
        this.i18n = i18n;
        this.apiProvider.getToolIoLockerModel().requestLocker(this.view.serialConnectParamsPanel.getTCILocker());
        // init dataModel
        this.onViewClose();
    }

    @Override
    public void onViewOpen() {
        ArrayList<Object> connectParams = this.getSerialConnectParams();
        this.view.serialConnectParamsPanel.setDefaultConnectParams((int) connectParams.get(0),
                                                                   (int) connectParams.get(1),
                                                                   (int) connectParams.get(2),
                                                                   (String) connectParams.get(3));
    }

    @Override
    public void onViewClose() {
        ArrayList<Object> connectParams = this.view.serialConnectParamsPanel.getConnectParams();
        this.setSerialConnectParams((int) connectParams.get(0), (int) connectParams.get(1), (int) connectParams.get(2),
                (String) connectParams.get(3));
    }

    @Override
    public void generateScript(ScriptWriter scriptWriter) {
        scriptWriter.appendMultiLines(String.join("\n",
                "def " + i18n.getString("globalEFTDFuncTitle") + "_enable_sensor_data(enable):",
                "    ft_rtsi_input_enable(enable, sensor_mass=0.0, sensor_measuring_offset=[0.0, 0.0, 0.0], sensor_cog=[0.0, 0.0, 0.0])",
                ""));
    }

    private void setSerialConnectParams(int baudrate, int dataBits, int stopBits, String parity) {
        this.dataModelWrapper.setInteger("baudrate", baudrate);
        this.dataModelWrapper.setInteger("dataBits", dataBits);
        this.dataModelWrapper.setInteger("stopBits", stopBits);
        this.dataModelWrapper.setString("parity", parity);
    }

    private ArrayList<Object> getSerialConnectParams() {
        ArrayList<Object> connectParams = new ArrayList<Object>();
        connectParams.add(this.dataModelWrapper.getInteger("baudrate"));
        connectParams.add(this.dataModelWrapper.getInteger("dataBits"));
        connectParams.add(this.dataModelWrapper.getInteger("stopBits"));
        connectParams.add(this.dataModelWrapper.getString("parity"));
        return connectParams;
    }
}
