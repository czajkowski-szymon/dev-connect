package pl.czajkowski.devconnect.technology;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnologyRepository extends JpaRepository<Technology, Integer> {
    Optional<Technology> findByTechnologyName(String name);
}
