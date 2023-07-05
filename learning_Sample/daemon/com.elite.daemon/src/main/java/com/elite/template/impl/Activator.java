package com.elite.template.impl;

import cn.elibot.robot.commons.lang.resource.LocaleProvider;
import cn.elibot.robot.plugin.contribution.configuration.SwingConfigurationNodeService;
import cn.elibot.robot.plugin.contribution.daemon.DaemonService;
import cn.elibot.robot.plugin.contribution.task.SwingTaskNodeService;
import com.elite.template.impl.configure.DaemonConfigurationNodeService;
import com.elite.template.impl.daemon.Daemon;
import com.elite.template.impl.resource.ResourceSupport;
import com.elite.template.impl.task.DaemonTaskNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Activator for the OSGi bundle com.elite.template.impl contribution
 */
public class Activator implements BundleActivator {
    private ServiceReference<LocaleProvider> localeProviderServiceReference;
    private Daemon daemon;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        localeProviderServiceReference = bundleContext.getServiceReference(LocaleProvider.class);
        if (localeProviderServiceReference != null) {
            LocaleProvider localeProvider = bundleContext.getService(localeProviderServiceReference);
            if (localeProvider != null) {
                ResourceSupport.setLocaleProvider(localeProvider);
            }
        }

        // register daemon
        daemon = new Daemon();
        bundleContext.registerService(DaemonService.class, daemon, null);

        // register configuration node
        bundleContext.registerService(SwingConfigurationNodeService.class, new DaemonConfigurationNodeService(), null);

        // register task node
        bundleContext.registerService(SwingTaskNodeService.class, new DaemonTaskNodeService(), null);
        System.out.println("com.elite.daemon.impl.Activator says Hello World!");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("com.elite.daemon.impl.Activator says Goodbye World!");
    }
}
