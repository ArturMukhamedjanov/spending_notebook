package application.cli;

import application.service.CategoryService;
import application.service.MccService;
import application.service.TransactionService;
import lombok.AllArgsConstructor;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.cli.converters.MonthConverter;
import application.cli.exceptions.MccCastException;
import application.cli.exceptions.MccExistException;
import application.models.*;

@Command
@AllArgsConstructor
public class MainCommands {

    private final CategoryService categoryService;
    private final MccService mccService;
    private final TransactionService transactionService;

    @CommandLine.Command(name = "help", description = "Помощь")
    void help() {
        CommandLine.usage(this, System.out);
    }

    @CommandLine.Command(name = "show_category_by_monthes", description = "Траты в категории по месяцам (сумма за месяц)")
    void showCategoryByMonthes(@CommandLine.Parameters(description = "Название категории") String categoryName) {
        Category category = categoryService.getCategoryByName(categoryName);
        if (category == null) {
            System.out.println("Category with name " + categoryName + " does not exist");
            return;
        }
        System.out.println("Transactions in category '" + categoryName + "' by months:");
        // Iterate over months
        for (Month month : Month.values()) {
            double totalSpent = transactionService.getTotalSpentInCategoryForMonth(category, month);
            System.out.println(month + ": " + totalSpent);
        }
    }


    @CommandLine.Command(name = "show_all_by_month", description = "Показать список категорий с суммой потраченных средств в выбранный месяц")
    void showCategoriesByMonth(
            @CommandLine.Parameters(description = "Месяц", converter = MonthConverter.class) Month month) {
        System.out.println("Categories:");
        // Iterate over root categories
        for (Category category : categoryService.getRootCategories()) {
            printCategoryTreeWithTotalSpent(category, month, 0);
        }
    }

    @CommandLine.Command(name = "remove_transaction", description = "Удалить трату")
    void removeTransaction(
            @CommandLine.Parameters(description = "Название транзакции") String name,
            @CommandLine.Parameters(description = "Значение транзакции") double value,
            @CommandLine.Parameters(description = "Месяц транзакции", converter = MonthConverter.class) Month month) {
        List<Transaction> transactions = transactionService.getTransactionsByNameAndValueAndMonth(name, value, month);
        if (transactions.isEmpty()) {
            System.out.println("Transaction not found.");
            return;
        }
        Transaction transactionToRemove = transactions.get(0); // Get the first matching transaction
        transactionService.deleteTransaction(transactionToRemove.getId());
        System.out.println("Transaction removed successfully.");
    }


    @CommandLine.Command(name = "add_transaction", description = "Добавить трату")
    void addTransaction(
            @CommandLine.Parameters(description = "Название траты") String transactionName,
            @CommandLine.Parameters(description = "Значение траты") double transactionValue,
            @CommandLine.Parameters(description = "Месяц", converter = MonthConverter.class) Month month,
            @CommandLine.Parameters(description = "MCC (необязательно)", arity = "0..1") String mccCode) {
        Transaction transaction = new Transaction();
        transaction.setName(transactionName);
        transaction.setValue(transactionValue);
        transaction.setMonth(month);

        if (mccCode != null) {
            Mcc mcc = mccService.getMccByCode(mccCode);
            transaction.setMcc(mcc);
        }
        transactionService.saveTransaction(transaction);
        String categoryName = "Without category";
        if(mccCode!= null){
            categoryName = mccService.getMccByCode(mccCode).getCategory().getName();
        }
        System.out.println("Transaction added successfully to category : " + categoryName);
    }

    @CommandLine.Command(name = "show_categories", description = "Показать список категорий")
    void showCategories() {
        System.out.println("Categories:");
        //categoryService.getAllCategories().forEach(System.out::println);
        for (Category category : categoryService.getRootCategories()) {
            printCategoryTree(category, 0);
        }
    }


    @CommandLine.Command(name = "remove_category", description = "Удалить категорию трат")
    void removeCategory(@CommandLine.Parameters(description = "Название категории") String categoryName) {
        if (!categoryService.isCategoryNameExists(categoryName)) {
            System.out.println("Category with name " + categoryName + " does not exist");
            return;
        }
        Category category = categoryService.getCategoryByName(categoryName);
        categoryService.deleteCategory(category.getId());
        System.out.println("Category with name " + categoryName + " was successfully removed");
    }


