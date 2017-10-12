package com.boraji.tutorial.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.boraji.tutorial.hibernate.entity.Person;
import org.hibernate.query.Query;

import java.util.List;

/**
 * @author M.atallah
 */
public class MainApp {

    public static void main(String[] args) {
        //  doSave();
        findAll();
        // deleteAll(Person.class);
    }

    private static void doSave() {
        for (int i = 0; i < 10200; i++) {
            create(i);
        }
    }

    private static void create(int number) {
        Session session = null;
        Transaction transaction = null;
        try {
            Person person = new Person("Muhamemd Atallah " + number);
            //Retrieve the person object from database
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(person);
            System.out.println(person.getId());
            transaction.commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void findAll() {
        Session session = null;
        Transaction transaction = null;
        try {
            long startTime = System.currentTimeMillis();
            //Retrieve the person object from db
            List list = findAll(Person.class);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Total time is  before Cache " + totalTime);
            System.out.println("List Size   " + list.size());
            long startTime2 = System.currentTimeMillis();
            //Retrieve the person object from cache
            List list2 = findAll(Person.class);
            long endTime2 = System.currentTimeMillis();
            long totalTime2 = endTime2 - startTime2;
            System.out.println("Total time is  after Cache " + totalTime2);
        } catch (Exception e) {
            e.printStackTrace();
        }


        HibernateUtil.shutdown();
    }

    protected static List findAll(Class clazz) {
        Session session = null;
        Transaction transaction = null;
        List objects = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.getTransaction();
            transaction.begin();
            Query query = session.createQuery("from " + clazz.getName());
            objects = query.list();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return objects;
    }

    private static void deleteAll(Class clazz) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.getTransaction();
            transaction.begin();
            Query query = session.createQuery("delete from " + clazz.getName());
            query.executeUpdate();
            transaction.commit();
            System.out.println("All deleted");
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

}
