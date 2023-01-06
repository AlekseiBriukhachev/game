package com.game.utils;

import com.game.dto.PlayerDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

@Component
public class PlayerDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PlayerDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PlayerDTO playerDTO = (PlayerDTO) target;

        if (playerDTO.getName() == null || playerDTO.getName().equals("") || playerDTO.getTitle() == null || playerDTO.getBirthday() == null ||
                playerDTO.getExperience() == null || playerDTO.getRace() == null || playerDTO.getProfession() == null) {
            errors.reject("", "Not filled completely");
        }

        if (playerDTO.getName().length() > 12) {
            errors.reject("", "Name of player is too long: must be shorter than 12 letters");
        }

        if (playerDTO.getTitle().length() > 30) {
            errors.reject("", "The title of player is too long: must be shorter than 30 letters");
        }

        if (playerDTO.getExperience() > 10000000 || playerDTO.getExperience() < 0) {
            errors.reject("", "Experience is too big: must be between 0 and 10000000");
        }

        if (playerDTO.getBirthday().getTime() < 0) {
            errors.reject("", "Birthday is negative number: must be positive number");
        }
        if (playerDTO.getBirthday().before(new Date(946684800000L)) || playerDTO.getBirthday().after(new Date(32503680000000L))) {
            errors.reject("", "Birth year is out of range: must be between 2000 and 3000");
        }
    }
}
