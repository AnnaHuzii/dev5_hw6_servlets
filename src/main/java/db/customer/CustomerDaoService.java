package db.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoService {
    public static  List<Customer> customers = new ArrayList<>();

    private final PreparedStatement getAllNamesSt;
    private final PreparedStatement getProjectsNamesSt;
    private final PreparedStatement selectMaxIdSt;
    private final PreparedStatement  addCustomerSt;
    private final PreparedStatement existsByIdSt;


    public CustomerDaoService(Connection connection) throws SQLException {
        PreparedStatement getAllInfoSt = connection.prepareStatement(
                "SELECT * FROM customers"
        );
        try (ResultSet rs = getAllInfoSt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getLong("id"));
                if (rs.getString("name") != null) {
                    customer.setCustomerName(rs.getString("name"));
                }
                customer.setEDRPOU(rs.getInt("EDRPOU"));
                customer.setProduct(rs.getString("product"));
                customers.add(customer);
            }
        }

        getAllNamesSt = connection.prepareStatement(
                " SELECT id, name, EDRPOU, product FROM customers"
        );
        getProjectsNamesSt = connection.prepareStatement(
                " SELECT project.name FROM customers " +
                        "JOIN project ON customers.id = project.id " +
                        " WHERE  customers.name  LIKE  ?"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(customers.id) AS maxId FROM customers"
        );

        addCustomerSt = connection.prepareStatement(
                "INSERT INTO customers  VALUES ( ?, ?, ?, ?)");

        existsByIdSt = connection.prepareStatement(
                "SELECT count(*) > 0 AS customerExists FROM customers WHERE id = ?"
        );
    }

    public void getAllNames() throws SQLException {
        System.out.println("Список всех  заказчиков :");
        try (ResultSet rs = getAllNamesSt.executeQuery()) {
            while (rs.next()) {
                long customerID = rs.getLong("id");
                String customerName = rs.getString("name");
                int customerEDRPOU = rs.getInt("EDRPOU");
                String customerProduct = rs.getString("product");
                System.out.println("\t" + customerID + ". "
                        + customerName
                        + "; EDRPOU - " + customerEDRPOU
                        + "; Продукт - " + customerProduct + ";");
                System.out.println("\t\tЯвляєтся замовником наступних проектів: ");
                getProjectsNamesSt.setString(1, "%" + customerName + "%");
                try (ResultSet rs1 = getProjectsNamesSt.executeQuery()) {
                    while (rs1.next()) {
                        System.out.println("\t\t" + rs1.getString("project.name"));
                    }
                }
            }
        }
    }

    public void addCustomer() throws SQLException {
        long newCustomerId;
        try(ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            newCustomerId = rs.getLong("maxId");
        }
        newCustomerId++;
        String newCustomerName = "BI-company, Ukraine";
        System.out.println("\tНазвана замовника, якого потрібно добавити: " + newCustomerName);
        int newCustomerEDRPOU = 1599512364;
        System.out.println("\tEDRPOU замовника: " + newCustomerEDRPOU);
        String newCustomerProduct = "ERP Construction management";
        System.out.println("\tПродукт замовника: " + newCustomerProduct);
        addCustomerSt.setLong(1, newCustomerId);
        addCustomerSt.setString(2, newCustomerName);
        addCustomerSt.setInt(3, newCustomerEDRPOU);
        addCustomerSt.setString(4, newCustomerProduct);

        Customer  customer = new Customer();

        customer.setCustomerId(newCustomerId);
        customer.setCustomerName(newCustomerName);
        customer.setEDRPOU(newCustomerEDRPOU);
        customer.setProduct(newCustomerProduct);

        addCustomerSt.executeUpdate();

        if (existsCustomer(newCustomerId)) {
            System.out.println("\tЗаказчик успешно добавлен");
        }
        else System.out.println("Что-то пошло не так и заказчик не был  добавлен в базу данных");
    }

    public boolean existsCustomer(long id) throws SQLException {
        existsByIdSt.setLong(1, id);
        try (ResultSet rs = existsByIdSt.executeQuery()) {
            rs.next();
            return rs.getBoolean("customerExists");
        }
    }
}
