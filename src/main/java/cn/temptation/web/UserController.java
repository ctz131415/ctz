package cn.temptation.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.temptation.dao.UserDao;
import cn.temptation.domain.User;

/**
 * 用户控制器
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
	final Logger logger = Logger.getLogger(UserController.class);
    @Resource
    private UserDao userDao;

    /*@RequestMapping("/view")
    public String view() {
        return "main/login";
    }

    @RequestMapping("/indexview")
    public String index() {
        return "main/index";
    }*/

    //登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(User model, HttpSession session) {
        User user = userDao.findByUsername(model.getUsername());

        if (user == null || !user.getPassword().equals(model.getPassword())) {
            return new ModelAndView("redirect:/login.jsp");
        } else {
            session.setAttribute("user", user);
            ModelAndView mav = new ModelAndView();
            mav.setViewName("index");
            logger.info(model.getUsername() + "登录成功！");
            return mav;
        }
    }
    
    //注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(User model, HttpSession session) {
        User user = userDao.findByUsername(model.getUsername().trim());
        if(null != user) {
        	logger.info(model.getUsername() + "账号已经存在！");
        	return new ModelAndView("redirect:/register.jsp");
        }else {
        	int ret = userDao.registerUser(model);
        	if(ret > 0) {
        		logger.info(model.getUsername() + "账号注册成功！");
        	}else {
        		logger.info(model.getUsername() + "账号注册失败！");
        	}
        	return null;
        }
    }
}