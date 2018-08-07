public class CheckResponse extends Response {
    private Player player;
    private String gameName;
    private String personalState;

    CheckResponse(Player player, int personalState, String gameName) {
        this.player = player;
        this.gameName = gameName;
        this.personalState = getState(personalState);
    }

    @Override
    Player getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return "*" + player.getName() + "*: статус - *" + personalState + "*, игра - " + gameName;
    }

    private String getState(int i) {
        switch (i) {
            case 0:
                return "Оффлайн";
            case 1:
                return "Онлайн";
            case 2:
                return "Занят";
            case 3:
                return "АФК";
            case 4:
                return "Глубокий АФК";
            case 5:
                return "ЛФТ";
            case 6:
                return "ЛФП";
            default: return " ";
        }
    }
}
