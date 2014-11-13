package MPQSimulator.MPQCharacters;

import java.util.ArrayList;
import java.util.List;

import MPQSimulator.Abilities.Ability;
import MPQSimulator.Abilities.AbilityComponent;
import MPQSimulator.Abilities.DestroyBoxAbilityComponent;
import MPQSimulator.Core.Tile.TileColor;

public class Punisher extends MPQCharacter {

  @Override
  protected List<Ability> initAbility1() {
    return null;
  }

  //Judgement
  @Override
  protected List<Ability> initAbility2() {
    List<Ability> abilityList = new ArrayList<Ability>();
    AbilityComponent destroybox = new DestroyBoxAbilityComponent(1);
    Ability ability = new Ability();
    ability.addComponent(destroybox);

    for (int i = 0; i < 5; i++) {
      abilityList.add(ability);
    }
    return abilityList;
  }

  @Override
  protected List<Ability> initAbility3() {
    return null;
  }

}