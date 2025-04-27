package ES;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ElasticSearchSingletonClient{
    
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchSingletonClient.class);

    //域名或IP
    //public static String HOST = JedisUtils.getRedisBlackDao().getConfig("es_host_new");
    //端口
    //public static Integer PORT = Integer.valueOf(JedisUtils.getRedisBlackDao().getConfigInt("es_port_new"));
    //用户名
   // public static String CNAME = JedisUtils.getRedisBlackDao().getConfig("es_uname_new");
    //密码
    //public static String USER = JedisUtils.getRedisBlackDao().getConfig("es_upwd_new");
    
   //域名或IP
    //public static String ES_HOST = "10.0.65.67";
    public static String ES_HOST = "10.0.65.69";
    //端口
    // public static Integer ES_PORT = 9200;
    public static Integer ES_PORT = 8081;
    //用户名
    public static String ES_CNAME = "";
    //密码
    public static String ES_USER = "";
    
    private volatile static RestHighLevelClient highLevelRestClient;
    
    private ElasticSearchSingletonClient() {
        throw new Error("Do not instantiate this class!");
    }
    
    public static RestHighLevelClient getRestHighLevelClientInstance(){
        if(highLevelRestClient == null){
            logger.info("获取高级别es客户端,HOST:"+ES_HOST+",端口:"+ES_PORT);
            synchronized (ElasticSearchSingletonClient.class) {
                if(highLevelRestClient == null){
                    try{
                        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        if(StringUtils.isNotBlank(ES_CNAME) && StringUtils.isNotBlank(ES_USER)){
                            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(ES_CNAME, ES_USER));
                        }else{
                            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("", ""));
                        }
                        highLevelRestClient = new RestHighLevelClient(RestClient
                                .builder(new HttpHost(ES_HOST, ES_PORT))
                                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                                    @Override
                                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                                    }
                                }).setRequestConfigCallback(requestConfigBuilder -> {
                                    requestConfigBuilder.setConnectTimeout(1000);
                                    requestConfigBuilder.setSocketTimeout(120000);
                                    requestConfigBuilder.setConnectionRequestTimeout(1000);
                                    return requestConfigBuilder;
                                }));

                        logger.info("获取高级别es客户端成功");

                    }catch(Exception e){
                        logger.info("获取高级别es客户端失败", e);
                    }
                }
            }
        }
        return highLevelRestClient;
    }

    public static void main(String[] args) throws Exception {
        try{
            getRestHighLevelClientInstance();
        }catch(Exception e){
            logger.info("异常：", e);
        }
    }

}
