# <img src="https://service.biztex.co.jp/wp-content/uploads/2018/08/SB-PaymentService_logo-3-1024x129.png" height="50">&nbsp;<sub>for <img src="https://simpleicons.org/icons/java.svg" height="40"/>JAVA</sub>

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-client/badge.svg?service=github)](https://search.maven.org/search?q=g:com.vogle.sbpayment)
[![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-client.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-client)
[![Nexus Staging](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.vogle.sbpayment/sbpayment-client.svg?style=flat)](https://oss.sonatype.org/#nexus-search;quick~com.vogle.sbpayment)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Join the chat at https://gitter.im/vogle/sbpayment](https://badges.gitter.im/vogle/sbpayment.svg)](https://gitter.im/vogle/sbpayment)

[![Build Status](https://travis-ci.org/vogle/sbpayment-java.svg?branch=master)](https://travis-ci.org/vogle/sbpayment-java)
[![Coverage Status](https://coveralls.io/repos/github/vogle/sbpayment-java/badge.svg?branch=master&service=github)](https://coveralls.io/github/vogle/sbpayment-java?branch=master)
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
</dependencies>
```

## プロジェクトソースからのビルド
ルートディレクトリで `./gradlew build -x integTest`を実行してください。
統合テストをしたい場合は、Sbpayment情報を設定する必要があります。

## ガイド
[ドキュメント](https://vogle.com/sbpayment-java/)を読んでください。

## サポート
[![GitHub issues](https://img.shields.io/github/issues/vogle/sbpayment-java.svg?color=blue&logo=github)](https://github.com/vogle/sbpayment-java/issues)
[![Gitter](https://img.shields.io/gitter/room/vogle/sbpayment.svg?color=blue&logo=gitter)](https://gitter.im/vogle/sbpayment)

サポートはGitHubの[Issueシステム](https://github.com/vogle/sbpayment-java/issues)と[Gitterチャット](https://gitter.im/vogle/sbpayment)を使用します

## 貢献
このプロジェクトを手助けしたいですか？遠慮なくプルリクエストを送信してください。

## ライセンス
Copyright 2019 Vogle Labs.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in
compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.