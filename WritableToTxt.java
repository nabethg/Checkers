import java.io.FileNotFoundException;

/**
 * [WritableToTxt.java]
 * An interface that indicates an object can be written to a '.txt' file.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public interface WritableToTxt {
    public void writeToTxt(String fileName) throws FileNotFoundException;
}