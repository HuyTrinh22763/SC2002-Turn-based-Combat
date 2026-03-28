package control;

import entity.combatants.Combatant;
import java.util.List;

public interface TurnOrderStrategy {
    List<Combatant> determineOrder(List<Combatant> combatants);
    
}
