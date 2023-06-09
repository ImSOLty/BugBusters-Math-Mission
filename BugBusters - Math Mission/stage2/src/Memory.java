import java.util.Scanner;

class Memory {
  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Welcome to Data Memory!\n" +
              "Possible actions:\n" +
              "1. Memorize booleans\n" +
              "2. Memorize numbers\n" +
              "3. Memorize words\n" +
              "0. Quit");
      String choice = scanner.next();
      switch (choice) {
        case "1":
          new BooleanMemorize().Run();
          break;
        case "2":
          new NumberMemorize().Run();
          break;
        case "3":
          new WordMemorize().Run();
          break;
        case "0":
          return;
      }
    }
  }
}