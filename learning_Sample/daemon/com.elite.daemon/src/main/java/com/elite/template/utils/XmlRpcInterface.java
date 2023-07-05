package com.elite.template.utils;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class XmlRpcInterface {
    private final XmlRpcClient client;

    public XmlRpcInterface(String host, int port) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setEnabledForExtensions(true);
        try {
            config.setServerURL(new URL("http://" + host + ":" + port + "/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        config.setConnectionTimeout(5000); //1s
        config.setReplyTimeout(5000);
        client = new XmlRpcClient();
        client.setConfig(config);
    }

    public Integer initGripper() {
        ArrayList<Object> args = new ArrayList<>();
        Object result = null;
        try {
            result = client.execute("init_gripper", args);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        return (Integer) result;
    }

    public Integer moveTo(Integer width, Integer speed, Integer force) {
        ArrayList<Object> args = new ArrayList<>();
        args.add(width);
        args.add(speed);
        args.add(force);
        Object result = null;
        try {
            result = client.execute("move_to", args);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        return (Integer) result;
    }

    public Integer[] getStatus() {
        ArrayList<Integer> args = new ArrayList<>();
        Object[] result = null;
        try {
            result = (Object[]) client.execute("get_status", args);

            Integer[] integerArray = new Integer[result.length];

            for (int i = 0; i < result.length; i++) {
                integerArray[i] = (Integer) result[i];
            }
            return integerArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
