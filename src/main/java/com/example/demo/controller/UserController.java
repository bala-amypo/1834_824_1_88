// package com.example.demo.controller;

// import com.example.demo.dto.RegisterRequest;
// import com.example.demo.model.User;
// import com.example.demo.service.UserService;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/users")
// public class UserController {

//     private final UserService service;

//     public UserController(UserService service) {
//         this.service = service;
//     }

//     @PostMapping
//     public User register(@RequestBody RegisterRequest req) {

//         User u = new User();
//         u.setName(req.getName());
//         u.setEmail(req.getEmail());
//         u.setPassword(req.getPassword());
//         u.setRole(req.getRole());

//         return service.register(u);
//     }
// }

package com.example.demo.controller;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.ApartmentUnit;
import com.example.demo.model.User;
import com.example.demo.service.ApartmentUnitService;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ApartmentUnitService apartmentUnitService;

    public UserController(UserService userService,
                          ApartmentUnitService apartmentUnitService) {
        this.userService = userService;
        this.apartmentUnitService = apartmentUnitService;
    }

    @PostMapping
    public User register(@RequestBody RegisterRequest req) {

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());

        User savedUser = userService.register(user);

        if (req.getApartmentUnit() != null) {
            ApartmentUnit unit = new ApartmentUnit();
            unit.setUnitNumber(req.getApartmentUnit());
            apartmentUnitService.assignUnitToUser(savedUser.getId(), unit);
        }

        return savedUser;
    }
}
