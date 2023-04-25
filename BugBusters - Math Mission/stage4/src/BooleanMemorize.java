import java.util.*;

public class BooleanMemorize {
  static ArrayList<Boolean> list = new ArrayList<>();
  String command = "";
  boolean finished = false;
  static List<Object> args = new ArrayList<>();
  static Map<String, Class<?>[]> commands;

  public BooleanMemorize() {
    list.clear();
    commands = new HashMap<>();
    commands.put("/help", new Class<?>[]{});
    commands.put("/menu", new Class<?>[]{});
    commands.put("/add", new Class<?>[]{Boolean.class});
    commands.put("/remove", new Class<?>[]{int.class});
    commands.put("/replace", new Class<?>[]{int.class, Boolean.class});
    commands.put("/replaceAll", new Class<?>[]{Boolean.class, Boolean.class});
    commands.put("/index", new Class<?>[]{Boolean.class});
    commands.put("/sort", new Class<?>[]{String.class});
    commands.put("/frequency", new Class<?>[]{});
    commands.put("/print", new Class<?>[]{int.class});
    commands.put("/printAll", new Class<?>[]{String.class});
    commands.put("/getRandom", new Class<?>[]{});
    commands.put("/count", new Class<?>[]{Boolean.class});
    commands.put("/size", new Class<?>[]{});
    commands.put("/equals", new Class<?>[]{int.class, int.class});
    commands.put("/readFile", new Class<?>[]{String.class});
    commands.put("/writeFile", new Class<?>[]{String.class});
    commands.put("/clear", new Class<?>[]{});
    commands.put("/compare", new Class<?>[]{int.class, int.class});
    commands.put("/mirror", new Class<?>[]{});
    commands.put("/unique", new Class<?>[]{});
    commands.put("/flip", new Class<?>[]{int.class});
    commands.put("/negateAll", new Class<?>[]{});
    commands.put("/and", new Class<?>[]{int.class, int.class});
    commands.put("/or", new Class<?>[]{int.class, int.class});
    commands.put("/logShift", new Class<?>[]{int.class});
    commands.put("/convertTo", new Class<?>[]{String.class});
    commands.put("/morse", new Class<?>[]{});
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
            else if (commands.get(command)[i - 1].equals(Boolean.class)) {
              if (!data[i].equals("true") && !data[i].equals("false"))
                throw new Exception();
              args.add(data[i].equals("true"));
            } else {
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
                    "Boolean-specific commands:\n" +
                    "===================================================================================================================\n" +
                    "/flip [<int> INDEX] - Flip the specified boolean\n" +
                    "/negateAll - Negate all the booleans in memory\n" +
                    "/and [<int> INDEX1] [<int> INDEX2] - Calculate the bitwise AND of the two specified elements\n" +
                    "/or [<int> INDEX1] [<int> INDEX2] - Calculate the bitwise OR of the two specified elements\n" +
                    "/logShift [<int> NUM] - Perform a logical shift of elements in memory by the specified amount\n" +
                    "/convertTo [string/number] - Convert the boolean(bit) sequence in memory to the specified type\n" +
                    "/morse - Convert the boolean(bit) sequence to Morse code\n" +
                    "===================================================================================================================");
  }

  void menu() {
    this.finished = true;
  }

  void add(Boolean element) {
    list.add(element);
    System.out.println("Element " + element + " added");
  }

  void remove(int index) {
    if (CheckIndexOutOfBounds(index)) return;
    list.remove(index);
    System.out.println("Element on " + index + " position removed");
  }

  void replace(int index, Boolean element) {
    if (CheckIndexOutOfBounds(index)) return;
    list.set(index, element);
    System.out.println("Element on " + index + " position replaced with " + element);
  }

