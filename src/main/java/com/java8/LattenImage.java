package main.java.com.java8;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class LattenImage {
    private Image image;
    private List<UnaryOperator<Color>> pendingOperators;

    public static LattenImage from(Image image) {
        LattenImage instance = new LattenImage();
        instance.image = image;
        instance.pendingOperators = new ArrayList<>();
        return instance;
    }

    public LattenImage transform(UnaryOperator<Color> op) {
        pendingOperators.add(op);
        return this;
    }

    public Image toImage() {
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color c = image.getPixelReader().getColor(x, y);
                for (UnaryOperator<Color> f : pendingOperators) {
                    c = f.apply(c);
                }
                out.getPixelWriter().setColor(x, y, c);
            }
        }
        return out;
    }
}
