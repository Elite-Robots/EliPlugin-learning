#!/usr/bin/env python3
# coding=utf-8
"""
Author       : Jay zhangjunjie@elibot.cn
Date         : 2023-02-13 16:48:04
LastEditTime : 2023-02-13 21:31:08
LastEditors  : zhangjunjie 1157860961@qq.com
Description  : SRI Force Sensor Drive
"""

from sensorCom import ForceSensor
import struct
from pluginLog import logger

class SRI(ForceSensor):
    def getRealSensorData(self):
        self.hwObj.flush()
        sensorData = self.hwObj.read(29 * 2).hex()
        
        count = sensorData.count("aa55")
        if count <= 0:
            return None
        if count > 2:
            # logger.info("===========================")
            return None
        if count == 1:
            head = sensorData.find("aa55")
            foot = head + 58
        else:
            head = sensorData.find("aa55")
            foot = sensorData.find("aa55", head + 3)
        # logger.info([head,foot,sensorData.count("aa55")])
        if len(sensorData[head:foot]) != 58:
            return None
        if head > -1:
            usefulData = sensorData[head:foot]
            frameHeader = usefulData[:4]
            packageLen = usefulData[4:6]
            packageNo = usefulData[6:8]
            
            fxd = usefulData[8:16]
            fyd = usefulData[16:24]
            fzd = usefulData[24:32]
            mxd = usefulData[32:40]
            myd = usefulData[40:48]
            mzd = usefulData[48:56]
            crc = usefulData[56:]
            
            # print(usefulData, len(usefulData))
            # print(frameHeader, packageLen, packageNo, fxd, fyd, fzd, mxd, myd, mzd, crc)

            fx = self.convert(fxd)
            fy = self.convert(fyd)
            fz = self.convert(fzd)
            mx = self.convert(mxd)
            my = self.convert(myd)
            mz = self.convert(mzd)
            # logger.info([fx, fy, fz, mx, my, mz])
            return [fx, fy, fz, mx, my, mz]

    def convert(self, singleData):
        hexData = bytearray.fromhex(singleData)
        return struct.unpack("<f", hexData)[0]
