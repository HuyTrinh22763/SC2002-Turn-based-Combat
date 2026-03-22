package entity;

public interface Stunnable {

    boolean isStunned();

    void setStunned(boolean stunned);

    int getStunDuration();

    void setStunDuration(int duration);
}
