package MPQSimulator.Core;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import MPQSimulator.Abilities.Ability;
import MPQSimulator.Abilities.AbilityComponent;
import MPQSimulator.Abilities.AbilityComponent.TileLocation;
import MPQSimulator.Abilities.ChangeTileColorAbilityComponent;
import MPQSimulator.Abilities.DestroyTileAbilityComponent;
import MPQSimulator.Abilities.DestroyBoxAbilityComponent;
import MPQSimulator.Abilities.DestroyColumnAbilityComponent;
import MPQSimulator.Abilities.SwapTileAbilityComponent;
import MPQSimulator.Core.GameBoardMoveResults.MatchedTileBlob;
import MPQSimulator.Core.Tile.TileColor;


public class GameEngine {
  private GameBoard board;

  public static final int NUM_BOARD_ROWS = 8;
  public static final int NUM_BOARD_COLS = 8;
  public static final int NUM_TILES_ON_BOARD = NUM_BOARD_COLS * NUM_BOARD_ROWS;
  // This is pretty messy, maybe think of another way to deal with TileColors.

  public GameEngine() {
      this.board = new GameBoard(NUM_BOARD_ROWS, NUM_BOARD_COLS);
      stabilizeBoard();
  }

  public GameEngineMoveResults useAbilityAndStabilizeBoard(Ability ability) {
      useAbility(ability);
      return stabilizeBoard();
  }


  public GameEngineMoveResults stabilizeBoard() {

    GameEngineMoveResults overallResults = new GameEngineMoveResults();

    GameEngineMoveResults currentMoveResults = resolveCurrentBoard();
    while (!currentMoveResults.empty()) {
      /*int totalTilesDestroyed = 0;
      for (TileColor color : currentMoveResults.getTilesDestroyedCount().keySet()) {
        totalTilesDestroyed += currentMoveResults.getTilesDestroyedCount().get(color);
      }

      if (totalTilesDestroyed > 20) {
        System.out.println("wat");
      }
      System.out.println("Total tiles destroyed: " + totalTilesDestroyed);*/
      overallResults.add(currentMoveResults);
      currentMoveResults = resolveCurrentBoard();
    }

    return overallResults;
  }

  // Finds and destroys all tiles involved in match 3s+ on the current board.
  public GameEngineMoveResults resolveCurrentBoard() {
    GameEngineMoveResults engineResults = new GameEngineMoveResults();

    GameBoardMoveResults results = board.findMatchesOnBoard();
    Set<Tile> tilesToDestroy = results.getDestroyedTileSet();

    List<MatchedTileBlob> blobs = results.findTileBlobs();
    for (MatchedTileBlob blob : blobs) {
      Set<Integer> rowsToDestroy = blob.getHorizontalMatchFours();
      for (Integer row : rowsToDestroy) {
        tilesToDestroy.addAll(board.getTilesInRow(row));
      }
      Set<Integer> colsToDestroy = blob.getVerticalMatchFours();
      for (Integer col : colsToDestroy) {
        tilesToDestroy.addAll(board.getTilesInCol(col));
      }
    }

    engineResults.addDestroyedTiles(tilesToDestroy);
    board.destroyTiles(tilesToDestroy);
    return engineResults;
  }

  private void useAbility(Ability ability) {
    List<AbilityComponent> components = ability.getComponents();
    Iterator<AbilityComponent> it = components.iterator();
    while (it.hasNext()) {
      AbilityComponent component = it.next();
      if (component instanceof SwapTileAbilityComponent) {
        processSwapTileAbility((SwapTileAbilityComponent) component);
      } else if (component instanceof DestroyTileAbilityComponent) {
        processDestroyTileAbilityComponent((DestroyTileAbilityComponent) component);
      } else if (component instanceof ChangeTileColorAbilityComponent) {
        processChangeTileColorAbilityComponent((ChangeTileColorAbilityComponent) component);
      } else if (component instanceof DestroyColumnAbilityComponent) {
        processDestroyColumnAbilityComponent((DestroyColumnAbilityComponent) component);
      } else if (component instanceof DestroyBoxAbilityComponent) {
        processDestroyBoxAbilityComponent((DestroyBoxAbilityComponent) component);
      }
    }
  }

  private void processChangeTileColorAbilityComponent(ChangeTileColorAbilityComponent component) {
    Set<Tile> tileSet = board.getTiles(component.oldTileColors);
    List<Tile> randomizedTileList = new ArrayList<Tile>(tileSet);
    Collections.shuffle(randomizedTileList);

    List<Tile> tilesToChangeColor = randomizedTileList.subList(0, Math.min(randomizedTileList.size(), component.maxTilesToChange));
    board.changeTileColor(new HashSet<Tile>(tilesToChangeColor), component.newTileColors);
  }

