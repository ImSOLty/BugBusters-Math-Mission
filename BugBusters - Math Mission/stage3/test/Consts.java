import java.util.Arrays;
import java.util.List;

public class Consts {
  public static final float threshold = 0.95f;
  public static final String MODIFIED = "There is no need to change the initial behaviour of the program:\n";
  public static final String INCORRECT_BEHAVIOUR = "Behaviour is incorrect or not matching the initial one:\n";

  public static String MORE_LIKELY_OOM(String command, List<String> sequence, boolean reveal) {
    return "Execution of \"" + command + "\" command finished with incorrect results for a large amount of data." +
            " More likely your program caught an OutOfMemoryError" + (reveal ? " while executing this sequence of " +
            "input: \n" + Arrays.toString(sequence.toArray()) : ".");
  }

  public static final String MENU_OUTPUT = """
          Welcome to Data Memory!
          Possible actions:
          1. Memorize booleans
          2. Memorize numbers
          3. Memorize words
          0. Quit
          """;

  public static final String HELP_GENERAL = """
          ===================================================================================================================
          Usage: COMMAND [<TYPE> PARAMETERS]
          ===================================================================================================================
          General commands:
          ===================================================================================================================
          /help - Display this help message
          /menu - Return to the menu

          /add [<T> ELEMENT] - Add the specified element to the list
          /remove [<int> INDEX] - Remove the element at the specified index from the list
          /replace [<int> INDEX] [<T> ELEMENT] - Replace the element at specified index with the new one
          /replaceAll [<T> OLD] [<T> NEW] - Replace all occurrences of specified element with the new one

          /index [<T> ELEMENT] - Get the index of the first specified element in the list
          /sort [ascending/descending] - Sort the list in ascending or descending order
          /frequency - The frequency count of each element in the list
          /print [<int> INDEX] - Print the element at the specified index in the list
          /printAll [asList/lineByLine/oneLine] - Print all elements in the list in specified format
          /getRandom - Get a random element from the list
          /count [<T> ELEMENT] - Count the number of occurrences of the specified element in the list
          /size - Get the number of elements in the list
          /equals [<int> INDEX1] [<int> INDEX2] - Check if two elements are equal
          /clear - Remove all elements from the list
          /compare [<int> INDEX1] [<int> INDEX2] Compare elements at the specified indices in the list
          /mirror - Mirror elements' positions in list
          /unique - Unique elements in the list
          /readFile [<string> FILENAME] - Import data from the specified file and add it to the list
          /writeFile [<string> FILENAME] - Export the list data to the specified file""";

  public static final String HELP_NUMBERS = """
          ===================================================================================================================
          Number-specific commands:
          ===================================================================================================================
          /sum [<int> INDEX1] [<int> INDEX2] - Calculate the sum of the two specified elements
          /subtract [<int> INDEX1] [<int> INDEX2] - Calculate the difference between the two specified elements
          /multiply [<int> INDEX1] [<int> INDEX2] - Calculate the product of the two specified elements
          /divide [<int> INDEX1] [<int> INDEX2] - Calculate the division of the two specified elements
          /pow [<int> INDEX1] [<int> INDEX2] - Calculate the power of the specified element to the specified exponent element
          /factorial [<int> INDEX] - Calculate the factorial of the specified element
          /sumAll - Calculate the sum of all elements
          /average - Calculate the average of all elements
          ===================================================================================================================
          """;
  public static final String HELP_BOOLEANS = """
          ===================================================================================================================
          Boolean-specific commands:
          ===================================================================================================================
          /flip [<int> INDEX] - Flip the specified boolean
          /negateAll - Negate all the booleans in memory
          /and [<int> INDEX1] [<int> INDEX2] - Calculate the bitwise AND of the two specified elements
          /or [<int> INDEX1] [<int> INDEX2] - Calculate the bitwise OR of the two specified elements
          /logShift [<int> NUM] - Perform a logical shift of elements in memory by the specified amount
          /convertTo [string/number] - Convert the boolean(bit) sequence in memory to the specified type
          /morse - Convert the boolean(bit) sequence to Morse code
          ===================================================================================================================
          """;
  public static final String HELP_WORDS = """
          ===================================================================================================================
          Word-specific commands:
          ===================================================================================================================
          /concat [<int> INDEX1] [<int> INDEX2] Concatenate two specified strings
          /swapCase [<int> INDEX] Output swapped case version of the specified string
          /upper [<int> INDEX] Output uppercase version of the specified string
          /lower [<int> INDEX] Output lowercase version of the specified string
          /reverse [<int> INDEX] Output reversed version of the specified string
          /length [<int> INDEX] Get the length of the specified string
          /join [<string> DELIMITER] Join all the strings with the specified delimiter
          /regex [<string> PATTERN] Search for all elements that match the specified regular expression pattern
          ===================================================================================================================
          """;
}
