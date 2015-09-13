import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that handles textfiles, files that can be read and written
 * with Strings.
 *
 * PlainSimple 2015. See github.com/Plain-Simple/TextFile for more
 * information.
 *
 * @author Stefan Kussmaul
 */
public class TextFile extends File {

  /**
   * Creates a new TextFile instance using the file
   * specified by the given path.
   *
   * @throws NullPointerException if the given path is null
   */
  public TextFile(String path) {
      super(path);
  }

  /**
   * Reads the file and returns its contents as a String.
   *
   * @return text contained in the file as a single- or multi-
   * line String, or null if the file cannot be read.
   */
  public String readFile() {
    /* Try creating a BufferedReader using the file's path */
    try (BufferedReader reader = Files.newBufferedReader(Paths.get(getPath()))) {
        String line, text = "";
        int line_counter = 0;
        while ((line = reader.readLine()) != null) {
            if(line_counter == 0)
                text = line;
            else
                text += "\n" + line;
            line_counter++;
        }
        return text;
    } catch (IOException e) {
        return null;
    }
  }

  /**
   * Writes the given String to the file.
   *
   * @param newText String to write to the file
   */
  public boolean writeFile(String newText) {
    /* Try creating a BufferedWriter using the file's path */
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(getPath()))) {
        writer.write(newText, 0, newText.length());
        return true;
    } catch (IOException e) {
        return false;
    }
  }

  /* Writes the given String array to the file by
   * concatenating the array's elements into a single String
   * and writing it to the file.
   *
   * @param text String array to expand and write to file
   */
  public void writeFile(String[] text) {
    String file_text = "";
    for(int i = 0; i < text.length; i++)
        file_text += text[i];
    writeFile(file_text);
  }

  /**
   * Appends the given String to the end of the file.
   * No new line will be created unless specified by
   * the String.
   *
   * @param append String to add to the  end of the file
   */
  public void appendText(String append) {
    writeFile(readFile() + append);
  }

  /**
   * Clears the file of text.
   */
  public void clear() {
    writeFile("");
  }

  /**
   * Reads the file line by line and returns a String arrayList
   * where each element is a separate line.
   *
   * @return a String arrayList containing individual lines
   * as elements, or null if file could not be read
   */
  public ArrayList<String> readLines() {
    ArrayList<String> file_lines = new ArrayList<String> ();
    try {
        BufferedReader reader = new BufferedReader(new FileReader(this));
        String line;
        while ((line = reader.readLine()) != null) {
          file_lines.add(line);
        }
        return file_lines;
    } catch(IOException e) {
      return null;
    }
  }

  /**
   * Pastes contents of system clipboard into file.
   *
   * @return whether clipboard contents were accessed and
   * written successfully to file
   */
  public boolean pasteInto() { // Todo: fix bug where linebreaks are lost
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    /* get contents from clipboard (stored in a Transferable, which manages data transfer) */
    Transferable contents = clipboard.getContents(null);
    /* if contents are transferable, the Transferable will not be null and will be the
    correct DataFlavor (String). DataFlavor refers to the type of object something is */
    if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        try {
            return writeFile((String)contents.getTransferData(DataFlavor.stringFlavor));
        } catch (UnsupportedFlavorException|IOException e) {
            return false;
        }
    } else {
        return false;
    }
  }

  /**
   * Copies contents of file to system clipboard.
   * If the file cannot be read system clipboard will
   * not be overwritten.
   *
   * @return whether file contents were successfully
   * copied
   */
   public boolean copyFrom() {
       try {
           StringSelection selection = new StringSelection(readFile());
           Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
           clipboard.setContents(selection, selection);
           return true;
       } catch(IllegalStateException e) {
           return false;
       }
   }
}
