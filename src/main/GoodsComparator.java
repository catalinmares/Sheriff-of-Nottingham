package main;

import java.util.Comparator;

/**
 * @author Mares Catalin-Constantin, 322CD
 *
 */
public class GoodsComparator implements Comparator<Goods> {

    // Comparator pentru sortarea bunurilor in functie de profit
    @Override
    public final int compare(final Goods g1, final Goods g2) {
        return Goods.getProfit(g1.getId()) - Goods.getProfit(g2.getId());
    }
}
