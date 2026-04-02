package entity.items;

import java.util.List;

import entity.combatants.Combatant;
import entity.combatants.Player;

public interface Item {
    String getName();
    default boolean canUse(Player user, List<Combatant> enemies, Combatant selectedTarget){
        return true;
    }

    default boolean requiresTarget(Player user) {
        return false;
    }

    String cannotUseMessage(Player user, List<Combatant> enemies, Combatant selectedTarget);
    
    String use(Player user, List<Combatant> enemies, Combatant selectedTarget);


}
