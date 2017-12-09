package com.ky.logic.common.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 外发线程池
 * Created by yl on 2017/7/15.
 */
public enum SendPool {

    INSTANCE;

    /**
     * 如果此时线程池中的数量大于corePoolSize，
     * 缓冲队列workQueue满，
     * 并且线程池中的数量等于maximumPoolSize，
     * 那么通过 handler所指定的策略来处理此任务。
     * 也就是：处理任务的优先级为：核心线程corePoolSize、任务队列workQueue、最大线程maximumPoolSize，
     * 如果三者都满了，使用handler处理被拒绝的任务。
     * <p/>
     * 以下配置最大容量 128 个线程，多余则丢失新来的任务
     */
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            28,                                         // 线程池维护线程的最少数量
            100,                                        // 线程池维护线程的最大数量
            60,                                         // 线程池维护线程所允许的空闲时间
            TimeUnit.SECONDS,                           // 线程池维护线程所允许的空闲时间的单位:秒
            new LinkedBlockingQueue<Runnable>(128),     // 线程池所使用的缓冲队列:可支持有界/无界的队列，使用链表实现
            new ThreadPoolExecutor.DiscardPolicy());    // 线程池对拒绝任务的处理策略:抛弃当前的任务

    public void addJob(Runnable job) {
        executor.execute(job);
    }

    public void removeJob(Runnable job) {
        executor.remove(job);
    }
}
