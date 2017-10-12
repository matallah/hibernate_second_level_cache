package com.boraji.tutorial.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import com.boraji.tutorial.hibernate.entity.Person;

/**
 * @author M.atallah
 */
public class HibernateUtil {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder =
                        new StandardServiceRegistryBuilder();

                Map<String, Object> settings = new HashMap<>();
                settings.put(Environment.DRIVER, "oracle.jdbc.OracleDriver");
                settings.put(Environment.URL, "jdbc:oracle:thin:@172.16.1.237:1521:XE1");
                settings.put(Environment.USER, "HR");
                settings.put(Environment.PASS, "hr");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                // Enable second level cache
                settings.put(Environment.USE_SECOND_LEVEL_CACHE, false);

                // Specify cache region factory class
                settings.put(Environment.CACHE_REGION_FACTORY,
                        "org.hibernate.cache.jcache.JCacheRegionFactory");

                // Specify cache provider
                settings.put("hibernate.javax.cache.provider",
                        "org.ehcache.jsr107.EhcacheCachingProvider");

                registryBuilder.applySettings(settings);
                registry = registryBuilder.build();
                MetadataSources sources = new MetadataSources(registry)
                        .addAnnotatedClass(Person.class);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
