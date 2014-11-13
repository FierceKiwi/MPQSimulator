package MPQSimulator.MPQCharacters;

import java.util.ArrayList;
import java.util.List;

import MPQSimulator.Abilities.Ability;
import MPQSimulator.Abilities.AbilityComponent;
import MPQSimulator.Abilities.ChangeTileColorAbilityComponent;
import MPQSimulator.Abilities.DestroyColumnAbilityComponent;
import MPQSimulator.Core.Tile.TileColor;

public class Ragnarok extends MPQCharacter {

  //Godlike Power
  @Override
  protected List<Ability> initAbility1() {

    List<Ability> abilityList = new ArrayList<>();
    List<Integer> columns = new ArrayList<Integer>();
    columns.add(3);
    columns.add(4);
    AbilityComponent destroyColumns = new DestroyColumnAbilityComponent(columns);
    Ability ability = new Ability();
    ability.addComponent(destroyColumns);

    for (int i = 0; i < 5; i++) {
      abilityList.add(ability);
    }
    return abilityList;
  }

  //Thunderstrike
  @Override
  protected List<Ability> initAbility2() {
    int[] tilesChangedByLevel = {2, 3, 3, 4, 4};
    List<Ability> abilityList = new ArrayList<>();

    for (int i = 0; i < 5; i++) {

      AbilityComponent changeTiles = new ChangeTileColorAbilityComponent(tilesChangedByLevel[i], TileColor.GREEN);
      Ability ability = new Ability();
      ability.addComponent(changeTiles);
      abilityList.add(ability);
    }
    return abilityList;
  }

  @Override
  protected List<Ability> initAbility3() {
    //Someday maybe :(
    return null;
  }

}
