/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd.transacciones.inicio;

import sd.transacciones.controller.TransactionRunnable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import sd.transacciones.controller.TransactionController;
import sd.transacciones.model.Persona;
import sd.transacciones.model.PersonaFacade;

/**
 *
 * @author jmferreira
 */
public class PrincipalSinBloqueo {
    
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final TransactionController TRANSACTION = new TransactionController();
    private static Logger LOG = null;
    
    static {
        InputStream stream = PrincipalSinBloqueo.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
            LOG = Logger.getLogger(PrincipalSinBloqueo.class.getName());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, Exception {
        
        System.out.println("Ingrese la cantidad de Transacciones concurrentes");
        int threadsCount = Integer.parseInt(SCANNER.nextLine());
        Thread[] threads = new Thread[threadsCount];
        //Inserta los dos registros de persona (persona1 y persona2) para operar las transacciones
        TRANSACTION.initEstado();
        
        TRANSACTION.setBloqueo(Boolean.FALSE);
        for (int i = 0; i < threadsCount; i++) {
            threads[i] = new Thread(new TransactionRunnable(TRANSACTION, 1, i));
        }
        for (int i = 0; i < threadsCount; i++) {
            threads[i].start();
        }
        
        for (int i = 0; i < threadsCount; i++) {
            threads[i].join();
        }
        
       TRANSACTION.mostrarEstado();
        
    }
    
    
    
}
