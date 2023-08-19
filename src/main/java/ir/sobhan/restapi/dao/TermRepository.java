package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.coursesection.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Integer> {
    Optional<Term> findByTitle(String title);
    void deleteTermByTitle(String title);
}
