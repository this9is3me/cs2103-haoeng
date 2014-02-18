/*
 * ==============NOTE TO STUDENTS======================================
 * This class is not written in pure Object-Oriented fashion. That is 
 * because we haven't covered OO theory yet. Yes, it is possible to 
 * write non-OO code using an OO language.
 * ====================================================================
 */

import java.util.Scanner;
/**
 * This class is used to store and retrieve the distance between various locations 
 * A route is assumed to be bidirectional. i.e., a route from CityA to CityB is 
 * same as a route from CityB to CityA. Furthermore, there can be no more than 
 * one route between two locations. Deleting a route is not supported at this point.
 * The storage limit for this version is 10 routes.
 * In the case more than multiple routes between the same two locations were entered,
 * we store only the latest one. The command format is given by the example interaction below:

 Welcome to SimpleRouteStore!
 Enter command:addroute Clementi BuonaVista 12
 Route from Clementi to BuonaVista with distance 12km added
 Enter command:getdistance Clementi BuonaVista
 Distance from Clementi to BuonaVista is 12
 Enter command:getdistance clementi buonavista
 Distance from clementi to buonavista is 12
 Enter command:getdistance Clementi JurongWest
 No route exists from Clementi to JurongWest!
 Enter command:addroute Clementi JurongWest 24
 Route from Clementi to JurongWest with distance 24km added
 Enter command:getdistance Clementi JurongWest
 Distance from Clementi to JurongWest is 24
 Enter command:exit

 * @author Dave Jun
 */
public class CityConnect {

	/*
	 * ==============NOTE TO STUDENTS======================================
	 * These messages shown to the user are defined in one place for convenient
	 * editing and proof reading. Such messages are considered part of the UI
	 * and may be subjected to review by UI experts or technical writers. Note
	 * that Some of the strings below include '%1$s' etc to mark the locations
	 * at which java String.format(...) method can insert values.
	 * ====================================================================
	 */
	private static final String MESSAGE_DISTANCE = "Distance from %1$s to %2$s is %3$s";
	private static final String MESSAGE_NO_ROUTE = "No route exists from %1$s to %2$s!";
	private static final String MESSAGE_ADDED = "Route from %1$s to %2$s with distance %3$skm added";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";
	private static final String WELCOME_MESSAGE = "Welcome to SimpleRouteStore!";
	private static final String MESSAGE_NO_SPACE = "No more space to store locations";

	// These are the possible command types
	enum COMMAND_TYPE {
		ADD_ROUTE, GET_DISTANCE, INVALID, EXIT
	};

	// This is used to indicate there is no suitable slot to store route
	private static final int SLOT_UNAVAILABLE = -1;
	
	// This is used to indicate the route was not found in the database
	private static final int NOT_FOUND = -2;

	// These are the correct number of parameters for each command
	private static final int PARAM_SIZE_FOR_ADD_ROUTE = 3, 
							 PARAM_SIZE_FOR_GET_DISTANCE = 2;

	/* These are the locations at which various parameters will appear in a command;
	 * locations at which various components of the route will be
	 * stored in the routes[][] array.
	*/
	private static final int startPos = 0,
							 endPos = 1,
							 distPos = 2;
	
	// This array will be used to store the routes
	private static String[][] route = new String[10][3];
	
	/*
	 * This variable is declared for the whole class (instead of declaring it
	 * inside the readUserCommand() method to facilitate automated testing using
	 * the I/O redirection technique. If not, only the first line of the input
	 * text file will be processed.
	 */
	private static Scanner sc = new Scanner(System.in);

	/*
	 * ==============NOTE TO STUDENTS======================================
	 * Notice how this method solves the whole problem at a very high level. We
	 * can understand the high-level logic of the program by reading this method
	 * alone.
	 * ====================================================================
	 */
	public static void main(String[] args) {
		showToUser(WELCOME_MESSAGE);
		while (true) {
			System.out.print("Enter command:");
			String userCommand = sc.nextLine();
			String feedback = executeCommand(userCommand);
			showToUser(feedback);
		}
	}

	/*
	 * ==============NOTE TO STUDENTS==========================================
	 * If the reader wants a deeper understanding of the solution, he/she can 
	 * go to the next level of abstraction by reading the methods (given below)
	 * that is referenced by the method above.
	 * ====================================================================
	 */

	private static void showToUser(String text) {
		System.out.println(text);
	}

	public static String executeCommand(String userCommand) {
		if (userCommand.trim().equals(""))
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);

		String commandTypeString = getFirstWord(userCommand);

