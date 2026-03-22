package entity;

public interface SpecialSkillCooldown {

    boolean canUseSpecial();

    void useSpecial();

    void reduceCooldown();

    int getSpecialCooldown();
}
