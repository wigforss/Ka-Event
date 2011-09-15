package org.kasource.spring.transaction;

/**
 * Result of a completed transaction
 * 
 * @author rikard
 **/
public enum TransactionResult {
   /** Completion status in case of commit */
   STATUS_COMMITTED,

   /** Completion status in case of roll back */
   STATUS_ROLLED_BACK,

   /** Completion status in case of heuristic mixed completion or system errors */
   STATUS_UNKNOWN;
}
