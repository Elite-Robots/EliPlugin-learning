package com.elibot.eco.forcesensor.impl.common;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class EXmlRpcClient {

    private final XmlRpcClient client;
    private String host;
    private int port;

    public EXmlRpcClient(int port) {
        this.host = System.getenv("LOCAL_SIM") == null ? "6.0.0.10" : "127.0.0.1";
        this.port = port;
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setEnabledForExtensions(true);
        try {
            config.setServerURL(new URL("http://" + host + ":" + port + "/RPC2"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // 1s
        config.setConnectionTimeout(1000);
        client = new XmlRpcClient();
        client.setConfig(config);
    }

    public String getRpcHost(){
        return host;
    }

    public int getPort(){
        return port;
    }


    public <T> ArrayList<Object> request(String function, T... args){
        try {
            ArrayList<Object> params = new ArrayList<Object>();
            for (int index = 0; index < args.length; index++) {
                params.add(args[index]);
            }
            Object result = this.client.execute(function, args);
            return this.respConvert(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<Object> request(String function) {
        try {
            ArrayList<String> args = new ArrayList<String>();
            Object result = this.client.execute(function, args);
            return this.respConvert(result);
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return null;
        }
    }


    public <M> ArrayList<Object> params(M... args) {
        ArrayList<Object> params = new ArrayList<Object>();
        for (int index = 0; index < args.length; index++) {
            params.add(args[index]);
        }
        return params;
    }

    private ArrayList<Object> respConvert(Object params) {
        ArrayList<Object> ret = new ArrayList<Object>();
        if (params instanceof Object[]){
            Object[] temp = (Object[]) params;
            for (int i = 0; i < temp.length; i++) {
                ret.add(temp[i]);
            }
        }else{
            ret.add(params);
        }
        return ret;
    }
}
