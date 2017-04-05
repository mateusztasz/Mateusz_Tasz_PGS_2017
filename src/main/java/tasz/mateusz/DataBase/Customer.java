package tasz.mateusz.DataBase;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Object-Relational-Mapping for CUSTOMER table
 */
@DatabaseTable(tableName = "CUSTOMER")
public class Customer {
    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private String password;

    
    public Customer() {
// ORMLite needs a no-arg constructor
    }
    public Customer(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}