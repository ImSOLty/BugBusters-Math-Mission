import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.PatternSyntaxException;

public class ExceptionsTests extends StageTest {

  @DynamicTest()
  CheckResult MenuFeedback() {
    TestedProgram pr = new TestedProgram();
    pr.start();
    for (String s : new String[]{"4", "integer", "-2"}) {
      CheckFeedback(pr.execute(s), "Incorrect command",
              "Other option than '0', '1', '2' or '3' selected in main menu","any type");
    }
    return CheckResult.correct();
  }

  Object[][] dataForTesting() {
    return new Object[][]{
            {"1", "booleans", new Boolean[]{true, false, false, true}, true},
            {"2", "numbers", new Integer[]{2, -1, -1, 2}, 42},
            {"3", "words", new String[]{"CakeIsALie", "CakeIsATruth", "CakeIsATruth", "CakeIsALie"}, "tmp"}
    };
  }

  @DynamicTest(data = "dataForTesting")
  CheckResult NoSuchMethodExceptionCheck(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute(typeEx);
    for (String s : new String[]{"hey?", "/smile", "/draw line", "add " + additional}) {
      UpdateFeedback(pr, type, s, NoSuchMethodException.class);
      CheckFeedback(pr.execute(s), "No such command",
              "Unknown command is called", type);
    }
    for (String s : new String[]{"/add", "/compare 2"}) {
      UpdateFeedback(pr, type, s, NoSuchMethodException.class);
      CheckFeedback(pr.execute(s), "Incorrect amount of arguments",
              "Incorrect amount of arguments provided to a command", type);
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting")
  CheckResult IndexOutOfBoundsExceptionCheck(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    for (String s : new String[]{"/remove 5", "/print 5", "/replace 5 " + additional, "/equals 5 2", "/compare 5 2",
            "/equals 2 5", "/compare 2 5"}) {
      UpdateFeedback(pr, type, s.split(" ")[0], IndexOutOfBoundsException.class);
      CheckFeedback(pr.execute(s), "Index out of bounds!",
              "Index out of bounds", type);
    }
    String[][] specifiedPrompts = {
            {
                    "/flip 5",
                    "/and 5 2", "/and 2 5",
                    "/or 5 2", "/or 2 5",
            },
            {
                    "/sum 5 2", "/sum 2 5",
                    "/subtract 5 2", "/subtract 2 5",
                    "/multiply 5 2", "/multiply 2 5",
                    "/divide 5 2", "/divide 2 5",
                    "/pow 5 2", "/pow 2 5",
                    "/factorial 5",
            },
            {
                    "/concat 5 2", "/concat 2 5",
                    "/swapCase 5",
                    "/upper 5",
                    "/lower 5",
                    "/reverse 5",
                    "/length 5",
            }
    };
    for (String s : specifiedPrompts[Integer.parseInt(typeEx) - 1]) {
      UpdateFeedback(pr, type, s.split(" ")[0], IndexOutOfBoundsException.class);
      CheckFeedback(pr.execute(s), "Index out of bounds!",
              "Index out of bounds", type);
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting")
  CheckResult NumberFormatExceptionCheck(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    for (String s : new String[]{"/remove A", "/print A", "/replace A " + additional, "/equals A B", "/compare A B"}) {
      UpdateFeedback(pr, type, s.split(" ")[0], NumberFormatException.class);
      CheckFeedback(pr.execute(s), "Some arguments can't be parsed!",
              "Integer arguments parsing", type);
    }
    String[][] specifiedPrompts = {
            {
                    "/flip A",
                    "/and A 2", "/and 2 A",
                    "/or A 2", "/or 2 A",
                    "/logShift A"
            },
            {
                    "/add A", "/replace 2 A", "/replaceAll 2 A", "/replaceAll A 2", "/index A", "/count A",
                    "/sum 2 A", "/sum A 2",
                    "/subtract 2 A", "/subtract A 2",
                    "/multiply 2 A", "/multiply A 2",
                    "/divide 2 A", "/divide A 2",
                    "/pow 2 A", "/pow A 2",
                    "/factorial A"
            },
            {
                    "/concat 2 A", "/concat A 2",
                    "/swapCase A",
                    "/upper A",
                    "/lower A",
                    "/reverse A",
                    "/length A",
            }
    };
    for (String s : specifiedPrompts[Integer.parseInt(typeEx) - 1]) {
      UpdateFeedback(pr, type, s.split(" ")[0], NumberFormatException.class);
      CheckFeedback(pr.execute(s), "Some arguments can't be parsed!",
              "Integer arguments parsing", type);
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting")
  CheckResult IllegalArgumentExceptionCheck(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = new TestedProgram();
    pr.start();
    UpdateFeedback(pr, type, "/getRandom", IllegalArgumentException.class);
    pr.execute(typeEx);
    CheckFeedback(pr.execute("/getRandom"), "There is no elements memorized",
            "Random element with no elements memorized", type);
    return CheckResult.correct();
  }

  @DynamicTest()
  CheckResult ArithmeticExceptionCheck() {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute("2");
    UpdateFeedback(pr, "integers", "/divide", ArithmeticException.class);
    pr.execute("/add 1");
    pr.execute("/add 0");
    CheckFeedback(pr.execute("/divide 0 1"), "Division by zero",
            "An element is divided by 0", "numbers");
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting")
  CheckResult FileNotFoundExceptionCheck(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    UpdateFeedback(pr, type, "/readFile", FileNotFoundException.class);
    CheckFeedback(pr.execute("/readFile notfound.txt"), "File not found!",
            "Reading a file, that doesn't exist", type);
    return CheckResult.correct();
  }

  @DynamicTest()
  CheckResult PatternSyntaxExceptionCheck() {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute("3");
    UpdateFeedback(pr, "words", "/regex", PatternSyntaxException.class);
    CheckFeedback(pr.execute("/regex ())"), "Incorrect regex pattern provided",
            "Calling regex operation with incorrect expression", "words");
    return CheckResult.correct();
  }

  //Feedbacks

  Object[][] dataForFeedbacks() {
    return new Object[][]{
            {"1", "booleans", new Boolean[]{true, true, true, true}, false},
            {"2", "numbers", new Integer[]{1, 1, 1, 1}, 42},
            {"3", "words", new String[]{"CakeIsALie", "CakeIsALie", "CakeIsALie", "CakeIsALie"}, "CakeIsATruth"}
    };
  }

  @DynamicTest(data = "dataForFeedbacks")
  CheckResult IndexFeedbackCheck(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    CheckFeedback(pr.execute("/index " + additional), "There is no such element",
            "Calling \"/index\" operation without such element in a list", type);
    CheckFeedback(pr.execute("/sort up"), "Incorrect argument, possible arguments: ascending, descending",
            "Calling \"/sort\" operation with an argument that is nor \"ascending\" nor \"descending\"", type);
    CheckFeedback(pr.execute("/printAll somehow"), "Incorrect argument, possible arguments: asList, lineByLine, oneLine",
            "Calling \"/printAll\" operation with an argument that is nor \"asList\" nor \"lineByLine\" nor \"oneLine\"", type);
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForFeedbacks")
  CheckResult FrequencyZeroFeedbackCheck(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute(typeEx);
    CheckFeedback(pr.execute("/frequency"), "There is no elements",
            "Calling \"/frequency\" operation with 0 elements in a list", type);
    return CheckResult.correct();
  }

  @DynamicTest()
  CheckResult BooleanFeedbackCheck() {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute("1");//Booleans
    CheckFeedback(pr.execute("/convertTo number"), "No data memorized",
            "Calling \"/convertTo\" operation with 0 elements in a list", "booleans");
    CheckFeedback(pr.execute("/morse"), "No data memorized",
            "Calling \"/morse\" operation with 0 elements in a list", "booleans");
    pr.execute("/add true");
    pr.execute("/add false");
    CheckFeedback(pr.execute("/add notTrue"), "Some arguments can't be parsed",
            "Calling \"/add\" operation with an argument that is nor \"true\" nor \"false\"", "booleans");
    CheckFeedback(pr.execute("/convertTo something"), "Incorrect argument, possible arguments: string, number",
            "Calling \"/convertTo\" operation with an argument that is nor \"string\" nor \"number\"", "booleans");
    return CheckResult.correct();
  }

  @DynamicTest()
  CheckResult RegexFeedbackCheck() {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute("3");//Words
    CheckFeedback(pr.execute("/regex .+"), "There is no strings that match provided regex",
            "No elements matching provided expression (\"/regex\" operation)", "words");
    pr.execute("/add apple");
    pr.execute("/add grape");
    CheckFeedback(pr.execute("/regex $.^"), "There is no strings that match provided regex",
            "No elements matching provided expression (\"/regex\" operation)", "words");
    return CheckResult.correct();
  }

  TestedProgram StartAndInitialize(String typeEx, Object[] data) {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute(typeEx);
    for (Object o : data) {
      pr.execute("/add " + o);
    }
    return pr;
  }

  void UpdateFeedback(TestedProgram pr, String type, String function, Class cause) {
    for (Class c : new Class[]{
            IndexOutOfBoundsException.class,
            NoSuchMethodException.class,
            InvocationTargetException.class,
            NumberFormatException.class,
            ArithmeticException.class,
            StackOverflowError.class,
            IllegalArgumentException.class,
            NullPointerException.class,
            IOException.class,
            FileNotFoundException.class,
            PatternSyntaxException.class
    })
      pr.feedbackOnException(c, "Caught an expected " + c.getSimpleName() + " while performing \"" + function + "\" " +
              "function for " + type + "." + (c == InvocationTargetException.class ?
              " Possible cause: " + cause.getSimpleName() : ""));
  }

  void CheckFeedback(String got, String expected, String action, String type) {
    if (!got.toLowerCase().contains(expected.toLowerCase()))
      throw new WrongAnswer("Feedback is incorrect for \"" + action + "\" case for "+type+". Expected existence of \"" + expected + "\" substring");
  }
}
