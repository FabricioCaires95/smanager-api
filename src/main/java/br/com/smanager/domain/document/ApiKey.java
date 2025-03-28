package br.com.smanager.domain.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collation = "api_key")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey {

    @Id
    private String id;

    private String value;

    private Instant expirationWhen;

    @CreatedDate
    private Instant creationWhen;
}
