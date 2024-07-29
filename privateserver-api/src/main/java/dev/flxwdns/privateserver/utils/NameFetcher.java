package dev.flxwdns.privateserver.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.regex.Pattern;

public final class NameFetcher {
    private static final String NAME_URL = "https://sessionserver.mojang.com"
                                           + "/session/minecraft/profile/";
    private static final Pattern NAME_PATTERN = Pattern.compile(",\\s*\"name\"\\s*:\\s*\"(.*?)\"");

    public static String name(UUID uniqueId) {
        String output = callURL(NAME_URL + uniqueId.toString().replace("-", ""));
        var m = NAME_PATTERN.matcher(output);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private static String callURL(String urlStr) {
        StringBuilder sb = new StringBuilder();
        URLConnection conn;
        BufferedReader br = null;
        InputStreamReader in = null;
        try {
            conn = new URL(urlStr).openConnection();
            if (conn != null) {
                conn.setReadTimeout(60 * 1000);
            }
            if (conn != null && conn.getInputStream() != null) {
                in = new InputStreamReader(conn.getInputStream(), "UTF-8");
                br = new BufferedReader(in);
                String line = br.readLine();
                while (line != null) {
                    sb.append(line).append("\n");
                    line = br.readLine();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable ignored) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable ignored) {
                }
            }
        }
        return sb.toString();
    }
}