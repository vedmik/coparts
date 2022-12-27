package software.sigma.bu003.internship.coparts.repository;

import com.mongodb.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import software.sigma.bu003.internship.coparts.entity.Part;

@Repository
public interface PartRepository extends MongoRepository<Part, String> {

    @NonNull
    Page<Part> findAll(@NonNull Pageable pageable);
}
