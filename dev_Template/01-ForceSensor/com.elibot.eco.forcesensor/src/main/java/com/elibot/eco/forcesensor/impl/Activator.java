package com.elibot.eco.forcesensor.impl;

import cn.elibot.robot.commons.lang.resource.LocaleProvider;
import cn.elibot.robot.plugin.contribution.configuration.SwingConfigurationNodeService;
import cn.elibot.robot.plugin.contribution.daemon.DaemonService;
import cn.elibot.robot.plugin.contribution.task.SwingTaskNodeService;

import com.elibot.eco.forcesensor.impl.confignode.ForceSensorConfigNodeService;
import com.elibot.eco.forcesensor.impl.daemon.MyDaemonService;
import com.elibot.eco.forcesensor.impl.resource.ResourceSupport;
import com.elibot.eco.forcesensor.impl.taskNode.EnableFTDTaskNodeService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Activator for the OSGi bundle elibot-com.elibot.eco.forcesensor.impl contribution
 */
public class Activator implements BundleActivator {
    private ServiceReference<LocaleProvider> localeProviderServiceReference;
    private MyDaemonService daemonService;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        localeProviderServiceReference = bundleContext.getServiceReference(LocaleProvider.class);
        if (localeProviderServiceReference != null) {
            LocaleProvider localeProvider = bundleContext.getService(localeProviderServiceReference);
            if (localeProvider != null) {
                ResourceSupport.setLocaleProvider(localeProvider);
            }
        }
        System.out.println("com.elibot.eco.forcesensor.impl.Activator says Hello World!");
        bundleContext.registerService(SwingConfigurationNodeService.class, new ForceSensorConfigNodeService(), null);
        this.daemonService = new MyDaemonService();
        bundleContext.registerService(DaemonService.class, this.daemonService, null);
        bundleContext.registerService(SwingTaskNodeService.class, new EnableFTDTaskNodeService(), null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        this.daemonService.getDaemonContribution().stop();
        System.out.println("com.elibot.eco.forcesensor.impl.Activator says Goodbye World!");
    }
}
