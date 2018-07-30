import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

class RequestHandler {

    private Response realProcess(long id, long dotaId) throws IOException {

        // формируем запрос
        String steamApiKey = "?key=3CA0BE48B4A477BAEB443C38E66EBD00&";
        String steamUrl = "http://api.steampowered.com/IDOTA2Match_570/";
        String methodHistory = "GetMatchHistory/V001/";
        String methodMatch = "GetMatchDetails/v1/";

        URL reqHistory = new URL(steamUrl + methodHistory + steamApiKey +
                "account_id=" + id + "&matches_requested=2");

        // открываем соединение
        HttpURLConnection reqConnection = (HttpURLConnection) reqHistory.openConnection();

        // поток ответа на запрос
        InputStream is = reqConnection.getInputStream();

        // формируем строку JSON
        String toParseHistory = getJSONString(is);

        // берем немного инфы для ответа и новых запросов
        long matchIdFirst = JsonPath.read(toParseHistory, "$.result.matches[0].match_id");
        long matchIdSecond = JsonPath.read(toParseHistory, "$.result.matches[1].match_id");

        int lobbyFirst = JsonPath.read(toParseHistory, "$.result.matches[0].lobby_type");
        int lobbySecond = JsonPath.read(toParseHistory, "$.result.matches[1].lobby_type");

        int timeFirst = JsonPath.read(toParseHistory, "$.result.matches[0].start_time");
        //    int timeSecond = JsonPath.read(toParseHistory, "$.result.matches[1].start_time");

        List<Integer> listSlotFirst = JsonPath.parse(toParseHistory).read("$.result.matches[0].players[?(@.account_id == '" + dotaId + "')].player_slot");
        List<Integer> listSlotSecond = JsonPath.parse(toParseHistory).read("$.result.matches[1].players[?(@.account_id == '" + dotaId + "')].player_slot");
        int slotFirst = getSlot(listSlotFirst);
        int slotSecond = getSlot(listSlotSecond);

        // запросы к матчам
        URL reqMatch1 = new URL(steamUrl + methodMatch + steamApiKey + "match_id=" + matchIdFirst);
        URL reqMatch2 = new URL(steamUrl + methodMatch + steamApiKey + "match_id=" + matchIdSecond);

        // коннект
        HttpURLConnection reqMatch1Connection = (HttpURLConnection) reqMatch1.openConnection();

        // поток ответа
        InputStream is2 = reqMatch1Connection.getInputStream();

        // строку json
        String toParseMatch1 = getJSONString(is2);

        // коннект
        HttpURLConnection reqMatch1Connection2 = (HttpURLConnection) reqMatch2.openConnection();

        // поток ответа
        InputStream is3 = reqMatch1Connection2.getInputStream();

        // строку json
        String toParseMatch2 = getJSONString(is3);

        // парсим остальную инфу
        int firstGameHero = JsonPath.read(toParseMatch1, "$.result.players[" + slotFirst + "].hero_id");
        int secondGameHero = JsonPath.read(toParseMatch2, "$.result.players[" + slotSecond + "].hero_id");

        boolean firstResult = JsonPath.read(toParseMatch1, "$.result.radiant_win");
        boolean secondResult = JsonPath.read(toParseMatch2, "$.result.radiant_win");

        int firstGameDuration = JsonPath.read(toParseMatch1, "$.result.duration");
        int secondGameDuration = JsonPath.read(toParseMatch2, "$.result.duration");


        return new Response(timeFirst, firstGameHero, secondGameHero, firstResult, secondResult,
                lobbyFirst, lobbySecond, firstGameDuration, secondGameDuration, slotFirst, slotSecond);
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

    Response process(String name) throws IOException {
        Response result;
        switch (name) {
            case "Making Cookies Having Teas.":
                result = realProcess(76561198076555150L, 116289422L);
                break;
            case "ATHLETE!":
                result = realProcess(76561198218694973L, 258429245L);
                break;
            case "фкыруевмило":
                result = realProcess(76561198090747251L, 130481523L);
                break;
            case "Milonov":
                result = realProcess(76561198038007191L, 77741463L);
                break;
            case "ANYA II. BACK TO DOTA":
                result = realProcess(76561197996852270L, 36586542L);
                break;
            case "Bird is the word":
                result = realProcess(76561198810259532L, 0);
                break;
            default:
                return null;
        }
        return result;
    }

    private int getSlot(List<Integer> list) {
        int result = list.get(0);
        if (result < 5) {
            return result;
        } else {
            return result - 123;
        }

    }
}