package com.mltrading.models.stock;

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

    public Notation getNotation(int period) {
        switch (period) {
            case 1: return lastDay;
            case 2: return lastWeek;
            case 3: return lastMonth;
            case 4: return lastTwoMonth;
            case 5: return lastThreeMonth;
            case 6: return lastYearMonth;
            default: return null;
        }
    }

    public Consensus() {
        lastDay = new Notation();
        lastWeek = new Notation();
        lastMonth = new Notation();
        lastTwoMonth = new Notation();
        lastThreeMonth = new Notation();
        lastYearMonth = new Notation();
    }

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


    }
}
