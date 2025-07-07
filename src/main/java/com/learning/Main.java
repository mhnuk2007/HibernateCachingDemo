package com.learning;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        // Step 1: Create Laptop entities
        Laptop l1 = new Laptop(1, "Dell", "Latitude", 8);
        Laptop l2 = new Laptop(2, "HP", "Pavilion", 16);
        Laptop l3 = new Laptop(3, "Dell", "Inspiron", 8);

        // Step 2: Create Alien entities and assign laptops
        Alien alien1 = new Alien(101, "Mukesh", "WordPress", Arrays.asList(l1));
        Alien alien2 = new Alien(102, "Honey", "Java", Arrays.asList(l2, l3));

        // Step 3: Set reverse mapping for bi-directional association
        l1.setAlien(alien1);
        l2.setAlien(alien2);
        l3.setAlien(alien2);

        // Step 4: Setup Hibernate configuration and session
        SessionFactory sf = new Configuration()
                .configure()
                .addAnnotatedClass(Alien.class)
                .addAnnotatedClass(Laptop.class)
                .buildSessionFactory();

        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        // Step 5: Persist data
        session.persist(l1);
        session.persist(l2);
        session.persist(l3);
        session.persist(alien1);
        session.persist(alien2);
        tx.commit();
        session.close();

        Session session1 = sf.openSession();

        // Step 6: First-Level Cache Demo
        System.out.println("\n=== First-Level Cache Demo ===");

        System.out.println("\nFetching Alien ID 101 for the first time (should hit DB)...");
        Alien a1 = session1.get(Alien.class, 101);
        System.out.println("First Fetch: " + a1);

        System.out.println("\nFetching Alien ID 101 again (should come from cache)...");
        Alien a1Again = session1.get(Alien.class, 101);
        System.out.println("Second Fetch (Cached): " + a1Again);

        System.out.println("\nResult: Only one SELECT query executed because of first-level cache (within the same session).");

        session1.close();
        sf.close();
    }
}
