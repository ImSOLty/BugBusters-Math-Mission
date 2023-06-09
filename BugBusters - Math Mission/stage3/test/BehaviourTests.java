import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BehaviourTests extends StageTest {
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

  Map<String, String> commandsRegex = Stream.of(new Object[][]{
          {"/add", "Element .+ added"},
          {"/remove", "Element on \\d+ position removed"},
          {"/replace", "Element on \\d+ position replaced with .+"},
          {"/replaceAll", "Each .+ element replaced with .+"},
          {"/index", "First occurrence of .+ is on \\d+ position"},
          {"/sort", "Memory sorted (ascending|descending)"},
          {"/frequency", "Frequency:(\\n.+: \\d+)+"},
          {"/print", "Element on \\d+ position is .+"},
          {"/printAll", "List of elements:\\n.*"},
          {"/getRandom", "Random element: .+"},
          {"/count", "Amount of .+: \\d+"},
          {"/size", "Amount of elements: \\d+"},
          {"/equals", "\\d+ and \\d+ elements are( not)? equal: .+ \\!?= .+"},
          {"/readFile", "Data imported: \\d+"},
          {"/writeFile", "Data exported: \\d+"},
          {"/clear", "Data cleared"},
          {"/compare", "Result: .+ (>|<|=) .+"},
          {"/mirror", "Data reversed"},
          {"/unique", "Unique values: \\[.+(,\\s*.+)*\\]"},

          {"/sum", "Calculation performed: -?\\d+ \\+ -?\\d+ = -?\\d+"},
          {"/subtract", "Calculation performed: -?\\d+ \\- -?\\d+ = -?\\d+"},
          {"/multiply", "Calculation performed: -?\\d+ \\* -?\\d+ = -?\\d+"},
          {"/divide", "Calculation performed: -?\\d+ \\/ -?\\d+ = -?\\d+(\\.\\d+)?"},
          {"/pow", "Calculation performed: -?\\d+ \\^ -?\\d+ = -?\\d+"},
          {"/factorial", "Calculation performed: -?\\d\\! = -?\\d+"},
          {"/sumAll", "Sum of all elements: -?\\d+"},
          {"/average", "Average of all elements: -?\\d+(\\.\\d+)?"},

          {"/flip", "Element on \\d+ position flipped"},
          {"/negateAll", "All elements negated"},
          {"/and", "Operation performed: \\((true|false) && (true|false)\\) is (true|false)"},
          {"/or", "Operation performed: \\((true|false) \\|\\| (true|false)\\) is (true|false)"},
          {"/logShift", "Elements shifted by -?\\d+"},
          {"/convertTo", "Converted: .+"},
          {"/morse", "Morse code: (\\.*\\_*)+"},

          {"/concat", "Concatenated string: .+"},
          {"/swapCase", "\".+\" string with swapped case: .+"},
          {"/upper", "Uppercase \".+\" string: .+"},
          {"/lower", "Lowercase \".+\" string: .+"},
          {"/reverse", "Reversed \".+\" string: .+"},
          {"/length", "Length of \".+\" string: \\d+"},
          {"/join", "Joined string: .+"},
          {"/regex", "((There is no strings that match provided regex)|(Strings that match provided regex:\n" +
                  "\\[.+(,\\s*.+)+\\]))"},
  }).collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

  @DynamicTest(order=0)
  CheckResult outputOnStartAndExit() {
    TestedProgram pr = new TestedProgram();
    if (!pr.start().equals(Consts.MENU_OUTPUT)) {
      return CheckResult.wrong(Consts.MODIFIED + "Incorrect output on start");
    }
    pr.execute("0");
    if (!pr.isFinished()) {
      return CheckResult.wrong(Consts.MODIFIED + "Program should terminate when choosing 0 in menu");
    }
    return CheckResult.correct();
  }

  Object[] types = {"1", "2", "3"};

  @DynamicTest(data = "types", order=1)
  CheckResult outputOnChoose(String type) {
    TestedProgram pr = new TestedProgram();
    pr.start();
    if (!pr.execute(type).toLowerCase().contains("perform action")) {
      return CheckResult.wrong(Consts.MODIFIED + "Can't find \"perform action\" substring after choosing data type");
    }
    return CheckResult.correct();
  }

  Object[][] data = {
          {"1", true, "booleans", Consts.HELP_BOOLEANS},
          {"2", 1, "numbers", Consts.HELP_NUMBERS},
          {"3", "CakeIsALie", "words", Consts.HELP_WORDS}
  };

  @DynamicTest(data = "data", order=2)
  CheckResult regexOnChooseAction(String typeEx, Object element, String type, String helpText) {
    String[] promptsBefore = {
            "/add %s",
            "/writeFile data.txt",
            "/readFile data.txt",
            "/size",
            "/frequency",
            "/print 0",
            "/printAll asList",
            "/getRandom",
            "/equals 0 1",
            "/sort ascending",
            "/index %s",
            "/count %s",
            "/compare 0 1",
            "/mirror",
            "/unique"
    };

    String[] promptsAfter = {
            "/replace 0 %s",
            "/replaceAll %s %s",
            "/remove 0",
            "/clear"
    };

    String[][] specifiedPrompts = {
            {
                    "/flip 0",
                    "/negateAll",
                    "/and 0 1",
                    "/or 0 1",
                    "/logShift 0",
                    "/convertTo number",
                    "/morse",
            },
            {
                    "/sum 0 1",
                    "/subtract 0 1",
                    "/multiply 0 1",
                    "/divide 0 1",
                    "/pow 0 1",
                    "/factorial 0",
                    "/sumAll",
                    "/average",
            },
            {
                    "/concat 0 1",
                    "/swapCase 0",
                    "/upper 0",
                    "/lower 0",
                    "/reverse 0",
                    "/length 0",
                    "/join _",
                    "/regex .+",
            }
    };

    List<String> prompts =
            new ArrayList<>(promptsBefore.length + specifiedPrompts[Integer.parseInt(typeEx) - 1].length + promptsAfter.length);
    Collections.addAll(prompts, promptsBefore);
    Collections.addAll(prompts, specifiedPrompts[Integer.parseInt(typeEx) - 1]);
    Collections.addAll(prompts, promptsAfter);

    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute(typeEx);

    String output = pr.execute("/help");
    if (!output.contains(Consts.HELP_GENERAL) || !output.contains(helpText)) {
      return CheckResult.wrong(Consts.MODIFIED + "Output for \"/help\" command not matching the initial " +
              "one for \"" + type + "\" type.");
    }

    for (String prompt : prompts) {
      output = pr.execute(prompt.replaceAll("%s", element.toString()));
      String command = prompt.split(" ")[0];
      if (!output.matches(commandsRegex.get(command) + "\\nPerform action:\\n")) {
        return CheckResult.wrong(Consts.MODIFIED + "Output for \"" + command + "\" command not matching the initial " +
                "one for \"" + type + "\" type.");
      }
    }

    output = pr.execute("/menu");
    if (!output.contains(Consts.MENU_OUTPUT)) {
      return CheckResult.wrong(Consts.MODIFIED + "Output for \"/menu\" command not matching the initial " +
              "one for \"" + type + "\" type.");
    }
    pr.execute("0");
    if (!pr.isFinished()) {
      return CheckResult.wrong(Consts.MODIFIED +
              "Program should terminate when choosing 0 in menu after working with data");
    }

    return CheckResult.correct();
  }

  Object[][] dataForHelp = {
          {"1", "booleans", Consts.HELP_BOOLEANS},
          {"2", "numbers", Consts.HELP_NUMBERS},
          {"3", "words", Consts.HELP_WORDS}
  };

  @DynamicTest(data = "dataForHelp",order=3)
  CheckResult resultOnActionHelp_switchingBetween(String typeEx, String type, String helpText) {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute(typeEx);

    String output = pr.execute("/help");
    if (!output.contains(Consts.HELP_GENERAL) || !output.contains(helpText)) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Output for \"/help\" command is incorrect for " + type + " type");
    }

    output = pr.execute("/menu");
    if (!output.contains(Consts.MENU_OUTPUT)) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Program should return to menu after choosing \"/menu\" command");
    }
    for (Object[] o : dataForHelp) {
      pr.execute(o[0].toString());

      output = pr.execute("/help");
      if (!output.contains(Consts.HELP_GENERAL) || !output.contains(o[2].toString())) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"/help\" command is incorrect for " + o[1] + " type after coming back to menu and " +
                "starting another memorizing process");
      }

      output = pr.execute("/menu");
      if (!output.contains(Consts.MENU_OUTPUT)) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Program should return to menu after choosing \"/menu\" command after coming back to menu and " +
                "starting another memorizing process");
      }
    }
    pr.execute("0");
    if (!pr.isFinished()) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Program should terminate when choosing 0 in menu after working with data");
    }

    return CheckResult.correct();
  }


  Object[][] dataForTesting() {
    return new Object[][]{
            {"1", "booleans", new Boolean[]{true, false, false, true}, false},
            {"2", "numbers", new Integer[]{2, -1, -1, 2}, 42},
            {"3", "words", new String[]{"CakeIsALie", "CakeIsATruth", "CakeIsATruth", "CakeIsALie"}, "tmp"}
    };
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

  void ElementsExistence(TestedProgram pr, Object[] expected, String command, String type) {
    String output = pr.execute("/printAll oneLine").strip();
    if (!output.equals("List of elements:\n" +
            Arrays.stream(expected).map(String::valueOf).collect(Collectors.joining(" ")).strip() +
            "\nPerform action:")) {
      throw new WrongAnswer(String.format(Consts.INCORRECT_BEHAVIOUR + "After \"%s\" command execution " +
              "\"/printAll oneLine\" output is incorrect for %s type", command, type));
    }
  }

  @DynamicTest(data = "dataForTesting", order = 4)
  CheckResult resultOnActionsAdd(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute(typeEx);
    for (int i = 0; i < data.length; i++) {
      String output = pr.execute("/add " + data[i]);
      if (!output.contains(data[i].toString())) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"/add\" command is incorrect for " + type + " type. It should contain added element.");
      }
      ElementsExistence(pr, Arrays.copyOfRange(data, 0, i + 1), "/add", type);
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 5)
  CheckResult resultOnActionsPrint(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    for (int i = 0; i < data.length; i++) {
      if (!pr.execute("/print " + i).contains(data[i].toString())) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"/print\" command is incorrect for " + type + " type. It should contain element on " +
                "specific position.");
      }
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 6)
  CheckResult resultOnActionsRemove(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    for (int i = data.length - 1; i >= 0; i--) {
      if (!pr.execute("/remove " + i).contains(String.valueOf(i))) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"/remove\" command is incorrect for " + type + " type. It should contain specified index");
      }
      ElementsExistence(pr, Arrays.copyOfRange(data, 0, i), "/remove", type);
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 7)
  CheckResult resultOnActionsReplace(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    String output;
    Object[] result = Arrays.copyOf(data, data.length);

    for (int i = data.length - 1; i >= 0; i--) {
      output = pr.execute("/replace " + i + " " + additional);
      if (!output.contains(String.valueOf(i)) || !output.contains(additional.toString())) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"/replace\" command is incorrect for " + type + " type. It should contain specified " +
                "index and a value");
      }
      result[i] = additional;
      ElementsExistence(pr, result, "/replace", type);
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 8)
  CheckResult resultOnActionsReplaceAll(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    List<Object> result = Arrays.asList(Arrays.copyOf(data, data.length));
    String output = pr.execute("/replaceAll " + data[0] + " " + additional);
    if (!output.contains(data[0].toString()) || !output.contains(additional.toString())) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Output for \"/replaceAll\" command is incorrect for " + type + " type. It should contain specified " +
              "both values");
    }
    Collections.replaceAll(result, data[0], additional);
    ElementsExistence(pr, result.toArray(), "/replaceAll", type);
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 9)
  CheckResult resultOnActionsIndex(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    List<Object> copy = Arrays.asList(data);
    for (Object o : data) {
      String output = pr.execute("/index " + o);
      int result = copy.indexOf(o);
      if (!output.contains(String.valueOf(result))) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"/index\" command is incorrect for " + type + " type. It should contain correct index");
      }
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 10)
  CheckResult resultOnActionsSort(String typeEx, String type, Object[] data, Object additional) {

    TestedProgram pr = StartAndInitialize(typeEx, data);
    List<Object> copy = Arrays.asList(data);
    pr.execute("/sort ascending");
    copy.sort(new CustomComparator());
    ElementsExistence(pr, copy.toArray(), "/sort", type);
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 11)
  CheckResult resultOnActionsFrequency(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    Map<Object, Long> counts = Arrays.stream(data)
            .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

    List<String> parts = new ArrayList<>();
    for (Map.Entry<Object, Long> entry : counts.entrySet()) {
      parts.add(entry.getKey() + ": " + entry.getValue());
    }

    String output = pr.execute("/frequency");
    for (String p : parts) {
      if (!output.contains(p)) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"/frequency\" command is incorrect for " + type + " type. It should contain correct " +
                "pairs \"element: amount\"");
      }
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 12)
  CheckResult resultOnActionsCount(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    List<Object> copy = Arrays.asList(data);
    for (Object o : data) {
      String output = pr.execute("/count " + o);
      int result = Collections.frequency(copy, o);
      if (!output.contains(String.valueOf(result))) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"/count\" command is incorrect for " + type + " type. It should contain correct number " +
                "of specified elements");
      }
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 13)
  CheckResult resultOnActionsSize(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);

    String output = pr.execute("/size");
    if (!output.contains(String.valueOf(data.length))) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Output for \"/size\" command is incorrect for " + type + " type. It should contain correct number of " +
              "elements");
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 14)
  CheckResult resultOnActionsEquals(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);

    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data.length; j++) {
        String output = pr.execute("/equals " + i + " " + j);
        boolean result = data[i] == data[j];
        if (!output.contains(String.valueOf(i)) || !output.contains(String.valueOf(j)) ||
                result && !output.contains(data[i] + " = " + data[j]) || !result && !output.contains(data[i] + " != " + data[j])) {
          return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                  "Output for \"/equals\" command is incorrect for " + type + " type. It should contain indexes, " +
                  "values and \"=\"/\"!=\" sign");
        }
      }
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 15)
  CheckResult resultOnActionsClear(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    pr.execute("/clear");
    ElementsExistence(pr, new Object[]{}, "/clear", type);
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 16)
  CheckResult resultOnActionsCompare(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data.length; j++) {
        String output = pr.execute("/compare " + i + " " + j);
        int result = new CustomComparator().compare(data[i], data[j]);
        if (!output.contains(data[i].toString()) || !output.contains(data[j].toString()) ||
                (result < 0) && !output.contains(data[i] + " < " + data[j]) ||
                (result > 0) && !output.contains(data[i] + " > " + data[j]) ||
                (result == 0) && !output.contains(data[i] + " = " + data[j])) {
          return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                  "Output for \"/compare\" command is incorrect for " + type + " type. It should contain both values " +
                  "and a correct sign");
        }
      }
    }
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 17)
  CheckResult resultOnActionsMirror(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    String output = pr.execute("/mirror");
    List<Object> copy = Arrays.asList(data);
    Collections.reverse(copy);
    ElementsExistence(pr, copy.toArray(), "/mirror", type);
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 18)
  CheckResult resultOnActionsUnique(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);
    String output = pr.execute("/unique");
    Set<Object> unique = new HashSet<>(Arrays.asList(data));
    if (!output.contains(Arrays.toString(unique.toArray()))) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Output for \"/unique\" command is incorrect for " + type + " type. It should contain correct list of " +
              "distinct values");
    }
    return CheckResult.correct();
  }

  public Map<String, String> getFiles() {
    return Map.of(
            "integers.txt", "2147483647\n".repeat(3),
            "strings.txt", "CakeIsALie\n".repeat(3),
            "booleans.txt", "true\n".repeat(3)
    );
  }

  @DynamicTest(data = "dataForTesting", order = 19, files = "getFiles")
  CheckResult resultOnActionsReadFile(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute(typeEx);

    String fileName = "";
    Object value = null;
    switch (type) {
      case "booleans":
        value = true;
        fileName = "booleans.txt";
        break;
      case "numbers":
        value = 2147483647;
        fileName = "integers.txt";
        break;
      case "words":
        value = "CakeIsALie";
        fileName = "strings.txt";
        break;
    }
    String output = pr.execute("/readFile " + fileName);
    if (!output.contains("3")) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Output for \"/readFile\" command is incorrect for " + type + " type. It should contain number of " +
              "imported " +
              "elements");
    }
    ElementsExistence(pr, new Object[]{value, value, value}, "/readFile", type);
    return CheckResult.correct();
  }

  @DynamicTest(data = "dataForTesting", order = 20)
  CheckResult resultOnActionsWriteFile(String typeEx, String type, Object[] data, Object additional) {
    TestedProgram pr = StartAndInitialize(typeEx, data);

    String fileName = "result.txt";
    String output = pr.execute("/writeFile " + fileName);
    if (!output.contains(String.valueOf(data.length))) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Output for \"/writeFile\" command is incorrect for " + type + " type. It should contain number of " +
              "exported " +
              "elements");
    }
    pr.execute("/clear");
    pr.execute("/readFile " + fileName);
    ElementsExistence(pr, data, "/writeFile\" and following \"/readFile", type);
    return CheckResult.correct();
  }

  @DynamicTest(order = 21)
  CheckResult resultOnActionsNumbers() {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute("2");
    pr.execute("/add 2");
    pr.execute("/add 4");

    String[][] promptResult = {
            {"/sum", " 0 1", "2 + 4 = 6"},
            {"/subtract", " 0 1", "2 - 4 = -2"},
            {"/multiply", " 0 1", "2 * 4 = 8"},
            {"/divide", " 0 1", "2 / 4 = 0.5"},
            {"/pow", " 0 1", "2 ^ 4 = 16"},
            {"/factorial", " 1", "4! = 24"},
            {"/sumAll", "", "6"},
            {"/average", "", "3"}
    };
    for (String[] s : promptResult)
      if (!pr.execute(s[0] + s[1]).contains(s[2])) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"" + s[0] + "\" command is incorrect for numbers type.");
      }
    return CheckResult.correct();
  }

  @DynamicTest(order = 22)
  CheckResult resultOnActionsStrings() {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute("3");
    pr.execute("/add Hello,");
    pr.execute("/add world!");

    String[][] promptResult = {
            {"/concat", " 0 1", "Hello,world!"},
            {"/swapCase", " 1", "WORLD!"},
            {"/upper", " 0", "HELLO,"},
            {"/lower", " 0", "hello,"},
            {"/reverse", " 0", ",olleH"},
            {"/length", " 0", "6"},
            {"/join", " _", "Hello,_world!"},
            {"/regex", " H.+", "[Hello,]"},
            {"/regex", " .+\\!", "[world!]"}
    };
    for (String[] s : promptResult)
      if (!pr.execute(s[0] + s[1]).contains(s[2])) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"" + s[0] + "\" command is incorrect for words type.");
      }
    return CheckResult.correct();
  }

  @DynamicTest(order = 23)
  CheckResult resultOnActionsBooleans() {
    TestedProgram pr = new TestedProgram();
    pr.start();
    pr.execute("1");
    List<Boolean> list = new ArrayList<>(List.of(false, true, false, true, false, false, false, false));

    for (Boolean s : list)
      pr.execute("/add " + s);

    String output = pr.execute("/flip 0");
    list.set(0, !list.get(0));
    if (!output.contains("0")) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Output for \"/flip\" command is incorrect for booleans type. It should contain specified position");
    }
    ElementsExistence(pr, list.toArray(), "/flip", "booleans");

    pr.execute("/negateAll");
    list.replaceAll(e -> !e);
    ElementsExistence(pr, list.toArray(), "/negateAll", "booleans");

    output = pr.execute("/logShift 2");
    Collections.rotate(list, 2);
    if (!output.contains("2")) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Output for \"/logShift\" command is incorrect for booleans type. It should contain specified amount");
    }
    ElementsExistence(pr, list.toArray(), "/logShift", "booleans");

    output = pr.execute("/logShift -2");
    Collections.rotate(list, -2);
    if (!output.contains("-2")) {
      return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
              "Output for \"/logShift\" command is incorrect for booleans type. It should contain specified amount");
    }
    ElementsExistence(pr, list.toArray(), "/logShift", "booleans");

    String[][] promptResult = {
            {"/and", " 0 4", "(false && true) is false"},
            {"/or", " 0 4", "(false || true) is true"},
            {"/convertTo", " string", "/"},
            {"/convertTo", " number", "47"},
            {"/morse", "", "__._...."},
    };

    for (String[] s : promptResult)
      if (!pr.execute(s[0] + s[1]).contains(s[2])) {
        return CheckResult.wrong(Consts.INCORRECT_BEHAVIOUR +
                "Output for \"" + s[0] + "\" command is incorrect for booleans type.");
      }
    return CheckResult.correct();
  }
}