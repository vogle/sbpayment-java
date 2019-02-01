package com.vogle.sbpayment.creditcard.responses;

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

    /**
     * Gets {@link CommitStatus} from the code
     *
     * @param code commit status code
     * @return CommitStatus
     */
    public static CommitStatus status(String code) {
        for (CommitStatus status : CommitStatus.values()) {
            if (code.equalsIgnoreCase(status.getCommitStatusCode())) {
                return status;
            }
        }
        return null;
    }

    /**
     * Gets current status code by String
     *
     * @return Status code
     */
    public String getCommitStatusCode() {
        return statusCode;
    }

}
