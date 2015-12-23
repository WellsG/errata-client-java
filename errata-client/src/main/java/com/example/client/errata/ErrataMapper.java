package com.example.client.errata;

import static org.dozer.loader.api.FieldsMappingOptions.hintB;
import static org.dozer.loader.api.TypeMappingOptions.oneWay;

import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

public class ErrataMapper {

    private final DozerBeanMapper mapper;
    private static ErrataMapper INSTANCE = new ErrataMapper();

    private ErrataMapper() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(Map.class, ErrataUser.class, oneWay()).fields(field("this").mapKey("login_name"), "loginName")
                        .fields(field("this").mapKey("realname"), "realName").fields(field("this").mapKey("enabled"), "enabled")
                        .fields(field("this").mapKey("organization"), "organization")
                        .fields(field("this").mapKey("roles"), "roles").fields(field("this").mapKey("realname"), "realName")
                        .fields(field("this").mapKey("receives_mail"), "receivesMail");
                mapping(ErrataUser.class, Map.class, oneWay())
                        .fields("receivesMail", field("this").mapKey("receives_mail"), hintB(Boolean.class))
                        .fields("realName", field("this").mapKey("realname"))
                        .fields("enabled", field("this").mapKey("enabled"), hintB(Boolean.class))
                        .fields("organization", field("this").mapKey("organization"))
                        .fields("roles", field("this").mapKey("roles"), hintB(List.class))
                        .fields("loginName", field("this").mapKey("login_name"));
            }
        };

        mapper = new DozerBeanMapper();
        mapper.addMapping(builder);
    }

    public static ErrataMapper getInstance() {
        return INSTANCE;
    }

    public ErrataUser mapErrataUser(Map<String, Object> context) {
        return map(context, ErrataUser.class);
    }

    private <T> T map(Map<String, Object> context, Class<T> type) {
        return map(context, type, null);
    }

    private <T> T map(Map<String, Object> context, Class<T> type, String mapId) {
        if (context == null) {
            return null;
        }
        return mapper.map(context, type, mapId);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> mapMap(Object o) {
        Map<String, Object> map = mapper.map(o, Map.class);
        map.remove("_junk");
        return map;
    }
}
