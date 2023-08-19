package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.dao.*;
import ir.sobhan.restapi.model.coursesection.*;
import ir.sobhan.restapi.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TermService {

    private final CourseRepository courseRepository;
    private final TermRepository termRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;
    private final InstructorRepository instructor;
    private final StudentRepository studentRepository;
    private final CourseSectionRepository courseSectionRepository;

    public String buildTerm(@NotNull Term termRequest) {
        var term = Term.builder()
                .title(termRequest.getTitle())
                .open(termRequest.isOpen())
                .courseSection(termRequest.getCourseSection())
                .build();

        termRepository.save(term);

        return "Term created successfully!";
    }

    public Optional<Term> getTermByTitle(@NotNull String title) {

        return termRepository.findByTitle(title);
    }

    public ListResponse<Term> getAllTerms() {

        return ListResponse.<Term>builder()
                .responseList(termRepository.findAll())
                .build();
    }

    public String deleteTerm(@NotNull String title) {

        termRepository.deleteTermByTitle(title);

        return "successfully deleted term!";
    }

}
