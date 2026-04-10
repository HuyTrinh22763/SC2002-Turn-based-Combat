package entity.combatants;

public interface TurnObserver {

    void onAttached(Combatant target);

    void onTurnStart(Combatant target);

    void onTurnEnd(Combatant target);

    boolean isExpired();
}
