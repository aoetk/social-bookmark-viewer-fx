package aoetk.bookmarkviewer.service;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;

/**
 * JacksonによるJSONのリゾルバを提供する.
 */
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return new ObjectMapper();
    }

}
