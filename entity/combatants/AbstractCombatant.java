package entity.combatants;

import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionResult;
import java.util.List;

public abstract class AbstractCombatant implements Combatant {

    protected String name;
    protected int maxHp;
    protected int baseAttack;
    protected int baseDefense;
    protected int baseSpeed;

    protected int currentHp;
    protected int currentAttack;
    protected int currentDefense;
    protected int currentSpeed;

    protected int attackModifier;
    protected int defenseModifier;
    protected int speedModifier;

    protected boolean stunned;
    protected int stunDuration;

    protected int specialCooldown;
    protected static final int SPECIAL_COOLDOWN_MAX = 3;

    protected final java.util.List<TurnObserver> statusEffects;

    public AbstractCombatant(String name, int maxHp, int baseAttack, int baseDefense, int baseSpeed) {
        this.name = name;
        this.maxHp = AttributeManager.clampHp(maxHp);
        this.baseAttack = AttributeManager.clampAttack(baseAttack);
        this.baseDefense = AttributeManager.clampDefense(baseDefense);
        this.baseSpeed = AttributeManager.clampSpeed(baseSpeed);

        this.currentHp = this.maxHp;
        this.currentAttack = this.baseAttack;
        this.currentDefense = this.baseDefense;
        this.currentSpeed = this.baseSpeed;

        this.attackModifier = 0;
        this.defenseModifier = 0;
        this.speedModifier = 0;

        this.stunned = false;
        this.stunDuration = 0;
        this.specialCooldown = 0;
        this.statusEffects = new java.util.ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public int getAttack() {
        return AttributeManager.clampAttack(currentAttack + attackModifier);
    }

    @Override
    public int getDefense() {
        return AttributeManager.clampDefense(currentDefense + defenseModifier);
    }

    @Override
    public int getSpeed() {
        return AttributeManager.clampSpeed(currentSpeed + speedModifier);
    }

    @Override
    public void takeDamage(int actualDamage) {
        if (actualDamage < 0) return;

        currentHp = AttributeManager.clampHp(currentHp - actualDamage);
    }

    @Override
    public void heal(int amount) {
        if (amount < 0) return;

        int capped = Math.min(maxHp, currentHp + amount);
        currentHp = AttributeManager.clampHp(capped);
    }

    @Override
    public boolean isAlive() {
        return AttributeManager.isAlive(currentHp);
    }

    @Override
    public void modifyAttack(int amount) {
        this.attackModifier += amount;
    }

    @Override
    public void modifyDefense(int amount) {
        this.defenseModifier += amount;
    }

    @Override
    public void modifySpeed(int amount) {
        this.speedModifier += amount;
    }

    @Override
    public void resetTemporaryModifiers() {
        this.attackModifier = 0;
        this.defenseModifier = 0;
        this.speedModifier = 0;
        this.statusEffects.clear();
    }

    @Override
    public void addStatusEffect(TurnObserver effect) {
        if (effect != null) {
            statusEffects.add(effect);
            effect.onAttached(this);
        }
    }

    @Override
    public boolean isStunned() {
        return stunDuration > 0;
    }

    @Override
    public void setStunned(boolean value) {
        if (!value) {
            stunDuration = 0;
        }
        syncStunFromDuration();
    }

    @Override
    public int getStunDuration() {
        return stunDuration;
    }

    @Override
    public void setStunDuration(int duration) {
        stunDuration = Math.max(0, duration);
        syncStunFromDuration();
    }

    private void syncStunFromDuration() {
        stunned = stunDuration > 0;
    }

    @Override
    public boolean canUseSpecial() {
        return specialCooldown == 0;
    }

    @Override
    public void useSpecial() {
        if (canUseSpecial()) {
            specialCooldown = SPECIAL_COOLDOWN_MAX;
        }
    }

    @Override
    public void reduceCooldown() {
        if (specialCooldown > 0) {
            specialCooldown--;
        }
    }

    @Override
    public int getSpecialCooldown() {
        return specialCooldown;
    }

    @Override
    public void onTurnStart() {
        for (TurnObserver effect : new java.util.ArrayList<>(statusEffects)) {
            effect.onTurnStart(this);
        }
    }

    @Override
    public void onTurnEnd() {
        if (stunDuration > 0) {
            stunDuration--;
        }
        syncStunFromDuration();
        
        java.util.Iterator<TurnObserver> iterator = statusEffects.iterator();
        while (iterator.hasNext()) {
            TurnObserver effect = iterator.next();
            effect.onTurnEnd(this);
            if (effect.isExpired()) {
                iterator.remove();
            }
        }
    }

    @Override
    public ActionResult performTurn(ActionProcessor processor, List<Combatant> enemies) {
        return ActionResult.failure(null, "Turn logic not yet implemented for " + getName());
    }

    @Override
    public int getLevelSpecialKills() {
        return 0;
    }

    @Override
    public int getLevelSpecialBonus() {
        return 0;
    }

    @Override
    public void resetLevelSpecialProgressForLevelEnd() {
    }

    @Override
    public PlayerClass getPlayerClass() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d, ATK: %d, DEF: %d, SPD: %d%s]",
                name, currentHp, maxHp, getAttack(), getDefense(), getSpeed(),
                isStunned() ? " (STUNNED)" : "");
    }
}