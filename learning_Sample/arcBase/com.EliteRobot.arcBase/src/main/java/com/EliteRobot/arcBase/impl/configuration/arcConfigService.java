package com.EliteRobot.arcBase.impl.configuration;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.ConfigurationViewAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.ContributionConfiguration;
import cn.elibot.robot.plugin.contribution.configuration.SwingConfigurationNodeService;
import cn.elibot.robot.plugin.domain.data.DataModelWrapper;
import com.EliteRobot.arcBase.impl.resource.ResourceSupport;

import java.util.Locale;

public class arcConfigService implements SwingConfigurationNodeService<arcConfigContribution,arcConfigView> {
    @Override
    public void configureContribution(ContributionConfiguration contributionConfiguration) {

    }

    @Override
    public String getTitle(Locale locale) {
        return ResourceSupport.getResourceBundle(locale).getString("arc");
    }

    @Override
    public arcConfigView createView(ConfigurationViewAPIProvider viewApiProvider) {
        return new arcConfigView(viewApiProvider);
    }

    @Override
    public arcConfigContribution createConfigurationNode(ConfigurationAPIProvider configurationApiProvider, arcConfigView configurationNodeView, DataModelWrapper context) {
        return new arcConfigContribution(configurationApiProvider,configurationNodeView,context);
    }
}
