package com.game.controller;

import com.game.entity.PlayerDTO;
import com.game.entity.Player;
import com.game.service.PlayerService;
import com.game.service.PlayerDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest")
public class PlayerController {
    private final PlayerService playerService;
    private final PlayerDTOValidator playerDTOValidator;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerDTOValidator playerDTOValidator) {
        this.playerService = playerService;
        this.playerDTOValidator = playerDTOValidator;
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayersList(@RequestParam Map<String, String> allRequestParams) {
        List<Player> playerList = playerService.getSortedAndOrderedPlayersList(allRequestParams);
        return new ResponseEntity<>(playerList, HttpStatus.OK);
    }

    @GetMapping("/players/count")
    public ResponseEntity<Integer> getPlayersCount(@RequestParam Map<String, String> allRequestParams) {
        List<Player> playerList = playerService.getPlayersList(allRequestParams);
        return new ResponseEntity<>(playerList.size(), HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Player> optionalPlayer = playerService.getPlayerById(id);
        return optionalPlayer.map(player -> new ResponseEntity<>(player, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/players")
    public ResponseEntity<Player> addPlayer(@RequestBody PlayerDTO playerDTO, BindingResult bindingResult) {
        try {
            playerDTOValidator.validate(playerDTO, bindingResult);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player = convertToPlayer(playerDTO);
        playerService.addPlayer(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<HttpStatus> removePlayer(@PathVariable("id") @Validated String sid) {
        try {
            double id = Double.parseDouble(sid);
            if (!(id % 1 == 0) || id <= 0){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.getPlayerById(Long.parseLong(sid)).isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        playerService.removePlayer(Long.parseLong(sid));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable(value = "id") Long id,
                                               @RequestBody PlayerDTO playerDTO, BindingResult bindingResult){
        if (id <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.getPlayerById(id).isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Player playerForUpdating = playerService.getPlayerById(id).get();
        PlayerDTO playerDTOForUpdating = convertToPlayerDTO(playerForUpdating);

        updatePlayerDTO(playerDTOForUpdating, playerDTO);

        playerDTOValidator.validate(playerDTOForUpdating, bindingResult);

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player resultPlayer = convertToPlayer(playerDTOForUpdating);
        resultPlayer.setId(id);

        System.out.println(playerForUpdating);
        System.out.println(resultPlayer);

        if (playerForUpdating.equals(resultPlayer)){
            return new ResponseEntity<>(playerForUpdating, HttpStatus.OK);
        }

        playerForUpdating = convertToPlayer(playerDTOForUpdating);

        playerService.updatePlayer(id, playerForUpdating);
        return new ResponseEntity<>(playerForUpdating, HttpStatus.OK);
    }
    private Player convertToPlayer(PlayerDTO playerDTO){
        Player player = new Player();

        player.setName(playerDTO.getName());
        player.setTitle(playerDTO.getTitle());
        player.setRace(playerDTO.getRace());
        player.setProfession(playerDTO.getProfession());
        player.setExperience(playerDTO.getExperience());
        player.calculateLevel();
        player.calculateUntilNextLevel();
        player.setBirthday(playerDTO.getBirthday());
        player.setBanned(playerDTO.getBanned());

        return player;
    }
    private PlayerDTO convertToPlayerDTO(Player player){
        PlayerDTO playerDTO = new PlayerDTO();

        playerDTO.setName(player.getName());
        playerDTO.setTitle(player.getTitle());
        playerDTO.setRace(player.getRace());
        playerDTO.setProfession(player.getProfession());
        playerDTO.setExperience(player.getExperience());
        playerDTO.setBirthday(player.getBirthday());
        playerDTO.setBanned(playerDTO.getBanned());

        return playerDTO;
    }

    private void updatePlayerDTO(PlayerDTO fromDB, PlayerDTO updateData){
        if (updateData.getName() != null){
            fromDB.setName(updateData.getName());
        }
        if (updateData.getTitle() != null){
            fromDB.setTitle(updateData.getTitle());
        }
        if (updateData.getRace() != null){
            fromDB.setRace(updateData.getRace());
        }
        if (updateData.getProfession() != null){
            fromDB.setProfession(updateData.getProfession());
        }
        if (updateData.getExperience() != null){
            fromDB.setExperience(updateData.getExperience());
        }
        if (updateData.getBirthday() != null){
            fromDB.setBirthday(updateData.getBirthday());
        }
        if (updateData.getBanned() != null){
            fromDB.setBanned(updateData.getBanned());
        }
    }
}
