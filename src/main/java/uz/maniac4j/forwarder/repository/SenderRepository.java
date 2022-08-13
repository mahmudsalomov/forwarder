package uz.maniac4j.forwarder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.maniac4j.forwarder.model.Sender;

public interface SenderRepository extends JpaRepository<Sender,Long> {

    boolean existsSenderByIdAndBanTrue(Long id);
}
