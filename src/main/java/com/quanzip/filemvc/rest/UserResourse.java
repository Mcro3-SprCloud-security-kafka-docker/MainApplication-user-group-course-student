package com.quanzip.filemvc.rest;

import com.quanzip.filemvc.service.UserService;
import com.quanzip.filemvc.service.dto.ResponseDTO;
import com.quanzip.filemvc.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user1")
public class UserResourse {
    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @GetMapping(value = "/form/add")
    public String showForm(){
        return "user/add";
    }

//    When using form to add user with file as an property, we have to use @RequestParam or @ModelAttribute
//    @RequestBody only work with json without file
//    When uploading file, we have to use method: POST when we edit/update that have file, only POST work
    @PostMapping(value = "/add")
    public ResponseDTO<UserDTO> addUser(@ModelAttribute UserDTO userDTO) throws Exception {
        UserDTO result = userService.addUser(userDTO);
        return new ResponseDTO<UserDTO>(200, "", result);
    }

    @GetMapping(value = "/search")
    public ResponseDTO<UserDTO> searchAndViewListUsers(@RequestParam(value = "search", required = false) String name, ModelAndView modelAndView){
        List<UserDTO> userDTOS = userService.searchByName(name).stream().peek(dto ->
        {
            String folder = environment.getProperty("application.avatar-folder");
            File avatar = new File(folder+ "/" + dto.getAvatar());
            dto.setFullAvatar(avatar.getAbsolutePath());

        }).collect(Collectors.toList());

        modelAndView.addObject("users", userDTOS);
        modelAndView.setViewName("user/search");

        ResponseDTO<UserDTO> responseDTO = new ResponseDTO(200, "",userDTOS);
        System.out.println(responseDTO);
        return responseDTO;
    }

    @GetMapping(value = "download-avatar")
    public void downloadUserAvatar(@RequestParam(value = "avatarName") String name, HttpServletResponse httpServletResponse) throws Exception {
        userService.downloadUserAvatar(name, httpServletResponse);
    }

    @GetMapping(value = "/delete")
    public String deleteUser(@RequestParam(value = "id") Long id) throws Exception {
        userService.deleteUserById(id);
        return "redirect:/user/search";
    }

    @GetMapping(value = "/edit")
    public ModelAndView editUser(@RequestParam(value = "id") Long id, ModelAndView modelAndView) throws Exception {
        UserDTO userDTO = userService.editUserById(id);
        modelAndView.addObject("user", userDTO);
        modelAndView.setViewName("/user/edit");
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public String updateUser(@RequestBody UserDTO userDTO) throws Exception {
        userService.updateUser(userDTO);
        return "redirect:/user/search";
    }


    @GetMapping(value = "form-edit-role/{userId}")
    public ModelAndView showFormToAddRole(@PathVariable(value = "userId") Long userId,
                                          ModelAndView modelAndView) throws Exception {
        UserDTO userDTO = userService.findUserById(userId);
        modelAndView.addObject("user", userDTO);
        modelAndView.setViewName("/user/edit_role");
        return modelAndView;
    }

    @PostMapping(value = "/edit-role")
    public String addRole(@RequestBody UserDTO userDTO) throws Exception {
        userService.editRole(userDTO);
        return "redirect:/user/search";
    }

}
