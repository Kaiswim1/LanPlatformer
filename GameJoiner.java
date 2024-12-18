
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

public class GameJoiner {

    // URL of the Node.js server - dynamically assigned based on local IP
    private static String SERVER_URL;

    // Method to get the local IP address
    private static String getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // For macOS, it might be en1, en0, etc., for Windows wlan0
                if (networkInterface.getName().startsWith("en") || networkInterface.getName().equals("wlan0")) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        // Ignore loopback address and non-IPv4 addresses
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof java.net.Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    // Method to get a specific variable by key (GET request)
    // Method to get a specific variable by key (GET request)
    // Method to get a specific variable by key (GET request)
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

    private static String getFirstThreeOctets(String ipAddress) {
        // Regex pattern to match the first three octets of an IP address
        String regex = "^((\\d{1,3})\\.){2}(\\d{1,3})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipAddress);

        if (matcher.find()) {
            return matcher.group()+"."; // Return the matched part
        }

        // Return null or an empty string if no match is found
        return "";
    }

    public static int connect(String host) throws IOException {
        System.out.println(getFirstThreeOctets(getLocalIPAddress()));
        SERVER_URL = "http://" + getFirstThreeOctets(getLocalIPAddress()) + host + ":3000/"; // Using the local IP address for server communication
        int pNum = getData("manyPlayers")+1;
        sendData("manyPlayers", pNum);
        return pNum;

    }
}
