package com.opennaru.jdg.example;

import java.util.List;

import org.apache.lucene.search.Query;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;

public class QueryCacheApp {

	public static void main(String args[]) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("jgroups.bind_addr", "127.0.0.1");
		System.setProperty("jgroups.udp.mcast_addr", "228.2.2.2");
		System.setProperty("jgroups.udp.mcast_port", "46655");
		
		
		EmbeddedCacheManager manager = new DefaultCacheManager("infinispan-distribution.xml");
		Cache<String, Book> cache = manager.getCache("testCache");

		cache.put("book01", new Book("title 1", "author 1", "editor 1"));
		cache.put("book02", new Book("title 2", "author 2", "editor 2"));
		cache.put("book03", new Book("title 3", "author 3", "editor 3"));
		cache.put("book04", new Book("title 4", "author 4", "editor 4"));
		cache.put("book05", new Book("title 5", "author 5", "editor 5"));
		
		
		SearchManager qf = Search.getSearchManager(cache);
		Query luceneQuery = qf.buildQueryBuilderForClass(Book.class)
				.get()
				.phrase()
				.onField("title")
				.sentence("3")
				.createQuery();
		
		CacheQuery query = qf.getQuery(luceneQuery, Book.class);
		List<Object> list = query.list();
		System.out.println("Search Result:\n" + list);
		
	}

}
