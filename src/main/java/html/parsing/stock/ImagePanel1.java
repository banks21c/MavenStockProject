/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel1 extends JPanel {

    private BufferedImage image;

    public ImagePanel1() {
        try {
            image = ImageIO.read(new File("image name and path"));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
    }

}
