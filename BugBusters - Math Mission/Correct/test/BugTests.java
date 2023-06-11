import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.output.InfiniteLoopDetector;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.*;
import java.util.stream.LongStream;

public class BugTests extends StageTest {

  static class CustomComparator implements Comparator<Object> {
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

  Object[][] dataForTesting() {
    return new Object[][]{
            {"1", "booleans", new Boolean[]{true, false}},
            {"1", "booleans", new Boolean[]{false, false, false}},
            {"1", "booleans", new Boolean[]{true, false, true, false}},
            {"2", "numbers", new Integer[]{1, 2}},
            {"2", "numbers", new Integer[]{5, 3, 4}},
            {"2", "numbers", new Integer[]{7, 8, 6, 9}},
            {"3", "words", new String[]{"Hello", "World"}},
            {"3", "words", new String[]{"Around", "The", "world"}},
            {"3", "words", new String[]{"Four", "Very", "interesting", "Words"}}
    };
  }

  public Map<String, String> getFiles() {
    return Map.of(
            "numbers.txt", "2147483647\n".repeat(10),
            "words.txt", "CakeIsALie\n".repeat(10),
            "booleans.txt", "true\nfalse\n".repeat(5),
            "numbers2.txt", "2147483647\n2147483647\n",
            "words2.txt", "CakeIsALie\nCakeIsALie\n",
            "booleans2.txt", "true\nfalse\n"
    );
  }

