import org.apache.commons.io.IOUtils;

import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bartek on 12/11/16.
 */
class JsonParser {

    private static HashMap<String, Integer> deputyByName = new HashMap<>();
    private static List<Integer> IDs = new ArrayList<>();


    private static void parse(){

        try (
                InputStream in =
                        new URL( "https://api-v3.mojepanstwo.pl/dane/poslowie.json?conditions[poslowie.kadencja]=8" ).openStream()
        ){

            JSONObject jsonObject = new JSONObject(IOUtils.toString(in ,"UTF-8"));
            for (Object o : jsonObject.getJSONArray("Dataobject")) {
               JSONObject jsonO = (JSONObject) o;
                int id = jsonO.getInt("id");
                String name = jsonO.getJSONObject("data").getString("ludzie.nazwa");
                IDs.add(jsonO.getInt("id"));
                deputyByName.put(name, id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static List<Integer> getIDs(){
        if(IDs.isEmpty()) parse();
        return IDs;
    }

    static HashMap<String, Integer> getDeputyByName(){
        if(deputyByName.isEmpty()) parse();
        return deputyByName;
    }



}
