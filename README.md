Social Bookmark Viewer FX
=========================
JavaFX で開発したソーシャルブックマークのビューアです。タグによるナビゲーションを重視しています。
現時点で対応しているソーシャルブックマークは [Diigo](https://www.diigo.com) のみです。
そのうちはてなブックマークにも対応したいと考えています。

How to use
----------
ビルドには Maven3 を使います。

まず、 [ここ](https://www.diigo.com/api_keys/new/) から Diigo の API キーを取得してください。
取得したら `src/main/resources/conf/config.properties` のキー `diigo.api.key` に取得したキーを設定します。

    diigo.api.key=(入手した API キー)

API キーの設定を行ったら、Maven を使ってビルドしてください。

    $ mvn package

ビルドの結果 `target` 以下に生成された JAR を実行します。

License
-------
Apache License, Version 2.0

Author
------
AOE, Takashi
