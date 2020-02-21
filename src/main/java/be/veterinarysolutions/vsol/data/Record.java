package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.main.Database;
import org.hibernate.Session;

public abstract class Record {
	
	public void create(Database db) {
		Session session = db.getSession();
		session.beginTransaction();
		
		session.save(this);
		
		session.getTransaction().commit();
	}
	
	public void save(Database db) {
		Session session = db.getSession();
		session.beginTransaction();
		
		session.update(this);
		
		session.getTransaction().commit();
	}
	

	
}
