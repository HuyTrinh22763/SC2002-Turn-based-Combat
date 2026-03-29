package control;

import entity.combatants.Combatant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class SpeedBasedOrder implements TurnOrderStrategy{
   @Override
    public List<Combatant> determineOrder(List<Combatant> combatants) {
        List<Combatant> ordered = new ArrayList<>();

        if (combatants == null) {
            return ordered;
        }

        for (Combatant combatant : combatants) {
            if (combatant != null && combatant.isAlive()) {
                ordered.add(combatant);
            }
        }

        ordered.sort(Comparator.comparingInt(Combatant::getSpeed).reversed());
        return ordered;
    } 
    
}
