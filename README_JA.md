# <img src="https://service.biztex.co.jp/wp-content/uploads/2018/08/SB-PaymentService_logo-3-1024x129.png" height="50">&nbsp;<sub>for <img src="https://simpleicons.org/icons/java.svg" height="40"/>JAVA</sub>

[![English](https://img.shields.io/badge/ReadMe-English-yellow.svg)](README.md)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-client/badge.svg?service=github)](https://search.maven.org/search?q=g:com.vogle.sbpayment)
[![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-client.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-client)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Gitter](https://badges.gitter.im/vogle/sbpayment.svg)](https://gitter.im/vogle/sbpayment?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

[![Build Status](https://travis-ci.org/vogle/sbpayment-java.svg?branch=master)](https://travis-ci.org/vogle/sbpayment-java)
[![Coveralls](https://img.shields.io/coveralls/github/vogle/sbpayment-java.svg)](https://coveralls.io/github/vogle/sbpayment-java?branch=master)
[![codecov](https://codecov.io/gh/vogle/sbpayment-java/branch/master/graph/badge.svg)](https://codecov.io/gh/vogle/sbpayment-java)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ff7cb7516ca248cfb798e6ab08faaacf)](https://www.codacy.com/app/vogle/sbpayment-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=vogle/sbpayment-java&amp;utm_campaign=Badge_Grade)
[![CodeFactor](https://www.codefactor.io/repository/github/vogle/sbpayment-java/badge)](https://www.codefactor.io/repository/github/vogle/sbpayment-java)
[![Maintainability](https://api.codeclimate.com/v1/badges/c27bba1102704c1853dc/maintainability)](https://codeclimate.com/github/vogle/sbpayment-java/maintainability)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.vogle.sbpayment%3Asbpayment-java&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.vogle.sbpayment%3Asbpayment-java)

    
「Sbpayment for JAVA」は、[SB-Payment Service](https://www.sbpayment.jp/)のAPIタイプをJAVAを利用して、簡単に使用できます。
サービス仕様書は[SBPaymentサイト](https://www.sbpayment.jp/document/specification/)で確認できるし、こちらでは現在下記の決済機能を提供しています。

- クレジットカード決済
- Pay-Easy決済

## 前提条件
- Java JDK 8以上
- Sbpaymentのアカウント情報

## インストール
リリースは「maven central」に公開されています

#### Gradle:
```groovy
dependencies {
    // creditcard
    implementation 'com.vogle.sbpayment:sbpayment-creditcard:1.+'
    // payeasy
    implementation 'com.vogle.sbpayment:sbpayment-payeasy:1.+'
    // if you use spring boot
    implementation 'com.vogle.sbpayment:sbpayment-spring-boot-starter:1.+'
}
```

#### Maven:
```xml
<dependencies>
  <!--creditcard-->
  <dependency>
    <groupId>com.vogle.sbpayment</groupId>
    <artifactId>sbpayment-creditcard</artifactId>
    <version>1.0.0</version>
  </dependency>
  <!--payeasy-->
  <dependency>
    <groupId>com.vogle.sbpayment</groupId>
    <artifactId>sbpayment-payeasy</artifactId>
    <version>1.0.0</version>
  </dependency>
  <!--if you use spring boot-->
    <dependency>
      <groupId>com.vogle.sbpayment</groupId>
      <artifactId>sbpayment-spring-boot-starter</artifactId>
      <version>1.0.0</version>
    </dependency>
</dependencies>
```

#### モジュール
| Module | Maven Central | JavaDoc | @since |
| ------------- | ------------- | ------------- | ------------- |
| Client | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-client/badge.svg?service=github)](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-client) | [![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-client.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-client) | ![@since v1.0](https://img.shields.io/badge/%40since-v1.0-blueviolet.svg) |
| Credit Card | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-creditcard/badge.svg?service=github)](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-creditcard) | [![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-creditcard.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-creditcard) | ![@since v1.0](https://img.shields.io/badge/%40since-v1.0-blueviolet.svg) |
| Pay-Easy | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-payeasy/badge.svg?service=github)](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-payeasy) | [![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-payeasy.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-payeasy) | ![@since v1.0](https://img.shields.io/badge/%40since-v1.0-blueviolet.svg) |
| Spring Boot Starter | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-spring-boot-starter/badge.svg?service=github)](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-spring-boot-starter) | [![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-spring-boot-starter.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-spring-boot-starter) | ![@since v1.0](https://img.shields.io/badge/%40since-v1.0-blueviolet.svg) |


## プロジェクトソースからのビルド
ルートディレクトリで `./gradlew build -x integTest`を実行してください。
統合テストをしたい場合は、Sbpayment情報を設定する必要があります。

#### テストプロパティの作成
- `it1.properties`と`it2.properties`を`/config`のディレクトリへ作成.
- `it1.properties`はSBペイメント（API・自動）のアカウント情報を作成。
- `it2.properties`はSBペイメント（API・指定）のアカウント情報を作成。

※　SBペイメントのアカウント情報は自分のアカウントを利用してください。

#### プロパティの情報

|項目|説明|デフォルト|
| --- | --- | --- |
|sbpayment.charset|SBペイメントのキャリクタセット|Shift_JIS|
|sbpayment.timeZone|SBペイメントのタイムゾーン|JST|
|sbpayment.apiUrl|SBペイメントのAPIサビースの接続先||
|sbpayment.merchantId|SBペイメントから取得したマーチャントID||
|sbpayment.serviceId|SBペイメントから取得したサービスID||
|sbpayment.basicAuthId|SBペイメントのベーシック認証ID||
|sbpayment.basicAuthPassword|SBペイメントのベーシック認証パスワード||
|sbpayment.allowableSecondOnRequest|リクエスト時の許容時間(秒)|600|
|sbpayment.hashKey|ハッシュキー||
|sbpayment.cipherEnabled|3DES 暗号化使用可否|FALSE|
|sbpayment.desKey|3DES 暗号化キー||
|sbpayment.desInitKey|3DES 初期化キー||

## 開発サンプル

- **Spring-Bootの利用:** [sbpayment-sample-spring-boot](sbpayment-samples/sbpayment-sample-spring-boot)
- **一般ウェブアプリケーション:** [sbpayment-sample-webapp](sbpayment-samples/sbpayment-sample-webapp)


## API

#### クレジットカード

インターフェース: [CreditCardPayment](sbpayment-creditcard/src/main/java/com/vogle/sbpayment/creditcard/CreditCardPayment.java)

|リクエストID|メッソド|説明|
| --- | --- | --- |
|ST01-00131-101|authorize|決済要求（トークン、保存カード）|
|ST01-00111-101|authorize|決済要求（クレジットカード情報）*|
|ST01-00133-101|reauthorize|再与信要求|
|ST02-00101-101|commit|確定要求|
|ST02-00201-101|capture|売上確定要求|
|ST02-00303-101|cancel|取消要求|
|ST02-00303-101|refund|返金要求（取消要求と統合）|
|ST02-00307-101|refund|部分返金要求|
|MG01-00101-101|lookup|決済結果参照|
|MG02-00131-101|saveCard|クレジットカード情報を保存（トークン）|
|MG02-00101-101|saveCard|クレジットカード情報を保存（クレジットカード情報）*|
|MG02-00132-101|updateCard|クレジットカード情報を更新（トークン）|
|MG02-00102-101|updateCard|クレジットカード情報を更新（クレジットカード情報）*|
|MG02-00103-101|deleteCard|クレジットカード情報を削除|
|MG02-00104-101|lookupCard|クレジットカード情報を参照|

- *決済要求：本サビスは本番環境では使わないでください、実際sbpaymentのAPIから削除されました。

#### ペイジー

See Interface: [PayEasyPayment](sbpayment-payeasy/src/main/java/com/vogle/sbpayment/payeasy/PayEasyPayment.java)

|リクエストID|メッソド|説明|
| --- | --- | --- |
|ST01-00101-703|payment|決済要求|
|NT01-00103-703|receiveDeposit|入金通知受信|
| |successDeposit|入金通知の成功結果を返す|
| |failDeposit|入金通知の失敗結果を返す|
|NT01-00104-703|receiveExpiredCancel|支払期限切れキャンセル通知信|
| |successExpiredCancel|支払期限切れキャンセル通の成功結果を返す|
| |failExpiredCancel|支払期限切れキャンセル通の失敗結果を返す|


## サポート
[![GitHub issues](https://img.shields.io/github/issues/vogle/sbpayment-java.svg?color=blue&logo=github)](https://github.com/vogle/sbpayment-java/issues)
[![Gitter](https://img.shields.io/gitter/room/vogle/sbpayment.svg?color=blue&logo=gitter)](https://gitter.im/vogle/sbpayment)

サポートはGitHubの[Issueシステム](https://github.com/vogle/sbpayment-java/issues)と[Gitterチャット](https://gitter.im/vogle/sbpayment)を使用します

## 貢献
このプロジェクトを手助けしたいですか？プルリクエストを送信してください。

## ライセンス
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fvogle%2Fsbpayment-java.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fvogle%2Fsbpayment-java?ref=badge_shield)

Copyright 2019 Vogle Labs.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in
compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.