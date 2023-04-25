package mathmission;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Memorize<T> {
  LinkedList<T> list = new LinkedList<>();
  String command = "";
  boolean finished = false;
  List<Object> args = new ArrayList<>();
  private final Class<T> type;
  Map<String, Class<?>[]> commands;

  public Memorize(Class<T> type) {
    this.type = type;
    this.commands = Stream.of(new Object[][]{
            {"/help", new Class<?>[]{}},
            {"/menu", new Class<?>[]{}},
            {"/add", new Class<?>[]{Object.class}},
            {"/remove", new Class<?>[]{int.class}},
            {"/replace", new Class<?>[]{int.class, Object.class}},
            {"/replaceAll", new Class<?>[]{Object.class, Object.class}},
            {"/index", new Class<?>[]{Object.class}},
            {"/sort", new Class<?>[]{String.class}},
            {"/frequency", new Class<?>[]{}},
            {"/print", new Class<?>[]{int.class}},
            {"/printAll", new Class<?>[]{String.class}},
            {"/getRandom", new Class<?>[]{}},
            {"/count", new Class<?>[]{Object.class}},
            {"/size", new Class<?>[]{}},
            {"/equals", new Class<?>[]{int.class, int.class}},
            {"/readFile", new Class<?>[]{String.class}},
            {"/writeFile", new Class<?>[]{String.class}},
            {"/clear", new Class<?>[]{}},
            {"/compare", new Class<?>[]{int.class, int.class}},
            {"/mirror", new Class<?>[]{}},
            {"/unique", new Class<?>[]{}},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Class<?>[]) data[1]));
  }

  void Run() {
    Scanner scanner = new Scanner(System.in);
    while (!finished) {
      this.command = "";
      this.args.clear();
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
            else if (commands.get(command)[i - 1].equals(Object.class)) {
              if (type.equals(Integer.class)) {
                args.add((T) Integer.valueOf(data[i]));
              } else if (type.equals(String.class)) {
                args.add((T) data[i]);
              } else if (type.equals(Boolean.class)) {
                if (!data[i].equals("true") && !data[i].equals("false"))
                  throw new Exception();
                args.add((T) Boolean.valueOf(data[i].equals("true")));
              }
            } else {
              args.add(data[i]);
            }
          } catch (Exception e) {
            ErrorWithContinue("Some arguments can't be parsed");
          }
        }
        Method method;
        try {
          method = this.getClass().getDeclaredMethod(command.substring(1), commands.get(command));
        } catch (NoSuchMethodException e) {
          method = Memorize.class.getDeclaredMethod(command.substring(1), commands.get(command));
        }
        method.invoke(this, args.toArray());
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
  }

  void menu() {
    this.finished = true;
  }

  void add(T element) {
    list.add(element);
    System.out.println("Element " + element + " added");
  }

  void remove(int index) {
    if (CheckIndexOutOfBounds(index)) return;
    list.remove(index);
    System.out.println("Element on " + index + " position removed");
  }

  void replace(int index, T element) {
    if (CheckIndexOutOfBounds(index)) return;
    list.set(index, element);
    System.out.println("Element on " + index + " position replaced with " + element);
  }

  void replaceAll(T from, T to) {
    Collections.replaceAll(list, from, to);
    System.out.println("Each " + from + " element replaced with " + to);
  }

  void index(T value) {
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
    list.sort(way.equals("descending") ? new CustomComparator().reversed() : new CustomComparator());
    System.out.printf("Memory sorted %s\n", way);
  }

  void frequency() {
    if (list.size() == 0) {
      System.out.println("There is no elements memorized");
      return;
    }
    Map<T, Long> counts = list.stream()
            .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

    System.out.println("Frequency:");
    for (Map.Entry<T, Long> entry : counts.entrySet()) {
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
        System.out.println("List of elements:\n" +
                Arrays.stream(list.toArray()).map(String::valueOf).collect(Collectors.joining("\n")));
        break;
      case "oneLine":
        System.out.println("List of elements:\n" +
                Arrays.stream(list.toArray()).map(String::valueOf).collect(Collectors.joining(" ")));
        break;
      default:
        Error("Incorrect argument, possible arguments: asList, lineByLine, oneLine");
        break;
    }
  }

  void count(T value) {
    System.out.println("Amount of " + value + ": " + Collections.frequency(list, value));
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
    FileReaderThread<T> readerThread = new FileReaderThread<>(path, list, type);
    readerThread.start();
    try {
      readerThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Data imported: " + (list.size() - before));
  }

  void writeFile(String path) {
    FileWriter<T> writer = new FileWriter<>();
    writer.write(path, list);
    System.out.println("Data exported: " + list.size());
  }

  void clear() {
    list.clear();
    System.out.println("Data cleared");
  }

  void compare(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int res = new CustomComparator().compare(list.get(i), list.get(j));
    System.out.println("Result: " + list.get(i) + (res > 0 ? " > " : res < 0 ? " < " : " = ") + list.get(j));
  }

  void mirror() {
    Collections.reverse(list);
    System.out.println("Data reversed");
  }

  void unique() {
    Set<T> unique = new HashSet<>(list);
    System.out.println("Unique values: " + Arrays.toString(unique.toArray()));
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

  public static class CustomComparator implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
      if (o1 instanceof Boolean && o2 instanceof Boolean) {
        return ((Boolean) o1).compareTo((Boolean) o2);
      } else if (o1 instanceof String && o2 instanceof String) {
        return ((String) o1).compareTo((String) o2);
      } else if (o1 instanceof Integer && o2 instanceof Integer) {
        return ((Integer) o1).compareTo((Integer) o2);
      } else {
        return 0;
      }
    }
  }
}