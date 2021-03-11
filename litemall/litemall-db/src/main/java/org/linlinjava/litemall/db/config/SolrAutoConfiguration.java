package org.linlinjava.litemall.db.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.solr.SolrProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.util.StringUtils;

import java.util.Collections;

/*
 * solr autoconfig 创建
 * by zhangran in 20190924
 */
@Configuration
@ConditionalOnClass({ HttpSolrClient.class, CloudSolrClient.class })
@EnableConfigurationProperties(SolrProperties.class)
public class SolrAutoConfiguration {
    private final SolrProperties properties;

    private SolrClient solrClient;

    public SolrAutoConfiguration(SolrProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(SolrTemplate.class)
    public SolrTemplate solrTemplate(SolrClient solrClient) {
        return new SolrTemplate(solrClient);
    }

    @Bean
    @ConditionalOnMissingBean(SolrClient.class)
    public SolrClient solrClient() {
        this.solrClient = createSolrClient();
        return this.solrClient;
    }

    private SolrClient createSolrClient() {
        //未来用zookeeper注册solr集群时再使用
        if (StringUtils.hasText(this.properties.getZkHost())) {
            return new CloudSolrClient.Builder(Collections.singletonList(this.properties.getZkHost()))
                    .withConnectionTimeout(3000).withSocketTimeout(3000).build();
        }
        //临时设置ip为测试环境内网ip
        //this.properties.setHost("http://192.168.0.163:8983/solr");
        //连接超时3秒，读取超时3秒，即最多等待solr约6秒即放弃，超时时间设置
        return new HttpSolrClient.Builder(this.properties.getHost())
                .withConnectionTimeout(3000).withSocketTimeout(3000).build();
    }

}
