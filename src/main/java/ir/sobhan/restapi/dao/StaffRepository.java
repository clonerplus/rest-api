package ir.sobhan.restapi.dao;

import ir.sobhan.restapi.model.entity.individual.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}
