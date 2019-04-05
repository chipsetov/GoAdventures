package io.softserve.goadventures.repositories;

import io.softserve.goadventures.models.Category;
import io.softserve.goadventures.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Integer> {
    Event findByTopic(String topic);

    Event findById(int id);

    Long countByTopic(String topic);

    Page<Event> findByCategoryId(int eventId, Pageable pageable);

    Page<Event> findAllByTopic(Pageable pageable, String topic);

    Page<Event> findAllByCategory(Pageable pageable, Category category);
}
