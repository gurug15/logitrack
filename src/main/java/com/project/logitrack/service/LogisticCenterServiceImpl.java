package com.project.logitrack.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.LogisticCenter;
import com.project.logitrack.Mappers.LogisticCenterMapper;
import com.project.logitrack.dto.LogisticCenterDto;
import com.project.logitrack.repositories.LogisticCenterRepository;

@Service
public class LogisticCenterServiceImpl implements LogisticCenterService {

	@Autowired
	LogisticCenterRepository logisticCenterRepository;

	@Override
	public List<LogisticCenterDto> getallCenters() {
		// TODO Auto-generated method stub
		return logisticCenterRepository.findAll().stream().map(LogisticCenterMapper::toDto).toList();
	}
	
	
	@Override
    public LogisticCenterDto createCenter(LogisticCenterDto centerDto) {
        LogisticCenter center = LogisticCenterMapper.toEntity(centerDto);
        LogisticCenter savedCenter = logisticCenterRepository.save(center);
        return LogisticCenterMapper.toDto(savedCenter);
    }
	
	
	//new added methods from here for route mapping 
	
	private static final Map<Integer, Integer> PARENT_MAP;

    static {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 0); map.put(2, 0); map.put(3, 1);
        map.put(4, 1); map.put(5, 2); map.put(6, 2);
        map.put(7, 3); map.put(8, 3); map.put(9, 4);
        map.put(10, 4); map.put(11, 5); map.put(12, 5);
        map.put(13, 6); map.put(14, 6);
        PARENT_MAP = Collections.unmodifiableMap(map);
    }
	
    @Override
    public int getCenterFromPincode(String pincode) {
    	if (pincode == null || pincode.length() != 6 || !pincode.startsWith("411")) {
            // In a real app, you would throw a custom exception here
            // throw new InvalidPincodeException("Invalid pincode: " + pincode);
            return -1;
        }

        int d4 = Character.getNumericValue(pincode.charAt(3));
        int d5 = Character.getNumericValue(pincode.charAt(4));
        int d6 = Character.getNumericValue(pincode.charAt(5));

        if (d4 <= 4) {
            if (d5 <= 4) { return (d6 <= 4) ? 7 : 8; } 
            else { return (d6 <= 4) ? 9 : 10; }
        } else {
            if (d5 <= 4) { return (d6 <= 4) ? 11 : 12; }
            else { return (d6 <= 4) ? 13 : 14; }
        }
    }
    
    @Override
    public List<Integer> findRoute(int source, int destination) {
    	List<Integer> pathFromSource = tracePathToRoot(source);
        List<Integer> pathFromDest = tracePathToRoot(destination);
        
        
        Collections.reverse(pathFromSource); 
        Collections.reverse(pathFromDest);  

        int i = 0;
        while (i < pathFromSource.size() && i < pathFromDest.size() && 
               pathFromSource.get(i).equals(pathFromDest.get(i))) {
            i++;
        }

        List<Integer> finalRoute = new ArrayList<>();

        for (int j = pathFromSource.size() - 1; j >= i - 1; j--) {
            finalRoute.add(pathFromSource.get(j));
        }
        for (int j = i; j < pathFromDest.size(); j++) {
            finalRoute.add(pathFromDest.get(j));
        }

        return finalRoute;
    }
    
 // This is a private helper method, so it does NOT go in the interface.
    private List<Integer> tracePathToRoot(int startNode) {
    	List<Integer> path = new ArrayList<>();
        Integer currentNode = startNode;
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = PARENT_MAP.get(currentNode);
        }
        return path;
    }
}
