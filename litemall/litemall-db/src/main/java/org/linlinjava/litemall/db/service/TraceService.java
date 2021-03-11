package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallTraceMapper;
import org.linlinjava.litemall.db.domain.LitemallProduction;
import org.linlinjava.litemall.db.domain.LitemallProductionExample;
import org.linlinjava.litemall.db.domain.LitemallTrace;
import org.linlinjava.litemall.db.domain.LitemallTraceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TraceService {
    @Autowired
    private LitemallTraceMapper litemallTraceMapper;


    public void saveTrace(String traceId,Integer storageId){
        LitemallTrace litemallTrace = new LitemallTrace();
        litemallTrace.setTraceId(traceId);
        litemallTrace.setStorageId(storageId);
        litemallTrace.setAddTime(LocalDateTime.now());
        litemallTrace.setUpdateTime(LocalDateTime.now());
        litemallTraceMapper.insertSelective(litemallTrace);
    }

    public LitemallTrace queryByTraceId(String trace_id) {

        LitemallTraceExample example = new LitemallTraceExample();
        example.createCriteria().andTraceIdEqualTo(trace_id);
        List<LitemallTrace> litemallTraces = litemallTraceMapper.selectByExample(example);

        return litemallTraces.get(0);



    }

    public void updateStatus(LitemallTrace trace) {
        trace.setUpdateTime(LocalDateTime.now());
        litemallTraceMapper.updateByPrimaryKey(trace);
    }
}
