package testinput.login.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import testinput.login.entity.ConfirmationTime;
import testinput.login.repository.ConfirmationTimeRepository;

@Service
public class ConfirmationService {
     @Autowired
    private ConfirmationTimeRepository confirmationTimeRepository;

    List<ConfirmationTime> getAllAvailableTimes(){
        var timeList = (List<ConfirmationTime>)confirmationTimeRepository.findAll();
        return timeList;
    } 

    ConfirmationTime get(int id) {
        return confirmationTimeRepository.findById(id).get();
    }

    void saveItems(ConfirmationTime confirmationTime) {
        ConfirmationTime timeList = new ConfirmationTime();
        confirmationTimeRepository.save(timeList);
    }
}
