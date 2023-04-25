import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class WordMemorize {
  static ArrayList<String> list = new ArrayList<>();
  String command = "";
  boolean finished = false;
  static List<Object> args = new ArrayList<>();
  static Map<String, Class<?>[]> commands;

  public WordMemorize() {
    list.clear();
    commands = new HashMap<>();
    commands.put("/help", new Class<?>[]{});
    commands.put("/menu", new Class<?>[]{});
    commands.put("/add", new Class<?>[]{String.class});
    commands.put("/remove", new Class<?>[]{int.class});
    commands.put("/replace", new Class<?>[]{int.class, String.class});
    commands.put("/replaceAll", new Class<?>[]{String.class, String.class});
    commands.put("/index", new Class<?>[]{String.class});
    commands.put("/sort", new Class<?>[]{String.class});
    commands.put("/frequency", new Class<?>[]{});
    commands.put("/print", new Class<?>[]{int.class});
    commands.put("/printAll", new Class<?>[]{String.class});
    commands.put("/getRandom", new Class<?>[]{});
    commands.put("/count", new Class<?>[]{String.class});
    commands.put("/size", new Class<?>[]{});
    commands.put("/equals", new Class<?>[]{int.class, int.class});
    commands.put("/readFile", new Class<?>[]{String.class});
    commands.put("/writeFile", new Class<?>[]{String.class});
    commands.put("/clear", new Class<?>[]{});
    commands.put("/compare", new Class<?>[]{int.class, int.class});
    commands.put("/mirror", new Class<?>[]{});
    commands.put("/unique", new Class<?>[]{});
    commands.put("/concat", new Class<?>[]{int.class, int.class});
    commands.put("/swapCase", new Class<?>[]{int.class});
    commands.put("/upper", new Class<?>[]{int.class});
    commands.put("/lower", new Class<?>[]{int.class});
    commands.put("/reverse", new Class<?>[]{int.class});
    commands.put("/length", new Class<?>[]{int.class});
    commands.put("/join", new Class<?>[]{String.class});
    commands.put("/regex", new Class<?>[]{String.class});
  }

  void Run() {
    Scanner scanner = new Scanner(System.in);
    while (!finished) {
      command = "";
      args.clear();
      try {
        System.out.println("Perform action:");
        String[] data = scanner.nextLine().split(" ");
        command = data[0];

        if (!commands.containsKey(command)) {
          ErrorWithContinue("No such command");
        }
        if (commands.get(command).length != data.length - 1) {
          ErrorWithContinue("Incorrect amount of arguments");
        }

        for (int i = 1; i < data.length; i++) {
          try {
            if (commands.get(command)[i - 1].equals(int.class))
              args.add(Integer.parseInt(data[i]));
            else {
              args.add(data[i]);
            }
          } catch (Exception e) {
            ErrorWithContinue("Some arguments can't be parsed");
          }
        }
        this.getClass().getDeclaredMethod(command.substring(1), commands.get(command)).invoke(this, args.toArray());
      } catch (ContinueException ignored) {
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  void help() {
    System.out.println(
            "===================================================================================================================\n" +
                    "Usage: COMMAND [<TYPE> PARAMETERS]\n" +
                    "===================================================================================================================\n" +
                    "General commands:\n" +
                    "===================================================================================================================\n" +
                    "/help - Display this help message\n" +
                    "/menu - Return to the menu\n" +
                    "\n" +
                    "/add [<T> ELEMENT] - Add the specified element to the list\n" +
                    "/remove [<int> INDEX] - Remove the element at the specified index from the list\n" +
                    "/replace [<int> INDEX] [<T> ELEMENT] - Replace the element at specified index with the new one\n" +
                    "/replaceAll [<T> OLD] [<T> NEW] - Replace all occurrences of specified element with the new " +
                    "one\n" +
                    "\n" +
                    "/index [<T> ELEMENT] - Get the index of the first specified element in the list\n" +
                    "/sort [ascending/descending] - Sort the list in ascending or descending order\n" +
                    "/frequency - The frequency count of each element in the list\n" +
                    "/print [<int> INDEX] - Print the element at the specified index in the list\n" +
                    "/printAll [asList/lineByLine/oneLine] - Print all elements in the list in specified format\n" +
                    "/getRandom - Get a random element from the list\n" +
                    "/count [<T> ELEMENT] - Count the number of occurrences of the specified element in the list\n" +
                    "/size - Get the number of elements in the list\n" +
                    "/equals [<int> INDEX1] [<int> INDEX2] - Check if two elements are equal\n" +
                    "/clear - Remove all elements from the list\n" +
                    "/compare [<int> INDEX1] [<int> INDEX2] Compare elements at the specified indices in the list\n" +
                    "/mirror - Mirror elements' positions in list\n" +
                    "/unique - Unique elements in the list\n" +
                    "/readFile [<string> FILENAME] - Import data from the specified file and add it to the list\n" +
                    "/writeFile [<string> FILENAME] - Export the list data to the specified file");
    System.out.println(
            "===================================================================================================================\n" +
                    "Word-specific commands:\n" +
                    "===================================================================================================================\n" +
                    "/concat [INDEX1] [INDEX2] Concatenate two specified strings\n" +
                    "/swapCase [INDEX] Output swapped case version of the specified string\n" +
                    "/upper [INDEX] Output uppercase version of the specified string\n" +
                    "/lower [INDEX] Output lowercase version of the specified string\n" +
                    "/reverse [INDEX] Output reversed version of the specified string\n" +
                    "/length [INDEX] Get the length of the specified string\n" +
                    "/join [<string> DELIMITER] Join all the strings with the specified delimiter\n" +
                    "/regex [<string> PATTERN] Search for all elements that match the specified regular expression " +
                    "pattern\n" +
                    "===================================================================================================================");
  }

  void menu() {
    this.finished = true;
  }

  void add(String element) {
    list.add(element);
    System.out.println("Element " + element + " added");
  }

  void remove(int index) {
    if (CheckIndexOutOfBounds(index)) return;
    list.remove(index);
    System.out.println("Element on " + index + " position removed");
  }

  void replace(int index, String element) {
    if (CheckIndexOutOfBounds(index)) return;
    list.set(index, element);
    System.out.println("Element on " + index + " position replaced with " + element);
  }

  void replaceAll(String from, String to) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals(from)) {
        list.set(i, to);
      }
    }
    System.out.println("Each " + from + " element replaced with " + to);
  }

  void index(String value) {
    int i = list.indexOf(value);
    if (i == -1) {
      System.out.println("There is no such element memorized");
      return;
    }
    System.out.println("First occurrence of " + value + " is on " + i + " position");
  }

  void sort(String way) {
    if (!way.equals("ascending") && !way.equals("descending")) {
      Error("Incorrect argument, possible arguments: ascending, descending");
      return;
    }
    for (int i = 0; i < list.size(); i++) {
      for (int j = i; j < list.size(); j++) {
        if (list.get(i).compareTo(list.get(j)) > 0 && way.equals("ascending") || list.get(i).compareTo(list.get(j)) < 0 && way.equals(
                "descending")) {
          String temp = list.get(i);
          list.set(i, list.get(j));
          list.set(j, temp);
        }
      }
    }
    System.out.printf("Memory sorted %s\n", way);
  }

  void frequency() {
    if (list.size() == 0) {
      System.out.println("There is no elements memorized");
      return;
    }
    Map<String, Long> counts = new HashMap<>();
    for (String i : list) {
      if (counts.get(i) == null) {
        counts.put(i, 1L);
      } else {
        counts.put(i, counts.get(i) + 1);
      }
    }

    System.out.println("Frequency:");
    for (Map.Entry<String, Long> entry : counts.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue());
    }
  }

  void print(int index) {
    if (CheckIndexOutOfBounds(index)) return;
    System.out.println("Element on " + index + " position is " + list.get(index));
  }

  void getRandom() {
    Random random = new Random();
    System.out.println("Random element: " + list.get(random.nextInt(list.size())));
  }

  void printAll(String type) {
    switch (type) {
      case "asList":
        System.out.println("List of elements:\n" +
                Arrays.toString(list.toArray()));
        break;
      case "lineByLine":
        System.out.println("List of elements:\n");
        for (String i : list) {
          System.out.println(i);
        }
        break;
      case "oneLine":
        System.out.println("List of elements:");
        for (int i = 0; i < list.size() - 1; i++) {
          System.out.print(list.get(i) + " ");
        }
        if (list.size() > 0)
          System.out.print(list.get(list.size() - 1));
        System.out.println();
        break;
      default:
        Error("Incorrect argument, possible arguments: asList, lineByLine, oneLine");
        break;
    }
  }

  void count(String value) {
    int amount = 0;
    for (String i : list) {
      if (i.equals(value)) {
        amount++;
      }
    }
    System.out.println("Amount of " + value + ": " + amount);
  }

  void size() {
    System.out.println("Amount of elements: " + list.size());
  }

  void equals(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    boolean res = list.get(i).equals(list.get(j));
    System.out.printf("%d and %d elements are%s equal: %s\n",
            i, j, res ? "" : " not", list.get(i) + (res ? " = " : " != ") + list.get(j));
  }

  void readFile(String path) {
    int before = list.size();
    FileReaderWords readerThread = new FileReaderWords();
    ArrayList<String> list2 = readerThread.read(path);
    for (String i : list2) {
      list.add(i);
    }
    System.out.println("Data imported: " + (list.size() - before));
  }

  void writeFile(String path) {
    FileWriterWords writer = new FileWriterWords();
    writer.write(path, list);
    System.out.println("Data exported: " + list.size());
  }

  void clear() {
    list.clear();
    System.out.println("Data cleared");
  }

  void compare(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    if (list.get(i).compareTo(list.get(j)) > 0) {
      System.out.println("Result: " + list.get(i) + " > " + list.get(j));
    } else if (list.get(i).compareTo(list.get(j)) < 0) {
      System.out.println("Result: " + list.get(i) + " < " + list.get(j));
    } else {
      System.out.println("Result: " + list.get(i) + " = " + list.get(j));
    }
  }

  void mirror() {
    ArrayList<String> list2 = new ArrayList<>();
    for (int i = list.size() - 1; i >= 0; i--) {
      list2.add(list.get(i));
    }
    list = list2;
    System.out.println("Data reversed");
  }

  void unique() {
    Map<String, Long> counts = new HashMap<>();
    for (String i : list) {
      if (counts.get(i) == null) {
        counts.put(i, 1L);
      } else {
        counts.put(i, counts.get(i) + 1);
      }
    }
    ArrayList<String> list2 = new ArrayList<>();
    for (Map.Entry<String, Long> entry : counts.entrySet()) {
      list2.add(entry.getKey());
    }
    System.out.println("Unique values: " + Arrays.toString(list2.toArray()));
  }

  void ErrorWithContinue(String info) throws ContinueException {
    System.out.printf("Error while performing command \"%s\"!\nDescription: %s!\n", command, info);
    throw new ContinueException("");
  }

  void Error(String info) {
    System.out.printf("Error while performing command \"%s\"!\nDescription: %s!\n", command, info);
  }

  boolean CheckIndexOutOfBounds(int i) {
    if (i < 0 || i >= list.size()) {
      Error("Index out of bounds");
      return true;
    }
    return false;
  }

  void concat(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    System.out.println("Concatenated string: " + list.get(i) + list.get(j));
  }

  void swapCase(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("\"%s\" string with swapped case: ", list.get(i));
    for (char c : (list.get(i)).toCharArray()) {
      if (Character.isUpperCase(c)) {
        System.out.print(Character.toLowerCase(c));
      } else if (Character.isLowerCase(c)) {
        System.out.print(Character.toUpperCase(c));
      } else {
        System.out.print(c);
      }
    }
    System.out.println();
  }

  void upper(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("Uppercase \"%s\" string: %s\n", list.get(i), (list.get(i)).toUpperCase());
  }

  void lower(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("Lowercase \"%s\" string: %s\n", list.get(i), (list.get(i)).toLowerCase());
  }

  void reverse(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("Reversed \"%s\" string: %s\n", list.get(i), new StringBuilder(list.get(i)).reverse());
  }

  void length(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("Length of \"%s\" string: %d\n", list.get(i), (list.get(i)).length());
  }

  void join(String delimiter) {
    System.out.printf("Joined string: %s\n", String.join(delimiter, list));
  }

  void regex(String regex) {
    List<String> matchingElements = new ArrayList<>();
    Pattern pattern;
    try {
      pattern = Pattern.compile(regex);
    } catch (PatternSyntaxException e) {
      Error("Incorrect regex pattern provided");
      return;
    }

    for (String element : list) {
      if (pattern.matcher(element).matches()) {
        matchingElements.add(element);
      }
    }
    if (matchingElements.size() == 0) {
      System.out.println("There is no strings that match provided regex");
    }
    System.out.println("Strings that match provided regex:");
    System.out.println(Arrays.toString(matchingElements.toArray()));
  }
}
