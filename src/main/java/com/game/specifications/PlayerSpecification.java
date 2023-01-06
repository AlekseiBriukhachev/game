package com.game.specifications;

import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.model.Player;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PlayerSpecification {
    public static Specification<Player> getSpecification(Map<String, String> requestParams) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (requestParams.containsKey("name")) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + requestParams.get("name") + "%"));
            }
            if (requestParams.containsKey("title")) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + requestParams.get("title") + "%"));
            }
            if (requestParams.containsKey("race")) {
                predicates.add(criteriaBuilder.equal(root.get("race"), Race.valueOf(requestParams.get("race"))));
            }
            if (requestParams.containsKey("profession")) {
                predicates.add(criteriaBuilder.equal(root.get("profession"), Profession.valueOf(requestParams.get("profession"))));
            }
            if (requestParams.containsKey("after")) {
                predicates.add(criteriaBuilder.greaterThan(root.get("birthday"), new Date(Long.parseLong(requestParams.get("after")))));
            }
            if (requestParams.containsKey("before")) {
                predicates.add(criteriaBuilder.lessThan(root.get("birthday"), new Date(Long.parseLong(requestParams.get("before")))));
            }
            if (requestParams.containsKey("minExperience")) {
                predicates.add(criteriaBuilder.greaterThan(root.get("experience"), Integer.parseInt(requestParams.get("minExperience"))));
            }

            if (requestParams.containsKey("maxExperience")) {
                predicates.add(criteriaBuilder.lessThan(root.get("experience"), Integer.parseInt(requestParams.get("maxExperience"))));
            }

            if (requestParams.containsKey("minLevel")) {
                predicates.add(criteriaBuilder.greaterThan(root.get("level"), Integer.parseInt(requestParams.get("minLevel"))));
            }

            if (requestParams.containsKey("maxLevel")) {
                predicates.add(criteriaBuilder.lessThan(root.get("level"), Integer.parseInt(requestParams.get("maxLevel"))));
            }
            if (requestParams.containsKey("banned")) {
                predicates.add(criteriaBuilder.equal(root.get("banned"), Boolean.valueOf(requestParams.get("banned"))));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        });
    }
}
