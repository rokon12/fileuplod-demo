package ca.bazlur.fileuploddemo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    private byte[] data;

    // constructors
    public Document() {}

    public Document(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    // getters and setters
}
