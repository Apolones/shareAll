package ru.fisenko.shareAll.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisenko.shareAll.models.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {
     Url findFirstByUrlIsNotNull();
}
