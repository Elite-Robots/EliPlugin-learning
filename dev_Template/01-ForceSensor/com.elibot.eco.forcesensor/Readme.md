## ForceSensor

版本要求：V2.3.0及以上，（TCI映射未测试）

### 项目介绍

力传感器只提供六维力数据，将整个力传感器插件功能抽象，作为一个插件模板提供。可供用户或内部工程师可以快速的开发一个力传感器插件。

### 项目功能

##### 串口连接参数面板

- [X] 提供一个简易的tool io locker（主要是配置串口相关参数）
- [X] 发现x86上的串口设备（包含映射后的TCI）
- [X] 修改连接参数后会直接映射到tool io locker上

##### daemon

- [X] 实现python对串口的访问
- [X] 抽离传感器驱动层，
- [X] 定义监控的socker端口，用于较高频的传感器数据返回到java侧，用于界面监控用
- [X] 传感器数据对接到RTSI的力控寄存器中（点击连接后，传感器数据即直接传送至RTSI的力控寄存器中）

##### 配置节点

- [X] 提供基础的串口连接相关参数
- [X] 较高频的数据监控（数据监控只将数据在界面上显示，实际上连接后数据就已经传送到RTSI的力控寄存器中）
- [ ] 数据绘制折线图

##### 任务节点

- [X] 封装一个指令，用于启用或关闭RTSI中的力传感器寄存器数据

### 项目效果

#### 静态展示

![ ](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303031929767.png)

![image-20230303193001556](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303031930636.png)

#### 动态展示

##### 获取数据

![forcesensor](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303031934905.gif)

##### 发现设备

![fsFindDev](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303031944838.gif)

##### tool io设置

![toolSettings](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303031944342.gif)

### 项目结构

```bash
.java/com/elibot/eco/forcesensor/impl
├── Activator.java
├── common									
│   ├── EXmlRpcClient.java							# xmlRpcClient类
│   ├── SensorDataPanel.java						# 传感器数据面板
│   └── SerialConnectParamsPanel.java				# 串口连接参数面板
├── confignode									
│   ├── ForceSensorConfigNodeContribution.java		# 配置节点贡献
│   ├── ForceSensorConfigNodeService.java			# 配置节点服务
│   └── ForceSensorConfigNodeView.java				# 配置节点视图
├── daemon										
│   └── MyDaemonService.java						# daemon服务
├── resource
│   ├── ImageHelper.java
│   └── ResourceSupport.java
└── taskNode
    ├── EnableFTDTaskNodeContribution.java			# 任务节点贡献
    ├── EnableFTDTaskNodeService.java				# 任务节点服务
    └── EnableFTDTaskNodeview.java					# 任务节点视图

# -------------------------------------------------------------------------------
.
├── daemon
│   ├── adapter.py									# xmlrpc服务器实现,适配串口方法
│   ├── drive.py									# 传感器驱动层(需继承sensorCom类)
│   ├── pluginLog.py								# 日志
│   ├── rtsi										# RTSI(V2.3.0)
│   │   ├── __init__.py
│   │   ├── rtsi.py
│   │   └── serialize.py
│   ├── sensorCom.py								# 传感器基础方法方法实现(定义父类)
│   └── tests
│       └── test_xmlrpc.py							# xmlrpc client 测试
├── i18n											# 国际化文件
│   ├── text_en.properties
│   ├── text.properties
│   └── text_zh.properties
├── images
│   └── icons
│       ├── demo.png
│       └── logo.png
└── META-INF
    └── LICENSE
```

### 如何使用该模板

1. 新建插件模板

   > SRI替换为品牌名称
   >

   ![image-20230304230511064](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303042305102.png)
2. 实现daemon中的drive驱动

   ![drive](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303032003726.png)

   需要继承 `ForceSensor`类，并且实现 `getRealSensorData()`方法，该方法需要返回一个列表 `[fx,fy,fz,mx,my,mz]`，继承该类后，`self.hwObj`即为已经打开的串口对象，你可以使用正常串口对象的相关接口。

   也提供了`beforeRunningTask`和`afterRunningTask`方法，用于获取数据线程开始前执行一些任务（如传感器状态初始化等）和获取数据线程结束后执行一些任务。
3. 修改端口号

   `adapter.py`中的 `xmlRpcServerPort`为实际申请的rpc端口号；

   ![image-20230304222355742](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303042223850.png)

   java端xmlRpc请求的rpc端口号

   ![image-20230304222339967](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303042223082.png)

   python侧输送传感器的端口号为实际申请的端口号

   ![image-20230303200701213](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303032007335.png)

   java侧监控的端口号为实际申请的监控端口号

   ![image-20230304222539925](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303042225017.png)
4. 修改pom清单字段

   ```xml
   <version>2.3.0-RELEASE</version>
   <!-- 格式统一为：CS_SW_VERSION-TYPE -->
   <!-- TYPE->SNAPSHOT:不稳定,尚处于开发中的版本 -->
   <!-- TYPE->RELEASE:稳定版本 -->

   <!-- 修改为对应品牌的名称，该名称会影响生成的toolIOLocker -->
   <name>EliRobots-ECO:SRI-ForceSensor</name>

   <!-- 插件描述 -->
   <plugin.description>SRI ForseSensor Drive For Elite CS Serial Robot</plugin.description>
   ```
5. 修改国际化资源文件

   > 修改sensorBrand和globalEFTDFuncTitle字段，其中globalEFTDFuncTitle必须为英文
   >
   > 根据实际需求添加sensorType字段用于描述力传感器型号
   >

   ![image-20230304224534515](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303042245592.png)
6. 修改品牌logo

   > 名称需要logo.png
   >

   ![image-20230304224828949](https://aliyun-oss-img-bed.oss-cn-hangzhou.aliyuncs.com/elite_imgbed/202303042248104.png)
7.