  // Processes abilities involving destroying tiles.
  private void processDestroyTileAbilityComponent(DestroyTileAbilityComponent component) {
    Set<Tile> tileSet = board.getTiles(component.tileColorsToDestroy);
    List<Tile> randomizedTileList = new ArrayList<Tile>(tileSet);
    Collections.shuffle(randomizedTileList);
    List<Tile> tilesToDestroy = randomizedTileList.subList(
        0, Math.min(randomizedTileList.size(), component.maxTilesToDestroy));

    Set<Tile> tileSetToDestroy = new HashSet<>(tilesToDestroy);
    board.destroyTiles(tileSetToDestroy);
  }

  // Logic for processing abilities involving swapping tiles.
  private void processSwapTileAbility(SwapTileAbilityComponent component) {

    if (component.tileBLocation != TileLocation.RANDOM) {
      throw new IllegalArgumentException();
    }

    Set<Tile> tileSetA = component.tileALocation == TileLocation.RANDOM
        ? board.getTiles(component.tileAColors)
            : Sets.newHashSet(board.getTile(component.tileARow, component.tileACol));
    Set<Tile> tileSetB = board.getTiles(component.tileBColors);

    List<Tile> randomizedTileListA = new ArrayList<Tile>(tileSetA);
    Collections.shuffle(randomizedTileListA);
    List<Tile> randomizedTileListB = new ArrayList<Tile>(tileSetB);
    Collections.shuffle(randomizedTileListB);

    Set<Tile> alreadyShuffledTiles = new HashSet<>();
    Iterator<Tile> aIt = randomizedTileListA.iterator();
    Iterator<Tile> bIt = randomizedTileListB.iterator();
    int tilePairsSwapped = 0;
    Tile tileA = null;
    Tile tileB = null;
    while ( (component.tilePairsToSwap > tilePairsSwapped)
        && aIt.hasNext() && bIt.hasNext()) {

      // Search through the lists until we find a tile A and B that hasn't already been shuffled.
      if (tileA == null) {
        Tile next = aIt.next();
        if (!alreadyShuffledTiles.contains(next)) {
          tileA = next;
        }
      }

      if (tileB == null) {
        Tile next = bIt.next();
        if (!alreadyShuffledTiles.contains(next)) {
          tileB = next;
        }
      }

      // If it's the same tile, and tileA is fixed, keep on looking through tileB.
      if (tileA == tileB && component.tileALocation == TileLocation.FIXED
          && component.tileBLocation == TileLocation.RANDOM) {
        tileB = null;
      }

      if (tileA != null && tileB != null) {
        board.swapTiles(tileA, tileB);
        alreadyShuffledTiles.add(tileA);
        alreadyShuffledTiles.add(tileB);
        tilePairsSwapped++;
        tileA = null;
        tileB = null;
      }
    }
  }

  private void processDestroyColumnAbilityComponent(DestroyColumnAbilityComponent component) {
    Set<Tile> tileSetToDestroy = new HashSet<>();
    for (int c : component.columnsToDestroy) {
      //Add each column in component to the list of tiles to destroy
      tileSetToDestroy.addAll(board.getTilesInCol(c));
    }
    board.destroyTiles(tileSetToDestroy);
  }

  private void processDestroyBoxAbilityComponent(DestroyBoxAbilityComponent component) {
    //Get all the valid center targets
    /*If they ever make abilities not fall of the edge you might need to restrict the
    edges of the board from this list*/
    Set<Tile> tiles = board.getTiles(component.tileColorsToDestroy);
    //Randomize the tiles
    List<Tile> randomtiles = new ArrayList<Tile>(tiles);
    Collections.shuffle(randomtiles);
    //Get a Set for the tiles we want to destroy to go in.
    Set<Tile> tilesToDestroy = new HashSet<Tile>();

    //Destroy a component.number of boxen to destroy
    for (int i = 0; i < component.number; i++) {
      Tile center;
      if (i < randomtiles.size()) {
        //Get the ith randomized Tile for the center of the box
        /*Another bit that might need to be restricted if they ever make it so boxes can't
        overlap*/
        center = randomtiles.get(i);
      } else {
        //We don't have any valid centers so just break out (should almsot never actually happen)
        break;
      }

      //Get the position of Center tile so we can find all it's neighbors to kill.
      int row = center.getRow();
      int col = center.getCol();
      int size = component.size;
      //System.out.println(String.format("(%d, %d): %s", col, row, center.getColor()));
      //Loop through each (x, y) pair that's in our range
      for (int x = col - size; x <= col + size; x++) {
        for (int y = row - size; y <= row + size; y++) {
          //If that tile is actually on the board add it to our Set of tiles to destory.
          if (x >= 0 && x < NUM_BOARD_COLS && y >= 00 && y < NUM_BOARD_ROWS) {
            //Tile temp = board.getTile(y, x);
            //System.out.println(String.format("(%d, %d): %s", x, y, temp.getColor()));
            tilesToDestroy.add(board.getTile(y, x));
          }
        }
      }
      //System.out.println(board);
    }
    //Destroy tiles.
    board.destroyTiles(tilesToDestroy);
  }
}
