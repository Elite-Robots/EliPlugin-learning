package cn.elibot.digital.lightup.impl;

import cn.elibot.digital.lightup.impl.resource.ResourceSupport;
import cn.elibot.robot.commons.lang.resource.LocaleProvider;
import cn.elibot.robot.plugin.contribution.task.SwingTaskNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Hello world activator for the OSGi bundle elibot-cn.elibot.digital.lightup.impl contribution
 *
 * @author shi
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
            bundleContext.registerService(SwingTaskNodeService.class,new LightUpTaskNodeService(),null);
        }
        System.out.println("cn.elibot.digital.lightup.impl.Activator says Hello World!");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("cn.elibot.digital.lightup.impl.Activator says Goodbye World!");
    }
}
