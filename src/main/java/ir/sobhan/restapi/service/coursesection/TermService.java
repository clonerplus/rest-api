package ir.sobhan.restapi.service.coursesection;

import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.dao.TermRepository;
import ir.sobhan.restapi.model.coursesection.Term;
import ir.sobhan.restapi.request.coursesection.TermRequest;
import ir.sobhan.restapi.response.ListResponse;
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
    public Optional<Term> getTermByTitle(@NotNull String title) {
        return termRepository.findByTitle(title);
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
