# Sbpayment Sample: Spring Boot

This is a sample application that uses the spring boot integration module of the sbpayment-java project.

## Preparation

Add the sbpayment information in `application.yml`:

```yaml
sbpayment:
  # Required it
  client:
    api-url: https://sbpayment.jp
    merchant-id: MERCHANT_ID
    service-id: SERVICE_ID
    basic-auth-id: BASIC_ID
    basic-auth-password: BASIC_PASS
    hash-key: HASH_KEY
    cipher-sets:
      enabled: true
      des-key: 01234567890123456789AAAA
      des-init-key: ABCDEFGH
      
  # Payment configuration eg.
  creditcard:
    token-url: https://TOKEN_URL/token.js
    cardbrand-return: true or false, Default is false
    customer-info-return: true or false, Default is false
  payeasy:
    type: ONLINE or LINK, Default is ONLINE
    bill-info: ...
    bill-info-kana: ...
```

## Running the Project using gradle

- Run:
```
./gradlew :sbpayment-samples:sbpayment-sample-spring-boot:bootRun
```

- Once started, use your favorite browser and visit this page:
```
http://localhost:8010/
```