  //Each type
  @DynamicTest(data = "dataForTesting")
  CheckResult GetRandomCheck(String typeEx, String type, Object[] data) {
    InfiniteLoopDetector.setCheckSameInputBetweenRequests(false);
    TestedProgram pr = StartAndInitialize(typeEx, data);
    String output;
    for (Object o : data) {
      int k = 0;
      do {
        output = pr.execute("/getRandom");
        k++;
      } while (!output.contains(o.toString()) && k != 50);
      if (k == 50) {
        return CheckResult.wrong("There is a bug in \"/getRandom\" function (" + type + "). Some of added values " +
                "can't be shown. Unlikely this is happening due to chance, but if you're sure of your solution - " +
                "restart the tests.");
      }
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", files = "getFiles")
  CheckResult ReadFileCheck(String typeEx, String type, Object[] data) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    String output = pr.execute("/readFile " + type + ".txt");
    if (!output.contains("10")) {
      return CheckResult.wrong("There is a bug in \"/readFile\" function (" + type + "). The number of only imported " +
              "elements should be printed");
    }
    output = pr.execute("/readFile " + type + "2.txt");
    if (!output.contains("2")) {
      return CheckResult.wrong("There is a bug in \"/readFile\" function (" + type + "). The number of only imported " +
              "elements should be printed");
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting")
  CheckResult MirrorCheck(String typeEx, String type, Object[] data) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    pr.execute("/mirror");
    String output = pr.execute("/printAll asList");
    List<Object> reversed = Arrays.asList(data);
    Collections.reverse(reversed);
    if (!output.contains(Arrays.toString(reversed.toArray()))) {
      return CheckResult.wrong("There is a bug in \"/mirror\" function (" + type + "). The order of the elements in " +
              "list should be mirrored, so after printing them all they should be printed in reverse order");
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting")
  CheckResult SortCheck(String typeEx, String type, Object[] data) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    List<Object> copy = Arrays.asList(data);
    pr.execute("/sort ascending");
    copy.sort(new CustomComparator());
    String output = pr.execute("/printAll asList").strip();
    if (!output.contains(Arrays.toString(copy.toArray()))) {
      return CheckResult.wrong("There is a bug in \"/sort\" function (" + type + "). The order of elements after " +
              "ascending sort is incorrect.");
    }
    pr.execute("/sort descending");
    copy.sort(new CustomComparator().reversed());
    output = pr.execute("/printAll asList").strip();
    if (!output.contains(Arrays.toString(copy.toArray()))) {
      return CheckResult.wrong("There is a bug in \"/sort\" function (" + type + "). The order of elements after " +
              "descending sort is incorrect.");
    }
    return CheckResult.correct();
  }

  Object[][] dataForBooleans() {
    return new Object[][]{
            {new Boolean[]{true, false, true, false, false, true, true, false}},
            {new Boolean[]{true, true, true, false, true, false, true, true, false, false}},
            {new Boolean[]{false, false, false, true, true, true, false, true, false, true, true, false}},
    };
  }

  //Booleans
  @DynamicTest(data = "dataForBooleans")
  CheckResult AndOrFlipCheck(Boolean[] data) {
    TestedProgram pr = StartAndInitialize("1", data);
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data.length; j++) {
        if (!pr.execute("/and " + i + " " + j).contains("(" + data[i] + " && " + data[j] + ") is " + (data[i] &&
                data[j]))) {
          return CheckResult.wrong("There is a bug in \"/and\" function (booleans). Result is incorrect.");
        }
        if (!pr.execute("/or " + i + " " + j).contains("(" + data[i] + " || " + data[j] + ") is " + (data[i] ||
                data[j]))) {
          return CheckResult.wrong("There is a bug in \"/or\" function (booleans). Result is incorrect.");
        }
      }
    }
    for (int i = 0; i < data.length; i++) {
      pr.execute("/flip " + i);
      if (!pr.execute("/print " + i).contains(String.valueOf(!data[i]))) {
        return CheckResult.wrong("There is a bug in \"/flip\" function (booleans). Result is incorrect.");
      }
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForBooleans")
  CheckResult LogShiftCheck(Boolean[] data) {
    TestedProgram pr = StartAndInitialize("1", data);
    List<Object> copy = Arrays.asList(data);
    String output;
    for (int i = 0; i < 10; i++) {
      pr.execute("/logShift " + i);
      Collections.rotate(copy, i);
      output = pr.execute("/printAll asList").strip();
      if (!output.contains(Arrays.toString(copy.toArray()))) {
        return CheckResult.wrong("There is a bug in \"/logShift\" function (booleans). Elements should be shifted " +
                "by an integer, provided as an argument.");
      }
    }
    return CheckResult.correct();
  }


  Object[][] dataForWords() {
    return new Object[][]{
            {new String[]{"Lorem", "ipsuM"}},
            {new String[]{"doLoR", "sIT", "AMet"}},
            {new String[]{"coNseCTetUr", "AdiPisCIng", "ElIT", "sed"}},
    };
  }

  //Words
  @DynamicTest(data = "dataForWords")
  CheckResult JoinSwapCaseCheck(String[] data) {
    TestedProgram pr = StartAndInitialize("3", data);
    for (String s : new String[]{"_", ",", "DOT"}) {
      String output = pr.execute("/join " + s);
      if (!output.contains(String.join(s, data))) {
        return CheckResult.wrong("There is a bug in \"/join\" function (words). Elements should be joined " +
                "by a delimiter, provided as an argument.");
      }
    }
    for (int i = 0; i < data.length; i++) {
      StringBuilder s = new StringBuilder(data[i]);
      for (int j = 0; j < s.length(); j++) {
        char c = s.charAt(j);
        if (Character.isUpperCase(c)) {
          s.setCharAt(j, Character.toLowerCase(c));
        } else if (Character.isLowerCase(c)) {
          s.setCharAt(j, Character.toUpperCase(c));
        }
      }
      if (!pr.execute("/swapCase " + i).contains(s)) {
        return CheckResult.wrong("There is a bug in \"/swapCase\" function (words). Result is incorrect.");
      }
    }
    return CheckResult.correct();
  }

  Object[][] dataForNumbers() {
    return new Object[][]{
            {new Integer[]{2, 4}},
            {new Integer[]{6, 1, 5}},
            {new Integer[]{0, 1, 0, 3}},
    };
  }

  //Numbers
  @DynamicTest(data = "dataForNumbers")
  CheckResult FactorialAverageCheck(Integer[] data) {
    TestedProgram pr = StartAndInitialize("2", data);
    for (int i = 0; i < data.length; i++) {
      String output = pr.execute("/factorial " + i);
      long res = LongStream.rangeClosed(1, data[i]).reduce(1, (long x, long y) -> x * y);
      if (!output.contains(String.valueOf(res))) {
        return CheckResult.wrong("There is a bug in \"/factorial\" function (numbers). Result is incorrect.");
      }
    }
    String output = pr.execute("/average");
    if (!output.contains(String.valueOf(Arrays.stream(data).mapToInt(Integer::intValue).sum() / data.length))) {
      return CheckResult.wrong("There is a bug in \"/average\" function (numbers). Result is incorrect.");
    }
    return CheckResult.correct();
  }

  Object[][] dataForOverUnderflow() {
    return new Object[][]{
            {new Integer[]{2147483647, 2147483647}, new String[]{"4294967294", "0", "4611686014132420609",
                    "4294967294", "2147483647"}},
            {new Integer[]{-2147483647, -2147483647}, new String[]{"-4294967294", "0", "4611686014132420609",
                    "-4294967294", "-2147483647"}},
            {new Integer[]{-2147483647, 2147483647}, new String[]{"0", "-4294967294", "-4611686014132420609", "0", "0"
            }},
            {new Integer[]{2147483647, -2147483647}, new String[]{"0", "4294967294", "-4611686014132420609", "0", "0"}},
    };
  }

  @DynamicTest(data = "dataForOverUnderflow")
  CheckResult OverflowUnderflowCheck(Integer[] data, String[] result) {
    TestedProgram pr = StartAndInitialize("2", data);
    if (!pr.execute("/sum 0 1").contains(result[0])) {
      return CheckResult.wrong("There is a bug in \"/sum\" function (numbers). Result is incorrect.");
    }
    if (!pr.execute("/subtract 0 1").contains(result[1])) {
      return CheckResult.wrong("There is a bug in \"/subtract\" function (numbers). Result is incorrect.");
    }
    if (!pr.execute("/multiply 0 1").contains(result[2])) {
      return CheckResult.wrong("There is a bug in \"/multiply\" function (numbers). Result is incorrect.");
    }
    if (!pr.execute("/sumAll").contains(result[3])) {
      return CheckResult.wrong("There is a bug in \"/sumAll\" function (numbers). Result is incorrect.");
    }
    if (!pr.execute("/average").contains(result[4])) {
      return CheckResult.wrong("There is a bug in \"/average\" function (numbers). Result is incorrect.");
    }
    return CheckResult.correct();
  }

  Object[][] dataForPowDivide() {
    return new Object[][]{
            {new Integer[]{9, 19}, "/pow 0 1", "1350851717672992000"},
            {new Integer[]{6, 1000000}, "/divide 0 1", "0,000006"},
            {new Integer[]{1000001, 1000000}, "/divide 0 1", "1,000001"},
            {new Integer[]{8, -2}, "/pow 0 1", "0,015625"},
            {new Integer[]{1, 1, 1, 2}, "/average", "1,25"},
    };
  }

  @DynamicTest(data = "dataForPowDivide")
  CheckResult PowDivideCheck(Integer[] data, String prompt, String result) {
    TestedProgram pr = StartAndInitialize("2", data);
    if (!pr.execute(prompt).replaceAll("\\.", ",").contains(result)) {
      return CheckResult.wrong("There is a bug in \"" + prompt.split(" ")[0] + "\" function (numbers). Result is " +
              "incorrect.");
    }
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
}
