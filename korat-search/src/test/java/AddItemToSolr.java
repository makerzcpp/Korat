import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.search.bean.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * 向solr添加索引
 *
 * @author solar
 * @date 2017/6/29
 */
public class AddItemToSolr {
    private HttpSolrServer httpSolrServer;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Before
    public void setUp() throws Exception {
        // 在url中指定core名称：taotao
        //http://solr.taotao.com/#/taotao  -- 界面地址
        String url = "http://solr.korat.com/korat"; //服务地址
        HttpSolrServer httpSolrServer = new HttpSolrServer(url); //定义solr的server
        httpSolrServer.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrServer.setMaxRetries(1); // 设置重试次数，推荐设置为1
        httpSolrServer.setConnectionTimeout(500); // 建立连接的最长时间

        this.httpSolrServer = httpSolrServer;
    }

    @Test
    public void addIndex() throws IOException, SolrServerException {
        String url = "http://manage.korat.com/rest/item?page={page}&rows=100";
        int page=1;
        int pageSize=0;
        do {
            String newUrl = StringUtils.replace(url, "{page}", "" + page);
            String result = doGet(newUrl);
            JsonNode jsonNode = objectMapper.readTree(result);
            String rows = jsonNode.get("rows").toString();
            List<Item> itemList = objectMapper.readValue(rows, objectMapper.getTypeFactory().constructCollectionType(List.class, Item.class));
            pageSize = itemList.size();
            httpSolrServer.addBeans(itemList);
            httpSolrServer.commit();
            page++;
        }while (pageSize==100);
    }

    private String doGet(String url) throws IOException {
        CloseableHttpClient httpClient= HttpClients.createDefault();
        //创建GET请求
        HttpGet httpGet = new HttpGet(url);
        //设置参数
        //创建响应
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
            //    判断返回状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
}
