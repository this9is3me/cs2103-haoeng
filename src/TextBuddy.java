import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author Hao Eng Assumption: the file will be saved after every operation.
 */

public class TextBuddy {
	// for numbering in the list.
	public static int count = 0;
	public static String WELCOME_MSG = "Welcome to TextBuddy. %s is ready for use.\n",
			COMMAND = "command: ",
			ADD = "add",
			DISPLAY = "display",
			CLEAR = "clear", DELETE = "delete", EXIT = "exit";
	public static Vector<String> list = new Vector<String>();
	public static Scanner sc = new Scanner(System.in);

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		String filename = retrieveFileName(args);
		copyOldfileToList(filename, list);
		printMsg(filename);
		commandHandler(filename);
	}

	/**
	 * @param filename
	 * @throws FileNotFoundException
	 */
	private static void commandHandler(String filename)
			throws FileNotFoundException {
		String command = null;
		do {
			command = readCommand();
			checkWordAssignAction(filename, command);

		} while (!keyword(command, EXIT)); // not an exit command
	}

	/**
	 * @param filename
	 * @param command
	 * @throws FileNotFoundException
	 */
	private static void checkWordAssignAction(String filename, String command)
			throws FileNotFoundException {
		// checking the action for the command
		if (keyword(command, ADD)) {
			String text = replacingAdd(command);
			list.add(text);
			writeTextToFile(text, list.size(), filename);
			printSuccessfulAddMsg(filename, text);
		}

		if (keyword(command, DISPLAY)) {
			printContentsOfFile(filename);
		}

		if (keyword(command, CLEAR)) {
			clearFile(filename);
			list.clear();
			printSuccessfulDeleteMsg(filename);
		}

		if (keyword(command, DELETE)) {
			String lineNum = replacingDelete(command);
			removeSelectedLine(lineNum, list, filename);
		}

		if (!keyword(command, EXIT)) {
			printCommandLine();
		}

		if (keyword(command, EXIT)) {
			closeFile();
		}
	}

	/**
	 * 
	 * @param command
	 * @param word
	 * @return
	 */
	private static boolean keyword(String command, String word) {
		if (command.startsWith(word)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return
	 */
	private static String readCommand() {
		String command;
		command = sc.nextLine();
		return command;
	}

	/**
	 * @param command
	 * @return
	 */
	private static String replacingDelete(String command) {
		String text = command.replace("delete ", "");
		return text;
	}

	private static void closeFile() {
		sc.close();
	}

	private static void printCommandLine() {
		System.out.print("command: ");
	}

	/**
	 * @param filename
	 */
	private static void printSuccessfulDeleteMsg(String filename) {
		System.out.println("all content deleted from " + filename);
	}

	/**
	 * @param filename
	 * @param text
	 */
	private static void printSuccessfulAddMsg(String filename, String text) {
		System.out.println("added to " + filename + ": " + "\"" + text + "\"");
	}

	/**
	 * @param command
	 * @return
	 */
	private static String replacingAdd(String command) {
		String text = command.replace("add ", "");
		return text;
	}

	/**
	 * @param args
	 * @return
	 */
	private static String retrieveFileName(String[] args) {
		String filename = null;
		// taking in the filename
		if (args.length > 0) {
			filename = args[0];
		}
		return filename;
	}

	/**
	 * @param filename
	 */
	private static void printMsg(String filename) {
		System.out.printf(WELCOME_MSG, filename);
		System.out.print(COMMAND);
	}

	/**
	 * @param filename
	 * @param list
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void copyOldfileToList(String filename, Vector<String> list)
			throws FileNotFoundException, IOException {
		File file = new File(filename);
		// if the file contains previous content
		if (file.length() != 0) {
			// copy file to the vector
			String line = null;
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(file);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				// remove the number in front of the sentence
				line = line.replaceAll("[0-9]+. ", "");
				list.add(line);
				count++;
			}
			bufferedReader.close();
		}
	}

	public static void printContentsOfFile(String file) {

		// This will reference one line at a time
		String line = null;
		File content = new File(file);
		if (content.length() == 0) {
			System.out.println(file + " is empty");
		}

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(file);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}

			// Always close files.
			bufferedReader.close();
		} catch (IOException ex) {
			System.out.println("Error reading file '" + file + "'");
		}
	}

	public static void clearFile(String filename) throws FileNotFoundException {
		try {
			File file = new File(filename);
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("");
			bw.close();

		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeTextToFile(String text, int index, String filename) {
		try {
			File file = new File(filename);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(index + ". " + text + "\n");
			bw.close();

		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void removeSelectedLine(String lineNum, Vector<String> list,
			String filename) throws FileNotFoundException {
		Integer position;
		try {
			position = Integer.parseInt(lineNum);
		} catch (NumberFormatException e) {
			System.out.println("Delete takes a number argument.");
			return;
		}
		// if the number to delete is out of the list
		if (position > list.size() || position <= 0) {
			System.out.println("Invalid deletion");
			return;
		}

		System.out.println("deleted from " + filename + ": \""
				+ list.get(position - 1) + "\"");

		// rewriting files
		list.removeElementAt(position - 1);
		clearFile(filename);
		Iterator<String> itr = list.iterator();
		int count = 0;
		while (itr.hasNext()) {
			count++;
			writeTextToFile(itr.next(), count, filename);
		}
	}
}
