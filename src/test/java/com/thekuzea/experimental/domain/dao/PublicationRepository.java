package com.thekuzea.experimental.domain.dao;

import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.domain.model.User;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = Publication.class, idClass = UUID.class)
public interface PublicationRepository {

    List<Publication> findAllByPublishedBy(User publishedBy);

    Optional<Publication> findByTopic(String topic);
}
