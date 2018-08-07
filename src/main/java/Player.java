public enum Player {
    MCHT("Making Cookies Having Teas.", 76561198076555150L, 116289422L),
    ARKAD("ATHLETE!", 76561198218694973L, 258429245L),
    ANTON("фкыруевмило", 76561198090747251L, 130481523L),
    MELKI("Milonov", 76561198038007191L, 77741463L),
    ANYA("ANYA II. BACK TO DOTA", 76561197996852270L, 36586542L),
    VOLSH("Bird is the word", 76561198810259532L, 0),
    ERR("not a player", 0,0);

    private String name;
    private long steamId;
    private long dotaId;

    Player(String name, long steamId, long dotaId) {
        this.name = name;
        this.steamId = steamId;
        this.dotaId = dotaId;
    }

    public String getName() {
        return name;
    }

    public long getSteamId() {
        return steamId;
    }

    public long getDotaId() {
        return dotaId;
    }

    public static Player getPlayerByName (String name) {
        for (Player p :
                Player.values()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return ERR;
    }
}
