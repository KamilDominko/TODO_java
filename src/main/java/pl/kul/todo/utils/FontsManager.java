package pl.kul.todo.utils;

import pl.kul.todo.constants.CommonConstans;

import java.awt.*;
import java.io.File;

public class FontsManager {
    public Font createFont(float size) {
        try {
            File customFontFile = new File(FontsManager.class.getClassLoader().getResource("fonts/LEMONMILK-Light.otf").getPath());
            return Font.createFont(Font.TRUETYPE_FONT, customFontFile).deriveFont(size);
        } catch (Exception e) {
            System.out.println("Błąd: " + e);
        }
        return null;
    }
}
