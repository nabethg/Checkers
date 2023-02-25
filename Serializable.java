import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * [Serializable.java]
 * An interface that indicates an object can be serialized and transferred via
 * an IOStream.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public interface Serializable {
    public void writeObject(OutputStream output) throws IOException;

    public void readObject(InputStream input) throws IOException, ClassNotFoundException;
}