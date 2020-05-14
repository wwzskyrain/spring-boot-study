package erik.study.spring.boot.test;

import org.springframework.stereotype.Service;

/**
 * @author erik.wang
 * @date 2020-02-27 20:33
 * @description
 */
@Service
public class BusinessService {

    private DataService dataService;

    public BusinessService(DataService dataService) {
        super();
        this.dataService = dataService;
    }

    public int findTheGreatestFromAllData() {
        int[] data = dataService.retrieveAllData();
        int greatest = Integer.MIN_VALUE;

        for (int value : data) {
            if (value > greatest) {
                greatest = value;
            }
        }
        return greatest;
    }

}
