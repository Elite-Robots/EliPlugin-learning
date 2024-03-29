#! /usr/bin/env python3

import functools
import inspect
import logging
import os
import sys
from logging.handlers import RotatingFileHandler
from typing import Optional


class Log:
    """
        用于CS插件 xmlrpc请求的日志记录

        Examples
        --------
            >>> from log import Log
            >>> logger = Log("AG95")    
            or 
            >>> logger = Log("AG95", log_path = "your log file path")

            >>> logger.info("info 级别的记录并不会使log_count增加")
            2022-07-07 02:07:41.632 | AG95:   0 | INFO   | info 级别的记录并不会使log_count增加

            >>> logger.debug("info 级别的记录并不会使log_count增加")
            2022-07-07 02:07:41.633 | AG95:   0 | DEBUG  | info 级别的记录并不会使log_count增加


            >>> logger.info("此时 log_count 已经变为1")
            2022-07-07 02:09:59.700 | AG95:   1 | INFO   | 此时 log_count 已经变为1

            # 同时还提供了一个xmlrpc启动的便捷日志方法
            >>> logger.xmlRPC_start_info("192.168.1.200", 40)
            2022-07-07 02:09:59.700 | AG95:   1 | INFO   | ----------------------------------------
            2022-07-07 02:09:59.700 | AG95:   1 | INFO   | -----XMLRPC server has been started-----
            2022-07-07 02:09:59.701 | AG95:   1 | INFO   | -----     Host: 192.168.1.200      -----
            2022-07-07 02:09:59.701 | AG95:   1 | INFO   | -----           Port: 40           -----
            2022-07-07 02:09:59.701 | AG95:   1 | INFO   | ----------------------------------------
            
            # 现在定义一个函数,并使用提供的装饰器装饰
            >>> @logger.logit
            >>> def connect_gripper(a, b, c:int=9):
            ...     print(a, b, c)
            ...     print("----")
            >>> connect_gripper(1, 4)
            2022-07-07 02:09:59.704 | AG95:   2 | DEBUG  | Func Call: log.py:253| Func: connect_gripper(a:int = 1, b:int = 4, c:int = 9)
            # 其中AG95:2 代表为第二次debug记录,Func Call:则表示触发记录的文件名称和行数,Func:则表示被调用的函数名以及具体的参数
    """
    FMT_DICT = {
        "ERROR": "\033[31mERROR\033[0m",
        "INFO": "\033[91m%s\033[0m" % "红色",
        "DEBUG": "\033[31mERROR\033[0m",
        "WARNING": "\033[31mERROR\033[0m",
        "CRITICAL": "\033[31mERROR\033[0m",
        "ERROR": "\033[31mERROR\033[0m"
    }
    DEFAULT_LOG_PATH = r"/home/elite/EliRobot/program/"

    def __init__(self, package_name: str, log_path: Optional[str] = None):

        self.logger = logging.getLogger(package_name)
        self.logger.setLevel(logging.DEBUG)
        self.__log_count = 0
        if log_path is not None:
            self.__log_path = log_path
        else:
            self.__log_path = package_name + ".log"
            if sys.platform == "linux":
                # todo:判断文件是否存在
                if os.path.exists(self.DEFAULT_LOG_PATH):
                    self.__log_path = self.DEFAULT_LOG_PATH + self.__log_path

        self._log_config()

    def _log_config(self):
        """配置 logging 样式
        """
        fmt = logging.Formatter(
            fmt="%(asctime)s.%(msecs)03d | %(name)s:%(log_count)04s | " +
            "%(levelname)-5s " + " | %(message)s",
            datefmt='%Y-%m-%d %I:%M:%S')
        flt = logging.Filter()
        flt.filter = self.__filter
        fh = RotatingFileHandler(self.__log_path,
                                 mode="w+",
                                 maxBytes=1 * 1024 * 1024,
                                 backupCount=3)
        sh = logging.StreamHandler()
        fh.setLevel(logging.DEBUG)
        sh.setLevel(logging.DEBUG)
        fh.setFormatter(fmt)
        fh.addFilter(flt)
        sh.setFormatter(fmt)
        sh.addFilter(flt)
        self.logger.addHandler(fh)
        self.logger.addHandler(sh)

    def __filter(self, record: logging.LogRecord):
        """实现一些自定义的日志格式
        #todo: 待实现颜色
        """
        record.log_count = self.__log_count
        return True

    @property
    def log_state(self) -> bool:
        """log 的状态

        Returns:
            bool: True:开启日志记录,False:关闭日志记录
        """
        return not self.logger.disabled

    @log_state.setter
    def log_state(self, state: bool) -> None:
        """修改 log 的状态

        Args:
            state (bool): True:开启日志记录,False:关闭日志记录
        """
        self.logger.disabled = not state

    @property
    def log_count(self) -> int:
        """返回日志 debug 记录的次数

        Returns:
            int: debug 记录的次数
        """
        return self.__log_count

    def __get_func_args(self, func: object, args: tuple) -> None:
        """获取函数的参数信息

        Args:
            func (object)): 函数
            args (tuple): 参数列表
        """
        # 获取函数参数返回一个有序字典
        parms = inspect.signature(func).parameters
        # 获取参数名，和参数类型
        # msg_args = "Args:"
        msg_args = "("
        temp = 0

        for name, parm in parms.items():
            if name == "self":
                temp += 1
                continue
            if temp < len(args):
                if temp < len(args):
                    value = args[temp]
                    value_type = ":" + type(value).__name__
                else:
                    value = str(parm).split("=")[-1]
                    value_type = ""
            else:
                if hasattr(parm, "default"):
                    value = str(parm).split("=")[-1].replace(" ", "")
                    if parm.annotation == inspect._empty:
                        value_type = ""
                    else:
                        _type = str(parm.annotation)
                        if "class" in _type:
                            value_type = ":" + _type[8:-2]
                        else:
                            value_type = ":" + str(parm.annotation)

                else:
                    value = ","
                    value_type = ","

            msg_args += name + "%s" % value_type + " = %s" % value
            temp += 1
            if temp != len(parms.items()): msg_args += ", "

        stack = inspect.stack()
        line = stack[2].lineno
        module_name = stack[2].filename.split("/")[-1]

        return msg_args + ")", line, module_name

    def logit(self, func):
        """装饰器实现
        """

        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            name = func.__name__
            # 获取函数参数返回一个有序字典
            func_args, lineno, module_name = self.__get_func_args(func, args)
            # self.debug(f"Func Call: {module_name}:{lineno}| Func: {name}" +
            #            func_args)
            self.debug("Func Call: %s:%s| Func: %s"%(module_name,lineno,name)+func_args)
            return func(*args, **kwargs)

        # self.info(wrapper)
        return wrapper

    def debug(self, msg: str, inc_count: bool = True) -> None:
        """log debug ,该方法调用会使内部debug count 增加

        Args:
            msg (str): 要记录的日志信息
            inc_count (bool, optional): . Defaults to True.
        """
        if inc_count:
            self.__log_count += 1
        self.logger.debug(msg)

    def info(self, msg: str) -> None:
        """log info ,该方法调用不会使内部debug count 增加

        Args:
            msg (str): 要记录的日志信息
        """
        self.logger.info(msg)

    def xmlRPC_start_info(self, host: str, port: int):
        """打印并记录

        Args:
            host (str): rpc host
            port (int): rpc port
        """
        MAX_STR_LENGTH = 30
        MAX_PRINT_LENGTH = 40
        t = []
        t.append("-".center(MAX_PRINT_LENGTH, "-"))
        t.append("XMLRPC server has been started".center(
            MAX_STR_LENGTH, " ").center(MAX_PRINT_LENGTH, "-"))
        t.append("Host: %s"%host.center(MAX_STR_LENGTH,
                                        " ").center(MAX_PRINT_LENGTH, "-"))
        t.append("Port: %s"%port.center(MAX_STR_LENGTH,
                                        " ").center(MAX_PRINT_LENGTH, "-"))
        t.append("-".center(MAX_PRINT_LENGTH, "-"))
        [self.info(i) for i in t]


# logger = Log("ForceSensor","/tmp/eliPlugin.log")
logger = Log("ForceSensor")
if __name__ == '__main__':

    logger = Log("AG95")

    logger.info("info 级别的记录并不会使log_count增加")
    logger.debug("info 级别的记录并不会使log_count增加")
    logger.info("此时 log_count 已经变为1")

    @logger.logit
    def connect_gripper(a, b, c: int = 9):
        print(a, b, c)
        print("----")

    logger.xmlRPC_start_info("192.168.1.200", 40)
    connect_gripper(1, 4)

    logger.info(logger.log_state)

    logger.info(logger.log_state)
