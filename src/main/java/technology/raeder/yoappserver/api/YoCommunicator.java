package technology.raeder.yoappserver.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import technology.raeder.yoappserver.api.exceptions.NoApiKeyException;

public class YoCommunicator {

    private final YoApp app;

    /**
     * The YoCommunicator offers functionality to communicate with the Yo API.
     *
     * @param app The YoApp requesting stuff from the API.
     */
    public YoCommunicator(YoApp app) {
        this.app = app;
    }

    /**
     * Send a yo to a username
     *
     * @param callback The callback that is called when the response has
     * finished
     * @param username The username that should receive the yo.
     * @param url The url that should be attached to the yo.
     * @throws NoApiKeyException
     */
    public void sendYo(final SuccessCallback callback, final String username, final String url) throws NoApiKeyException {
        if (app.getApiKey() == null) {
            throw new NoApiKeyException();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendGenericApiRequest("https://api.justyo.co/yo/", app.getApiKey(), username, url, callback);
            }
        }).start();
    }

    /**
     * Send a yo to a username
     *
     * @param username The username that should receive the yo.
     * @param callback The callback that is called when the response has
     * finished
     * @throws NoApiKeyException
     */
    public void sendYo(SuccessCallback callback, String username) throws NoApiKeyException {
        this.sendYo(callback, username, null);
    }

    /**
     * Send a yo to all subscribers.
     *
     * @param url The url that should be attached to the yo.
     * @param callback The callback that is called when the response has
     * finished
     * @throws NoApiKeyException
     */
    public void sendYoToAll(final SuccessCallback callback, final String url) throws NoApiKeyException {
        if (app.getApiKey() == null) {
            throw new NoApiKeyException();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendGenericApiRequest("https://api.justyo.co/yoall/", app.getApiKey(), null, url, callback);
            }
        }).start();
    }

    /**
     * Send a yo to all subscribers.
     *
     * @param callback The callback that is called when the response has finished
     * @throws NoApiKeyException
     */
    public void sendYoToAll(final SuccessCallback callback) throws NoApiKeyException {
        this.sendYoToAll(callback, null);
    }

    /**
     * Calls the callback and sets the current amount of subscribers.
     * @param callback The Callback to call.
     * @throws NoApiKeyException 
     */
    public void getSubscriberCount(SuccessSubscriberCallback callback) throws NoApiKeyException {
        try {
            if (app.getApiKey() == null) {
                throw new NoApiKeyException();
            }
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            final HttpGet httpGet = new HttpGet("https://api.justyo.co/subscribers_count/?api_token=" + app.getApiKey());
            final CloseableHttpResponse response = httpclient.execute(httpGet);
            final boolean success = response.getStatusLine().getStatusCode() == 200;
            final InputStream is = response.getEntity().getContent();
            final String json = IOUtils.toString(is, "UTF-8");
            EntityUtils.consume(response.getEntity());
            response.close();
            final JsonElement parsed = new JsonParser().parse(json);
            long numberOfSubs = -1;
            if (parsed.isJsonObject()) {
                final JsonObject obj = parsed.getAsJsonObject();
                if (obj.has("result")) {
                    numberOfSubs = obj.get("result").getAsLong();
                }
            }
            callback.setSubscribers(numberOfSubs);
            callback.setSuccess(success);
            callback.run();
        } catch (IOException ex) {
            Logger.getLogger(YoCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            callback.setSuccess(false);
            callback.run();
        }
    }

    /**
     * Sends an API request and calls the callback.
     * @param apiUrl The API url to call.
     * @param apiKey The API key to send.
     * @param username The username to send (optional).
     * @param url The url to send (optional).
     * @param callback The callback to call.
     */
    private void sendGenericApiRequest(String apiUrl, String apiKey, String username, String url, SuccessCallback callback) {
        try {
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            final HttpPost httpPost = new HttpPost(apiUrl);
            final List<NameValuePair> postData = new ArrayList<NameValuePair>();
            postData.add(new BasicNameValuePair("api_token", apiKey));
            if (username != null) {
                postData.add(new BasicNameValuePair("username", username));
            }
            if (url != null) {
                postData.add(new BasicNameValuePair("link", url));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(postData));
            final CloseableHttpResponse response = httpclient.execute(httpPost);
            final boolean success = response.getStatusLine().getStatusCode() == 200;
            EntityUtils.consume(response.getEntity());
            response.close();
            informCallback(callback, success);
        } catch (IOException ex) {
            Logger.getLogger(YoCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            informCallback(callback, false);
        }
    }

    /**
     * Informs the callback if the call was successful or not and calls it.
     * @param callback
     * @param success 
     */
    private void informCallback(SuccessCallback callback, boolean success) {
        callback.setSuccess(success);
        callback.run();
    }

}
