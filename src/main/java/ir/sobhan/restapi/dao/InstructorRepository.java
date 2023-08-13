package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.individual.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Integer> {

}
