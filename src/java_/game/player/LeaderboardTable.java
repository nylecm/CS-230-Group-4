package java_.game.player;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LeaderboardTable {
    private final SimpleStringProperty rName;
    private final SimpleIntegerProperty rWins;
    private final SimpleIntegerProperty rLosses;

    public LeaderboardTable(String name, int wins, int losses) {
        this.rName = new SimpleStringProperty(name);
        this.rWins = new SimpleIntegerProperty(wins);
        this.rLosses = new SimpleIntegerProperty(losses);
    }

    public String getrName() {
        return rName.get();
    }

    public SimpleStringProperty rNameProperty() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName.set(rName);
    }

    public int getrWins() {
        return rWins.get();
    }

    public SimpleIntegerProperty rWinsProperty() {
        return rWins;
    }

    public void setrWins(int rWins) {
        this.rWins.set(rWins);
    }

    public int getrLosses() {
        return rLosses.get();
    }

    public SimpleIntegerProperty rLossesProperty() {
        return rLosses;
    }

    public void setrLosses(int rLosses) {
        this.rLosses.set(rLosses);
    }
}
