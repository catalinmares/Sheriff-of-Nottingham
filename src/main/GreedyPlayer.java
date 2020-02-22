package main;

import java.util.List;

/**
 * @author Mares Catalin-Constantin, 322CD
 *
 */
public class GreedyPlayer extends BasicPlayer {
    private int round;
    public GreedyPlayer(final List<Goods> inHandGoods) {
        super(inHandGoods);
        this.round = 0;
    }

    public final int getRound() {
        return this.round;
    }

    public final void setRound(final int round) {
        this.round = round;
    }

    // Crearea sacului pentru jucatorul Greedy
    public final void createSack() {
        // Aplica strategia de baza
        super.createSack();
        this.round++;

        // In rundele pare, daca are loc in sac, adauga un bun ilegal
        if (this.round % 2 == 0 && getSack().size() < MAXSACKSIZE) {
            int maxProfitIndex = 0;
            int maxProfit = 0;

            // Cauta in mana bunul cu profitul cel mai mare
            for (int i = 0; i < getInHandGoods().size(); i++) {
                Goods good = getInHandGoods().get(i);
                if (Goods.getProfit(good.getId()) > maxProfit) {
                    maxProfit = Goods.getProfit(good.getId());
                    maxProfitIndex = i;
                }
            }

            // Daca bunul gasit e ilegal se adauga in sac si se extrage din mana
            Goods maxProfitItem = getInHandGoods().get(maxProfitIndex);
            if (maxProfitItem.getType().equals("Illegal")) {
                getInHandGoods().remove(maxProfitItem);
                getSack().add(maxProfitItem);
            }
        }
    }

    // Controlul comerciantului pentru Greedy serif
    public final void control(final BasicPlayer player) {
        // Daca comerciantul a dat mita, se ia mita si se inchie verificarea
        if (player.getBribe() != 0) {
            player.pay(this, player.getBribe());
            player.resetBribe();
        } else {
            // Altfel, se verifica sacul comerciantului prin metoda de baza
            super.control(player);
        }
    }
}
