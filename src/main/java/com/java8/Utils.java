package main.java.com.java8;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Utils {
    public static void printMessage(String m) {
        System.out.println(m);
    }

    public static void saveImage(Image i, String extension, String pathname)  {
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(i, null), extension, new File(pathname));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
