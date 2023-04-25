import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoryTests extends StageTest {

  public Map<String, String> getFiles() {
    return Map.of(
            "numbers.txt", "2147483647\n".repeat(1_500_000),
            "words.txt", "CakeIsALie\n".repeat(1_500_000),
            "booleans.txt", "true\nfalse\n".repeat(750_000)
    );
  }

  //  @DynamicTest(timeLimit = 100_000, files = "getFiles")
//  CheckResult TestDebug() {
//    List<String> usageList = new ArrayList<>();
//    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
//    System.gc();
//    TestedProgram pr = new TestedProgram();
//    pr.start();
//
//    Object[][] data = {
//            {"1", "boolean", "booleans.txt", "false", 750_000},
//            {"2", "numbers", "numbers.txt", "2147483647", 1_500_000},
//            {"3", "words", "words.txt", "CakeIsALie", 1_500_000}
//    };
//    for (Object[] d : data) {
//      pr.execute((String) d[0]);
//      for (int i = 0; i < 4; i++) {
//        usageList.add("Before " + d[1] + " " + i + " allocation: \t\t" + memoryMXBean.getHeapMemoryUsage().getUsed());
//        pr.execute("/readFile " + d[2]);
//        pr.execute("/size");
//        pr.execute("/count " + d[3]);
//      }
//      pr.execute("/menu");
//      usageList.add("After " + d[1] + " allocation: \t\t" + memoryMXBean.getHeapMemoryUsage().getUsed());
//    }
//    System.gc();
//    usageList.add("After gc: \t\t" + memoryMXBean.getHeapMemoryUsage().getUsed());
//    for (String u : usageList) {
//      System.out.println(u);
//    }
//    pr.execute("0");
//    return CheckResult.correct();
//  }
  void updateFeedbackOnException(TestedProgram pr, String lastCommand, String lastMemorizerType) {
    pr.feedbackOnException(InvocationTargetException.class, "Your program threw an InvocationTargetException. Most " +
            "likely it happened due to OutOfMemoryError, caused by memory leaks while executing \"" + lastCommand +
            "\" for " + lastMemorizerType + " type");
    pr.feedbackOnException(OutOfMemoryError.class, "Your program threw an OutOfMemoryError. Most " +
            "likely it is caused by memory leaks while executing \"" + lastCommand + "\" for " + lastMemorizerType +
            " type");
  }

  @DynamicTest(timeLimit = 100_000, files = "getFiles")
  CheckResult memoryTest() {
    String lastCommand = "";
    String lastMemorizerType = "";

    List<String> commands = new ArrayList<>();
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    //InfiniteLoopDetector.setCheckSameInputBetweenRequests(false);
    System.gc();
    TestedProgram pr = new TestedProgram();

    pr.start();

    Object[][] data = {
            {"1", "boolean", "booleans.txt", "false", 750_000},
            {"2", "numbers", "numbers.txt", "2147483647", 1_500_000},
            {"3", "words", "words.txt", "CakeIsALie", 1_500_000}
    };
    long startUsage = memoryMXBean.getHeapMemoryUsage().getUsed();
    for (Object[] d : data) {
      lastMemorizerType = (String) d[1];
      updateFeedbackOnException(pr, lastCommand, lastMemorizerType);
      if ((float) startUsage / (float) memoryMXBean.getHeapMemoryUsage().getUsed() < Consts.threshold) {
        System.gc();
        if ((float) startUsage / (float) memoryMXBean.getHeapMemoryUsage().getUsed() >= Consts.threshold) {
          return CheckResult.wrong("After returning to the main menu, the amount of memory used by the program is " +
                  "much larger than at the start of the program. Menu is a natural place to call garbage collector to" +
                  " free up memory, used in memorizers");
        } else {
          return CheckResult.wrong("After returning to the main menu, the amount of memory used by the program is " +
                  "much larger than at the start of the program. There is a memory leak in " + d[1] + " memorizer");
        }
      } else {
        startUsage = memoryMXBean.getHeapMemoryUsage().getUsed();
      }
      pr.execute((String) d[0]);
      commands.clear();
      for (int i = 0; i < 4; i++) {
        pr.execute("/readFile " + d[2]);
        commands.add("/readFile <fileName>");
        lastCommand = "/readFile <fileName>";
        updateFeedbackOnException(pr, lastCommand, lastMemorizerType);

        String output = pr.execute("/size");
        commands.add("/size");
        lastCommand = "/size";
        updateFeedbackOnException(pr, lastCommand, lastMemorizerType);
        if (!output.contains(String.valueOf((i + 1) * 1_500_000))) {
          return CheckResult.wrong(Consts.MORE_LIKELY_OOM("/size", commands, true));
        }

        output = pr.execute("/count " + d[3]);
        commands.add("/count <value>");
        lastCommand = "/count";
        updateFeedbackOnException(pr, lastCommand, lastMemorizerType);
        if (!output.contains(String.valueOf((Integer) d[4] * (i + 1)))) {
          return CheckResult.wrong(Consts.MORE_LIKELY_OOM("/count", commands, false));
        }
      }
      pr.execute("/menu");
    }

    pr.execute("0");
    return CheckResult.correct();
  }
}