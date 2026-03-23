package entity.combatants;

public interface SpecialSkillCooldown {

    boolean canUseSpecial();

    void useSpecial();

    void reduceCooldown();

    int getSpecialCooldown();
}
