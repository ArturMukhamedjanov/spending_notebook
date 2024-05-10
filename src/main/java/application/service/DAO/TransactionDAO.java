package application.service.DAO;

import java.util.List;

import application.models.Mcc;
import application.models.Month;
import application.models.Transaction;

public interface TransactionDAO {
    void saveTransaction(Transaction transaction); 
    Transaction getTransactionById(Long id); 
    List<Transaction> getAllTransactions(); 
    void updateTransaction(Transaction transaction); 
    void deleteTransaction(Long id);
    List<Transaction> getTransactionsByNameAndValueAndMonth(String name, double value, Month month);
    List<Transaction> getTransactionsByMccAndMonth(Mcc mcc, Month month);
    List<Transaction> getTransactionsByNullMccAndMonth(Month month);
}
