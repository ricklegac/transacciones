/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd.transacciones.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import sd.transacciones.model.Persona;
import sd.transacciones.model.PersonaFacade;

/**
 *
 * @author jmferreira
 */
public class TransactionController {

    private Boolean bloqueo = Boolean.FALSE;
    PersonaFacade personaFacade = new PersonaFacade();
    private static final Logger LOG = Logger.getLogger("InfoLog");

    public Boolean getBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(Boolean bloqueo) {
        this.bloqueo = bloqueo;
    }
/**
 * Metodo que lanza la operacion extraccion y deposito parametrizado por bloqueo/nobloque
 * @param nroTransaccion 
 */
    public void extractAndDeposit(int nroTransaccion) {
        String infoTransaccion = "T#" + nroTransaccion + " | ";
        try {
            this.personaFacade.processExtractAndDeposit(nroTransaccion, bloqueo);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "{1}{0}", new Object[]{ex.getMessage(), infoTransaccion});

        }
    }
/**
 * Metodo que lanza la operacion de extraccion parametrizado por bloqueo/nobloqueo
 * @param nroTransaccion 
 */
    public void extract(int nroTransaccion) {
        String infoTransaccion = "T#" + nroTransaccion + " | ";
        try {
            this.personaFacade.processExtract(nroTransaccion, bloqueo);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "{1}{0}", new Object[]{infoTransaccion, ex.getMessage()});

        }
    }

    public void mostrarEstado() {
        LOG.info("\n---------------ESTADO -------------------\n");
        this.personaFacade = new PersonaFacade();
        Persona p1 = personaFacade.findPersona(1);
        LOG.log(Level.INFO, "Persona1: {0}", p1.toString());
        Persona p2 = personaFacade.findPersona(2);
        LOG.info("Persona2: " + p2.toString());
        LOG.info("\n-----------------------------------------\n");

    }

    public void initEstado() throws Exception {
        LOG.info("\n---------------Iniciando Estado -------------------\n");
        this.personaFacade = new PersonaFacade();
        Persona p1 = personaFacade.findPersona(1);
        if (p1 == null) {
            p1 = new Persona(1, "Juan", "Perez", Double.valueOf(500));
            personaFacade.create(p1);
        } else {
            p1.setSaldo(Double.valueOf(500));
            personaFacade.edit(p1);
        }
        LOG.info("Persona1: " + p1.toString());
        Persona p2 = personaFacade.findPersona(2);
        if (p2 == null) {
            p2 = new Persona(2, "Alma", "Almada", Double.valueOf(0));
            personaFacade.create(p2);
        } else {
            p2.setSaldo(Double.valueOf(0));
            personaFacade.edit(p2);
        }
        LOG.info("Persona2: " + p2.toString());
        LOG.info("\n-----------------------------------------\n");

    }

}
