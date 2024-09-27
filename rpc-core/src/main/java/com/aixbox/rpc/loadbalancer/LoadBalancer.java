package com.aixbox.rpc.loadbalancer;

import com.aixbox.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * Description: 负载均衡器（消费端使用）
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/15 下午7:45
 */
public interface LoadBalancer {

    /**
     * 选择服务调用
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 可用服务列表
     * @return
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);

}
