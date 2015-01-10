package main.java.com.java8;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.java.com.java8.Utils.printMessage;

public class MyLambdas {

    public void executeTests() {
        testBasicSupplier();
        testIntConsumer();
        testBasicUnaryOperator();
    }

    public void testBasicSupplier() {
        printMessage("\nTest Basic supplier:");
        infoSupplier(Logger.getLogger("My Logger"), () -> "\nMy lambda message from Supplier");
    }

    private void infoSupplier(Logger logger, Supplier<String> message) {
        if (logger.isLoggable(Level.INFO)) {
            printMessage(message.get());
        }
    }

    public void testIntConsumer() {
        printMessage("\nTest basic Int Consumer:");
        repeat(3, i -> printMessage("Count down: " + (3 - i)));

        printMessage("\nTest Runnable Consumer:");
        repeat(2, () -> printMessage("Runnable.run() executed"));
    }

    private void repeat(int times, IntConsumer action) {
        for (int i = 0; i < times; i++) {
            action.accept(i);
        }
    }

    private void repeat(int times, Runnable action) {
        for (int i = 0; i < times; i++) {
            action.run();
        }
    }

    public void testBasicUnaryOperator() {
        printMessage("\nBasic Image transformation using UnaryOperator:");
        Image image = new Image(getPreparedImageFis());
        Image brighter = transform(image, Color::brighter);
        Utils.saveImage(brighter, "jpg", "eiffel-tower-brighter.jpg");

        Image brighterByFactor = transform(image, brighter(1.8f));
        Utils.saveImage(brighterByFactor, "jpg", "eiffel-tower-brighter-by-factor.jpg");

        Image res = LattenImage.from(image)
                .transform(brighter(1.8f))
                .transform(Color::grayscale)
                .toImage();
        Utils.saveImage(res, "jpg", "eiffel-tower-list-operators-transformation.jpg");
    }

    private FileInputStream getPreparedImageFis() {
        File origin = new File("src/main/resources/eiffel-tower.jpg");
        File result = new File("eiffel-tower.jpg");
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(origin);
            fos = new FileOutputStream(result);
            byte[] buf = new byte[1024];
            int length;
            while ((length = fis.read(buf)) > 0) {
                fos.write(buf, 0, length);
            }
            return new FileInputStream(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fos != null) try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private UnaryOperator<Color> brighter(float factor) {
        return c -> c.deriveColor(0, 1, factor, 1);
    }

    private Image transform(Image image, UnaryOperator<Color> f) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage output = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                output.getPixelWriter().setColor(x, y,
                        f.apply(image.getPixelReader().getColor(x, y)));
            }
        }
        return output;
    }
}
