package az.ingress.mscard.exception;

public interface ExceptionConstants {
    String UNEXPECTED_EXCEPTION_CODE = "UNEXPECTED_EXCEPTION";

    String UNEXPECTED_EXCEPTION_MESSAGE = "Unexpected exception occurred";

    String INVALID_REQUEST_CODE = "INVALID_REQUEST_CODE";

    String INVALID_REQUEST_MESSAGE = "Request is not valid";

    String INVALID_INPUT_PARAMETER_CODE = "INVALID_INPUT_PARAMETER_CODE";

    String INVALID_INPUT_PARAMETER_MESSAGE = "Input parameter is not valid: %s";

    String INVALID_URL_CODE = "INVALID_URL_CODE";

    String INVALID_URL_MESSAGE = "URL is wrong";

    String CARD_NOT_FOUND_CODE = "CARD_NOT_FOUND";

    String CARD_NOT_FOUND_MESSAGE = "Card with id: %s not found";
}
