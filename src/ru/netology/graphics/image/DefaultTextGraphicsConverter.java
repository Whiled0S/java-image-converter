package ru.netology.graphics.image;

import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;

public class DefaultTextGraphicsConverter implements TextGraphicsConverter {
  private TextColorSchema schema;

  private Integer maxHeight;
  private Integer maxWidth;
  private Double maxRatio;

  public DefaultTextGraphicsConverter(TextColorSchema schema) {
    this.schema = schema;
  }

  @Override
  public String convert(String url) throws IOException, BadImageSizeException {
    final BufferedImage image = ImageIO.read(new URL(url));

    if (this.maxRatio != null) {
      this.validateRatio(image, this.maxRatio);
    }

    BufferedImage imageToConvert = this.convertToGrayscale(image);

    if (this.maxHeight != null && this.maxWidth != null) {
      imageToConvert = this.scaleToAllowedDimensions(imageToConvert, this.maxWidth, this.maxHeight);
    }

    return this.convertToCharset(imageToConvert);
  }

  @Override
  public void setMaxWidth(int width) {
    this.maxWidth = width;
  }

  @Override
  public void setMaxHeight(int height) {
    this.maxHeight = height;
  }

  @Override
  public void setMaxRatio(double maxRatio) {
    this.maxRatio = maxRatio;
  }

  @Override
  public void setTextColorSchema(TextColorSchema schema) {
    this.schema = schema;
  }

  private BufferedImage convertToGrayscale(BufferedImage image) {
    final BufferedImage grayscale = new BufferedImage(
        image.getWidth(),
        image.getHeight(),
        BufferedImage.TYPE_BYTE_GRAY);
    final Graphics2D graphics = grayscale.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    return grayscale;
  }

  private String convertToCharset(BufferedImage image) {
    final WritableRaster raster = image.getRaster();

    final int width = image.getWidth();
    final int height = image.getHeight();

    final int[] preallocatedArray = new int[3];
    final StringBuilder result = new StringBuilder(width * height + height);

    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        int color = raster.getPixel(i, j, preallocatedArray)[0];

        result.append(this.schema.convert(color));
      }

      result.append('\n');
    }

    return result.toString();
  }

  private BufferedImage scaleToAllowedDimensions(BufferedImage image, int maxWidth, int maxHeight) {
    final int width = image.getWidth();
    final int height = image.getHeight();

    if (width <= maxWidth && height <= maxHeight) {
      return image;
    }

    final double widthDiff = width / maxWidth;
    final double heightDiff = height / maxHeight;
    final double mainDiff = widthDiff > heightDiff ? widthDiff : heightDiff;

    final int newWidth = (int) Math.floor(width / mainDiff);
    final int newHeight = (int) Math.floor(height / mainDiff);

    BufferedImage scaled = new BufferedImage(newWidth, newHeight, image.getType());
    Graphics2D graphics = scaled.createGraphics();

    graphics.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, width, height, null);

    return scaled;
  }

  private void validateRatio(BufferedImage image, double maxRatio) throws BadImageSizeException {
    final double ratio = image.getWidth() / image.getHeight();

    if (ratio > this.maxRatio) {
      throw new BadImageSizeException(ratio, maxRatio);
    }
  }
}
