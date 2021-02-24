package dev.vabalas.kkkk;


import java.util.List;

public class NewGameRequest {
    private List<String> players;

    public NewGameRequest(List<String> players) {
        this.players = players;
    }

    public NewGameRequest() {
    }

    public List<String> getPlayers() {
        return players;
    }
}
