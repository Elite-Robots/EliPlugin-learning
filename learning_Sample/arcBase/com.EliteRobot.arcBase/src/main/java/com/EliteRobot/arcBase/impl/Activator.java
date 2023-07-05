package com.EliteRobot.arcBase.impl;

import cn.elibot.robot.commons.lang.resource.LocaleProvider;
import cn.elibot.robot.plugin.contribution.configuration.SwingConfigurationNodeService;
import cn.elibot.robot.plugin.contribution.task.SwingTaskNodeService;
import com.EliteRobot.arcBase.impl.configuration.arcConfigService;
import com.EliteRobot.arcBase.impl.resource.ResourceSupport;
import com.EliteRobot.arcBase.impl.task.arcTaskService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Activator for the OSGi bundle elibot-com.EliteRobot.arcBase.impl contribution
 */
public class Activator implements BundleActivator {
    private ServiceReference<LocaleProvider> localeProviderServiceReference;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        localeProviderServiceReference = bundleContext.getServiceReference(LocaleProvider.class);
        if (localeProviderServiceReference != null) {
            LocaleProvider localeProvider = bundleContext.getService(localeProviderServiceReference);
            if (localeProvider != null) {
                ResourceSupport.setLocaleProvider(localeProvider);
            }
        }
        bundleContext.registerService(SwingConfigurationNodeService.class,new arcConfigService(),null);
        bundleContext.registerService(SwingTaskNodeService.class,new arcTaskService(),null);

        System.out.println("com.EliteRobot.arcBase.impl.Activator says Hello World!");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("com.EliteRobot.arcBase.impl.Activator says Goodbye World!");
    }
}