		COMMAND_TYPE commandType = determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD_ROUTE:
			return addRoute(userCommand);
		case GET_DISTANCE:
			return getDistance(userCommand);
		case INVALID:
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		case EXIT:
			System.exit(0);
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
		/*
		 * ==============NOTE TO STUDENTS======================================
		 * If the rest of the program is correct, this error will never be thrown.
		 * That is why we use an Error instead of an Exception.
		 * ====================================================================
		 */
	}

	/*
	 * ==============NOTE TO STUDENTS======================================
	 * After reading the above code, the reader should have a reasonable
	 * understanding of how the program works. If the reader wants to go EVEN
	 * more deep into the solution, he/she can read the methods given below that
	 * solves various sub-problems at lower levels of abstraction.
	 * ====================================================================
	 */

	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 * 
	 * @param commandTypeString
	 *            is the first word of the user command
	 */
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null) throw new Error("command type string cannot be null!");
		else if (commandTypeString.equalsIgnoreCase("addroute")) return COMMAND_TYPE.ADD_ROUTE;
		else if (commandTypeString.equalsIgnoreCase("getdistance")) return COMMAND_TYPE.GET_DISTANCE;
		else if (commandTypeString.equalsIgnoreCase("exit")) return COMMAND_TYPE.EXIT;
		else return COMMAND_TYPE.INVALID;
	}

	/**
	 * This operation is used to find the distance between two locations
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return the distance
	 */
	private static String getDistance(String userCommand) {

		String[] var = splitParameters(removeFirstWord(userCommand));

		if (var.length < distPos) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		String newStartLocation = var[startPos];
		String newEndLocation = var[endPos];

		int position = getPositionOfExistingRoute(newStartLocation, newEndLocation);

		if (position == NOT_FOUND) {
			return String.format(MESSAGE_NO_ROUTE, newStartLocation,
					newEndLocation);
		} else {
			return String.format(MESSAGE_DISTANCE, newStartLocation, newEndLocation,
					route[position][distPos]);
		}

	}

	/**
	 * @return Returns the position of the route represented by 
	 *    newStartLocation and newEndLocation. Returns NOT_FOUND if not found.
	 */
	private static int  getPositionOfExistingRoute(String newStartLocation, String newEndLocation) {
		for (int i = 0; i < route.length; i++) {

			String existing_start_location = route[i][startPos];
			String existing_end_location = route[i][endPos];

			if (existing_start_location == null) {
				return NOT_FOUND; 
			} else if (sameRoute(existing_start_location, existing_end_location,
					newStartLocation, newEndLocation)) { 
				return i;
			}
		}
		return NOT_FOUND;
	}

	/**
	 * This operation adds a route to the storage. If the route already exists,
	 * it will be overwritten.
	 * 
	 * @param userCommand
	 *            (although we receive the full user command, we assume without
	 *            checking the first word to be 'addroute')
	 * @return status of the operation
	 */
	private static String addRoute(String userCommand) {
		
		String[] var = splitParameters(removeFirstWord(userCommand));
		
		if (var.length < PARAM_SIZE_FOR_ADD_ROUTE){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		String newStartLocation = var[startPos];
		String newEndLocation = var[endPos];
		String distance = var[distPos];

		if (!isPositive(distance)){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		int slotPosition = location(newStartLocation, newEndLocation);

		if (slotPosition == SLOT_UNAVAILABLE){
			return MESSAGE_NO_SPACE;
		}

		addRouteAtPosition(newStartLocation, newEndLocation, distance,
				slotPosition);

		return String.format(MESSAGE_ADDED, newStartLocation, newEndLocation,
				distance);
	}

	private static void addRouteAtPosition(String newStartLocation,
			String newEndLocation, String distance, int entryPosition) {
		route[entryPosition][startPos] = newStartLocation;
		route[entryPosition][endPos] = newEndLocation;
		route[entryPosition][distPos] = distance;
	}

	/**
	 * @return Returns a suitable slot for the route represented by 
	 *   newStartLocation and newEndLocation. Returns SLOT_UNAVAILABLE if
	 *   no suitable slot is found.
	 */
	private static int location(String newStartLocation,
			String newEndLocation) {
		
		for (int i = 0; i < route.length; i++) {

			String existingStartLocation = route[i][startPos];
			String existingEndLocation = route[i][endPos];
			
			if (existingStartLocation == null  // empty slot
					|| sameRoute(existingStartLocation, existingEndLocation,
							newStartLocation, newEndLocation)) 
				return i;
			}
		return SLOT_UNAVAILABLE;
	}

	/**
	 * This operation checks if two routes represents the same route.
	 */
	private static boolean sameRoute(String start1,
			String end1, String start2, String end2) {

		if ((start1 == null) || (end1 == null)
				&& (start2 == null) || (end2 == null)){
			throw new Error("Route end points cannot be null");
		}

		return (start1.equalsIgnoreCase(start2) && end1.equalsIgnoreCase(end2))
				|| (start1.equalsIgnoreCase(end2) && end1.equalsIgnoreCase(start2));
	}

	private static boolean isPositive(String s) {
		try {
			int i = Integer.parseInt(s);
			return (i > 0 ? true : false);
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		return userCommand.trim().split("\\s+")[0];
	}

	private static String[] splitParameters(String commandParametersString) {
		String[] parameters = commandParametersString.trim().split("\\s+");
		return parameters;
	}
}
