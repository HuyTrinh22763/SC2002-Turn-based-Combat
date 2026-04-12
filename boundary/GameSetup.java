package boundary;

import entity.battlerules.DifficultyLevel;
import entity.combatants.PlayerClass;
import entity.items.Item;
import entity.items.Potion;
import entity.items.PowerStone;
import entity.items.SmokeBomb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameSetup {

    public enum ItemChoice {
        POTION("Potion"),
        POWER_STONE("Power Stone"),
        SMOKE_BOMB("Smoke Bomb"),
        CALTROPS("Caltrops");

        private final String displayName;

        ItemChoice(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Item createItem() {
            switch (this) {
                case POTION:
                    return new Potion();
                case POWER_STONE:
                    return new PowerStone();
                case SMOKE_BOMB:
                    return new SmokeBomb();
                case CALTROPS:
                    return new entity.items.Caltrops();
                default:
                    throw new IllegalStateException("Unsupported item choice: " + this);
            }
        }
    }

    public enum TurnStrategyType {
        SPEED_BASED("Speed-based (Classical)"),
        RANDOM("Random (Saga)");

        private final String displayName;

        TurnStrategyType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public control.TurnOrderStrategy createStrategy() {
            switch (this) {
                case SPEED_BASED:
                    return new control.SpeedBasedOrder();
                case RANDOM:
                    return new control.RandomTurnStrategy();
                default:
                    throw new IllegalStateException("Unsupported strategy: " + this);
            }
        }
    }

    private final PlayerClass playerClass;
    private final DifficultyLevel difficultyLevel;
    private final List<ItemChoice> selectedItems;
    private final TurnStrategyType turnStrategyType;

    public GameSetup(PlayerClass playerClass, DifficultyLevel difficultyLevel, TurnStrategyType strategyType,
            List<ItemChoice> selectedItems) {
        if (playerClass == null) {
            throw new IllegalArgumentException("Player class cannot be null.");
        }
        if (difficultyLevel == null) {
            throw new IllegalArgumentException("Difficulty level cannot be null.");
        }
        if (strategyType == null) {
            throw new IllegalArgumentException("Turn strategy type cannot be null.");
        }
        if (selectedItems == null || selectedItems.size() != 2) {
            throw new IllegalArgumentException("Exactly 2 items must be selected.");
        }

        this.playerClass = playerClass;
        this.difficultyLevel = difficultyLevel;
        this.turnStrategyType = strategyType;
        this.selectedItems = Collections.unmodifiableList(new ArrayList<>(selectedItems));
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public TurnStrategyType getTurnStrategyType() {
        return turnStrategyType;
    }

    public List<ItemChoice> getSelectedItemsView() {
        return selectedItems;
    }
}
