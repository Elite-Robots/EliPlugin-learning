#!/usr/bin/env python3
import os
import time
from socketserver import ThreadingMixIn

import serial
from modbus_tk import modbus_rtu
import modbus_tk.defines as cst
# xml rpc server in python3
from xmlrpc.server import SimpleXMLRPCServer

slave_id = 1
master = None

init_reg = 0x0100
auto_init_reg = 0x0504

width_reg = 0x0103
speed_reg = 0x0104
force_reg = 0x0101

current_width_reg = 0X0202
current_force_reg = 0X0101
current_speed_reg = 0X0104


def check_connection():
    try:
        global master
        master = modbus_rtu.RtuMaster(
            serial.Serial(port="/dev/ttyTCI0", baudrate=115200, bytesize=8, parity='N', stopbits=1, xonxoff=0))
        # master = modbus_rtu.RtuMaster(
        #     serial.Serial(port="COM11", baudrate=115200, bytesize=8, parity='N', stopbits=1, xonxoff=0))
        master.set_timeout(5.0)
        master.set_verbose(True)
    except Exception as e:
        print(e)


def init_gripper():
    try:
        if master is None:
            check_connection()
        master.execute(slave_id, cst.WRITE_SINGLE_REGISTER, init_reg, output_value=1)
        master.execute(slave_id, cst.WRITE_SINGLE_REGISTER, auto_init_reg, output_value=1)
    except Exception as e:
        print(e)


def move_to(width=0, speed=50, force=50):
    try:
        if master is None:
            check_connection()
        master.execute(slave_id, cst.WRITE_SINGLE_REGISTER, force_reg, output_value=force)
        master.execute(slave_id, cst.WRITE_SINGLE_REGISTER, speed_reg, output_value=speed)
        master.execute(slave_id, cst.WRITE_SINGLE_REGISTER, width_reg, output_value=width)
        return 0
    except Exception as e:
        print(e)


def get_status():
    try:
        if master is None:
            check_connection()
        current_width = master.execute(slave_id, cst.READ_HOLDING_REGISTERS, current_width_reg, 1)[0]
        current_force = master.execute(slave_id, cst.READ_HOLDING_REGISTERS, current_force_reg, 1)[0]
        current_speed = master.execute(slave_id, cst.READ_HOLDING_REGISTERS, current_speed_reg, 1)[0]
        return [current_width, current_force, current_speed]
    except Exception as e:
        print(e)
        return [-1, -1, -1]


class ThreadXMLRpcServer(ThreadingMixIn, SimpleXMLRPCServer):
    pass


check_connection()
addr = "127.0.0.1" if os.getenv("LOCAL_SIM") is not None else "6.0.0.10"
port = 60005
print("try to listen on {}:{}".format(addr, port))
server = ThreadXMLRpcServer((addr, port), allow_none=True)
server.register_function(init_gripper, "init_gripper")
server.register_function(move_to, "move_to")
server.register_function(get_status, "get_status")
server.serve_forever()

# test case
if __name__ == '__main__':
    print("main")
    init_gripper()
    time.sleep(2)
    while 1:
        move_to(500)
        time.sleep(2)
        move_to(1000)
        time.sleep(2)
        print(get_status())
