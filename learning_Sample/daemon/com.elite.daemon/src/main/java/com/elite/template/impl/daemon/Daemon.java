package com.elite.template.impl.daemon;

import cn.elibot.robot.plugin.contribution.daemon.DaemonContribution;
import cn.elibot.robot.plugin.contribution.daemon.DaemonService;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * daemon service is program running in system, which can be python, shell or c++ program etc.
 */
public class Daemon implements DaemonService {
    private DaemonContribution daemonContribution;

    @Override
    public void init(DaemonContribution daemon) {
        this.daemonContribution = daemon;
        try {
            daemonContribution.installResource(new URL("file:daemon/"));
            daemonContribution.start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public URL getExecutable() {
        try {
            return new URL("file:daemon/daemon.py");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DaemonContribution getDaemon() {
        return daemonContribution;
    }
}
