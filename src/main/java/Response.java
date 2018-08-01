import java.util.Calendar;


// TODO: создать качественный объект для информации об одной игре, чтобы удобно хранить Х объектов
class Response {
    private final String header = " играл последний раз ";
    private final String middler = "2 катки с конца:";
    private String timeOfLastGame;
    private String firstGameHero;
    private String secondGameHero;
    private String firstGameSide;
    private String secondGameSide;
    private String firstGameResult;
    private String secondGameResult;
    private String firstGameDuration;
    private String secondGameDuration;
    private String firstGameLobby;
    private String secondGameLobby;
    private String gameName;
    private String personalState;

    Response(int state, String gameName) {
        this.gameName = gameName;
        this.personalState = getState(state);
    }

    Response(int time, int firstGameHero, int secondGameHero, boolean firstGameResult, boolean secondGameResult,
             int firstGameLobby, int secondGameLobby, int firstGameDuration, int secondGameDuration,
             int slotFirst, int slotSecond) {

        timeOfLastGame = getTime(time);

        this.firstGameHero = getHeroFromId(firstGameHero);
        this.secondGameHero = getHeroFromId(secondGameHero);
        firstGameSide = getSide(slotFirst);
        secondGameSide = getSide(slotSecond);
        this.firstGameResult = getResult(slotFirst, firstGameResult);
        this.secondGameResult = getResult(slotSecond, secondGameResult);
        this.firstGameDuration = getDuration(firstGameDuration);
        this.secondGameDuration = getDuration(secondGameDuration);
        this.firstGameLobby = getLobby(firstGameLobby);
        this.secondGameLobby = getLobby(secondGameLobby);

    }

