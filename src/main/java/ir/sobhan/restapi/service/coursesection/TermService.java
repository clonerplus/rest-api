package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.controller.exception.ApiRequestException;
import ir.sobhan.restapi.dao.TermRepository;
import ir.sobhan.restapi.model.entity.coursesection.Term;
import ir.sobhan.restapi.model.input.coursesection.TermRequest;
import ir.sobhan.restapi.model.output.ListResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TermService {
    private final TermRepository termRepository;
    @Autowired
    public TermService(TermRepository termRepository) {
        this.termRepository = termRepository;
    }
    public void buildTerm(@NotNull TermRequest termRequest) {
        termRepository.findByTitle(termRequest.getTitle())
                .ifPresent(existingCourse -> {
                    throw new ApiRequestException("Term already exists!\n" +
                            "Please consider updating the term for your use", HttpStatus.BAD_REQUEST);
                });
        var term = Term.builder()
                .title(termRequest.getTitle())
                .open(termRequest.isOpen())
                .courseSection(termRequest.getCourseSection())
                .build();

        termRepository.save(term);
    }
    public Term getTermByTitle(String title) {
        if (title == null) {
            throw new ApiRequestException("Invalid term title", HttpStatus.BAD_REQUEST);
        }
        return termRepository.findByTitle(title)
                .orElseThrow(() -> new ApiRequestException("term not found!", HttpStatus.NOT_FOUND));
    }
    public Optional<Term> getTermById(long id) {
        return termRepository.findById(id);
    }
    public ListResponse<Term> getAllTerms() {
        return ListResponse.<Term>builder()
                .responseList(termRepository.findAll())
                .build();
    }
    public void deleteTerm(@NotNull String title) {
        termRepository.deleteTermByTitle(title);
    }
}
