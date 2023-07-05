package com.elite.template.impl.communicator;

/**
 * @author elite Zhang
 */
public class InterfaceTester {

	/**
	 * This class can be used to test the Exporter and Sender
	 * Hit the Play button in your IDE, to execute this
	 * 
	 */
	
	public static void main(String[] args) {
		/**
		 * Testing Sender
		 */
		ScriptSender sender = new ScriptSender();
		
		ScriptCommand senderCommand = new ScriptCommand("SenderCommand");
		senderCommand.appendLine("textmsg(\"Add test msg to the logfile...\")");
		
		sender.sendScriptCommand(senderCommand);
		
		/**
		 * Testing Exporter
		 */
		ScriptExporter export = new ScriptExporter();
		
		ScriptCommand commandString = new ScriptCommand("testCommand1");
		commandString.appendLine("pose = get_actual_tcp_pose()");
		commandString.appendLine("x_Value = pose[0]");
		String resultString = export.exportStringFromEliScript(commandString, "x_Value");
		System.out.println("String result is: "+resultString);

		ScriptCommand commandInt = new ScriptCommand("testCommand2");
		commandInt.appendLine("var_1 = 2020 + 20");
		int resultInt = export.exportIntegerFromEliScript(commandInt, "var_1");
		System.out.println("Integer result is: "+resultInt);
	}

}
