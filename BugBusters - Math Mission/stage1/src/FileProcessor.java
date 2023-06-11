//Chapter 2

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
//One sunny morning, as Lily strolled by the village square, she noticed a...
/**
 This class provides functionality for processing files in some way.
 The FileProcessor class offers methods to read and write files.

  With the FileProcessor class at our disposal, we can confidently work with files in this application.
 It simplifies the process of reading, writing, and managing files, allowing us to focus on the actual logic of our programs.
 */
class FileReaderInteger {

  static ArrayList<Integer> list = new ArrayList<>();

  public ArrayList<Integer> read(String fileName) throws IOException {

    list.clear();

    List<String> lines = Files.readAllLines(Paths.get(fileName)  , StandardCharsets.UTF_8 );

    for (String s : lines) {

      list.add( Integer.parseInt(s)  ) ;

    }

    return list ;

  }

}
//peculiar shimmering object hidden behind a cluster of bushes. Intrigued, she cautiously...
class  FileWriterInteger   {
  public   void  write  (  String  fileName,  ArrayList <Integer>  data  ) throws  IOException  {
    FileWriter  fileWriter  =  new  FileWriter  (  fileName  )  ;
    PrintWriter  printWriter  =  new  PrintWriter  (  fileWriter  )  ;
    for  (  Integer  i  :  data  )  {
      printWriter  .  println  (  i  )  ;
    }
    printWriter  .  close  (  )  ;
  }
}

class  FileReaderWords {  private final    ArrayList<String>  list  =  new   ArrayList<>();  public   ArrayList<String>  read(String  fileName)  throws  IOException {  list.clear();  List<String>  lines  =  Files.readAllLines(Paths.get(fileName),  StandardCharsets.UTF_8);  for  (String  s  :  lines)  {  list.add(s);  }  return  list;  }  }

class FileWriterWords {
  public    void    write   (  String  fileName,    ArrayList < String >  data  )   throws    IOException  {
    FileWriter  fileWriter  =  new    FileWriter  (  fileName   )  ;
    PrintWriter    printWriter  =  new    PrintWriter  (  fileWriter  )  ;
    for    (  String  i  :  data   )  {
      printWriter   .  println  (  i  )  ;
    }
    printWriter   .  close  (  )  ;
  }
}
//approached the bushes and discovered a beautifully crafted silver key. The key...
class   FileReaderBoolean   {
  private  final  ArrayList<Boolean>   list   =   new   ArrayList<>();

  public  ArrayList<Boolean>  read   (  String  fileName  )  throws   IOException  {
    list  .  clear  (  )  ;
    List<String>  lines  =  Files  .  readAllLines  (  Paths  .  get  (  fileName  )  ,  StandardCharsets  .  UTF_8  )  ;
    for  (  String  s  :  lines  )  {
      list  .  add  (  Boolean  .  parseBoolean  (  s  )  )  ;
    }
    return  list  ;
  }
}
//gleamed in the sunlight, as if beckoning her to uncover its secrets.
class  FileWriterBoolean    {
  public   void  write  (  String  fileName,  ArrayList<Boolean>  data  )   throws  IOException   {
    FileWriter  fileWriter  =  new   FileWriter  (  fileName  ) ;
    PrintWriter   printWriter   =  new  PrintWriter  (  fileWriter  ) ;
    for  (  Boolean  i  :  data  )   {
      printWriter . println  (  i  ) ;
    }
    printWriter  .  close  (  )  ;
  }
}