package com.wt.project.route;

import com.wt.project.mapper.RouteMapper;
import com.wt.project.route.MysqlRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author xcx
 * @date
 */
@Component
public class Refrash {
    @Autowired
    private MysqlRouteDefinitionRepository repository;

//    public void addCache(String predict, String content, String host) {
//        ArrayList<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
//        try {
//            PredicateDefinition predicateDefinition = new PredicateDefinition();
//            predicateDefinition.setName(predict);
//            predicateDefinition.addArg("_genkey_i", content);
//
//            RouteDefinition routeDefinition = new RouteDefinition();
//            UUID uuid = UUID.randomUUID();
//            routeDefinition.setId(uuid.toString());
//            routeDefinition.setUri(new URI(host));
//            routeDefinition.setPredicates(predicateDefinitionList);
//
//            repository.addCache(routeDefinition);
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void refrash(){
        repository.loadCache();
        repository.publishEvent();
    }

}
