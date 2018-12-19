package org.superbiz.moviefun.moviesapi;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class MoviesClient {

    private String moviesUrl;
    private RestOperations restOperations;

    private static ParameterizedTypeReference<List<MovieInfo>> movieListType = new ParameterizedTypeReference<List<MovieInfo>>() {
    };

    public MoviesClient(String moviesUrl, RestOperations restOperations) {
        this.moviesUrl = moviesUrl;
        this.restOperations = restOperations;
    }

    public void addMovie(MovieInfo movie) {
        restOperations.postForEntity(moviesUrl, movie, MovieInfo.class);
    }

    public void deleteMovieId(Long movieId) {
        restOperations.delete(moviesUrl + "/" + movieId);
    }

    public int countAll() {
        return restOperations.getForObject(moviesUrl + "/count", Integer.class);
    }


    public int count(String field, String key) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl + "/count")
                .queryParam("field", field)
                .queryParam("key", key);

        return restOperations.getForObject(builder.toUriString(), Integer.class);
    }


    public List<MovieInfo> findAll(int start, int pageSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl)
                .queryParam("start", start)
                .queryParam("pageSize", pageSize);

        return restOperations.exchange(builder.toUriString(), GET, null, movieListType).getBody();
    }

    public List<MovieInfo> findRange(String field, String key, int start, int pageSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(moviesUrl)
                .queryParam("field", field)
                .queryParam("key", key)
                .queryParam("start", start)
                .queryParam("pageSize", pageSize);

        return restOperations.exchange(builder.toUriString(), GET, null, movieListType).getBody();
    }

    public List<MovieInfo> getMovies() {
        return restOperations.exchange(moviesUrl, GET, null, movieListType).getBody();
    }
}
/*package org.superbiz.moviefun.moviesapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestOperations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.List;


public class MoviesClient {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String moviesUrl;
    private  RestOperations restOperations;

    @PersistenceContext
    private EntityManager entityManager;

    public MovieInfo find(Long id) {
        return entityManager.find(MovieInfo.class, id);
    }


    public MoviesClient(String moviesUrl, RestOperations restOperations){
        this.moviesUrl=moviesUrl;
        this.restOperations= restOperations;
    }


    @Transactional
    public void addMovie(MovieInfo movie) {
        logger.debug("Creating movie with title {}, and year {}", movie.getTitle(), movie.getYear());

        entityManager.persist(movie);
    }

    @Transactional
    public void updateMovie(MovieInfo movie) {
        entityManager.merge(movie);
    }

    @Transactional
    public void deleteMovie(MovieInfo movie) {
        entityManager.remove(movie);
    }

    @Transactional
    public void deleteMovieId(long id) {
        MovieInfo movie = entityManager.find(MovieInfo.class, id);
        deleteMovie(movie);
    }

    public List<MovieInfo> getMovies() {
        CriteriaQuery<MovieInfo> cq = entityManager.getCriteriaBuilder().createQuery(MovieInfo.class);
        cq.select(cq.from(MovieInfo.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<MovieInfo> findAll(int firstResult, int maxResults) {
        CriteriaQuery<MovieInfo> cq = entityManager.getCriteriaBuilder().createQuery(MovieInfo.class);
        cq.select(cq.from(MovieInfo.class));
        TypedQuery<MovieInfo> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public int countAll() {
        CriteriaQuery<Long> cq = entityManager.getCriteriaBuilder().createQuery(Long.class);
        Root<MovieInfo> rt = cq.from(MovieInfo.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        TypedQuery<Long> q = entityManager.createQuery(cq);
        return (q.getSingleResult()).intValue();
    }

    public int count(String field, String searchTerm) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<MovieInfo> root = cq.from(MovieInfo.class);
        EntityType<MovieInfo> type = entityManager.getMetamodel().entity(MovieInfo.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.select(qb.count(root));
        cq.where(condition);

        return entityManager.createQuery(cq).getSingleResult().intValue();
    }

    public List<MovieInfo> findRange(String field, String searchTerm, int firstResult, int maxResults) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MovieInfo> cq = qb.createQuery(MovieInfo.class);
        Root<MovieInfo> root = cq.from(MovieInfo.class);
        EntityType<MovieInfo> type = entityManager.getMetamodel().entity(MovieInfo.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.where(condition);
        TypedQuery<MovieInfo> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public void clean() {
        entityManager.createQuery("delete from Movie").executeUpdate();
    }
}
*/