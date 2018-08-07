public class LastResponse extends Response {
    private Player player;
    private Game[] games;
    private final String header = " играл последний раз ";
    private final String middler = " катки с конца:\n";
    private boolean error = false;

    LastResponse(){
        error = true;
    }

    LastResponse (Player player, Game[] games) {
        this.games = games;
        this.player = player;
    }

    @Override
    Player getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {

        if (!error) {
            int gamesCount = games.length;
            final StringBuilder sb = new StringBuilder(player.getName());
            sb.append(" ")
                    .append(header)
                    .append(games[0].getStartTime())
                    .append("\n")
                    .append(gamesCount)
                    .append(middler);
            for (int i = 0; i < gamesCount; i++) {
                Game current = games[i];
                sb.append(i+1)
                        .append(". ")
                        .append(current.getHero())
                        .append(", ")
                        .append(current.getSide())
                        .append(", *")
                        .append(current.getResult())
                        .append("*, ")
                        .append(current.getLobby())
                        .append(", ")
                        .append(current.getDuration())
                        .append("\n");
            }

            return sb.toString();
        } else return "ERROR! TOO MUCH GAMES TO PARSE";
    }
}
