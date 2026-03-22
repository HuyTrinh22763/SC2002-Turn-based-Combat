package entity;

import java.util.List;

public interface Item {
    String getName();
    default boolean canUse(Player user, List<Combatant> enemies, Combatant selectedTarget){
        return true;
    }

    String cannotUseMessage(Player user, List<Combatant> enemies, Combatant selectedTarget);
    
    String use(Player user, List<Combatant> enemies, Combatant selectedTarget);


}
