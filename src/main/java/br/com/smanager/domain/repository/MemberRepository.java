package br.com.smanager.domain.repository;

import br.com.smanager.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmailAndDeleted(String email, Boolean deleted);

    Optional<Member> findByIdAndDeleted(String id, Boolean deleted);
}
