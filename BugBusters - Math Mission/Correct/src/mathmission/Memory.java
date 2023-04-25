package mathmission;
import java.util.Scanner;

class ContinueException extends Exception {
  public ContinueException(String errorMessage) {
    super(errorMessage);
  }
}

class Memory {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.gc();
      System.out.println("Welcome to Data Memory!\n" +
              "Possible actions:\n" +
              "1. Memorize booleans\n" +
              "2. Memorize numbers\n" +
              "3. Memorize words\n" +
              "0. Quit");
      String choice = scanner.next();
      switch (choice) {
        case "1":
          new MemorizeBoolean().Run();
          break;
        case "2":
          new MemorizeInteger().Run();
          break;
        case "3":
          new MemorizeString().Run();
          break;
        case "0":
          return;
        default:
          System.out.println("Incorrect command!");
          break;
      }
    }
  }
}