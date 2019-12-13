package utils;

import java.awt.Color;

public class ColorUtils {

    public static String getHexByColor(Color color) {
        return "#" + Integer.toHexString(color.getRed()) + Integer.toHexString(color.getGreen()) + Integer.toHexString(color.getBlue());
    }
}
