package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.response.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService {
    private final CustomUserRepository customUserRepository;
    @Autowired
    public CustomUserService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }
    public ListResponse<CustomUser> getAllCustomUsers() {
        return ListResponse.<CustomUser>builder()
                .responseList(customUserRepository.findAll())
                .build();
    }
}
