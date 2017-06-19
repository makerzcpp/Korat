package com.korat.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台service
 *
 * @author solar
 * @date 2017/6/19
 */
@Service
public class IndexService {

    @Value("${KORAT_MANAGE_URL}")
    private String KORAT_MANAGE_URL;


    private final static ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private ApiService apiService;

    public String queryIndexAd1(String indexADURL) {
        try {
            String url = KORAT_MANAGE_URL + indexADURL;

            String jsonData = apiService.doGet(url);
            if (StringUtils.isBlank(jsonData)) {
                return null;
            }

            //    解析json数据
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");

            BufferedImage image = null;
            BufferedImage imageB = null;

            List<Map<String, Object>> result = new ArrayList<>();
            for (JsonNode row : rows) {
                Map<String, Object> map = new LinkedHashMap<>();

                if (row.get("pic") != null) {
                    image = ImageIO.read(urlToFile(row.get("pic").asText()));
                    map.put("height", image.getHeight());
                    map.put("width", image.getWidth());
                }
                if (row.get("picB") != null) {
                    imageB = ImageIO.read(urlToFile(row.get("picB").asText()));
                    map.put("widthB", imageB.getWidth());
                    map.put("heightB", imageB.getHeight());
                }
                map.put("srcB", row.get("pic").asText());
                map.put("alt", row.get("title").asText());
                map.put("src", row.get("pic").asText());
                map.put("href", row.get("url").asText());
                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static File urlToFile(String url) {
        String str1 = StringUtils.replace(url, "http://image.korat.com", "E:/image");
        str1 = StringUtils.replace(str1, "/", "\\\\");
        return new File(str1);
    }
}
