package com.elite.template.impl.resource;

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
    private static String properties = "i18n.text";
    private static ResourceBundle resourceBundle;

    public static void setLocaleProvider(LocaleProvider provider) {
        ResourceSupport.provider = provider;
        Locale locale = ResourceSupport.getLocalProvider().getLocale();
        resourceBundle = ResourceBundle.getBundle(properties, locale, new UTF8Control());
    }

    public static LocaleProvider getLocaleProvider() {
        return provider;
    }

    public static LocaleProvider getLocalProvider() {
        return provider;
    }

    public static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(properties, locale, new UTF8Control());
    }

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static ResourceBundle getDefaultResourceBundle() {
        return ResourceBundle.getBundle(properties, provider.getLocale(), new UTF8Control());
    }
}
