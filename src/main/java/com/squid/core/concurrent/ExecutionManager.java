/*******************************************************************************
 * Copyright © Squid Solutions, 2016
 *
 * This file is part of Open Bouquet software.
 *  
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 *
 * There is a special FOSS exception to the terms and conditions of the 
 * licenses as they are applied to this program. See LICENSE.txt in
 * the directory of this program distribution.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * Squid Solutions also offers commercial licenses with additional warranties,
 * professional functionalities or services. If you purchase a commercial
 * license, then it supersedes and replaces any other agreement between
 * you and Squid Solutions (above licenses and LICENSE.txt included).
 * See http://www.squidsolutions.com/EnterpriseBouquet/
 *******************************************************************************/
package com.squid.core.concurrent;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage operation execution. Create a different pool for each customer
 *
 */
public class ExecutionManager {
	
    private static final Logger logger = LoggerFactory.getLogger(ExecutionManager.class);
	
	public static final ExecutionManager INSTANCE = new ExecutionManager();

	private static final int CORE_POOL_SIZE = 20;
	private static final int MAXIMUM_POOL_SIZE = 20;
	
	private boolean shutdown = false;// turn to true when requested to shutdown
	private ConcurrentHashMap<String, ExecutorService> dispatcher = new ConcurrentHashMap<String, ExecutorService>();
	
	private ExecutionManager() {
		//
	}
	
	private Collection<CancellableCallable<?>> monitoring = new ConcurrentLinkedQueue<CancellableCallable<?>>();
	
	public <V> void registerTask(CancellableCallable<V> task) {
		monitoring.add(task);
	}
	
	public <V> void unregisterTask(CancellableCallable<V> task) {
		monitoring.remove(task);
	}
	
	public <V> Future<V> submit(String customerId, Callable<V> task) {
		ExecutorService service = dispatcher.get(customerId);
		if (service==null) {
			service = createService(customerId);
		}
		return service.submit(task);
	}
	
	public void submit(String customerId, Runnable task) {
		ExecutorService service = dispatcher.get(customerId);
		if (service==null) {
			service = createService(customerId);
		}
		service.submit(task);
	}
	
	public ExecutorService getExecutor(String customerId) {
		ExecutorService service = dispatcher.get(customerId);
		if (service==null) {
			service = createService(customerId);
		}
		return service;
	}
	
	public ExecutorService getExecutor(String customerId, int corePoolSize, int maximumPoolSize) {
		ExecutorService service = dispatcher.get(customerId);
		if (service==null) {
			service = createService(customerId,corePoolSize,maximumPoolSize);
		}
		return service;
	}

	private ExecutorService createService(String customerId) {
		return createService(customerId, CORE_POOL_SIZE, MAXIMUM_POOL_SIZE);
	}

	private ExecutorService createService(String customerId, int corePoolSize, int maximumPoolSize) {
		synchronized (this) {
			if (shutdown) {
				// throw an exception
				throw new RuntimeException("shutdown in progress");
			}
			ExecutorService service = dispatcher.get(customerId);// double check
			if (service==null) {
				service = new CustomThreadPoolExecutor(corePoolSize, maximumPoolSize,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>());
				        //Executors.newFixedThreadPool(THREAD_SIZE);
				dispatcher.put(customerId, service);
			}
			return service;
		}
	}

	/**
	 * shutdown all running jobs, for every customers
	 */
    public void shutdownJobsExecutor() {
		synchronized (this) {
            logger.info("stopping executor pool for "+this.getClass().getName());
            this.shutdown = true; // avoid new customer to create fresh executor
            for (ExecutorService executor : dispatcher.values()) {
            	executor.shutdown();
            	try {
            		if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            			// try to cancel
            			for (CancellableCallable<?> task : monitoring) {
            				task.cancel();
            			}
            		}
				} catch (InterruptedException e) {
					// ignore
				}
            	executor.shutdownNow();
            }
        }
    }

}
