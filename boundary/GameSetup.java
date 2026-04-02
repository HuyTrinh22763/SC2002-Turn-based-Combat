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
        SMOKE_BOMB("Smoke Bomb");

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
                default:
                    throw new IllegalStateException("Unsupported item choice: " + this);
            }
        }
    }

    private final PlayerClass playerClass;
    private final DifficultyLevel difficultyLevel;
    private final List<ItemChoice> selectedItems;

    public GameSetup(PlayerClass playerClass, DifficultyLevel difficultyLevel, List<ItemChoice> selectedItems) {
        if (playerClass == null) {
            throw new IllegalArgumentException("Player class cannot be null.");
        }
        if (difficultyLevel == null) {
            throw new IllegalArgumentException("Difficulty level cannot be null.");
        }
        if (selectedItems == null || selectedItems.size() != 2) {
            throw new IllegalArgumentException("Exactly 2 items must be selected.");
        }

        this.playerClass = playerClass;
        this.difficultyLevel = difficultyLevel;
        this.selectedItems = Collections.unmodifiableList(new ArrayList<>(selectedItems));
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public List<ItemChoice> getSelectedItemsView() {
        return selectedItems;
    }
}
