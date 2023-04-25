import java.util.*;

public class NumberMemorize {
  static ArrayList<Integer> list = new ArrayList<>();
  String command = "";
  boolean finished = false;
  static List<Object> args = new ArrayList<>();
  static Map<String, Class<?>[]> commands;

  public NumberMemorize() {
    list.clear();
    commands = new HashMap<>();
    commands.put("/help", new Class<?>[]{});
    commands.put("/menu", new Class<?>[]{});
    commands.put("/add", new Class<?>[]{int.class});
    commands.put("/remove", new Class<?>[]{int.class});
    commands.put("/replace", new Class<?>[]{int.class, int.class});
    commands.put("/replaceAll", new Class<?>[]{int.class, int.class});
    commands.put("/index", new Class<?>[]{int.class});
    commands.put("/sort", new Class<?>[]{String.class});
    commands.put("/frequency", new Class<?>[]{});
    commands.put("/print", new Class<?>[]{int.class});
    commands.put("/printAll", new Class<?>[]{String.class});
    commands.put("/getRandom", new Class<?>[]{});
    commands.put("/count", new Class<?>[]{int.class});
    commands.put("/size", new Class<?>[]{});
    commands.put("/equals", new Class<?>[]{int.class, int.class});
    commands.put("/readFile", new Class<?>[]{String.class});
    commands.put("/writeFile", new Class<?>[]{String.class});
    commands.put("/clear", new Class<?>[]{});
    commands.put("/compare", new Class<?>[]{int.class, int.class});
    commands.put("/mirror", new Class<?>[]{});
    commands.put("/unique", new Class<?>[]{});
    commands.put("/sum", new Class<?>[]{int.class, int.class});
    commands.put("/subtract", new Class<?>[]{int.class, int.class});
    commands.put("/multiply", new Class<?>[]{int.class, int.class});
    commands.put("/divide", new Class<?>[]{int.class, int.class});
    commands.put("/pow", new Class<?>[]{int.class, int.class});
    commands.put("/factorial", new Class<?>[]{int.class});
    commands.put("/sumAll", new Class<?>[]{});
    commands.put("/average", new Class<?>[]{});
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
                    "Number-specific commands:\n" +
                    "===================================================================================================================\n" +
                    "/sum [<int> INDEX1] [<int> INDEX2] - Calculate the sum of the two specified elements\n" +
                    "/subtract [<int> INDEX1] [<int> INDEX2] - Calculate the difference between the two specified " +
                    "elements\n" +
                    "/multiply [<int> INDEX1] [<int> INDEX2] - Calculate the product of the two specified elements\n" +
                    "/divide [<int> INDEX1] [<int> INDEX2] - Calculate the division of the two specified elements\n" +
                    "/pow [<int> INDEX1] [<int> INDEX2] - Calculate the power of the specified element to the " +
                    "specified exponent element\n" +
                    "/factorial [<int> INDEX] - Calculate the factorial of the specified element\n" +
                    "/sumAll - Calculate the sum of all elements\n" +
                    "/average - Calculate the average of all elements\n" +
                    "===================================================================================================================");
  }

  void menu() {
    this.finished = true;
  }

  void add(int element) {
    list.add(element);
    System.out.println("Element " + element + " added");
  }

  void remove(int index) {
    if (CheckIndexOutOfBounds(index)) return;
    list.remove(index);
    System.out.println("Element on " + index + " position removed");
  }

  void replace(int index, int element) {
    if (CheckIndexOutOfBounds(index)) return;
    list.set(index, element);
    System.out.println("Element on " + index + " position replaced with " + element);
  }

  void replaceAll(int from, int to) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals(from)) {
        list.set(i, to);
      }
    }
    System.out.println("Each " + from + " element replaced with " + to);
  }

  void index(int value) {
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
        if (list.get(i) > list.get(j) && way.equals("ascending") || list.get(i) < list.get(j) && way.equals(
                "descending")) {
          int temp = list.get(i);
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
    Map<Integer, Long> counts = new HashMap<>();
    for (int i : list) {
      if (counts.get(i) == null) {
        counts.put(i, 1L);
      } else {
        counts.put(i, counts.get(i) + 1);
      }
    }

    System.out.println("Frequency:");
    for (Map.Entry<Integer, Long> entry : counts.entrySet()) {
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
        for (int i : list) {
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

  void count(int value) {
    int amount = 0;
    for (int i : list) {
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
    FileReaderInteger readerThread = new FileReaderInteger();
    ArrayList<Integer> list2 = readerThread.read(path);
    for (int i : list2) {
      list.add(i);
    }
    System.out.println("Data imported: " + (list.size() - before));
  }

  void writeFile(String path) {
    FileWriterInteger writer = new FileWriterInteger();
    writer.write(path, list);
    System.out.println("Data exported: " + list.size());
  }

  void clear() {
    list.clear();
    System.out.println("Data cleared");
  }

  void compare(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    if (list.get(i) > list.get(j)) {
      System.out.println("Result: " + list.get(i) + " > " + list.get(j));
    } else if (list.get(i) < list.get(j)) {
      System.out.println("Result: " + list.get(i) + " < " + list.get(j));
    } else {
      System.out.println("Result: " + list.get(i) + " = " + list.get(j));
    }
  }

  void mirror() {
    ArrayList<Integer> list2 = new ArrayList<>();
    for (int i = list.size() - 1; i >= 0; i--) {
      list2.add(list.get(i));
    }
    list = list2;
    System.out.println("Data reversed");
  }

  void unique() {
    Map<Integer, Long> counts = new HashMap<>();
    for (int i : list) {
      if (counts.get(i) == null) {
        counts.put(i, 1L);
      } else {
        counts.put(i, counts.get(i) + 1);
      }
    }
    ArrayList<Integer> list2 = new ArrayList<>();
    for (Map.Entry<Integer, Long> entry : counts.entrySet()) {
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

  void sum(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = list.get(i), b = list.get(j);
    int res = a + b;
    System.out.printf("Calculation performed: %d + %d = %d\n", a, b, res);
  }

  void subtract(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = list.get(i), b = list.get(j);
    int res = a - b;
    System.out.printf("Calculation performed: %d - %d = %d\n", a, b, res);
  }

  void multiply(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = list.get(i), b = list.get(j);
    int res = a * b;
    System.out.printf("Calculation performed: %d * %d = %d\n", a, b, res);
  }

  void divide(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = list.get(i), b = list.get(j);
    if (b == 0) {
      Error("Division by zero");
      return;
    }
    float res = (float) a / (float) b;
    System.out.printf("Calculation performed: %d / %d = %f\n", a, b, res);
  }

  void pow(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = list.get(i), b = list.get(j);
    long res = (long) Math.pow(a, b);
    System.out.printf("Calculation performed: %d ^ %d = %d\n", a, b, res);
  }

  void factorial(int index) {
    if (CheckIndexOutOfBounds(index)) return;
    long res = 1;
    for (int i = 2; i <= list.get(index); i++) {
      res = res * i;
    }
    System.out.printf("Calculation performed: %d! = %d\n", list.get(index), res);
  }

  void sumAll() {
    int sum = 0;
    for (int i : list) {
      sum += i;
    }
    System.out.println("Sum of all elements: " + sum);
  }

  void average() {
    int sum = 0;
    for (int i : list) {
      sum += i;
    }
    System.out.println("Average of all elements: " + sum / list.size());
  }
}
