package software.sigma.bu003.internship.coparts.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import software.sigma.bu003.internship.coparts.entity.Part;

@Repository
public interface PartRepository extends MongoRepository<Part, String> {

}
