
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**I shall fjdskoafnskdalvklw
 * @author Hao Eng
 *
 */
//assumption: the file will be saved after every operation
public class TextBuddy {

	/**
	 * @param args
	 * @throws IOException 
	 */
	static int count = 0; //for numbering in the list

	public static void main(String[] args) throws Exception {
		String filename = null;
		Vector<String> list = new Vector<String>();
		Scanner sc = new Scanner(System.in);
		//taking in the filename
		if(args.length > 0){
			filename  = args[0];
		}
		copyOldfileToList(filename, list);
		String command = null;
		System.out.println("Welcome to TextBuddy. " + filename + " is ready for use");
		System.out.print("command: ");
		
		do {
			command = sc.nextLine();
			//checking the action for the command
			if(command.contains("add")) {
				String text = command.replace("add ", "");
				list.add(text);
				writingFile(text, list.size() , filename);
				System.out.println("added to " + filename + ": " + "\"" + text + "\"");
			}
			
			if(command.equalsIgnoreCase("display")){
				printingFile(filename);
			}
			
			if(command.equalsIgnoreCase("clear")){
				clearFile(filename);
				list.clear();
				System.out.println("all content deleted from " + filename);
			}
			
			if(command.contains("delete")){
				String text = command.replace("delete ", "");
				removeSelectedLine(text, list, filename);
			}
			
			if(!command.equalsIgnoreCase("exit")){
				System.out.print("command: ");
			}

			if(command.equalsIgnoreCase("exit")){
				sc.close();
			}
			
		} while(!command.equalsIgnoreCase("exit"));	//not an exit command	
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
		//if the file contains previous content
		if(file.length()!=0){
			//copy file to the vector
			String line = null;
			// FileReader reads text files in the default encoding.
	        FileReader fileReader = 
	            new FileReader(file);

	        // Always wrap FileReader in BufferedReader.
	        BufferedReader bufferedReader = 
	            new BufferedReader(fileReader);
	        while((line = bufferedReader.readLine()) != null) {
	        	//remove the number in front of the sentence
	        	line = line.replaceAll("[0-9]+. ", "");
	        	list.add(line);
	        	count++;
	        }
	        bufferedReader.close();
		}
	}
	
	public static void printingFile(String file){

        // This will reference one line at a time
        String line = null;
        File content = new File(file);
        if(content.length() == 0) System.out.println(file + " is empty");
        
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(file);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
           
            // Always close files.
            bufferedReader.close();			
        }
        catch(IOException ex) {
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
	
	public static void writingFile(String text, int index, String filename) {
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
	
	private static void removeSelectedLine(String replace, Vector<String> list, String filename) throws FileNotFoundException {
		Integer position;
		try {
			position = Integer.parseInt(replace);
		} catch (NumberFormatException e){
			System.out.println("Delete takes a number argument.");
			return;
		}
		//if the number to delete is out of the list
		if(position > list.size() || position <= 0){
			System.out.println("Invalid deletion");
			return;
		}

		System.out.println("deleted from " + filename + ": \"" + list.get(position-1) + "\"");
		
		//rewriting files
		list.removeElementAt(position-1);
		clearFile(filename);
		Iterator<String> itr = list.iterator();
		int count = 0;
		while(itr.hasNext()){
			count++;
			writingFile(itr.next(), count, filename);
		}
	} 
}

