package main;

/**
 * @author Mares Catalin-Constantin, 322CD
 *
 */
public class Goods {
    private int id;
    private String name;
    private String type;
    private int penalty;
    static final int LEGALPENALTY = 2;
    static final int ILLEGALPENALTY = 4;
    static final int APPLEID = 0;
    static final int APPLEPROFIT = 2;
    static final int CHEESEID = 1;
    static final int CHEESEPROFIT = 3;
    static final int BREADID = 2;
    static final int BREADPROFIT = 4;
    static final int CHICKENID = 3;
    static final int CHICKENPROFIT = 4;
    static final int SILKID = 10;
    static final int SILKPROFIT = 9;
    static final int PEPPERID = 11;
    static final int PEPPERPROFIT = 8;
    static final int BARRELID = 12;
    static final int BARRELPROFIT = 7;
    static final int APPLEKB = 20;
    static final int APPLEQB = 10;
    static final int CHEESEKB = 15;
    static final int CHEESEQB = 10;
    static final int BREADKB = 15;
    static final int BREADQB = 10;
    static final int CHICKENKB = 10;
    static final int CHICKENQB = 5;

    public Goods(final int id) {
        this.id = id;

        switch (this.id) {
            case Goods.APPLEID:
                this.name = "Apple";
                this.type = "Legal";
                this.penalty = Goods.LEGALPENALTY;
                break;

            case Goods.CHEESEID:
                this.name = "Cheese";
                this.type = "Legal";
                this.penalty = Goods.LEGALPENALTY;
                break;

            case Goods.BREADID:
                this.name = "Bread";
                this.type = "Legal";
                this.penalty = Goods.LEGALPENALTY;
                break;

            case Goods.CHICKENID:
                this.name = "Chicken";
                this.type = "Legal";
                this.penalty = Goods.LEGALPENALTY;
                break;

            case Goods.SILKID:
                this.name = "Silk";
                this.type = "Illegal";
                this.penalty = Goods.ILLEGALPENALTY;
                break;

            case Goods.PEPPERID:
                this.name = "Pepper";
                this.type = "Illegal";
                this.penalty = Goods.ILLEGALPENALTY;
                break;

            case Goods.BARRELID:
                this.name = "Barrel";
                this.type = "Illegal";
                this.penalty = Goods.ILLEGALPENALTY;
                break;

            default: break;
        }
    }

    public final int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final String getType() {
        return type;
    }

    public static int getProfit(final int inputId) {
        switch (inputId) {
            case Goods.APPLEID: return Goods.APPLEPROFIT;
            case Goods.CHEESEID: return Goods.CHEESEPROFIT;
            case Goods.BREADID: return Goods.BREADPROFIT;
            case Goods.CHICKENID: return Goods.CHICKENPROFIT;
            case Goods.SILKID: return Goods.SILKPROFIT;
            case Goods.PEPPERID: return Goods.PEPPERPROFIT;
            case Goods.BARRELID: return Goods.BARRELPROFIT;
            default: return 0;
        }
    }

    public final int getPenalty() {
        return penalty;
    }
}
