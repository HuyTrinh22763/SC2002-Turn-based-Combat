package entity.combatants;

public interface Combatant extends CombatantStats, Damageable, CombatStatModifier, Stunnable,
        SpecialSkillCooldown, TurnLifecycle {
}
