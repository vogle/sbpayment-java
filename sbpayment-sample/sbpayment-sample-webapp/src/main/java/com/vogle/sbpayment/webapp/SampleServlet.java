/*
 * Copyright 2019 Vogle Labs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vogle.sbpayment.webapp;

import com.vogle.sbpayment.client.InvalidAccessException;
import com.vogle.sbpayment.client.SpsResult;
import com.vogle.sbpayment.client.params.PaymentInfo;
import com.vogle.sbpayment.client.responses.SpsResponse;
import com.vogle.sbpayment.creditcard.CreditCardPayment;
import com.vogle.sbpayment.creditcard.params.BySavedCard;
import com.vogle.sbpayment.creditcard.params.ByToken;
import com.vogle.sbpayment.creditcard.params.CardInfoResponseType;
import com.vogle.sbpayment.creditcard.responses.CardAuthorizeResponse;
import com.vogle.sbpayment.creditcard.responses.CardInfoDeleteResponse;
import com.vogle.sbpayment.creditcard.responses.CardInfoLookupMethodInfo;
import com.vogle.sbpayment.creditcard.responses.CardInfoLookupResponse;
import com.vogle.sbpayment.creditcard.responses.DefaultResponse;
import com.vogle.sbpayment.payeasy.PayEasyPayment;
import com.vogle.sbpayment.payeasy.params.PayEasy;
import com.vogle.sbpayment.payeasy.receivers.PayEasyDepositReceived;
import com.vogle.sbpayment.payeasy.receivers.PayEasyExpiredCancelReceived;
import com.vogle.sbpayment.payeasy.responses.PayEasyPaymentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.vogle.sbpayment.webapp.SamplePayment.SAMPLE_CUSTOMER_CODE;
import static com.vogle.sbpayment.webapp.SamplePayment.SESSION_RESULT;
import static com.vogle.sbpayment.webapp.SamplePayment.SESSION_TRACKING_ID;
import static com.vogle.sbpayment.webapp.SamplePayment.TOKEN_URL;
import static com.vogle.sbpayment.webapp.SamplePayment.getCreditCardPayment;
import static com.vogle.sbpayment.webapp.SamplePayment.getPayEasyPayment;
import static com.vogle.sbpayment.webapp.SamplePayment.getSpsInfo1;

/**
 * Web App Sample
 *
 * @author Allan Im
 */
public class SampleServlet {

    private static ObjectMapper mapper = new ObjectMapper();

    @WebServlet(name = "CheckoutServlet", urlPatterns = {""}, loadOnStartup = 1)
    public static class CheckoutServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            // payment info
            request.setAttribute("spsTokenUrl", TOKEN_URL);
            request.setAttribute("merchantId", getSpsInfo1().getMerchantId());
            request.setAttribute("serviceId", getSpsInfo1().getServiceId());

