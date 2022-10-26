package consoleapp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public abstract class ConsoleApplication {
	private Map<String, CommandConsumer> commands = new HashMap<>();
	private Map<String, String> help = new LinkedHashMap<>();

	private String cmdDelim = " ", multiCmdDelim = ";";
	private String helpCmd = "help", exitCmd = "exit";
	private String promptStr = "> ";
	private String afterInput = "", afterOutput = "";

	private String helpMenuMsg = "Command list:";
	private boolean shutdownOnException = false;

	private Scanner scanner;

	public ConsoleApplication() {
		addCommand(helpCmd, () -> printHelpMenu());
		addCommand(exitCmd, () -> exit());
	}

	public void listenForCommands() {
		scanner = new Scanner(System.in);
		String in = "";
		while (true) {
			System.out.print(promptStr);
			in = scanner.nextLine();
			if (!afterInput.isEmpty())
				System.out.print(afterInput);
			try {
				executeCommand(in);
			} catch (Exception e) {
				e.printStackTrace();
				if (shutdownOnException)
					exit();
			}
			if (!afterOutput.isEmpty())
				System.out.print(afterOutput);
		}
	}

	public void executeCommand(String in) {
		String commandList[] = in.trim().split(multiCmdDelim, 2);
		String tokens[] = commandList[0].split(cmdDelim);
		String cmd = tokens[0].strip();

		if (!commands.containsKey(cmd))
			return;

		Arguments args = new Arguments(Arrays.copyOfRange(tokens, 1, tokens.length));
		commands.get(cmd).accept(args);

		if (commandList.length > 1)
			executeCommand(commandList[1]);
	}

	public void printHelpMenu() {
		System.out.println(helpMenuMsg);
		help
				.forEach((cmd, description) -> System.out.println(cmd + " - " + description));
	}

	public void exit() {
		if (scanner != null)
			scanner.close();
		System.exit(0);
	}

	protected ConsoleApplication addCommand(String name, String description, CommandConsumer cmdAction) {
		String helpName = name;
		/* If name specifies command's arguments - use it only for help menu
		 *  and strip everything after the `cmdDelim` for this command's mapping. */
		if (name.contains(cmdDelim)) {
			helpName = name;
			name = name.split(cmdDelim, 2)[0];
		}

		commands.put(name, cmdAction);

		if (description != null)
			help.put(helpName, description);

		return this;
	}

	protected ConsoleApplication addCommand(String name, CommandConsumer cmdAction) {
		return addCommand(name, null, cmdAction);
	}

	protected ConsoleApplication addCommand(String name, String description, Runnable noArgCmd) {
		return addCommand(name, description, args -> noArgCmd.run());
	}

	protected ConsoleApplication addCommand(String name, Runnable noArgCmd) {
		return addCommand(name, null, noArgCmd);
	}

	protected void executeConsoleArgs(String[] args) {
		if (args.length > 1)
			executeCommand(String.join(cmdDelim, args));
	}

	public String getCmdDelim() {
		return cmdDelim;
	}

	public void setCmdDelim(String cmdDelim) {
		this.cmdDelim = cmdDelim;
	}

	public String getMultiCmdDelim() {
		return multiCmdDelim;
	}

	public void setMultiCmdDelim(String multiCmdDelim) {
		this.multiCmdDelim = multiCmdDelim;
	}

	public String getPromptStr() {
		return promptStr;
	}

	public void setPromptStr(String promptStr) {
		this.promptStr = promptStr;
	}

	public String getAfterInput() {
		return afterInput;
	}

	public void setAfterInput(String afterInput) {
		this.afterInput = afterInput;
	}

	public String getAfterOutput() {
		return afterOutput;
	}

	public void setAfterOutput(String afterOutput) {
		this.afterOutput = afterOutput;
	}

	public String getHelpMenuMsg() {
		return helpMenuMsg;
	}

	public void setHelpMenuMsg(String helpMenuMsg) {
		this.helpMenuMsg = helpMenuMsg;
	}

	public boolean isShutdownOnException() {
		return shutdownOnException;
	}

	public void setShutdownOnException(boolean shutdownOnException) {
		this.shutdownOnException = shutdownOnException;
	}

	public static interface CommandConsumer extends Consumer<Arguments> {}
}
