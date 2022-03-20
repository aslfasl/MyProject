package com.example.project.repo;

import com.example.project.dto.ClientPage;
import com.example.project.dto.ClientSearchCriteria;
import com.example.project.entity.ClientEntity;
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

@Repository
public class ClientCriteriaRepo {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public ClientCriteriaRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<ClientEntity> findAllWithFilters(ClientPage clientPage,
                                                 ClientSearchCriteria clientSearchCriteria) {
        CriteriaQuery<ClientEntity> criteriaQuery = criteriaBuilder.createQuery(ClientEntity.class);
        Root<ClientEntity> clientRoot = criteriaQuery.from(ClientEntity.class);
        Predicate predicate = getPredicate(clientSearchCriteria, clientRoot);
        criteriaQuery.where(predicate);
        setOrder(clientPage, criteriaQuery, clientRoot);

        TypedQuery<ClientEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(clientPage.getPageNumber() * clientPage.getPageSize());
        typedQuery.setMaxResults(clientPage.getPageSize());

        Pageable pageable = getPageable(clientPage);

        long clientsCount = getClientsCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, clientsCount);
    }

    private Predicate getPredicate(ClientSearchCriteria clientSearchCriteria,
                                   Root<ClientEntity> clientRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(clientSearchCriteria.getFirstName())){
            predicates.add(
                    criteriaBuilder.like(clientRoot.get("firstName"), "%" + clientSearchCriteria.getFirstName() + "%")
            );
        }
        if (Objects.nonNull(clientSearchCriteria.getLastName())){
            predicates.add(
                    criteriaBuilder.like(clientRoot.get("lastName"), "%" + clientSearchCriteria.getLastName() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(ClientPage clientPage,
                          CriteriaQuery<ClientEntity> criteriaQuery,
                          Root<ClientEntity> clientRoot) {
        if (clientPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(clientRoot.get(clientPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(clientRoot.get(clientPage.getSortBy())));
        }
    }

    private Pageable getPageable(ClientPage clientPage) {
        Sort sort = Sort.by(clientPage.getSortDirection(), clientPage.getSortBy());
        return PageRequest.of(clientPage.getPageNumber(), clientPage.getPageSize(), sort);
    }

    private long getClientsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<ClientEntity> countRoot = countQuery.from(ClientEntity.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