    private String getTime(int time) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis((long) time * 1000);
        int month = date.get(Calendar.MONTH) + 1;
        return date.get(Calendar.DAY_OF_MONTH) + "." + month +
                " " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE);
    }

    String getPersonalState() {
        return personalState;
    }

    String getGameName() {
        return gameName;
    }

    String getHeader() {
        return header;
    }

    String getMiddler() {
        return middler;
    }

    String getTimeOfLastGame() {
        return timeOfLastGame;
    }

    String getFirstGameHero() {
        return firstGameHero;
    }

    String getSecondGameHero() {
        return secondGameHero;
    }

    String getFirstGameSide() {
        return firstGameSide;
    }

    String getSecondGameSide() {
        return secondGameSide;
    }

    String getFirstGameResult() {
        return firstGameResult;
    }

    String getSecondGameResult() {
        return secondGameResult;
    }

    String getFirstGameDuration() {
        return firstGameDuration;
    }

    String getSecondGameDuration() {
        return secondGameDuration;
    }

    String getFirstGameLobby() {
        return firstGameLobby;
    }

    String getSecondGameLobby() {
        return secondGameLobby;
    }

    private String getLobby(int type) {
        switch (type) {
            case 0:
                return "Пабчик";
            case 1:
                return "Тренировка";
            case 4:
                return "С ботами";
            case 5:
                return "Командная";
            case 6:
                return "Соляново";
            case 7:
                return "RMM";
            case 8:
                return "1x1 mid";
            default:
                return "unknown lobby type";
        }
    }

    private String getDuration(int time) {
        int m = time / 60;
        int s = time % 60;

        return m + ":" + s;
    }

    private String getResult(int slot, boolean result) {
        if (slot < 5 & result || slot >= 5 & !result) {
            return "Win";
        } else return "Lose";
    }

    private String getSide(int slot) {
        if (slot < 5) {
            return "Radiant";
        } else return "Dire";
    }

    private String getState(int i) {
        switch (i) {
            case 0:
                return "Offline";
            case 1:
                return "Online";
            case 2:
                return "Busy";
            case 3:
                return "Away";
            case 4:
                return "Snooze";
            case 5:
                return "Looking to trade";
            case 6:
                return "Looking to play";
                default: return " ";
        }
    }

    private String getHeroFromId(int id) {
        switch (id) {
            case 1:
                return "Antimage";
            case 2:
                return "Axe";
            case 3:
                return "Bane";
            case 4:
                return "Bloodseeker";
            case 5:
                return "Crystal Maiden";
            case 6:
                return "Drow Ranger";
            case 7:
                return "Earthshaker";
            case 8:
                return "Juggernaut";
            case 9:
                return "Mirana";
            case 11:
                return "SF";
            case 10:
                return "Morphling";
            case 12:
                return "PL";
            case 13:
                return "Puck";
            case 14:
                return "Pudge";
            case 15:
                return "Razor";
            case 16:
                return "SK";
            case 17:
                return "Storm Spirit";
            case 18:
                return "Sven";
            case 19:
                return "Tiny";
            case 20:
                return "Vengeful Spirit";
            case 21:
                return "Windrunner";
            case 22:
                return "Zeus";
            case 23:
                return "Kunnka";
            case 25:
                return "Lina";
            case 31:
                return "Lich";
            case 26:
                return "Lion";
            case 27:
                return "Shadow Shaman";
            case 28:
                return "Slardar";
            case 29:
                return "Tidehunter";
            case 30:
                return "Witch Doctor";
            case 32:
                return "Riki";
            case 33:
                return "Enigma";
            case 34:
                return "Tinker";
            case 35:
                return "Sniper";
            case 36:
                return "Necrolyte";
            case 37:
                return "Warlock";
            case 38:
                return "Beastmaster";
            case 39:
                return "QoP";
            case 40:
                return "Venomancer";
            case 41:
                return "Void";
            case 42:
                return "Papich";
            case 43:
                return "DP";
            case 44:
                return "PA";
            case 45:
                return "Pugna";
            case 46:
                return "TA";
            case 47:
                return "Viper";
            case 48:
                return "Luna";
            case 49:
                return "DK";
            case 50:
                return "Dazzle";
            case 51:
                return "Rattletrap";
            case 52:
                return "Leshrac";
            case 53:
                return "Furion";
            case 54:
                return "Lifestealer";
            case 55:
                return "Dark Seer";
            case 56:
                return "Clinkz";
            case 57:
                return "Omniknight";
            case 58:
                return "Enchantress";
            case 59:
                return "Huskar";
            case 60:
                return "NS";
            case 61:
                return "Broodmother";
            case 62:
                return "BH";
            case 63:
                return "Weaver";
            case 64:
                return "Jakiro";
            case 65:
                return "Batrider";
            case 66:
                return "Chen";
            case 67:
                return "Spectre";
            case 69:
                return "Doom";
            case 68:
                return "AA";
            case 70:
                return "Ursa";
            case 71:
                return "SB";
            case 72:
                return "Gyrocopter";
            case 73:
                return "Alchemist";
            case 74:
                return "Invoker";
            case 75:
                return "Silencer";
            case 76:
                return "OD";
            case 77:
                return "Lycan";
            case 78:
                return "Brewmaster";
            case 79:
                return "SD";
            case 80:
                return "Lone Druid";
            case 81:
                return "CK";
            case 82:
                return "Meepo";
            case 83:
                return "Treant";
            case 84:
                return "Ogre Magi";
            case 85:
                return "Undying";
            case 86:
                return "Rubick";
            case 87:
                return "Disruptor";
            case 88:
                return "Nyx";
            case 89:
                return "Naga Siren";
            case 90:
                return "KotL";
            case 91:
                return "Wisp";
            case 92:
                return "Visage";
            case 93:
                return "Slark";
            case 94:
                return "Medusa";
            case 95:
                return "Troll Warlord";
            case 96:
                return "Centaur";
            case 97:
                return "Magnus";
            case 98:
                return "Timbersaw";
            case 99:
                return "Bristleback";
            case 100:
                return "Tusk";
            case 101:
                return "SWM";
            case 102:
                return "Abaddon";
            case 103:
                return "Elder titan";
            case 104:
                return "Legion commander";
            case 106:
                return "Ember Spirit";
            case 107:
                return "Earth Spirit";
            case 109:
                return "Terrorblade";
            case 110:
                return "Phoenix";
            case 111:
                return "Oracle";
            case 105:
                return "Techies";
            case 112:
                return "WW";
            case 113:
                return "Arc Warden";

        }
        return "Unknown hero";
    }
}