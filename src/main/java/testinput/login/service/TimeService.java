package testinput.login.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import testinput.login.entity.Time;
import testinput.login.repository.TimeRepository;

@Service
public class TimeService {
    @Autowired
    private TimeRepository repository; 

    public List<Time> getAllInventory(){
        var list = (List<Time>)repository.findAll();
        return list;
    }

    public Time get(String day_of_week) {
        return repository.findById(day_of_week).get();
    }

    public void addItems() {
        Time times = new Time();
        repository.save(times);
    }
}
