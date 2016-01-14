package com.mltrading.models.stock;

import scala.tools.nsc.backend.icode.Opcodes;

/**
 * Created by gmo on 03/12/2015.
 */
public class Consensus {
    private Notation lastDay;
    private Notation lastWeek;
    private Notation lastMonth;
    private Notation lastTwoMonth;
    private Notation lastThreeMonth;
    private Notation lastYearMonth;
    private Notation staticNotation;


    public Notation getNotation(int period) {
        switch (period) {
            case 1: return lastDay;
            case 2: return lastWeek;
            case 3: return lastMonth;
            case 4: return lastTwoMonth;
            case 5: return lastThreeMonth;
            case 6: return lastYearMonth;
            default: return staticNotation;
        }
    }

    /**
     * give approximation period for consensus
     * @param indice
     * @return
     */
    public int getIndice(int indice) {

        if (indice ==1) return 1;
        if(indice > 1 && indice <=7) return 2;
        if(indice > 1 && indice <=7) return 2;
        if(indice > 7 && indice <=31) return 3;
        if(indice > 31 && indice <=62) return 4;
        if(indice > 62 && indice <=93) return 5;
        if(indice > 93 && indice <=300) return 6;

        return 0; //max
    }

    @Override
    public String toString() {
        return "Consensus{" +
            "lastDay=" + lastDay +
            ", lastWeek=" + lastWeek +
            ", lastMonth=" + lastMonth +
            ", lastTwoMonth=" + lastTwoMonth +
            ", lastThreeMonth=" + lastThreeMonth +
            ", lastYearMonth=" + lastYearMonth +
            '}';
    }

    public Consensus() {
        lastDay = new Notation();
        lastWeek = new Notation();
        lastMonth = new Notation();
        lastTwoMonth = new Notation();
        lastThreeMonth = new Notation();
        lastYearMonth = new Notation();
        staticNotation = new Notation();
    }

    static int BUY_COEF=5;
    static int REINFORCE_COEF=4;
    static int KEEP_COEF=3;
    static int RELIEVE_COEF=2;
    static int SELL_COEF=1;

    public class Notation{

        protected int buy;
        protected int reinforce;
        protected int keep;
        protected int relieve;
        protected int sell;

        public int getBuy() {
            return buy;
        }

        public void setBuy(int buy) {
            this.buy = buy;
        }

        public int getReinforce() {
            return reinforce;
        }

        public void setReinforce(int reinforce) {
            this.reinforce = reinforce;
        }

        public int getKeep() {
            return keep;
        }

        public void setKeep(int keep) {
            this.keep = keep;
        }

        public int getRelieve() {
            return relieve;
        }

        public void setRelieve(int relieve) {
            this.relieve = relieve;
        }

        public int getSell() {
            return sell;
        }

        public void setSell(int sell) {
            this.sell = sell;
        }

        public Notation() {
            buy = reinforce = keep = relieve = sell = 0;
        }

        @Override
        public String toString() {
            return "Notation{" +
                "buy=" + buy +
                ", reinforce=" + reinforce +
                ", keep=" + keep +
                ", relieve=" + relieve +
                ", sell=" + sell +
                '}';
        }

        public Double getAvg() {
            try {
                if (buy+reinforce+keep+relieve+sell == 0) return new Double(0);
                return new Double(buy*BUY_COEF+reinforce*REINFORCE_COEF+keep*KEEP_COEF+relieve*RELIEVE_COEF+sell*SELL_COEF)
                    /new Double(buy+reinforce+keep+relieve+sell);
            } catch (Exception e) {
                return new Double(0);
            }

        }
    }
}
