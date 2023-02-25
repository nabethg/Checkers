import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * [Settings.java]
 * A class that encapsulates a collection of settings parameters including theme,
 * sound effects, AI difficulty, time limit, and the player's pieces' color.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class Settings implements WritableToTxt, Serializable {
    public static final int NATIVE_THEME = 0, DARK_THEME = 1, LIGHT_THEME = 2;
    public static final int MIN_AI_DIFFICULTY = 1, MAX_AI_DIFFICULTY = 8;
    public static final int[] TIME_LIMITS = { 1, 2, 3, 5, 10, 30 };

    private int theme;
    private boolean hasSoundEffects;
    private int AIDifficulty;
    private int timeLimitIndex;
    private boolean playAsBlack;

    public Settings() {
    }

    public Settings(String fileName) throws FileNotFoundException {
        Scanner input = new Scanner(new File(fileName));
        this.theme = input.nextInt();
        this.hasSoundEffects = input.nextBoolean();
        this.AIDifficulty = input.nextInt();
        this.timeLimitIndex = input.nextInt();
        this.playAsBlack = input.nextBoolean();
    }

    public int getTheme() {
        return this.theme;
    }

    public boolean hasSoundEffects() {
        return this.hasSoundEffects;
    }

    public int getAIDifficulty() {
        return this.AIDifficulty;
    }

    public int getTimeLimitIndex() {
        return this.timeLimitIndex;
    }

    /**
     * getTimeMilli
     * Returns the time limit based on its index within available time limits.
     * 
     * @return a long representing the time limit in milliseconds.
     */
    public long getTimeMilli() {
        return 1000 * 60 * Settings.TIME_LIMITS[this.timeLimitIndex];
    }

    public boolean PlayAsBlack() {
        return this.playAsBlack;
    }

    /**
     * getPlayer
     * Returns a player based on the player's pieces' color.
     * 
     * @return a Player representing the player associated with these Settings.
     */
    public Player getPlayer() {
        if (this.playAsBlack == true) {
            return Player.PLAYER1;
        } else {
            return Player.PLAYER2;
        }
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public void setHasSoundEffects(boolean hasSoundEffects) {
        this.hasSoundEffects = hasSoundEffects;
    }

    public void setAIDifficulty(int AIDifficulty) {
        this.AIDifficulty = AIDifficulty;
    }

    public void setTimeLimitIndex(int timeLimitIndex) {
        this.timeLimitIndex = timeLimitIndex;
    }

    public void setPlayAsBlack(boolean goesFirst) {
        this.playAsBlack = goesFirst;
    }

    @Override
    public void writeToTxt(String fileName) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new FileOutputStream(new File(fileName), false));
        output.println(this.theme);
        output.println(this.hasSoundEffects);
        output.println(this.AIDifficulty);
        output.println(this.timeLimitIndex);
        output.println(this.playAsBlack);
        output.close();
    }

    @Override
    public void writeObject(OutputStream output) throws IOException {
        output.write(this.timeLimitIndex);
        byte playAsBlack = (byte) ((this.playAsBlack == true) ? 0 : 1);
        output.write(playAsBlack);
    }

    @Override
    public void readObject(InputStream input) throws IOException, ClassNotFoundException {
        this.timeLimitIndex = input.read();
        this.playAsBlack = (input.read() == 1);
    }
}