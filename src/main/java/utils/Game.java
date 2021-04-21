package utils;

public class Game {
    private String winningTeam;
    private int id;
    private String record;
    private String winningId;

    public Game(String winningTeam, int id, String record, String winningId) {
        this.id = id;
        this.record = record;
        this.winningTeam = winningTeam;
        this.winningId = winningId;
    }
    public String getTeam() {
        return winningTeam;
    }
    public String getRecord() {
        return record;
    }
    public int getId() {
        return id;
    }
    public String getWinningId() {
        return winningId;
    }
}
