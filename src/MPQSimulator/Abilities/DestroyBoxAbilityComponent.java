package MPQSimulator.Abilities;

import java.util.Arrays;
import java.util.List;

import MPQSimulator.Core.Tile.TileColor;

/*Represents abilities that destroy a Box (really Squares) on the board. ie Punisher's Judgement*/
public class DestroyBoxAbilityComponent implements AbilityComponent {
  /*Distance from Center of Box to destroy size 1 = 3x3 size 2 = 5x5 size 0 = single tile
  You can't create even length sides this way but that's probably not an issues since I
  can't think of an ability that destroys 2x2, 4x4 ect. areas.*/
  public final int size;
  /*The number of Boxes to destroy for all practical purposes this is 1 but GSBW and Hood
  each have abilities that destroy multiple boxes*/
  public final int number;
  //The color(s) for the initial chosen center tile (useful for iMags Red where it's a Red CD that destroys a Box)
  public final List<TileColor> tileColorsToDestroy;

  //Most useful constructor since size is probably the only thing you care about.
  public DestroyBoxAbilityComponent(int size) {
    this(size, 1, AbilityComponent.ALL_COLORS_LIST);
  }

  //Other less usefull constructors that are hopefully somewhat obvious
  public DestroyBoxAbilityComponent(int size, int number) {
    this(size, number, AbilityComponent.ALL_COLORS_LIST);
  }

  public DestroyBoxAbilityComponent(int size, TileColor color) {
    this(size, 1, Arrays.asList(color));
  }

  public DestroyBoxAbilityComponent(int size, int number, TileColor color) {
    this(size, number, Arrays.asList(color));
  }

  public DestroyBoxAbilityComponent(int size, List<TileColor> colors) {
    this(size, 1, colors);
  }

  public DestroyBoxAbilityComponent(int size, int number, List<TileColor> colors) {
    this.size = size;
    this.number = number;
    this.tileColorsToDestroy = colors;
  }
}