            // saved card information
            CreditCardPayment creditCardPayment = getCreditCardPayment();
            SpsResult<CardInfoLookupResponse> cardInfo = creditCardPayment.lookupCard(
                SAMPLE_CUSTOMER_CODE, CardInfoResponseType.LOWER4);
            CardInfoLookupMethodInfo methodInfo = cardInfo.getBody().getPayMethodInfo();
            request.setAttribute("hasSavedCard", methodInfo != null);
            request.setAttribute("savedCard", methodInfo);

            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
    }

    @WebServlet(name = "ResultServlet", urlPatterns = {"result"})
    public static class ResultServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse servletResponse)
            throws ServletException, IOException {

            HttpSession session = request.getSession();
            SpsResult result = (SpsResult) session.getAttribute(SESSION_RESULT);
            notNull(result, "Don't have result information");

            if (result.isSuccessfulConnection()) {
                request.setAttribute("title", result.getBody().getDescription());
            } else {
                request.setAttribute("title", "Fail: " + result.getStatus());
            }

            // check tracking ID
            String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
            if (!isEmpty(trackingId)) {
                request.setAttribute("hasTrackingId", true);
            }

            SpsResponse response = result.getBody();
            if (response instanceof PayEasyPaymentResponse) {
                String custNumber = ((PayEasyPaymentResponse) response).getPayEasyInfo().getCustNumber();
                request.setAttribute("custNumberUrl", custNumber);
            }

            request.setAttribute("resultHeaders", result.getHeaders());
            request.setAttribute("bodyMap", mapper.convertValue(result.getBody(), Map.class));

            request.getRequestDispatcher("result.jsp").forward(request, servletResponse);
        }
    }

    @WebServlet(name = "PaymentServlet", urlPatterns = {"payment"})
    public static class PaymentServlet extends HttpServlet {
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession session = request.getSession();
            PaymentInfo paymentInfo = PaymentInfo.builder()
                .orderId(UUID.randomUUID().toString())
                .customerCode(SAMPLE_CUSTOMER_CODE)
                .amount(1080)
                .tax(80)
                .itemId(UUID.randomUUID().toString().replace("-", ""))
                .build();

            if ("newCard".equals(request.getParameter("type"))) {
                ByToken token = ByToken.builder()
                    .token(request.getParameter("token"))
                    .tokenKey(request.getParameter("tokenKey"))
                    .savingCreditCard(Boolean.valueOf(request.getParameter("isSaveCard")))
                    .build();

                CreditCardPayment creditCardPayment = getCreditCardPayment();
                String redirect = saveCreditCardResult(session, creditCardPayment.authorize(paymentInfo, token));
                response.sendRedirect(redirect);

            } else if ("myCard".equals(request.getParameter("type"))) {
                BySavedCard savedCard = BySavedCard.builder().build();

                CreditCardPayment creditCardPayment = getCreditCardPayment();
                String redirect = saveCreditCardResult(session, creditCardPayment.authorize(paymentInfo, savedCard));
                response.sendRedirect(redirect);
            } else if ("payeasy".equals(request.getParameter("type"))) {
                PayEasy payEasy = PayEasy.builder()
                    .firstName("太郎").lastName("名前")
                    .firstNameKana("タロウ").lastNameKana("ナマエ")
                    .tel("08011112222")
                    .mail("mail@sample.sample")
                    .build();

                PayEasyPayment payEasyPayment = getPayEasyPayment();
                String redirect = saveResult(session, payEasyPayment.payment(paymentInfo, payEasy));
                response.sendRedirect(redirect);
            } else {
                throw new IllegalStateException("Don't have the payment type");
            }
        }
    }

    @WebServlet(name = "CancelServlet", urlPatterns = {"cancel"})
    public static class CancelServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession session = request.getSession();
            String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
            hasText(trackingId, "Don't have the trackingId");

            CreditCardPayment creditCardPayment = getCreditCardPayment();
            SpsResult<DefaultResponse> result = creditCardPayment.cancel(trackingId);
            session.removeAttribute(SESSION_TRACKING_ID);
            String redirect = saveResult(session, result);
            response.sendRedirect(redirect);
        }
    }

    @WebServlet(name = "CaptureServlet", urlPatterns = {"capture"})
    public static class CaptureServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession session = request.getSession();
            String trackingId = (String) session.getAttribute(SESSION_TRACKING_ID);
            hasText(trackingId, "Don't have the trackingId");

            CreditCardPayment creditCardPayment = getCreditCardPayment();
            SpsResult<DefaultResponse> result = creditCardPayment.capture(trackingId);
            String redirect = saveResult(session, result);
            response.sendRedirect(redirect);
        }
    }

    @WebServlet(name = "DeleteServlet", urlPatterns = {"delete"})
    public static class DeleteServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            CreditCardPayment creditCardPayment = getCreditCardPayment();
            SpsResult<CardInfoDeleteResponse> result = creditCardPayment.deleteCard(SAMPLE_CUSTOMER_CODE);
            String redirect = saveResult(request.getSession(), result);
            response.sendRedirect(redirect);
        }
    }

    @WebServlet(name = "ReceiveDepositServlet", urlPatterns = {"deposit"})
    public static class ReceiveDepositServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            PayEasyPayment payEasyPayment = SamplePayment.getPayEasyPayment();

            String result;
            try {
                PayEasyDepositReceived depositReceived = payEasyPayment.receiveDeposit(getBody(request));

                // some check data
                String trackingId = depositReceived.getTrackingId();
                String errorMsg = "";
                if (trackingId == null) {
                    errorMsg = "Don't have order";
                }

                if (isEmpty(errorMsg)) {
                    // some success process
                    result = payEasyPayment.successDeposit();
                } else {
                    result = payEasyPayment.failDeposit(errorMsg);
                }
            } catch (InvalidAccessException e) {
                result = payEasyPayment.failDeposit(e.getMessage());
            }

            PrintWriter out = response.getWriter();
            response.setContentType("application/xml");
            response.setCharacterEncoding("Shift_JIS");
            out.print(result);
        }
    }

    @WebServlet(name = "ReceiveExpiredCancelServlet", urlPatterns = {"expired-cancel"})
    public static class ReceiveExpiredCancelServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            PayEasyPayment payEasyPayment = SamplePayment.getPayEasyPayment();

            String result;
            try {
                PayEasyExpiredCancelReceived cancelReceived = payEasyPayment.receiveExpiredCancel(getBody(request));
                // some check data
                String trackingId = cancelReceived.getTrackingId();
                String errorMsg = "";
                if (trackingId == null) {
                    errorMsg = "Don't have order";
                }

                if (isEmpty(errorMsg)) {
                    // some cancel process
                    result = payEasyPayment.successExpiredCancel();
                } else {
                    result = payEasyPayment.failExpiredCancel(errorMsg);
                }
            } catch (InvalidAccessException e) {
                result = payEasyPayment.failExpiredCancel(e.getMessage());
            }

            PrintWriter out = response.getWriter();
            response.setContentType("application/xml");
            response.setCharacterEncoding("Shift_JIS");
            out.print(result);
        }
    }

    private static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void hasText(String text, String message) {
        if (isEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    private static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    private static String saveCreditCardResult(HttpSession session, SpsResult result) {
        if (result.isSuccess()) {
            String trackingId = ((CardAuthorizeResponse) result.getBody()).getTrackingId();
            session.setAttribute(SESSION_TRACKING_ID, trackingId);
        }

        return saveResult(session, result);
    }

    private static String saveResult(HttpSession session, SpsResult result) {
        session.setAttribute(SESSION_RESULT, result);
        return "/result";
    }

    private static String getBody(HttpServletRequest request) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }


}
