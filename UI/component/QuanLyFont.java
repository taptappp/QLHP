package UI.component;

import java.awt.*;

public class QuanLyFont {

    public static Font customFont17;
    public static Font customFont23;

    static {
        try {
            customFont17 = new Font("Times New Roman", Font.PLAIN, 17);
            customFont23 = new Font("Times New Roman", Font.PLAIN, 23);

        } catch (Exception e) {
            e.printStackTrace();

            customFont17 = new Font("SansSerif", Font.PLAIN, 17);
            customFont23 = new Font("SansSerif", Font.PLAIN, 23);
        }
    }
}
