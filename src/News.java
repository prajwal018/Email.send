import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;

public class News {
    private static final String API_KEY = "232bdb4cf0d44bc2b01fd8d6b13c33f1";
    private static final String API_ENDPOINT = "https://newsapi.org/v2/top-headlines?country=in&apiKey=" + API_KEY;

    public static void main(String[] args) throws IOException, JSONException {
        String response = getNews();
        System.out.println(response);
    }

    private static String getNews() throws IOException, JSONException {
        URL url = new URL(API_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            JSONObject json = new JSONObject(response.toString());
            JSONArray articles = json.getJSONArray("articles");
            StringBuilder newsStringBuilder = new StringBuilder();
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("title");
                String description = article.getString("description");
                newsStringBuilder.append("Title: " + title + "\n" + "Description: " + description + "\n\n");
            }
            String news = newsStringBuilder.toString();
            return news;
        } else {
            throw new IOException("Failed to retrieve news. Response code: " + responseCode);
        }
    }
}
