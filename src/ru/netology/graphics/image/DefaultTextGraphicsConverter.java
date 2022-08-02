package ru.netology.graphics.image;

import java.io.IOException;
import java.net.URL;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;

public class DefaultTextGraphicsConverter implements TextGraphicsConverter {
  private TextColorSchema schema;

  public DefaultTextGraphicsConverter(TextColorSchema schema) {
    this.schema = schema;
  }

  @Override
  public String convert(String url) throws IOException, BadImageSizeException {
    final BufferedImage image = ImageIO.read(new URL(url));

    final BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    final Graphics graphics = grayImage.getGraphics();

    graphics.drawImage(image, 0, 0, null);
    graphics.dispose();

    final WritableRaster raster = grayImage.getRaster();

    final int grayImageWidth = grayImage.getWidth();
    final int grayImageHeight = grayImage.getHeight();

    StringBuilder result = new StringBuilder(grayImageWidth * grayImageHeight + grayImageHeight);

    for (int j = 0; j < grayImageHeight; j++) {
      for (int i = 0; i < grayImageWidth; i++) {
          int color = raster.getPixel(i, j, new int[3])[0];
          char c = this.schema.convert(color);
          
          result.append(c);
      }

      result.append('\n');
    }

    return result.toString();
  }

  @Override
  public void setMaxWidth(int width) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setMaxHeight(int height) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setMaxRatio(double maxRatio) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setTextColorSchema(TextColorSchema schema) {
    this.schema = schema;
  }
}
