package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public void addPlayer(Player player) {
        this.playerRepository.save(player);
    }

    @Override
    @Transactional
    public void updatePlayer(Long id, Player player) {
        player.setId(id);
        this.playerRepository.save(player);
    }

    @Override
    @Transactional
    public void removePlayer(Long id) {
        this.playerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Player> getPlayersList(Map<String, String> allRequestParams) {
        if (allRequestParams.isEmpty()) {
            return playerRepository.findAll();
        }
        Specification<Player> specification = PlayerSpecification.getSpecification(allRequestParams);
        return playerRepository.findAll(specification);
    }

    @Transactional
    public List<Player> getSortedAndOrderedPlayersList(Map<String, String> allRequestParams) {
        PlayerOrder order = PlayerOrder.ID;
        int pageNumber = 0;
        int pageSize = 3;

        if (allRequestParams.containsKey("order")) {
            order = PlayerOrder.valueOf(allRequestParams.get("order"));
            allRequestParams.remove("order");
        }

        if (allRequestParams.containsKey("pageNumber")) {
            pageNumber = Integer.parseInt(allRequestParams.get("pageNumber"));
            allRequestParams.remove("pageNumber");
        }

        if (allRequestParams.containsKey("pageSize")) {
            pageSize = Integer.parseInt(allRequestParams.get("pageSize"));
            allRequestParams.remove("pageSize");
        }

        if (allRequestParams.isEmpty())
            return playerRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))).getContent();

        Specification<Player> specification = PlayerSpecification.getSpecification(allRequestParams);
        return playerRepository.findAll(specification, PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))).getContent();
    }
}
