package worker;

import com.google.common.collect.Sets;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by vladimir on 26.05.18.
 */
public class UpstreamUpdater {

    public UpstreamUpdater() {

    }

    public static void main(String[] args) {
//        Set<String> set = Sets.newHashSet("ffw.com", "w199.com");
//        UpstreamUpdater upstreamUpdater = new UpstreamUpdater(Arrays.asList(set));
//        upstreamUpdater.postDomains(set);
    }





    public static void postDomains(Collection domains) {
        try {
            JSONObject body = new JSONObject();
            body.put("domains", domains);
            HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:8080/put")
                    .header("Content-Type","application/json")
                    .body(body)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }



}
