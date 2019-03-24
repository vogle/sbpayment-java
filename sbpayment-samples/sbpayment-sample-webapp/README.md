# Sbpayment Sample: Web Application

This is a sample application that uses the spring boot integration module of the sbpayment-java project.

## Preparation

- Create `it1.properties` and `it2.properties` in a directory with the path '/ config'.
- `it1.properties` is Sbpayment information with API-Automatic
- `it2.properties` is Sbpayment information with API-Specified

```properties
sbpayment.apiUrl=https://sbpayment.jp
sbpayment.merchantId=MERCHANT_ID
sbpayment.serviceId=SERVICE_ID
sbpayment.basicAuthId=BASIC_ID
sbpayment.basicAuthPassword=BASIC_PASS
sbpayment.hashKey=HASH_KEY
sbpayment.cipherEnabled=true
sbpayment.desKey=01234567890123456789AAAA
sbpayment.desInitKey=ABCDEFGH
```

## Running the Project using gradle

- Run:
```
./gradlew :sbpayment-samples:sbpayment-sample-webapp:appRun
```

- Once started, use your favorite browser and visit this page:
```
http://localhost:8020/
```
