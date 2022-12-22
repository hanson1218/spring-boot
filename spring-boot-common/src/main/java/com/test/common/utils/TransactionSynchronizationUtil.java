package com.test.common.utils;

import org.springframework.transaction.event.TransactionalApplicationListenerAdapter;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Consumer;

/**
 * 当前事务提交后执行
 */
public class TransactionSynchronizationUtil {
    public TransactionSynchronizationUtil() {
    }

    /**
     * 注册事务提交后的回调
     * @param consumer
     */
    public static void registerAfterCommitCallback(Consumer<Object> consumer){
        //spring 5.3以下写法
        if(TransactionSynchronizationManager.isSynchronizationActive()){
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                   if(null != consumer){
                        consumer.accept(null);
                   }
                }
            });
        }
        //spring5.3以上写法
        if(TransactionSynchronizationManager.isSynchronizationActive()){
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    if(null != consumer){
                        consumer.accept(null);
                    }
                }
            });
        }
    }
}
