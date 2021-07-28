package com.ty.basketmicroservice.repository;

import com.ty.basketmicroservice.domain.Basket;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BasketRepository extends CouchbaseRepository<Basket, UUID> {

}
