package cn.gzxy.gtxyrgzn.annotation;

import cn.gzxy.gtxyrgzn.data.Token;
import cn.gzxy.gtxyrgzn.exception.UnauthorizationException;
import cn.gzxy.gtxyrgzn.service.TokenService;
import cn.gzxy.gtxyrgzn.utils.RandomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Set;

public @interface PermissionCheck {
    boolean isCheck() default true;

    boolean requiredAllAccess() default true;

    String[] roles() default {};

    String[] permissions() default {};

    @Aspect
    @Component
    @RequiredArgsConstructor
    @SuppressWarnings("all")
    public class AccessChecker {
        static ObjectMapper mapper = new ObjectMapper();
        final TokenService tokenService;

        @SneakyThrows
        @Around(value = "@annotation(cn.gzxy.gtxyrgzn.annotation.PermissionCheck)")
        public Object check(ProceedingJoinPoint point) {
            // get the permissions that are specified by the annotation
            MethodSignature signature = (MethodSignature) point.getSignature();
            Object target = point.getTarget();
            Method method = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            PermissionCheck annotation = method.getAnnotation(PermissionCheck.class);

            // get Authorization header
            String authorization = request.getHeader("Authorization");

            // check if login is required
            if (annotation.isCheck() && authorization == null)
                throw new UnauthorizationException(RandomUtils.uuid(), request.getRequestURI());


            Token token = tokenService.parse(authorization);

            // check if login is expired
            if (annotation.isCheck() && token == null)
                throw new UnauthorizationException(RandomUtils.uuid(), request.getRequestURI());

            // check if all permissions are required
            Set<String> willBeCheck = Set.of(annotation.roles());

            if (!annotation.requiredAllAccess()) {
                if (!tokenService.checkAccess(token, willBeCheck)) {
                    throw new UnauthorizationException(RandomUtils.uuid(), request.getRequestURI());
                }
            } else {
                for (String permission : willBeCheck) {
                    if (!tokenService.checkAccess(token, permission)) {
                        throw new UnauthorizationException(RandomUtils.uuid(), request.getRequestURI());
                    }
                }
            }

            // set id to attribute
            request.setAttribute("ACCOUNT", token.belong());
            if (token != null) {
                request.setAttribute("TOKEN", token);
            }

            return point.proceed();
        }

        private Class<?>[] getParameterTypes(ProceedingJoinPoint point) {
            Object[] args = point.getArgs();
            Class<?>[] parameterTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
            return parameterTypes;
        }
    }
}
