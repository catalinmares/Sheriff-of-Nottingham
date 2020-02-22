package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Mares Catalin-Constantin, 322CD
 *
 */
public class BasicPlayer {
    private List<Goods> inHandGoods;
    private List<Goods> onMerchantStandGoods;
    private List<Goods> sack;
    private String declaredGoods;
    private int money;
    private int bribe;
    static final int MAXSACKSIZE = 5;
    static final int LOWBRIBE = 5;
    static final int HIGHBRIBE = 10;

    public BasicPlayer(final List<Goods> inHandGoods) {
        this.inHandGoods = inHandGoods;
        this.money = Main.INITIALMONEY;
        this.onMerchantStandGoods = new ArrayList<Goods>();
        this.sack = new ArrayList<Goods>();
    }

    public final void setDeclaration(final String declaration) {
       this.declaredGoods = declaration;
    }

    public final String getDeclaration() {
        return declaredGoods;
    }

    public final List<Goods> getSack() {
        return sack;
    }

    public final void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    public final int getBribe() {
        return this.bribe;
    }

    public final int getMoney() {
        return money;
    }

    public final List<Goods> getStand() {
        return onMerchantStandGoods;
    }

    public final void pay(final BasicPlayer player, final int toPay) {
        player.money += toPay;
        this.money -= toPay;
    }

    public final void resetBribe() {
        this.bribe = 0;
    }

    public final List<Goods> getInHandGoods() {
        return inHandGoods;
    }

    // Creeaza sacul atunci cand avem bunuri legale
    public final void createLegalSack() {
        Map<Goods, Integer> countFrequency = new HashMap<Goods, Integer>();
        int[] frequency = new int[Main.MAXID];
        int[] profit = new int[Main.MAXID];

        for (Goods asset : inHandGoods) {
            countFrequency.put(asset, countFrequency.getOrDefault(asset, 0) + 1);
        }

        // Determinare frecvente pentru fiecare bun din mana jucatorului
        for (Goods asset : countFrequency.keySet()) {
            frequency[asset.getId()] += countFrequency.get(asset);
        }

        int maxFrequency = Main.maxValue(frequency, Main.NRLEGALGOODS);

        // Determinarea profitului bunurilor cu frecventa maxima
        for (int i = 0; i < Main.NRLEGALGOODS; i++) {
            if (frequency[i] == maxFrequency) {
                profit[i] = Goods.getProfit(i);
            }
        }

        int maxProfit = Main.maxValue(profit, Main.NRLEGALGOODS);
        Goods assetToAdd = null;

        // Determinarea bunului de adaugat in sac
        for (Goods assets : inHandGoods) {
            // Primul bun din mana care are profitul maxim si frecventa maxima
            if (Goods.getProfit(assets.getId()) == maxProfit
            && frequency[assets.getId()] == maxFrequency) {
                assetToAdd = assets;
                break;
            }
        }

        // Stergerea bunului din mana si adaugarea lui in sac
        for (Iterator<Goods> iterator = inHandGoods.iterator(); iterator.hasNext();) {
            Goods asset = iterator.next();
            if (asset.getName().equals(assetToAdd.getName())) {
                iterator.remove();
                sack.add(assetToAdd);
            }
        }

        // Setare declaratie
        this.declaredGoods = assetToAdd.getName();
    }

    // Creeaza sacul cand nu avem niciun bun legal
    public final void createIllegalSack() {
        this.declaredGoods = "Apple";
        int maxProfitIndex = 0;
        int maxProfit = 0;

        // Determinare bun ilegal cu profit maxim din mana
        for (int i = 0; i < inHandGoods.size(); i++) {
            Goods good = inHandGoods.get(i);
            if (Goods.getProfit(good.getId()) > maxProfit) {
                maxProfit = Goods.getProfit(good.getId());
                maxProfitIndex = i;
            }
        }

        // Stergere bun din mana si adaugare in sac
        Goods asset = inHandGoods.remove(maxProfitIndex);
        sack.add(asset);
    }

    // Decide daca se creeaza sac legal sau ilegal
    /* @see #createSack()*/
    public void createSack() {
        int legalGoods = 0;
        for (Goods asset : inHandGoods) {
            if (asset.getType().equals("Legal")) {
                legalGoods++;
            }
        }

        if (legalGoods != 0) {
            createLegalSack();
        } else {
            createIllegalSack();
        }
    }

    // Verifica sacul unui comerciant
    /* @see #control()*/
    public void control(final BasicPlayer player) {
        String declaration = player.getDeclaration();

        // Basic nu ia mita, deci daca jucatorul a dat mita o inapoiaza
        if (player.getBribe() != 0) {
            player.resetBribe();
        }

        int toPay = 0;
        int toBePayed = 0;
        List<Goods> ctrlSack = player.getSack();

        // Verifica fiecare element din sac si confisca bunurile nedeclarate
        for (Iterator<Goods> iterator = ctrlSack.iterator(); iterator.hasNext();) {
            Goods asset = iterator.next();
            if (!(asset.getName().equals(declaration))) {
                toBePayed += asset.getPenalty();
                iterator.remove();
            }
        }

        // Comerciantul ii plateste serifului penalty
        player.pay(this, toBePayed);

        // Comerciantul a fost cinstit. Seriful ii plateste penalty
        if (toBePayed == 0) {
            for (Goods asset : ctrlSack) {
                toPay += asset.getPenalty();
            }

            this.pay(player, toPay);
        }
    }

    // Se adauga bunurile ramase in sac pe taraba
    public final void endRound() {
        while (sack.size() != 0) {
            Goods asset = sack.remove(0);
            onMerchantStandGoods.add(asset);
        }
    }
}
