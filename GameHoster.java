import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameHoster {

    // URL of the Node.js server - dynamically assigned based on local IP
    private static String SERVER_URL;

    // Method to get the local IP address
    public static String getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // Check if the IP is not the loopback address (127.0.0.1) and is IPv4
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof java.net.Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to send a specific variable (POST request)
    public static void sendData(String key, int value) throws IOException {
        URL url = new URL(SERVER_URL + "data");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // JSON data to send
        String jsonInputString = String.format("{\"key\":\"%s\",\"value\":%d}", key, value);


        // Send the data
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Check response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Value updated successfully.");
        } else {
            System.out.println("Error: " + connection.getResponseMessage());
        }
    }
    
    public static int getData(String key) throws IOException {
        // Fix the URL to match the server route (/data/:key)
        URL url = new URL(SERVER_URL + "data/" + key);  // Use "data/" before the key
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int data = 0;
        // Get the response
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                String r = responseLine.split(":")[1];
                data = Integer.parseInt(r.substring(0, r.length()-1));
                System.out.println("Response from server: " + data);
            }
        }
        return data;
    }


    public static String getLastOctet(String ipAddress) {
        // Regex pattern to match the last octet of an IP address
        String regex = "\\d{1,3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipAddress);

        if (matcher.find()) {
            return matcher.group(); // Return the matched part
        }

        // Return null or an empty string if no match is found
        return "";
    }



    // Method to connect to the server and demonstrate sending/retrieving data
    public static int connect() throws IOException {
        String localIP = getLocalIPAddress();
        SERVER_URL = "http://" + localIP + ":3000/";
        sendData("manyPlayers", 1); //First player to connect
        return 1;
    }
}
