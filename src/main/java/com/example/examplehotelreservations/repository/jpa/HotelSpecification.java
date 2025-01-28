package com.example.examplehotelreservations.repository.jpa;

import com.example.examplehotelreservations.web.filter.HotelFilter;
import com.example.examplehotelreservations.web.model.hotel.Hotel;
import org.springframework.data.jpa.domain.Specification;

public interface HotelSpecification {

    static Specification<Hotel> withFilter(HotelFilter filter){
        return Specification.where(byId(filter.getId()))
                .and(byName(filter.getName()))
                .and(byHeadline(filter.getHeadline()))
                .and(byCity(filter.getCity()))
                .and(byAddress(filter.getAddress()))
                .and(byCenterDistance(filter.getCenterDistance()))
                .and(byRating(filter.getRating()))
                .and(byReviewCount(filter.getReviewCount()));
    }

    static Specification<Hotel> byId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    static Specification<Hotel> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    static Specification<Hotel> byHeadline(String headline) {
        return (root, query, criteriaBuilder) -> {
            if (headline == null || headline.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("headline"), "%" + headline + "%");
        };
    }

    static Specification<Hotel> byCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("city"), "%" + city + "%");
        };
    }

    static Specification<Hotel> byAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (address == null || address.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("address"), "%" + address + "%");
        };
    }

    static Specification<Hotel> byCenterDistance(Double centerDistance) {
        return (root, query, criteriaBuilder) -> {
            if (centerDistance == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("centerDistance"), centerDistance);
        };
    }

    static Specification<Hotel> byRating(Double rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("rating"), rating);
        };
    }

    static Specification<Hotel> byReviewCount(Integer reviewCount) {
        return (root, query, criteriaBuilder) -> {
            if (reviewCount == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("reviewCount"), reviewCount);
        };
    }
}
