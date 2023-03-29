package com.elibot.eco.forcesensor.impl.daemon;


import java.net.MalformedURLException;
import java.net.URL;

import cn.elibot.robot.plugin.contribution.daemon.DaemonContribution;
import cn.elibot.robot.plugin.contribution.daemon.DaemonService;

public class MyDaemonService implements DaemonService{

    private DaemonContribution daemonContribution;

    @Override
    public void init(DaemonContribution contribution) {
        this.daemonContribution = contribution;
        try {
            daemonContribution.installResource(new URL("file:daemon/"));
        } catch (MalformedURLException ignore) {
            
        }
        daemonContribution.start();
        
    }

    @Override
    public URL getExecutable() {
        try {
            return new URL("file:daemon/adapter.py");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public DaemonContribution getDaemonContribution() {
        return daemonContribution;
    }
    
}