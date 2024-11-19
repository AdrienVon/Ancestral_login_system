package cn.gzxy.gtxyrgzn.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("SpellCheckingInspection")
public class UnauthorizationException extends StarplexException {
    @SuppressWarnings("all")
    public UnauthorizationException(String traceId, String path) {
        super(
                traceId,
                401,
                "Unauthorization"
        );

        log.warn(
                """
                        Trace ID: {traceId}
                        Catch UnauthorizationException: {path}
                        """,
                traceId,
                path
        );
    }
}
