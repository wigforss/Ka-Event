package org.kasource.spring.transaction;


import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;


public class TransactionSupportImpl  implements TransactionSupport {

	private final ThreadLocal<Set<TransactionListener>> listenersByThread = new ThreadLocal<Set<TransactionListener>>();
	private final TransactionSynchronization txSynch = new TransactionSynchronization();
	
	
	/**
	 * Initialize once per transaction
	 **/
	private void initializeThread() {
		if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.initSynchronization();
		}
		TransactionSynchronizationManager.registerSynchronization(txSynch);
	}
	
	public void addListener(TransactionListener listener) {
		if(!hasTransaction()) {
			throw new IllegalStateException("No valid transaction found!");
		}
		Set<TransactionListener> listeners = listenersByThread.get();
		if (listeners == null) {
			initializeThread();
			listeners = new HashSet<TransactionListener>();
			listenersByThread.set(listeners);
		}
		listeners.add(listener);
	}
	
	public void removeListener(TransactionListener listener) {
		Set<TransactionListener> listeners = listenersByThread.get();
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	private boolean hasTransaction() {
		return TransactionSynchronizationManager.isActualTransactionActive(); 
	}

	/**
	 * Listens on springs Transaction Synchronization and calls listeners.
	 * 
	 * @author Rikard Wigforss
	 **/
	private class TransactionSynchronization extends TransactionSynchronizationAdapter {
	    /**
         * Handles before completion -> calls all listeners
         **/
        @Override
        public void beforeCompletion() {
            super.beforeCompletion();
            Set<TransactionListener> listeners = listenersByThread.get();
            if (listeners != null) {
                for (TransactionListener listener : listeners) {
                    listener.beforeCompletion();
                }
            }
        }

        /**
         * Handles before commit -> calls all listeners
         **/
        @Override
        public void beforeCommit(boolean readOnly) {
            super.beforeCommit(readOnly);
            Set<TransactionListener> listeners = listenersByThread.get();
            if (listeners != null) {
                for (TransactionListener listener : listeners) {
                    listener.beforeCommit(readOnly);
                }
            }
        }

        /**
         * Handles after commit -> calls all listeners
         **/
        @Override
        public void afterCommit() {
            super.afterCommit();
            Set<TransactionListener> listeners = listenersByThread.get();
            if (listeners != null) {
                for (TransactionListener listener : listeners) {
                    listener.afterCommit();
                }
            }
        }

        /**
         * Handles after completion -> calls all listeners
         **/
        @Override
        public void afterCompletion(int status) {
            super.afterCompletion(status);
            Set<TransactionListener> listeners = listenersByThread.get();
            if (listeners != null) {
                TransactionResult transactionStatus = TransactionResult.STATUS_UNKNOWN;
                switch (status) {
                case STATUS_COMMITTED:
                    transactionStatus = TransactionResult.STATUS_COMMITTED;
                    break;
                case STATUS_ROLLED_BACK:
                    transactionStatus = TransactionResult.STATUS_ROLLED_BACK;
                    break;
                }

                for (TransactionListener listener : listeners) {
                    listener.afterCompletion(transactionStatus);
                }
            }

            listenersByThread.set(null);
        }
    
	
	
	}
	

	
	
}
