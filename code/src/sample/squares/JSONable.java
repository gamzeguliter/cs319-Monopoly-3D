package sample.squares;

import org.json.JSONObject;

public interface JSONable {
    JSONObject getJSON();
    void extractPropertiesFromJSON(JSONObject jo);
}
