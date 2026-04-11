package boundary;

import control.BattleState;
import control.SpeedBasedOrder;
import control.TurnManager;
import control.TurnManagerFactory;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;
import entity.battlerules.DifficultyLevel;
import entity.combatants.Combatant;
import entity.combatants.Player;
import entity.combatants.PlayerClass;
import control.PlayerFactory;
import control.DefaultPlayerFactory;
import entity.items.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CliGameSession {

    private static final String DEFAULT_PLAYER_NAME = "Player";

    private final CliInput input;
    private final CliRenderer renderer;

    public CliGameSession(Scanner scanner) {
        this.input = new CliInput(scanner);
        this.renderer = new CliRenderer();
    }

    public void run() {
        try {
            runGameLoop();
        } catch (QuitGameException e) {
            System.out.println("\nExiting game early. Thanks for playing!");
        }
    }

    private void runGameLoop() {
        GameSetup currentSetup = collectSetup();
        boolean running = true;
        while (running) {
            renderer.clearScreen();
            renderer.renderBattleStart(currentSetup);
            input.waitForEnter();
            runSingleGame(currentSetup);
            renderer.clearScreen();
            renderer.renderResultPage(
                    lastBattleState,
                    lastPlayer,
                    lastEnemies,
                    lastRoundCount,
                    currentSetup.getSelectedItemsView());
            List<String> postGameOptions = Arrays.asList(
                    "Replay with same setup",
                    "Start a new game",
                    "Exit game"
            );
            renderer.printPostGameMenu(postGameOptions);
            CliInput.PostGameOption option = input.choosePostGameOption(postGameOptions.size());
            if (option == CliInput.PostGameOption.REPLAY_SAME) {
                continue;
            }
            if (option == CliInput.PostGameOption.NEW_GAME) {
                currentSetup = collectSetup();
                continue;
            }
            running = false;
        }
        System.out.println("Thanks for playing.");
    }

    private BattleState lastBattleState;
    private Player lastPlayer;
    private List<Combatant> lastEnemies;
    private int lastRoundCount;

    private GameSetup collectSetup() {
        renderer.clearScreen();
        renderer.renderSetupPage();

        PlayerClass playerClass = choosePlayerClass();
        List<GameSetup.ItemChoice> selectedItems = chooseTwoItems();
        entity.battlerules.DifficultyLevel difficultyLevel = chooseDifficulty();

        return new GameSetup(playerClass, difficultyLevel, selectedItems);
    }

    private void runSingleGame(GameSetup setup) {
        Player player = createPlayer(setup);
        for (GameSetup.ItemChoice itemChoice : setup.getSelectedItemsView()) {
            player.addItemToInventory(itemChoice.createItem());
        }

        TurnManager turnManager = TurnManagerFactory.fromDifficulty(player, new SpeedBasedOrder(),
                setup.getDifficultyLevel());

        while (!turnManager.isBattleOver()) {
            turnManager.startNewRound();
            renderer.clearScreen();
            renderer.renderRoundHeader(
                    turnManager.getRoundNumber(),
                    turnManager.getCurrentTurnOrderView(),
                    player,
                    turnManager.getEnemiesView());

            while (turnManager.hasMoreTurnsInRound() && !turnManager.isBattleOver()) {
                Combatant actor = turnManager.getCurrentActor();
                boolean isPlayerTurn = actor == player;
                renderer.printActorTurn(actor, isPlayerTurn);

                ActionRequest request = null;
                if (isPlayerTurn) {
                    request = buildPlayerActionRequest(player, turnManager.getEnemiesView());
                }

                ActionResult result = turnManager.processCurrentTurn(request);
                renderer.printActionResult(result);
                if (!isPlayerTurn) {
                    renderer.renderLiveTurnState(
                            turnManager.getRoundNumber(),
                            player,
                            turnManager.getEnemiesView());
                }
                input.waitForEnter();
            }

            if (!turnManager.isBattleOver()) {
                turnManager.endRound();
            }
            renderer.renderRoundSnapshot(
                    turnManager.getRoundNumber(),
                    player,
                    turnManager.getEnemiesView());
            input.waitForEnter();
        }

        turnManager.finalizeLevelSpecialProgress();

        lastBattleState = turnManager.getBattleState();
        lastPlayer = player;
        lastEnemies = turnManager.getEnemiesView();
        lastRoundCount = turnManager.getRoundNumber();
    }

    private ActionRequest buildPlayerActionRequest(Player player, List<Combatant> allEnemies) {
        while (true) {
            boolean hasItems = !player.getInventory().isEmpty();
            renderer.printPlayerActions(hasItems);
            int actionChoice = input.readMenuChoice("> ", 1, 4);

            if (actionChoice == 1) {
                List<Combatant> aliveEnemies = getAliveEnemies(allEnemies);
                renderer.printAliveEnemies(aliveEnemies);
                int selected = input.chooseEnemyTarget(aliveEnemies);
                return new ActionRequest(player, ActionType.BASIC_ATTACK, aliveEnemies.get(selected), allEnemies, null);
            }

            if (actionChoice == 2) {
                return new ActionRequest(player, ActionType.DEFEND, null, allEnemies, null);
            }

            if (actionChoice == 3) {
                if (!hasItems) {
                    renderer.printInvalidItemActionFallback();
                    continue;
                }
                List<Item> items = player.getInventory().getItemsView();
                renderer.printInventory(items);
                int itemIndex = input.chooseItem(items);
                Combatant target = null;
                if (items.get(itemIndex).requiresTarget(player)) {
                       List<Combatant> aliveEnemies = getAliveEnemies(allEnemies);
                       renderer.printAliveEnemies(aliveEnemies);
                       target = aliveEnemies.get(input.chooseEnemyTarget(aliveEnemies));
                }
                return new ActionRequest(player, ActionType.USE_ITEM, target, allEnemies, itemIndex);
            }

            Combatant specialTarget = null;
            if (player.getPlayerClass().requiresTargetForSpecialSkill()) {
                List<Combatant> aliveEnemies = getAliveEnemies(allEnemies);
                renderer.printAliveEnemies(aliveEnemies);
                specialTarget = aliveEnemies.get(input.chooseEnemyTarget(aliveEnemies));
            }
            return new ActionRequest(player, ActionType.SPECIAL_SKILL, specialTarget, allEnemies, null);
        }
    }

    private Player createPlayer(GameSetup setup) {
        PlayerFactory factory = new DefaultPlayerFactory();
        return factory.createPlayer(setup.getPlayerClass(), DEFAULT_PLAYER_NAME);
    }

    private PlayerClass choosePlayerClass() {
        PlayerClass[] classes = PlayerClass.values();
        List<String> classOptions = new ArrayList<>();
        for (PlayerClass pc : classes) {
            classOptions.add(pc.getDisplayName());
        }
        renderer.printSelectPlayerClassMenu(classOptions);
        int choice = input.readMenuChoice("> ", 1, classes.length);
        return classes[choice - 1];
    }

    private DifficultyLevel chooseDifficulty() {
        DifficultyLevel[] levels = DifficultyLevel.values();
        List<String> diffOptions = new ArrayList<>();
        for (DifficultyLevel dl : levels) {
            diffOptions.add(dl.name().charAt(0) + dl.name().substring(1).toLowerCase());
        }
        renderer.printSelectDifficultyMenu(diffOptions);
        int choice = input.readMenuChoice("> ", 1, levels.length);
        return levels[choice - 1];
    }

    private List<GameSetup.ItemChoice> chooseTwoItems() {
        GameSetup.ItemChoice[] items = GameSetup.ItemChoice.values();
        List<String> itemOptions = new ArrayList<>();
        for (GameSetup.ItemChoice ic : items) {
            itemOptions.add(ic.getDisplayName());
        }
        List<GameSetup.ItemChoice> selected = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            renderer.printSelectItemSlot(i, itemOptions);
            int choice = input.readMenuChoice("> ", 1, items.length);
            selected.add(items[choice - 1]);
        }
        return selected;
    }

    private List<Combatant> getAliveEnemies(List<Combatant> enemies) {
        List<Combatant> alive = new ArrayList<>();
        for (Combatant enemy : enemies) {
            if (enemy != null && enemy.isAlive()) {
                alive.add(enemy);
            }
        }
        return alive;
    }
}
