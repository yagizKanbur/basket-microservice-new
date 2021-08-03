package com.ty.basketmicroservice.repository;

import com.ty.basketmicroservice.model.Basket;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends CouchbaseRepository<Basket, String> {

}
