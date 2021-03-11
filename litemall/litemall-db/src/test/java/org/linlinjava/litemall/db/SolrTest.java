package org.linlinjava.litemall.db;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linlinjava.litemall.db.util.SolrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SolrTest {

    @Autowired
    private SolrUtil solrUtil;
    @Test
    public void testImoportGoodsDataBySolrJ() {
        solrUtil.importGoodsDataBySolrJ();
    }
    @Test
    public void testImoportCategoryDataBySolrJ() {
        solrUtil.importCategoryDataBySolrJ();
    }
    @Test
    public void testListParams(){
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println(list.toString()
                .replaceAll("\\[","(")
                .replaceAll("\\]",")"));

    }


}
