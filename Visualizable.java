import java.awt.Graphics2D;

/**
 * [Visualizable.java]
 * An interface that indicates an object can be visualized by the Visualizer
 * class.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public interface Visualizable {
    public void draw(Graphics2D g2d, int x, int y, int width, int height);
}