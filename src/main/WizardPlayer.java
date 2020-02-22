package main;

import java.util.List;

/**
 * @author Mares Catalin-Constantin, 322CD
 *
 */
public class WizardPlayer extends BasicPlayer {
    private int round;

    public WizardPlayer(final List<Goods> inHandGoods) {
        super(inHandGoods);
        this.round = 0;
    }

    public final void createSack() {
        super.createSack();

        // Se aplica strategia de baza si daca am bun ilegal dau mita
        if (getSack().get(0).getType().equals("Illegal")) {
            setBribe(1);
        }
    }

    @Override
    public final void control(final BasicPlayer player) {
        this.round++;

        // Daca comerciantul a dat mita se returneaza mita si se verifica sacul
        if (player.getBribe() != 0) {
            player.resetBribe();
            super.control(player);
        } else {
            // Altfel, se verifica sacul doar in rundele pare de serif
            if (this.round % 2 == 0) {
                 super.control(player);
            }
        }
    }

}
