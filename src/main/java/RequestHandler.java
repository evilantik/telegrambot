import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

class RequestHandler {

    private String steamApiKey = "?key=" + getSteamAipKey() + "&";
    private String steamMatchUrl = "http://api.steampowered.com/IDOTA2Match_570/";
    private String steamUserUrl = "http://api.steampowered.com/ISteamUser/";
    private String methodPlayerSum = "GetPlayerSummaries/v0002/";
    private String methodHistory = "GetMatchHistory/V001/";
    private String methodMatch = "GetMatchDetails/v1/";

    private CheckResponse checkProcess(Player player) throws IOException {
        // формируем запрос
        URL reqHistory = new URL(steamUserUrl + methodPlayerSum + steamApiKey +
                "steamids=" + player.getSteamId());

        // открываем соединение
        HttpURLConnection reqConnection = (HttpURLConnection) reqHistory.openConnection();

        // поток ответа на запрос
        InputStream is = reqConnection.getInputStream();

        // формируем строку JSON
        String toParseUserSum = getJSONString(is);

        // берем инфу
        int personalState = JsonPath.read(toParseUserSum, "$.response.players[0].personastate");


        // наверное так делать нехорошо, но не знаю как ещё проверить отсуствите \ наличие ключа
        String gameName;
        try {
            gameName = JsonPath.read(toParseUserSum, "$.response.players[0].gameextrainfo");
        } catch (PathNotFoundException e) {
            gameName = "не в игре";
        }
        return new CheckResponse(player, personalState, gameName);
    }

    private Response lastProcess(Player player, int n) throws IOException {
        if (n > 20) return new LastResponse();

        long[] matchIds = new long[n];
        Game[] games = new Game[n];


        // формируем запрос
        URL reqHistory = new URL(steamMatchUrl + methodHistory + steamApiKey +
                "account_id=" + player.getSteamId() + "&matches_requested=" + n);

        // открываем соединение
        HttpURLConnection reqConnection = (HttpURLConnection) reqHistory.openConnection();

        // поток ответа на запрос
        InputStream is = reqConnection.getInputStream();

        // формируем строку JSON
        String toParseHistory = getJSONString(is);

        // берем немного инфы для ответа и новых запросов

        for (int i = 0; i < n; i++) {
            matchIds[i] = JsonPath.read(toParseHistory, "$.result.matches[" + i + "].match_id");
        }

        for (int k = 0; k < n; k++) {
            // запрос к матчу, коннект, поток в строку json
            URL reqMatch = new URL(steamMatchUrl + methodMatch + steamApiKey + "match_id=" + matchIds[k]);
            HttpURLConnection reqMatchConnection = (HttpURLConnection) reqMatch.openConnection();
            InputStream inputStream = reqMatchConnection.getInputStream();
            String toParseMatch = getJSONString(inputStream);

            // парсим json
            int lobby = JsonPath.read(toParseMatch, "$.result.lobby_type");
            int time = JsonPath.read(toParseMatch, "$.result.start_time");

            // jsonpath тут работает с выражением ? поэтому возвращает jsonarray, для этого приходится создавать список
            List<Integer> listSlot = JsonPath.parse(toParseMatch).read("$.result.players[?(@.account_id == '" + player.getDotaId() + "')].player_slot");
            int slot = getSlot(listSlot);
            int hero = JsonPath.read(toParseMatch, "$.result.players[" + slot + "].hero_id");
            boolean result = JsonPath.read(toParseMatch, "$.result.radiant_win");
            int duration = JsonPath.read(toParseMatch, "$.result.duration");

            games[k] = new Game(time, hero, result, lobby, duration, slot);

        }
        return new LastResponse(player, games);
    }

    private String getJSONString(InputStream is) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    String counterProcess(String data) throws IOException {
        Hero hero = Hero.getHeroByShortName(data);

        Document doc;
        StringBuilder s = new StringBuilder();

        doc = Jsoup.connect("https://www.dotabuff.com/heroes/" + hero.getLink() + "/counters/").userAgent("Mozilla").get();
        Elements counter = doc.body().getElementsByClass("counter-outline").select("td");
        for (Element e :
                counter) {
            s.append(e.text());
            s.append("!");
        }
        s.deleteCharAt(0);
        s.deleteCharAt(s.length() - 1);
        String toSplit = s.toString().replace("!!", "!");
        String[] strings = toSplit.split("!");

        StringBuilder result = new StringBuilder();
        int k = 1;
        for (int i = 0; i < 15; i += 3) {
            result.append("*").append(k).append(". ").append(strings[i]).append("*").append(" (преимущество: ").append(strings[i + 1]).append(", винрейт: ").append(strings[i + 2]).append(")\n");
            k++;
        }

        String name = hero.getName()+" контрят:\n";

        return name + result.toString();


    }

    Response process(String data) throws IOException {
        Response result;

        String name = data.substring(3);
        Player player = Player.getPlayerByName(name);
        char command = data.charAt(1);

        switch (command) {
            case 'l':
                result = lastProcess(player, 2);
                break;
            case 'c':
                result = checkProcess(player);
                break;
            default:
                return null;
        }

        return result;
    }

    //TODO: тут некрасиво, переделать
    Response process(int n, Player player) throws IOException {
        return lastProcess(player, n);
    }

    Response process(Player player) throws IOException {
        return checkProcess(player);
    }

    private int getSlot(List<Integer> list) {
        int result = list.get(0);
        if (result < 5) {
            return result;
        } else {
            return result - 123;
        }

    }

    private String getSteamAipKey() {
        return System.getenv("steamApiKey");
    }
}