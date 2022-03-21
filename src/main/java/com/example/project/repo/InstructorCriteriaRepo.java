package com.example.project.repo;

import com.example.project.converter.Converter;
import com.example.project.dto.*;
import com.example.project.entity.InstructorEntity;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class InstructorCriteriaRepo {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    private final Converter converter;

    public InstructorCriteriaRepo(EntityManager entityManager, Converter converter) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.converter = converter;
    }

    public Page<InstructorDto> findAllWithFilters(InstructorPage instructorPage,
                                              InstructorSearchCriteria instructorSearchCriteria) {
        CriteriaQuery<InstructorEntity> criteriaQuery = criteriaBuilder.createQuery(InstructorEntity.class);
        Root<InstructorEntity> instructorRoot = criteriaQuery.from(InstructorEntity.class);
        Predicate predicate = getPredicate(instructorSearchCriteria, instructorRoot);
        criteriaQuery.where(predicate);
        setOrder(instructorPage, criteriaQuery, instructorRoot);

        TypedQuery<InstructorEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(instructorPage.getPageNumber() * instructorPage.getPageSize());
        typedQuery.setMaxResults(instructorPage.getPageSize());

        Pageable pageable = getPageable(instructorPage);

        long clientsCount = getClientsCount(predicate);

        List<InstructorDto> convertedQueryResult = typedQuery.getResultList().stream()
                .map(converter::convertInstructorEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(convertedQueryResult, pageable, clientsCount);
    }

    private Predicate getPredicate(InstructorSearchCriteria instructorSearchCriteria,
                                   Root<InstructorEntity> instructorRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(instructorSearchCriteria.getFirstName())){
            predicates.add(
                    criteriaBuilder.like(instructorRoot.get("firstName"), "%" + instructorSearchCriteria.getFirstName() + "%")
            );
        }
        if (Objects.nonNull(instructorSearchCriteria.getLastName())){
            predicates.add(
                    criteriaBuilder.like(instructorRoot.get("lastName"), "%" + instructorSearchCriteria.getLastName() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(InstructorPage instructorPage,
                          CriteriaQuery<InstructorEntity> criteriaQuery,
                          Root<InstructorEntity> instructorRoot) {
        if (instructorPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(instructorRoot.get(instructorPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(instructorRoot.get(instructorPage.getSortBy())));
        }
    }

    private Pageable getPageable(InstructorPage instructorPage) {
        Sort sort = Sort.by(instructorPage.getSortDirection(), instructorPage.getSortBy());
        return PageRequest.of(instructorPage.getPageNumber(), instructorPage.getPageSize(), sort);
    }

    private long getClientsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<InstructorEntity> countRoot = countQuery.from(InstructorEntity.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
