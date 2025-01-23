package com.example.examplehotelreservations.repository.jpa;

import com.example.examplehotelreservations.web.filter.RoomFilter;
import com.example.examplehotelreservations.web.model.Booking;
import com.example.examplehotelreservations.web.model.Room;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import java.util.Date;

public interface RoomSpecification {

    static Specification<Room> withFilter(RoomFilter filter){
        return Specification.where(byId(filter.getId()))
                .and(byName(filter.getName()))
                .and(byCostRange(filter.getMinCost(), filter.getMaxCost()))
                .and(byGuestsLimit(filter.getGuestsLimit()))
                .and(byHotelId(filter.getHotelId()))
                .and(byAvailability(filter.getCheckInDate(), filter.getCheckOutDate()));
    }

    static Specification<Room> byId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    static Specification<Room> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    static Specification<Room> byCostRange(Double minCost, Double maxCost) {
        return (root, query, criteriaBuilder) -> {
            if (minCost == null && maxCost == null) {
                return null;
            }
            if (minCost != null && maxCost != null) {
                return criteriaBuilder.between(root.get("cost"), minCost, maxCost);
            } else if (minCost != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), minCost);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("cost"), maxCost);
            }
        };
    }

    static Specification<Room> byGuestsLimit(Integer guestsLimit) {
        return (root, query, criteriaBuilder) -> {
            if (guestsLimit == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("guestsLimit"), guestsLimit);
        };
    }

    static Specification<Room> byHotelId(Long hotelId) {
        return (root, query, criteriaBuilder) -> {
            if (hotelId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("hotel").get("id"), hotelId);
        };
    }

    static Specification<Room> byAvailability(Date checkInDate, Date checkOutDate) {
        return (root, query, criteriaBuilder) -> {
            if (checkInDate == null || checkOutDate == null) {
                return null;
            }

            assert query != null;
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Room> subRoot = subquery.from(Room.class);
            Join<Room, Booking> bookings = subRoot.join("bookings");

            subquery.select(subRoot.get("id"))
                    .where(criteriaBuilder.or(
                            criteriaBuilder.and(
                                    criteriaBuilder.lessThanOrEqualTo(bookings.get("checkInDate"), checkInDate),
                                    criteriaBuilder.greaterThanOrEqualTo(bookings.get("checkOutDate"), checkInDate)
                            ),
                            criteriaBuilder.and(
                                    criteriaBuilder.lessThanOrEqualTo(bookings.get("checkInDate"), checkOutDate),
                                    criteriaBuilder.greaterThanOrEqualTo(bookings.get("checkOutDate"), checkOutDate)
                            ),
                            criteriaBuilder.and(
                                    criteriaBuilder.greaterThanOrEqualTo(bookings.get("checkInDate"), checkInDate),
                                    criteriaBuilder.lessThanOrEqualTo(bookings.get("checkOutDate"), checkOutDate)
                            )
                    ));

            return criteriaBuilder.not(root.get("id").in(subquery));
        };
    }

}
