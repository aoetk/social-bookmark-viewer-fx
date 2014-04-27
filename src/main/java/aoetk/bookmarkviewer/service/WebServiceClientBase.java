package aoetk.bookmarkviewer.service;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Webサービスクライアントの基底クラス.
 */
public abstract class WebServiceClientBase {

    /**
     * Webサービスにアクセスするクライアント.
     */
    protected Client client;

    /**
     * コンストラクタ.
     */
    public WebServiceClientBase() {
        this.client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .register(JacksonObjectMapperProvider.class)
                .build();
    }

    /**
     * リソース解放処理を実装する.
     * 利用側はアプリケーション終了時にこのメソッドを呼び出すこと.
     */
    public void tearDown() {
        client.close();
    }

}
