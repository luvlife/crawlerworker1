package worker;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class QueueService {

    private BlockingQueue<String> domains = new ArrayBlockingQueue<>(50);

    String nextDomain() {
        return domains.poll();
    }

    QueueService() {
        new Thread(
            () -> {
                while (true) {

                    try {
                        HttpResponse<JsonNode> res = Unirest.get("http://localhost:8080/get/180").asJson();
                        System.out.println(res);
                        for (int i = 0; i < ((JSONArray)res.getBody().getObject().get("domains")).length(); i++)
                        {
                            try {
                                domains.put(((JSONArray)res.getBody().getObject().get("domains")).get(i).toString());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        ).start();
    }



    public BlockingQueue<String> getDomains() {
        return domains;
    }

    public static void main(String[] args) {
        new QueueService();
    }

}
