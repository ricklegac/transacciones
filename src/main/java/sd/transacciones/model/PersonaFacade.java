/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd.transacciones.model;

import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import sd.transacciones.modelo.exceptions.NonexistentEntityException;
import sd.transacciones.modelo.exceptions.PreexistingEntityException;

/**
 *
 * @author jmferreira
 */
public class PersonaFacade {

    private static final SecureRandom RANDOM_GENERATOR = new SecureRandom();
    private static final Logger LOG = Logger.getLogger("InfoLog");

    private EntityManager getEntityManager() {
        return EntityManagerHelper.getEntityManager();
    }

    /**
     * Metodo que realiza la extracción y deposito de una cuenta a otra cuenta
     * mediante transacciones (bloqueantes y no bloqueantes)
     *
     * @param idTran
     * @param secured
     * @throws PreexistingEntityException
     * @throws Exception
     */
    public void processExtractAndDeposit(int idTran, Boolean secured) throws PreexistingEntityException, Exception {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        LockModeType tipoBloqueo;
        String infoTransaccion = "T#" + idTran + " | ";
        Double monto = Double.valueOf(100);
        try {
            //Iniciamos la transacción
            tx.begin();
            if (secured) {
                tipoBloqueo = LockModeType.PESSIMISTIC_READ;
            } else {
                tipoBloqueo = LockModeType.NONE;
            }
            LOG.log(Level.INFO, "{0}Recuperando persona 1", infoTransaccion);
            Persona p1 = em.find(Persona.class, 1, tipoBloqueo);
            LOG.log(Level.INFO, "{0}Persona recuperada: {1}", new Object[]{infoTransaccion, p1.toString()});
            LOG.log(Level.INFO, "{0}Extraer {1}", new Object[]{infoTransaccion, monto});
            p1.extraer(monto);
            em.merge(p1);
            LOG.log(Level.INFO, "{0}Recuperando persona 2", infoTransaccion);
            Persona p2 = em.find(Persona.class, 2, tipoBloqueo);
            LOG.log(Level.INFO, "{0} {1}", new Object[]{infoTransaccion, p2.toString()});
            LOG.log(Level.INFO, "{0}Depositar {1}", new Object[]{infoTransaccion, monto});
            p2.depositar(monto);
            em.merge(p2);

            LOG.log(Level.INFO, "{0}Sleeping 5seg", infoTransaccion);
            Thread.sleep(5000);

        } catch (Throwable t) {
            tx.setRollbackOnly();
            LOG.severe("Rollback due to " + t.toString());
//            throw t;
        } finally {
            if (tx.isActive()) {
                tx.commit();
            } else {
                tx.rollback();
            }
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Metodo que realiza la extracción de una cuenta mediante transacciones
     * (bloqueante y no bloqueante)
     *
     * @param idTran
     * @param secured
     * @throws PreexistingEntityException
     * @throws Exception
     */
    public void processExtract(int idTran, Boolean secured) throws PreexistingEntityException, Exception {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        LockModeType tipoBloqueo;
        String infoTransaccion = "T#" + idTran + " | ";
        Double monto = Double.valueOf(150);
        try {
            //Iniciamos la transacción
            tx.begin();
            if (secured) {
                tipoBloqueo = LockModeType.PESSIMISTIC_READ;
            } else {
                tipoBloqueo = LockModeType.NONE;
            }
            LOG.log(Level.INFO, "{0}Recuperando persona", infoTransaccion);
            Persona p1 = em.find(Persona.class, 1, tipoBloqueo);
            LOG.log(Level.INFO, "{0} {1}", new Object[]{infoTransaccion, p1.toString()});
            LOG.log(Level.INFO, "{0}Extraer {1}", new Object[]{infoTransaccion, monto});
            p1.extraer(monto);
            em.merge(p1);
            LOG.log(Level.INFO, "{0}Saldo actual: {1}", new Object[]{infoTransaccion, p1.getSaldo()});

        } catch (Throwable t) {
            tx.setRollbackOnly();
            LOG.severe("Rollback due to " + t.toString());
//            throw t;
        } finally {
            if (tx.isActive()) {
                tx.commit();
            } else {
                tx.rollback();
            }
            if (em != null) {
                em.close();
            }
        }
    }

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public void create(Persona persona) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(persona);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersona(persona.getCedula()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            persona = em.merge(persona);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = persona.getCedula();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
