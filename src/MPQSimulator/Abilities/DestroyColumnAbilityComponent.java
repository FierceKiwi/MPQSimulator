package MPQSimulator.Abilities;

import java.util.Arrays;
import java.util.List;

import MPQSimulator.Core.Tile.TileColor;

/*Representa Abilities that destroy coloumns of tiles.
Right now only Rags did this but the old City Environ.  had one too
so it's not unthinkable this could be usefull later.*/
public class DestroyColumnAbilityComponent implements AbilityComponent {
  //List of 0-based column indexes to destroy;
  public final List<Integer> columnsToDestroy;

  //Single column constructor.
  public DestroyColumnAbilityComponent(int column) {
    this(Arrays.asList(column));
  }

  //Multe-column.
  public DestroyColumnAbilityComponent(List<Integer> columns) {
    this.columnsToDestroy = columns;
  }
}
