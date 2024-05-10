package application.service;

import java.util.List;

import application.models.Category;
import application.models.Mcc;
import application.models.Month;
import application.models.Transaction;
import application.service.DAO.TransactionDAO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransactionService {
    private TransactionDAO transactionDAO;

    public void saveTransaction(Transaction transaction) {
        transactionDAO.saveTransaction(transaction);
    }

    public Transaction getTransactionById(Long id) {
        return transactionDAO.getTransactionById(id);
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAllTransactions();
    }

    public void updateTransaction(Transaction transaction) {
        transactionDAO.updateTransaction(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionDAO.deleteTransaction(id);
    }

    public List<Transaction> getTransactionsByNameAndValueAndMonth(String name, double value, Month month) {
        return transactionDAO.getTransactionsByNameAndValueAndMonth(name, value, month);
    }
    
    public double getTotalSpentInCategoryForMonth(Category category, Month month) {
        double totalSpent = 0.0;
        if(category == null){
            List<Transaction> transactions = transactionDAO.getTransactionsByNullMccAndMonth(month);
            for (Transaction transaction : transactions) {
                totalSpent += transaction.getValue();
            }
            return totalSpent;
        }
        for(Mcc mcc : category.getMccs()){
            List<Transaction> transactions = transactionDAO.getTransactionsByMccAndMonth(mcc, month);
            for (Transaction transaction : transactions) {
                totalSpent += transaction.getValue();
            }
        }
        for(Category childrenCategory : category.getChildren()){
            totalSpent += getTotalSpentInCategoryForMonth(childrenCategory, month);
        }
        return totalSpent;
    }
    
}
