package com.chipswu.aiapplicationgenerator.aop;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * 日志切面类（基于 Jackson 序列化器）
 *
 * @author WuJiaJun
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    private ObjectMapper objectMapper;

    // 虚拟混入类，用于启用动态过滤
    @JsonFilter("excludeFilter")
    static class ExcludeFilterMixIn {
    }

    /**
     * 定义一个切点
     */
    @Pointcut("execution(public * com.chipswu..*Controller.*(..))")
    public void controllerPointcut() {
    }

    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return;
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        // 打印请求信息
        log.info("------------- 开始 -------------");
        log.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        log.info("类名方法: {}.{}", signature.getDeclaringTypeName(), name);
        log.info("远程地址: {}", request.getRemoteAddr());

        // 打印请求参数
        Object[] args = joinPoint.getArgs();

        // 排除特殊类型的参数，如文件类型
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        //String[] excludeProperties = {"mobile"};
        String[] excludeProperties = {};
        log.info("请求参数: {}", toJsonStringWithExcludes(arguments, excludeProperties));
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        //String[] excludeProperties = {"mobile"};
        String[] excludeProperties = {};
        log.info("返回结果: {}", toJsonStringWithExcludes(result, excludeProperties));
        log.info("------------- 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 使用 Jackson 序列化对象，并排除指定字段（仅对普通 Java Bean 生效）
     */
    private String toJsonStringWithExcludes(Object obj, String... excludeProperties) {
        if (obj == null) {
            return "null";
        }

        try {
            // 如果没有要排除的字段，直接序列化（最快最安全）
            if (excludeProperties.length == 0) {
                return objectMapper.writeValueAsString(obj);
            }

            // 判断是否为“可过滤的普通 Java 对象”
            // 排除：基本类型、包装类、String、Map、List、数组等
            if (!isFilterableBean(obj)) {
                return objectMapper.writeValueAsString(obj);
            }

            ObjectMapper copy = objectMapper.copy();
            copy.addMixIn(Object.class, ExcludeFilterMixIn.class);

            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(excludeProperties);
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter("excludeFilter", filter)
                    .setFailOnUnknownId(false); // 安全兜底

            return copy.writer(filters).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("JSON序列化失败", e);
            return "[JSON_SERIALIZATION_ERROR]";
        }
    }

    /**
     * 判断对象是否为可应用属性过滤的普通 Java Bean
     */
    private boolean isFilterableBean(Object obj) {
        if (obj == null) return false;

        Class<?> clazz = obj.getClass();

        // 排除基本类型及其包装类
        if (
                clazz.isPrimitive() ||
                        clazz == String.class ||
                        clazz == Boolean.class ||
                        clazz == Byte.class ||
                        clazz == Character.class ||
                        clazz == Short.class ||
                        clazz == Integer.class ||
                        clazz == Long.class ||
                        clazz == Float.class ||
                        clazz == Double.class
        ) {
            return false;
        }

        // 排除常见容器类型
        if (obj instanceof java.util.Map ||
                obj instanceof Iterable ||
                obj.getClass().isArray()) {
            return false;
        }

        // 排除 JDK 内部类或代理类（如 Spring AOP 代理）
        // 其他情况视为普通 Bean（可过滤）
        return !clazz.getName().startsWith("java.") &&
                !clazz.getName().startsWith("javax.") &&
                !clazz.getName().contains("$$");
    }

}
