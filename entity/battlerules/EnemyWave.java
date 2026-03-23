package entity.battlerules;

import entity.combatants.EnemyType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnemyWave {

    private final List<EnemyType> enemies;

    private EnemyWave(List<EnemyType> enemies) {
        this.enemies = Collections.unmodifiableList(new ArrayList<>(enemies));
    }

    public static EnemyWave of(EnemyType type, int count) {
        List<EnemyType> enemies = new ArrayList<>();
        addIfPositive(enemies, type, count);
        return new EnemyWave(enemies);
    }

    public static EnemyWave of(EnemyType firstType, int firstCount, EnemyType secondType, int secondCount) {
        List<EnemyType> enemies = new ArrayList<>();
        addIfPositive(enemies, firstType, firstCount);
        addIfPositive(enemies, secondType, secondCount);
        return new EnemyWave(enemies);
    }

    // For example, if a wave has 2 Wolves and 1 Goblin, "enemies" list will look like this: [WOLF, WOLF, GOBLIN]
    private static void addIfPositive(List<EnemyType> enemies, EnemyType type, int count) {
        if (type == null || count <= 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            enemies.add(type);
        }
    }

    public int totalEnemies() {
        return enemies.size();
    }

    public List<EnemyType> getEnemiesView() {
        return enemies;
    }
    // List<> is the interface/type contract
    // ArrayList<> is one concrete implementation of List<> interface
    // ArrayList is-a List
    // Return List<> because it's more general, and more flexibility changing function's code
    // without being afraid of type mismatch
    public List<EnemyType> toExpandedEnemyTypes() {
        return new ArrayList<>(enemies);
    }
}
