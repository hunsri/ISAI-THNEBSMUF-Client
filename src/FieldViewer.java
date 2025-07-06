import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FieldViewer extends JPanel {
    private BufferedImage image;
    private Field field;

    public FieldViewer(Field field) {
        this.field = field;
        int scale = 5;

        image = new BufferedImage(Field.SIZE*scale, Field.SIZE*scale, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(Field.SIZE*scale, Field.SIZE*scale));

        Color color = new Color (0, 0, 0);
        for (int y = 0; y < Field.SIZE; y++) {
            for (int x = 0; x < Field.SIZE; x++) {
                int value = field.getCachedAreaId(x, y);
                color = new Color(value, value, value).brighter();

                image.setRGB(x*scale, y*scale, color.getRGB());

                for(int i = 0; i < scale; i++) {
                //     image.setRGB(x*scale+i, y*scale, color.getRGB());
                    for(int j = 0; j < scale; j++) {
                        image.setRGB(x*scale+i, y*scale+j, color.getRGB());
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    // Example method to manipulate a pixel
    public void setPixel(int x, int y, int rgb) {
        if (x >= 0 && x < 256 && y >= 0 && y < 256) {
            image.setRGB(x, y, rgb);
            repaint();
        }
    }

    public void display() {
        JFrame frame = new JFrame("FieldViewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

    // Main method to show the viewer
    public static void main(String[] args) {
        JFrame frame = new JFrame("FieldViewer");
        FieldViewer viewer = new FieldViewer(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(viewer);
        frame.pack();
        frame.setVisible(true);
    }
}
