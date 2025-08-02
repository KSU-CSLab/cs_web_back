package kr.ac.ks.cs_web_back.global.swagger.error;

public record ProblemDetailSchema(
        int code,
        String detail,
        String instance
) {
    public static ProblemDetailSchema of(ApiErrorResponse apiErrorResponse, int code, String detail) {
        return new ProblemDetailSchema(
                code,
                detail,
                apiErrorResponse.instance()
        );
    }
}