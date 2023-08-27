package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.dao.CourseSectionRegistrationRepository;
import ir.sobhan.restapi.dao.CourseSectionRepository;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.StudentRepository;
import ir.sobhan.restapi.model.coursesection.CourseSection;
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

    private void saveStudentRecordToDatabase(
            Student student, CustomUser customUser) {

        student.setCustomUser(customUser);
        customUser.setStudent(student);
        customUser.setRole(Role.STUDENT);
        studentRepository.save(student);
    }

    public String authorizeStudent(String username, Student student) {

        if (username == null)
            return "Invalid username!";

        return customUserRepository.findByUsername(username)
                .map(customUser -> {
                    saveStudentRecordToDatabase(student, customUser);
                    return "Authorized user to student limits successfully";
                })
                .orElse("username not found!");
    }

    private Optional<CourseSection> findCourseSection(CourseSectionRequest courseSectionRequest) {

        return courseSectionRepository.findByTermTitleAndCourseTitle(
                courseSectionRequest.getTermTitle(),
                courseSectionRequest.getCourseTitle());
    }

    private CourseSectionRegistration buildCourseSectionRegistration(
            Student student, CourseSection courseSection) {

        return CourseSectionRegistration.builder()
                .courseSection(courseSection)
                .student(student)
                .build();
    }

    private List<CourseSectionRegistration> setStudentCourseSectionRegistrationList(
            Student student, CourseSectionRegistration courseSectionRegistration) {

        student.getCourseSectionRegistration().add(courseSectionRegistration);
        return student.getCourseSectionRegistration();
    }

    private List<CourseSectionRegistration> setCourseSectionCourseSectionRegistrationList(
            CourseSection courseSection, CourseSectionRegistration courseSectionRegistration) {

        courseSection.getCourseSectionRegistration().add(courseSectionRegistration);
        return courseSection.getCourseSectionRegistration();
    }

    private void saveCourseSectionRecordsToDatabase(CourseSection courseSection,
            CourseSectionRegistration courseSectionRegistration, Student student) {

        courseSectionRegistrationRepository.save(courseSectionRegistration);
        studentRepository.save(student);
        courseSectionRepository.save(courseSection);
    }

    private CourseSection fetchCourseSection(
            CourseSectionRequest courseSectionRequest) throws Exception {

        return findCourseSection(courseSectionRequest)
                .orElseThrow(() -> new Exception("Invalid term title or course title!"));
    }

    private Student fetchStudent(String studentName) throws Exception {

        if (studentName == null)
            throw new NullPointerException("Invalid student name");

        return studentRepository.findByCustomUserUsername(studentName)
                .orElseThrow(() -> new Exception("Student not found!"));
    }

    public String joinCourseSection(
            CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        try {

            var courseSection = fetchCourseSection(courseSectionRequest);
            var student = fetchStudent(authentication.getName());

            var courseSectionRegistration = buildCourseSectionRegistration(student, courseSection);

            var courseSectionRegistrationList = setStudentCourseSectionRegistrationList(
                    student, courseSectionRegistration
            );

            student.setCourseSectionRegistration(courseSectionRegistrationList);

            courseSectionRegistrationList = setCourseSectionCourseSectionRegistrationList(
                    courseSection, courseSectionRegistration
            );
            courseSection.setCourseSectionRegistration(courseSectionRegistrationList);

            saveCourseSectionRecordsToDatabase(courseSection, courseSectionRegistration,
                    student);

            return "joined to course section successfully!";

        } catch (Exception e) {

            return e.getMessage();
        }
    }

    private Map<String, Double> fetchCourseScores(Student student, long termId) {

        return student.getCourseSectionRegistration().stream()
                .filter(courseSectionRegistration ->
                        courseSectionRegistration.getCourseSection().getTerm().getId() == termId)
                .collect(Collectors.toMap(
                        courseSectionRegistration ->
                                courseSectionRegistration.getCourseSection().getCourse().getTitle(),
                        CourseSectionRegistration::getScore
                ));
    }

    private double calculateStudentTermAverageScore(Map<String, Double> courseScores) {

        return courseScores.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.averagingDouble(Double::doubleValue));
    }
    public GetStudentScoreResponse fetchTermScores(
            long termId, Authentication authentication) {
        try {

            var student = fetchStudent(authentication.getName());

            var courseScores = fetchCourseScores(student, termId);

            double termAverageScore = calculateStudentTermAverageScore(courseScores);

            return GetStudentScoreResponse.builder()
                    .scores(courseScores)
                    .average(termAverageScore)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
