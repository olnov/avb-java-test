@Entity
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id", nullable = false)
    private Long id;

    @Column("first_name", nullable = false, length = 50)
    private String firstName;

    @Column("last_name", nullable = false, length = 50)
    private String lastName;

    @Column("email", nullable = false, length = 255)
    private String email;

}
