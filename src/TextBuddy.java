import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author Hao Eng
 * @version CE2
 * @see Assumption: the file will be saved after every operation.
 */

public class TextBuddy {
	// for numbering in the list.
	private static int count = 0;
	private static String WELCOME_MSG = "Welcome to TextBuddy. %s is ready for use.\n",
			COMMAND = "command: ",
			ADD = "add",
			DISPLAY = "display",
			CLEAR = "clear", DELETE = "delete", EXIT = "exit", SORT = "sort";
	private static Vector<String> list = new Vector<String>();
	private static Scanner sc = new Scanner(System.in);

	/**
	 * @param filename
	 * @param command
	 * @throws FileNotFoundException
	 */
	static void checkWordAssignAction(String filename, String command)
			throws FileNotFoundException {

		if (command.startsWith(ADD)) {
			String text = replacingAdd(command);
			list.add(text);
			writeTextToFile(text, list.size(), filename);
			printSuccessfulAddMsg(filename, text);
		}
		if (command.startsWith(SORT)) {
			sortLines();
		}
		if (command.startsWith(DISPLAY)) {
			printContentsOfFile(filename);
		}

		if (command.startsWith(CLEAR)) {
			clearFile(filename);
			list.clear();
			printSuccessfulDeleteMsg(filename);
		}

		if (command.startsWith(DELETE)) {
			String lineNum = replacingDelete(command);
			int position = convertStringToInteger(lineNum);
			removeSelectedLine(position, list, filename);
		}

		if (!command.startsWith(EXIT)) {
			printCommandLine();
		}

		if (command.startsWith(EXIT)) {
			closeFile();
		}
	}

	/**
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public static void clearFile(String filename) throws FileNotFoundException {
		try {
			File file = new File(filename);
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("");
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void closeFile() {
		sc.close();
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

		} while (!command.startsWith(EXIT)); // not an exit command
	}

	/**
	 * 
	 * @param lineNum
	 * @return
	 * @throws NumberFormatException
	 */
	private static int convertStringToInteger(String lineNum)
			throws NumberFormatException {
		int position = -1;
		try {
			position = Integer.parseInt(lineNum);

		} catch (NumberFormatException e) {
			System.out.println("'Delete' takes a number argument.");
		}
		return position;
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
			// copy file to the List
			String line = null;
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(file);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				// remove the number in front of the sentence
				line = line.replaceAll("[0-9]+. ", "");
				list.add(line);
				setCount(getCount() + 1);
			}
			bufferedReader.close();
		}
	}

	/**
	 * @return the count
	 */
	public static int getCount() {
		return count;
	}

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

	private static void printCommandLine() {
		System.out.print("command: ");
	}

	/**
	 * 
	 * @param file
	 */
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

	/**
	 * @param filename
	 */
	private static void printMsg(String filename) {
		System.out.printf(WELCOME_MSG, filename);
		System.out.print(COMMAND);
	}

	/**
	 * @param filename
	 * @param text
	 */
	private static void printSuccessfulAddMsg(String filename, String text) {
		System.out.println("added to " + filename + ": " + "\"" + text + "\"");
	}

	/**
	 * @param filename
	 */
	private static void printSuccessfulDeleteMsg(String filename) {
		System.out.println("all content deleted from " + filename);
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
	 * 
	 * @param lineNum
	 * @param list
	 * @param filename
	 * @throws FileNotFoundException
	 */
	private static void removeSelectedLine(int position, Vector<String> list,
			String filename) throws FileNotFoundException {

		// if the number to delete is out of the list
		if ((position > list.size()) || (position <= 0)) {
			System.out.println("Invalid deletion");
			return;
		}

		System.out.println("deleted from " + filename + ": \""
				+ list.get(position - 1) + "\"");

		rewriteFile(position, list, filename);
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
	 * @param command
	 * @return
	 */
	private static String replacingDelete(String command) {
		String text = command.replace("delete ", "");
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
	 * @param position
	 * @param list
	 * @param filename
	 * @throws FileNotFoundException
	 */
	private static void rewriteFile(int position, Vector<String> list,
			String filename) throws FileNotFoundException {

		list.removeElementAt(position - 1);
		clearFile(filename);
		Iterator<String> itr = list.iterator();
		int count = 0;
		while (itr.hasNext()) {
			count++;
			writeTextToFile(itr.next(), count, filename);
		}
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public static void setCount(int count) {
		TextBuddy.count = count;
	}

	/**
	 * sorting in alphabetical order and display immediately
	 */
	private static void sortLines() {
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + 1 + ". " + list.get(i));
		}
	}

	/**
	 * 
	 * @param text
	 * @param index
	 * @param filename
	 */
	public static void writeTextToFile(String text, int index, String filename) {
		try {
			File file = new File(filename);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(index + ". " + text + "\n");
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
