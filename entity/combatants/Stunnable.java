package entity.combatants;

public interface Stunnable {

    boolean isStunned();

    void setStunned(boolean stunned);

    int getStunDuration();

    void setStunDuration(int duration);
}