    @CommandLine.Command(name = "add_group_to_category", description = "Добавить группу категорий в категорию")
    void addGroupToCategory(
            @CommandLine.Parameters(description = "Название категории") String categoryName,
            @CommandLine.Parameters(description = "Категории для добавления", arity = "1..*") List<String> categoriesToAdd) {
        Category targetCategory = categoryService.getCategoryByName(categoryName);
        if (targetCategory == null) {
            System.out.println("Category with name " + categoryName + " does not exist");
            return;
        }

        List<Category> categories = new ArrayList<>();
        for (String categoryNameToAdd : categoriesToAdd) {
            Category categoryToAdd = categoryService.getCategoryByName(categoryNameToAdd);
            if (categoryToAdd == null) {
                System.out.println("Category " + categoryNameToAdd + " does not exist");
                return;
            }
            if(categoryService.isSubChildren(categoryToAdd, targetCategory)){
                System.out.println("Unable to add. Category " + targetCategory.getName() + " is subchildren of " + categoryNameToAdd);
                return;
            }
            categoryToAdd.addParent(targetCategory);
            categories.add(categoryToAdd);
        }
        targetCategory.getChildren().addAll(categories);
        categoryService.updateCategory(targetCategory);

        System.out.println("Added group to category " + categoryName + " (" + categoriesToAdd + ")");
    }


    @CommandLine.Command(name = "add_mcc_to_category", description = "Изменить категорию трат")
    void addMccToCategory(
        @CommandLine.Parameters(description = "Название категории") String categoryName,
        @CommandLine.Parameters(description = "MCC коды", arity = "1..*") int[] mccCodesToAdd){
        Category category = categoryService.getCategoryByName(categoryName);
        if (category == null) {
            System.out.println("Category with name " + categoryName + " does not exist");
            return;
        }
        List<Mcc> newMccsToAdd = new ArrayList<>();
        try {
            newMccsToAdd = castIntArrToMcc(mccCodesToAdd, category);
            checkMccsFree(newMccsToAdd);
            category.getMccs().addAll(newMccsToAdd);
            categoryService.updateCategory(category);
            System.out.println("Added MCC codes to category " + categoryName);
        } catch (MccCastException | MccExistException e) {
            System.out.println(e.getMessage());
            return;
        }
    }   


    @CommandLine.Command(name = "delete_category", description = "Удалить категорию трат")
    void deleteCategory(@CommandLine.Parameters(description = "Название категории") String categoryName){
        if(!categoryService.isCategoryNameExists(categoryName)){
            System.out.println("Category with name " + categoryName + " does not exists");
            return;
        }
        Category category = categoryService.getCategoryByName(categoryName);
        categoryService.deleteCategory(category.getId());
        System.out.println("Category with name " + categoryName + " was successfully removed");
    }


    @CommandLine.Command(name = "add_category", description = "Добавить категорию трат")
    void addCategory(
        @CommandLine.Parameters(description = "Название категории") String categoryName,
        @CommandLine.Parameters(description = "MCC коды", arity = "1..*") int[] mccCodes){
        if(categoryService.isCategoryNameExists(categoryName)){ 
            System.out.println("Category name already exists.");
            return;
        }
        Category category = new Category();
        category.setName(categoryName);
        List<Mcc> mccs = new ArrayList<>();
        try{
            mccs = castIntArrToMcc(mccCodes, category);

        }catch(MccCastException e){
            System.out.println(e.getMessage());
            return;
        }
        try{
            checkMccsFree(mccs);
        }catch(MccExistException e){
            System.out.println(e.getMessage());
            return;
        }
        try{
            category.setMccs(mccs);
            categoryService.saveCategory(category);
        }catch(Exception e){
            System.out.println("Unable to save category. Restart app and check db connection.");
        }
        System.out.println("Category " + categoryName + " was saved successfully with mccs:" + Arrays.toString(mccCodes));

    }

    public boolean checkMccsFree(List<Mcc> mccs) throws MccExistException{
        for(Mcc mcc : mccs){
            if(mccService.isMccCodeExists(mcc.getCode())){
                Mcc exisingMcc = mccService.getMccByCode(mcc.getCode());
                throw new MccExistException("Mcc code " + mcc.getCode() + " already reserved for category " + exisingMcc.getCategory().getName());
            }
        }
        return true;
    }

    public static List<Mcc> castIntArrToMcc(int[] mccCodes, Category category) throws MccCastException{
        List<Mcc> mccs = new ArrayList<>();
        for(int mccCode : mccCodes){
            if(mccCode >= 1000 && mccCode <= 10000){
                Mcc mcc = new Mcc();
                mcc.setCode(String.valueOf(mccCode));
                mcc.setCode(String.valueOf(mccCode));
                mcc.setCategory(category);
                mccs.add(mcc);
            }
            else{
                throw new MccCastException("Mcc " + mccCode + " must be a four-digit number");
            }
        }
        return mccs;
    }

    private void printCategoryTree(Category category, int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        sb.append("- ").append(category);
        System.out.println(sb.toString());
        for (Category child : category.getChildren()) {
            printCategoryTree(child, depth + 1);
        }
    }
    
    private void printCategoryTreeWithTotalSpent(Category category, Month month, int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        double totalSpent = transactionService.getTotalSpentInCategoryForMonth(category, month);
        sb.append("- ").append(category).append(" (Total spent: ").append(totalSpent).append(")");
        System.out.println(sb.toString());
        // Recursively print children
        for (Category child : category.getChildren()) {
            printCategoryTreeWithTotalSpent(child, month, depth + 1);
        }
    }
}
