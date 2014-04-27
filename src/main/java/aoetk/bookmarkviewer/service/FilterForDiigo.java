package aoetk.bookmarkviewer.service;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * DiigoのWebサービス用フィルタ.
 */
public class FilterForDiigo implements ClientResponseFilter {
    /**
     * DiigoはContent-Typeをtext/htmlで返してくるので、application/jsonに置き換えてしまう.
     *
     * @param requestContext HTTPリクエスト
     * @param responseContext HTTPレスポンス
     * @throws IOException 何らかのIO例外が発生
     */
    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext)
            throws IOException {
        MultivaluedMap<String, String> headers = responseContext.getHeaders();
        headers.putSingle("Content-Type", MediaType.APPLICATION_JSON);
    }
}
