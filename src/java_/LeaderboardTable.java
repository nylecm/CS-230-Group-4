package java_;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Models the data which is presented by the leaderboard table. Has columns for
 * username, and their number of wins and losses.
 *
 * @author Waleed Ashraf
 */
public class LeaderboardTable {
    private final SimpleStringProperty rName; // Rows of the table
    private final SimpleIntegerProperty rWins;
    private final SimpleIntegerProperty rLosses;

    /**
     * Instantiates a new leaderboard table .
     *
     * @param name
     * @param wins
     * @param losses
     */
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
