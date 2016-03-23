package com.wedroid.framework.module.http;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.wedroid.framework.common.WedroidLog;

/**
 * @author 吴传龙
 */
public class WeDroidRequestExecutor {
	
	public static final int THREAD_DEFAULT_PRIOITY = 0;
	public static final int THREAD_HIGH_PRIOITY = 1;
    private static final int CORE_POOL_SIZE = 20;
    private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE * 2 + 1;
    private static final int KEEP_ALIVE = 1;// 发呆1分钟后自己销毁
    private static Runnable mActive;
    // 双端数组队列
    private final static WeDroidRequestArrayDeque<Runnable> mTasks = new WeDroidRequestArrayDeque<Runnable>();
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);
    
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        public Thread newThread(Runnable r) {
//        	int requestToken = -1;
//        	if (r instanceof WeDroidBusiness){
//        		WeDroidBusiness baseServiceImpl = (WeDroidBusiness) r;
//        		requestToken = baseServiceImpl.getRequestToken();
//        	}
        	if (mTasks!=null)
        		WedroidLog.e("CurrentThread", "请求队列中有"+mTasks.size()+"个任务待执行");
        	Thread t = new Thread(r);
        	t.setName("SidiHttpExecutor#Thread#"+mCount.getAndIncrement());
        	return t;
        }
    };
    
    public static final Executor threadPoolExecutor = new ThreadPoolExecutor(
    		CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
             TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    
    /**
     * default THREAD_DEFAULT_PRIOITY priority
     */
    public static void execute(final Runnable runnable) {
    	execute(runnable, THREAD_DEFAULT_PRIOITY);
    }
    
    /**
     * 
     * @param runnable
     * @param priority:	THREAD_DEFAULT_PRIOITY or THREAD_HIGH_PRIOITY
     */
    public static void execute(final Runnable runnable,int priority) {
    	Runnable r = new Runnable() {
    		 public void run() {
                 try {
                 	runnable.run();
                 } finally {
                     scheduleNext();
                 }
             }
		};
		if (priority == THREAD_HIGH_PRIOITY){
			// 在队列的最前面入队
			mTasks.offerFirst(r);
		}else{
			mTasks.offer(r);
		}
//        if (mActive == null) {
            scheduleNext();
//        }
    }
    
    /**
    * @Title: tasksClear 
    * @Description: 清除所有的待执行任务
    * @author 吴传龙
     */
    public static synchronized void tasksClear(){
    	if (mTasks!=null){
    		mTasks.clear();
    	}
    }
    
    protected static synchronized void scheduleNext() {
    	// 取得头队列并执行
    	 if ((mActive = mTasks.poll()) != null) {
             threadPoolExecutor.execute(mActive);
         }
    	 Runnable runnable = mTasks.poll();
    	 if (runnable != null) {
             threadPoolExecutor.execute(mActive);
         }
    	 Runnable runnable1 = mTasks.poll();
    	 if (runnable1 != null) {
             threadPoolExecutor.execute(mActive);
         }
    	 Runnable runnable2 = mTasks.poll();
    	 if (runnable2 != null) {
             threadPoolExecutor.execute(mActive);
         }
    }
    
    /**
     * 得到当前线程的请求码
     */
    public static int getThreadrequestToken(Thread thread){
    	String name = thread.getName();
    	int requestToken = Integer.parseInt(name.substring(name.indexOf("#"), name.length()));
    	return requestToken;
    }
}
