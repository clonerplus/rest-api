package ir.sobhan.restapi.dao;

import java.util.List;
import java.util.Optional;

import ir.sobhan.restapi.auth.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
      select t from Token t inner join CustomUser u\s
      on t.customUser.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(long id);

    Optional<Token> findByToken(String token);
}
