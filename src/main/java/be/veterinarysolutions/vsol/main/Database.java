package be.veterinarysolutions.vsol.main;

import be.veterinarysolutions.vsol.data.Record;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Database {

	private SessionFactory sessionFactory;
	private Session session;
	
	public Database() {
		(new Thread() {
			public void run() {
				final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
				sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
			}
		}).start();
	}
	
	public void exit() {
		if (session != null && session.isOpen()) {
			session.close();
		}
		
		if (sessionFactory != null && sessionFactory.isOpen()) {
			sessionFactory.close();
		}
	}
	
	public SessionFactory getSessionFactory() { return sessionFactory; }
	
	public Session getSession() {
		while (sessionFactory == null || sessionFactory.isClosed()) {
			try {
				System.out.println("sleeping");
				Thread.sleep(1000);
				return getSession();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (session == null || !session.isOpen()) {
			session = sessionFactory.openSession();
		}
				
		return session;
	}
	
	public <E extends Record> E load(Class<E> classtype, int id) {
		Session session = getSession();
				
		E e = session.get(classtype, id);
		
		return e;
	}
	
}
