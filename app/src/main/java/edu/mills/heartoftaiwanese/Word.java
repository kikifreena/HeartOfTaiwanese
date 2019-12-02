package edu.mills.heartoftaiwanese;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

class Word {
    private static final String URL_TO_CRAWL = "http://210.240.194.97/q/THq.asp";
    private String chinese;
    private String taiwanese = "";
    private String output = "";
    private static final String INVALID_MESSAGE = "Not found";

    Word(String chinese) {
        this.chinese = chinese;
    }

    Boolean run() {
        try {
            this.crawlSite();
            if (Objects.equals(taiwanese, INVALID_MESSAGE)) return false;
            else return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    String getChinese() {
        return chinese;
    }

    String getTaiwanese() {
        return taiwanese;
    }

//    @Override
//    public String toString() {
//        return taiwanese;
//    }

    private String parse(String data){
        try {
            int loc = data.indexOf("台語羅馬字");
            int loc2 = 1 + data.indexOf(">", data.indexOf("<span", loc));
            int loc3 = data.indexOf("</span>", loc2);
            return convertToString(data.substring(loc2, loc3));
        } catch (StringIndexOutOfBoundsException e){
            return INVALID_MESSAGE;
        }
    }

    private String convertToString(String hex){
        String[] split = hex.split("&#|;");
        StringBuilder sb = new StringBuilder();
        System.out.println(hex);
        for (int i = 0; i < split.length; i++) {
            String curr = split[i];
            if (curr.matches("\\d+")){
                sb.append((char) Integer.parseInt(split[i]));
            }
            else {
                sb.append(curr);
            }
        }
        return sb.toString();
    }

    private void crawlSite() throws IOException {
        URL url = new URL(URL_TO_CRAWL + "?w=" + chinese);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        int response = connection.getResponseCode();
        if (response == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuffer resp = new StringBuffer();

            while ((input = br.readLine()) != null) {
                resp.append(input);
            }
            br.close();
            output = resp.toString();
            taiwanese = this.parse(output);
        }
    }
}
