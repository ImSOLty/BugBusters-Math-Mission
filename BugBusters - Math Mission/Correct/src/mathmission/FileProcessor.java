package mathmission;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

class FileReaderThread<T> extends Thread {
  private final LinkedList<T> ll;
  private final String fileName;
  private final Class<T> clazz;
  private int amount = 0;

  public FileReaderThread(String fileName, LinkedList<T> ll, Class<T> clazz) {
    this.fileName = fileName;
    this.ll = ll;
    this.clazz = clazz;
  }

  @Override
  public void run() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
      char[] buffer = new char[1024];
      int bytesRead;
      StringBuilder sb = new StringBuilder();
      String leftPart = "";
      while ((bytesRead = br.read(buffer)) != -1) {
        sb.append(buffer, 0, bytesRead);
        String[] parts = sb.toString().split("\n", -1);
        if (parts.length > 1) {
          leftPart = parts[parts.length - 1];
          parts[parts.length - 1] = null;
        }
        for (String part : parts) {
          if (part != null && !part.equals("")) {
            if (clazz == Integer.class) {
              ll.push((T) Integer.valueOf(part));
            } else if (clazz == String.class) {
              ll.push((T) part);
            } else if (clazz == Boolean.class) {
              ll.push((T) Boolean.valueOf(part.equals("true")));
            }
            amount++;
          }
        }
        sb.setLength(0);
        sb.append(leftPart);
      }
      String[] parts = sb.toString().split("\n", -1);
      for (String part : parts) {
        if (part != null && !part.equals("")) {
          if (clazz == Integer.class) {
            ll.push((T) Integer.valueOf(part));
          } else if (clazz == String.class) {
            ll.push((T) part);
          } else if (clazz == Boolean.class) {
            ll.push((T) Boolean.valueOf(part.equals("true")));
          }
          amount++;
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("Error while performing command \"/readFile\"!\nDescription: File not found!");
    } catch (IOException e) {
      System.out.println("Error while performing command \"/readFile\"!\nDescription: File can't be imported!");
    }
  }
}

class FileWriter<T> {
  public void write(String fileName, LinkedList<T> data) {
    String stringData = Arrays.stream(data.toArray()).map(String::valueOf).collect(Collectors.joining("\n"));
    try (FileOutputStream fos = new FileOutputStream(fileName);
         BufferedOutputStream bos = new BufferedOutputStream(fos)) {
      byte[] bytes = stringData.getBytes();
      bos.write(bytes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}