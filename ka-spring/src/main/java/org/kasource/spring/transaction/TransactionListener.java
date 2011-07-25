package org.kasource.spring.transaction;




/**
 * Used to get notification of transaction commit / completion. 
 * See org.springframework.transaction.support.TransactionSynchronization.
 * 
 * Usage: Implement this interface and call TransactionSupport.addListener
 * 
 * @author rikard
 **/
public interface TransactionListener {
   
    /**
    * Invoked before transaction commit (before "beforeCompletion"). Can e.g. flush transactional O/R Mapping sessions to the database.
    *
    * This callback does not mean that the transaction will actually be committed. A rollback decision can still occur after this method has been called. 
    * This callback is rather meant to perform work that's only relevant if a commit still has a chance to happen, 
    * such as flushing SQL statements to the database.
    *
    * Note that exceptions will get propagated to the commit caller and cause a rollback of the transaction.
    **/ 
    public void beforeCommit(boolean readOnly);
    
    /**
    * Invoked after transaction commit. Can perform further operations right after the main transaction has successfully committed.
    *
    *  Can e.g. commit further operations that are supposed to follow on a successful commit of the main transaction, like confirmation messages or emails.
    **/  
    public void afterCommit();

    /**
    * Invoked before transaction commit/rollback. Can perform resource cleanup before transaction completion.
    *
    * This method will be invoked after beforeCommit, even when beforeCommit threw an exception. This callback allows for closing resources 
    * before transaction completion, for any outcome.
    **/    
    public void beforeCompletion();
    
    /**
    * Invoked after transaction commit/rollback. Can perform resource cleanup after transaction completion.
    *
    * <b>NOTE:</b> The transaction will have been committed or rolled back already, but the transactional resources 
    * might still be active and accessible. As a consequence, any data access code triggered at this point will 
    * still "participate" in the original transaction, allowing to perform some cleanup (with no commit following anymore!), 
    * unless it explicitly declares that it needs to run in a separate transaction. 
    * Hence: Use PROPAGATION_REQUIRES_NEW for any transactional operation that is called from here. 
    **/  
  public void afterCompletion(TransactionResult status);
 
}
