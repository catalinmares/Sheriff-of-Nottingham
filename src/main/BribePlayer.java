package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mares Catalin-Constantin, 322CD
 *
 */
public class BribePlayer extends BasicPlayer {

    public BribePlayer(final List<Goods> inHandGoods) {
        super(inHandGoods);
    }

    // Crearea sacului pentru jucatorul Bribe
    /* @see #createSack()*/
    public void createSack() {
        int illegalGoods = 0;
        List<Goods> illegalMerchandise = new ArrayList<Goods>();

        // Extrag din mana bunurile ilegale si le contorizez numarul
        for (Goods asset : getInHandGoods()) {
            if (asset.getType().equals("Illegal")) {
                illegalGoods++;
                illegalMerchandise.add(asset);
            }
        }

        // Daca nu am carti ilegale, aplic metoda de baza
        if (illegalGoods == 0) {
            super.createLegalSack();
            return;
        }

        // Sortez bunurile ilegale dupa profit
        GoodsComparator goodsComparator = new GoodsComparator();
        Collections.sort(illegalMerchandise, goodsComparator);

        int sackSize = illegalMerchandise.size();
        int bribe = 0;

        // Daca adaug mai mult de 2 bunuri ilegale dau mita 10
        if (sackSize > 2) {
            bribe = HIGHBRIBE;
        } else {
            // Altfel dau mita 5
            bribe = LOWBRIBE;
        }

        // Daca nu imi ajung banii pentru a da mita, aplic strategia de baza
        if (getMoney() < bribe) {
            super.createSack();
        } else {
            // Altfel, setez mita si declar mere
            setBribe(bribe);
            setDeclaration("Apple");

            // 6 bunuri ilegale in mana => il elimin pe cel mai neprofitabil
            if (sackSize > MAXSACKSIZE) {
                illegalMerchandise.remove(0);
            }

            List<Goods> sack = getSack();
            List<Goods> cardsInHand = getInHandGoods();

            // Adaug bunurile ilegale in sac
            while (illegalMerchandise.size() != 0) {
                Goods asset = illegalMerchandise.remove(0);
                sack.add(asset);
                cardsInHand.remove(asset);
            }
        }
    }
}
