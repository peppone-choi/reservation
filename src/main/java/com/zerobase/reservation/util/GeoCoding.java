package com.zerobase.reservation.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class GeoCoding {
    /**
     * 지오코딩을 통한 주소 -> 좌표 변환 메소드
     * vworld Open API를 사용.
     * https://www.vworld.kr/dev/v4dv_geocoderguide2_s001.do
     * @param addr 좌표를 변환할 주소 (도로명 주소 사용)
     * @return 반환된 좌표값 : double 형태의 배열
     */
    public double[] geoPoint(String addr) {
        String apikey = "79EA3263-C9B7-341A-BEE5-00E50ABC1EA7";
        String searchType = "road";
        String searchAddr = addr;
        String epsg = "epsg:4326";

        StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/address");
        sb.append("?service=address");
        sb.append("&request=getCoord");
        sb.append("&format=json");
        sb.append("&crs=" + epsg);
        sb.append("&key=" + apikey);
        sb.append("&type=" + searchType);
        sb.append("&address=" + URLEncoder.encode(addr, StandardCharsets.UTF_8));

        double[] array = new double[2];

        try {
            URL url = new URL(sb.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject result = (JSONObject) response.get("result");
            JSONObject point = (JSONObject) result.get("point");

            array[0] = Double.parseDouble(point.get("x").toString());
            array[1] = Double.parseDouble(point.get("y").toString());


        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }

        return array;
    }
}
