package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.entity.coursesection.CourseSectionRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseSectionRegistrationRepository extends JpaRepository<CourseSectionRegistration, Long> {
}
