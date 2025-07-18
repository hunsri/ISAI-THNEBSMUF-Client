import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FieldViewer extends JPanel {

    private final Color DEBUG_COLOR = new Color(255, 165, 0);

    private BufferedImage image;
    private Field field;

    //setting the scale of the image
    private final int scale = 5; 

    public FieldViewer(Field field) {
        this.field = field;
        image = new BufferedImage(Field.SIZE*scale, Field.SIZE*scale, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(Field.SIZE*scale, Field.SIZE*scale));

        render();
    }

    private void render() {
        Color color = new Color (0, 0, 0);

        for (int y = 0; y < Field.SIZE; y++) {
            for (int x = 0; x < Field.SIZE; x++) {

                int value = field.getFieldAreaId(x, y);
                if(value > 255) { //checking for special values
                    color = Trails.getTrail(value).getColor();
                } else {
                    color = new Color(value, value, value).brighter();
                    if(field.getDebugTrace()[x][y]) {
                        color = DEBUG_COLOR;
                    }
                }

                image.setRGB(x*scale, y*scale, color.getRGB());

                for(int i = 0; i < scale; i++) {
                    for(int j = 0; j < scale; j++) {
                        image.setRGB(x*scale+i, y*scale+j, color.getRGB());
                    }
                }
            }
        }
    }

    public void updateImage() {
        render();
        repaint();
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

    public void cleanDebug() {
        field.cleanDebug();
    }
}
