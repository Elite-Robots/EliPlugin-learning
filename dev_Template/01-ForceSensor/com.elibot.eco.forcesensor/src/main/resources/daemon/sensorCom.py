#!/usr/bin/env python3
# coding=utf-8
'''
Author       : Jay zhangjunjie@elibot.cn
Date         : 2023-02-08 19:02:57
LastEditTime : 2023-03-13 16:26:52
LastEditors  : Jay zhangjunjie@elibot.cn
Description  : 
'''

from abc import ABC, abstractmethod
from threading import Thread
from enum import Enum
from logging import Logger
import serial
import time
import socket
from rtsi import rtsi


class SensorData():
    
    def __init__(self, sensorData) -> None:
        self.__Fx = sensorData[0]           # unit:N
        self.__Fy = sensorData[1]           # unit:N
        self.__Fz = sensorData[2]           # unit:N
        self.__Mx = sensorData[3]           # unit:Nm
        self.__My = sensorData[4]           # unit:Nm
        self.__Mz = sensorData[5]           # unit:Nm


    @property
    def force(self):
        return [self.__Fx, self.__Fy, self.__Fz]

    @property
    def moment(self):
        return [self.__Mx, self.__My, self.__Mz]

    @property
    def torque(self):
        return [self.__Fx, self.__Fy, self.__Fz,self.__Mx, self.__My, self.__Mz]
        
    
class Protocol(Enum):
    SERIAL = "serial"
    TCP    = "TCP"    
    
class DataBits(Enum):
    FIVE  = 5
    SIX   = 6
    SEVEN = 7
    EIGHT = 8

class Baudrate(Enum):
    _110     = 110
    _300     = 300
    _600     = 600
    _1200    = 1200
    _2400    = 2400
    _4800    = 4800
    _9600    = 9600
    _14400   = 14400
    _28800   = 28800
    _19200   = 19200
    _38400   = 38400
    _56000   = 56000
    _57600   = 57600
    _115200  = 115200
    _230400  = 230400
    _460800  = 460800
    _921600  = 921600
    _1000000 = 1000000
    _2000000 = 2000000
    _4000000 = 4000000
    
    
class Parity(Enum):
    NONE = "N"
    ODD  = "O"
    EVEN = "E"


class StopBits(Enum):
    ONE = 1
    TWO = 2


class SerialPort(Enum):
    COM8 = "COM8"
    USB0 = "/dev/ttyUSB0"
    USB1 = "/dev/ttyUSB1"
    USB2 = "/dev/ttyUSB2"
    TCI  = "/dev/ttyELITE"
    # RS485  = "/dev/ttyELITE"



class HWConfigParams():
    protocol = None
    ip = "192.168.1.100"
    port = 502
    dataBits = DataBits.EIGHT
    baudrate = Baudrate._9600
    parity  = Parity.NONE
    stopBits = StopBits.ONE
    serialPort = SerialPort.USB0
    
    



class ForceSensor(ABC,Thread):
    """ Force Sensor ABC Class
    """
    
    def __init__(self, ) -> None:
        super().__init__(name="Force Sensor Data Convert Thread",daemon=True)
        self.__sensorData = SensorData([-999,-999,-999,-99,-99,-99])
        self.__offset = [0,0,0,0,0,0]
        self.__running = True
        self.__HW_init()
        self.start()


    def __HW_init(self):
        self.protocol = HWConfigParams.protocol.value
        self.ip = HWConfigParams.ip
        self.port = HWConfigParams.port
        self.dataBits = HWConfigParams.dataBits.value
        self.baudrate = HWConfigParams.baudrate.value
        self.parity = HWConfigParams.parity.value
        self.stopBits = HWConfigParams.stopBits.value
        self.serialPort = HWConfigParams.serialPort.value
        if self.protocol == Protocol.SERIAL.value:
            self.hwObj = serial.Serial(self.serialPort,self.baudrate,self.dataBits,self.parity,self.stopBits,timeout=0.5)
        else:
            self.hwObj = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
            self.hwObj.connect((self.ip, self.port))
        

    @property
    def sensorData(self):
        currentData = self.__sensorData.torque
        offsetSensorData = [currentData[i] - self.__offset[i] for i in range(6)]
        return SensorData(offsetSensorData)


    @property
    def offset(self):
        return self.__offset


    def __repr__(self) -> str:
        return ""


    def __delattr__(self, __name: str) -> None:
        self.hwObj.close()
        return super().__delattr__(__name)

    def run(self) -> None:
        rt = rtsi("6.0.0.9")
        rt.connect()
        rt.version_check()
        inputSub = rt.input_subscribe('external_force_torque') #输入订阅
        rt.start()
        self.beforeRunningTask()
        while self.__running:
            realSensorData = self.getRealSensorData()
            if realSensorData is not None:
                self.__sensorData = SensorData(realSensorData)
                inputSub.external_force_torque = self.__sensorData.torque
                rt.set_input(inputSub)
        rt.pause()
        rt.disconnect()
        self.afterRunningTask()

            
        
            
    @abstractmethod
    def getRealSensorData(self):
        """ need to send & recv & parse data ,and return [fx, fy, fz, mx, my, mz] (force unit: N, Moment: Nm) 
        """
        pass
    
    
    def beforeRunningTask(self):
        """ This method will run before thread executing
        """
        pass
    
    

    def afterRunningTask(self):
        """ This method will run after thread executing
        """
        pass

        
    def stop(self):
        self.__running = False
        self.hwObj.close()


    def zero(self):
        pass
        

    def unZero(self):
        pass


