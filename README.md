# <img src="https://service.biztex.co.jp/wp-content/uploads/2018/08/SB-PaymentService_logo-3-1024x129.png" height="50">&nbsp;<sub>for <img src="https://simpleicons.org/icons/java.svg" height="40"/>JAVA</sub>

[![Japanese](https://img.shields.io/badge/ReadMe-%E6%97%A5%E6%9C%AC%E8%AA%9E-yellow.svg)](README_JA.md)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-client/badge.svg?service=github)](https://search.maven.org/search?q=g:com.vogle.sbpayment)
[![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-client.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-client)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Join the chat at https://gitter.im/vogle/sbpayment](https://badges.gitter.im/vogle/sbpayment.svg)](https://gitter.im/vogle/sbpayment)
[![Gradle Scan](https://img.shields.io/badge/Gradle-Scan-blue.svg)](https://vogle.page.link/scan)

[![Build Status](https://travis-ci.org/vogle/sbpayment-java.svg?branch=master)](https://travis-ci.org/vogle/sbpayment-java)
[![Coveralls](https://img.shields.io/coveralls/github/vogle/sbpayment-java.svg)](https://coveralls.io/github/vogle/sbpayment-java?branch=master)
[![codecov](https://codecov.io/gh/vogle/sbpayment-java/branch/master/graph/badge.svg)](https://codecov.io/gh/vogle/sbpayment-java)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ff7cb7516ca248cfb798e6ab08faaacf)](https://www.codacy.com/app/vogle/sbpayment-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=vogle/sbpayment-java&amp;utm_campaign=Badge_Grade)
[![CodeFactor](https://www.codefactor.io/repository/github/vogle/sbpayment-java/badge)](https://www.codefactor.io/repository/github/vogle/sbpayment-java)
[![Maintainability](https://api.codeclimate.com/v1/badges/c27bba1102704c1853dc/maintainability)](https://codeclimate.com/github/vogle/sbpayment-java/maintainability)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.vogle.sbpayment%3Asbpayment-java&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.vogle.sbpayment%3Asbpayment-java)



Sbpayment for JAVA makes it easy to use the API type of [SB-Payment Service](https://www.sbpayment.jp/) using JAVA.

Service specifications can be confirmed on the [SBPayment site](https://www.sbpayment.jp/document/specification/), and it currently offer the following payment features.
- Credit Card
- Pay-Easy

## Prerequisites
- Java JDK 8 or higher
- Sbpayment Service Account

## Getting Started
Releases are published to maven central

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

#### Modules
| Module | Maven Central | JavaDoc | @since |
| ------------- | ------------- | ------------- | ------------- |
| Client | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-client/badge.svg?service=github)](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-client) | [![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-client.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-client) | ![@since v1.0](https://img.shields.io/badge/%40since-v1.0-blueviolet.svg) |
| Credit Card | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-creditcard/badge.svg?service=github)](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-creditcard) | [![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-creditcard.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-creditcard) | ![@since v1.0](https://img.shields.io/badge/%40since-v1.0-blueviolet.svg) |
| Pay-Easy | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-payeasy/badge.svg?service=github)](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-payeasy) | [![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-payeasy.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-payeasy) | ![@since v1.0](https://img.shields.io/badge/%40since-v1.0-blueviolet.svg) |
| Spring Boot Starter | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-spring-boot-starter/badge.svg?service=github)](https://maven-badges.herokuapp.com/maven-central/com.vogle.sbpayment/sbpayment-spring-boot-starter) | [![Javadocs](http://javadoc.io/badge/com.vogle.sbpayment/sbpayment-spring-boot-starter.svg)](http://javadoc.io/doc/com.vogle.sbpayment/sbpayment-spring-boot-starter) | ![@since v1.0](https://img.shields.io/badge/%40since-v1.0-blueviolet.svg) |


## Building from Source
Execute `./gradlew build -x integTest` in the root directory.
If you want to integTest, You have to setup Sbpayment information

#### Setup Sbpayment information
- Create `it1.properties` and `it2.properties` in a directory with the path '/ config'.
- `it1.properties` is Sbpayment information with API-Automatic
- `it2.properties` is Sbpayment information with API-Specified

※ Use your own account for SB Payment account information.

#### Properties

|Items|Description|Default|
| --- | --- | --- |
|sbpayment.charset|Character code of SB-Payment|Shift_JIS|
|sbpayment.timeZone|Timezone of SB-Payment|JST|
|sbpayment.apiUrl|API Service URL||
|sbpayment.merchantId|Merchant ID of SB-Payment||
|sbpayment.serviceId|Service ID of SB-Payment||
|sbpayment.basicAuthId|Basic authentication ID||
|sbpayment.basicAuthPassword|Basic authentication password||
|sbpayment.allowableSecondOnRequest|Allowable time for request (seconds)|600|
|sbpayment.hashKey|Hash Key||
|sbpayment.cipherEnabled|3DES Encryption availability|FALSE|
|sbpayment.desKey|3DES Encryption key||
|sbpayment.desInitKey|3DES Initialization key||


## Development Sample

- **Use Spring-Boot:** See [sbpayment-sample-spring-boot](sbpayment-samples/sbpayment-sample-spring-boot)
- **General Web Application:** See [sbpayment-sample-webapp](sbpayment-samples/sbpayment-sample-webapp)


## API

#### Credit-Card

See Interface: [CreditCardPayment](sbpayment-creditcard/src/main/java/com/vogle/sbpayment/creditcard/CreditCardPayment.java)

|API-Request ID|Method|Description|
| --- | --- | --- |
|ST01-00131-101|authorize|Authorize by token or saved card|
|ST01-00111-101|authorize|Authorize by credit-card*|
|ST01-00133-101|reauthorize|Reauthorize by tracking information|
|ST02-00101-101|commit|Commit|
|ST02-00201-101|capture|Capture payment|
|ST02-00303-101|cancel|Cancel payment|
|ST02-00303-101|refund|It was integrated with Cancel|
|ST02-00307-101|refund|Refund with amount|
|MG01-00101-101|lookup|Lookup transaction|
|MG02-00131-101|saveCard|Save card by token|
|MG02-00101-101|saveCard|Save card by credit-card*|
|MG02-00132-101|updateCard|Update card by token|
|MG02-00102-101|updateCard|Update card by credit-card*|
|MG02-00103-101|deleteCard|Delete card|
|MG02-00104-101|lookupCard|Lookup card|

- *Don't use in production environment, indeed it has been removed from Sbpayment API.

#### Pay-Easy

See Interface: [PayEasyPayment](sbpayment-payeasy/src/main/java/com/vogle/sbpayment/payeasy/PayEasyPayment.java)

|API-Request ID|Method|Description|
| --- | --- | --- |
|ST01-00101-703|payment|Payment by PayEasy|
|NT01-00103-703|receiveDeposit|Receive a deposit notification|
| |successDeposit|Return successful result of deposit notification|
| |failDeposit|Return failure result of deposit notification|
|NT01-00104-703|receiveExpiredCancel|Receive a expired cancel notice|
| |successExpiredCancel|Return successful result of cancellation due date expired|
| |failExpiredCancel|Return failure result of cancellation due date expired|


## Support
[![GitHub issues](https://img.shields.io/github/issues/vogle/sbpayment-java.svg?color=blue&logo=github)](https://github.com/vogle/sbpayment-java/issues)
[![Gitter](https://img.shields.io/gitter/room/vogle/sbpayment.svg?color=blue&logo=gitter)](https://gitter.im/vogle/sbpayment)

The project uses GitHub’s integrated [issue tracking system](https://github.com/vogle/sbpayment-java/issues) and [Gitter Community](https://gitter.im/vogle/sbpayment)


## Contributions
Contributions to the project can be done using pull requests.
You will be asked to sign a contribution agreement after creating the first one.


## License 
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Copyright 2019 Vogle Labs.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in
compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.