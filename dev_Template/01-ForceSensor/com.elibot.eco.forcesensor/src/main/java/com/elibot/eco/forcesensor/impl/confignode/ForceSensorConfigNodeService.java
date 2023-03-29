package com.elibot.eco.forcesensor.impl.confignode;

import java.util.Locale;
import java.util.ResourceBundle;

import com.elibot.eco.forcesensor.impl.resource.ResourceSupport;

import cn.elibot.robot.plugin.contribution.configuration.ConfigurationAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.ConfigurationViewAPIProvider;
import cn.elibot.robot.plugin.contribution.configuration.ContributionConfiguration;
import cn.elibot.robot.plugin.contribution.configuration.SwingConfigurationNodeService;
import cn.elibot.robot.plugin.domain.data.DataModelWrapper;

public class ForceSensorConfigNodeService implements SwingConfigurationNodeService<ForceSensorConfigNodeContribution, ForceSensorConfigNodeView>{

    private ResourceBundle i18n = ResourceSupport.getDefaultResourceBundle();

    @Override
    public void configureContribution(ContributionConfiguration contributionConfiguration) {
        
        
    }

    @Override
    public String getTitle(Locale locale) {
        
        return i18n.getString("sensorBrand")+"-"+i18n.getString("sensorType")+i18n.getString("PluginConfigTitle");
    }

    @Override
    public ForceSensorConfigNodeView createView(ConfigurationViewAPIProvider viewApiProvider) {

        return new ForceSensorConfigNodeView(viewApiProvider, i18n) ;
    }

    @Override
    public ForceSensorConfigNodeContribution createConfigurationNode(ConfigurationAPIProvider configurationApiProvider,
            ForceSensorConfigNodeView configurationNodeView, DataModelWrapper context) {
        
        return new ForceSensorConfigNodeContribution(configurationApiProvider, configurationNodeView, context, i18n);
    }
    
}
