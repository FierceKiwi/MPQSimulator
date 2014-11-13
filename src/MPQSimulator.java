import MPQSimulator.Core.Tile.TileColor;
import MPQSimulator.Core.Tile;
import MPQSimulator.Core.GameBoardMoveResults;
import MPQSimulator.Core.GameBoardMoveResults.MatchedTileBlob;
import MPQSimulator.Core.GameEngine;
import MPQSimulator.Core.Simulation;
import MPQSimulator.MPQCharacters.LokiDarkReign;
import MPQSimulator.MPQCharacters.MPQCharacter;
import MPQSimulator.MPQCharacters.MagnetoClassic;
import MPQSimulator.MPQCharacters.Mystique;
import MPQSimulator.MPQCharacters.Punisher;
import MPQSimulator.MPQCharacters.Ragnarok;
import MPQSimulator.MPQCharacters.StormMohawk;
import MPQSimulator.MPQCharacters.MPQCharacter.AbilityLevel;
import java.util.List;

public class MPQSimulator {

  public static void main(String[] args) {
    MPQCharacter punpun = new Punisher();
    System.out.println("Punisher Judgement:\n");
    Simulation sim = new Simulation(punpun.getAbility2(AbilityLevel.ONE));
    sim.printResults();

    MPQCharacter rags = new Ragnarok();
    System.out.println("\nRagnarok Godlike Power\n");
    sim = new Simulation(rags.getAbility1(AbilityLevel.ONE));
    sim.printResults();

    System.out.println("\nRagnarok Thunderstrike\n");
    sim = new Simulation(rags.getAbility2(AbilityLevel.ONE));
    sim.printResults();
    sim = new Simulation(rags.getAbility2(AbilityLevel.TWO));
    sim.printResults();
    sim = new Simulation(rags.getAbility2(AbilityLevel.FOUR));
    sim.printResults();

    MPQCharacter storm = new StormMohawk();
    MPQCharacter cmags = new MagnetoClassic();
    MPQCharacter loki = new LokiDarkReign();

    System.out.println("\nMohawk Mistress:\n");
    for (int i = 0; i < 5; i++) {
      sim = new Simulation(storm.getAbility2(AbilityLevel.values()[i]));
      sim.printResults();
    }

    System.out.println("\nC Mags Blue:\n");
    sim = new Simulation(cmags.getAbility3(AbilityLevel.ONE));
    sim.printResults();
    sim = new Simulation(cmags.getAbility3(AbilityLevel.THREE));
    sim.printResults();
    sim = new Simulation(cmags.getAbility3(AbilityLevel.FIVE));
    sim.printResults();

    System.out.println("\nC Mags Red:\n");
    sim = new Simulation(cmags.getAbility2(AbilityLevel.ONE));
    sim.printResults();

    /*System.out.println("\nLoki:\n");
    for (int i = 0; i < 5; i++) {
      Simulation sim = new Simulation(loki.getAbility2(AbilityLevel.values()[i]));
      sim.printResults();
    }*/
  }
}
