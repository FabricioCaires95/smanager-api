package br.com.smanager.domain.repository;

import br.com.smanager.domain.document.ApiKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends MongoRepository<ApiKey, String> {
}