  void replaceAll(Boolean from, Boolean to) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals(from)) {
        list.set(i, to);
      }
    }
    System.out.println("Each " + from + " element replaced with " + to);
  }

  void index(Boolean value) {
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
        if (list.get(i) && !list.get(j) && way.equals("ascending") || !list.get(i) && list.get(j) && way.equals(
                "descending")) {
          Boolean temp = list.get(i);
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
    Map<Boolean, Long> counts = new HashMap<>();
    for (Boolean b : list) {
      if (counts.get(b) == null) {
        counts.put(b, 1L);
      } else {
        counts.put(b, counts.get(b) + 1);
      }
    }

    System.out.println("Frequency:");
    for (Map.Entry<Boolean, Long> entry : counts.entrySet()) {
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
        for (Boolean i : list) {
          System.out.println(i);
        }
        break;
      case "oneLine":
        System.out.println("List of elements:");
        for (int i=0; i<list.size()-1;i++) {
          System.out.print(list.get(i) + " ");
        }
        if(list.size()>0)
          System.out.print(list.get(list.size()-1));
        System.out.println();
        break;
      default:
        Error("Incorrect argument, possible arguments: asList, lineByLine, oneLine");
        break;
    }
  }

  void count(Boolean value) {
    int amount = 0;
    for (Boolean i : list) {
      if (i == value) {
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
    FileReaderBoolean readerThread = new FileReaderBoolean();
    ArrayList<Boolean> list2 = readerThread.read(path);
    for (Boolean i : list2) {
      list.add(i);
    }
    System.out.println("Data imported: " + (list.size() - before));
  }

  void writeFile(String path) {
    FileWriterBoolean writer = new FileWriterBoolean();
    writer.write(path, list);
    System.out.println("Data exported: " + list.size());
  }

  void clear() {
    list.clear();
    System.out.println("Data cleared");
  }

  void compare(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    if (list.get(i) && !list.get(j)) {
      System.out.println("Result: " + list.get(i) + " > " + list.get(j));
    } else if (!list.get(i) && list.get(j)) {
      System.out.println("Result: " + list.get(i) + " < " + list.get(j));
    } else {
      System.out.println("Result: " + list.get(i) + " = " + list.get(j));
    }
  }

  void mirror() {
    ArrayList<Boolean> list2 = new ArrayList<>();
    for (int i = list.size() - 1; i >= 0; i--) {
      list2.add(list.get(i));
    }
    list = list2;
    System.out.println("Data reversed");
  }

  void unique() {
    Map<Boolean, Long> counts = new HashMap<>();
    for (Boolean i : list) {
      if (counts.get(i) == null) {
        counts.put(i, 1L);
      } else {
        counts.put(i, counts.get(i) + 1);
      }
    }
    ArrayList<Boolean> list2 = new ArrayList<>();
    for (Map.Entry<Boolean, Long> entry : counts.entrySet()) {
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

  void flip(int index) {
    if (CheckIndexOutOfBounds(index)) return;
    list.set(index, !list.get(index));
    System.out.println("Element on " + index + " position flipped");
  }

  void negateAll() {
    list.replaceAll(e -> !e);
    System.out.println("All elements negated");
  }

  void and(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    boolean a = list.get(i), b = list.get(j);
    boolean res = a && b;
    System.out.printf("Operation performed: (%b && %b) is %b\n", a, b, res);
  }

  void or(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    boolean a = list.get(i), b = list.get(j);
    boolean res = a || b;
    System.out.printf("Operation performed: (%b || %b) is %b\n", a, b, res);
  }

  void logShift(int n) {
    int outputValue = n;
    int size = list.size();
    if (size == 0) {
      return;
    }
    n %= size;
    if (n < 0) {
      n += size;
    }
    for (int i = 0; i < n; i++) {
      Boolean last = list.get(size - 1);
      for (int j = size - 1; j > 0; j--) {
        list.set(j, list.get(j - 1));
      }
      list.set(0, last);
    }
    System.out.println("Elements shifted by " + outputValue);
  }

  void convertTo(String type) {
    if (list.size() == 0) {
      Error("No data memorized yet");
      return;
    }
    StringBuilder binary = new StringBuilder();
    for (boolean b : list) {
      if (b) {
        binary.append("1");
      } else {
        binary.append("0");
      }
    }
    switch (type.toLowerCase()) {
      case "number":
        System.out.println("Converted: " + Long.parseLong(binary.toString(), 2));
        break;
      case "string":
        StringBuilder sb = new StringBuilder();
        if (binary.length() % 8 != 0) {
          System.out.println("Amount of elements is not divisible by 8, so the last " + binary.length() % 8 + " of " +
                  "them will be ignored");
        }
        for (int i = 0; i < binary.length(); i += 8) {
          String segment = binary.substring(i, Math.min(i + 8, binary.length()));
          int asciiValue = Integer.parseInt(segment, 2);
          sb.append((char) asciiValue);
        }
        String asciiSequence = sb.toString();
        System.out.println("Converted: " + asciiSequence);
        break;
      default:
        Error("Incorrect argument, possible arguments: string, number");
        break;
    }
  }

  void morse() {
    if (list.size() == 0) {
      Error("No data memorized yet");
      return;
    }
    StringBuilder morseCode = new StringBuilder("Morse code: ");
    for (boolean b : list) {
      if (b) {
        morseCode.append(".");
      } else {
        morseCode.append("_");
      }
    }
    System.out.println(morseCode);
  }
}
