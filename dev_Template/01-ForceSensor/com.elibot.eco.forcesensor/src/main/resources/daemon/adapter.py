#!/usr/bin/env python3
# coding=utf-8
"""
Author       : Jay zhangjunjie@elibot.cn
Date         : 2023-02-13 16:49:14
LastEditTime : 2023-02-13 19:05:05
LastEditors  : Jay zhangjunjie@elibot.cn
Description  : 
"""

import importlib
import inspect
import os
from socketserver import ThreadingMixIn
import traceback
from xmlrpc.server import SimpleXMLRPCServer

from pluginLog import logger
from sensorCom import *

HWConfigParams.protocol = Protocol.SERIAL
HWConfigParams.baudrate = Baudrate._115200
HWConfigParams.serialPort = SerialPort.COM8

HWConfigParams.parity = Parity.EVEN
HWConfigParams.stopBits = StopBits.ONE
HWConfigParams.dataBits = DataBits.EIGHT


def driverInstantiate():
    module = importlib.import_module(".", "drive")
    for attr in dir(module):
        var = getattr(module, attr)
        if inspect.isclass(var) and issubclass(var, ForceSensor) and var.__module__ == "drive":
            return var()



def enumMatch(value, enumCls):
    if isinstance(value, Enum):
        value = value.value
    if enumCls == SerialPort and "COM" not in value:
        value = "/dev/tty"+value
    if value in enumCls._value2member_map_:
        return enumCls(value)
    else:
        raise ValueError("param(%s) is not an enum(%s) value" % (value, enumCls))


class XMLRpcAdapter:
    def __init__(self) -> None:
        self.connectStatus = False
        self.driveObj = None
        self.monitorThreadLoop = True

    @logger.logit
    def setParams(self, serialPort, baudrate, parity, stopBits, dataBits, protocol=Protocol.SERIAL):
        try:
            HWConfigParams.protocol = enumMatch(protocol,Protocol)
            HWConfigParams.serialPort = enumMatch(serialPort,SerialPort)
            HWConfigParams.baudrate = enumMatch(baudrate,Baudrate)
            HWConfigParams.parity = enumMatch(parity,Parity)
            HWConfigParams.stopBits = enumMatch(stopBits,StopBits)
            HWConfigParams.dataBits = enumMatch(dataBits,DataBits)
            return True
        except Exception as e:
            traceback.print_exc()
            return False, e

    @logger.logit
    def connect(self):
        try:
            self.driveObj = driverInstantiate()
            self.connectStatus = True
            return True
        except Exception as e:
            traceback.print_exc()
            return False, e

    @logger.logit
    def disconnect(self):
        if self.driveObj is not None:
            self.driveObj.stop()
            self.connectStatus = False
            self.driveObj = None
        return True

    @logger.logit
    def getConnectStatus(self):
        if self.driveObj is not None:
            return self.driveObj.is_alive()
        else:
            return self.connectStatus

    def getForce(self):
        if self.driveObj is not None:
            return self.driveObj.sensorData.force
        else:
            return False

    @logger.logit
    def getMoment(self):
        if self.driveObj is not None:
            return self.driveObj.sensorData.moment
        else:
            return False

    @logger.logit
    def getTorque(self):
        if self.driveObj is not None:
            return self.driveObj.sensorData.torque
        else:
            return False

    @logger.logit
    def monitor(self, enable):
        def monitorThread():
            try:
                server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                server.bind(("127.0.0.1", 9105))
                server.listen(1)
                client, addr = server.accept()
                while self.monitorThreadLoop:
                        sendData = self.driveObj.sensorData.torque
                        sendData = ",".join(map(str, sendData))+"\r\n"
                        client.send(sendData.encode())
                        time.sleep(0.001)
            except Exception as e:
                self.monitorThreadLoop = False
                traceback.print_exc()
                # break
                server.shutdown(2)
                server.close()
                logger.info("done")

        try:
            if enable:
                self.monitorThreadLoop = True
                Thread(target=monitorThread, args=(), name="ForceSensorMonitor Thread", daemon=True).start()
            else:
                self.monitorThreadLoop = False
            return True
        except Exception as e:
            traceback.print_exc()
            return False


class ThreadXMLRpcServer(ThreadingMixIn, SimpleXMLRPCServer):
    pass


if __name__ == "__main__":

    xmlRpcServerHost = "6.0.0.10" if "EliRobot_" in os.environ.get("PWD") else "127.0.0.1"
    xmlRpcServerPort = 9104
    server = ThreadXMLRpcServer((xmlRpcServerHost, xmlRpcServerPort))
    server.register_introspection_functions()
    main = XMLRpcAdapter()
    server.register_instance(main)
    logger.debug("xmlRPC has started RPCServer ip is %s..." % xmlRpcServerHost)
    server.serve_forever()
