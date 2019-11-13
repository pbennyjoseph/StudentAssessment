import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class webClient {

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) {

//        webClient obj = new webClient();

//        try {
//            System.out.println(webClient.hasInternet());
//        } finally {
//            obj.close();
//        }
    }

    void close() throws IOException {
        httpClient.close();
    }

//    public String sendGet(String urlWithParams) {
//
//        HttpGet request = new HttpGet(urlWithParams);
//
//        // add request headers
////        request.addHeader("AUTH-KEY", "2a4b63c");
////        request.addHeader(HttpHeaders.USER_AGENT, "java");
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//
//            // Get HttpResponse Status
//            System.out.println(response.getStatusLine().toString());
//
//            HttpEntity entity = response.getEntity();
////            Header headers = entity.getContentType();
////            System.out.println(headers);
//            System.out.println(EntityUtils.toString(entity));
//            return "0";
//
//        } catch (Exception ignored) {
//
//        }
//        return "";
//    }

    String sendPost(String url, List<NameValuePair> urlParameters) throws Exception {

        HttpPost post = new HttpPost(url);


        // add request parameter, form parameters
//        List<NameValuePair> urlParameters = new ArrayList<>();
//        urlParameters.add(new BasicNameValuePair("username", "abc"));
//        urlParameters.add(new BasicNameValuePair("password", "123"));
//        urlParameters.add(new BasicNameValuePair("custom", "secret"));
        urlParameters.add(new BasicNameValuePair("submit", "isSubmitted"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

//            System.out.println(EntityUtils.toString(response.getEntity()));
            return EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Server is not responding, try again later");
            ex.printStackTrace();
            return "0";
        }
    }

    static boolean hasInternet() {
        try {
            URL url = new URL("https://google.com/");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
