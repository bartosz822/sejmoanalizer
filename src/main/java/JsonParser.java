import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by bartek on 12/11/16.
 */
class JsonParser {

    private static final Map<String, Integer> deputyByName = new ConcurrentHashMap<>();


    private static void parsemain(String Url) throws IOException, ExecutionException, InterruptedException {

        try (
                InputStream in =
                        new URL(Url).openStream()
        ) {

            JSONObject jsonObject = new JSONObject(IOUtils.toString(in, "UTF-8"));
            putNameAndIdIntoMap(jsonObject, deputyByName);

//            Optional<String> linkToLast = Optional.ofNullable(getLinkToLast(jsonObject));
            if (jsonObject.getJSONObject("Links").has("last")) {
                String linkBody = jsonObject.getJSONObject("Links").getString("last");
                int last = getLastPage(linkBody);
                List<String> links = new ArrayList<>();
                for (int i = last; i > 1; i--) {
                    links.add(linkBody.substring(0, linkBody.length() - 2) + i);
                }

                deputyByName.putAll(links
                        .parallelStream()
                        .flatMap(JsonParser::parsePages)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a1, a2) -> a1)));
            }
        }
    }

//    private static String getLinkToLast(JSONObject jsonObject) {
//        try {
//           return jsonObject.getJSONObject("Links").getString("last");
//        }catch (JSONException e){
//            return null;
//        }
//    }

    private static Stream<Map.Entry<String, Integer>> parsePages(String Url) throws UncheckedIOException {
        HashMap<String, Integer> deputies = new HashMap<>();
        try (InputStream in =
                     new URL(Url).openStream()
        ) {
            JSONObject jsonObject = new JSONObject(IOUtils.toString(in, "UTF-8"));

            putNameAndIdIntoMap(jsonObject, deputies);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return deputies.entrySet().stream();
    }

    private static void putNameAndIdIntoMap(JSONObject jsonObject, Map<String, Integer> deputies){
        for (Object o : jsonObject.getJSONArray("Dataobject")) {
            JSONObject jsonO = (JSONObject) o;
            int id = jsonO.getInt("id");
            String name = jsonO.getJSONObject("data").getString("ludzie.nazwa");
            deputies.put(name, id);
        }
    }

    static List<Integer> getIDs(String Url) throws IOException, ExecutionException, InterruptedException {
        if (deputyByName.isEmpty()) parsemain(Url);
        return new CopyOnWriteArrayList<>(deputyByName.values());
    }

    //throws NumberFormatException if link doesn't end with nuber but it's kind of impossible pasend on the structure of the json string
    private static int getLastPage(String link) {
        int i = link.length() - 1;
        StringBuilder number = new StringBuilder();
        while (i > 0 && link.charAt(i) != '=') {
            number.append(link.charAt(i));
            i--;
        }
        return Integer.parseInt(number.reverse().toString());
    }


    static Map<String, Integer> getDeputyByName(String Url) throws IOException, ExecutionException, InterruptedException {
        if (deputyByName.isEmpty()) parsemain(Url);
        return deputyByName;
    }


}
