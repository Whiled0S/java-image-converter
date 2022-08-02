package ru.netology.graphics.image;

import java.util.Arrays;
import java.util.List;

public class DefaultTextColorSchema implements TextColorSchema {
  private final int COLOR_VARIATIONS_AMOUNT = 256;
  private final List<Character> CHARACTERS = Arrays.asList('#', '$', '@', '%', '*', '+', '-', '\'');

  @Override
  public char convert(int color) {
    return this.CHARACTERS.get(color / (this.COLOR_VARIATIONS_AMOUNT / this.CHARACTERS.size()));
  }
}