package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.controller.exception.ApiRequestException;
import ir.sobhan.restapi.dao.CourseSectionRegistrationRepository;
import ir.sobhan.restapi.dao.CourseSectionRepository;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.StudentRepository;
import ir.sobhan.restapi.model.entity.coursesection.CourseSection;
import ir.sobhan.restapi.model.entity.coursesection.CourseSectionRegistration;
import ir.sobhan.restapi.model.entity.individual.CustomUser;
import ir.sobhan.restapi.model.entity.individual.Student;
import ir.sobhan.restapi.model.input.coursesection.CourseSectionRequest;
import ir.sobhan.restapi.model.output.GetStudentScoreResponse;
import ir.sobhan.restapi.model.output.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private void saveStudentRecordToDatabase(Student student, CustomUser customUser) {
        student.setCustomUser(customUser);
        customUser.setStudent(student);
        customUser.setRole(Role.STUDENT);
        studentRepository.save(student);
    }

    public void authorizeStudent(String username, Student student) {

        if (username == null)
            throw new ApiRequestException("Invalid username!", HttpStatus.NOT_FOUND);

        CustomUser customUser = customUserRepository.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException("username not found!", HttpStatus.NOT_FOUND));

        saveStudentRecordToDatabase(student, customUser);
    }

    private Optional<CourseSection> findCourseSection(CourseSectionRequest courseSectionRequest) {

        return courseSectionRepository.findByTermTitleAndCourseTitle(
                courseSectionRequest.getTermTitle(),
                courseSectionRequest.getCourseTitle());
    }

    private CourseSectionRegistration buildCourseSectionRegistration(Student student,
                                                                     CourseSection courseSection) {
        return CourseSectionRegistration.builder()
                .courseSection(courseSection)
                .student(student)
                .build();
    }

    private List<CourseSectionRegistration> setStudentCourseSectionRegistrationList(Student student,
                                            CourseSectionRegistration courseSectionRegistration) {
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

    private CourseSection fetchCourseSection(CourseSectionRequest courseSectionRequest) {
        return findCourseSection(courseSectionRequest)
                .orElseThrow(() -> new ApiRequestException("Invalid term title or course title!",
                        HttpStatus.NOT_FOUND));
    }

    private Student fetchStudent(String studentName) {
        if (studentName == null)
            throw new ApiRequestException("Invalid student name", HttpStatus.BAD_REQUEST);

        return studentRepository.findByCustomUserUsername(studentName)
                .orElseThrow(() -> new ApiRequestException("Student not found!", HttpStatus.NOT_FOUND));
    }

    public void joinCourseSection(CourseSectionRequest courseSectionRequest, Authentication authentication) {
        var courseSection = fetchCourseSection(courseSectionRequest);
        var student = fetchStudent(authentication.getName());

        var courseSectionRegistration = buildCourseSectionRegistration(student, courseSection);

        var courseSectionRegistrationList = setStudentCourseSectionRegistrationList(student,
                courseSectionRegistration);

        student.setCourseSectionRegistration(courseSectionRegistrationList);

        courseSectionRegistrationList = setCourseSectionCourseSectionRegistrationList(courseSection,
                courseSectionRegistration);
        courseSection.setCourseSectionRegistration(courseSectionRegistrationList);

        saveCourseSectionRecordsToDatabase(courseSection, courseSectionRegistration, student);
    }

    private Map<String, Double> fetchCourseScores(Student student, long termId) {
        return student.getCourseSectionRegistration().stream()
                .filter(courseSectionRegistration ->
                        courseSectionRegistration.getCourseSection().getTerm().getId() == termId)
                .collect(Collectors.toMap(courseSectionRegistration ->
                                courseSectionRegistration.getCourseSection().getCourse().getTitle(),
                        CourseSectionRegistration::getScore));
    }

    private double calculateStudentTermAverageScore(Map<String, Double> courseScores) {
        return courseScores.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.averagingDouble(Double::doubleValue));
    }

    public GetStudentScoreResponse fetchTermScores(long termId, Authentication authentication) {
        var student = fetchStudent(authentication.getName());
        var courseScores = fetchCourseScores(student, termId);
        double termAverageScore = calculateStudentTermAverageScore(courseScores);

        return GetStudentScoreResponse.builder()
                .scores(courseScores)
                .average(termAverageScore)
                .build();
    }
}
