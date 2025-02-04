package coworking.model;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class UserEntity {
    /**
     * Entity, that represents a table "Users" in database.
     * Has reference to "Reservations" table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "USER";

    public UserEntity() {}

    public UserEntity(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String newPassword){
        this.password = newPassword;
    }

    public String getRole(){
        return this.role;
    }

    public void setRole(String newRole){
        this.role = newRole;
    }

    @Override
    public String toString() {
        return String.format("A user with id: %d, name: %s, role: %s", getId(), getName(), getRole());
    }
}
