<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, shrink-to-fit=no" name="viewport">

    <title>Sbpayment WebApp Sample</title>

    <link crossorigin="anonymous" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">

    <script crossorigin="anonymous"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script crossorigin="anonymous"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script crossorigin="anonymous"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

    <script src="${spsTokenUrl}" type="text/javascript"></script>
    <script type="text/javascript">
        function newCard() {
            var merchantId = String($("body").data("merchant-id"));
            var serviceId = String($("body").data("service-id"));
            var ccNumber = String($("#cc-number").val());
            var ccExpiration = String($("#cc-expiration").val());
            var securityCode = String($("#cc-cvv").val());

            com_sbps_system.generateToken({
                merchantId: merchantId,
                serviceId: serviceId,
                ccNumber: ccNumber,
                ccExpiration: ccExpiration,
                securityCode: securityCode
            }, self.afterGenerateToken);
        }

        var afterGenerateToken = function (response) {
            console.log(response);
            var isSave = String($("#cc-save").val());
            if (response.result === "OK") {
                $("#token").val(response.tokenResponse.token);
                $("#tokenKey").val(response.tokenResponse.tokenKey);
                $("#isSaveCard").val(isSave);
                $("#payNewCard").submit();
            } else {
                alert('Fail');
            }
        }

        function myCard() {
            $("#payMyCard").submit();
        }

        function payeasy() {
            $("#payPayEasy").submit();
        }
    </script>

</head>

<body data-merchant-id="${merchantId}" data-service-id="${serviceId}">
<div class="container-fluid">
    <div class="header m-3">
        <h1><img alt=""
                 class="mr-3" src="https://gravatar.com/userimage/110059180/7c4c2d37772675fc66900248f5da5500.png?size=50"/>sbpayment-sample-webapp</h1>
    </div>

    <div class="accordion" id="paymentList">
        <c:if test = "${hasSavedCard}">
        <!-- saved card -->
        <div class="card">
            <div class="card-header" id="myCard">
                <h2 class="mb-0">
                    <button aria-controls="collapseMyCard" aria-expanded="true" class="btn btn-link" data-target="#collapseMyCard"
                            data-toggle="collapse" type="button">
                        My Credit Card
                    </button>
                </h2>
            </div>

            <div aria-labelledby="myCard" class="collapse show" data-parent="#paymentList" id="collapseMyCard">
                <div class="card-body media">
                    <img alt=""
                         class="mr-3" height="85" src="http://www.sbpayment.jp/images/specification/index_creditcard.png"/>

                    <div class="media-body">
                        <div class="mb-3">
                            SBペイメントへ保存されているカード番号や有効期限などを利用して、商品等の代金支払いを行うことができます。
                        </div>
                        <a class="btn btn-sm btn-danger" href="/delete">Delete My Card</a>

                        <dl class="mt-3">
                            <dt>Brand</dt>
                            <dd>${savedCard.creditCardBrand}</dd>
                            <dt>Credit Card Number</dt>
                            <dd>${savedCard.ccNumber}</dd>
                            <dt>Expiration</dt>
                            <dd>${savedCard.ccExpiration}</dd>
                        </dl>

                        <button class="btn btn-primary btn-sm btn-block" onclick="myCard()" type="button">
                            Checkout by my credit card
                        </button>
                        <form action="/payment" id="payMyCard" method="post">
                            <input name="type" type="hidden" value="myCard">
                        </form>
                    </div>
                </div>
            </div>
        </div>
        </c:if>

        <!-- credit card -->
        <div class="card">
            <div class="card-header" id="newCard">
                <h2 class="mb-0">
                    <button aria-controls="collapseNewCard" aria-expanded="false" class="btn btn-link collapsed"
                            data-target="#collapseNewCard" data-toggle="collapse" type="button">
                        New Credit Card
                    </button>
                </h2>
            </div>
            <div aria-labelledby="newCard" class="collapse" data-parent="#paymentList" id="collapseNewCard">
                <div class="card-body">
                    <div class="media">
                        <img alt=""
                             class="mr-3" height="85" src="http://www.sbpayment.jp/images/specification/index_creditcard.png"/>
                        <div class="media-body">
                            <div class="mb-3">
                                クレジットカード決済とは、クレジットカード会社が提供する決済手段です。カード番号や有効期限などを利用して、商品等
                                の代金支払いを行うことができます。
                            </div>
                            <div class="form-group">
                                <label for="cc-number">Credit card number</label>
                                <input class="form-control" id="cc-number" placeholder="" required type="text"
                                       value="4111111111111111">
                            </div>
                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label for="cc-expiration">Expiration</label>
                                    <input class="form-control" id="cc-expiration" placeholder="" required type="text"
                                           value="202212">
                                </div>
                                <div class="form-group col-md-6">
                                    <label for="cc-expiration">CVV</label>
                                    <input class="form-control" id="cc-cvv" placeholder="" required type="text"
                                           value="1234">
                                </div>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" id="cc-save" type="checkbox" value="true">
                                <label class="form-check-label" for="cc-save">
                                    Save Credit Card
                                </label>
                            </div>

                            <button class="btn btn-primary btn-sm btn-block" onclick="newCard()" type="button">
                                Checkout by new credit card
                            </button>
                            <form action="/payment" id="payNewCard" method="post">
                                <input name="type" type="hidden" value="newCard">
                                <input name="saveCard" type="hidden" value="false">
                                <input id="token" name="token" type="hidden" value="">
                                <input id="tokenKey" name="tokenKey" type="hidden" value="">
                                <input id="isSaveCard" name="isSaveCard" type="hidden" value="">
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- pay easy -->
        <div class="card">
            <div class="card-header" id="payeasy">
                <h2 class="mb-0">
                    <button aria-controls="collapsePayEasy" aria-expanded="false" class="btn btn-link collapsed"
                            data-target="#collapsePayEasy" data-toggle="collapse" type="button">
                        Pay-Easy
                    </button>
                </h2>
            </div>
            <div aria-labelledby="payeasy" class="collapse" data-parent="#paymentList" id="collapsePayEasy">
                <div class="card-body media">
                    <img alt=""
                         class="mr-3" height="85" src="http://www.sbpayment.jp/images/specification/index_pay-easy.png"/>
                    <div class="media-body">
                        <div class="mb-3">
                            Pay-easy決済とは、購入時に払い出される情報を利用して、ATMやインターネットバンキングで商品等の代金を支払うこ
                            とができる決済サービスです。
                        </div>

                        <button class="btn btn-primary btn-sm btn-block" onclick="payeasy()" type="button">
                            Checkout by Pay-Easy
                        </button>
                        <form action="/payment" id="payPayEasy" method="post">
                            <input name="type" type="hidden" value="payeasy">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript">
    $('.collapse:first').addClass('show');
</script>


</body>

</html>
