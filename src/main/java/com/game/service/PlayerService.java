package com.game.service;

import com.game.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlayerService {
    void addPlayer(Player player);
    void updatePlayer(Long id, Player player);
    void removePlayer(Long id);
    Optional<Player> getPlayerById(Long id);
    List<Player> getPlayersList(Map<String, String> allRequestParams);
    List<Player> getSortedAndOrderedPlayersList(Map<String, String> allRequestParams);
}
