package com.example.springmvc.example01;

import com.example.springmvc.example01.entity.Address;
import com.example.springmvc.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/example01/")
public class exampleController01 {

    @GetMapping("index")
    public ResultVO getIndex(){
        return ResultVO.success(Map.of("name","SUN"));
    }

    @GetMapping("addresses")
    public ResultVO getAddress(){
        return ResultVO.success(Map.of("address",ADDRESSES));
    }
    @GetMapping("inject")
    public void inject(HttpServletRequest request,
                       HttpServletResponse response,
                       @RequestHeader HttpHeaders headers){
        log.debug(request.getRequestURI());
        log.debug(String.valueOf(headers));
    }
    @GetMapping("addresses/{aid}")
    public ResultVO getAddress(@PathVariable("aid") int aid){
        Address address = ADDRESSES.stream()
                .filter(a -> a.getId() == aid)
                .findFirst()
                .orElse(new Address());
        return ResultVO.success(Map.of("address",address));
    }
    @PostMapping("addresses")
    public ResultVO postAddress(@RequestBody Address address){
        log.debug(address.getDetail());
        log.debug(address.getComment());
        return ResultVO.success(Map.of());
    }
    @PostMapping("addresses02")
    public ResultVO postAddress2(@RequestBody Address address){
        log.debug(address.getDetail());
        log.debug(address.getComment());
        log.debug("{}",address.getUser().getId());
        return ResultVO.success(Map.of("address",address));
    }

    private final List<Address> ADDRESSES = create();

    private List<Address> create() {
        Address a1 = new Address(1, "956", "a", LocalDateTime.now());
        Address a2 = new Address(2, "925", "b", LocalDateTime.now());
        Address a3 = new Address(3, "121", null, null);
        return List.of(a1, a2, a3);
    }
}
