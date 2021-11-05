package sd.transacciones.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmferreira
 */
public class TransactionRunnable implements Runnable {

    private static final SecureRandom RANDOM_GENERATOR = new SecureRandom();
    private final TransactionController transactionController;
    private final int transactionsCount;
    private static final Logger LOG = Logger.getLogger("InfoLog");

    private final int hilo;

    public TransactionRunnable(TransactionController transactionController, int transactionsCount, int hilo) {
        this.transactionController = transactionController;
        this.transactionsCount = transactionsCount;
        this.hilo = hilo;

    }

    @Override
    public void run() {
        for (int i = 0; i < this.transactionsCount; i++) {
            int transactionType = Math.abs(RANDOM_GENERATOR.nextInt()) % 2;
            String infoHilo = "T#" + hilo+" |";
            if (transactionType == 0) {
                LOG.log(Level.INFO, "{0}--- EXTRAER P1 (150)---", infoHilo);
                this.transactionController.extract(hilo);
            } else {
                LOG.log(Level.INFO, "{0}--- EXTRAER P1 (100) Y DEPOSITAR P2 (100)---", infoHilo);
                this.transactionController.extractAndDeposit(hilo);
            }
        }
    }
}
