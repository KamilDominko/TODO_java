package utils;

import constants.CommonConstans;

import java.awt.*;
import java.io.File;

public class FontsManager {
    public Font createFont(float size) {
        try {
            File customFontFile = new File(CommonConstans.FONT_FILE_PATH);
            return Font.createFont(Font.TRUETYPE_FONT, customFontFile).deriveFont(size);
        } catch (Exception e) {
            System.out.println("Błąd: " + e);
        }
        return null;
    }
}
