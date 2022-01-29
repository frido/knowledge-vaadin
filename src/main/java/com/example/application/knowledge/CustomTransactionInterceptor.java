package com.example.application.knowledge;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

public class CustomTransactionInterceptor extends TransactionInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("CustomTransactionInterceptor-invoke");
        return super.invoke(invocation);
    }

    @Override
    protected void commitTransactionAfterReturning(TransactionInfo txInfo) {
        System.out.println("CustomTransactionInterceptor-commitTransactionAfterReturning");
        super.commitTransactionAfterReturning(txInfo);
    }

    @Override
    protected TransactionInfo createTransactionIfNecessary(PlatformTransactionManager tm, TransactionAttribute txAttr,
            String joinpointIdentification) {
                System.out.println("CustomTransactionInterceptor-createTransactionIfNecessary");
        return super.createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
    }

    @Override
    protected Object invokeWithinTransaction(Method method, Class<?> targetClass, InvocationCallback invocation)
            throws Throwable {
                System.out.println("CustomTransactionInterceptor-invokeWithinTransaction");
        return super.invokeWithinTransaction(method, targetClass, invocation);
    }
    
}
