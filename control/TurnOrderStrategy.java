package control;

import entity.combatants.Combatant;
import java.util.List;
// Open space for future extension of the game where other attributes can decide the turn order logic rather than Speed
public interface TurnOrderStrategy {
    List<Combatant> determineOrder(List<Combatant> combatants);
}
