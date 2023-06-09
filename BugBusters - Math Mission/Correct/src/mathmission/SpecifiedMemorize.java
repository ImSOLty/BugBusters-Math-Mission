package mathmission;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class MemorizeInteger extends Memorize {

  public MemorizeInteger() {
    super(Integer.class);
    this.commands.putAll(
            Stream.of(new Object[][]{
                    {"/sum", new Class<?>[]{int.class, int.class}},
                    {"/subtract", new Class<?>[]{int.class, int.class}},
                    {"/multiply", new Class<?>[]{int.class, int.class}},
                    {"/divide", new Class<?>[]{int.class, int.class}},
                    {"/pow", new Class<?>[]{int.class, int.class}},
                    {"/factorial", new Class<?>[]{int.class}},
                    {"/sumAll", new Class<?>[]{}},
                    {"/average", new Class<?>[]{}}}
            ).collect(Collectors.toMap(data -> (String) data[0], data -> (Class<?>[]) data[1])));
  }

  void help() {
    super.help();
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

  void sum(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = (Integer) list.get(i), b = (Integer) list.get(j);
    int res = a + b;
    System.out.printf("Calculation performed: %d + %d = %d\n", a, b, res);
  }

  void subtract(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = (Integer) list.get(i), b = (Integer) list.get(j);
    int res = a - b;
    System.out.printf("Calculation performed: %d - %d = %d\n", a, b, res);
  }

  void multiply(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = (Integer) list.get(i), b = (Integer) list.get(j);
    int res = a * b;
    System.out.printf("Calculation performed: %d * %d = %d\n", a, b, res);
  }

  void divide(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = (Integer) list.get(i), b = (Integer) list.get(j);
    if (b == 0) {
      Error("Division by zero");
      return;
    }
    float res = (float) a / (float) b;
    System.out.printf("Calculation performed: %d / %d = %f\n", a, b, res);
  }

  void pow(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    int a = (Integer) list.get(i), b = (Integer) list.get(j);
    long res = (long) Math.pow(a, b);
    System.out.printf("Calculation performed: %d ^ %d = %d\n", a, b, res);
  }

  void factorial(int index) {
    if (CheckIndexOutOfBounds(index)) return;
    long res = LongStream.rangeClosed(1, (Integer) list.get(index)).reduce(1, (long x, long y) -> x * y);
    System.out.printf("Calculation performed: %d! = %d\n", (Integer) list.get(index), res);
  }

  void sumAll() {
    System.out.println("Sum of all elements: " + list.stream().mapToInt(Integer.class::cast).sum());
  }

  void average() {
    System.out.println("Average of all elements: " + list.stream().mapToInt(Integer.class::cast).sum() / list.size());
  }
}

class MemorizeBoolean extends Memorize {

  public MemorizeBoolean() {
    super(Boolean.class);
    this.commands.putAll(
            Stream.of(new Object[][]{
                    {"/flip", new Class<?>[]{int.class}},
                    {"/negateAll", new Class<?>[]{}},
                    {"/and", new Class<?>[]{int.class, int.class}},
                    {"/or", new Class<?>[]{int.class, int.class}},
                    {"/logShift", new Class<?>[]{int.class}},
                    {"/convertTo", new Class<?>[]{String.class}},
                    {"/morse", new Class<?>[]{}}}
            ).collect(Collectors.toMap(data -> (String) data[0], data -> (Class<?>[]) data[1])));
  }

  void help() {
    super.help();
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

  void flip(int index) {
    if (CheckIndexOutOfBounds(index)) return;
    list.set(index, !(Boolean) list.get(index));
    System.out.println("Element on " + index + " position flipped");
  }

  void negateAll() {
    list.replaceAll(e -> !(Boolean) e);
    System.out.println("All elements negated");
  }

  void and(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    boolean a = (Boolean) list.get(i), b = (Boolean) list.get(j);
    boolean res = a && b;
    System.out.printf("Operation performed: (%b && %b) is %b\n", a, b, res);
  }

  void or(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    boolean a = (Boolean) list.get(i), b = (Boolean) list.get(j);
    boolean res = a || b;
    System.out.printf("Operation performed: (%b || %b) is %b\n", a, b, res);
  }

  void logShift(int n) {
    Collections.rotate(list, n);
    System.out.println("Elements shifted by " + n);
  }

  void convertTo(String type) {
    if (list.size() == 0) {
      Error("No data memorized yet");
      return;
    }
    String binary = (String) list.stream().map(b -> (Boolean) b ? "1" : "0").collect(Collectors.joining());
    switch (type.toLowerCase()) {
      case "number":
        System.out.println("Converted: " + Long.parseLong(binary, 2));
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
    System.out.println("Morse code: " + list.stream().map(b -> (Boolean) b ? "." : "_").collect(Collectors.joining()));
  }
}


class MemorizeString extends Memorize {

  public MemorizeString() {
    super(String.class);
    this.commands.putAll(
            Stream.of(new Object[][]{
                    {"/concat", new Class<?>[]{int.class, int.class}},
                    {"/swapCase", new Class<?>[]{int.class}},
                    {"/upper", new Class<?>[]{int.class}},
                    {"/lower", new Class<?>[]{int.class}},
                    {"/reverse", new Class<?>[]{int.class}},
                    {"/length", new Class<?>[]{int.class}},
                    {"/join", new Class<?>[]{String.class}},
                    {"/regex", new Class<?>[]{String.class}}}
            ).collect(Collectors.toMap(data -> (String) data[0], data -> (Class<?>[]) data[1])));
  }

  void help() {
    super.help();
    System.out.println(
            "===================================================================================================================\n" +
                    "Word-specific commands:\n" +
                    "===================================================================================================================\n" +
                    "/concat [<int> INDEX1] [<int> INDEX2] Concatenate two specified strings\n" +
                    "/swapCase [<int> INDEX] Output swapped case version of the specified string\n" +
                    "/upper [<int> INDEX] Output uppercase version of the specified string\n" +
                    "/lower [<int> INDEX] Output lowercase version of the specified string\n" +
                    "/reverse [<int> INDEX] Output reversed version of the specified string\n" +
                    "/length [<int> INDEX] Get the length of the specified string\n" +
                    "/join [<string> DELIMITER] Join all the strings with the specified delimiter\n" +
                    "/regex [<string> PATTERN] Search for all elements that match the specified regular expression " +
                    "pattern\n" +
                    "===================================================================================================================");
  }

  void concat(int i, int j) {
    if (CheckIndexOutOfBounds(i) || CheckIndexOutOfBounds(j)) return;
    System.out.println("Concatenated string: " + list.get(i) + list.get(j));
  }

  void swapCase(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("\"%s\" string with swapped case: ", list.get(i));
    for (char c : ((String) list.get(i)).toCharArray()) {
      if (Character.isUpperCase(c)) {
        System.out.print(Character.toLowerCase(c));
      } else if (Character.isLowerCase(c)) {
        System.out.print(Character.toUpperCase(c));
      } else{
        System.out.print(c);
      }
    }
    System.out.println();
  }

  void upper(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("Uppercase \"%s\" string: %s\n", list.get(i), ((String) list.get(i)).toUpperCase());
  }

  void lower(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("Lowercase \"%s\" string: %s\n", list.get(i), ((String) list.get(i)).toLowerCase());
  }

  void reverse(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("Reversed \"%s\" string: %s\n", list.get(i), new StringBuilder((String) list.get(i)).reverse());
  }

  void length(int i) {
    if (CheckIndexOutOfBounds(i)) return;
    System.out.printf("Length of \"%s\" string: %d\n", list.get(i), ((String) list.get(i)).length());
  }

  void join(String delimiter) {
    System.out.printf("Joined string: %s\n", String.join(delimiter, list));
  }

  void regex(String regex) {
    List<String> matchingElements = null;
    try {
      matchingElements = (List<String>) list.stream()
              .filter(Pattern.compile(regex).asPredicate())
              .collect(Collectors.toList());
    } catch (PatternSyntaxException e) {
      Error("Incorrect regex pattern provided");
      return;
    }
    if (matchingElements.size() == 0) {
      System.out.println("There is no strings that match provided regex");
    }
    System.out.println("Strings that match provided regex:");
    System.out.println(Arrays.toString(matchingElements.toArray()));
  }

}