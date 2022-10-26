package com.mcarrental.carservice.advice;

import com.google.common.base.CaseFormat;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PageableSortSnakeCaseResolverAdvice {

    private static final String SNAKE_CASE_DELIMITER_CHAR = "_";

    @Pointcut("within(com.mcarrental.carservice.service..*)")
    public void serviceLayer() {
    }

    @Pointcut("args(.., org.springframework.data.domain.Pageable)")
    public void hasPageableArg() {
    }

    @Around(value = "serviceLayer() && hasPageableArg()")
    public Object processPageable(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        int pageableIndex = pageableArgIndex(args);
        if (pageableIndex > -1) {
            Pageable pageable = (Pageable) args[pageableIndex];
            Sort sort = pageable.getSort();
            if (sort.isSorted()) {
                if (sort.get().anyMatch(this::hasSnakeCaseProperty)) {
                    args[pageableIndex] = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), swapToCamelCaseSort(sort));
                }
            }
        }
        return jp.proceed(args);
    }

    private int pageableArgIndex(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Pageable) return i;
        }
        return -1;
    }

    private boolean hasSnakeCaseProperty(Sort.Order order) {
        return order.getProperty().contains(SNAKE_CASE_DELIMITER_CHAR);
    }

    private Sort swapToCamelCaseSort(Sort sort) {
        Sort.Order[] newOrders = sort.get()
                .filter(this::hasSnakeCaseProperty)
                .map(this::swapOrderToCamelCase)
                .toArray(Sort.Order[]::new);
        return Sort.by(newOrders);
    }

    private Sort.Order swapOrderToCamelCase(Sort.Order order) {
        String snakeCaseProperty = order.getProperty();
        String camelCaseProperty = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, snakeCaseProperty);
        Sort.Order newOrder = new Sort.Order(order.getDirection(), camelCaseProperty, order.getNullHandling());
        if (order.isIgnoreCase()) {
            newOrder.ignoreCase();
        }
        return newOrder;
    }
}