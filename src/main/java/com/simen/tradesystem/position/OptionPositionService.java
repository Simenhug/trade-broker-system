package com.simen.tradesystem.position;

import com.simen.tradesystem.securities.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionPositionService {
    @Autowired
    private OptionPositionRepository positionRepository;

    public void buy(int amount, OptionPosition position) {
        int size = position.getQuantity() + amount;
        position.setQuantity(size);
        positionRepository.save(position);
    }

    public void sell(int amount, OptionPosition position) {
        int size = position.getQuantity() - amount;
        position.setQuantity(size);
        positionRepository.save(position);
    }

    public void save(OptionPosition position) {
        positionRepository.save(position);
    }

    public void delete(OptionPosition position) {
        positionRepository.delete(position);
    }
}
