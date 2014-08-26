/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.graphics;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;





public class Images {

    private static volatile GraphicsConfiguration config;


    public static GraphicsConfiguration config() {
        if (config == null) {
            synchronized (Images.class) {
                if (config == null) {
                    config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
                }
            }
        }
        return config;
    }


    public static BufferedImage createCompatibleImage(int width, int height, int alpha) {
        return config().createCompatibleImage(width, height, alpha);
    }


    public static BufferedImage toCompatibleImage(BufferedImage source) {
        GraphicsConfiguration config = config();

        if (source.getColorModel().equals(config.getColorModel()))
            return source;

        BufferedImage result = config.createCompatibleImage(
                source.getWidth(),
                source.getHeight(),
                source.getTransparency()
            );

        Graphics2D g2d = (Graphics2D) result.getGraphics();
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();

        return result;
    }



    public static BufferedImage createImage(int width, int height, int[] data) {
        ColorModel  cm = ColorModel.getRGBdefault();
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBuffer buffer = new DataBufferInt(data, data.length);
        WritableRaster raster = Raster.createWritableRaster(sm, buffer, null);
        return new BufferedImage(cm, raster, false, null);
    }



    private Images() {}
}
