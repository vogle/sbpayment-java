package com.vogle.sbpayment.client.response;

/**
 * Commit Status
 *
 * @author Allan Im
 **/
public enum CommitStatus {

    UNPROCESSED("0"), COMMIT("1"), COMMITTED_CANCEL("2"), UNCOMMITTED_CANCEL("3");

    private String statusCode;

    CommitStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCommitStatusCode() {
        return statusCode;
    }

    public static CommitStatus status(String code) {
        for (CommitStatus status : CommitStatus.values()) {
            if (code.equalsIgnoreCase(status.getCommitStatusCode())) {
                return status;
            }
        }
        return null;
    }

}
