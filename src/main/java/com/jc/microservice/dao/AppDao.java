package com.jc.microservice.dao;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.springframework.stereotype.Repository;
@Repository
public class AppDao {
	Ignite ignite;

	IgniteCache<Integer, String> cache=null;
	ContinuousQuery<Integer, String> qry=null;
	QueryCursor<Cache.Entry<Integer, String>> cur=null;//这个生命周期必须是全局的

	@PostConstruct
	public void init() {
		System.out.println("partitioned启动！");
		addListener();
	}
	public void addListener() {
		ignite = Ignition.start("cache-partitioned.xml");
		// Getting all the server nodes that are already up and running.
		Collection<ClusterNode> nodes = ignite.cluster().forServers().nodes();
		// Setting the baseline topology that is represented by these nodes.
		ignite.cluster().setBaselineTopology(nodes);
		ignite.cluster().active(true);
		cache = ignite.getOrCreateCache("demo");
		qry = new ContinuousQuery<>();
		//初始查询
        qry.setInitialQuery(new ScanQuery<>(new IgniteBiPredicate<Integer, String>() {
            @Override public boolean apply(Integer key, String val) {
                return key > 0;
            }
        }));
        // 本地监听触发回调
        qry.setLocalListener(new CacheEntryUpdatedListener<Integer, String>() {
            @Override public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends String>> evts) {
                for (CacheEntryEvent<? extends Integer, ? extends String> e : evts) {
                	//put(e.getKey(),e.getValue());
                    System.out.println("partitioned updated entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
                }
            }
        });
       // pull();
	}
	public void pull() {
		cur = cache.query(qry);
            // Iterate through existing data.
            for (Cache.Entry<Integer, String> e : cur)
                System.out.println("Queried existing entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
            // Add a few more keys and watch more query notifications.
	}
	public String get(int key){
		IgniteCache<Integer, String> cache = ignite.getOrCreateCache("demo");
		return cache.get(key);
	}
	/**
	 * 写的话一般会通过主节点
	 * @param key
	 * @param value
	 */
	public void put(int key, String value){
		IgniteCache<Integer, String> cache = ignite.getOrCreateCache("demo");
		cache.put(key, value);
	}

}
