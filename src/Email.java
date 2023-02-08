import java.util.Properties;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    private static final String API_KEY = "232bdb4cf0d44bc2b01fd8d6b13c33f1";
    private static final String API_ENDPOINT = "https://newsapi.org/v2/top-headlines?country=in&apiKey=" + API_KEY;

    public static void main(String[] args) throws IOException, JSONException {
        // Specify the email provider and setup the email properties
        final String username = "prajwalkuchewar3@gmail.com";
        final String password = "luxerdtovlmztwoy";
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Your Email ID :");
        final String mail = sc.next("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        sc.close();
        String response = getNews();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a new session and authenticate with the email provider
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail));
            message.setSubject("Testing  Subject");
            message.setText(response);

            // Send the email message
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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
