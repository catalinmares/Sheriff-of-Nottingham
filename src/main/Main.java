package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Mares Catalin-Constantin, 322CD
 *
 */
public final class Main {
    static final int MAXID = 15;
    static final int INITIALMONEY = 50;
    static final int NRLEGALGOODS = 4;
    static final int SILKBONUS = 3;
    static final int PEPPERBONUS = 2;
    static final int BARRELBONUS = 2;
    static final int MAXCARDS = 6;

    private Main() {
    }

    // Adauga toate elementele din vector intr-o lista si intoarce maximul listei
    public static int maxValue(final int[] array, final int nrElementsChecked) {
        List<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < nrElementsChecked; i++) {
            list.add(array[i]);
        }

        return Collections.max(list);
    }

    // Calculeaza pentru bunul dat cine primeste King/Queen pentru bunul respectiv
    public static void giveBonus(final int[] scores, final int[] assetCount,
    final int kingBonus, final int queenBonus) {

        // Calculam frecventa maxima a bunului
        int maxCount = Main.maxValue(assetCount, assetCount.length);
        int numberOfPlayers = scores.length;

        // Acordam King Bonus jucatorilor cu cele mai multe bunuri de acel tip
        for (int i = 0; i < numberOfPlayers; i++) {
            if (assetCount[i] == maxCount) {
                scores[i] += kingBonus;

                // Setam nr bunuri pe -1 pentru ca jucatorul i sa nu primeasca
                // King si Queen in acelasi timp
                assetCount[i] = -1;
            }
        }

        // Urmatorul maxim va fi pentru Queen Bonus
        maxCount = Main.maxValue(assetCount, assetCount.length);

        // Daca maximul este -1 toti au luat King, deci nu avem Queen Bonus de dat
        if (maxCount == -1) {
            return;
        }

        // Acordam analog Queen Bonus
        for (int i = 0; i < numberOfPlayers; i++) {
            if (assetCount[i] == maxCount) {
                scores[i] += queenBonus;
                assetCount[i] = -1;
            }
        }
    }

    // Afiseaza la consola scorurile jucatorilor in ordine descrescatoare
    /**
     * @param scores - scorurile jucatorilor
     * @param playerList - numele jucatorilor
     * @return scorul maxim
     */
    private static int printScores(final int[] scores,
    final List<String> playerList) {
        // Calculam scorul maxim
        int maxScore = Main.maxValue(scores, scores.length);
        ArrayList<String> playerNames = new ArrayList<String>();

        // Decidem cui apartine scorul
        for (int i = 0; i < scores.length; i++) {
            if (maxScore == scores[i]) {
                playerNames.add(playerList.get(i).toUpperCase());

                // Setam scorul maxim pe 0 pentru a nu fi regasit la reapelare
                scores[i] = 0;
                }
            }

        while (playerNames.size() != 0) {
            System.out.println(playerNames.remove(0) + ": " + maxScore);
        }

        // Returnam urmatorul maxim
        return Main.maxValue(scores, scores.length);
    }

    /**
     * @param players - vectorul de jucatori
     * @return scorurile jucatorilor
     */
    public static int[] calculateScores(final BasicPlayer[] players) {
        int numberOfPlayers = players.length;
        int[] scores = new int[numberOfPlayers];
        int[] appleCount = new int[numberOfPlayers];
        int[] cheeseCount = new int[numberOfPlayers];
        int[] breadCount = new int[numberOfPlayers];
        int[] chickenCount = new int[numberOfPlayers];

        // Adaugare carti bonus din ilegale si calculare scor fara bonus
        for (int j = 0; j < numberOfPlayers; j++) {
            List<Goods> stand = players[j].getStand();
            scores[j] = players[j].getMoney();

            // Adaugare bunuri bonus pe taraba
            int initialStandSize = stand.size();
            for (int k = 0; k < initialStandSize; k++) {
                switch (stand.get(k).getName()) {
                    case "Silk":
                        for (int i = 0; i < Main.SILKBONUS; i++) {
                            stand.add(new Goods(Goods.CHEESEID));
                        }
                        break;

                    case "Pepper":
                        for (int i = 0; i < Main.PEPPERBONUS; i++) {
                        stand.add(new Goods(Goods.CHICKENID));
                        }
                        break;

                    case "Barrel":
                        for (int i = 0; i < Main.BARRELBONUS; i++) {
                            stand.add(new Goods(Goods.BREADID));
                        }
                        break;

                    default: break;
                }
            }

            // Adaugare profit de pe taraba la scor si calculare frecvente bunuri
            for (Goods asset : stand) {
                scores[j] += Goods.getProfit(asset.getId());
                switch (asset.getName()) {
                    case "Apple":
                        appleCount[j]++;
                        break;

                    case "Cheese":
                        cheeseCount[j]++;
                        break;

                    case "Bread":
                        breadCount[j]++;
                        break;

                    case "Chicken":
                        chickenCount[j]++;
                        break;

                    default: break;
                }
            }
        }

        // Acordare bonusuri de King si Queen
        Main.giveBonus(scores, appleCount, Goods.APPLEKB, Goods.APPLEQB);
        Main.giveBonus(scores, cheeseCount, Goods.CHEESEKB, Goods.CHEESEQB);
        Main.giveBonus(scores, breadCount, Goods.BREADKB, Goods.BREADQB);
        Main.giveBonus(scores, chickenCount, Goods.CHICKENKB, Goods.CHICKENQB);

        return scores;
    }

    private static final class GameInputLoader {
        private final String mInputPath;

        private GameInputLoader(final String path) {
            mInputPath = path;
        }

        public GameInput load() {
            List<Integer> assetsIds = new ArrayList<>();
            List<String> playerOrder = new ArrayList<>();

            try {
                BufferedReader inStream = new BufferedReader(new FileReader(mInputPath));
                String assetIdsLine = inStream.readLine().replaceAll("[\\[\\] ']", "");
                String playerOrderLine = inStream.readLine().replaceAll("[\\[\\] ']", "");

                for (String strAssetId : assetIdsLine.split(",")) {
                    assetsIds.add(Integer.parseInt(strAssetId));
                }

                for (String strPlayer : playerOrderLine.split(",")) {
                    playerOrder.add(strPlayer);
                }
                inStream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GameInput(assetsIds, playerOrder);
        }
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0]);
        GameInput gameInput = gameInputLoader.load();
        List<Integer> assetsList = gameInput.getAssetIds();
        List<String> playerList = gameInput.getPlayerNames();
        int numberOfPlayers = playerList.size();
        int totalRounds = 2 * numberOfPlayers;

        // Creare lista de carti
        List<Goods> cardList = new ArrayList<Goods>();
        for (int asset : assetsList) {
            cardList.add(new Goods(asset));
        }

        // Alocare jucatori
        BasicPlayer[] players = new BasicPlayer[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            List<Goods> inHandCards = new ArrayList<Goods>();

            // Creare lista de carti initiale pentru jucatori
            for (int j = 0; j < MAXCARDS; j++) {
                inHandCards.add(cardList.remove(0));
            }

            // Creare jucatori
            String strategy = playerList.get(i);
            switch (strategy) {
                case "basic":
                    players[i] = new BasicPlayer(inHandCards);
                    break;

                case "greedy":
                    players[i] = new GreedyPlayer(inHandCards);
                    break;

                case "bribed":
                    players[i] =  new BribePlayer(inHandCards);
                    break;

                case "wizard":
                    players[i] = new WizardPlayer(inHandCards);

                default: break;
                }
            }

        for (int i = 0; i < totalRounds; i++) {
            // Determinare serif
            int sheriffId = i % numberOfPlayers;

            // Etapele I si II
            for (int j = 0; j < numberOfPlayers; j++) {
                // Daca e serif nu creeaza sacul
                if (j == sheriffId) {
                    continue;
                }

                players[j].createSack();
            }

            // Etapa a III-a
            for (int j = 0; j < numberOfPlayers; j++) {
                // Seriful nu se controleaza pe el
                if (j == sheriffId) {
                    continue;
                }

                // Bribe controleaza doar stanga-dreapta
                if (playerList.get(sheriffId).equals("bribed")) {
                    if (numberOfPlayers == 2) {
                        players[sheriffId].control(players[j]);
                    } else {
                        int toControl1 = sheriffId - 1;
                        int toControl2 = sheriffId + 1;

                        if (toControl1 == -1) {
                            toControl1 = numberOfPlayers - 1;
                        }

                        if (toControl2 == numberOfPlayers) {
                            toControl2 = 0;
                        }

                        players[sheriffId].control(players[toControl1]);
                        players[sheriffId].control(players[toControl2]);
                        break;
                    }
                } else {
                    players[sheriffId].control(players[j]);
                }
            }

            // Etapa a IV-a
            for (int j = 0; j < numberOfPlayers; j++) {
                // Comerciantii pun bunurile pe taraba
                if (j != sheriffId) {
                    players[j].endRound();

                    // Recompletarea bunurilor din mana
                    List<Goods> inHandGoods = players[j].getInHandGoods();

                    while (inHandGoods.size() < MAXCARDS) {
                        inHandGoods.add(cardList.remove(0));
                    }
                }
            }
        }

        // Calculare scor final
        int[] scores = Main.calculateScores(players);

        // Printare scor final
        int maxScore = 0;
        do {
            maxScore = Main.printScores(scores, playerList);
        } while (maxScore != 0);
    }
}
