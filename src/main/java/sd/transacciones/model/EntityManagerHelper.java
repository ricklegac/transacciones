/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd.transacciones.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author jmferreira
 */
public class EntityManagerHelper {
	//PROPIEDADES DE CLASE
	private static final EntityManagerFactory emf; 
	private static final ThreadLocal<EntityManager> threadLocal;
	private static final Logger logger;
	
	//CREACION DEL ENTITYMANAGERFACTORY
	static {
		emf = Persistence.createEntityManagerFactory("sd_transacciones_jar_1.0-SNAPSHOTPU"); 		
		threadLocal = new ThreadLocal<EntityManager>();
		logger = Logger.getLogger("InfoLog");
		logger.setLevel(Level.ALL);
	}
	
	//MECANISMO DE OBTENCION DE ENTITYMANAGER
	public static EntityManager getEntityManager() {
		EntityManager manager = threadLocal.get();		
		if (manager == null || !manager.isOpen()) {
			manager = emf.createEntityManager();
			threadLocal.set(manager);
		}
		return manager;
	}
	
	//CIERRE DEL ENTITYMANAGER
	 public static void closeEntityManager() {
        EntityManager em = threadLocal.get();
        threadLocal.set(null);
        if (em != null) em.close();
    }
    
	 //GESTION DE TRANSACCIONES
    public static void beginTransaction() {
    	getEntityManager().getTransaction().begin();
    }
    
    public static void commit() {
    	getEntityManager().getTransaction().commit();
    }  
    
    public static void rollback() {
    	getEntityManager().getTransaction().rollback();
    } 
    
    //CREACION DEL QUERY (CONSULTAS)
    public static Query createQuery(String query) {
		return getEntityManager().createQuery(query);
	}
	
	public static void log(String info, Level level, Throwable ex) {
    	logger.log(level, info, ex);
    }
    
}

