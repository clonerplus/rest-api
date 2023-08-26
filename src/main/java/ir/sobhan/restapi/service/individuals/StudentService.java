package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.dao.CourseSectionRegistrationRepository;
import ir.sobhan.restapi.dao.CourseSectionRepository;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.StudentRepository;
import ir.sobhan.restapi.model.coursesection.CourseSectionRegistration;
import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.model.individual.Student;
import ir.sobhan.restapi.request.CourseSectionRequest;
import ir.sobhan.restapi.response.GetStudentScoreResponse;
import ir.sobhan.restapi.response.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final CustomUserRepository customUserRepository;
    private final StudentRepository studentRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    @Autowired
    public StudentService(CustomUserRepository customUserRepository,
                             StudentRepository studentRepository,
                             CourseSectionRepository courseSectionRepository,
                             CourseSectionRegistrationRepository courseSectionRegistrationRepository) {
        this.customUserRepository = customUserRepository;
        this.studentRepository = studentRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
    }

    public ListResponse<Student> getAllStudents() {

        return ListResponse.<Student>builder()
                .responseList(studentRepository.findAll())
                .build();
    }

    public String authorizeStudent(String username, Student student) {

        if (username == null)
            return "Invalid username!";

        Optional<CustomUser> customUser = customUserRepository.findByUsername(username);

        if (customUser.isEmpty())
            return "username not found!";

        student.setCustomUser(customUser.get());
        customUser.get().setStudent(student);
        customUser.get().setRole(Role.STUDENT);
        studentRepository.save(student);

        return "Authorized user to student limits successfully";
    }

    public String joinCourseSection(
            CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        var courseSection = courseSectionRepository.findByTermTitleAndCourseTitle(
                courseSectionRequest.getTermTitle(),
                courseSectionRequest.getCourseTitle());

        if (courseSection.isEmpty())
            return "Invalid term title or course title!";


        var student = studentRepository.findByCustomUserUsername(authentication.getName());

        CourseSectionRegistration courseSectionRegistration =
                CourseSectionRegistration.builder()
                        .courseSection(courseSection.get())
                        .student(student.get())
                        .build();

        var courseSectionRegistrationList = student.get().getCourseSectionRegistration();
        courseSectionRegistrationList.add(courseSectionRegistration);
        student.get().setCourseSectionRegistration(courseSectionRegistrationList);

        courseSectionRegistrationList = courseSection.get().getCourseSectionRegistration();
        courseSectionRegistrationList.add(courseSectionRegistration);
        courseSection.get().setCourseSectionRegistration(courseSectionRegistrationList);

        courseSectionRegistrationRepository.save(courseSectionRegistration);
        studentRepository.save(student.get());
        courseSectionRepository.save(courseSection.get());

        return "joined to course section successfully!";
    }

    public GetStudentScoreResponse fetchTermScores(
            long termId, Authentication authentication) {

        var student = studentRepository.findByCustomUserUsername(authentication.getName());


        Map<String, Double> courseScores = student.get().getCourseSectionRegistration().stream()
                .filter(courseSectionRegistration ->
                        courseSectionRegistration.getCourseSection().getTerm().getId() == termId)
                .collect(Collectors.toMap(
                        courseSectionRegistration ->
                                courseSectionRegistration.getCourseSection().getCourse().getTitle(),
                        CourseSectionRegistration::getScore
                ));

        double termAverageScore = courseScores.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.averagingDouble(Double::doubleValue));


        return GetStudentScoreResponse.builder()
                .scores(courseScores)
                .average(termAverageScore)
                .build();
    }

}
