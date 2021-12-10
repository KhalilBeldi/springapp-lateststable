package integration.DTO;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
public class CatgoryDto {


    long id;
    String name;

    Timestamp updateddate;

    Timestamp creationdate;

}
