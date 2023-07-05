package cn.elibot.digital.lightup.impl.resource;

import cn.elibot.robot.commons.lang.resource.LocaleProvider;
import cn.elibot.robot.commons.lang.resource.UTF8Control;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Resource Support for i18n
 *
 * @author shi
 */
public class ResourceSupport {
    private static LocaleProvider provider;
    private static String properties ="i18n.text";

    public static void setLocaleProvider(LocaleProvider provider) {
        ResourceSupport.provider = provider;
    }

    public static LocaleProvider getLocaleProvider() {
        return ResourceSupport.provider;
    }

    public static ResourceBundle getResourceBundle(Locale locale){
        return ResourceBundle.getBundle(properties, locale, new UTF8Control());
    }

    public static ResourceBundle getDefaultResourceBundle(){
        return ResourceBundle.getBundle(properties, provider.getLocale(), new UTF8Control());
    }
}
