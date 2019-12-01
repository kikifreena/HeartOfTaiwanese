package edu.mills.heartoftaiwanese;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Word {
    private static final String URL_TO_CRAWL = "http://210.240.194.97/q/THq.asp";
    private String chinese;
    private String taiwanese = "";

    public Word(String chinese) {
        this.chinese = chinese;
    }

    public Boolean run() {
        try {
            this.crawlSite();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getChinese() {
        return chinese;
    }

    public String getTaiwanese() {
        return taiwanese;
    }

    @Override
    public String toString() {
        return taiwanese;
    }

    // TODO
    private String parse(String data){
        return data;
    }

    private void crawlSite() throws IOException {
        URL url = new URL(URL_TO_CRAWL + "?w=" + chinese);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        ;
        int response = connection.getResponseCode();
        if (response == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuffer resp = new StringBuffer();

            while ((input = br.readLine()) != null) {
                resp.append(input);
            }
            br.close();
            taiwanese = this.parse(resp.toString());
            System.out.println(taiwanese);
        }
    }


}
