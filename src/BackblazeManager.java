import java.io.*;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.json.*;

public class BackblazeManager {
    static private String apiUrl = null;
    static private String authorizationToken = null;
    static private String downloadUrl = null;
    static private String bucketname = null;

    private static String[] authorizeb2(String applicationKeyId, String applicationKey) {
        HttpURLConnection connection = null;
        String headerForAuthorizeAccount = "Basic " + Base64.getEncoder().encodeToString((applicationKeyId + ":" + applicationKey).getBytes());
        String[] response = new String[3];
        try {
            URL url = new URL("https://api.backblazeb2.com/b2api/v2/b2_authorize_account");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", headerForAuthorizeAccount);
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String jsonResponse = myInputStreamReader(in);
            JSONObject json = new JSONObject(jsonResponse);
            response = new String[]{json.getString("apiUrl"),json.getString("authorizationToken"),json.getString("downloadUrl"),json.getJSONObject("allowed").getString("bucketName")};
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return response;
    }

    private static String[] b2GetUploadUrl(String apiUrl, String accountAuthorizationToken, String bucketId){
        HttpURLConnection connection = null;
        String postParams = "{\"bucketId\":\"" + bucketId + "\"}";
        byte postData[] = postParams.getBytes(StandardCharsets.UTF_8);
        String[] response = new String[1];
        try {
            URL url = new URL(apiUrl + "/b2api/v2/b2_get_upload_url");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", accountAuthorizationToken);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
            connection.setDoOutput(true);
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.write(postData);
            String jsonResponse = myInputStreamReader(connection.getInputStream());
            JSONObject json = new JSONObject(jsonResponse);
            response = new String[]{json.getString("uploadUrl"),json.getString("authorizationToken")};
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return response;
    }

    private static void b2UploadFile(byte[] fileData, String uploadUrl, String uploadAuthorizationToken, String fileName, String contentType, String sha1) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(uploadUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", uploadAuthorizationToken);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("X-Bz-File-Name", fileName);
            connection.setRequestProperty("X-Bz-Content-Sha1", sha1);
            connection.setDoOutput(true);
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.write(fileData);
            String jsonResponse = myInputStreamReader(connection.getInputStream());
            System.out.println(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    public static void uploadFile(String filepath, String uploadedFileName, String applicationKeyId, String applicationKey, String bucketId) throws IOException, NoSuchAlgorithmException {
        if(authorizationToken == null){
            String[] authResult = authorizeb2(applicationKeyId, applicationKey);
            apiUrl = authResult[0];
            authorizationToken = authResult[1];
            downloadUrl = authResult[2];
            bucketname = authResult[3];
        }
        String[] uploadData = b2GetUploadUrl(apiUrl,authorizationToken,bucketId);
        b2UploadFile(FileUtils.readFileToByteArray(new File(filepath)),uploadData[0],uploadData[1], uploadedFileName, "b2/x-auto", SHA1.sha1Code(filepath));
    }

    public static void downloadFile(String fileName, String downloadedFileName, String applicationKeyId, String applicationKey) throws IOException {
        if(authorizationToken == null){
            String[] authResult = authorizeb2(applicationKeyId, applicationKey);
            apiUrl = authResult[0];
            authorizationToken = authResult[1];
            downloadUrl = authResult[2];
            bucketname = authResult[3];
        }
        byte[] file = b2downloadFile(fileName, authorizationToken, downloadUrl, bucketname);
        File filepath = new File(downloadedFileName);
        try {
            OutputStream os = new FileOutputStream(filepath);
            os.write(file);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] b2downloadFile(String fileName, String accountAuthorizationToken, String downloadUrl, String bucketName) throws IOException {
        HttpURLConnection connection = null;
        byte downloadedData[] = null;
        try {
            URL url = new URL(downloadUrl + "/file/" + bucketName + "/" + fileName);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Authorization", accountAuthorizationToken);
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            downloadedData = myDataInputStreamHandler(connection.getInputStream());
        } catch (ProtocolException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return downloadedData;
    }

    private static String myInputStreamReader(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);
        StringBuilder sb = new StringBuilder();
        int c = reader.read();
        while (c != -1) {
            sb.append((char)c);
            c = reader.read();
        }
        reader.close();
        return sb.toString();
    }

    private static byte[] myDataInputStreamHandler(InputStream in) throws IOException {
        return in.readAllBytes();
    }
}
