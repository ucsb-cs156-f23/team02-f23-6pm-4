package edu.ucsb.cs156.example.repositories;

import edu.ucsb.cs156.example.entities.Articles;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesRepository extends CrudRepository<Articles, Long> {

}
