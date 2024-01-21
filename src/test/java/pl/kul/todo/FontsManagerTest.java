package pl.kul.todo;

import org.junit.jupiter.api.Test;
import pl.kul.todo.utils.FontsManager;

import java.awt.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FontsManagerTest {

    @Test
    void testCreateFont() {
        FontsManager fontsManager = new FontsManager();

        // Test with a valid font file path
        Font validFont = fontsManager.createFont(12.0f);
        assertNotNull(validFont, "The font should not be null");

        // Verify that the font has the correct size
        assertEquals(12.0f, validFont.getSize(), 0.01, "The font size should be 12.0");

        // Test with an invalid font file path
        FontsManager invalidFontsManager = new FontsManagerWithInvalidFilePath();
        Font invalidFont = invalidFontsManager.createFont(14.0f);
        assertNull(invalidFont, "The font should be null for an invalid font file path");
    }

    // Custom FontsManager with an invalid file path for testing
    private static class FontsManagerWithInvalidFilePath extends FontsManager {
        @Override
        public Font createFont(float size) {
            try {
                // Using an invalid file path for testing
                File invalidFontFile = new File("invalid_font_file.ttf");
                return Font.createFont(Font.TRUETYPE_FONT, invalidFontFile).deriveFont(size);
            } catch (Exception ignored) {}
            return null;
        }
    }
}
