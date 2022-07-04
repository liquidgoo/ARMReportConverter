import javax.swing.text.html.CSS;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class HeightUtil {
  static BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_BINARY);
   static Graphics2D g2d = (Graphics2D) image.getGraphics();

    public static float calculateHeight(String text, int fontSize, float width) {
        Font font = new Font("Times New Roman", Font.PLAIN, fontSize);
        AttributedString attributedString = new AttributedString(text.equals("") ? " " : text);
        attributedString.addAttribute(TextAttribute.FONT, font);

        AttributedCharacterIterator paragraph = attributedString.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = g2d.getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        float drawPosY = 0;

        lineMeasurer.setPosition(paragraphStart);

        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width);

            drawPosY += layout.getAscent();

            drawPosY += layout.getDescent() + layout.getLeading();
        }
        return drawPosY;
    }
}
