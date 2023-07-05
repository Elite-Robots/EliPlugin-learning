package com.elite.template.impl.configure;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.ConfigurationNodeContribution;
import cn.elibot.robot.plugin.domain.RpcService;
import cn.elibot.robot.plugin.domain.data.DataModelWrapper;
import cn.elibot.robot.plugin.domain.function.FunctionModel;
import cn.elibot.robot.plugin.domain.robot.RobotMovementService;
import cn.elibot.robot.plugin.domain.script.ScriptWriter;
import com.elite.template.utils.XmlRpcInterface;

public class DaemonConfigurationNodeContribution implements ConfigurationNodeContribution {
    private final DataModelWrapper model;
    private final ConfigurationAPIProvider configurationApiProvider;
    private final DaemonConfigurationNodeView view;
    private final RpcService rpcService;
    private final RobotMovementService robotMovementService;
    private final FunctionModel functionModel;
    private final XmlRpcInterface xmlRpcInterface;

    public XmlRpcInterface getXmlRpcInterface() {
        return xmlRpcInterface;
    }

    public DaemonConfigurationNodeContribution(ConfigurationAPIProvider configurationApiProvider, DaemonConfigurationNodeView configurationNodeView, DataModelWrapper context) {
        this.configurationApiProvider = configurationApiProvider;
        this.model = context;
        this.view = configurationNodeView;

        // get service from configuration api provider
        this.rpcService = configurationApiProvider.getConfigurationApi().getRpcService();
        this.robotMovementService = configurationApiProvider.getConfigurationApi().getRobotMovementService();
        this.functionModel = configurationApiProvider.getFunctionModel();

        // create xmlrpc client interface to connect daemon program, elite robot include x86(upper computer) with ip "6.0.0.10" and a9(lower computer) with ip "6.0.0.9",
        // and the daemon running in upper computer, so the ip address should be "6.0.0.10".And if use simulator, the ip of upper computer and lower computer is "127.0.0.1"
        // we use a system environment variable to distinguish whether in simulator, so that we need to create a environment variable in our simulator in advance.
        this.xmlRpcInterface = new XmlRpcInterface(System.getenv("LOCAL_SIM") != null ? "127.0.0.1" : "6.0.0.10", 60005);
    }

    @Override
    public void onViewOpen() {
        view.updateView(this);
    }

    @Override
    public void onViewClose() {

    }

    @Override
    public void generateScript(ScriptWriter scriptWriter) {
        scriptWriter.appendLine("global dh_gripper");
        scriptWriter.appendLine("dh_gripper = rpc_factory(\"xmlrpc\",\"http://6.0.0.10:60005/\")");
        //cn.elibot.robot.plugin.domain.SystemIpService.
        //scriptWriter.appendLine(String.format("dh_gripper = rpc_factory(\"xmlrpc\",%1$s)", getIP()));
    }

    @Override
    public void onModelChanged() {

    }

    public RpcService getRpcService() {
        return this.rpcService;
    }
}
