package UI.component;


import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BieuTuongAnh extends ImageIcon {

    public BieuTuongAnh(String path, int width, int height) {

        URL url = getClass().getClassLoader().getResource(path);

        if (url == null) {
            System.err.println(" Không tìm thấy ảnh: " + path);
            return;
        }

        Image img = new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        setImage(img);
    }
}
