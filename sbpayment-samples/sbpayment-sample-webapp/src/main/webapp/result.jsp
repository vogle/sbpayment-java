<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

</head>

<body>
<div class="container-fluid">
    <div class="header m-3">
        <h1><img alt="" class="mr-3"
                 src="https://gravatar.com/userimage/110059180/7c4c2d37772675fc66900248f5da5500.png?size=50"/>sbpayment-sample-webapp
        </h1>
    </div>

    <div class="card">
        <div class="card-header">
            ${title}
        </div>
        <div class="card-body">
            <h5 class="card-title">Headers</h5>
            <div class="card-text mb-3">
                <table class="table table-sm">
                    <c:forEach var="resultHeader" items="${resultHeaders}">
                        <tr>
                            <td>${resultHeader.key}</td>
                            <td>${resultHeader.value}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <h5 class="card-title">Body</h5>
            <div class="card-text mb-3">
                <table class="table table-sm">
                    <c:forEach var="body" items="${bodyMap}">
                        <c:if test="${body.key ne 'payMethodInfo'}">
                            <tr>
                                <td>${body.key}</td>
                                <td>${body.value}</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    <c:forEach var="body" items="${bodyMap}">
                        <c:if test="${body.key eq 'payMethodInfo'}">
                            <tr>
                                <td>method.${body.key}</td>
                                <td>${body.value}</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
            </div>
            <a class="btn btn-primary" href="/">Go to checkout</a>
            <c:if test="${not empty hasTrackingId}">
                <a class="btn btn-primary" href="/capture">Capture</a>
                <a class="btn btn-primary" href="/cancel">Cancel or Refund</a>
            </c:if>
            <c:if test="${not empty custNumberUrl}">
                <a class="btn btn-primary" target="_blank" href="${custNumberUrl}">PAY-EASY URL</a>
            </c:if>
        </div>
    </div>
</div>

</body>

</html>
