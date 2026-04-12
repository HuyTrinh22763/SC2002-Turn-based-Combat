package control;

import entity.combatants.Combatant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A turn order strategy that shuffles the combatants every round.
 */
public class RandomTurnStrategy implements TurnOrderStrategy {

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

        // Shuffle the list for a randomized turn order
        Collections.shuffle(ordered);

        return ordered;
    }
}
