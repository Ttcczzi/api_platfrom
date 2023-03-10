package com.wt.project.route;

import com.wt.mysqlmodel.model.entity.Route;
import com.wt.project.mapper.RouteMapper;
import com.wt.mysqlmodel.model.vo.DBRouteDefination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author xcx
 * @date
 */
@Slf4j
@Component
public class MysqlRouteDefinitionRepository implements RouteDefinitionRepository
        , ApplicationEventPublisherAware {
    private ApplicationEventPublisher eventPublisher;
    private Map<String, RouteDefinition> cache = new ConcurrentHashMap<>();

    @Resource
    RouteMapper routeMapper;

    ArrayList<FilterDefinition> filterDefinitions = new ArrayList<>();
    {
        //todo 动态去数据库查询，存在哪些过滤器
        FilterDefinition filterDefinition = new FilterDefinition();
        //请求流量限制过滤器，结合Redis完成的令牌桶算法，需要配置StrinRedisTemplate
        filterDefinition.setName("RequestRateLimiter");
        //每秒可以装进的令牌数量
        filterDefinition.addArg("redis-rate-limiter.replenishRate", "1");
        //桶的最大令牌容量
        filterDefinition.addArg("redis-rate-limiter.burstCapacity", "2");

        filterDefinitions.add(filterDefinition);
    }

    public void loadCache() {
        cache.clear();

        List<DBRouteDefination> DBRouteDefinations =
                routeMapper.queryAllByRoutId();

        for (DBRouteDefination dbd : DBRouteDefinations) {
            log.info("数据库信息：routeId: {}. 断言机制 {} 种",dbd.getRouteId(), dbd.getRoutes().size());
            RouteDefinition definition = new RouteDefinition();
            definition.setId(dbd.getRouteId());

            List<Route> routes = dbd.getRoutes();
            if(routes == null || routes.size() <= 0){
                continue;
            }
            try {
                definition.setUri(new URI(routes.get(0).getTargethost()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            //添加断言
            List<PredicateDefinition> predicateDefinitionList = routes.stream().map(route -> {
                PredicateDefinition predicateDefinition = new PredicateDefinition();
                predicateDefinition.setName(route.getPredicate());
                predicateDefinition.addArg("_genkey_i", route.getContent());

                return predicateDefinition;
            }).collect(Collectors.toList());
            definition.setPredicates(predicateDefinitionList);

            log.info("路由信息 {} {}",definition.getUri(), predicateDefinitionList);

            //添加过滤器
            log.info("过滤器信息 {} {}",definition.getUri(), filterDefinitions);
            definition.setFilters(filterDefinitions);
            cache.put(definition.getId(), definition);
        }

    }

    @PostConstruct
    public void init() {
        loadCache();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(cache.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    public void addCache(RouteDefinition routeDefinition) {
        cache.putIfAbsent(routeDefinition.getId(), routeDefinition);
        eventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.eventPublisher = publisher;
    }

    void publishEvent() {
        eventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
