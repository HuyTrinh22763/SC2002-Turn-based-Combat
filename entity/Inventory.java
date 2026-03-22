package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {

    private final List<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<Item> getItemsView() {
        return Collections.unmodifiableList(items);
    }

    public String useItem(int index, Player user, List<Combatant> enemies, Combatant selectedTarget) {
        if (index < 0 || index >= items.size()) {
            return "Invalid item selection.";
        }
        Item item = items.get(index);
        if (!item.canUse(user, enemies, selectedTarget)){
            return item.cannotUseMessage(user, enemies, selectedTarget);
        }
        items.remove(index);
        return item.use(user, enemies, selectedTarget);
    }
}
