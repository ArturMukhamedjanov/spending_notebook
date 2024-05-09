import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import application.models.Category;
import application.models.Mcc;
import application.models.Month;
import application.models.Transaction;
import application.service.TransactionService;
import application.service.DAO.TransactionDAO;

public class TransactionServiceTest {

    private TransactionService transactionService;
    private TransactionDAO transactionDAO;

    @Before
    public void setUp() {
        transactionDAO = mock(TransactionDAO.class);
        transactionService = new TransactionService(transactionDAO);
    }

    @Test
    public void testSaveTransaction() {
        Transaction transaction = new Transaction();

        transactionService.saveTransaction(transaction);

        verify(transactionDAO).saveTransaction(transaction);
    }

    @Test
    public void testGetTransactionById() {
        Long id = 1L;
        Transaction transaction = new Transaction();
        transaction.setId(id);

        when(transactionDAO.getTransactionById(id)).thenReturn(transaction);

        assertEquals(transaction, transactionService.getTransactionById(id));
    }

    @Test
    public void testGetAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        when(transactionDAO.getAllTransactions()).thenReturn(transactions);

        assertEquals(transactions, transactionService.getAllTransactions());
    }

    @Test
    public void testUpdateTransaction() {
        Transaction transaction = new Transaction();

        transactionService.updateTransaction(transaction);

        verify(transactionDAO).updateTransaction(transaction);
    }

    @Test
    public void testDeleteTransaction() {
        Long id = 1L;

        transactionService.deleteTransaction(id);

        verify(transactionDAO).deleteTransaction(id);
    }

    @Test
    public void testGetTransactionsByNameAndValueAndMonth() {
        String name = "Test Transaction";
        double value = 100.0;
        Month month = Month.JANUARY;

        List<Transaction> expectedTransactions = new ArrayList<>();
        expectedTransactions.add(new Transaction());

        when(transactionDAO.getTransactionsByNameAndValueAndMonth(name, value, month)).thenReturn(expectedTransactions);

        assertEquals(expectedTransactions, transactionService.getTransactionsByNameAndValueAndMonth(name, value, month));
    }

    @Test
    public void testGetTotalSpentInCategoryForMonth() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        Month month = Month.JANUARY;

        List<Mcc> mccs = new ArrayList<>();
        Mcc mcc1 = new Mcc();
        mcc1.setId(1L);
        mcc1.setCode("1234");
        Mcc mcc2 = new Mcc();
        mcc2.setId(2L);
        mcc2.setCode("5678");
        mccs.add(mcc1);
        mccs.add(mcc2);
        category.setMccs(mccs);

        Transaction transaction1 = new Transaction();
        transaction1.setValue(50.0);
        transaction1.setMonth(month);
        transaction1.setMcc(mcc1);

        Transaction transaction2 = new Transaction();
        transaction2.setValue(100.0);
        transaction2.setMonth(month);
        transaction2.setMcc(mcc2);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        when(transactionDAO.getTransactionsByMccAndMonth(eq(mcc1), eq(month))).thenReturn(Arrays.asList(transaction1));
        when(transactionDAO.getTransactionsByMccAndMonth(eq(mcc2), eq(month))).thenReturn(Arrays.asList(transaction2));
        


        double expectedTotalSpent = transaction1.getValue() + transaction2.getValue();

        assertEquals(expectedTotalSpent, transactionService.getTotalSpentInCategoryForMonth(category, month), 0.0);
    }
}
