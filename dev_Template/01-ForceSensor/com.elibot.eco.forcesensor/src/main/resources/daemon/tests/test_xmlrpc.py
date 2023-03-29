#!/usr/bin/env python3
# coding=utf-8
'''
Author       : Jay zhangjunjie@elibot.cn
Date         : 2023-02-16 11:20:24
LastEditTime : 2023-03-01 18:05:25
LastEditors  : Jay zhangjunjie@elibot.cn
Description  : 
'''

from xmlrpc import client
import pytest


serverIP = "127.0.0.1"
serverPort = "9200"
url = "http://%s:%s/" % (serverIP, serverPort)


# @pytest.mark.parametrize("")
def test_setParams(serialPort="USB0", baudrate=115200, parity="E", stopBits=1, dataBits=8):
    with client.ServerProxy(url) as proxy:
        print(proxy.setParams(serialPort, baudrate, parity, stopBits, dataBits))

# test_setParams()

def test_connect():
    with client.ServerProxy(url) as proxy:
        print(proxy.connect())
test_connect()

def test_disconnect():
    with client.ServerProxy(url) as proxy:
        print(proxy.disconnect())



def test_getConnectStatus():
    with client.ServerProxy(url) as proxy:
        print(proxy.getConnectStatus())


def test_getForce():
    with client.ServerProxy(url) as proxy:
        print(proxy.getForce())


def test_getMoment():
    with client.ServerProxy(url) as proxy:
        print(proxy.getMoment())


def test_getTorque():
    with client.ServerProxy(url) as proxy:
        print(proxy.getTorque())

# @pytest.mark.parametrize("enable",True)
def test_monitor(enable=True):
    with client.ServerProxy(url) as proxy:
        print(proxy.monitor(enable))
