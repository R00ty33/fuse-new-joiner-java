package org.galatea.starter.elasticsearch;

import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Slf4j
@Configuration
public class ElasticsearchConfig {
  private String esHost = "127.0.0.1";
  private Integer esPort = 9300;
  private String esClusterName = "test-cluster";

  /**
  * Javadoc Comment.
  */
  @Bean
  public Client client() throws Exception {
    Settings esSettings = Settings.builder()
            .put("cluster.name", esClusterName)
            .build();

    TransportClient client = null;

    try {
      client = new PreBuiltTransportClient(esSettings)
            .addTransportAddress(new TransportAddress(InetAddress.getByName(esHost), esPort));
      CreateIndexRequest createIndexRequest = new CreateIndexRequest("historical_prices");
      createIndexRequest.settings(Settings.builder()
            .put("index.number_of_shards", 1)
            .put("index.number_of_replicas", 0).build());
    } catch (Exception e) {
      client.close();
      log.warn("Error occurred while creating Elastic Search Client: " + e);
    }

    return client;
  }

  /**
  * Javadoc Comment.
  */
  @Bean
  public ElasticsearchOperations elasticsearchTemplate() throws Exception {
    return new ElasticsearchTemplate(client());
  }


}
