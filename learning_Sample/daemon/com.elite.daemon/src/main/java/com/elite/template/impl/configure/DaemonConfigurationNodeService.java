package com.elite.template.impl.configure;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.ConfigurationViewAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.ContributionConfiguration;
import cn.elibot.robot.plugin.contribution.configuration.SwingConfigurationNodeService;
import cn.elibot.robot.plugin.domain.data.DataModelWrapper;
import com.elite.template.impl.style.Style;

import java.util.Locale;

public class DaemonConfigurationNodeService implements SwingConfigurationNodeService<DaemonConfigurationNodeContribution, DaemonConfigurationNodeView> {
    @Override
    public void configureContribution(ContributionConfiguration contributionConfiguration) {

    }

    @Override
    public String getTitle(Locale locale) {
        return "Daemon Test";
    }

    @Override
    public DaemonConfigurationNodeView createView(ConfigurationViewAPIProvider viewApiProvider) {
        return new DaemonConfigurationNodeView(viewApiProvider, new Style());
    }

    @Override
    public DaemonConfigurationNodeContribution createConfigurationNode(ConfigurationAPIProvider configurationApiProvider, DaemonConfigurationNodeView configurationNodeView, DataModelWrapper context) {
        return new DaemonConfigurationNodeContribution(configurationApiProvider, configurationNodeView, context);
    }
}
