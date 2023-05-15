package dev.bestzige;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bank {
    private String name;

    public Bank() {
    }

    public Bank(String name) {
        this.name = name;
    }

    public void listAccounts() {
        Connection connection = SQLConnection.connect();
        Statement statement;
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM accounts";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " +
                        resultSet.getString(3));
            }
        } catch (Exception e) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void openAccount(Account account) {
        Connection connection = SQLConnection.connect();
        String query = "INSERT INTO accounts (id,name,balance) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, account.getNumber());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setDouble(3, account.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void closeAccount(int number) {
        Connection connection = SQLConnection.connect();
        String query = "DELETE FROM accounts WHERE id = ?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, number);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void depositMoney(int number, double amount) {
        Account account = getAccount(number);
        account.deposit(amount);
        Connection connection = SQLConnection.connect();
        String query = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setInt(2, account.getNumber());
            System.out.println("Account balance: " + account.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void withdrawMoney(int number, double amount) {
        Account account = getAccount(number);
        account.withdraw(amount);
        Connection connection = SQLConnection.connect();
        String query = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setInt(2, account.getNumber());
            System.out.println("Account balance: " + account.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Account getAccount(int number) {
        Connection connection = SQLConnection.connect();
        String query = "SELECT * FROM accounts WHERE id = ?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, number);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Account(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3));
            }
        } catch (SQLException e) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
